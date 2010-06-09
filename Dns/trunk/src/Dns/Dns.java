package Dns;
/*
 * DNS.java
 *
 * Created on 19 October 2007, 17:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import java.net.InetAddress;


import com.PluggableBot.*;
import com.PluggableBot.plugin.DefaultPlugin;

/**
 * 
 * @author Administrator
 */
public class Dns extends DefaultPlugin {

	@Override
	public void onMessage(String channel, String sender, String login,
			String hostname, String message) {
		if (message.toLowerCase().startsWith("!dns")) {
			try {
				bot.Message(channel, InetAddress.getByName(
						message.substring(5).trim()).getHostAddress());
			} catch (Exception e) {
				bot.Message(channel,
						"Sorry, I couldn't find that host");
			}
		}
	}

	public String getHelp() {
		return "This plugin lets me resolve hostnames the their ip addresses. Type !dns <hostname> to use.";
	}
}
