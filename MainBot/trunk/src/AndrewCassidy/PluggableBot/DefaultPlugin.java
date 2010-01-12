package AndrewCassidy.PluggableBot;

import org.jibble.pircbot.User;

public abstract class DefaultPlugin implements Plugin {

	@Override
	public abstract String getHelp() ;

	@Override
	public void onAction(String sender, String login, String hostname,
			String target, String action) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAdminMessage(String sender, String login, String hostname,
			String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onJoin(String channel, String sender, String login,
			String hostname) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onKick(String channel, String kickerNick, String kickerLogin,
			String kickerHostname, String recipientNick, String reason) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMessage(String channel, String sender, String login,
			String hostname, String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPart(String channel, String sender, String login,
			String hostname) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPrivateMessage(String sender, String login, String hostname,
			String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onQuit(String sourceNick, String sourceLogin,
			String sourceHostname, String reason) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUserList(String channel, User[] users) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unload() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNickChange(String oldNick, String login, String hostname,
			String newNick) {
		// TODO Auto-generated method stub
		
	}

}
