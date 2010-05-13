package Markov;

public class MarkovLink {
	public MarkovNode from, to;
	public double weight = 1;
	
	
	public MarkovLink(MarkovNode from, MarkovNode to ) {
		this.from = from;
		this.to = to;
	}
	
	public MarkovNode getFrom() {
		return from;
	}

	public MarkovNode getTo() {
		return to;
	}

	public double getWeight() {
		return weight;
	}
	
	public void setWeight(double weight) {
		this.weight = weight;
	}
	

}
