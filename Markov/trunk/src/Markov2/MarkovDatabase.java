/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Markov2;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ajc39
 */
public class MarkovDatabase extends Thread
{
    private ObjectContainer database;
    private boolean busy = false;
    private boolean shuttingDown = false;
    private final ConcurrentHashMap<String, MarkovNode> cache = new ConcurrentHashMap<String, MarkovNode>();
    private final ConcurrentLinkedQueue<MarkovNode> queue = new ConcurrentLinkedQueue<MarkovNode>();

    public MarkovDatabase()
    {
        busy = true;
        Db4o.configure().automaticShutDown(false);
        // set up indexing
        Db4o.configure().objectClass(MarkovNode.class).objectField("word").indexed(false);
        // set it up to update the lists properly
        Db4o.configure().objectClass(MarkovNode.class).updateDepth(3);
        // and activate the lists far enough
        Db4o.configure().objectClass(MarkovNode.class).minimumActivationDepth(3);
        database = Db4o.openFile("Markov2.db4o");
        busy = false;
    }

    public void populate()
    {
        busy = true;
        Logger.getLogger(MarkovDatabase.class.getName()).log(Level.INFO, "Loading data");
        // get a list of all nodes
        ObjectSet<MarkovNode> set = database.get(MarkovNode.class);
        // if we dont have any, we have an empty database and need to start
        // learning
        if (set.size() == 0)
        {
                database.set(new MarkovNode("["));
                database.set(new MarkovNode("]"));
        }
        else
        {
            for (MarkovNode n : set)
            {
                cache.put(n.getWord(), n);
            }
        }
        Logger.getLogger(MarkovDatabase.class.getName()).log(Level.INFO, "Loading done");
        busy = false;
    }

    public void queue(MarkovNode node)
    {
        Logger.getLogger(MarkovDatabase.class.getName()).log(Level.INFO, "Queueing");
        queue.add(node);
        
        if (!cache.containsKey(node.getWord()))
        {
            Logger.getLogger(MarkovDatabase.class.getName()).log(Level.INFO, "Updating cache");
            cache.put(node.getWord(), node);
        }

        Logger.getLogger(MarkovDatabase.class.getName()).log(Level.INFO, "Done");

        if (!busy)
        {
            synchronized (this)
            {
                this.notify();
            }
        }
    }

    public int[] getStats()
    {
		int ret[] = new int[2];

        ret[0] = cache.size();
        for (MarkovNode n : cache.values())
            ret[1] += n.getConnectionCount();

		return ret;
	}

    void shutdown()
    {
        shuttingDown = true;
        synchronized (this)
        {
            this.notify();
        }
    }

    private void commit()
    {
        MarkovNode current = null;
        busy = true;
        while ((current = queue.poll()) != null)
        {
            database.set(current);
        }
        database.commit();
        busy = false;
    }

    public boolean isBusy()
    {
        return busy;
    }

    public MarkovNode getNode(String word)
    {
        if (cache.containsKey(word))
        {
            return cache.get(word);
        }
        else
        {
            ObjectSet<MarkovNode> query = database.get(new MarkovNode(word, true));
            if (query.size() == 0)
            {
                return null;
            }
            else
            {
                MarkovNode n = query.get(0);
                cache.put(n.getWord(), n);
                return n;
            }
        }
    }

    @Override
    public void run()
    {
        populate();
        while(!shuttingDown)
        {
            try
            {
                Logger.getLogger(MarkovDatabase.class.getName()).log(Level.INFO, "Waiting");
                synchronized (this)
                {
                    this.wait();
                }
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(MarkovDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
            Logger.getLogger(MarkovDatabase.class.getName()).log(Level.INFO, "Committing");
            commit();
        }
        database.close();
    }
}
