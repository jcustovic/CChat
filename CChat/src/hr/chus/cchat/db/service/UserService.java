package hr.chus.cchat.db.service;

import java.util.List;

import hr.chus.cchat.model.db.jpa.Nick;
import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.db.jpa.ServiceProvider;
import hr.chus.cchat.model.db.jpa.User;

public interface UserService {
	
	public void addUser(User user);
	public User editUser(User user);
	public void deleteUser(User user);
	public User getUserId(Integer id);
	public Object[] searchUsers(Nick nick, Operator operator, ServiceProvider serviceProvider, String msisdn, String name, String surname, int start, int limit);
	public Long getCount();

}
