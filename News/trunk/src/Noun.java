
/**
 * Noun class
 * @author Murmew
 * @auther Mex
 */
public class Noun
{
	public String word;
	public Perspective perspective;
	public Plurality plurality;

	/**
	 * Constructor for objects of class Noun
	 */
	public Noun(String word, Perspective perspective, Plurality plurality)
	{
		this.word = word;
		this.perspective = perspective;
		this.plurality = plurality;
	}
}