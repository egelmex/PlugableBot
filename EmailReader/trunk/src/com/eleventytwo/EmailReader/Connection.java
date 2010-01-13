package com.eleventytwo.EmailReader;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;

import AndrewCassidy.PluggableBot.PluggableBot;

public class Connection implements Runnable {

	java.util.Properties props;

	private static final int MAX_RETRIES = 3;

	public Connection(Properties props) {
		super();
		this.props = props;
	}

	@Override
	public void run() {
		int retries = 0;
		javax.mail.Store store = null;
		while (retries < MAX_RETRIES) {
			System.out.println("retries = " + retries);
			if (store == null || !store.isConnected()) {
				boolean ok = false;

				try {
					String port = props.getProperty("port");
					String username = props.getProperty("username").trim();
					String password = props.getProperty("password").trim();
					String server = props.getProperty("server").trim();
					
						java.security.Security
								.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
						java.util.Properties props = new java.util.Properties();
						props.setProperty("mail.imap.socketFactory.class",
								"javax.net.ssl.SSLSocketFactory");
						props.setProperty("mail.imap.socketFactory.fallback", "false");
						props.setProperty("mail.imap.socketFactory.port", port);

						javax.mail.Session session = javax.mail.Session.getInstance(props);
						store = session.getStore(new javax.mail.URLName(
								"imap://" + username + ":" + password + "@" + server + "/"));
						store.connect();
						System.out.println("connected to store.");
						ok = true;
				} catch (NoSuchProviderException e1) {
					e1.printStackTrace();
				} catch (MessagingException e1) {
					e1.printStackTrace();
				}

				if (ok) {
					retries = 0;
				} else {
					retries++;
					try {
						Thread.sleep(30 * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} else {
				read(store, props.getProperty("mailbox"));
			}

		}
		if (retries >= MAX_RETRIES) {
			System.out.println("MAX_RETRIES REACHED");
		}

	}

	public java.util.Properties getProps() {
		return props;
	}

	private void read(javax.mail.Store store, String mailbox) {
		System.out.println("reading mailbox");
		try {
			Folder folder = store.getFolder(mailbox);

			folder.open(Folder.READ_WRITE);

			// Get directory
			Message message[] = folder.getMessages();

			for (int i = 0, n = message.length; i < n; i++) {
				for (String chan : props.getProperty("channels").split(",")) {
					System.out.println(chan.trim() + " : "
							+ message[i].getSubject());
					PluggableBot.Message(chan.trim(), "email: "
							+ message[i].getSubject());
				}
				// message[i].setFlag(Flags.Flag.DELETED, true);
			}

			folder.close(true);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(30 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
