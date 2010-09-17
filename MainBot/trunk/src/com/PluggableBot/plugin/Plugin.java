/*
 * Plugin.java
 *
 * Created on 09 October 2007, 14:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.PluggableBot.plugin;

import org.jibble.pircbot.User;

import com.PluggableBot.PluggableBot;

/**
 * 
 * @author A.Cassidy (a.cassidy@bytz.co.uk)
 * @author Mex (ellism88@gmail.com)
 */
public interface Plugin {
	/**
	 * Sets a reference to the PluggableBot allowing messages to be sent from
	 * this plugin.
	 * 
	 * @param bot
	 *            the PluggableBot reference
	 */
	void setBot(PluggableBot bot);

	/**
	 * 
	 * @param sender
	 * @param login
	 * @param hostname
	 * @param target
	 * @param action
	 */
	void onAction(String sender, String login, String hostname, String target,
			String action);

	/**
	 * This method will be called every time a user joins a channel that this
	 * bot is in.
	 * 
	 * @param channel
	 *            The channel the user joined
	 * @param sender
	 *            The Nick of the user who joined.
	 * @param login
	 *            The login of the user who joined.
	 * @param hostname
	 *            The hostname of the user who joined.
	 */
	void onJoin(String channel, String sender, String login, String hostname);

	/**
	 * This method will be called when a user is kicked from a channel
	 * 
	 * @param channel
	 *            The channel the user was kicked from.
	 * @param kickerNick
	 *            The Nick of the user who did the kicking
	 * @param kickerLogin
	 *            The login of the user doing the kicking
	 * @param kickerHostname
	 *            The hostname of the user doing the kicking
	 * @param recipientNick
	 *            The nick of the user who was kicked
	 * @param The
	 *            reason given for the user being kicked.
	 */
	void onKick(String channel, String kickerNick, String kickerLogin,
			String kickerHostname, String recipientNick, String reason);

	/**
	 * This Method will be called everytime anyone speaks in this channel. Do
	 * not block on this method as doing so will cause the whole bot the be
	 * blocked.
	 * 
	 * @param channel
	 * @param sender
	 * @param login
	 * @param hostname
	 * @param message
	 */
	void onMessage(String channel, String sender, String login,
			String hostname, String message);

	/**
	 * This method will be called whenever a User leaves a channel the bot is
	 * in.
	 * 
	 * @param channel
	 *            The channel the User left
	 * @param sender
	 *            The Nick of the user who left
	 * @param login
	 *            The login of the user who left
	 * @param hostname
	 *            The hostname of the user who left
	 */
	void onPart(String channel, String sender, String login, String hostname);

	/**
	 * This method will be called whenever a user quits a channel the bot is in.
	 * 
	 * @param nick
	 *            The nick of the user who left the channel
	 * @param login
	 *            The login of the user who left the channel
	 * @param hostname
	 *            The hostname of the user who left the channel
	 * @param reason
	 *            The reason given for the user leaving the channel
	 */
	void onQuit(String nick, String login, String hostname, String reason);

	/**
	 * This method will be called whenever a user private messages the bot
	 * 
	 * @param sender
	 *            the nick of the user who sent the PM
	 * @param login
	 *            the login of the user who sent the PM
	 * @param hostname
	 *            the hostname of the user who sent the PM
	 * @param message
	 *            the message the user sent in the PM
	 */
	void onPrivateMessage(String sender, String login, String hostname,
			String message);

	/**
	 * This method will be called whenever an admin PMs the bot. Please be aware
	 * that {@link}OnPrivateMessage will also be called.
	 * 
	 * @param sender
	 * @param login
	 * @param hostname
	 * @param message
	 */
	void onAdminMessage(String sender, String login, String hostname,
			String message);

	/**
	 * TODO
	 * @param channel
	 * @param users
	 */
	void onUserList(String channel, User[] users);

	/**
	 * This method will be called when this plugin is unloaded. Any cleanup
	 * should be done here and any resources freed. After this call your plugin
	 * will no longer be registered and will revice no more updates.
	 */
	void unload();

	/**
	 * This method will be called when a user changes their nick.
	 * 
	 * @param oldNick
	 *            The old nick of the user
	 * @param login
	 *            The login name registered with this user
	 * @param hostname
	 *            The Host name registered with this user
	 * @param newNick
	 *            The new nick of the user
	 */
	void onNickChange(String oldNick, String login, String hostname,
			String newNick);

	/**
	 * Return a help message to the user, that will help them to use this
	 * plugin.
	 * 
	 * @return A help message.
	 */
	String getHelp();

	/**
	 * Return an extended help message for Admin use.
	 * 
	 * @return A help string for the Admin Users.
	 */
	String getAdminHelp();
}
