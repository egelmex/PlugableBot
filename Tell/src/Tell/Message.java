package Tell;

import java.util.Date;

public class Message {
	String sender;
	Date date;
	String target;
	String message;
	String channel;
	
	public Message(String sender, Date date, String target, String message, String channel) {
		super();
		this.sender = sender;
		this.date = date;
		this.target = target;
		this.message = message;
	}
	/**
	 * @return the sender
	 */
	public final String getSender() {
		return sender;
	}
	/**
	 * @return the date
	 */
	public final Date getDate() {
		return date;
	}
	/**
	 * @return the target
	 */
	public final String getTarget() {
		return target;
	}
	/**
	 * @return the message
	 */
	public final String getMessage() {
		return message;
	}
}
