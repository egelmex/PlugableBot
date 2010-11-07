 package Defcon;

public class EventSelfDestruct extends Event
{
    private String plr, tgt, killed;

    public EventSelfDestruct(String plr, String tgt, String killed)
    {
        this.plr = plr;
        this.tgt = tgt;
        this.killed = killed;
    }
    
    public String toXML() {
        return "<event type=\"SelfDestruct\"><nuker>" + this.plr + "</nuker><to>" + this.tgt + "</to><killed>" + this.killed + "</killed></event>";
    }
}