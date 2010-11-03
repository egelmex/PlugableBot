package Defcon;

public class City
{
    private String name;
    private int population; //1 = 0.1M, 10 = 1M, 100 = 10M

    public City(String name, int population)
    {
        this.name = name;
        this.population = population;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public int getPop()
    {
        return this.population;
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