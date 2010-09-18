package hr.chus.cchat.model.helper.db;

import java.util.Date;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class Conversation {
	
	private Date time;
	private String text;
	
	
	public Conversation(Date time, String text) {
		this.time = time;
		this.text = text;
	}
	
	
	// Getters & setters
	
	public Date getTime() { return time; }
	public void setTime(Date time) { this.time = time; }
	
	public String getText() { return text; }
	public void setText(String text) { this.text = text; }
	
}
