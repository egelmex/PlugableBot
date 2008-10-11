/*
 * MarkovString.java
 *
 * Created on 06 October 2007, 15:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package markov;

import java.util.*;
import java.io.*;
/**
 *
 * @author Administrator
 */
public class MarkovString extends TimerTask {
    
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
    
    private HashMap<String, MarkovNode> nodes;
    private ArrayList<String> newlines = new ArrayList<String>();
    private Timer t = new Timer();
    /** Creates a new instance of MarkovString */
    public MarkovString() {
        
        File f = new File("Markov");
        nodes = new HashMap<String, MarkovNode>();

        nodes.put("[", new MarkovNode("["));
        nodes.put("]", new MarkovNode("]"));

        if (f.exists())
        {
            try {
                FileReader fr = new FileReader(f);
                BufferedReader i = new BufferedReader(fr);
                String s;

                    while ((s = i.readLine()) != null)
                    {
                        String[] ss = s.trim().split(",");
                        MarkovNode a, b;
                        if (!nodes.containsKey(ss[0]))
			{
                            a = new MarkovNode(ss[0]);
			    nodes.put(ss[0], a);
			}
                        else 
                            a = nodes.get(ss[0]);
                        
                        if (!nodes.containsKey(ss[1]))
			{
                            b = new MarkovNode(ss[1]);
			    nodes.put(ss[1], b);
			}
                        else
                            b = nodes.get(ss[1]);
                        
                        a.AddChild(b);
                    }
                i.close();
                fr.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        t.schedule(this, 0, 300000);
    }
    
    public void run()
    {
        Save();
    }
    
    public void Save()
    {
        FileWriter w;
        try {
            w = new FileWriter("Markov", true);
            
        BufferedWriter r = new BufferedWriter(w);
        
        for (String n : newlines)
        {
                r.write(n + "\n");
        }

		r.flush();
        	r.close();
                newlines.clear();
		w.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
	
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
                {
                    parent = nodes.get(lastWord);
                }

                
                if (parent.AddChild(n));
                    newlines.add(parent.getWord()+","+n.getWord());
                lastWord = word;
            }
            
            if (lastWord != null)
                if (nodes.get(lastWord).AddChild(nodes.get("]")))
                    newlines.add(lastWord+",]");
        }

        public String Generate()
        {
            StringBuffer sb = new StringBuffer();
            MarkovNode current = nodes.get("[");
            while (current.getWord() != "]")
            {
                current = current.GetRandomNode();
                if (current == null)
                {
                    current.AddChild(nodes.get("]"));
                    current = nodes.get("]");
                    newlines.add(current.getWord()+",]");
                }
                sb.append(current.getWord());
                sb.append(" ");
            }
            return sb.toString().replace("]", " ").trim();
        }
        
        protected void finalize() throws Throwable {
            try {
                t.cancel();
            } finally {
                super.finalize();
            }
        }
}
