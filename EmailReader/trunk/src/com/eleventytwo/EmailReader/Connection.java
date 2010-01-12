package com.eleventytwo.EmailReader;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import AndrewCassidy.PluggableBot.PluggableBot;

public class Connection implements Runnable {

	private String username;
	private String password;

	private String mailbox;

	private List<String> channels = new ArrayList<String>();

	private String server;

	private int port;

	private static final int MAX_RETRIES = 3;

	public Connection(String username, String password, String mailbox,
			List<String> channels, String server, int port) {
		super();
		this.username = username;
		this.password = password;
		this.mailbox = mailbox;
		this.channels = channels;
		this.server = server;
		this.port = port;
	}
	
	public Connection() {
		
	}

	@Override
	public void run() {
		int retries = 0;
		Store store = null;
		while (retries < MAX_RETRIES) {
			System.out.println("retries = " + retries);
			if (store == null || !store.isConnected()) {
				boolean ok = false;
				
				try {
					Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
					Properties props = new java.util.Properties();
					props.setProperty("mail.imap.socketFactory.class",
							"javax.net.ssl.SSLSocketFactory");
					props.setProperty("mail.imap.socketFactory.fallback", "false");
					props.setProperty("mail.imap.socketFactory.port", Integer
							.toString(port));

					Session session = javax.mail.Session.getInstance(props);
					System.out.println("imap://"
							+ username + ":" + password + "@" + server + "/");
					store = session.getStore(new javax.mail.URLName("imap://"
							+ username + ":" + password + "@" + server + "/"));
					store.connect();
					ok = true;
					
				} catch (NoSuchProviderException e) {
					e.printStackTrace();
				} catch (MessagingException e) {
					e.printStackTrace();
				}

				
				
				
				if (ok) {
					retries = 0;
				} else {
					retries++;
					try {
						Thread.currentThread().sleep(30 * 1000);
					} catch (InterruptedException e) {
					}
				}
			} else {
				read(store, mailbox);
			}

		}
		if (retries >= MAX_RETRIES ){
			System.out.println("MAX_RETRIES REACHED");
		}

	}

	private void read(Store store, String mailbox) {
		System.out.println("reading mailbox");
		try {
			Folder folder = store.getFolder(mailbox);

			folder.open(Folder.READ_WRITE);

			// Get directory
			Message message[] = folder.getMessages();

			for (int i = 0, n = message.length; i < n; i++) {
				for (String chan : channels) {
					System.out.println(chan + " : " + message[i].getSubject());
					PluggableBot.Message(chan, "email: "
							+ message[i].getSubject());
				}
				message[i].setFlag(Flags.Flag.DELETED, true);
			}

			folder.close(true);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		try {
			Thread.currentThread().sleep(30 * 1000);
		} catch (InterruptedException e) {
		}
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMailbox() {
		return mailbox;
	}

	public void setMailbox(String mailbox) {
		this.mailbox = mailbox;
	}

	public List<String> getChannels() {
		return channels;
	}

	public void setChannels(List<String> channels) {
		this.channels = channels;
	}
	
	public void setChannels(String[] channels) {
		for(String chan : channels) {
			this.channels.add(chan);
		}
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
