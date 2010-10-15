package Markov.tools;

import com.db4o.ObjectSet;

import Markov.MarkovDatabase;
import Markov.MarkovLink;
import Markov.MarkovNode;

public class DotTool {
	public static void main(String[] args) {
		MarkovDatabase md = new MarkovDatabase();
		ObjectSet<MarkovNode> nodes = md.getWords();
		ObjectSet<MarkovLink> links = md.getLinks();
		
		
		System.out.println("graph brain {");
		
		for (MarkovNode node : nodes) {
			System.out.println(node.getWord() + "[label=\"" + node.getWord() + "\"];");
		}
		
		System.out.println();
		
		
		for (MarkovLink link : links) {
			System.out.println(link.from + "->" + link.to + ";");
		}
			
		System.out.println("}");
		
		md.shutdown();
	}
}
