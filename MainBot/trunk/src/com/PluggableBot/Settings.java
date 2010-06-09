package com.PluggableBot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class Settings {
	Properties properties;
	Logger log = Logger.getLogger(Settings.class.getName());

	public Settings() {
		properties = new Properties();
		File configFile = new File("./config.ini");
		if (configFile.exists()) {
			try {
				properties.load(new FileInputStream(configFile));
			} catch (FileNotFoundException e) {
				log.warning(e.getMessage());
			} catch (IOException e) {
				log.warning(e.getMessage());
			}
		}
	}

	public String getNick() {
		return properties.getProperty("nick", "Bob");
	}

	public String getServer() {
		String server = properties.getProperty("server");
		if (server == null)
			throw new RuntimeException("A server has not been defined");
		return server;
	}

	public String getPassword() {
		String password = properties.getProperty("password");
		return password;
	}

	public String[] getChannels() {
		String channels = properties.getProperty("channels");
		if (channels != null) {
			return channels.split(",");
		} else {
			return new String[] {};
		}
	}

	public String[] getPlugins() {
		String plugins = properties.getProperty("plugins");
		if (plugins != null) {
			return plugins.split(",");
		} else {
			return new String[] {};
		}

	}

	public String getNickservPassword() {
		return properties.getProperty("nickservpassword", "");
	}
}
