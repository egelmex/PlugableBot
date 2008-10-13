package Markov2;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 

import java.util.Map;
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
    private ArrayMap4<MarkovNode, Integer> occurances;
    private static Random r = new Random();
    
    public List<MarkovNode> getChildren()
    {
        return children;
    }
    
    public Map<MarkovNode, Integer> getOccuranceTable()
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
            occurances = new ArrayMap4<MarkovNode, Integer>();            
        }
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
    
    @Override
    public int hashCode()
    {
        return word.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MarkovNode other = (MarkovNode) obj;
        if (this.word != other.word && (this.word == null || !this.word.equals(other.word))) {
            return false;
        }
        return true;
    }
}