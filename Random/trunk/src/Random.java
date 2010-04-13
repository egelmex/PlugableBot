/*
 * Random.java
 *
 */

import AndrewCassidy.PluggableBot.DefaultPlugin;
import AndrewCassidy.PluggableBot.PluggableBot;

/**
 * RNG plugin for Plugable Bot
 * @author Murmew
 * @author Mex
 */
public class Random extends DefaultPlugin
{
	public PluggableBot bot;
	
    private MersenneTwister mersenneRandom;
    private java.util.Random random;
	
	public Random()
	{
        mersenneRandom = new MersenneTwister();
        random = new java.util.Random(new java.util.Date().getTime());
	}

    private boolean isNumber(String number)
    {
        try {
            Integer.valueOf(number);
            return true;
        }
        catch (Exception e) { }
        return false;
    }
    
    private int getRand(int min, int max)
    {
        int ret = 0;
        
        int tries = 0;
        do {
            ret = random.nextInt(max);
            tries++;
        } while ((tries < 5000) && (ret < min)); // muh. D:
        
        return ret;
    }
	
    @Override
	public void onMessage(String channel, String sender, String login,
			String hostname, String message) {
		if (message.toLowerCase().startsWith("!random")) {
            String[] parts = message.split(" "); // sorry mex, i dont know an easier way :<
			
			int random = 0;
			if (parts.length == 2) {
				if (isNumber(parts[1])) {
					random = getRand(0, Integer.parseInt(parts[1]));
				}
			}
			else if (parts.length == 3) {
				if (isNumber(parts[1]) && isNumber(parts[2])) {
					int a = Integer.parseInt(parts[1]);
					int b = Integer.parseInt(parts[2]);
					if (a < b) {
						random = getRand(a, b);
					}
					else {
						PluggableBot.Message(channel, sender + ": Nu.");
						return;
					}
				}
			}
			else {
				random = mersenneRandom.extractNumber();
			}
			PluggableBot.Message(channel, sender + ": Your random number is: " + random);
        }
	}

    @Override
	public String getHelp() {
		return "Type \"!random optional:min optional:max\" to get a random number!";
	}
}
