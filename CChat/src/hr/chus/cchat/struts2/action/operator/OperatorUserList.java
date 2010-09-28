package hr.chus.cchat.struts2.action.operator;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hr.chus.cchat.db.service.UserService;
import hr.chus.cchat.helper.UserAware;
import hr.chus.cchat.model.db.jpa.Nick;
import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.db.jpa.User;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class OperatorUserList extends ActionSupport implements UserAware {
	
	private static final long serialVersionUID = 1L;
	
	private Log log = LogFactory.getLog(getClass());
	
	private UserService userService;
	private Operator user;
	private Nick nick;
	private Operator operator;
	private String name;
	private String surname;
	private Integer id;
	private int start;
	private int limit;
	
	private List<User> userList;
	private Long totalCount;


	@SuppressWarnings("unchecked")
	@Override
	public String execute() {
		if (user.getRole().getName().equals("operator")) operator = user;
		log.info("Searching for users from ..." + start + " for " + limit);
		Object[] result = userService.searchUsers(nick, operator, null, null, id, name, surname, false, start, limit);
		totalCount = (Long) result[0];
		userList = (List<User>) result[1];
		log.debug("Found " + totalCount + " users.");
		return SUCCESS;
	}

	
	// Getters & setters
	
	public void setUserService(UserService userService) { this.userService = userService; }
	
	@Override
	public void setAuthenticatedUser(Operator user) { this.user = user; }

	public List<User> getUserList() { return userList; }
	public void setUserList(List<User> userList) { this.userList = userList; }

	public void setNick(Nick nick) { this.nick = nick; }

	public void setOperator(Operator operator) { this.operator = operator; }
	
	public void setName(String name) { this.name = name; }

	public void setSurname(String surname) { this.surname = surname; }
	
	public void setId(Integer id) { this.id = id; }

	public void setStart(int start) { this.start = start; }

	public void setLimit(int limit) { this.limit = limit; }

	public Long getTotalCount() { return totalCount; }
	public void setTotalCount(Long totalCount) { this.totalCount = totalCount; }	
					
}
