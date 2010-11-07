package Defcon; 

import java.util.*;
import java.io.*;

public class History
{
    private ArrayList<Event> events;
    
    public History()
    {
        events = new ArrayList<Event>();
    }
    
    public String toXML()
    {
        String xml = "<history>";
        for (Event e : events) {
            xml += e.toXML();
        }
        xml += "</history>";
        return xml;
    }
    
    public void addEvent(Event e)
    {
        this.events.add(e);
    }
}
