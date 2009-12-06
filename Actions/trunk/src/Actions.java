/*
 * ActionsPlugin.java
 *
 * Created on 09 October 2007, 20:53
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
import AndrewCassidy.PluggableBot.PluggableBot;
import AndrewCassidy.PluggableBot.Plugin;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.io.*;
/**
 *
 * @author andee, Mex
 */
public class Actions implements Plugin {
    
    private ArrayList<String> attacks = new ArrayList<String>();
    private Random r = new Random();
    private boolean adminEnabled = false;
    
    private ThreadPoolExecutor pool = new ThreadPoolExecutor(3,5,60,TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(10));
    
    public Actions()
    {
    	reloadActions();
                
    }
    
    public void onAction(String sender, String login, String hostname, String target, String action) {
        if (action.toLowerCase().indexOf(PluggableBot.Nick().toLowerCase()) > -1)
        {
        	if (attacks.size() > 0) {
	            String a = attacks.get(r.nextInt(attacks.size()));
	            PluggableBot.Action(target, a.replaceAll("%SENDER", sender).replaceAll("%NAME", sender));
        	} else {
        		PluggableBot.Action(target, "I don't know what to do about that :(".replaceAll("%SENDER", sender).replaceAll("%NAME", sender));
        	}
        }
    }

    public void onJoin(String channel, String sender, String login, String hostname) {
    }

    public void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason) {
    }
    public void reloadActions() {
    	attacks = new ArrayList<String>();
		try
		{
		    FileReader fr = new FileReader("Action");
		    BufferedReader br = new BufferedReader(fr);
		    String line;
		    while ((line = br.readLine()) != null)
		    {
		        attacks.add(line);
		    }
		}
		catch (Exception e)
		{
		    throw new IllegalStateException("Unable to load actions file.");
		}
    }
    
    public boolean saveActions() {
    	boolean ok = true;
        try
        {
            FileWriter fw = new FileWriter("Action");
            BufferedWriter bw = new BufferedWriter(fw);
            for(String si : attacks){
            	bw.write(si + "\n");
            }
            bw.flush();
            bw.close();
            fw.close();
        }
        catch (Exception e)
        {
            ok=false;
        }
        return ok;
    }

    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        if (message.startsWith("!addaction")  && adminEnabled)
        {
            String s = message.substring(11);
            if (attacks.contains(s)) {
            	PluggableBot.Message(channel, sender + ": Stop feeding me the same junk!");
            } else {
            attacks.add(s);
            boolean ok = saveActions();
            if (ok) {
            	PluggableBot.Message(channel, sender + ": I will remeber that!");
            } else {
            	PluggableBot.Message(channel, sender + ": Whoops I am being forgetful today!" );
            	attacks.remove(s);
            }
            }
            
        } else if (message.toLowerCase().startsWith("!listactions") && adminEnabled) {
        	pool.execute(new Runny(sender, attacks));
        }
    }
    
    public class Runny implements Runnable {
    	String sender;
    	List<String> actions;
    	
    	public Runny(String sender, ArrayList<String> actions) {
    		this.sender = sender;
    		this.actions = (List<String>) actions.clone();
    	}
    	
		@Override
		public void run() {
        	PluggableBot.Message(sender, "My retorts are:");
        	for (String s : actions) {
        		PluggableBot.Message(sender, s);
        	}
			
		}
    	
    }

    public void onPart(String channel, String sender, String login, String hostname) {
    }

    public void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {
    }
    
    public String getHelp()
    {
    	if (adminEnabled) {
    		return "This plugin makes me retaliate to any action performed against me. Use !addaction followed by the action, using %NAME and %SENDER as placeholders to add a new one. !listactions";
    	} else {
    		return "This plugin makes me retaliate to any action performed against me.";
    	}
        
    }

    public void onPrivateMessage(String sender, String login, String hostname, String message) {
       
    }

    public void unload() {}

	@Override
	public void onAdminMessage(String sender, String login, String hostname,
			String message) {
		if (message.toLowerCase().startsWith("!actionenableadmin")) {
			this.adminEnabled = true;
			PluggableBot.Message(sender, "Action Admin Enabled");
		} else if (message.toLowerCase().startsWith("!actiondisableadmin")){
			this.adminEnabled = true;
			PluggableBot.Message(sender, "Action Admin Re-enabled");
		} else if (message.toLowerCase().startsWith("!actionreload")){
			reloadActions();
			PluggableBot.Message(sender, "Tried to reload Actions");
		} else if (message.toLowerCase().trim().equals("!help Actions")) {
			PluggableBot.Message(sender, "Actions Admin Help: !actionenableadmin, !actiondisableadmin, !actionreload");
		}
		
		
	}
}
