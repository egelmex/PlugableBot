package Markov2;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.Random;

import com.db4o.collections.ArrayList4;
import com.db4o.collections.ArrayMap4;


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
    private ArrayList4<MarkovNode> children;
    private ArrayMap4<String, Integer> occurances;
    private transient static Random r = new Random();
    
    public List<MarkovNode> getChildren()
    {
        return children;
    }
    
    public Map<String, Integer> getOccuranceTable()
    {
        return occurances;    
    }
    
    /** Creates a new instance of MarkovNode */
    public MarkovNode(String word) {
        this(word, false);
    }
    
    public MarkovNode(String word, boolean search)
    {
        this.word = word;
        if (!search)
        {
            children = new ArrayList4<MarkovNode>();
            occurances = new ArrayMap4<String, Integer>();            
        }
    }
    
    public boolean AddChild(MarkovNode n)
    {
        if (!occurances.containsKey(n.word))
        {
            occurances.put(n.word, 1);
            children.add(n);
        }
        else
        {
            int old = occurances.get(n.word);
            int newW = old + 1;
            
            // update the occarances
            occurances.put(n.word, newW);
            
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