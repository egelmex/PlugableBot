package org.jibble.socnet;


@SuppressWarnings("serial")
public class KarmaInferenceHeuristic extends InferenceHeuristic {

	public enum Karma  {
		PLUS,
		MINUS
	}
	
	public KarmaInferenceHeuristic(Graph graph, Configuration config) {
		super(graph, config);
	}

	@Override
	public boolean infer(String nick, String message) {
		Graph g = getGraph();
		boolean changed = false;
		
		String[] parts = message.split(" ");
		for (String part : parts) {
			Node n = null;
			Karma k = null;
			if (part.startsWith("++")) {
				n = new Node(part.substring(2));
				k = Karma.PLUS;
			} else if (part.endsWith("++")) {
				n = new Node(part.substring(part.length() - 2));
				k = Karma.PLUS;
			} else if (part.startsWith("--")) {
				n = new Node(part.substring(0, 2));
				k = Karma.MINUS;
			} else if (part.endsWith("--")) {
				n = new Node(part.substring(part.length() - 2));
				k=Karma.MINUS;
			}
			
			//Some one has karma'd a string
			if (n != null && g.contains(n)) {
				switch (k) {
				case PLUS:
					g.addEdge(new Node(nick), n, +1);
					break;
				case MINUS:
					g.addEdge(new Node(nick), n, -1);
					break;
				}
				changed = true;
				
			}
		}
		
		return changed;

	}

}
