import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;

import AndrewCassidy.PluggableBot.DefaultPlugin;
import AndrewCassidy.PluggableBot.PluggableBot;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;

public class Payments extends DefaultPlugin {

	private ObjectContainer database;

	public Payments() {
		database = Db4o.openFile("payments.db4o");
	}

	@Override
	public String getHelp() {
		return "Manages payments between members";
	}

	@Override
	public void onMessage(final String channel, final String sender,
			final String login, final String hostname, final String message) {
		String lmessage = message.toLowerCase();
		if (lmessage.startsWith("!debt ")) {
			lmessage = lmessage.substring("!debt ".length());

			String[] sMessage = lmessage.split(" ");
			if (sMessage.length > 3) {
				for (String string : sMessage) {
					System.err.println(string);
				}
				String from = sMessage[0];
				String action = sMessage[1];
				String to = sMessage[2];

				if (from.equals(sender.toLowerCase())) {
					from = sender.toLowerCase();
				}

				if (action.equals("owes") || action.equals("owe") || action.equals("paid")) {
					lmessage = lmessage
							.substring((from + " " + action + " " + to)
									.length());

					Number amount = parseCurrency(lmessage);
					
					if (amount != null) {
						System.out.println(from + action + to + amount);
					} else {
						System.out.println(sender
								+ ": could not parse value of money");
						PluggableBot.Message(channel, sender
								+ ": could not parse value of money");
					}

				} else {
					System.out.println(sender
							+ ": I do not know how to " + action + " money.");
					PluggableBot.Message(channel, sender
							+ ": I do not know how to " + action + " money.");
				}

			} else {
				System.out.println(sender
						+ ": Could not parse debt, not enough peramiters");
				PluggableBot.Message(channel, sender
						+ ": Could not parse debt, not enough peramiters");

			}

		} else {
			System.out.println("FAIL");
		}

	}

	private Number parseCurrency(String amount)  {
		Number n = null;
		
		 try {
			n = NumberFormat.getCurrencyInstance(Locale.UK).parse(amount.trim());
		} catch (ParseException e) {
			try {
				n = NumberFormat.getCurrencyInstance(Locale.UK).parse("£0." + amount.trim());
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return n;
	}
	
	public static void main(String[] args) {
		Payments t = new Payments();
		t.onMessage(null, "Mex", null, null, "!debt I owe Bob £5");
		t.onMessage(null, "Mex", null, null, "!debt I owe Bob £5.59");
		t.onMessage(null, "Mex", null, null, "!debt I owe Bob £5.59p");
		t.onMessage(null, "Mex", null, null, "!debt I owe Bob 59p");
		t.onMessage(null, "Mex", null, null, "!debt");

	}

	@Override
	public void onAdminMessage(String sender, String login, String hostname,
			String message) {
		// TODO Auto-generated method stub

	}
}
 