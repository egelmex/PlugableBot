package Defcon;

import java.util.*;
import java.util.logging.Logger;

public class NukePerson
{
    private String name;
    private int score;
    private Country c;
    //private int nukesLeft;
    private History history;
    private int selfthreshold;
    private long lastNuke;
    private static final Logger LOG = Logger.getLogger(Defcon.class.getCanonicalName());
    
    public NukePerson(String name)
    {
        this.name = name;
        this.score = 0;
    }
    
    public void reset(Country c) {
		this.c = c;
		this.history = new History();
		this.score = 0;
		this.selfthreshold = 0;
		this.lastNuke = 0;
		//this.nukesLeft = 30;
    }
	
	public String toXML() {
		String xml = "";
		xml += "<player><name>" + this.name + "</name><score>" + this.score + "</score>";
		xml += c.toXML();
		//xml += "<nukes>" + this.nukesLeft + "</nukes>";
		xml += history.toXML();
		xml += "</player>";
		return xml;
	}

    public int getThreshold()
    {
    	return this.selfthreshold;
    }

    public void addHistory(Event e)
    {
    	this.history.addEvent(e);
    }

    public History getHistory()
    {
    	return this.history;
    }
    
    public int getScore() {
        return this.score;
    }
    
    public String getName() {
        return this.name.toLowerCase();
    }
    
    public String getDisplayName() {
        return this.name;
    }
    
    public void addToScore(int amount) {
        score += amount;
    }
    
    public void removeFromScore(int amount) {
        score -= amount;
    }
    
    public Country getCountry()
    {
        return this.c;
    }
    
    public boolean hasANuke()
    {
        for (City c : getCountry().getCities()) { //TODO: All Nuking can nuke
            if (c.hasNuke()) {
                //c.fireNuke();
                return true;
            }
        }
        return false;
        
        /*if (this.nukesLeft > 0)
        {
            this.nukesLeft--;
            return true;
        }
        else {
            return false;
        }*/
    }
        
    public void updateThreshold()
    {
    	long millis = System.currentTimeMillis();
    	long difftime = millis - lastNuke;
    	Defcon.logger.log("Df", difftime + ", LN: " + lastNuke + ", MS: " + millis);
    	lastNuke = millis;
    	
    	if (difftime <= (60*1000)) { this.selfthreshold += 20; }
    	else if (difftime <= (120*1000)) { this.selfthreshold += 10; }
    	else if (difftime <= (180*1000)) { this.selfthreshold += 5; }
    	else if (difftime <= (300*1000)) { this.selfthreshold += 3; }
    	else if (difftime <= (600*1000)) { this.selfthreshold -= 20; }
		else if (difftime <= (900*1000)) { this.selfthreshold -= 50; }
    	else if (difftime <= (1800*1000)) { this.selfthreshold -= 100; }
    	else if (difftime <= (3600*1000)) { this.selfthreshold -= 100; }
    	else { this.selfthreshold -= 50; }
    
    	if (this.selfthreshold > 100)
    	    {
    		this.selfthreshold = 100;
    	    }
    	else if (this.selfthreshold < 0) {
    	    this.selfthreshold = 0;
    	}
    }
}
