package hr.chus.cchat.db.service;

import java.util.Date;
import java.util.List;

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
	public Object[] searchUsers(Nick nick, Operator operator, ServiceProvider serviceProvider, String msisdn, Integer id, String name, String surname, Boolean deleted, int start, int limit);
	public Long getCount();
	public List<User> getByOperator(Operator operator);
	public List<User> getRandom(int count, Date lastMsgDate);
	public List<User> getNewest(Date lastMsgDate);
	public void clearOperatorField(Operator operator);

}
