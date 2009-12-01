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
import java.io.*;
/**
 *
 * @author andee
 */
public class Actions implements Plugin {
    
    private ArrayList<String> attacks = new ArrayList<String>();
    private Random r = new Random();
    
    public Actions()
    {
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
    
    public void onAction(String sender, String login, String hostname, String target, String action) {
        if (action.toLowerCase().indexOf(PluggableBot.Nick().toLowerCase()) > -1)
        {
            String a = attacks.get(r.nextInt(attacks.size()));
            PluggableBot.Action(target, a.replaceAll("%SENDER", sender).replaceAll("%NAME", sender));
        }
    }

    public void onJoin(String channel, String sender, String login, String hostname) {
    }

    public void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason) {
    }

    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        if (message.startsWith("!addaction"))
        {
            String s = message.substring(11);
            attacks.add(s);
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
            if (ok) {
            	PluggableBot.Message(channel, sender + ": I will remeber that!");
            } else {
            	PluggableBot.Message(channel, sender + ": Whoops I am being forgetful today!" );
            	attacks.remove(s);
            }
        }
    }

    public void onPart(String channel, String sender, String login, String hostname) {
    }

    public void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {
    }
    
    public String getHelp()
    {
        return "This plugin makes me retaliate to any action performed against me. Use !addaction followed by the action, using %NAME and %SENDER as placeholders to add a new one.";
    }

    public void onPrivateMessage(String sender, String login, String hostname, String message) {
       
    }

    public void unload() {}

	@Override
	public void onAdminMessage(String sender, String login, String hostname,
			String message) {
		// TODO Auto-generated method stub
		
	}
}
