package com.PluggableBot.plugin;

import org.jibble.pircbot.User;

import com.PluggableBot.PluggableBot;


public abstract class DefaultPlugin implements Plugin {

	public PluggableBot bot;
	
	@Override
	public abstract String getHelp() ;

	@Override
	public void onAction(String sender, String login, String hostname,
			String target, String action) {	}

	@Override
	public void onAdminMessage(String sender, String login, String hostname,
			String message) {	}

	@Override
	public void onJoin(String channel, String sender, String login,
			String hostname) {}
	@Override
	public void onKick(String channel, String kickerNick, String kickerLogin,
			String kickerHostname, String recipientNick, String reason) {	}

	@Override
	public void onMessage(String channel, String sender, String login,
			String hostname, String message) {	}

	@Override
	public void onPart(String channel, String sender, String login,
			String hostname) {	}

	@Override
	public void onPrivateMessage(String sender, String login, String hostname,
			String message) {	}

	@Override
	public void onQuit(String sourceNick, String sourceLogin,
			String sourceHostname, String reason) {	}

	@Override
	public void onUserList(String channel, User[] users) {	}

	@Override
	public void unload() {	}

	@Override
	public void onNickChange(String oldNick, String login, String hostname,
			String newNick) {
	}

	@Override
	public void setBot(PluggableBot bot) {
		this.bot = bot;
	}

	@Override
	public String getAdminHelp() {
		return "There is no Admin Help for this plugin... :(";
	}

	/* (non-Javadoc)
	 * @see com.PluggableBot.plugin.Plugin#onCommand(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void onCommand(String command, String channel, String sender,
			String login, String hostname, String message) {	}

	@Override
	public void onAdminCommand(String command, String channel, String sender,
			String login, String hostname, String message) {
		
	}

	/* (non-Javadoc)
	 * @see com.PluggableBot.plugin.Plugin#onPrivateAdminCommand(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void onPrivateAdminCommand(String command, String sender,
			String login, String hostname, String message) {
	}

	/* (non-Javadoc)
	 * @see com.PluggableBot.plugin.Plugin#onPrivateCommand(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void onPrivateCommand(String command, String sender, String login,
			String hostname, String message) {
	}
	
	
}
