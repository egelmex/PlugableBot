package Tell;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.PluggableBot.plugin.DefaultPlugin;

public class Tell extends DefaultPlugin {

	private static final String command = "tell";
	Map<String, Map<String, List<Message>>> messages = new HashMap<String, Map<String, List<Message>>>();

	@Override
	public void onMessage(String channel, String sender, String login,
			String hostname, String message) {

		if (message.toLowerCase().startsWith("!" + command)) {
			message = message.substring(("!" + command).length() + 1);
			String[] split = message.split(" ");
			if (split.length > 2) {
				String target = split[0];
				message = message.substring(split[0].length() + 1);
				Map<String, List<Message>> ls = messages.get(channel);
				if (ls == null)
					ls = new HashMap<String, List<Message>>();
				List<Message> ms = ls.get(target);
				if (ms == null)
					ms = new ArrayList<Message>();
				ms.add(new Message(sender, new Date(), target, message));
			}
		}
		Map<String, List<Message>> chls = messages.get(channel);
		if (chls != null) {
			List<Message> toSend = chls.get(sender);
			if (toSend != null)
				for (Message m : toSend) {
					bot.sendMessage(channel, m.target+ ":" + m + " [sent: " + m.date.toGMTString() + ", from: " + m.sender );
				}
		}

	}
	
	@Override
	public void onNickChange(String oldNick, String login, String hostname,
			String newNick) {
		
	}

	@Override
	public String getHelp() {
		return "";
	}

}
