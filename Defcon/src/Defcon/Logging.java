package Defcon;

import java.util.*;
import java.util.logging.*;
import java.io.*;

public class Logging
{
    private static final Logger LOG = Logger.getLogger(Defcon.class.getCanonicalName());

    public Logging()
    {
		try {
			FileHandler fh = new FileHandler("defcon.log", true);
			LOG.addHandler(fh);
			LOG.setLevel(Level.ALL);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
		}
		catch (Exception e)
		{	
			LOG.info("DEFCON LOG FAIL");//fail
		}
    }
    
    public static String padRight(String s, int n) {
		return String.format("%1$-" + n + "s", s);  
	}
    
    public static void log(String id, String s) {
        LOG.info(padRight(id, 10) + ":" + s);
    }
}
