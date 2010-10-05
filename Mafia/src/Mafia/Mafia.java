package Mafia;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.hibernate.hql.ast.tree.Case2Node;
import org.jibble.pircbot.User;

import jmathlib.toolbox.jmathlib.system.foreach;

import com.PluggableBot.plugin.DefaultPlugin;

public class Mafia extends DefaultPlugin {

	private enum Stage {
		loading, noGame, joining, waitingForBosses, mafiahit, slickermob
	}

	private static final String COMMAND_START = "!mafiastart";
	private static final String COMMAND_JOIN = "!mafiajoin";
	private static final String COMMAND_HIT = "!mafiahit";
	private static final String COMMAND_KILL = "!mafiakill";

	private static final int TIMEOUT_JOIN = 10 * 1000;
	private static final int TIMEOUT_HIT = 10 * 1000;
	private static final int TIMEOUT_INVITE = 10 * 1000;
	

	private Stage stage = Stage.loading;

	private Timer timer = new Timer();

	private List<String> players;
	private List<String> mafia;

	private Map<String, String> hitMap;
	private String gameChannel;
	private String mafiaChannel;

	@Override
	public void load() {
		bot.addCommand(COMMAND_START, this);
		bot.addCommand(COMMAND_JOIN, this);
		joinMafiaChannel();
		joinGameChannel();
		stage = Stage.noGame;

	}

	private void joinMafiaChannel() {
		mafiaChannel = "#mafiaboss";
		Random rng = new Random();
		int number = rng.nextInt(999999);
		mafiaChannel += number;

		bot.joinChannel(mafiaChannel);

		User[] users = bot.getUsers(mafiaChannel);
		if (users.length == 1 && users[0].getNick() == bot.getNick()
				&& users[0].isOp()) {
			bot.setTopic(mafiaChannel, "Mob Bosses hideout");
			bot.setMode(mafiaChannel, "is");
		} else {
			joinMafiaChannel();
		}

	}

	private void joinGameChannel() {
		mafiaChannel = "#mafia";
		Random rng = new Random();
		int number = rng.nextInt(999999);
		mafiaChannel += number;

		bot.joinChannel(mafiaChannel);

		User[] users = bot.getUsers(mafiaChannel);
		if (users.length == 1 && users[0].getNick() == bot.getNick()
				&& users[0].isOp()) {
			bot.setTopic(mafiaChannel, "Down town Italy");
			bot.setMode(mafiaChannel, "is");
		} else {
			joinGameChannel();
		}

	}

	@Override
	public void onCommand(String command, String channel, String sender,
			String login, String hostname, String message) {
		if (command.equals(COMMAND_START)) {

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
				bot.sendMessage(channel, sender
						+ ": Sorry a game is already in progress.");
			}

		} else if (command.equals(COMMAND_JOIN)) {
			switch (stage) {
			case noGame:
				bot.sendMessage(channel, sender
						+ ": There is no current game, you can start one with "
						+ COMMAND_START);
				break;
			case joining:
				players.add(sender);
				bot.sendInvite(sender, gameChannel);
				break;
			default:
				bot.sendAction(channel, sender
						+ ": You can not join a running game.");
			}

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
			}

		}

		bot.sendMessage(gameChannel,
				"The mafia is loose, people are going to die");

		for (String boss : mafia) {
			bot.sendInvite(boss, mafiaChannel);
		}


	}

	@Override
	public void onJoin(String channel, String sender, String login,
			String hostname) {
		if (channel == mafiaChannel) {
		}
	}

	public void doHit() {
		String hitTarget = "";
		bot.sendMessage(gameChannel,
				"During the night there was a mafia hit and " + hitTarget
						+ " died.");
	}

	@Override
	public String getHelp() {
		return null;
	}

}
