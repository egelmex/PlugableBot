/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Markov2;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ajc39
 */
public class MarkovDatabase implements Runnable
{
    private ObjectContainer database;
    private boolean shuttingDown = false;
    private LinkedBlockingQueue<MarkovNode> saveQueue;

//    private final ConcurrentHashMap<String, MarkovNode> cache = new ConcurrentHashMap<String, MarkovNode>();
//    private final ConcurrentLinkedQueue<MarkovNode> queue = new ConcurrentLinkedQueue<MarkovNode>();

    public MarkovDatabase(LinkedBlockingQueue<MarkovNode> saveQueue)
    {
        this.saveQueue = saveQueue;
        Db4o.configure().automaticShutDown(false);
        // set up indexing
        Db4o.configure().objectClass(MarkovNode.class).objectField("word").indexed(false);
        // set it up to update the lists properly
        Db4o.configure().objectClass(MarkovNode.class).updateDepth(3);
        // and activate the lists far enough
        Db4o.configure().objectClass(MarkovNode.class).minimumActivationDepth(3);
        database = Db4o.openFile("Markov2.db4o");
    }

//    public void populate()
//    {
//        busy = true;
//        Logger.getLogger(MarkovDatabase.class.getName()).log(Level.INFO, "Loading data");
//        // get a list of all nodes
//        ObjectSet<MarkovNode> set = database.get(MarkovNode.class);
//        // if we dont have any, we have an empty database and need to start
//        // learning
//        if (set.size() == 0)
//        {
//                database.set(new MarkovNode("["));
//                database.set(new MarkovNode("]"));
//        }
//        else
//        {
//            for (MarkovNode n : set)
//            {
//                cache.put(n.getWord(), n);
//            }
//        }
//        Logger.getLogger(MarkovDatabase.class.getName()).log(Level.INFO, "Loading done");
//        busy = false;
//    }

//    public void queue(MarkovNode node)
//    {
//        Logger.getLogger(MarkovDatabase.class.getName()).log(Level.INFO, "Queueing");
//        queue.add(node);
//
//        if (!cache.containsKey(node.getWord()))
//        {
//            Logger.getLogger(MarkovDatabase.class.getName()).log(Level.INFO, "Updating cache");
//            cache.put(node.getWord(), node);
//        }
//
//        Logger.getLogger(MarkovDatabase.class.getName()).log(Level.INFO, "Done");
//
//        if (!busy)
//        {
//            synchronized (this)
//            {
//                this.notify();
//            }
//        }
//    }

    public int[] getStats()
    {
		int ret[] = {0, 0};
        ObjectSet<MarkovNode> query = database.get(new MarkovNode(null, false));
        ret[0] = query.size();
        for (MarkovNode n : query)
            ret[1] += n.getConnectionCount();
		return ret;
	}

    void shutdown()
    {
        shuttingDown = true;
    }

    public MarkovNode getNode(String word)
    {
        ObjectSet<MarkovNode> query = database.get(new MarkovNode(word, true));
        if (query.size() == 0)
        {
            return null;
        }
        else
        {
            return query.get(0);
        }
    }

    public void run()
    {
        while(!shuttingDown || saveQueue.peek() != null)
        {
            try
            {
                database.set(saveQueue.take());
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(MarkovDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
            database.commit();
        }
        database.close();
    }
}
