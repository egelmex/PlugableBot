/*
 * Eval.java
 *
 */

import AndrewCassidy.PluggableBot.DefaultPlugin;
import AndrewCassidy.PluggableBot.PluggableBot;
import jmathlib.core.interpreter.Interpreter;

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
		if (message.startsWith("!eval")) {
			String s = message.substring(6);
			inter.executeExpression(s);
			bot.Message(channel, sender + ": " + s + " = " + inter.getResult());
        }
	}

	public void onPart(String channel, String sender, String login,
			String hostname) {
	}

	public void onQuit(String sourceNick, String sourceLogin,
			String sourceHostname, String reason) {
	}

	public String getHelp() {
		return "Type \"!eval expression\" to evaluate a mathematical expression";
	}

	public void onPrivateMessage(String sender, String login, String hostname,
			String message) {
	}

	public void unload() {
	}
	
	@Override
	public void onAdminMessage(String sender, String login, String hostname,
			String message) {
	}
}
