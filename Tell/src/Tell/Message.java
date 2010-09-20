/*	
 * Copyright 2010 Mex (ellism88@gmail.com)
 * 
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package Tell;

import java.util.Date;

/**
 * Used to model messages which will be stored in the db4o database.
 * @author Mex
 *
 */
public class Message {
	String sender;
	Date date;
	String target;
	String message;
	String channel;
	
	/**
	 * Initialise the message with all its fields filled in.
	 * @param sender the nick of the user who wanted the message sent
	 * @param date the date the message was sent
	 * @param target the person to send the message to next time they talk
	 * @param message the message to send
	 * @param channel the channel to send the message to
	 */
	public Message(String sender, Date date, String target, String message, String channel) {
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
