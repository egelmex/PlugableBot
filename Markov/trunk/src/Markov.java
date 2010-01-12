
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import AndrewCassidy.PluggableBot.DefaultPlugin;
import AndrewCassidy.PluggableBot.IgnoreLib;
import AndrewCassidy.PluggableBot.PluggableBot;
import Markov2.MarkovDatabase;
import Markov2.MarkovNode;
import Markov2.MarkovString;

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
 * @author Administrator
 */
public class Markov extends DefaultPlugin {

    private static final LinkedBlockingQueue<String> learnQueue = new LinkedBlockingQueue<String>();
    private static final LinkedBlockingQueue<MarkovNode> saveQueue = new LinkedBlockingQueue<MarkovNode>();
    private static final MarkovDatabase db = new MarkovDatabase(saveQueue);
    private static final MarkovString m = new MarkovString(learnQueue, saveQueue, db);
    private IgnoreLib ignore = new IgnoreLib(this, "ignore");
    private IgnoreLib ignoreLearn = new IgnoreLib(this, "learn");
    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 3, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3));

    
    private Timer timer;
    private Integer  spamCount = 0;
    private int maxSpamPerMin = 10; //0==unmimited spam
    
    public Markov() {
        Logger.getLogger(Markov.class.getName()).log(Level.INFO, "Marokv Loaded, staring threads.");
        executor.execute(db);
        executor.execute(m);
        TimerTask spamControl = new TimerTask() {
			@Override
			public void run() {
				synchronized (spamCount) {
					if (spamCount > 0) {
						spamCount--;
						
						Logger.getLogger(Markov.class.getName()).log(Level.INFO, "spamCount == " + spamCount);
					}
				}
				
			}
		};
		(timer = new Timer(true)).schedule(spamControl, 10000, 10000);
    }

    public void onMessage(String channel, String sender, String login,
            String hostname, String message) {
        if (!ignore.ignore(sender)) {
            if (message.startsWith("!bobstats")) {
                File f = new File("Markov2.db4o");
                double size = (double) f.length() / 1048576f;
                int[] stats = db.getStats();
                PluggableBot.Message(channel, "My dictionary currently holds " + stats[0] + " words and " + stats[1] + " word associations. My dictionary file is " + String.format("%1$.2f", size) + " MB. Learn Queue: " + learnQueue.size() + ", Save Queue: " + saveQueue.size());
            } else {
                if (!ignoreLearn.ignore(sender)) {
                    m.Learn(message);
                }
                if (message.toLowerCase().indexOf(
                        PluggableBot.Nick().toLowerCase()) > -1) {
                	
                	synchronized (spamCount) {
						if (spamCount < maxSpamPerMin || maxSpamPerMin == 0) {
							spamCount++;
							Logger.getLogger(Markov.class.getName()).log(Level.INFO, "spamCount == " + spamCount);
							PluggableBot.Message(channel, db.Generate());
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
                ignore.addIgnore(message.substring("ignoreAll ".length()).trim());
            } else if (message.startsWith("UnignoreAll ")) {
                ignore.removeIgnore(message.substring("UnignoreAll ".length()).trim());
            } else if (message.startsWith("Ignore ")) {
                ignoreLearn.addIgnore(message.substring("ignore ".length()).trim());
            } else if (message.startsWith("Unignore ")) {
                ignoreLearn.removeIgnore(message.substring("unignore ".length()).trim());
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
            if (saveQueue.peek() == null) {
                saveQueue.put(new MarkovNode("", true));
            }

            executor.awaitTermination(10, TimeUnit.MINUTES);
            
        } catch (InterruptedException ex) {
            Logger.getLogger(Markov.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
