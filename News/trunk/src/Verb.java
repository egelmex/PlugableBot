/**
 * Verb class
 * @author Murmew
 */
public class Verb
{
	public String singular;
	public String plural;
	public Tense tense;

	/**
	 * Constructor for objects of class Verb
	 * @param singular Singular form of Verb
	 * @param plural Plural form of Verb
	 * @param tense
	 */
	public Verb(String singular, String plural, Tense tense)
	{
		this.singular = singular;
		this.plural = plural;
		this.tense = tense;
	}
}