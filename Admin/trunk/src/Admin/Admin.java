/*	
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

package Admin;

import AndrewCassidy.PluggableBot.DefaultPlugin;

/**
 * 
 * @author Mex (ellism88@gmail.com) 2010
 *
 */
public class Admin extends DefaultPlugin {

	@Override
	public String getHelp() {
		return "Like I would tell you!";
	}

	@Override
	public void onAdminMessage(String sender, String login, String hostname,
			String message) {
		if (message.startsWith("unload")) {
			bot.unloadPlugin(message.substring(7));
			bot.Message(sender, "unloaded");
		} else if (message.startsWith("reload")) {
			bot.unloadPlugin(message.substring(7));
			bot.loadPlugin(message.substring(7));
			bot.Message(sender, "reloaded");
		} else if (message.startsWith("join")) {
			bot.joinChannel(message.substring(5));
			bot.Message(sender, "joined");
		} else if (message.startsWith("part")) {
			bot.partChannel(message.substring(5));
			bot.Message(sender, "left");
		} else if (message.startsWith("load")) {
			bot.loadPlugin(message.substring(5));
		}

	}

}
