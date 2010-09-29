/*
 * Copyright 2007 A.Cassidy (andrew.cassidy@bytz.co.uk)
 * Copyright 2010 Mex (me92@kent.ac.uk)
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
package Chooose;

import java.util.Random;

import com.PluggableBot.plugin.DefaultPlugin;

/**
 * Choose plugin. Allows the user to make a random choice between a list of
 * options. !choose yes or no for example
 * 
 * @author A.Cassidy (andrew.cassidy@bytz.co.uk) 31 October 2007
 * @author Mex 2010
 */
public class Choose extends DefaultPlugin {

	private static Random r = new Random();
	private static final String ACTION = "!choose";

	public Choose() {
		bot.addCommand(ACTION, this);
	}

	@Override
	public void onCommand(String command, String channel, String sender,
			String login, String hostname, String message) {
		if (message.equals(ACTION)) {
			try {
				String[] opts = message.split(" or ");
				bot.Message(channel, sender + ": "
						+ opts[r.nextInt(opts.length)]);
			} catch (Exception e) {

			}
		}
	}

	@Override
	public String getHelp() {
		return "This plugin will allow me to make a simple decision between a list of items. Use !choose <item 1> or <item 2>...";
	}

}
