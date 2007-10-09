/*
 * PluggableBot.java
 *
 * Created on 09 October 2007, 14:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package AndrewCassidy.PluggableBot;

import java.util.*;
import java.io.*;
import org.jibble.pircbot.*;
/**
 *
 * @author AndyC
 */
public class PluggableBot extends PircBot {
    
    private static HashMap<String, Plugin> loadedPlugins = new HashMap<String, Plugin>();
    private static String nick = "Bob";
    private static String server = "irc.freenode.net";
    private static PluggableBot b = new PluggableBot();
    
    public static void main(String[] args)
    {
        
    }
    
    private static void loadSettings()
    {
        
    }
    
    private static void LoadPlugin(String name)    
    {
        
    }
}
