package hr.chus.cchat.helper;

import hr.chus.cchat.model.db.jpa.Operator;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public interface UserAware {
	
	public void setAuthenticatedUser(Operator user);
	
}
