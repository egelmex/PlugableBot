 package Defcon;

import java.util.ArrayList;

public class Country
{
    private String name;
    private ArrayList<City> cities;
    private ArrayList<Target> targets;
    private boolean used;

    public Country(String name, ArrayList<City> cities)
    {
		targets = new ArrayList<Target>();
        this.name = name;
        this.cities = cities;
        for (City c : cities) {
            targets.add((Target) c);
        }
        this.used = false;
    }
    
    public String getName()
    {
        return this.name;
    }
	
	public String toXML() {
		String xml = "";
		xml += "<country><name>" + this.name + "</name><cities>";
		for (City c : cities) {
			xml += c.toXML();
		}
		xml += "</cities></country>";
		return xml;
	}
    
    public boolean isUsed()
    {
        return this.used;
    }
    
    public void setUsed()
    {
        this.used = true;
    }
    
    public ArrayList<City> getCities()
    {
        return this.cities;
    }
    
    public ArrayList<Target> getValidTargets()
    {
        return this.targets;
    }
	
	public Nuking getValidNuker(int start)
	{
		start = start % cities.size();
        int icity = 0;
        for (int i = 0; i < cities.size(); i++) {
            icity = start % cities.size();
            if (cities.get(icity).isValidTarget()) {
                return cities.get(icity);
            }
            start++;
        }
        return cities.get(icity);
	}
    
    //TO DO - ABSTRACT TO TARGETS
    public Target getValidTarget(int start)
    {
        start = start % cities.size();
        int icity = 0;
        for (int i = 0; i < cities.size(); i++) {
            icity = start % cities.size();
            if (cities.get(icity).isValidTarget()) {
                return cities.get(icity);
            }
            start++;
        }
        return cities.get(icity);
    }

}
