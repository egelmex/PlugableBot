/*	
 * Copyright 2007 Andee
 * Copywrite 2010 Mex
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
package Pounce;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;

import com.PluggableBot.plugin.DefaultPlugin;




/**
 * 
 * @author AndyC & Mex
 */
public class Pounce extends DefaultPlugin {

	private ArrayList<String> pounces = new ArrayList<String>();
	private Random r = new Random();
	private int probability = 4;

	public Pounce() {
		try {
			FileReader fr = new FileReader("Pounce");
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				pounces.add(line);
			}
		} catch (Exception e) {
			throw new IllegalStateException("Unable to load pounce file.");
		}
	}

	public void onJoin(String channel, String sender, String login,
			String hostname) {
		if (sender.equals(bot.Nick()))
			return;

		if (r.nextInt(probability) == 1) {
			bot.Message(channel, pounces.get(r.nextInt(pounces.size()))
					.replaceAll("%NAME", sender));
		}
	}

	public void onKick(String channel, String kickerNick, String kickerLogin,
			String kickerHostname, String recipientNick, String reason) {
		bot.Message(channel, "haha " + recipientNick + " was a dick anyway.");
	}

	public void onMessage(String channel, String sender, String login,
			String hostname, String message) {

		String tMessage = message.trim();
		if (message.startsWith("!addpounce")) {

			String s = message.substring(11);
			if (pounces.contains(s)) {
				bot.Message(channel, sender
						+ ": Stop feeding me the same junk!");
			} else {
				pounces.add(s);
				boolean ok = saveActions();
				if (ok) {
					bot.Message(channel, sender + ": I will remeber that!");
				} else {
					bot.Message(channel, sender
							+ ": Whoops I am being forgetful today!");
					pounces.remove(s);
				}
			}
		} else if (message.startsWith("ls ") || message.startsWith("cd ")
				|| message.startsWith("cp ") || message.equals("ls")
				|| message.equals("cd")) {
			bot.Message(channel, sender + ": Wrong window?");
		} else if (message.trim().toLowerCase().equals("ping")) {
			bot.Message(channel, sender + ": PONG!");
		} else if (message.trim().toLowerCase().startsWith("win ")) {
			bot.Message(channel, sender + ": Fail!");
		} else if (message.startsWith("rm ") || tMessage.equals("rm")) {
			bot.Message(channel, sender + ": delete porn [y/n]");
		} else if (tMessage.equals("ps")) {
			bot.Message(channel, "PID      TTY     TIME        CMD");
			bot.Message(channel, "18101    pts/5   00:00:00    bash");
			bot.Message(channel,
					"18187    pts/5   00:00:00    'firefox.com hotbabes.com'");
			bot.Message(channel, "18200    pts/5   00:00:00    ps");

		} else if (message.toLowerCase().startsWith("identify ")) {
			bot.Message(channel, "Hey everyone! " + sender + "'s password is "
					+ message.substring(9).trim());

		}
	}

	private boolean saveActions() {
		boolean ok = true;
		try {
			FileWriter fw = new FileWriter("Pounce");
			BufferedWriter bw = new BufferedWriter(fw);
			for (String si : pounces) {
				bw.write(si + "\n");
			}
			bw.flush();
			bw.close();
			fw.close();
		} catch (Exception e) {
			ok = false;
		}
		return ok;
	}

	public String getHelp() {
		return "This plugin makes me 'pounce' on new users that enter the channel, with a probablity of 1 in "
				+ probability
				+ ". to add a new pounce, use !addpounce followed by the message, using %NAME as a placeholder.";
	}

	@Override
	public void onAdminMessage(String sender, String login, String hostname,
			String message) {
		if (message.startsWith("!pounce setProb")) {

			String s = message.substring(15);
			try {
				probability = Integer.parseInt(s);
				bot.Message(sender, "prob set to " + probability);

			} catch (Exception e) {
			}
		} else if (message.toLowerCase().trim().equals("!help Actions")) {

			bot.Message(sender, "Actions Admin Help: !pounce setProb <prob>");
		}

	}
}
