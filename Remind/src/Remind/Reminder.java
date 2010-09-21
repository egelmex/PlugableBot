package Remind;

import java.util.Date;
import java.util.TimerTask;

public class Reminder extends TimerTask{
	private String from;
	private String to;
	private String message;
	Date date;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	public Reminder(String from, String to, String message, Date date) {
		super();
		this.from = from;
		this.to = to;
		this.message = message;
		this.date = date;
	}
}
