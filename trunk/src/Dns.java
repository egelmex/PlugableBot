/*
 * DNS.java
 *
 * Created on 19 October 2007, 17:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import AndrewCassidy.PluggableBot.*;
import java.net.InetAddress;
/**
 *
 * @author Administrator
 */
public class Dns implements Plugin {
    public void onAction(String sender, String login, String hostname, String target, String action) {
    }

    public void onJoin(String channel, String sender, String login, String hostname) {
    }

    public void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason) {
    }

    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        if (message.toLowerCase().startsWith("!dns"))
        {
            try
            {
                PluggableBot.Message(channel, InetAddress.getByName(message.substring(5).trim()).getHostAddress());
            }
            catch(Exception e)
            {
                PluggableBot.Message(channel, "Sorry, I couldn't find that host");
            }
        }
    }

    public void onPart(String channel, String sender, String login, String hostname) {
    }

    public void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {
    }

    public String getHelp() {
        return "This plugin lets me resolve hostnames the their ip addresses. Type !dns <hostname> to use.";
    }
}
