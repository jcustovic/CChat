package hr.chus.cchat.struts2.action.operator;

import java.util.Date;
import java.util.List;

import hr.chus.cchat.db.service.UserService;
import hr.chus.cchat.helper.UserAware;
import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.db.jpa.User;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class UserList extends ActionSupport implements UserAware {
	
	private static final long serialVersionUID = 1L;
	private static final long NEWEST_TIME = 172800000L; // 48 hours
	
	private UserService userService;
	private Operator operator;
	private List<User> operatorUserList;
	private List<User> newestUserList;
	private List<User> randomUserList;
	
	
	@Override
	public String execute() {
		operatorUserList = userService.getByOperator(operator);
		Date lastMsgDate = new Date(new Date().getTime() - NEWEST_TIME);
		newestUserList = userService.getNewest(lastMsgDate);
		randomUserList = userService.getRandom(10, lastMsgDate);
		return SUCCESS;
	}
	
	
	// Getters & setters
	
	public void setUserService(UserService userService) { this.userService = userService; }

	@Override
	public void setUser(Operator user) { operator = user; }

	public List<User> getOperatorUserList() { return operatorUserList; }

	public List<User> getNewestUserList() { return newestUserList; }

	public List<User> getRandomUserList() { return randomUserList; }
			
}
