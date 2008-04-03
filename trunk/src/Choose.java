/*
 * Choose.java
 *
 * Created on 31 October 2007, 08:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import AndrewCassidy.PluggableBot.*;
import java.util.Random;
/**
 *
 * @author AndyC
 */
public class Choose implements Plugin {
    
    private static Random r = new Random();
    
    /** Creates a new instance of Choose */
    public Choose() {
    }

    public void onAction(String sender, String login, String hostname, String target, String action) {
    }

    public void onJoin(String channel, String sender, String login, String hostname) {
    }

    public void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason) {
    }

    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        if (message.startsWith("!choose"))
        {
            try
            {
              String[] opts = message.substring(8).split(" or ");
              PluggableBot.Message(channel, sender + ": " + opts[r.nextInt(opts.length)]);
            }
            catch (Exception e)
            {
                
            }
        }
    }

    public void onPart(String channel, String sender, String login, String hostname) {
    }

    public void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {
    }

    public String getHelp() {
        return "This plugin will allow me to make a simple decision between a list of items. Use !choose <item 1> or <item 2>...";
    }
    
    public void onPrivateMessage(String sender, String login, String hostname, String message) {
       
    }
}
