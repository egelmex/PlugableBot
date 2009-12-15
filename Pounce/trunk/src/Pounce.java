/*
 * Pounce.java
 *
 * Created on 11 October 2007, 09:05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
import AndrewCassidy.PluggableBot.PluggableBot;
import AndrewCassidy.PluggableBot.Plugin;
import java.util.*;
import java.io.*;
/**
 *
 * @author AndyC
 */
public class Pounce implements Plugin {

	private ArrayList<String> pounces = new ArrayList<String>();
	private Random r = new Random();
	private int probability = 4; 

	public Pounce()
	{
		try
		{
			FileReader fr = new FileReader("Pounce");
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null)
			{
				pounces.add(line);
			}
		}
		catch (Exception e)
		{
			throw new IllegalStateException("Unable to load pounce file.");
		}
	}

	public void onAction(String sender, String login, String hostname, String target, String action) {

	}

	public void onJoin(String channel, String sender, String login, String hostname) {
		if (r.nextInt(probability) == 1)
		{
			PluggableBot.Message(channel, pounces.get(r.nextInt(pounces.size())).replaceAll("%NAME", sender));
		}
	}

	public void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason) {
		PluggableBot.Message(channel, "haha " +recipientNick + " was a dick anyway.");
	}

	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		if (message.startsWith("!pounce setProb")) {

			String s = message.substring(15);
			try {
				probability = Integer.parseInt(s);
				PluggableBot.Message(sender, "prob set to "  +probability);

			} catch (Exception e) {
			}
		}

		/*if (message.startsWith("!addpounce"))	
		  {
		  String s = message.substring(11);

		  try
		  {
		  FileWriter fw = new FileWriter("Pounce", true);
		  BufferedWriter bw = new BufferedWriter(fw);

		  bw.write(s + "\n");
		  bw.flush();
		  bw.close();
		  fw.close();
		  }
		  catch (Exception e)
		  {

		  }
		  }*/
	}

	public void onPart(String channel, String sender, String login, String hostname) {
	}

	public void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {
	}

	public String getHelp() {
		return "This plugin makes me 'pounce' on new users that enter the channel, with a probablity of 1 in 3. to add a new pounce, use !addpounce followed by the message, using %NAME as a placeholder.";
	}
	public void onPrivateMessage(String sender, String login, String hostname, String message) {

	}

	public void unload() {}
    
	@Override
	public void onAdminMessage(String sender, String login, String hostname,
			String message) {
		// TODO Auto-generated method stub
		
	}
}
