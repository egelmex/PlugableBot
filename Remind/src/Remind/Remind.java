package Remind;

import java.text.DateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import com.PluggableBot.PluggableBot;
import com.PluggableBot.plugin.DefaultPlugin;
import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

public class Remind extends DefaultPlugin {

	private static final String COMMAND = "!remind";

	private Timer timer;
	private static final Logger log = Logger.getLogger(Remind.class.getName());
	private final ObjectContainer database;
	
	
	public Remind() {
		timer = new Timer();
		log.info("Tell: loading database");
		database = Db4o.openFile("Reminders.db4o");
		log.info("Tell: database open");
		
		ObjectSet<Reminder> storedreminders = database.get(Reminder.class);
		Date now = new Date();
		log.info("resceduling missed remidners");
		for (Reminder r : storedreminders) {
			if (r.getDate().before(now)) {
				log.info("dropped message" + r);
			} else {
				log.info("resceduled message" + r);
				timer.schedule(new Action(bot, r), r.date);
			}
		}
	}

	@Override
	public void onMessage(String channel, String sender, String login,
			String hostname, String message) {
		String target;

		if (message.toLowerCase().startsWith(COMMAND)) {
			log.info("got !remind");
			String[] messageSplit = message.toLowerCase().split(" ");
			target = messageSplit[1];
			if (target.equals("me"))
				target = sender;
			log.info("target = " + target);

			if (messageSplit[2].equals("in")) {

				int delay = 0;
				int i;
				for (i = 3; i < (messageSplit.length) ; i++) {
					try {
						if (messageSplit[ i].equals("and")|| messageSplit[i] == "," ) {
							continue;
						}
						log.info("parsing " + messageSplit[i]);
						Double val = Double.parseDouble(messageSplit[i]);
						i++;
						log.info("parsing " + messageSplit[i]);
						if (messageSplit[i].equals("min")
								|| messageSplit[i].equals("mins")
								|| messageSplit[i].equals("minute")
								|| messageSplit[i].equals("minutes")||
								messageSplit[i].equals("min,")
								|| messageSplit[i].equals("mins,")
								|| messageSplit[i].equals("minute,")
								|| messageSplit[i].equals("minutes,")) {
							delay += val * 60;
						} else if (messageSplit[i].equals("sec")
								|| messageSplit[i].equals("secs")
								|| messageSplit[i].equals("seconds")
								|| messageSplit[i].equals("second")||
								messageSplit[i].equals("sec,")
								|| messageSplit[i].equals("secs,")
								|| messageSplit[i].equals("seconds,")
								|| messageSplit[i].equals("second,")) {
							delay += val;
						} else if (messageSplit[i].equals("hour")
								|| messageSplit[i].equals("hours")||
								messageSplit[i].equals("hour,")
								|| messageSplit[i].equals("hours,")) {
							delay += val * 60 * 60;
						} else if (messageSplit[i].equals("day")
								|| messageSplit[i].equals("days") ||
								messageSplit[i].equals("day,")
								|| messageSplit[i].equals("days,")) {
							delay += val * 60 * 60 * 24;
						} else {
							break;
						}
						log.info("delay is now " + delay);
					} catch (java.lang.NumberFormatException e) {
						log.severe(e.toString());
						break;

					} catch (Exception e) {
						log.severe(e.toString());
						break;

					}

				}
				if (delay > 0) {
					String[] m = new String[messageSplit.length - i];
					System.arraycopy(messageSplit, i, m, 0, messageSplit.length - i);
					message = "";
					for (String ms: m) {
						message += ms + " ";
					}
					
					Date date = new Date(
							(new Date().getTime() + (delay * 1000)));
					bot.sendMessage(channel, sender
							+ ": I will remind " + target + " of that at "
							+ DateFormat.getDateTimeInstance().format(date));
					if (sender.equals(target)) sender = "you";
					Reminder r = new Reminder(sender, target, message, date, channel);
					TimerTask t = new Action(bot, r);
						
					timer.schedule(t, r.date);
					database.set(r);
					database.commit();
				}
			}

		}
	}

	@Override
	public String getHelp() {
		return "!remind me in XXX to YYY";
	}

	private class Action extends TimerTask {
		Reminder r;
		PluggableBot b;
		public Action(PluggableBot b, Reminder r) {
			this.r = r;
			this.b = b;
		}
		@Override
		public void run() {
			b.sendMessage(r.getChannel(), r.getTo() + ": " + r.getFrom() + " asked me to remind you " + r.getMessage());
			database.delete(r);
			database.commit();
		}
		
	}
	
	@Override
	public void unload() {
		timer.cancel();
		database.close();
	}
}
