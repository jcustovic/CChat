package hr.chus.cchat.struts2.action.operator;

import hr.chus.cchat.db.service.UserService;
import hr.chus.cchat.helper.UserAware;
import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.db.jpa.User;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class OperatorUserFunction extends ActionSupport implements UserAware {

	private static final long serialVersionUID = 1L;
	private Log log = LogFactory.getLog(getClass());
	
	private UserService userService;
	private String operation;
	private User user;
	private Map<String, String> errorFields;
	private Boolean status;
	private String errorMsg;
	private Operator operator;
	
	
	@Override
	public void validate() {
		log.info(operator.getRole());
		log.info(operator.getRole().getName().equals("operator"));
		if (user == null) {
			errorMsg = "User must be set";
		} else if (operator.getRole().getName().equals("operator") && (user.getOperator() == null || !user.getOperator().equals(operator))) {
			errorMsg = "Operator " + operator.getUsername() + " can't change user with id " + user.getId() + " because he isn't assigned to that user.";
			log.warn("Operator " + operator.getUsername() + " can't change user with id " + user.getId() + " because he isn't assigned to that user.");
		}
		if (errorMsg != null) {
			addActionError(errorMsg);
			status = false;
		}
	}
	
	@Override
	public String execute() throws Exception {
		if (operation != null && operation.equals("update")) {
			log.debug("Updating user " + user + " ...");
			user = userService.editUser(user);
		}
		status = true;
		return SUCCESS;
	}

	
	// Getters & setters
	
	public void setUserService(UserService userService) { this.userService = userService; }
	
	public void setOperation(String operation) { this.operation = operation; }
	
	public User getUser() { return user; }
	public void setUser(User user) { this.user = user; }

	public Map<String, String> getErrorFields() { return errorFields; }
	public void setErrorFields(Map<String, String> errorFields) { this.errorFields = errorFields; }

	public Boolean getStatus() { return status; }

	public String getErrorMsg() { return errorMsg; }

	@Override
	public void setAuthenticatedUser(Operator operator) { this.operator = operator; }
	
}