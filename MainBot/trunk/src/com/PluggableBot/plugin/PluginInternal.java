package com.PluggableBot.plugin;

import java.util.List;

import org.jibble.pircbot.User;

import com.PluggableBot.PluggableBot;

public class PluginInternal implements Plugin {
	private Plugin plugin;
	private List<String> commands;

	public PluginInternal(Plugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * @return the plugin
	 */
	public final Plugin getPlugin() {
		return plugin;
	}

	/**
	 * @param plugin
	 *            the plugin to set
	 */
	public final void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * @return the commands
	 */
	public final List<String> getCommands() {
		return commands;
	}

	/**
	 * Add a command
	 * 
	 * @param command
	 */
	public final boolean addCommand(String command) {
		if (commands.contains(command)) {
			return true;
		} else {
			commands.add(command);
			return false;
		}
	}

	@Override
	public String getAdminHelp() {
		return plugin.getAdminHelp();
	}

	@Override
	public String getHelp() {
		return plugin.getHelp();
	}

	@Override
	public void onAction(String sender, String login, String hostname,
			String target, String action) {
		plugin.onAction(sender, login, hostname, target, action);
	}

	@Override
	public void onAdminMessage(String sender, String login, String hostname,
			String message) {
		plugin.onAdminMessage(sender, login, hostname, message);
	}

	@Override
	public void onJoin(String channel, String sender, String login,
			String hostname) {
		plugin.onJoin(channel, sender, login, hostname);
	}

	@Override
	public void onKick(String channel, String kickerNick, String kickerLogin,
			String kickerHostname, String recipientNick, String reason) {
		plugin.onKick(channel, kickerNick, kickerLogin, kickerHostname,
				recipientNick, reason);
	}

	@Override
	public void onMessage(String channel, String sender, String login,
			String hostname, String message) {
		plugin.onMessage(channel, sender, login, hostname, message);
	}

	@Override
	public void onNickChange(String oldNick, String login, String hostname,
			String newNick) {
		plugin.onNickChange(oldNick, login, hostname, newNick);
	}

	@Override
	public void onPart(String channel, String sender, String login,
			String hostname) {
		plugin.onPart(channel, sender, login, hostname);
	}

	@Override
	public void onPrivateMessage(String sender, String login, String hostname,
			String message) {
		plugin.onPrivateMessage(sender, login, hostname, message);
	}

	@Override
	public void onQuit(String nick, String login, String hostname, String reason) {
		plugin.onQuit(nick, login, hostname, reason);
	}

	@Override
	public void onUserList(String channel, User[] users) {
		plugin.onUserList(channel, users);
	}

	@Override
	public void setBot(PluggableBot bot) {
		plugin.setBot(bot);
	}

	@Override
	public void unload() {
		plugin.unload();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.PluggableBot.plugin.Plugin#onCommand(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void onCommand(String command, String channel, String sender,
			String login, String hostname, String message) {
		plugin.onCommand(command, channel, sender, login, hostname, message);

	}

	@Override
	public void onAdminCommand(String command, String channel, String sender,
			String login, String hostname, String message) {
		plugin.onAdminCommand(command, channel, sender, login, hostname, message);
		
	}

	/* (non-Javadoc)
	 * @see com.PluggableBot.plugin.Plugin#onPrivateAdminCommand(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void onPrivateAdminCommand(String command, String sender,
			String login, String hostname, String message) {
		plugin.onPrivateAdminCommand(command, sender, login, hostname, message);
		
	}

	/* (non-Javadoc)
	 * @see com.PluggableBot.plugin.Plugin#onPrivateCommand(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void onPrivateCommand(String command, String sender, String login,
			String hostname, String message) {
		plugin.onPrivateCommand(command, sender, login, hostname, message);
		
	}

}
