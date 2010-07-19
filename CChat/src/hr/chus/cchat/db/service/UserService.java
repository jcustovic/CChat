package hr.chus.cchat.db.service;

import hr.chus.cchat.model.db.jpa.Nick;
import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.db.jpa.ServiceProvider;
import hr.chus.cchat.model.db.jpa.User;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public interface UserService {
	
	public void addUser(User user);
	public User editUser(User user);
	public void deleteUser(User user);
	public User getUserId(Integer id);
	public Object[] searchUsers(Nick nick, Operator operator, ServiceProvider serviceProvider, String msisdn, String name, String surname, int start, int limit);
	public Long getCount();

}
