/*
 * Plugin.java
 *
 * Created on 09 October 2007, 14:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package AndrewCassidy.PluggableBot;

/**
 *
 * @author AndyC
 */
public interface Plugin {
    void onAction(String sender, String login, String hostname, String target, String action);
    void onJoin(String channel, String sender, String login, String hostname);
    void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason);
    void onMessage(String channel, String sender, String login, String hostname, String message);
    void onPart(String channel, String sender, String login, String hostname);
    void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason);
    void onPrivateMessage(String sender, String login, String hostname, String message);
    String getHelp();
}
