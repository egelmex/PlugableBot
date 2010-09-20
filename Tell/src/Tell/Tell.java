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

package Tell;

import java.text.DateFormat;
import java.util.Date;
import java.util.logging.Logger;

import com.PluggableBot.plugin.DefaultPlugin;
import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

public class Tell extends DefaultPlugin {

	private static final String command = "tell";
	private static final Logger log = Logger.getLogger(Tell.class.toString());
	private final ObjectContainer database;

	/**
	 * Default constructor.
	 */
	public Tell() {
		log.info("Tell: loading database");
		database = Db4o.openFile("Tell.db4o");
		Db4o.configure().objectClass(Message.class).cascadeOnUpdate(true);
		log.info("Tell: database open");
	}

	@Override
	public void onMessage(String channel, String sender, String login,
			String hostname, String message) {

		Message proto = new Message(null, null, sender, null, channel);

		ObjectSet<Message> set = database.get(proto);
		for (Message m : set) {
			bot.sendMessage(channel, m.target + ": " + m.message + " [sent: "
					+ DateFormat.getDateInstance().format(m.date) + ", from: " + m.sender + "]");
			database.delete(m);
		}
		database.commit();
		
		if (message.toLowerCase().startsWith("!" + command)) {

			message = message.substring(("!" + command).length() + 1);
			log.info("Tell:Saving new message " + message);
			String[] split = message.split(" ");
			if (split.length > 1) {
				String target = split[0];
				message = message.substring(split[0].length() + 1);
				Message m = new Message(sender, new Date(), target, message,
						channel);
				database.set(m);
				database.commit();
				bot.sendMessage(channel, sender + ": Thanks, I will tell "
						+ target + " that.");
			}
		}
	}

	@Override
	public void onNickChange(String oldNick, String login, String hostname,
			String newNick) {
		Message proto = new Message(null, null, oldNick, null, null);
		ObjectSet<Message> set = database.get(proto);
		for (Message m : set) {
			log.info("Tell: updating message" + m.message);
			m.target = newNick;
		}
	}

	@Override
	public void onJoin(String channel, String sender, String login,
			String hostname) {
		Message proto = new Message(null, null, sender, null, null);
		ObjectSet<Message> set = database.get(proto);
		if (!set.isEmpty()) {
			bot.Message(channel, sender, "You have " + set.size() + " message"
					+ (set.size() > 1 ? "s" : "") + " waiting.");
		}
	}

	@Override
	public String getHelp() {
		return "";
	}

	/* (non-Javadoc)
	 * @see com.PluggableBot.plugin.DefaultPlugin#unload()
	 */
	@Override
	public void unload() {
		if (database != null) {
			database.close();
		}
		
	}

	
}
