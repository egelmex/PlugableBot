package Markov2;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import java.util.TimerTask;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Timer;

/**
 *
 * @author Andrew
 */
public class MarkovString extends TimerTask {
    
    // needed to save the changes to the graph without causing a performance hit
    private Timer t = new Timer();
    // the database file
    private ObjectContainer database;
    // a list of updated nodes that need saving
    private LinkedList<MarkovNode> updated = new LinkedList<MarkovNode>();
    // a list of cahced nodes for fact lookup
    private HashMap<String, MarkovNode> cache = new HashMap<String, MarkovNode>();

    public MarkovString()
    {
        // set up indexing
        Db4o.configure().objectClass(MarkovNode.class).objectField("word").indexed(true);
        // open the database file
        database = Db4o.openFile("Markov2");
        // get a list of all nodes
        ObjectSet<MarkovNode> set = database.get(MarkovNode.class);
        // if we dont have any, we have an empty database and need to start learning
        if(set.size() == 0)
        {
            MarkovNode tmp = new MarkovNode("[");
            database.set(tmp);
            MarkovNode tmp2 = new MarkovNode("]");
            database.set(tmp2);
            updated.add(tmp);
            updated.add(tmp2);
            
            cache.put("[", tmp);
            cache.put("]", tmp2);
        }
        // schedule the saves for 5 minute intervals
        t.schedule(this, 0, 60000);
    }
    
    public int getWordCount()
    {
        return database.get(MarkovNode.class).size();
    }
    
    public int getConnectionCount()
    {
        int ret = 0;
        ObjectSet<MarkovNode> set = database.get(MarkovNode.class);
        for (MarkovNode n : set)
            ret += n.getConnectionCount();
        return ret;
    }
    
    public String Generate()
    {
        StringBuffer sb = new StringBuffer();
        // get the beginning node
        MarkovNode current = getNode("[");
        // loop through until we hit the end
        while (!current.getWord().equals("]"))
        {
            // get a random next node
            MarkovNode newNode = current.GetRandomNode();
            // if its null we need to add a new join to the end
            if (newNode == null)
            {
                MarkovNode end = getNode("]");
                current.AddChild(end);
                if (!updated.contains(current))
                    updated.add(current);
                newNode = end;
            }
            current = newNode;
            
            // append the word at the new nodes
            sb.append(current.getWord());
            // append a space
            sb.append(" ");
        }
        // return the whole string
        return sb.toString().replace("]", " ").trim();
    }
    
    public void Learn(String sentence)
    {
        StringBuffer sb = new StringBuffer();
        // remove anything we dont really understand
        for (char c :  sentence.toCharArray())
        {
            if (Character.isWhitespace(c))
                sb.append(" ");
            else if (Character.isLetterOrDigit(c))
                sb.append(Character.toLowerCase(c));
        }
        // split into a list of words
        String[] words = sb.toString().split(" ");
        // always need to know what the last word was
        MarkovNode parent = getNode("[");
        for (String word : words)
        {
            // if the word is blank, ignore it
            if (word.trim().equals("")) continue;
            // get the word from the database if we already have it
            MarkovNode n;
            MarkovNode query = getNode(word);
            if (query == null)
            {
                // if we dont have it, add it
                n = new MarkovNode(word);
                cache.put(word, n);
                database.set(n);
            }
            else
            {
                n = query;
            }

            // add to the parent node
            parent.AddChild(n);
            if (updated.contains(parent))
                updated.add(parent);
            
            // move to the next node
            parent = n;
        }

        // not sure why this'd ever be null ...
        if (parent != null)
        {
            // add the end marker at the end
            parent.AddChild(getNode("]"));
            if (updated.contains(parent))
                updated.add(parent);
        }
    }
    
    private MarkovNode getNode(String word)
    {
        if (cache.containsKey(word))
        {
            return cache.get(word);
        }
        else
        {
            ObjectSet<MarkovNode> query = database.get(new MarkovNode(word, true));
            if (query.size() == 0)
                return null;
            else
            {
                MarkovNode n = query.get(0);
                cache.put(n.getWord(), n);
                return n;
            }
        }
    }
    
    public void run()
    {
        if (database != null && updated.size() > 0)
        {
            for (MarkovNode n : updated)
                database.set(n);

            updated.clear();
            database.commit();
        }
    }
    
    @Override
    protected void finalize() throws Throwable
    {
        try {
            t.cancel();
            run();            
            database.close();
        } finally {
            super.finalize();
        }
    }
}
