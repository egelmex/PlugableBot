/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Andrew
 */
public class MarkovNode implements Comparable<MarkovNode> {
    
    public int getConnectionCount()
    {
        return occurances.size();
    }
    
    private String word;
    private List<MarkovNode> children;
    private HashMap<MarkovNode, Integer> occurances;
    private static Random r = new Random();
    
    public List<MarkovNode> getChildren()
    {
        return children;
    }
    
    public HashMap<MarkovNode, Integer> getOccuranceTable()
    {
        return occurances;    
    }
    
    /** Creates a new instance of MarkovNode */
    public MarkovNode(String word) {
        this.word = word;
        children = new ArrayList<MarkovNode>();
        occurances = new HashMap<MarkovNode, Integer>();
    }
    
    public boolean AddChild(MarkovNode n)
    {
        if (!occurances.containsKey(n))
        {
            occurances.put(n, 1);
            children.add(n);
        }
        else
        {
            int old = occurances.get(n);
            int newW = old + 1;
            
            // update the occarances
            occurances.put(n, newW);
            
            if (log2(newW) > log2(old))
            {
                children.add(n);
            }
        }
        return true;
    }
    
    public MarkovNode GetRandomNode()
    {
        if (children.size() > 0)
            return children.get(r.nextInt(children.size()));
        else return null;
    }

    public int compareTo(MarkovNode o) {
        return word.compareTo(o.word);
    }
    
    public String getWord()
    {
        return word;
    }
    
    private int log2(int x)
    {
        return (int) Math.ceil(Math.log(x)/Math.log(2));
    }
}