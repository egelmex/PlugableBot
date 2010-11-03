package Defcon;

import java.util.ArrayList;

public class Country
{
    private String name;
    private ArrayList<City> cities;
    private boolean used;

    public Country(String name, ArrayList<City> cities)
    {
        this.name = name;
        this.cities = cities;
        this.used = false;
    }
    
    public String getName()
    {
        return this.name;
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
    
    public City getValidTarget(int start)
    {
        start = start % cities.size();
        int icity = 0;
        for (int i = 0; i < cities.size(); i++) {
            icity = start % cities.size();
            if (cities.get(icity).getPop() > 1) {
                return cities.get(icity);
            }
            start++;
        }
        return cities.get(icity);
    }

}
