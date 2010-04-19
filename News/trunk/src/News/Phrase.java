package News;

/**
 * Phrase class
 * @author Murmew
 */
public class Phrase
{
	public String present;
	public String past;
	public String active;
	public String object;

	/**
	 * Constructor for objects of class Phrase
	 */
	public Phrase(String present, String past, String active, String object)
	{
		this.present = present;
		this.past = past;
		this.active = active;
		this.object = object;
	}
}