package Factoids;

import java.util.Date;
import java.util.logging.Logger;

import com.PluggableBot.plugin.DefaultPlugin;
import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

public class Factoids extends DefaultPlugin {

	private static final Logger log = Logger.getLogger(Factoids.class.toString());
	private final ObjectContainer database;

	private static final String COMMAND_ADD = "!set";
	private static final String COMMAND_SET = "!setreply";

	public Factoids() {
		database = Db4o.openFile("Facts.db4o");
	}

	@Override
	public void load() {
		bot.addCommand(COMMAND_ADD, this);
		bot.addCommand(COMMAND_SET, this);
	}

	@Override
	public void onMessage(String channel, String sender, String login, String hostname,
			String message) {

		if (message.startsWith("?") && message.split(" ").length == 1) {
			if (message.equals("?")) {
				// don't do anything
			} else {
				Fact proto = new Fact(message.toLowerCase().substring(1), null, null, null, true);
				ObjectSet<Fact> facts = database.get(proto);
				if (facts.size() > 0) {
					bot.Message(channel, sender + ": " + facts.get(0));
				} else {
					bot.Message(channel, sender + ": Sorry I do not know about that");
				}
			}
		}
	}

	private void proccessAdd(String message, String sender, String channel) {
		String[] messageSplit = message.split(" ");
		if (messageSplit.length < 2) {
			bot.sendMessage(channel, sender + ": not enough pareters set");
		} else {

			String factString = messageSplit[0].toLowerCase();
			String factMessage;

			if (messageSplit[1].trim().equals("is")) {
				factMessage = message.trim();
			} else {
				StringBuilder sb = new StringBuilder(message.trim());
				sb.insert(messageSplit[0].length(), " is ");
				factMessage = sb.toString();
			}

			String factSetBy = sender;
			Date factDate = new Date();
			Fact fact = new Fact(factString, factMessage, factSetBy, factDate, true);

			Fact proto = new Fact(factString, null, null, null, true);
			ObjectSet<Fact> set = database.get(proto);
			for (Fact old : set) {
				old.setOld();
			}

			database.set(fact);
			database.commit();
			bot.sendMessage(channel, sender + ": Stored, thanks.");
		}
	}

	private void proccessSet(String message, String channel, String sender) {
		String[] messageSplit = message.split(" ");
		String factString = messageSplit[0].toLowerCase();
		String factMessage = message.substring(messageSplit[0].length()).trim();

		String factSetBy = sender;
		Date factDate = new Date();
		Fact fact = new Fact(factString, factMessage, factSetBy, factDate, true);

		Fact proto = new Fact(factString, null, null, null, true);
		ObjectSet<Fact> set = database.get(proto);
		for (Fact old : set) {
			old.setOld();
			database.set(old);
		}

		database.set(fact);
		database.commit();
		bot.sendMessage(channel, sender + ": Stored, thanks.");

	}

	@Override
	public void onCommand(String command, String channel, String sender, String login,
			String hostname, String message) {
		if (command.equals(COMMAND_ADD)) {
			proccessAdd(message, sender, channel);
		} else if (command.equals(COMMAND_SET)) {
			proccessSet(message, channel, sender);
		}
	}

	@Override
	public String getHelp() {
		return "Use " + COMMAND_ADD + "<fact> is <message> to set a factoid. Use " + COMMAND_SET
				+ "<fact> <message> to set a literal reply to a factoid.";
	}

	@Override
	public void unload() {
		database.close();
	}

}
