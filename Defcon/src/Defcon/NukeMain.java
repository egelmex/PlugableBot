 package Defcon;

import java.util.ArrayList;
import java.util.Arrays;  
import java.util.List;
import java.util.Random;
import java.util.HashMap;

public class NukeMain
{
    private static Random randomC = new Random();
    private ArrayList<Country> countries = new ArrayList<Country>();
    
    private HashMap<String, NukePerson> players = null;
    private Nuker nuker;
    
    private static final int MAX_COUNTRY_POP = 300; //30M
    private static final int[] modifiers = {4,2,2,1,1}; //NOTE: MUST TOTAL MAX_TOTAL
    private static final int MAX_TOTAL = 10;
    private static final int[] pops = {120, 70, 50, 40, 20};
    
    private String cityList = "Afghanistan:Kabul,Kandahar,Herat,Jalalabad,Lashgar Gah"+"\n"+
                                "Argentina:Buenos Aires,Cordoba,Rosario,Mendoza,Santa Fe"+"\n"+
                                "Australia:Sydney,Melbourne,Brisbane,Perth,Adelaide"+"\n"+
                                "Brazil:Sao Paulo,Rio de Janeiro,Salvador,Brasilia,Fortaleza"+"\n"+
                                "Canada:Toronto,Montreal,Calgary,Winnipeg,Vancouver"+"\n"+
                                "China:Shanghai,Beijing,Hong Kong,Tianjin,Guangzhou"+"\n"+
                                "Egypt:Cairo,Alexandria,Giza,Suez,Rosetta"+"\n"+
                                "France:Paris,Marseille,Lyon,Toulouse,Strasbourg"+"\n"+
                                "Germany:Berlin,Hamburg,Munich,Cologne,Frankfurt am Main"+"\n"+
                                "Greece:Athens,Thessaloniki,Heraklion,Larissa,Rhodes"+"\n"+
                                "India:Delhi,Mumbai,Kolkata,Bangalore,Bhopal"+"\n"+
                                "Iran:Tehran,Tabriz,Qom,Mashhad,Shiraz"+"\n"+
                                "Iraq:Baghdad,Mosul,Basra,Kirkuk,Fallujah"+"\n"+
                                "Ireland:Dublin,Cork,Limerick,Belfast,Galway"+"\n"+
                                "Israel:Jerusalem,Tel Aviv,Haifa,Ashdod,Acre"+"\n"+
                                "Italy:Rome,Milan,Naples,Turin,Florence"+"\n"+
                                "Japan:Tokyo,Osaka,Kobe,Kyoto,Hiroshima"+"\n"+
                                "Mexico:Mexico City,Tijuana,Guadalajara,Ciudad Juarez,Chihuahua"+"\n"+
                                "Netherlands:Amsterdam,Rotterdam,The Hague,Utrecht,Eindhoven"+"\n"+
                                "North Korea:Pyongyang,Hamhung,Chongjin,Nampo,Wonsan"+"\n"+
                                "Pakistan:Karachi,Lahore,Peshawar,Islamabad,Gujrat"+"\n"+
                                "Russia:Moscow,St. Petersburg,Volgograd,Rostov,Saratov"+"\n"+
                                "South Korea:Seoul,Busan,Incheon,Daegu,Daejeon"+"\n"+
                                "Spain:Madrid,Barcelona,Valencia,Seville,Malaga"+"\n"+
                                "Turkey:Ankara,Istanbul,Izmir,Konya,Antalya"+"\n"+
                                "United Kingdom:London,Birmingham,Manchester,Edinburgh,Cardiff"+"\n"+
                                "Vietnam:Ho Chi Minh City,Hanoi,Haiphong,Dalat,Danang";

    public NukeMain()
    {
        nuker = new Nuker();
        String[] cntS = cityList.split("\n");
        for (int i = 0; i < cntS.length; i++) {
            String cName = cntS[i].split(":")[0];
            String[] ctyS = cntS[i].split(":")[1].split(",");
            
//System.out.println(cName + " " + cntS[i].split(":")[1]);
            
            Country cs = new Country(cName,makeCities(ctyS));
            countries.add(cs);
        }
        
        players = new HashMap<String, NukePerson>();
		//delete all xml files so that old matches arent included
    }
    
    public ArrayList<String> simpleprint()
    {
        Printer p = new Printer();
        return p.simplePrint(players);
    }

    public ArrayList<String> playerPrint(String player) {
    	Printer p = new Printer();
    	if (isNewPlayer(player)) {
    	    return null;
    	}
    	return p.playerPrint(players, player);
    }
    
    public String nuke(String from, String to) {
        NukePerson pfrom = getPlayer(from);
        NukePerson pto = getPlayer(to);
		String s = nuker.nuke(pfrom, pto, null);
		Printer p = new Printer();
		p.writePerson(pfrom);
		p.writePerson(pto);
        return s;
    }
    
    public String nukeTarget(String from, String to) {
        NukePerson pfrom = getPlayer(from);
        NukePerson pto = targetsPlayer(to);
        Target t = getTarget(to);
		
		Defcon.logger.log("nT", from + " " + to + " " + pto.getDisplayName() + " b" + (pfrom==pto) + " " + t.getName());
		
        String s = nuker.nuke(pfrom, pto, t);
        Printer p = new Printer();
		p.writePerson(pfrom);
		p.writePerson(pto);
        return s;
    }
    
    public NukePerson targetsPlayer(String target) {
        for (NukePerson np : players.values()) {
            for (Target t : np.getCountry().getValidTargets()) {
                if (t.getName().equals(target)) {
                    return np;
                }
            }
        }
		return null;
    }
    
    public Target getTarget(String target)
    {
        for (NukePerson np : players.values()) {
            for (Target t : np.getCountry().getValidTargets()) {
                if (t.getName().equals(target)) {
                    return t;
                }
            }
        }
        return null;
    }
    
    public boolean isValidTarget(String target)
    {
        for (NukePerson np : players.values()) {
            for (Target t : np.getCountry().getValidTargets()) {
                if (t.getName().equals(target)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean isNewPlayer(String player)
    {
        return !players.containsKey(player.toLowerCase());
    }
    
    public NukePerson getPlayer(String player)
    {
        return players.get(player.toLowerCase());
    }
    
    public boolean newPlayer(String player)
    {
        NukePerson np = new NukePerson(player);
        Country c = allocateCountry();
        if (c != null) {
            np.reset(c);
            players.put(player.toLowerCase(), np);
			Printer p = new Printer();
			p.writeIndex(this.players);
			//p.writePerson(np);
            return true;
        }
        else {
            return false;
        }
    }
    
    public String getRandomCity()
    {
        int cnty = randomC.nextInt(countries.size());
        return countries.get(cnty).getCities().get(randomC.nextInt(countries.get(cnty).getCities().size())).getName();
    }
    
    public ArrayList<City> makeCities(String[] names)
    {
        ArrayList<City> cities = new ArrayList<City>();
        for (int i = 0; i < names.length; i++) {
            int cityPop = makePop(i);
           // System.out.println(names[i] + " " + cityPop);
            cities.add(new City(names[i], cityPop, 5));
        }
        return cities;
    }
    
    public int makePop(int cityNum)
    {
        return pops[cityNum];
    }
    
    public Country allocateCountry()
    {
        int start = randomC.nextInt(countries.size());
        int ic = 0;
        
        for (int i = 0; i < countries.size(); i++) {
            ic = start % countries.size();
            if (countries.get(ic).isUsed() == false) {
                countries.get(ic).setUsed();
                return countries.get(ic);
            }
            start++;
        }
        return null; //Couldnt find an empty one
    }
}