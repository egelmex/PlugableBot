package Markov2;
/**
 *
 * @author Mex
 */
public class MarkovNode{

    public String getWord() {
		return word;
	}

	private String word;

    /**
     *  Creates a new instance of MarkovNode 
     **/
    public MarkovNode(String word) {
        this.word = word;
    }
}