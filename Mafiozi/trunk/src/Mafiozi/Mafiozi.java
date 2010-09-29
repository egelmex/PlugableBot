package Mafiozi;
import java.util.ArrayList;
import java.util.Random;

import org.jibble.pircbot.Colors;

import com.PluggableBot.plugin.DefaultPlugin;




/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * 
 * A.Cassidy (a.cassidy@bytz.co.uk)
 */
@Deprecated
public class Mafiozi extends DefaultPlugin {

	private Boolean inGame = false, voted = false;
	private static Random r = new Random();
	private ArrayList<String> numberList;
	private ArrayList<String> actions;

	public void onAction(String sender, String login, String hostname,
			String target, String action) {

	}

	public void onJoin(String channel, String sender, String login,
			String hostname) {

	}

	public void onKick(String channel, String kickerNick, String kickerLogin,
			String kickerHostname, String recipientNick, String reason) {

	}

	public void onMessage(String channel, String sender, String login,
			String hostname, String message) {
		if (channel.equals("#mafiozi")) {
			if (message.startsWith("!reg") && !inGame) {
				bot.Message(channel, "!reg");
				inGame = true;
				bot.Message("mafiozi", "!ide BOB bobspassword");
			} else if (inGame && sender.equals("mafiozi")) {
				if (message.contains("The End!") || message.contains("Cast:")) {
					clear();
				} else if (message.toLowerCase().contains(bot.Nick())
						&& message.toLowerCase().contains("killed")) {
					clear();
				} else if (message.contains("!yes")) {
					if (!message.contains(bot.Nick())) {
						bot.Message(channel, "!yes");
					} else {
						bot.Message(channel, "!no");
					}
					voted = false;
				} else if (message.contains("Current Players:")) {
					processPlayersList(Colors
							.removeFormattingAndColors(message));
				} else if (message.contains("Who is accused?") && !voted) {
					if (numberList == null)
						return;
					String number = numberList
							.get(r.nextInt(numberList.size()));
					bot.Message(channel, "!" + number);
					voted = true;
				}
			}
		}
	}

	private void clear() {
		inGame = false;
		actions = null;
		voted = false;
	}

	private String processPlayersList(String message) {
		System.out.println(message);
		int location = message.indexOf(bot.Nick());
		System.out.println("location: " + location);
		int dotLocation = message.substring(0, location).lastIndexOf(".");
		System.out.println("dot location: " + dotLocation);
		int spaceLocation = message.substring(0, dotLocation).lastIndexOf(" ");
		System.out.println("space location: " + spaceLocation);

		String us = message.substring(spaceLocation, dotLocation).replaceAll(
				"[^0-9.]", "");

		String numberString = message.substring(0,
				message.indexOf("Players total")).replaceAll("[^0-9.]", "")
				.replaceAll(us, "");
		String[] a = numberString.replaceAll("[.]$", "").split("[.]");
		numberList = new ArrayList<String>();
		for (String s : a) {
			if (!s.trim().equals("")) {
				numberList.add(s);
			}
		}
		System.out.println(numberString);
		System.out.println(us);
		return numberList.get(r.nextInt(numberList.size()));
	}

	private String processActionList(String message) {
		if (actions == null) {
			actions = new ArrayList<String>();
			String[] actionsArray = message.split("[/]");
			for (String s : actionsArray) {
				if (s.startsWith("!") && s.indexOf(" ") < 0)
					actions.add(s);
				System.out.println(s);
			}
		}
		return actions.get(r.nextInt(actions.size()));
	}

	public void onPart(String channel, String sender, String login,
			String hostname) {

	}

	public void onQuit(String sourceNick, String sourceLogin,
			String sourceHostname, String reason) {

	}

	public String getHelp() {
		return "This plugin lets bob join in a game of Mafiozi. He's not very smart, though.";
	}

	public void onPrivateMessage(String sender, String login, String hostname,
			String message) {
		if (sender.equals("mafiozi") && message.contains("Current Players:")) {
			System.out.println("PM for required action");
			// get a random number
			String playerNumber = processPlayersList(Colors
					.removeFormattingAndColors(message));

			// now choose a command
			String action = processActionList(Colors
					.removeFormattingAndColors(message));

			bot.Message("mafiozi", action + " " + playerNumber);
		}

	}

	public void unload() {
	}

	@Override
	public void onAdminMessage(String sender, String login, String hostname,
			String message) {
		// TODO Auto-generated method stub

	}
}
