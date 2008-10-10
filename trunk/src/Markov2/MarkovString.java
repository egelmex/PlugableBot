/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Markov2;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import java.util.TimerTask;
import java.util.HashMap;
import java.util.Timer;

/**
 *
 * @author Andrew
 */
public class MarkovString extends TimerTask {
    
    private Timer t = new Timer();
    private ObjectContainer database;
    private HashMap<String, MarkovNode> nodes;

    public MarkovString()
    {
        nodes = new HashMap<String, MarkovNode>();
        database = Db4o.openFile("Markov2");
        ObjectSet<HashMap<String, MarkovNode>> set = database.get(nodes);
        if(set.size() == 0)
        {
            nodes.put("[", new MarkovNode("["));
            nodes.put("[", new MarkovNode("]"));
            database.set(nodes);
        }
        else
        {
            nodes = set.get(0);
        }
        
        t.schedule(this, 0, 300000);
    }
    
    public int getWordCount()
    {
        return nodes.size() - 2;
    }
    
    public int getConnectionCount()
    {
        int ret = 0;
        for (MarkovNode n : nodes.values())
            ret += n.getConnectionCount();
        return ret;
    }
    
    public String Generate()
    {
        StringBuffer sb = new StringBuffer();
        MarkovNode current = nodes.get("[");
        while (!current.getWord().equals("]"))
        {
            current = current.GetRandomNode();
            if (current == null)
            {
                current.AddChild(nodes.get("]"));
                current = nodes.get("]");
            }
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

        String[] words = sb.toString().split(" ");
        String lastWord = null;
        for (String word : words)
        {
            if (word.trim().equals("")) continue;
            MarkovNode n, parent;
            if (!nodes.containsKey(word))
            {
                n = new MarkovNode(word);
                nodes.put(word, n);
            }
            else
                n = nodes.get(word);

            if (lastWord == null)
                parent = nodes.get("[");
            else
                parent = nodes.get(lastWord);

            database.set(parent);
            database.set(nodes);
                
            lastWord = word;
        }
    }
    
    public void run()
    {
        if (database != null)
            database.commit();
    }
    
    @Override
    protected void finalize() throws Throwable
    {
        try {
            t.cancel();
            database.close();
        } finally {
            super.finalize();
        }
    }
}
