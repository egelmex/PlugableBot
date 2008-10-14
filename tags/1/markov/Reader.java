/*
 * Reader.java
 *
 * Created on 11 October 2007, 19:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package markov;

import java.io.*;
import java.util.*;
/**
 *
 * @author Administrator
 */
public class Reader {
    
    public static void main(String[] args)
    {
        try
        {
            MarkovString s = new MarkovString();
            FileReader fr = new FileReader(args[0]);
            BufferedReader br = new BufferedReader(fr);
            String line;
            
            while ((line = br.readLine()) != null)
                s.Learn(line);
            
            s.Save();
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage());
        }
        System.exit(0);
    }    
}
