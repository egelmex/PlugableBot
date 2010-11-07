package Defcon;

import java.util.Random;

public class Nuker
{
    private static Random randomer = new Random();
    private static int MIN_DAMAGE = 10;
    private static int MAX_DAMAGE = 75;

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
    
    public void updateThreshold(NukePerson from)
    {
        int oldT = from.getThreshold();
        from.updateThreshold();
        int newT = from.getThreshold();
        
        Defcon.logger.log("--Thre", "Old: " + oldT + "%, Now: " + newT);
    }
    
    public boolean performSDtest(NukePerson from)
    {
        int perc = randomer.nextInt(100) + 1;
		Defcon.logger.log("--Test", ""+perc+"%");
        return (perc <= from.getThreshold());
    }
    
    public Target chooseTarget(NukePerson p)
    {
        return p.getCountry().getValidTarget(randomer.nextInt(10000));
    }
	
	public Nuking chooseNuker(NukePerson p)
	{
		/*Target t = null;
		while (!(t instanceof Nuking)) {
			t = p.getCountry().getValidTarget(randomer.nextInt(10000));
		}
		return (Nuking) t;*/
		return p.getCountry().getValidNuker(randomer.nextInt(10000));
	}
    
    public int genDamage(City target) {
        int currentpop = target.getPop();
		int perkill = (randomer.nextInt(MAX_DAMAGE-MIN_DAMAGE)+MIN_DAMAGE);
		Defcon.logger.log("--Dmg", ""+perkill+"% Pop killed: " + (currentpop * perkill) / 100);
        int damage = target.decrPop((currentpop * perkill) / 100);
		Defcon.logger.log("--Act", ""+damage);
		return damage;
    }
    
    public void nukeEvent(NukePerson pfrom, NukePerson pto, Target tfrom, Target tto, int killed)
    {
        //TODO: MAKE A PROPER FROM VALID TARGET
        EventNuke e = new EventNuke(pfrom.getDisplayName(), pto.getDisplayName(), tfrom.getName(), tto.getName(), ""+(killed / 10.0)+"M");
        pfrom.addHistory(e);
        pto.addHistory(e);
    }
    
    public void nukeSelfEvent(NukePerson p, Target t, int killed)
    {
        //TODO: MAKE A PROPER FROM VALID TARGET
        EventSelfDestruct e = new EventSelfDestruct(p.getDisplayName(), t.getName(), ""+(killed / 10.0)+"M");
        p.addHistory(e);
    }
    
    public void hitSelfScore(NukePerson p, int d) {
        p.removeFromScore(d);
    }
    
    public void hitScore(NukePerson from, NukePerson to, int d) {
        from.addToScore(d*2);
        to.removeFromScore(d);
    }

    public String nuke(NukePerson from, NukePerson to, Target t) {
		Defcon.logger.log("Nuke", "from=" + from.getDisplayName() + " to=" + to.getDisplayName() + " self=" + (from==to));
	
		if (from == to) {
			return to.getDisplayName() + " tried to nuke himself... the twat!";
		}
	
        if (from.hasANuke()) {
            updateThreshold(from);
            boolean hitSelf = performSDtest(from);
            Target target = t;
            Nuking tfrom = chooseNuker(from);
			
			/*while(!tfrom.hasNuke())
			{
				tfrom = chooseNuker(from);
			}*/
            
            tfrom.fireNuke();
			
            
            if (hitSelf) {
                target = tfrom;
            }
            else {
                if (t == null) {
                    target = chooseTarget(to);
                }
            }
			
			Defcon.logger.log("NukeNull", "isTargetNull="+(target==null));
            
			if (target instanceof City) {
				City c = (City) target;
			
				int damage = genDamage(c);
				
				double killed = damage / 10.0;
				
				if (hitSelf) {
					hitSelfScore(from, damage);
					nukeSelfEvent(from, tfrom, damage);
					return "Self Destruct! " + tfrom.getName() + " hit! " + killed + "M dead.";
				}
				else {
					hitScore(from, to, damage);
					nukeEvent(from, to, tfrom, c, damage);
					return tfrom.getName() + " -> " + c.getName() + " hit! " + killed + "M dead.";
				}
			}
			else {
				Defcon.logger.log("nuke", "ERROR: NO INSTANCEOF MATCHES");
			}
        }
        else {
            return from.getDisplayName() + "'s silos are empty!";
        }
		
		return "Wow. This went wrong.";
    }
}
