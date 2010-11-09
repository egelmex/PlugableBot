package Say;

import com.PluggableBot.plugin.DefaultPlugin;

import java.util.Random;
import java.util.ArrayList;
import java.util.logging.Logger;


public class Say extends DefaultPlugin {

	private static final String SAY_COMMAND = "!say";
	private static final String DO_COMMAND = "!do";
	private static final String BAN_ADMINCMD = "!sayban";
	private static final String UNBAN_ADMINCMD = "!sayunban";
	private static final String PERCENT_ADMINCMD = "!saysetper";
    private static final String VERSION_ADMINCMD = "!sayver";

    private int ver = 12;
	private int triggerPercent = 5;
	private static boolean silence = false;
	private static Random randomer = new Random();
	private static final Logger LOG = Logger.getLogger(Say.class.getCanonicalName());
	private ArrayList<String> banList = new ArrayList<String>();

	@Override
	public void load() {
		bot.addCommand(SAY_COMMAND, this);
		bot.addCommand(DO_COMMAND, this);
		bot.addAdminCommand(BAN_ADMINCMD, this);
		bot.addAdminCommand(UNBAN_ADMINCMD, this);
		bot.addAdminCommand(PERCENT_ADMINCMD, this);
		bot.addAdminCommand(VERSION_ADMINCMD, this);
	}
	
	/*
	 *
	 *
	 * PERSONAL FUNCTIONS
	 *
	 *
	 */
	
	public boolean checkPermissions(String sender) {
		if (banList.contains(sender)) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public boolean randomPass(int threshold) {
		
		if ((threshold > 100) || (threshold < 1)) {
			return false;
		}
		int random = randomer.nextInt(100);
		LOG.info("RANDOM: " + threshold + "%" + random);
		if (random < threshold) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void reaction(String condition, boolean isEquals, boolean isLimited, boolean isAction, String response, String match, String channel)
	{
		if ((isEquals && (condition.toLowerCase().equals(match.toLowerCase()))) || ((!isEquals) && (condition.toLowerCase().contains(match.toLowerCase())))) {
			if ((isLimited && (randomPass(triggerPercent))) || (!isLimited)) {
				if (isAction) {
					bot.Action(channel, response);
				}
				else {
					bot.Message(channel, response);
				}
			}
		}
	}
	
	public void handle(String command, String sender, String message) {
		
		if (command.equals(SAY_COMMAND) || command.equals(DO_COMMAND)) {
			try {
				String chn = message.split(" ")[0];
				String msg = message.substring(chn.length()+1);
				
				if (!chn.startsWith("#")) {
                    chn = "#" + chn;
                }
				
				if (command.equals(SAY_COMMAND)) {
					bot.Message(chn, msg);
				}
				else if (command.equals(DO_COMMAND)) {
					bot.Action(chn, msg);
				}
				
			} catch (Exception e) {
				bot.Message(sender, "Sorry, there was a problem extracting the message");
			}
		}
	}
	
	/*
	 *
	 *
	 * OVERRIDDEN FUNCTIONS
	 *
	 *
	 */
	
	@Override
	public void onPrivateAdminCommand(String command, String sender, String login, String hostname, String message) {
		message = message.trim();
		
		LOG.info(sender + ": " + command + " " + message + " (" + login + " on " + hostname + " )");
		
		if (command.equals(BAN_ADMINCMD)) {
			banList.add(message);
		}
		else if (command.equals(UNBAN_ADMINCMD)) {
			banList.remove(banList.indexOf(message));
		}
		else if (command.equals(PERCENT_ADMINCMD)) {
			try {
				triggerPercent = Integer.parseInt(message);
				if ((triggerPercent < 0) || (triggerPercent > 100)) {
					triggerPercent = 10;
					bot.sendMessage(sender, "Incorrect percent. New percentage chance: 10");
					return;
				}
				bot.sendMessage(sender, "New percentage chance: "+triggerPercent);
			}
			catch (NumberFormatException e) {
				triggerPercent = 10;
				bot.sendMessage(sender, "Failed to parse int. New percentage chance: 10");
			}
		}
		else if (command.equals(VERSION_ADMINCMD)) {
		    bot.sendMessage(sender, "Say Version: " + ver + ". Silence: " + silence + ". triggerPercent: " + triggerPercent);
		}
	}
	
	@Override
	public void onJoin(String channel, String sender, String login, String hostname) {
		if (sender.equals(bot.Nick()))
			return;

		if (silence) {
			return;
		}
		
		reaction("", true, true, false, "Oh Hai " + sender + "!", "", channel);
	}
	
	@Override
	public void onPrivateCommand(String command, String sender, String login, String hostname, String message) {
		LOG.info(sender + ": " + command + " " + message + " (" + login + " on " + hostname + " )");
		
		if (!checkPermissions(sender)) {
			bot.Message(sender, "Permission Denied!");
			return;
		}
		
		message = message.trim();
		
		handle(command, sender, message);
	}
	
	@Override
	public void onCommand(String command, String channel, String sender, String login, String hostname, String message) {
		LOG.info(sender + ": " + command + " " + message + " (" + login + " on " + hostname + " )");
		
		if (!checkPermissions(sender)) {
			bot.Message(channel, "ZOMG! " + sender + " is banned and he tried to use me!");
			return;
		}
		
		message = message.trim();

		handle(command, sender, message);
	}
	
	@Override
	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		LOG.info(sender + ": " + message + " (" + login + " on " + hostname + " )");
		
		message = message.trim();
		
		if (message.equalsIgnoreCase("shut up bot")) {
			bot.Message(channel, "OK ;_; I'll keep my reactions silent.");
			bot.Action(channel, "cries");
			silence = true;
		}
		else if (message.equalsIgnoreCase("wake up bot")) {
			silence = false;
			if (sender.equalsIgnoreCase("gairne")) {
				bot.Message(channel, "Yes Master!");
			}
			else {
				bot.Message(channel, "OK");
			}
			bot.Action(channel, "woops with joy!");
		}
		
		if (silence) {
			return;
		}
		
		//reaction(String condition, boolean equals, boolean isLimited, boolean isAction, String response, String match, String channel)
		reaction(" lol", false, true, false, "I lol'ed", message, channel);
		reaction("hehe", false, true, false, "I lol'ed", message, channel);
		reaction("haha", false, true, false, "I lol'ed", message, channel);
		reaction("gairne", false, true, true, "reacts to his master's name with a dominant bark", message, channel);
		reaction("zoo", false, true, false, "Woo! The zoo rocks!", message, channel);
		reaction("bot fail", false, true, false, "Oh shut up!", message, channel);
		reaction("fail", true, true, false, "fail much?", message, channel);
		reaction("naughty bot", false, true, true, "cries ;_;", message, channel);
		reaction("bot spam", false, true, false, "Will you be having ham, jam or lamb with that spam?", message, channel);
		reaction("troll", false, true, true, "is a naughty bot: Oi bob!", message, channel);
		reaction("boo", true, true, false, "EEEEK!", message, channel);
	}
	
	@Override
	public String getHelp() {
		return "This plugin lets you impersonate the bot: !say #channel message or !do #channel message";
	}

}
