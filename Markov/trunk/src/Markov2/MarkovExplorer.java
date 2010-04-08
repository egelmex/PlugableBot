package Markov2;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.ooweb.http.Server;
import net.sf.ooweb.http.pygmy.OowebServer;
import net.sf.ooweb.objectmapping.Controller;


@Controller("/")
public class MarkovExplorer {
	
	ConcurrentHashMap<String, MarkovNode> cache;

	public MarkovExplorer(ConcurrentHashMap<String, MarkovNode> cache) {
		this.cache = cache;
		try {
			Server ooweb = new OowebServer();
			ooweb.addController(this);
			ooweb.start();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
    public String index() {
    	String output = "";
    	List<MarkovNode> nodes = cache.get("[").getChildren();
    	for (MarkovNode node : nodes) {
    		output += node.getWord() + "\n";
    	}
    	
        return  output;
    }
    
    public String get(String word) {
    	String output = "";
    	List<MarkovNode> nodes = cache.get(word).getChildren();
    	for (MarkovNode node : nodes) {
    		output += node.getWord() + "\n";
    	}
    	
        return  output;
    }

    
}

