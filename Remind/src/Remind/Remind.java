package Remind;

import java.text.DateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.logging.Logger;

import com.PluggableBot.plugin.DefaultPlugin;

public class Remind extends DefaultPlugin {

	private static final String COMMAND = "!remind";

	private Timer timer;
	private static final Logger log = Logger.getLogger(Remind.class.getName());
	
	public Remind() {
		timer = new Timer();

	}

	@Override
	public void onMessage(String channel, String sender, String login,
			String hostname, String message) {
		String target;
		
		if (message.toLowerCase().startsWith(COMMAND)) {
			log.info("got !remind");
			String[] messageSplit = message.toLowerCase().split(" ");
			target = messageSplit[1];
			if (target == "me") target = sender;
			log.info( "target = " + target);

			if (messageSplit[2] == "in") {
			
				int delay = 0;
				int i;
				for (i = 3; i < messageSplit.length; i++) {
					try {
						int val = Integer.parseInt(messageSplit[i]);
						i++;
						if (messageSplit[i] == "min"
								|| messageSplit[i] == "mins"
								|| messageSplit[i] == "minute"
								|| messageSplit[i] == "minutes") {
							delay += val * 60;
						} else if (messageSplit[i] == "sec"
								|| messageSplit[i] == "secs"
								|| messageSplit[i] == "seconds"
								|| messageSplit[i] == "second") {
							delay += val;
						} else if (messageSplit[i] == "hour"
								|| messageSplit[i] == "hours") {
							delay += val * 60 * 60;
						} else if (messageSplit[i] == "day"
								|| messageSplit[i] == "days") {
							delay += val * 60 * 60 * 24;
						} else {
							break;
						}
						log.info("delay is now " + delay );
					} catch (Exception e) {
						log.severe(e.toString());
						break;
						
					}

				}
				if (delay > 0) {
					message = message.substring(message.indexOf(messageSplit[i]), message.length() -message.indexOf(messageSplit[i]) );
					Date date = new Date((new Date().getTime() + (delay * 1000)));
					bot.sendMessage(channel, sender + ": I will remind you of that at " + DateFormat.getDateTimeInstance().format(date) );
					Reminder r = new Reminder(sender, target, message, date);
					timer.schedule(r, date);
				}
			}

		}
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

}
