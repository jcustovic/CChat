package hr.chus.cchat.struts2.action.operator;

import hr.chus.cchat.db.service.UserService;
import hr.chus.cchat.helper.UserAware;
import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.db.jpa.User;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that implements user functions. Thru this web service users can be created,
 * updated or deleted (dao services are invoked).
 * 
 * User that have the role of "operators" can only update users that they are assigned to.
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
		if (operation != null && operation.equals("get")) return;
		errorFields = new HashMap<String, String>();
		if (operation == null) {
			log.warn("Operation must not be null.");
			errorFields.put("operation", getText("operation.empty"));
		} else if (!(operation.equals("update") || operation.equals("get"))) {
			log.warn("Unsupported operation: " + operation);
			errorFields.put("operation", getText("operation.notSupported"));
		} else if (user == null) {
			errorMsg = "User must be set";
		} else if (operator.getRole().getName().equals("operator") && (user.getOperator() == null || !user.getOperator().equals(operator))) {
			errorMsg = "Operator " + operator.getUsername() + " can't change/get user with id " + user.getId() + " because he isn't assigned to that user.";
			log.warn("Operator " + operator.getUsername() + " can't change/get user with id " + user.getId() + " because he isn't assigned to that user.");
			errorFields.put("user.operator", getText("user.notAllowedToEdit"));
		}
		if (errorMsg != null || errorFields.size() > 0) {
			addActionError(errorMsg + "; Error count: " + errorFields.size());
			status = false;
		}
	}
	
	@Override
	public String execute() throws Exception {
		if (operation.equals("update")) {
			log.debug("Updating user " + user + " ...");
			user = userService.editUserOperator(user);
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