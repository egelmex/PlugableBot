package Defcon;

import java.util.Random;

import java.util.logging.Logger;

public class Nuker
{
    private static Random randomer = new Random();
    private static int MIN_DAMAGE = 10;
    private static int MAX_DAMAGE = 75;

    private static final Logger LOG = Logger.getLogger(Defcon.class.getCanonicalName());

    public Nuker()
    {
        //sanity check
        if (MAX_DAMAGE <= MIN_DAMAGE) {
              MAX_DAMAGE = 60;
              MIN_DAMAGE = 10;
        }
    }

    public static String padRight(String s, int n) {
       	return String.format("%1$-" + n + "s", s);  
    }

    public String nuke(NukePerson from, NukePerson to) {
        if (from.fireNuke()) {

	    int oldT = from.getThreshold();
	    from.updateThreshold();
	    int newT = from.getThreshold();
	    int perc = randomer.nextInt(100);

	    LOG.info("OT: " + oldT + "%, NT: " + newT + "%, PER: " + perc);

	    boolean hitSelf = false;
	    City target;

	    if (perc <= from.getThreshold()) {
		target = from.getCountry().getValidTarget(randomer.nextInt(100));
		hitSelf = true;
	    }
	    else {
		target = to.getCountry().getValidTarget(randomer.nextInt(100));
	    }

            //A nuke will hit between MIN% and MAX% of a city's population
            int currentpop = target.getPop();
            int damage = target.decrPop((currentpop * (randomer.nextInt(MAX_DAMAGE-MIN_DAMAGE)+MIN_DAMAGE)) / 100);
            double killed = damage / 10.0;
	    LOG.info("D: " + damage + ", K: " + killed);

	    if (hitSelf) {
		from.addHistory(padRight(from.getName(), 10) + " nuke's self destructed in " + padRight(target.getName(), 15) + " killing " + killed + "M");
		if (from == to) {
		    from.removeFromScore(damage);
		    to.removeFromScore(damage);
		}
		else {
		    from.removeFromScore(damage);
		    to.addToScore(damage);
		}

		return "Self Destruct! " + target.getName() + " ("+from.getName()+") hit! " + killed + "M dead.";
	    }
	    else {
		from.addHistory(padRight(from.getName(), 10) + " nuked " + padRight(target.getName() + " (" + to.getName() + ")", 25) + " killing " + killed + "M");
		to.addHistory(padRight(from.getName(), 10) + " nuked " + padRight(target.getName() + " (" + to.getName() + ")", 25) + " killing " + killed + "M");

		//Update scores
		if (from == to) {
		    from.removeFromScore(damage);
		    to.removeFromScore(damage);
		}
		else {
		    from.addToScore(damage);
		    to.removeFromScore(damage);
		}
            
		return from.getName() + " -> " + to.getName() + ": " + target.getName() + " hit! " + killed + "M dead.";
	    }
        }
        else {
            return from.getName() + "'s silo is empty!";
        }
    }
}
