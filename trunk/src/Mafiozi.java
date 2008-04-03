
import AndrewCassidy.PluggableBot.Plugin;
import AndrewCassidy.PluggableBot.PluggableBot;
import org.jibble.pircbot.Colors;
import java.util.ArrayList;
import java.util.Random;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Andrew
 */
public class Mafiozi implements Plugin {

    private Boolean inGame = false;
    private static Random r = new Random();
    private String[] numberList;
    private ArrayList<String> actions;
    
    public void onAction(String sender, String login, String hostname, String target, String action) {
        
    }

    public void onJoin(String channel, String sender, String login, String hostname) {
        
    }

    public void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason) {
        
    }

    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        if (channel.equals("#mafiozi"))
        {
            if (message.startsWith("!reg") && !inGame)
            {
                PluggableBot.Message(channel, "!reg");
                inGame = true;
            }
            else if (inGame && sender.equals("mafiozi"))
            {
                if (message.contains("The End!"))
                {
                    clear();
                }
                else if (message.toLowerCase().contains(PluggableBot.Nick()) && message.toLowerCase().contains("killed"))
                {
                    clear();
                }
                else if (message.contains("!yes"))
                {
                    if (!message.contains(PluggableBot.Nick()))
                        PluggableBot.Message(channel, "!yes");
                    else
                        PluggableBot.Message(channel, "!no");
                }
                else if (message.contains("Current Players:"))
                {
                    processPlayersList(Colors.removeFormattingAndColors(message));
                }
                else if (message.contains("who will be executed?"))
                {
                    if (numberList == null) return;
                    String number = numberList[r.nextInt(numberList.length)];
                    PluggableBot.Message(channel, "!" + number);
                }
            }
        }
        else if (channel.equals(PluggableBot.Nick()) && sender.equals("mafiozi") && message.contains("Current Players:"))
        {
            // get a random number
            String playerNumber = processPlayersList(Colors.removeFormattingAndColors(message));
            
            // now choose a command
            String action = processActionList(Colors.removeFormattingAndColors(message));
            
            PluggableBot.Message("mafiozi", action + " " + playerNumber);
        }
    }

    private void clear()
    {
        inGame = false;
        actions = null;
    }
    
    private String processPlayersList(String message)
    {
        int location = message.indexOf(PluggableBot.Nick());
        int dotLocation = message.substring(0, location).lastIndexOf(".");
        int spaceLocation = message.substring(0, dotLocation).lastIndexOf(" ");
        
        String us = message.substring(spaceLocation, spaceLocation - dotLocation).replaceAll("[^0-9.]", "");
        
        String numberString = message.substring(0, message.indexOf("Players total")).replaceAll("[^0-9.]", "").replaceAll(us, "");
        numberList = numberString.replaceAll("[.]$", "").split("[.]");
        System.out.println(numberString);
        System.out.println(us);
        return numberList[r.nextInt(numberList.length)];
    }
    
    private String processActionList(String message)
    {
        if (actions == null)
        {
            actions = new ArrayList<String>();
            String[] actionsArray = message.split("[/]");
            for (String s : actionsArray)
            {
                if (s.startsWith("!") && s.indexOf(" ") < 0)
                    actions.add(s);
                System.out.println(s);
            }
        }
        return actions.get(r.nextInt(actions.size()));
    }
    
    public void onPart(String channel, String sender, String login, String hostname) {
        
    }

    public void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {
        
    }

    public String getHelp() {
        return "This plugin lets bob join in a game of Mafiozi. He's not very smart, though.";
    }

}
