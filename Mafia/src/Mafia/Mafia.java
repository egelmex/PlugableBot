package Mafia;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import jmathlib.toolbox.jmathlib.system.foreach;

import com.PluggableBot.plugin.DefaultPlugin;

public class Mafia extends DefaultPlugin {
	private static final String COMMAND_START = "!mafiastart";
	private static final String COMMAND_JOIN = "!mafiajoin";
	private static final String COMMAND_HIT = "!mafiahit";
	private static final String COMMAND_KILL = "!mafiakill";

	private Timer timer = new Timer();
	
	private List<String> players;
	private List<String> mafia;
	private String channel;

	@Override
	public void load() {
		bot.addCommand(COMMAND_START, this);
		bot.addCommand(COMMAND_JOIN, this);
	}

	@Override
	public void onCommand(String command, String channel, String sender,
			String login, String hostname, String message) {
		if (command.equals(COMMAND_START)) {
			if (players == null) {
				channel = channel;
				players = new ArrayList<String>();
				players.add(sender);
				bot.sendMessage(channel, sender + " started a game of Mafia!");
				bot.sendMessage(channel, "Joing the game with " + COMMAND_JOIN);
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						chooseMafia();
					}
				}, 1000);
			} else {
				bot.sendMessage(channel, sender
						+ ": Sorry a game is already in progress.");
			}
		} else if (command.equals(COMMAND_JOIN)) {
			if (mafia == null) {
				players.add(sender);
				bot.sendMessage(channel, sender + " want to be in the mafia");
			}
		}
	}

	public void chooseMafia() {
		Random rng = new Random();
		int numberOfMafia = (players.size() / 10) + 1;
		mafia = new ArrayList<String>();
		while (mafia.size() < numberOfMafia) {
			String nextMafia =players.get(rng.nextInt(players.size()));
			if (!mafia.contains(nextMafia)) {
				mafia.add(nextMafia);
			}
			
		}
		
		bot.sendMessage(channel, "The mafia is loose, people are going to die");
		
		for(String player : players) {
			if (mafia.contains(player)) {
				bot.sendMessage(player, "You are a mafia boss!");
			} else {
				bot.sendMessage(player, "You are a city slicker");
			}
		}
	}
	
	public void doHit() {
		String hitTarget = "";
		bot.sendMessage(channel, "During the night there was a mafia hit and " + hitTarget + " died.");
	}
	
	@Override
	public String getHelp() {
		return null;
	}


	
}
