package Factoids;

import java.util.Date;

public class Fact {
	private String fact;
	private String message;
	private String setBy;
	private Date setAt;
	private boolean current;
	
	public Fact(String fact, String message, String setBy, Date setAt, boolean current) {
		super();
		this.message = message;
		this.fact = fact;
		this.setBy = setBy;
		this.setAt = setAt;
		this.current = current;
	}
	
	public String getFact() {
		return fact;
	}
	public void setFact(String fact) {
		this.fact = fact;
	}
	public String getSetBy() {
		return setBy;
	}
	public void setSetBy(String setBy) {
		this.setBy = setBy;
	}
	public Date getSetAt() {
		return setAt;
	}
	public void setSetAt(Date setAt) {
		this.setAt = setAt;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isCurrent() {
		return current;
	}

	public void setCurrent(boolean current) {
		this.current = current;
	}
	
	public void setOld() {
		this.current = false;
	}
	
	@Override
	public String toString() {
		return getMessage();
	}
}
