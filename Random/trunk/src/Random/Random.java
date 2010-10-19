/*	
 * Copyright 2010 Murmew
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
package Random;

import com.PluggableBot.PluggableBot;
import com.PluggableBot.plugin.DefaultPlugin;

/**
 * RNG plugin for Plugable Bot
 * 
 * @author Murmew
 * @author Mex
 */
public class Random extends DefaultPlugin {
	public PluggableBot bot;

	private MersenneTwister mersenneRandom;
	private java.util.Random random;

	private static final String COMMAND = "!random";

	@Override
	public void load() {
		bot.addCommand(COMMAND, this);
	}

	public Random() {
		mersenneRandom = new MersenneTwister();
		random = new java.util.Random(new java.util.Date().getTime());
	}

	private boolean isNumber(String number) {
		try {
			Integer.valueOf(number);
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	private int getRand(int min, int max) {
		int ret = 0;

		int tries = 0;
		do {
			ret = random.nextInt(max);
			tries++;
		} while ((tries < 5000) && (ret < min)); // muh. D:

		return ret;
	}

	@Override
	public void onCommand(String command, String channel, String sender, String login,
			String hostname, String message) {
		if (command.equals(COMMAND)) {
			String[] parts = message.split(" "); // sorry mex, i dont know an
													// easier way :<

			int random = 0;
			if (parts.length == 1) {
				if (isNumber(parts[0])) {
					random = getRand(0, Integer.parseInt(parts[0]));
				}
			} else if (parts.length == 2) {
				if (isNumber(parts[0]) && isNumber(parts[1])) {
					int a = Integer.parseInt(parts[0]);
					int b = Integer.parseInt(parts[1]);
					if (a < b) {
						random = getRand(a, b);
					} else {
						bot.Message(channel, sender + ": Nu.");
						return;
					}
				}
			} else {
				random = mersenneRandom.extractNumber();
			}
			bot.Message(channel, sender + ": Your random number is: " + random);
		}

	}

	@Override
	public String getHelp() {
		return "Type \"!random optional:min optional:max\" to get a random number!";
	}
}
