/*
 * Copyright	Mex (ellism88@gmail.com)	2010
 * Copyright	A.Cassidy (a.cassidy@bytz.co.uk)        2007
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.PluggableBot;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

import com.PluggableBot.plugin.Plugin;


/**
 * An implementation of a PircBot with loadable module support.
 * 
 * @author A.Cassidy (a.cassidy@bytz.co.uk) 09 October 2007
 * @author M.Ellis (ellism88@gmail.com)
 */
public class PluggableBot extends PircBot {

	private ConcurrentHashMap<String, Plugin> loadedPlugins = new ConcurrentHashMap<String, Plugin>();
	private static Settings settings;
	private static PluggableBot b = new PluggableBot();
	private static ThreadPoolExecutor pool = new ThreadPoolExecutor(5, 10, 100,
			TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100));
	private static String admin = "";
	private static Logger log = Logger.getLogger(PluggableBot.class.getName());

	private static final String PLUGIN_DIR = "plugins";

	/**
	 * Main method. Used to star up the plugin
	 * 
	 * @param args
	 *            takes no options.
	 */
	public static void main(String[] args) {

		settings = new Settings();

		// add the shutdown hook for cleaning up
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				b.cleanup();
			}
		});
		b.setVerbose(true);
		b.loadPlugins(settings.getPlugins());

		b.connect();
		
		b.identify(settings.getNickservPassword());
	}

	/**
	 * Load a list of plugins. Currently used to start up the bot.
	 * 
	 * @param plugins
	 *            A list of plugins to load.
	 */
	public void loadPlugins(String[] plugins) {
		for (String plugin : plugins)
			loadPlugin(plugin);
	}

	/**
	 * Load a singular plugin. Currently used internaly to laod plugins. But
	 * could be used if a plugin wanted to load a plugin.
	 * 
	 * @param name
	 *            The Name of the plugin.
	 */
	public void loadPlugin(String name) {
		try {
			log.log(Level.INFO, "MainBot: attempting to load " + name);

			ArrayList<URL> paths = new ArrayList<URL>();
			File f = new File(PLUGIN_DIR + "/" + name + ".jar");
			paths.add(f.toURI().toURL());

			File f2 = new File("lib");
			for (File ff : f2.listFiles())
				paths.add(ff.toURI().toURL());

			URL[] urls = new URL[paths.size()];
			paths.toArray(urls);

			pool.execute(new PluggableBotLoader(name, urls, b));

		} catch (Exception ex) {
			log.log(Level.WARNING, "Failed to load plugin: " + ex.getMessage());
			ex.printStackTrace();
		}

	}

	/**
	 * Add a plugin to the list of loaded plugins.
	 * 
	 * @param name
	 *            Name of the plugin being added. This is the name you will need
	 *            to use to remove the plugin.
	 * @param p
	 *            The plugin that is being added
	 */
	protected void addPlugin(String name, Plugin p) {
		loadedPlugins.put(name, p);
		p.setBot(b);
	}

	public void unloadPlugin(String name) {
		loadedPlugins.get(name).unload();
		loadedPlugins.remove(name);
	}

	@Override
	protected void onAction(String sender, String login, String hostname,
			String target, String action) {
		for (Plugin p : loadedPlugins.values())
			p.onAction(sender, login, hostname, target, action);
	}

	@Override
	protected void onJoin(String channel, String sender, String login,
			String hostname) {
		for (Plugin p : loadedPlugins.values())
			p.onJoin(channel, sender, login, hostname);
	}

	private void connect() {
		try {
			b.setName(settings.getNick());
			b.connect(settings.getServer());
			for (String s : settings.getChannels())
				b.joinChannel(s);
		} catch (Exception e) {
			System.err
					.println("Could not connect to server: " + e.getMessage());
			System.exit(0);
		}
	}

	@Override
	protected void onDisconnect() {
		while (!b.isConnected()) {
			try {
				java.lang.Thread.sleep(60000);
			} catch (InterruptedException ex) {

			}
			connect();
		}
	}

	@Override
	protected void onKick(String channel, String kickerNick,
			String kickerLogin, String kickerHostname, String recipientNick,
			String reason) {
		for (Plugin p : loadedPlugins.values())
			p.onKick(channel, kickerNick, kickerLogin, kickerHostname,
					recipientNick, reason);
	}

	@Override
	protected void onMessage(String channel, String sender, String login,
			String hostname, String message) {
		if (message.startsWith("!help")) {
			if (message.trim().split(" ").length == 1) {
				// loaded plugins
				String m = "Plugins loaded: ";

				if (loadedPlugins.size() > 0) {
					for (String s : loadedPlugins.keySet())
						m += s + ", ";
				} else {
					m += " no plugins laoded., ";
				}
				m = m.substring(0, m.length() - 2);
				sendMessage(channel, m);
			} else {
				// try to find loaded plugin help
				String[] s = message.trim().split(" ");

				boolean flag = false;
				for (String string : loadedPlugins.keySet()) {
					if (string.toLowerCase().equals(s[1].toLowerCase())) {
						sendMessage(channel, loadedPlugins.get(string)
								.getHelp());
						flag = true;
					}
				}
				if (!flag) {
					sendMessage(channel,
							"Could not find help for the specified plugin");
				}

			}
		} else {
			for (Plugin p : loadedPlugins.values())
				p.onMessage(channel, sender, login, hostname, message);
		}
	}

	@Override
	protected void onPart(String channel, String sender, String login,
			String hostname) {
		for (Plugin p : loadedPlugins.values())
			p.onPart(channel, sender, login, hostname);
	}

	@Override
	protected void onQuit(String sourceNick, String sourceLogin,
			String sourceHostname, String reason) {
		for (Plugin p : loadedPlugins.values())
			p.onQuit(sourceNick, sourceLogin, sourceHostname, reason);

		if (sourceNick.equals(admin))
			admin = "";
	}

	@Override
	protected void onPrivateMessage(String sender, String login,
			String hostname, String message) {

		if (message.startsWith("identify")) {
			if (settings.getPassword() != null) {
				if (message.substring(9).equals(settings.getPassword())) {
					admin = sender;
					b.sendMessage(sender, "identified");
				}
			} else {
				log.info("Password was not Set, Admin is disabled.");
			}
		} else if (admin.equals(sender)) {
			for (Plugin p : loadedPlugins.values())
				p.onAdminMessage(sender, login, hostname, message);
		}

		for (Plugin p : loadedPlugins.values())
			p.onPrivateMessage(sender, login, hostname, message);

	}

	@Override
	protected void onNickChange(String oldNick, String login, String hostname,
			String newNick) {
		if (oldNick.equals(admin))
			admin = newNick;
	}

	public String Nick() {
		return b.getNick();
	}

	public void Action(String target, String action) {
		b.sendAction(target, action);
	}

	public void Message(String target, String message) {
		b.sendMessage(target, message);
	}

	public void Message(String channel, String target, String message) {
		b.sendMessage(channel, new StringBuilder().append(target).append(": ")
				.append(message).toString());
	}

	private void cleanup() {
		log.log(Level.INFO, "Shutting down...");
		for (Plugin p : loadedPlugins.values())
			p.unload();
	}

	@Override
	protected void onUserList(String channel, User[] users) {

		for (Plugin p : loadedPlugins.values())
			p.onUserList(channel, users);

	}

	public void sendFileDcc(File file, String nick, int timeout) {
		b.dccSendFile(file, nick, timeout);
	}

	public String[] getChans() {
		return b.getChannels();
	}

	public void kill(String nick, String channel) {
		b.kick(channel, nick);
	}

	public User[] users(String channel) {
		return b.getUsers(channel);
	}

	public void getNicks() {
	}

}
