/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import AndrewCassidy.PluggableBot.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
/**
 *
 * @author AndyC
 */
public class Mailinfo implements Plugin {

    private static final String COMMAND = "/usr/local/bin/mailinfo";
    
    public void onAction(String sender, String login, String hostname, String target, String action) {
        
    }

    public void onJoin(String channel, String sender, String login, String hostname) {
        
    }

    public void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason) {
        
    }

    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        //PluggableBot.Message(channel, sender + ": " + hostname);
        if (message.startsWith("!mailinfo"))
        {
	    String user = login;
	    String tmp = message.substring(9).trim();
	    if (tmp.length() > 0)
	    	user = tmp;
            try
            {
              Process exec = Runtime.getRuntime().exec(COMMAND + " " + user);
              BufferedReader br = new BufferedReader(new InputStreamReader(exec.getInputStream()));
              // headers
              br.readLine();
              // extra
              PluggableBot.Message(channel, sender + ": " + br.readLine());
              exec.waitFor();
              br.close();
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

    public void onPrivateMessage(String sender, String login, String hostname, String message) {
        
    }

    public String getHelp() {
        return "Type !mailinfo <login> to get email info for the specified login";
    }

    public void unload() {}
    
	@Override
	public void onAdminMessage(String sender, String login, String hostname,
			String message) {
		// TODO Auto-generated method stub
		
	}
}
