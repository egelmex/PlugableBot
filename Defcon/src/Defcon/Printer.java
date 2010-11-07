 package Defcon;

import java.util.*;
import java.io.*;

public class Printer
{

	private static final String xmlHeader = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
	private static final String xmlStyle = "<?xml-stylesheet type=\"text/xsl\" href=\"MYSTYLE\"?>\n";
	private static final String xmlFooter = "";
	
	private static final String CONFIG_FILE = "defcon.ini";
	
	private String outDir = "";
        
    /**
     * Constructor for objects of class Printer
     */
    public Printer()
    {
        Properties p = new Properties();
		try {
			p.load(new FileInputStream(new File(CONFIG_FILE)));
			}
			catch (Exception e) {
			
			}
		outDir = p.getProperty("outputDirectory") + "//";
    }
	
	public static String padLeft(String s, int n) {
		return String.format("%1$#" + n + "s", s);  
	}
	public static String padRight(String s, int n) {
		return String.format("%1$-" + n + "s", s);  
	}
	
	private void writeXML(String filename, String xml, String stylesheet) {
		File f = new File(outDir + filename);
        try {
            FileWriter fw = new FileWriter(f);
            fw.write(xmlHeader + (stylesheet.equals("") ? "" : xmlStyle.replaceAll("MYSTYLE", stylesheet)) + xml + xmlFooter);
            fw.close();
        }
        catch (Exception e) {
            Defcon.logger.log("wXML", "ERROR: " + e.toString());
        }
	}
	
	public void writeIndex(HashMap<String, NukePerson> players) {
		String xml = "<players>";
	
		for (Map.Entry<String, NukePerson> entry : players.entrySet()) {
            String key = entry.getKey();
			NukePerson p = entry.getValue();
			xml += "<player><name>" + p.getDisplayName() + "</name></player>";
            writePerson(p);
        }
		
		xml += "</players>";
		writeXML("index.xml", xml, "index.xsl");
	}
	
	public void writePerson(NukePerson p) {
		writeXML(p.getDisplayName() + ".xml", p.toXML(), "person.xsl");
	}

    public ArrayList<String> simplePrint(HashMap<String, NukePerson> players)
    {
        ArrayList<String> scores = new ArrayList<String>();
        
		scores.add("Defcon scores:");
		
        for (Map.Entry<String, NukePerson> entry : players.entrySet()) {
            String key = entry.getKey();
			NukePerson p = entry.getValue();
            int value = p.getScore();
            String str = padRight(p.getDisplayName(),10) + " = " + padLeft(new Integer(value).toString(),5);

            scores.add(str);
        }
        
        return scores;
    }
	
	public ArrayList<String> playerPrint(HashMap<String, NukePerson> players, String player) {
		return null;
		/*
		ArrayList<String> scores = new ArrayList<String>();
		
		scores.add("Defcon scores for " + player + ":");		
		NukePerson p = players.get(player);

		scores.add(p.getName() + " (" + p.getCountry().getName() + ") = " + p.getScore());

		scores.add("Cities:");
		
		for (int k = 0; k < p.getCountry().getCities().size(); k++) {
		    City c = p.getCountry().getCities().get(k);
		    scores.add("   " + padRight(c.getName() + ":", 15) + padLeft(new Double(c.getPop() / 10.0).toString()+"M", 5));
		}

		scores.add("History:");

		for (int l = 0; l < p.getHistory().size(); l++) {
		    scores.add("   "+p.getHistory().get(l));
		}
		
		return scores;*/
	}
}
