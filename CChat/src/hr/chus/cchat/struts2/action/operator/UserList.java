package hr.chus.cchat.struts2.action.operator;

import java.util.Date;
import java.util.List;

import hr.chus.cchat.db.service.UserService;
import hr.chus.cchat.helper.UserAware;
import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.db.jpa.User;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that return users that are assigned to operator (users that operator communicates with),
 * newest user that do not have assigned operator that contacted this application in the last 48 hours so operators
 * can contact them and random users that are randomly grabbed from the database so operators can contact them.
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class UserList extends ActionSupport implements UserAware {
	
	private static final long serialVersionUID = 1L;
	
	private static final long NEWEST_TIME = 172800000L; // 48 hours
	
	private UserService userService;
	
	private Operator operator;
	private String errorMsg;
	private List<User> operatorUserList;
	private List<User> newestUserList;
	private List<User> randomUserList;
	
	
	@Override
	public String execute() {
		if (operator.getDisabled()) { // This should not happen. When user is disabled he must not have active session!
			errorMsg = getText("operator.disabled");
			return ERROR;
		}
		if (!operator.getIsActive()) {
			errorMsg = getText("operator.notActive");
			return ERROR;
		}
		operatorUserList = userService.getByOperator(operator);
		Date lastMsgDate = new Date(new Date().getTime() - NEWEST_TIME);
		newestUserList = userService.getNewest(lastMsgDate);
		randomUserList = userService.getRandom(10, lastMsgDate);
		return SUCCESS;
	}
	
	
	// Getters & setters
	
	public void setUserService(UserService userService) { this.userService = userService; }

	@Override
	public void setAuthenticatedUser(Operator user) { operator = user; }
	
	public String getErrorMsg() { return errorMsg; }

	public List<User> getOperatorUserList() { return operatorUserList; }

	public List<User> getNewestUserList() { return newestUserList; }

	public List<User> getRandomUserList() { return randomUserList; }
			
}
