/*	
 * Copyright 2007 andee
 * Copyright 2010 Mex (ellism88@gmail.com)
 * 
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package Hometime;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.PluggableBot.plugin.DefaultPlugin;




/**
 * Hometime plugin.., Simply tells you how long till you have to go home
 * 
 * @author Andee 11 November 2007
 * @author Mex 2010
 */
public class Hometime extends DefaultPlugin {

	private HashMap<String, String> hometimes = new HashMap<String, String>();

	/**
	 * Creates a new instance of Hometime
	 **/
	public Hometime() {
		try {
			FileReader fr = new FileReader("Hometime");
			BufferedReader br = new BufferedReader(fr);

			String line;

			while ((line = br.readLine()) != null) {
				String[] vals = line.split(":", 2);
				hometimes.put(vals[0], vals[1]);
			}

			br.close();
			fr.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void save() {
		FileWriter w;
		try {
			w = new FileWriter("Hometime");

			BufferedWriter r = new BufferedWriter(w);

			for (Map.Entry<String, String> e : hometimes.entrySet()) {
				r.write(e.getKey() + ":" + e.getValue() + "\n");
			}
			r.flush();
			r.close();

			w.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onMessage(String channel, String sender, String login,
			String hostname, String message) {
		if (message.startsWith("!hometime")) {
			String hometime;
			// look up user's hometime

			if (hometimes.containsKey(sender)) {
				hometime = hometimes.get(sender);
			} else
				hometime = "17:00";

			Calendar now = Calendar.getInstance();

			String[] p = hometime.split(":");
			int hr = Integer.parseInt(p[0].replace('-', '0'));
			int min = Integer.parseInt(p[1].replace('-', '0'));

			int chr = now.get(Calendar.HOUR_OF_DAY);
			int cmin = now.get(Calendar.MINUTE);

			// has hometime past for the day?
			if (chr > hr || (chr == hr && cmin > min)) {
				hr += 24;
			}

			/*
			 * if (hr < chr) { hr += 24; }
			 */

			if (min < cmin) {
				min += 60;
				hr--;
			}

			int hrs = hr - chr;
			int mins = min - cmin;

			String m = "Hometime is at " + hometime + ", which is ";

			if (hrs == 0 && mins == 0) {
				m += "now!";
			} else {
				if (hrs == 0) {
					m += "only ";
				} else if (hrs == 1) {
					m += "1 hour ";
				} else {
					m += hrs + " hours ";
				}

				if (hrs > 0 && mins > 0)
					m += "and ";

				if (mins == 1) {
					m += "1 minute ";
				} else if (mins > 1) {
					m += mins + " minutes ";
				}

				m += "away!";
			}

			bot.Message(channel, m);

			// System.out.println("Hometime is at " + hometime + ", which is "
			// + hrs + " hours and " + mins + " minutes away!");
		} else if (message.startsWith("!sethometime")) {
			String newtime = message.substring(13).trim();
			if (newtime.length() == 5 && newtime.charAt(2) == ':') {
				String[] p = newtime.split(":");

				try {
					int hr = Integer.parseInt(p[0].replace('-', '0'));
					int min = Integer.parseInt(p[1].replace('-', '0'));

					if (hr > 23 || hr < 0 || min < 0 || min > 59) {
						bot.Message(channel, "Invalid time : " + newtime);
					} else {

						hometimes.put(sender, newtime);
						save();
					}
				} catch (NumberFormatException e) {
					bot.Message(channel, "Generic error saving hometime : "
							+ newtime);
				}
			} else {
				bot.Message(channel, "Time must be in the format HH:MM");
			}
		}
	}

	@Override
	public String getHelp() {
		return "Call !hometime and i will tell you how long until the next home time. To set your hometime, use !sethometime HH:MM";
	}

}
