/*	
 * Copyright 2007 Andee (andee@bytz.co.uk)
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
package Mailinfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.PluggableBot.plugin.DefaultPlugin;

/**
 * 
 * @author A.Cassidy (a.cassidy@bytz.co.uk)
 * @author Mex 2010
 */
public class Mailinfo extends DefaultPlugin {

	private static final String COMMAND = "/usr/local/bin/mailinfo";
	private static final String COMMAND_MAILINFO = "!mailinfo";

	public Mailinfo() {
		bot.addCommand(COMMAND_MAILINFO, this);

	}

	@Override
	public void onCommand(String command, String channel, String sender,
			String login, String hostname, String message) {
		if (command.equals(COMMAND_MAILINFO)) {
			String user = login;
			String tmp = message.trim();
			if (tmp.length() > 0)
				user = tmp;
			try {
				ProcessBuilder pb = new ProcessBuilder(COMMAND, user);
				Process exec = pb.start();

				BufferedReader br = new BufferedReader(new InputStreamReader(
						exec.getInputStream()));
				// headers
				br.readLine();
				// extra
				bot.Message(channel, sender + ": " + br.readLine());
				exec.waitFor();
				br.close();
			} catch (Exception e) {

			}
		}

	}

	public String getHelp() {
		return "Type !mailinfo <login> to get email info for the specified login";
	}

}
