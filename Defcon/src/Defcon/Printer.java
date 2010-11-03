package Defcon;

import java.util.*;

public class Printer
{
    /**
     * Constructor for objects of class Printer
     */
    public Printer()
    {
        
    }
	
	public static String padLeft(String s, int n) {
		return String.format("%1$#" + n + "s", s);  
	}
	public static String padRight(String s, int n) {
		return String.format("%1$-" + n + "s", s);  
	}

    public ArrayList<String> simplePrint(HashMap<String, NukePerson> players)
    {
        ArrayList<String> scores = new ArrayList<String>();
        
		scores.add("Defcon scores:");
		
        for (Map.Entry<String, NukePerson> entry : players.entrySet()) {
            String key = entry.getKey();
			NukePerson p = entry.getValue();
            int value = p.getScore();
            String str = padRight(key,10) + " = " + padLeft(new Integer(value).toString(),5);

            scores.add(str);
        }
        
        return scores;
    }
	
	public ArrayList<String> playerPrint(HashMap<String, NukePerson> players, String player) {
		ArrayList<String> scores = new ArrayList<String>();
		
		scores.add("Defcon scores for " + player + ":");		
		NukePerson p = players.get(player);

		scores.add(p.getName() + " (" + p.getCountry().getName() + ") = " + p.getScore());

		scores.add("Cities:");
		
		for (int k = 0; k < p.getCountry().getCities().size(); k++) {
		    City c = p.getCountry().getCities().get(k);
		    scores.add("   " + padRight(c.getName() + ":", 15) + padLeft(new Double(c.getPop() / 10.0).toString()+"M", 5));
		}

		scores.add("History:");

		for (int l = 0; l < p.getHistory().size(); l++) {
		    scores.add("   "+p.getHistory().get(l));
		}
		
		return scores;
	}
}
