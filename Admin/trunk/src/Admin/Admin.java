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

import java.io.File;

import com.PluggableBot.plugin.DefaultPlugin;

/**
 * 
 * @author Mex (ellism88@gmail.com) 2010
 *
 */
public class Admin extends DefaultPlugin {
	
	public static final String ACTION_STRING = "!";
	public static final String ACTION_UNLOAD = "unload";
	public static final String ACTION_LOAD = "load";
	public static final String ACTION_RELOAD = "reload";
	public static final String ACTION_PART = "part";
	public static final String ACTION_JOIN = "join";
	public static final String ACTION_LIST = "list";
	
	@Override
	public String getHelp() {
		return "Like I would tell you!";
	}
	

	/* (non-Javadoc)
	 * @see com.PluggableBot.plugin.DefaultPlugin#getAdminHelp()
	 */
	@Override
	public String getAdminHelp() {
		return ("Admin Commands are: " + ACTION_STRING + ACTION_UNLOAD  + ", " + ACTION_STRING + ACTION_LOAD  + ", " + ACTION_STRING + ACTION_RELOAD  + ", " + ACTION_STRING + ACTION_JOIN  + ", " + ACTION_STRING + ACTION_PART );
	}



	@Override
	public void onAdminMessage(String sender, String login, String hostname,
			String message) {
		if (message.toLowerCase().startsWith(ACTION_STRING + ACTION_UNLOAD)) {
			bot.unloadPlugin(message.substring(7));
			bot.Message(sender, "unloaded " + message.substring(7));
		} 
		else if (message.toLowerCase().startsWith(ACTION_STRING + ACTION_RELOAD)) {
			bot.unloadPlugin(message.substring(7));
			bot.loadPlugin(message.substring(7));
			bot.Message(sender, "reloaded " + message.substring(7));
		} 
		else if (message.toLowerCase().startsWith(ACTION_STRING + ACTION_JOIN)) {
			bot.joinChannel(message.substring(5));
			bot.Message(sender, "joined " + message.substring(5));
		} 
		else if (message.toLowerCase().startsWith(ACTION_STRING + ACTION_PART)) {
			bot.partChannel(message.substring(5));
			bot.Message(sender, "left channel " + message.substring(5));
		} 
		else if (message.toLowerCase().startsWith(ACTION_STRING + ACTION_LOAD)) {
			bot.loadPlugin(message.substring(5));
			bot.Message(sender, "Tried to load " + message.substring(5));
		}
		else if (message.toLowerCase().startsWith("!help admin")) {
			bot.Message(sender, getAdminHelp());
		}
		else if (message.toLowerCase().startsWith(ACTION_STRING + ACTION_LIST)) {
			File pluginsFolder = new File("./plugins") ;
			for (String f : pluginsFolder.list()) {
				if (f.endsWith(".jar")) {
					bot.Message(sender, f.substring(0, f.length() - 4));
				}
			}
		}

	}
	

}
