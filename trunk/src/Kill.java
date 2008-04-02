/*
 * KillPlugin.java
 *
 * Created on 10 October 2007, 13:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
import AndrewCassidy.PluggableBot.Plugin;
import AndrewCassidy.PluggableBot.PluggableBot;

import java.util.*;
import java.io.*;
/**
 *
 * @author AndyC
 */
public class Kill implements Plugin {
    
    private Map<String, String> kills = Collections.synchronizedMap(new HashMap<String, String>());
    private boolean saving = false;
    private String def = "stabs %NAME with a elongated frozen eel";
    /** Creates a new instance of KillPlugin */
    public Kill() {
        load();
    }

    public void onAction(String sender, String login, String hostname, String target, String action) { }

    public void onJoin(String channel, String sender, String login, String hostname) { }

    public void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason) { }

    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        if (message.startsWith("!kill"))
        {
            String s = kills.get(sender);
            if (s == null) s = def;
            String target = message.substring(6).trim();
            if (target.toLowerCase().equals(PluggableBot.Nick().toLowerCase()))
                target = sender;
            PluggableBot.Action(channel, s.replaceAll("%NAME", target));
        }
        else if (message.startsWith("!setkill"))
        {
            kills.put(sender, message.substring(9));
            save();
        }
    }

    public void onPart(String channel, String sender, String login, String hostname) { }

    public void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) { }
    
    public String getHelp()
    {
        return "This allows users to order me to kill other users, by using !kill <username>. To customise your kill message, use !setkill, followed by the attack. use %NAME as a placeholder for a user's nick.";
    }
    
    private void load()
    {
        try {
            FileReader fr = new FileReader("Kills");
            BufferedReader br = new BufferedReader(fr);
            
            String line;
            
            while ((line = br.readLine()) != null)
            {
                String[] vals = line.split(":", 2);
                kills.put(vals[0], vals[1]);
            }
            
            br.close();
            fr.close();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
    private void save()
    {
        FileWriter w;
        try {
            w = new FileWriter("Kills");
        
        BufferedWriter r = new BufferedWriter(w);
        
        for (Map.Entry e : kills.entrySet())
        {
            r.write(e.getKey() + ":" + e.getValue() + "\n");            
        }
		r.flush();
        	r.close();

		w.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
