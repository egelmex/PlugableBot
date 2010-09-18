/*
 * Eval.java
 *
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
