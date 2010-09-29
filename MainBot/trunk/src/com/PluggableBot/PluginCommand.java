package com.PluggableBot;
import com.PluggableBot.plugin.Plugin;

public class PluginCommand {
	String command;
	Plugin plugin;
	boolean admin;

	public PluginCommand(String command, Plugin plugin, boolean admin) {
		super();
		this.command = command;
		this.plugin = plugin;
		this.admin = admin;
	}

	/**
	 * @return the command
	 */
	public final String getCommand() {
		return command;
	}

	/**
	 * @param command
	 *            the command to set
	 */
	public final void setCommand(String command) {
		this.command = command;
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
	 * @return the admin
	 */
	public final boolean isAdmin() {
		return admin;
	}

	/**
	 * @param admin
	 *            the admin to set
	 */
	public final void setAdmin(boolean admin) {
		this.admin = admin;
	}

}
