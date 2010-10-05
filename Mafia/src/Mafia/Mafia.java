package Mafia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import org.jibble.pircbot.User;

import com.PluggableBot.plugin.DefaultPlugin;

public class Mafia extends DefaultPlugin {

	private enum Stage {
		loading, noGame, joining, waitingForBosses, mafiahit, slickermob
	}

	private static final String COMMAND_START = "!mafiastart";
	private static final String COMMAND_JOIN = "!mafiajoin";
	private static final String COMMAND_HIT = "!mafiahit";
	private static final String COMMAND_KILL = "!mafialockup";

	private static final int TIMEOUT_JOIN = 10 * 1000;
	private static final int TIMEOUT_HIT = 10 * 1000;
	private static final int TIMEOUT_JAIL = 20 * 1000;
	private static final int TIMEOUT_INVITE = 10 * 1000;
	
	private static final Logger LOG = Logger.getLogger(Mafia.class.getCanonicalName());

	private Stage stage = Stage.loading;

	private Timer timer = new Timer();

	private List<String> players;
	private List<String> mafia;

	private Map<String, String> hitMap;
	private String gameChannel;
	private String mafiaChannel;

	@Override
	public void load() {

		try {
			bot.addCommand(COMMAND_START, this);
			bot.addCommand(COMMAND_JOIN, this);

			mafiaChannel = joinChannel("#mafiaboss", 10);
			bot.setTopic(mafiaChannel, "Mob Bosses hideout");
			gameChannel = joinChannel("#mafia", 10);
			bot.setTopic(gameChannel, "Little Italy");
			stage = Stage.noGame;
		} catch (Exception e) {
			bot.unloadPlugin("Mafia");
		}

	}

	private String joinChannel(String prefix, int maxTries) throws Exception {
		if (maxTries == 0) {
			throw new Exception("Could not find a channel");
		}
		
		String chan = prefix;
		Random rng = new Random();
		int number = rng.nextInt(999999);
		chan += number;
		bot.joinChannel(chan);
		this.wait(1000);
		User[] users = bot.getUsers(chan);
		LOG.info( Arrays.toString(users));
		if (users.length == 1 && users[0].getNick().equals(bot.getNick()) && users[0].isOp()) {
			bot.setMode(chan, "ism");
			return chan;
		} else {
			bot.partChannel(chan);
			return joinChannel(prefix, --maxTries);
		}
	}
	
