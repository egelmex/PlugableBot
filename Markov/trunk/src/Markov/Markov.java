package Markov;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.ooweb.http.Server;
import net.sf.ooweb.http.pygmy.OowebServer;

import com.PluggableBot.ignorelib.IgnoreLib;
import com.PluggableBot.plugin.DefaultPlugin;


/*
 * Markov.java
 *
 * Created on 10 October 2007, 19:34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
/**
 * 
 * @author A.Cassidy (a.cassidy@bytz.co.uk)
 */

public class Markov extends DefaultPlugin {

	private final LinkedBlockingQueue<String> learnQueue = new LinkedBlockingQueue<String>();
	private final MarkovDatabase db = new MarkovDatabase();
	private final MarkovString m = new MarkovString(learnQueue, db);
	private IgnoreLib ignore = new IgnoreLib(this, "ignore");
	private IgnoreLib ignoreLearn = new IgnoreLib(this, "learn");
	private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(
			3, 3, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3));

	private static final Logger log = Logger.getLogger(Markov.class.getName());

	private Timer timer;
	private Integer spamCount = 0;
	private int maxSpamPerMin = 10; // 0==unmimited spam
	
	private Server web_server;

	public Markov() {
		log.info("Marokv Loaded, staring threads.");
		executor.execute(db);
		executor.execute(m);
		TimerTask spamControl = new TimerTask() {
			@Override
			public void run() {
				synchronized (spamCount) {
					if (spamCount > 0) {
						spamCount--;

						Logger.getLogger(Markov.class.getName()).log(
								Level.INFO, "spamCount == " + spamCount);
					}
				}

			}
		};
		(timer = new Timer(true)).schedule(spamControl, 10000, 10000);
		
		try {
			log.info("initialising OOserver");
			web_server = new OowebServer(new File("web.properties"));
			log.info("adding controler to OOserver");
			web_server.addController(new MarkovExplorer(db));
			log.info("OOserver start");
			Runnable webStarter = new Runnable() {
				@Override
				public void run() {
					try {
						web_server.start();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			executor.execute(webStarter);//web_server.start();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	public void onMessage(String channel, String sender, String login,
			String hostname, String message) {

		if (!ignore.ignore(sender)) {
			if (message.startsWith("!bobstats")) {
				File f = new File("Markov2.db4o");
				double size = (double) f.length() / 1048576f;
				int[] stats = db.getStats();
				bot.Message(channel, "My dictionary currently holds "
						+ stats[0] + " words and " + stats[1]
						+ " word associations. My dictionary file is "
						+ String.format("%1$.2f", size) + " MB. Learn Queue: "
						+ learnQueue.size());
			} else {
				if (!ignoreLearn.ignore(sender)) {
					m.Learn(message);
				}
				if (message.toLowerCase().indexOf(bot.Nick().toLowerCase()) > -1) {

					synchronized (spamCount) {
						if (spamCount < maxSpamPerMin || maxSpamPerMin == 0) {
							spamCount++;
							Logger.getLogger(Markov.class.getName()).log(
									Level.INFO, "spamCount == " + spamCount);
							bot.Message(channel, db.Generate());
						}
					}

				}
			}
		}
	}

	public String getHelp() {
		return "The Markov plugin is a simple implementation of Markov chains. This plugin allows me to 'Learn' from what is said in the channel and be able to peice together sentences.";
	}

	public void onPrivateMessage(String sender, String login, String hostname,
			String message) {
		if (!ignore.ignore(sender)) {
			if (message.startsWith("IgnoreAll ")) {
				ignore.addIgnore(message.substring("ignoreAll ".length())
						.trim());
			} else if (message.startsWith("UnignoreAll ")) {
				ignore.removeIgnore(message.substring("UnignoreAll ".length())
						.trim());
			} else if (message.startsWith("Ignore ")) {
				ignoreLearn.addIgnore(message.substring("ignore ".length())
						.trim());
			} else if (message.startsWith("Unignore ")) {
				ignoreLearn.removeIgnore(message
						.substring("unignore ".length()).trim());
			}
		}
	}

	public void unload() {
		try {

			timer.cancel();

			executor.shutdown();

			m.shutdown();
			if (learnQueue.peek() == null) {
				learnQueue.put("");
			}

			db.shutdown();

			executor.awaitTermination(10, TimeUnit.MINUTES);

		} catch (InterruptedException ex) {
			Logger.getLogger(Markov.class.getName())
					.log(Level.SEVERE, null, ex);
		}

	}
}
