package Defcon;

public abstract class Nuking extends Target
{
    String name;
    abstract String toXML();
    abstract String getName();
	abstract void fireNuke();
	abstract boolean hasNuke();
	
}
