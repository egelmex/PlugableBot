package Markov;

import java.util.logging.Logger;

import net.sf.ooweb.http.RequestState;
import net.sf.ooweb.objectmapping.Controller;

import com.db4o.ObjectSet;

@Controller("/")
public class MarkovExplorer {

	MarkovDatabase db;
	
	private static final Logger log = Logger.getLogger(Markov.class.getName());

	public MarkovExplorer(MarkovDatabase db) {
		this.db = db;
	}

	public String index() {
		String output = "";
		try {
			log.info("index: 1");
			ObjectSet<MarkovLink> links = db.getLinks("[");
			if (links == null) {
				log.info("links was null :(");
			} else {
			log.info("index: 2");
			for (MarkovLink link : links) {
				if (link.getTo().getWord().equals("]")) {
					
				} else {
					output += "<a href=\"/get?word=" +  link.getTo().getWord()+"\">" +  link.getTo().getWord() + "</a><br />\n";
				}
			}
			log.info("index: 3");
			
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output;
		// //List<MarkovNode> nodes = cache.get("[").getChildren();
		// for (MarkovNode node : nodes) {
		// output += node.getWord() + "\n";
		// }
		//    	

	}

	public String get(RequestState state) {
		String output = "";
		try {
			log.info("index: 1");
			ObjectSet<MarkovLink> links = db.getLinks((String)(state.getRequestArgs().get("word")));
			output += "<h1>" + state.getRequestArgs().get("word") + "</h1>";
			if (links == null) {
				log.info("links was null :(");
			} else {
			log.info("index: 2");
			for (MarkovLink link : links) {
				if (link.getTo().getWord().equals("]")) {
					
				} else {
					output += "<a href=\"/get?word=" +  link.getTo().getWord()+"\">" +  link.getTo().getWord() + "</a><br />\n";
				}
			}
			log.info("index: 3");
			
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output;
	}

}
