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
    private static PluginLoader l = new PluginLoader();
    private static ArrayList<String> channels = new ArrayList<String>();
    
    public static void main(String[] args)
    {
        b.setVerbose(true);
        try
        {
            loadSettings();
        }
        catch (Exception e)
        {
            System.err.println("Failed to process config file: "+e.getMessage());
            System.exit(0);
        }
        
        try
        {
            b.setName(nick);
            b.connect(server);
            for (String s : channels)
                b.joinChannel(s);
        }
        catch (Exception e)
        {
            System.err.println("Could not connect to server: "+e.getMessage());
            System.exit(0);
        }
    }
    
    private static void loadSettings() throws Exception
    {
        String context = "", line;
        FileReader fr = new FileReader("config");
        BufferedReader br = new BufferedReader(fr);
        
        while ((line = br.readLine()) != null)
        {
            if (line.startsWith("[") && line.endsWith("]"))
            {
                context = line.substring(1, line.length()-1);
            }
            else
            {
                if (line.trim().length() == 0)
                    continue;
                else if (context.equals("nick"))
                    nick = line;
                else if (context.equals("server"))
                    server = line;
                else if (context.equals("channels"))
                    channels.add(line);
                else if (context.equals("plugins"))
                    loadPlugin(line);
            }
        }
    }
    
    private static void loadPlugin(String name)    
    {
        try
        {
            Plugin p = l.loadPlugin(name);
            loadedPlugins.put(name, p);
        }
        catch (Exception ex)
        {
            System.err.println("Failed to load plugin: "+ex.getMessage());
        }
    }
    
    protected void onAction(String sender, String login, String hostname, String target, String action) 
    {
        for (Plugin p : loadedPlugins.values())
            p.onAction(sender, login, hostname, target, action);
    }
    
    protected void onJoin(String channel, String sender, String login, String hostname)
    {
        for (Plugin p : loadedPlugins.values())
            p.onJoin(channel, sender, login, hostname);
    }
    
    protected void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason)
    {
        for (Plugin p : loadedPlugins.values())
            p.onKick(channel, kickerNick, kickerLogin, kickerHostname, recipientNick, reason);
    }
    protected void onMessage(String channel, String sender, String login, String hostname, String message)
    {
        for (Plugin p : loadedPlugins.values())
            p.onMessage(channel, sender, login, hostname, message);    
    }
    protected void onPart(String channel, String sender, String login, String hostname)
    {
        for (Plugin p : loadedPlugins.values())
            p.onPart(channel, sender, login, hostname);
    }
    protected void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason)
    {
        for (Plugin p : loadedPlugins.values())
            p.onQuit(sourceNick, sourceLogin, sourceHostname, reason);
    }
    
    public static String Nick()
    {
        return b.getNick();
    }
            
    public static void Action(String target, String action) 
    {
        b.sendAction(target, action);
    }
    
    public static void Message(String target, String action) 
    {
        b.sendMessage(target, action);
    }
}
