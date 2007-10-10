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
            Plugin p = new PluginLoader().loadPlugin(name);
            loadedPlugins.put(name, p);
        }
        catch (Exception ex)
        {
            System.err.println("Failed to load plugin: "+ex.getMessage());
        }
    }
    
    private static void unloadPlugin(String name)
    {
        loadedPlugins.remove(name);
        System.gc();
        System.gc();
        System.gc();
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
        if (message.startsWith("!help"))
        {    
            if (message.trim().split(" ").length == 1)
            {
                // loaded plugins
                String m = "Plugins loaded: ";
                for (String s : loadedPlugins.keySet())
                    m += s + ", ";
                
                m = m.substring(0, m.length() - 2);
                sendMessage(channel, m);
            }
            else
            {
                // try to find loaded plugin help
                String[] s = message.trim().split(" ");
                if (loadedPlugins.containsKey(s[1]))
                    sendMessage(channel, loadedPlugins.get(s[1]).getHelp());
                else
                    sendMessage(channel, "could not find help for the specified plugin");
            }
        }
        else
        {
            for (Plugin p : loadedPlugins.values())
                p.onMessage(channel, sender, login, hostname, message);    
        }
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
    
    protected void onPrivateMessage(String sender, String login, String hostname, String message)
    {
        if (message.startsWith("load"))
            loadPlugin(message.substring(5));
        else if (message.startsWith("unload"))
            unloadPlugin(message.substring(7));        
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