	private void doStart(String channel, String sender) {

		switch (stage) {
		case loading:
			break;
		case noGame:
			this.gameChannel = channel;
			players = new ArrayList<String>();
			players.add(sender);
			bot.sendMessage(channel, sender + " started a game of Mafia!");
			bot.sendMessage(channel, "Joing the game with " + COMMAND_JOIN);
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					chooseMafia();
				}
			}, TIMEOUT_JOIN);
			break;
		default:
			bot.sendMessage(channel, sender + ": Sorry a game is already in progress.");
		}
	}

	private void doJoin(String channel, String sender) {
		switch (stage) {
		case noGame:
			bot.sendMessage(channel, sender + ": There is no current game, you can start one with "
					+ COMMAND_START);
			break;
		case joining:
			players.add(sender);
			bot.sendInvite(sender, gameChannel);
			break;
		default:
			bot.sendAction(channel, sender + ": You can not join a running game.");
		}
	}

	private void doHit(String message, String sender, String channel) {
		switch (stage) {
		case mafiahit:
			if (players.contains(message.trim())) {
				hitMap.put(sender, message);
				bot.sendMessage(channel, sender + ": OK.");
				if (hitMap.size() == mafia.size()) {
					timer.cancel();
					timer = new Timer();
					calculateHit();

				}
			} else {
				bot.sendMessage(channel, sender + ": Unknown target");
			}
			break;
		default: 
			bot.sendMessage(channel, sender
					+ ": You can only plan your hits under the cover of darkness.");
			break;
		}
	}

	private void doKill(String message, String sender, String channel) {
		switch (stage) {
		case slickermob:
			if (players.contains(message.trim())) {
				hitMap.put(sender, message);
				bot.sendMessage(channel, sender + ": OK.");
				if (hitMap.size() == players.size()) {
					timer.cancel();
					timer = new Timer();
					calculateJail();
				}
			} else {
				bot.sendMessage(channel, sender + ": Unknown target");
			}
			break;
		default:
			bot.sendMessage(channel, sender
					+ ": You can only plan your hits under the cover of darkness.");
			break;
		}
	}

	@Override
	public void onCommand(String command, String channel, String sender, String login,
			String hostname, String message) {
		if (command.equals(COMMAND_START)) {
			doStart(channel, sender);
		} else if (command.equals(COMMAND_JOIN)) {
			doJoin(channel, sender);
		} else if (command.equals(COMMAND_HIT) && channel.equals(mafiaChannel)) {
			doHit(message, sender, channel);
		} else if (command.equals(COMMAND_KILL) && channel.equals(gameChannel)) {
			doKill(message, sender, channel);
		}
	}

	public void chooseMafia() {
		Random rng = new Random();
		int numberOfMafia = (players.size() / 10) + 1;
		mafia = new ArrayList<String>();
		while (mafia.size() < numberOfMafia) {
			String nextMafia = players.get(rng.nextInt(players.size()));
			if (!mafia.contains(nextMafia)) {
				mafia.add(nextMafia);
				bot.sendInvite(mafiaChannel, nextMafia);
			}

		}

		bot.sendMessage(gameChannel, "The mafia is loose, people are going to die");

		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				checkMafia();

			}
		}, TIMEOUT_INVITE);

	}

	public void checkMafia() {
		User[] mafiaU = bot.getUsers(mafiaChannel);
		if (mafiaU.length < 2) {
			for (String player : players) {
				bot.kick(gameChannel, player, "No Mafia turned up :(");
				bot.kick(mafiaChannel, player, "Too late.");
			}
			stage = Stage.noGame;
		} else {
			stage = Stage.mafiahit;
			bot.sendMessage(mafiaChannel, "Nightime falls, its time to plan your kill.");
			String users = "";
			for (String player : players) {
				users += player + " ";
			}
			bot.sendMessage(mafiaChannel, "The following people are still alive:" + users);
			bot.sendMessage(mafiaChannel, "Choose who to kill with " + COMMAND_HIT + " <name>");
			bot.sendMessage(mafiaChannel, "You have " + TIMEOUT_HIT / 1000 + " seconds to choose");
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					calculateHit();

				}
			}, TIMEOUT_HIT);
		}
	}

	private void calculateHit() {
		bot.sendMessage(mafiaChannel, "Times up mobsters!");
		stage = Stage.slickermob;

		ArrayList<String> uniqueElements = new ArrayList<String>();
		ArrayList<Integer> counts = new ArrayList<Integer>();

		// traverse the array, collect the elements/counts
		for (String element : hitMap.values()) {
			int index = uniqueElements.indexOf(element);
			// element has been seen:
			if (index != -1) {
				// increment its count
				int newCount = counts.get(index) + 1;
				counts.set(index, newCount);
			} else {
				// add it to uniqueElements, set its count to 1
				uniqueElements.add(element);
				counts.add(1);
			}
		}

		// find maximum count, get the corresponding element
		int maxCount = 0;
		int index = -1;

		for (int i = 0; i < counts.size(); i++) {
			if (maxCount < counts.get(i)) {
				maxCount = counts.get(i);
				index = i;
			}
		}
		String target = uniqueElements.get(index);

		hitMap = new HashMap<String, String>();

		bot.sendMessage(gameChannel, "The Mob has had enough of " + target);
		bot.deVoice(gameChannel, target);
		bot.deVoice(mafiaChannel, target);

		bot.sendMessage(gameChannel, "Try and work out who the mafia are!");
		String users = "";
		for (String player : players) {
			users += player + " ";
		}
		bot.sendMessage(mafiaChannel, "The following people are still alive:" + users);
		bot.sendMessage(gameChannel, "Once you have made up your choice use " + COMMAND_KILL
				+ " <target> to lock up a mobster");
		bot.sendMessage(gameChannel, "You have  " + TIMEOUT_JAIL / 1000 + " seconds to comply");

		timer.schedule(new TimerTask() {

			@Override
			public void run() {

			}
		}, TIMEOUT_JAIL);

	}

	private void calculateJail() {
		bot.sendMessage(mafiaChannel, "Times up mobsters!");
		stage = Stage.mafiahit;

		ArrayList<String> uniqueElements = new ArrayList<String>();
		ArrayList<Integer> counts = new ArrayList<Integer>();

		// traverse the array, collect the elements/counts
		for (String element : hitMap.values()) {
			int index = uniqueElements.indexOf(element);
			// element has been seen:
			if (index != -1) {
				// increment its count
				int newCount = counts.get(index) + 1;
				counts.set(index, newCount);
			} else {
				// add it to uniqueElements, set its count to 1
				uniqueElements.add(element);
				counts.add(1);
			}
		}

		// find maximum count, get the corresponding element
		int maxCount = 0;
		int index = -1;

		for (int i = 0; i < counts.size(); i++) {
			if (maxCount < counts.get(i)) {
				maxCount = counts.get(i);
				index = i;
			}
		}
		String target = uniqueElements.get(index);

		hitMap = new HashMap<String, String>();

		bot.sendMessage(gameChannel, "The city people think " + target
				+ " is a mobster so they locked him up");

		bot.deVoice(gameChannel, target);
		bot.deVoice(mafiaChannel, target);
		players.remove(target);
		mafia.remove(target);

		bot.sendMessage(gameChannel, "Try and work out who the mafia are!");
		String users = "";
		for (String player : players) {
			users += player + " ";
		}
		bot.sendMessage(mafiaChannel, "The following people are still alive:" + users);
		bot.sendMessage(gameChannel, "Once you have made up your choice use " + COMMAND_KILL
				+ " <target> to lock up a mobster");
		bot.sendMessage(gameChannel, "You have  " + TIMEOUT_JAIL / 1000 + " seconds to comply");

		timer.schedule(new TimerTask() {

			@Override
			public void run() {

			}
		}, TIMEOUT_JAIL);

	}
	
	

	@Override
	public void onJoin(String channel, String sender, String login, String hostname) {
		if (channel.equals(mafiaChannel)) {
			if (mafia.contains(sender)) {
				bot.voice(mafiaChannel, sender);
			}
		} else if (channel.equals(gameChannel)) {
			if (players.contains(sender)) {
				bot.voice(gameChannel, sender);
			}
		}
	}

	@Override
	public void onAdminCommand(String command, String channel, String sender, String login,
			String hostname, String message) {
		bot.sendInvite(sender, gameChannel);
		bot.sendInvite(sender, mafiaChannel);
		
	}
	
	
	@Override
	public String getHelp() {
		return null;
	}

}
