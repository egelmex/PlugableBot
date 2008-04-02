
import AndrewCassidy.PluggableBot.Plugin;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Andrew
 */
public class Mafiozi implements Plugin {

    public void onAction(String sender, String login, String hostname, String target, String action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onJoin(String channel, String sender, String login, String hostname) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onPart(String channel, String sender, String login, String hostname) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getHelp() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
