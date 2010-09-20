/*	
 * Copyright 2010 Murmew 
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


import jmathlib.core.interpreter.Interpreter;

import com.PluggableBot.plugin.DefaultPlugin;

/**
 * 
 * @author Murmew
 */
public class Eval extends DefaultPlugin
{
	private Interpreter inter;
	
	public Eval()
	{
		inter = new Interpreter(true);
	}

	public void onMessage(String channel, String sender, String login,
			String hostname, String message) {
		if (message.startsWith("!eval")) {
			String s = message.substring(6);
			inter.executeExpression(s);
			bot.Message(channel, sender + ": " + s + " = " + inter.getResult());
        }
	}

	public String getHelp() {
		return "Type \"!eval expression\" to evaluate a mathematical expression";
	}

}
