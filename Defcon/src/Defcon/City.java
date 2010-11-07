 package Defcon;

public class City extends Nuking
{
    private String name;
    private int population; //1 = 0.1M, 10 = 1M, 100 = 10M
	private int originalPop;
	private int silos;

    public City(String name, int population, int silos)
    {
        this.name = name;
		this.originalPop = population;
        this.population = population;
		this.silos = silos;
    }
	
	public boolean canFireNukes()
	{
		return true;
	}
	
	public String hPop(int pop)
	{
		return "" + new Double(pop / 10.0) + "M";
	}
    
    public String getName()
    {
       	   return this.name;
    }
	
	public String toXML() {
		String xml = "";
		xml += "<city><name>" + this.name + "</name><population>" + hPop(this.population) + "</population><dead>" + hPop(this.originalPop - this.population) + "</dead><nukes>" + this.silos + "</nukes></city>";
		return xml;
	}
    
    public int getPop()
    {
        return this.population;
    }
    
    public boolean hasNuke()
    {
        return (this.silos > 1);
    }
    
    public void fireNuke()
    {
        this.silos--;
    }
	
	public boolean isValidTarget()
	{
		if (this.population > 1) {
			return true;
		}
		return false;
	}
    
    public int decrPop(int amount)
    {
        if ((getPop() - amount) <= 1) {
            int actual = getPop() - 1;
            this.population = 1;
            return actual;
        }
        else {
            this.population -= amount;
            return amount;
        }
    }

}