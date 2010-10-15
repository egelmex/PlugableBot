package Markov.tools;

import Markov.MarkovDatabase;
import Markov.MarkovLink;
import Markov.MarkovNode;

import com.db4o.ObjectSet;

public class DotTool {
	public static void main(String[] args) {
		MarkovDatabase md = new MarkovDatabase(args[0]);
		ObjectSet<MarkovNode> nodes = md.getWords();
		ObjectSet<MarkovLink> links = md.getLinks();
				
		
		System.out.println("graph brain {");
		
		for (MarkovNode node : nodes) {
			System.out.println("_" + node.getWord() + "[label=\"" + node.getWord() + "\"];");
		}
		
		System.out.println();
		
		
		for (MarkovLink link : links) {
			System.out.println("_" + link.from.getWord() + "->" + "_" + link.to.getWord() + ";");
		}
			
		System.out.println("}");
		
		md.shutdown();
	}
}
