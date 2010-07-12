package Markov;

import java.util.logging.Logger;

import net.sf.ooweb.http.RequestState;
import net.sf.ooweb.objectmapping.Controller;
import net.sf.ooweb.objectmapping.Exclude;

import com.db4o.ObjectSet;

@Controller("/")
public class MarkovExplorer {

	MarkovDatabase db;

	private static final Logger log = Logger.getLogger(Markov.class.getName());

	public MarkovExplorer(MarkovDatabase db) {
		this.db = db;
	}

	public String index() {
		return getNode("[");

	}

	@Exclude
	public String getNode(String id) {
		log.info("Getting children of " + id);
		String output = "";
		output += "{id:\"";
		output += id.equals("[")?"root":id;
		output += "\",name:\"root\",data:{},children:[";

		try {
			ObjectSet<MarkovLink> links = db.getLinks(id);
			if (links == null) {

			} else {
				for (MarkovLink link : links) {
					if (link.getTo().getWord().equals("]")) {

					} else {
						String word = link.getTo().getWord();
						output += "{id:\"" + word
								+ "\", name:\"" + word
								+ "\", data:{}, children:[]}";
						if (links.hasNext()) {
							output += ",";
						}
					}
				}

			}
			output += "]}";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output;
	}
	
	public String get(RequestState state) {
		
		return getNode((String) state.getRequestArgs().get("word"));
	}

}
