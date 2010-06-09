package Mailinfo;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.PluggableBot.plugin.DefaultPlugin;




/**
 * 
 * @author AndyC
 */
public class Mailinfo extends DefaultPlugin {

	private static final String COMMAND = "/usr/local/bin/mailinfo";

	public void onMessage(String channel, String sender, String login,
			String hostname, String message) {
		// PluggableBot.Message(channel, sender + ": " + hostname);
		if (message.startsWith("!mailinfo")) {
			String user = login;
			String tmp = message.substring(9).trim();
			if (tmp.length() > 0)
				user = tmp;
			try {
				ProcessBuilder pb = new ProcessBuilder(COMMAND, user);
				Process exec = pb.start();

				BufferedReader br = new BufferedReader(new InputStreamReader(
						exec.getInputStream()));
				// headers
				br.readLine();
				// extra
				bot.Message(channel, sender + ": " + br.readLine());
				exec.waitFor();
				br.close();
			} catch (Exception e) {

			}
		}
	}

	public String getHelp() {
		return "Type !mailinfo <login> to get email info for the specified login";
	}

}
