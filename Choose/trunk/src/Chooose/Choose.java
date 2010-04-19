package Chooose;
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
public class Choose extends DefaultPlugin {
    
    private static Random r = new Random();
    

    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        if (message.startsWith("!choose"))
        {
            try
            {
              String[] opts = message.substring(8).split(" or ");
              bot.Message(channel, sender + ": " + opts[r.nextInt(opts.length)]);
            }
            catch (Exception e)
            {
                
            }
        }
    }

    public String getHelp() {
        return "This plugin will allow me to make a simple decision between a list of items. Use !choose <item 1> or <item 2>...";
    }
    
}
