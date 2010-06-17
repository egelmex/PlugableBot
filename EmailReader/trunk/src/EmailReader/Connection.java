/*	
 * Copyright 2010 Mex (ellism88@gmail.com)
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

package EmailReader;

import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;

import com.sun.net.ssl.internal.ssl.Provider;

public class Connection implements Runnable {

	
	java.util.Properties props;
	EmailReader reader;
	Logger log = Logger.getLogger(Connection.class.getName());

	private boolean running = true;
	private static final int MAX_RETRIES = 3;

	public Connection(Properties props, EmailReader reader) {
		super();
		this.props = props;
		this.reader = reader;
	}

	public void kill() {
		running = false;
	}

	@Override
	public void run() {
		int retries = 0;
		javax.mail.Store store = null;
		while (retries < MAX_RETRIES && running) {
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
					props.setProperty("mail.imap.socketFactory.fallback",
							"false");
					props.setProperty("mail.imap.socketFactory.port", port);

					javax.mail.Session session = javax.mail.Session
							.getInstance(props);
					for (javax.mail.Provider p : session.getProviders())
						log.info(p.getProtocol());
					store = session.getStore(new javax.mail.URLName("imap://"
							+ username + ":" + password + "@" + server + "/"));
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

			for (int i = 0, n = message.length; (i < n) && running; i++) {
				for (String chan : props.getProperty("channels").split(",")) {
					System.out.println(chan.trim() + " : "
							+ message[i].getSubject());
					reader.bot.Message(chan.trim(), "email: "
							+ message[i].getSubject());
				}
				message[i].setFlag(Flags.Flag.DELETED, true);
			}

			folder.close(true);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(2 * 60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
