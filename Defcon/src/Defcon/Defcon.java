package Defcon;

import com.PluggableBot.plugin.DefaultPlugin;
import org.jibble.pircbot.User;

import java.util.*;
import java.util.logging.*;

public class Defcon extends DefaultPlugin {

    private static final String NUKE_COMMAND = "!nuke";
    private static final String SCORE_COMMAND = "!score";
    private static final String END_COMMAND = "!nukeend";
    private static final String BAN_COMMAND = "!ban";
    private static final String UNBAN_COMMAND = "!unban";
    
    private NukeMain nm = new NukeMain();

    private static final int ver = 3;
    private static final int MAX_DEFCON_SECS_DELAY = 20;
    
    private static Random randomer = new Random();
    public static Logging logger = new Logging();
   
    private ArrayList<String> banList = new ArrayList<String>();
    
    @Override
    public void load() {
        bot.addCommand(NUKE_COMMAND, this);
        bot.addCommand(SCORE_COMMAND, this);
        bot.addAdminCommand(END_COMMAND, this);
        bot.addAdminCommand(BAN_COMMAND, this);
        bot.addAdminCommand(UNBAN_COMMAND, this);
    }
    
    @Override
    public void onPrivateAdminCommand(String command, String sender, String login, String hostname, String message) {
        message = message.trim();
        
        Defcon.logger.log("oPAC", sender + ": " + command + " " + message + " (" + login + " on " + hostname + " )");
        
        if (command.equals(END_COMMAND)) {
            bot.Message("#defcon", "End of the world...");
            scorer("#defcon", "");
            nm = new NukeMain();
            bot.Action("#defcon", "searches the multiverse for a similar universe");
        }
        
        if (command.equals(BAN_COMMAND)) {
            banList.add(message);
        }
        else if (command.equals(UNBAN_COMMAND)) {
            banList.remove(banList.indexOf(message));
        }
    }

    public boolean checkPermissions(String sender) {
        if (banList.contains(sender)) {
            return false;
        }
        else {
            return true;
        }
    }

    public void scorer(String sender, String message) {
        if (message.equals("")) {
            ArrayList<String> pt = nm.simpleprint();
            for (int i = 0; i < pt.size(); i++) {
            bot.Message(sender, pt.get(i));
            }
        }
        /*else { // too many messages
            ArrayList<String> pt = nm.playerPrint(message);
            if (pt == null) {
                return;
            }
            else {
                for (int i = 0; i < pt.size(); i++) {
                    bot.Message(sender, pt.get(i));
                }
            }
        } */
    }
    
    /*public void soloNuke(String channel) {
        int delay = randomer.nextInt(MAX_DEFCON_SECS_DELAY+1);
        double number = (new Double(randomer.nextInt(90)+10.0)) / 10.0;
        
        bot.Message(channel, "Launch Detected!");
        try {
            Thread.sleep(delay*1000);
        }
        catch (Exception e) {
            
        }
        bot.Message(channel, nm.getRandomCity() + " hit. " + number + "M dead");
    }*/
    
    public boolean validatePlayer(String sender, String channel)
    {
        User[] users = bot.getUsers(channel);
        for (int i = 0; i < users.length; i++) {
            if (users[i].getNick().equals(sender)) {
                return true;
            }
            if (!nm.isNewPlayer(sender)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void onCommand(String command, String channel, String sender, String login, String hostname, String message) {
        Defcon.logger.log("oC", sender + ": " + command + " " + message + " (" + login + " on " + hostname + " )");
        
        message = message.trim();

        if (!checkPermissions(sender)) {
            return;
        }

        if (command.equals(NUKE_COMMAND)) {
            //if (message.equals("")) {
                //soloNuke("#cs");
            //}
            if (validatePlayer(message, "#defcon") && validatePlayer(sender, "#defcon")) {
				Defcon.logger.log("oC","isvalidplayer");
                if (nm.isNewPlayer(sender)) {
                    if (nm.newPlayer(sender) == false) {
                        return;
                    }
                    else {
                        bot.Message("#defcon", sender + " is now playing Defcon and is representing " + nm.getPlayer(sender).getCountry().getName());
                    }
                }
                if (nm.isNewPlayer(message)) {
                    if (nm.newPlayer(message) == false) {
                        return;
                    }
                    else {
                        bot.Message("#defcon", message + " is now playing Defcon and is representing " + nm.getPlayer(message).getCountry().getName());
                    }
                }
                
                String mes = nm.nuke(sender, message);
                bot.Message("#defcon", mes);
            }
            else if (validatePlayer(sender, "#defcon") && nm.isValidTarget(message)) {
				Defcon.logger.log("oC","isvalidtarget");
				
				if (nm.isNewPlayer(sender)) {
                    if (nm.newPlayer(sender) == false) {
                        return;
                    }
                    else {
                        bot.Message("#defcon", sender + " is now playing Defcon and is representing " + nm.getPlayer(sender).getCountry().getName());
                    }
                }
				
                String mes = nm.nukeTarget(sender, message);
                bot.Message("#defcon", mes);
            }
            else {
                bot.Message("#defcon", "Not a valid player or target: "+message);
            }
        }
    }

    @Override
    public void onPrivateCommand(String command, String sender, String login, String hostname, String message) {
        Defcon.logger.log("oPC", sender + ": " + command + " " + message + " (" + login + " on " + hostname + " )");
    
        message = message.trim();
    
        if (!checkPermissions(sender)) {
            return;
        }
 
        if (command.equals(SCORE_COMMAND)) {
            scorer(sender,message);
        }
    }
    
    @Override
    public String getHelp() {
        return "/msg Gairnetaru: !nuke [player] or !nuke [city] or !score. Only players in #defcon can do this.";
        //return "This plugin simulates Defcon. Usage: !nuke [player] to nuke a player or !score [player] to get a list of scores messaged to you. When you are first noticed, you are assigned a country with 5 cities. These cities have a population, and when are nuked, lose population. The nuker gets points proportional to population loss, and the victim loses points proportionally. Example commands (comma separated): !nuke, !nuke Gairne, !score, !score Gairne";
    }
    
}
