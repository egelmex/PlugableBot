/*	
 * Copyright 2007 A.Cassidy (andrew.cassidy@bytz.co.uk)
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

package Dns;

/*
 * DNS.java
 *
 * Created on 19 October 2007, 17:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import java.net.InetAddress;

import com.PluggableBot.plugin.DefaultPlugin;

/**
 * DNS lookup plugin.
 * @author A.Cassidy (a.cassidy@bytz.co.uk)
 * @author Mex (ellism88@gmail.com) 2010 
 */
public class Dns extends DefaultPlugin {

	private static final String COMMAND = "!dns";

	@Override
	public void load() {
		bot.addCommand(COMMAND, this);
	}
	
	@Override
	public void onCommand(String command, String channel, String sender,
			String login, String hostname, String message) {
		if (command.equals(COMMAND)) {
			try {
				bot.Message(channel, InetAddress.getByName(
						message.trim()).getHostAddress());
			} catch (Exception e) {
				bot.Message(channel, "Sorry, I couldn't find that host");
			}
		}
	}


	public String getHelp() {
		return "This plugin lets me resolve hostnames the their ip addresses. Type !dns <hostname> to use.";
	}
}
