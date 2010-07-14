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
		return getNode("[", "1");

	}

	@Exclude
	public String getNode(String id, String level) {
		log.info("Getting children of " + id + " at level " + level);
		int i_level = Integer.parseInt(level);
		String output = "";
		output += "{id:\"";
		output += (i_level) + "_";
		output += id.equals("[") ? "root" : id;
		output += "\",name:\"";
		output += id.equals("[") ? "root" : id;
		output += "\",";
		output += "data:{level:\"";
		output += (i_level);
		output += "\"}, children:[";
		try {
			ObjectSet<MarkovLink> links = db.getLinks(id);
			if (links == null) {

			} else {
				for (MarkovLink link : links) {
					if (link.getTo().getWord().equals("]")) {

					} else {
						String word = link.getTo().getWord();
						output += "{id:\"";
						output += (i_level + 1) + "_";
						output += word + "\", name:\"" + word;
						output += "\",";
						
						output += "data:{level:\"";
						output += (i_level + 1);
						output += "\"}, children:[]}";
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
		log.info("output: " + output);
		return output;
	}

	public String get(RequestState state) {

		return getNode((String) state.getRequestArgs().get("word"),
				(String) state.getRequestArgs().get("level"));
	}

}
