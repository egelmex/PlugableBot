package Stats;

import java.util.Date;

import com.PluggableBot.plugin.DefaultPlugin;

public class Stats extends DefaultPlugin{
	
	private static final String UPTIME_COMMAND = "!uptime";

	private final Date up_date;
	
	public Stats() {
		up_date = new Date();
	}
	
	@Override
	public void onMessage(String channel, String sender, String login,
			String hostname, String message) {
		if (message.toLowerCase().startsWith(UPTIME_COMMAND)) {
			Date currentDate = new Date();
		    long milliseconds1 = currentDate.getTime();
		    long milliseconds2 = up_date.getTime();
		    long diff = milliseconds2 - milliseconds1;
		    
		    long diffDays = diff / (24 * 60 * 60 * 1000);
		    diff = diff - (diffDays * (24 * 60 * 60 * 1000));
		    
		    long diffHours = diff / (60 * 60 * 1000);
		    diff = diff - (diffHours * (60 * 60 * 1000));
		    
		    long diffMinutes = diff / (60 * 1000);
		    diff = diff - (diffMinutes * (60 * 1000));
		    
		    long diffSeconds = diff / 1000;
		    
		   
		    String uptime = "";
		    uptime += (diffDays == 0) ? "" : (diffDays + " days " + ((diffHours == 0) ? "" : "and "));
		    uptime += (diffHours == 0) ? "" : (diffDays + " hours " + ((diffMinutes == 0) ? "" : "and "));
		    uptime += (diffMinutes == 0) ? "" : (diffDays + " mins " + ((diffSeconds == 0) ? "" : "and "));
		    uptime += (diffSeconds == 0) ? "" : (diffDays + " seconds ");
		}
	}
	
	
	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

}
