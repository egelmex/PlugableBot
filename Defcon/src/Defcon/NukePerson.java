package Defcon;

import java.util.*;


import java.util.logging.Logger;

public class NukePerson
{
    private String name;
    private int score;
    private Country c;
    private int nukesLeft;
    private ArrayList<String> history;
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
	this.history = new ArrayList<String>();
        this.score = 0;
	this.selfthreshold = 0;
	this.lastNuke = 0;
        this.nukesLeft = 5;
    }

    public void updateThreshold()
    {
	long millis = System.currentTimeMillis();
	long difftime = millis - lastNuke;
	LOG.info("Df: " + difftime + ", LN: " + lastNuke + ", MS: " + millis);
	lastNuke = millis;
	if (difftime <= (60*1000)) {
	    this.selfthreshold += 50;
	}
	else if (difftime <= (180*1000)) {
	    this.selfthreshold += 20;
	}
	else if (difftime <= (300*1000)) {
	    this.selfthreshold += 10;
	}
	else if (difftime <= (600*1000)) {
	    //this.selfthreshold -= 50;
	}
	else if (difftime <= (1800*1000)) {
	    this.selfthreshold -= 10;
	}
	else if (difftime <= (60*1000)) {
	    this.selfthreshold -= 20;
	}
	else {
	    this.selfthreshold -= 50;
	}

	if (this.selfthreshold > 99)
	    {
		this.selfthreshold = 99;
	    }
	else if (this.selfthreshold < 1) {
	    this.selfthreshold = 1;
	}
    }

    public int getThreshold()
    {
	return this.selfthreshold;
    }

    public void addHistory(String s)
    {
	this.history.add(s);
    }

    public ArrayList<String> getHistory()
    {
	return this.history;
    }
    
    public int getScore() {
        return this.score;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void addToScore(int amount) {
        //if (score <= 0)
        //    return;
        //}
        score += amount;
    }
    
    public void removeFromScore(int amount) {
        score -= amount;
    }
    
    public Country getCountry()
    {
        return this.c;
    }
    
    public boolean fireNuke()
    {
        if (this.nukesLeft > 0)
        {
            this.nukesLeft--;
            return true;
        }
        else {
            return false;
        }
    }
    
    /*public boolean isDead() {
        if (score <= 0) {
            return true;
        }
        else {
            return false;
        }
    }*/

}
