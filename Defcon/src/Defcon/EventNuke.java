 package Defcon;

public class EventNuke extends Event
{
    private String fplr, tplr, ftgt, ttgt, killed;

    public EventNuke(String fplr, String tplr, String ftgt, String ttgt, String killed)
    {
        this.fplr = fplr;
        this.tplr = tplr;
        this.ftgt = ftgt;
        this.ttgt = ttgt;
        this.killed = killed;
    }
    
    public String toXML() {
        return "<event type=\"Nuke\"><nuker>" + this.fplr + "</nuker><nukee>" + this.tplr + "</nukee><from>" + this.ftgt + "</from><to>" + this.ttgt + "</to><killed>" + this.killed + "</killed></event>";
    }
}
