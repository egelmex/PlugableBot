import AndrewCassidy.PluggableBot.DefaultPlugin;
import AndrewCassidy.PluggableBot.PluggableBot;

public class Admin extends DefaultPlugin {

	@Override
	public String getHelp() {
		return "Like I would tell you!";
	}

	@Override
	public void onAdminMessage(String sender, String login, String hostname,
			String message) {
		if (message.startsWith("unload")) {
			bot.unloadPlugin(message.substring(7));
			PluggableBot.Message(sender, "unloaded");
		} else if (message.startsWith("reload")) {
			bot.unloadPlugin(message.substring(7));
			bot.loadPlugin(message.substring(7));
			PluggableBot.Message(sender, "reloaded");
		} else if (message.startsWith("join")) {
			bot.joinChannel(message.substring(5));
			PluggableBot.Message(sender, "joined");
		} else if (message.startsWith("part")) {
			bot.partChannel(message.substring(5));
			PluggableBot.Message(sender, "left");
		} else if (message.startsWith("load")) {
			bot.loadPlugin(message.substring(5));
		}

	}

}
