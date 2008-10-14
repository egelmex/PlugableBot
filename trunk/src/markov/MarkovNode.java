/*
 * MarkovNode.java
 *
 * Created on 06 October 2007, 15:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package markov;


import java.util.*;
/**
 *
 * @author Administrator
 */
public class MarkovNode implements Comparable<MarkovNode> {
    
    public int getConnectionCount()
    {
        return children.size();
    }
    
    private String word;
    private List<MarkovNode> children;
    private static Random r = new Random();
    
    public List<MarkovNode> getChildren()
    {
        return children;
    }
    
    /** Creates a new instance of MarkovNode */
    public MarkovNode(String word) {
        this.word = word;
        children = new ArrayList<MarkovNode>();
    }
    
    public boolean AddChild(MarkovNode n)
    {
	if (!children.contains(n))
        {
            children.add(n);
            return true;
        }
        return false;
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
}
