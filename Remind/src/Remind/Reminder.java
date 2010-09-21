package Remind;

import java.util.Date;

public class Reminder {
	private String from;
	private String to;
	private String message;
	/**
	 * @return the channel
	 */
	public final String getChannel() {
		return channel;
	}

	private String channel;
	/**
	 * @return the from
	 */
	public final String getFrom() {
		return from;
	}

	/**
	 * @return the to
	 */
	public final String getTo() {
		return to;
	}

	/**
	 * @return the message
	 */
	public final String getMessage() {
		return message;
	}

	/**
	 * @return the date
	 */
	public final Date getDate() {
		return date;
	}

	Date date;

	public Reminder(String from, String to, String message, Date date, String channel) {
		super();
		this.from = from;
		this.to = to;
		this.message = message;
		this.date = date;
		this.channel = channel;
	}
}
