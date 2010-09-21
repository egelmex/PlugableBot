package Remind;

import java.text.DateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.PluggableBot.plugin.DefaultPlugin;

public class Remind extends DefaultPlugin {

	private static final String COMMAND = "!remind";

	private Timer timer;

	public Remind() {
		timer = new Timer();

	}

	@Override
	public void onMessage(String channel, String sender, String login,
			String hostname, String message) {
		String target;
		
		if (message.toLowerCase().startsWith(COMMAND)) {
			String[] messageSplit = message.toLowerCase().split(" ");
			target = messageSplit[1];
			

			if (messageSplit[2] == "in") {
			
				int delay = 0;
				int i;
				for (i = 3; i < messageSplit.length; i = i + 2) {
					try {
						int val = Integer.parseInt(messageSplit[i]);
						if (messageSplit[i + 1] == "min"
								|| messageSplit[i + 1] == "mins"
								|| messageSplit[i + 1] == "minute"
								|| messageSplit[i + 1] == "minutes") {
							delay += val * 60;
						} else if (messageSplit[i + 1] == "sec"
								|| messageSplit[i + 1] == "secs"
								|| messageSplit[i + 1] == "seconds"
								|| messageSplit[i + 1] == "second") {
							delay += val;
						} else if (messageSplit[i + 1] == "hour"
								|| messageSplit[i + 1] == "hours") {
							delay += val * 60 * 60;
						} else if (messageSplit[i + 1] == "day"
								|| messageSplit[i + 1] == "days") {
							delay += val * 60 * 60 * 24;
						} else {
							break;
						}
					} catch (Exception e) {
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
