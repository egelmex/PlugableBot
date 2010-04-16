/*
 * Hometime.java
 *
 * Created on 11 November 2007, 19:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import AndrewCassidy.PluggableBot.DefaultPlugin;
import AndrewCassidy.PluggableBot.PluggableBot;

/**
 *
 * @author Administrator
 */
public class Hometime extends DefaultPlugin {
    
    private HashMap<String, String> hometimes = new HashMap<String, String>();
    
    /** Creates a new instance of Hometime */
    public Hometime() {
        try {
            FileReader fr = new FileReader("Hometime");
            BufferedReader br = new BufferedReader(fr);
            
            String line;
            
            while ((line = br.readLine()) != null)
            {
                String[] vals = line.split(":", 2);
                hometimes.put(vals[0], vals[1]);
            }
            
            br.close();
            fr.close();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void save()
    {
        FileWriter w;
        try {
            w = new FileWriter("Hometime");
        
        BufferedWriter r = new BufferedWriter(w);
        
        for (Map.Entry e : hometimes.entrySet())
        {
            r.write(e.getKey() + ":" + e.getValue() + "\n");            
        }
		r.flush();
        	r.close();

		w.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        if (message.startsWith("!hometime"))
        {
            String hometime;
            // look up user's hometime
            
            if (hometimes.containsKey(sender))
            {
                hometime = hometimes.get(sender);
            }
            else hometime = "17:00";
            
            Calendar now = Calendar.getInstance();
            
            String[] p = hometime.split(":");
            int hr = Integer.parseInt(p[0].replace('-','0'));
            int min = Integer.parseInt(p[1].replace('-','0'));
                                    
            int chr = now.get(Calendar.HOUR_OF_DAY);
            int cmin = now.get(Calendar.MINUTE);
            
            // has hometime past for the day?
            if (chr > hr || (chr == hr && cmin > min))
            {
		hr += 24;
            }

/*          if (hr < chr)
            {
                hr += 24;
            }*/
            
            if (min < cmin)
            {
                min += 60;                
                hr--;
            }
            
            int hrs = hr-chr;
            int mins = min-cmin;
            
            String m = "Hometime is at " + hometime + ", which is ";
            
            if (hrs == 0 && mins == 0)
            {
                m += "now!";                
            }
            else
            {
                if (hrs == 0)
                {
                    m += "only ";
                }
                else if (hrs == 1)
                {
                    m += "1 hour ";
                }
                else
                {
                    m += hrs + " hours ";
                }
            
                if (hrs > 0 && mins > 0) m += "and ";
            
                if (mins == 1)
                {
                    m += "1 minute ";
                }
                else if (mins > 1)
                {
                    m += mins + " minutes ";
                }
                        
                m += "away!";
            }
            
            PluggableBot.Message(channel, m);
            
            //System.out.println("Hometime is at " + hometime + ", which is " 
                    //+ hrs + " hours and " + mins + " minutes away!");
        }
        else if (message.startsWith("!sethometime"))
        {
            String newtime = message.substring(13).trim();
            if (newtime.length() == 5 && newtime.charAt(2) == ':')
            {
                String[] p = newtime.split(":");
               
                try {
					int hr = Integer.parseInt(p[0].replace('-','0'));
					int min = Integer.parseInt(p[1].replace('-','0'));
					
					if (hr > 23 || hr < 0 || min < 0 || min > 59) {
						 PluggableBot.Message(channel, "Invalid time : " + newtime);
					} else {
					
						hometimes.put(sender, newtime);
						save();
					}
				} catch (NumberFormatException e) {
					 PluggableBot.Message(channel, "Generic error saving hometime : " + newtime);
				}
            }
            else
            {
                PluggableBot.Message(channel, "Time must be in the format HH:MM");
            }
        }
    }


    public String getHelp() {
        return "Call !hometime and i will tell you how long until the next home time. To set your hometime, use !sethometime HH:MM";
    }

}
