package hr.chus.cchat.helper;

import hr.chus.cchat.model.db.jpa.Operator;

/**
 * Every Struts2 action that wants to get logged in user info needs to implement this interface
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public interface UserAware {
	
	public void setAuthenticatedUser(Operator user);
	
}
