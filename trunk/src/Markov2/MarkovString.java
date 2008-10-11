package Markov2;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import java.util.TimerTask;
//import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;

/**
 *
 * @author Andrew
 */
public class MarkovString extends TimerTask {
    
    private Timer t = new Timer();
    private ObjectContainer database;
    private LinkedList<MarkovNode> updated = new LinkedList<MarkovNode>();

    public MarkovString()
    {
        Db4o.configure().activationDepth(10);
        database = Db4o.openFile("Markov2");
        ObjectSet<MarkovNode> set = database.get(new MarkovNode(null, true));
        if(set.size() == 0)
        {
            MarkovNode tmp = new MarkovNode("[");
            database.set(tmp);
            MarkovNode tmp2 = new MarkovNode("]");
            database.set(tmp2);
            database.commit();
        }
        
        t.scheduleAtFixedRate(this, 0, 300000);
    }
    
    public int getWordCount()
    {
        return database.get(new MarkovNode(null, true)).size();
    }
    
    public int getConnectionCount()
    {
        int ret = 0;
        ObjectSet<MarkovNode> set = database.get(new MarkovNode(null, true));
        for (MarkovNode n : set)
            ret += n.getConnectionCount();
        return ret;
    }
    
    public String Generate()
    {
        StringBuffer sb = new StringBuffer();
        MarkovNode current = getNode("[");
        while (!current.getWord().equals("]"))
        {
            System.out.println(current.getWord());
            
            MarkovNode newNode = current.GetRandomNode();
            if (newNode == null)
            {
                MarkovNode end = getNode("]");
                current.AddChild(end);
                newNode = end;
            }
            current = newNode;
            
            sb.append(current.getWord());
            sb.append(" ");
        }
        return sb.toString().replace("]", " ").trim();
    }
    
    public void Learn(String sentence)
    {
        StringBuffer sb = new StringBuffer();
        for (char c :  sentence.toCharArray())
        {
            if (Character.isWhitespace(c))
                sb.append(" ");
            else if (Character.isLetterOrDigit(c))
                sb.append(Character.toLowerCase(c));
        }

        System.out.println(sentence);
        
        String[] words = sb.toString().split(" ");
        String lastWord = null;
        for (String word : words)
        {
            System.out.println(word);
            
            if (word.trim().equals("")) continue;
            MarkovNode n, parent;
            MarkovNode query = getNode(word);
            if (query == null)
            {
                n = new MarkovNode(word);
                database.set(n);
            }
            else
            {
                n = query;
            }

            if (lastWord == null)
            {
                parent = getNode("[");
            }
            
            else
            {
                parent = getNode(lastWord);
            }

            parent.AddChild(n);
            updated.add(parent);
//            database.set(parent.getChildren());
//            database.set(parent.getOccuranceTable());
//            database.set(parent);

            lastWord = word;
        }

        if (lastWord != null)
        {
            MarkovNode last = getNode(lastWord);
            last.AddChild(getNode("]"));
            updated.add(last);
//            database.set(last.getChildren());
//            database.set(last.getOccuranceTable());
//            database.set(last);
        }
        
        //database.commit();
    }
    
    private MarkovNode getNode(String word)
    {
        ObjectSet<MarkovNode> query = database.get(new MarkovNode(word, true));
        if (query.size() == 0)
            return null;
        else
            return query.get(0);
    }
    
    public void run()
    {
        if (database != null)
        {
            for (MarkovNode n : updated)
            {
                database.set(n);
//                database.set(n.get);
//                database.set(occurances);
            }
            updated.clear();
            database.commit();
        }
    }
    
    @Override
    protected void finalize() throws Throwable
    {
        try {
            t.cancel();
            database.commit();
            database.close();
        } finally {
            super.finalize();
        }
    }
}
