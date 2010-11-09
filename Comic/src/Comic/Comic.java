package Comic;

import com.PluggableBot.plugin.DefaultPlugin;
import org.jibble.pircbot.User;

import java.util.*;
import java.io.*;
import java.util.logging.Logger;

public class Comic extends DefaultPlugin {

    public static final int MAX_QUOTES = 10;
    
    private static Random randomer = new Random();
    private static final Logger LOG = Logger.getLogger(Comic.class.getCanonicalName());

    private static final String CONFIG_FILE = "comic.ini";
    
    private File _outputDirectory;
    private String _helpString;

    private String[] MATCH_STR = {"lol", "haha", "rofl", "hehe"};

    private HashMap<String, LinkedList> c_quotes = new HashMap<String, LinkedList>();
    private HashMap<String, LinkedList> c_senders = new HashMap<String, LinkedList>();

    private String[] channels;

    public Comic() {
	System.setProperty("java.awt.headless", "true");
    }


    public boolean tryMatch(String[] ss, String message)
    {
	for (int i = 0; i < ss.length; i++) {
	    if (message.contains(ss[i])) { //contains, not starts with, yeah i know.
		return true;
	    }
	}
	return false;
    }

    @Override
    public void load() {
	try {
        Properties p = new Properties();
        p.load(new FileInputStream(new File(CONFIG_FILE)));
        _outputDirectory = new File(p.getProperty("outputDirectory"));
	_helpString = p.getProperty("helpString");
	
	channels = p.getProperty("monitoredChannels").split(",");

	for (int i = 0; i < channels.length; i++) {
	    channels[i] = channels[i].startsWith("#") ? channels[i] : "#" + channels[i];
	    c_quotes.put(channels[i], new LinkedList());
	    c_senders.put(channels[i], new LinkedList());
	    LOG.info(channels[i] + " " + "#"+channels[i]);
	}

        }
	catch (Exception e) {
	    System.out.println("Cannot find file. " + e.toString());
	}	
	if (!_outputDirectory.isDirectory()) {
            System.out.println("Output directory must be a valid directory, not " + _outputDirectory.toString());
        }
    }
    
    @Override
    public String getHelp() {
	return "Produces comics based on logged conversations. " + _helpString;
    }

    @Override
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        LOG.info(sender + " @" + channel + ": " + message + " (" + login + " on " + hostname + " )");
	processMessage(sender, channel, message);
    }

    @Override
    public void onAction(String sender, String login, String hostname, String target, String action) {
        LOG.info(sender + " (A) : " + action + " (" + login + " on " + hostname + " )");
        processMessage(sender, target, "* " + sender + " " + action);
    }

    public void processMessage(String sender, String channel, String message) {
        message = message.trim();
        String lowMsg = message.toLowerCase();

	LOG.info("My current channel is : " + channel);

	boolean found = false;
	for (int i = 0; i < channels.length; i++) {
	    LOG.info(channel + " <=> " + channels[i]); 
	    if (channel.equals(channels[i])) {
		found = true;
	    }
	}
	if (!found) {
	    return;
	}

	LOG.info("AND IT WAS FOUND");
	LOG.info("" + c_quotes.get(channel).size());
	LOG.info(lowMsg + " : " + tryMatch(MATCH_STR, lowMsg));

        if (c_quotes.get(channel).size() == MAX_QUOTES && (tryMatch(MATCH_STR, lowMsg))) { //(lowMsg.startsWith("lol") || lowMsg.startsWith("rofl")))
            // Let's make a cartoon!
            String[] texts = new String[MAX_QUOTES];
            String[] nicks = new String[MAX_QUOTES];

	    Iterator quoteIt = c_quotes.get(channel).iterator();
	    Iterator senderIt = c_senders.get(channel).iterator();

            for (int i = 0; i < MAX_QUOTES; i++) {
                String text = (String)quoteIt.next();
                String nick = (String)senderIt.next();
                texts[i] = text;
                nicks[i] = nick;
            }
            try {
                boolean result = ComicWrite.createCartoonStrip(_outputDirectory, channel, texts, nicks);
                if (result) {
                   bot.Message("Gairne", "New comic strip! " + _helpString);
                }
            }
            catch (IOException e) {
                bot.Message("Gairne", "Urgh, I'm crap cos I just did this: " + e);
            }
            c_quotes.get(channel).clear();
            c_senders.get(channel).clear();
        }
        else {
            c_quotes.get(channel).add(message);
            c_senders.get(channel).add(sender);
            if (c_quotes.get(channel).size() > MAX_QUOTES) {
                c_quotes.get(channel).removeFirst();
                c_senders.get(channel).removeFirst();
            }
        }
    }
}
