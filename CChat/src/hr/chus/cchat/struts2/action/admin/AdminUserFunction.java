package hr.chus.cchat.struts2.action.admin;

import hr.chus.cchat.db.service.UserService;
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
public class AdminUserFunction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private Log log = LogFactory.getLog(getClass());
	
	private UserService userService;
	private String operation;
	private User user;
	private Map<String, String> errorFields;
	private String status;
	
	@Override
	public String execute() throws Exception {
		if (operation != null && operation.equals("update")) {
			log.debug("Updating user " + user + " ...");
			user = userService.editUser(user);
		}
		return SUCCESS;
	}

	
	// Getters & setters
	
	public void setUserService(UserService userService) { this.userService = userService; }
	
	public void setOperation(String operation) { this.operation = operation; }
	
	public User getUser() { return user; }
	public void setUser(User user) { this.user = user; }

	public Map<String, String> getErrorFields() { return errorFields; }
	public void setErrorFields(Map<String, String> errorFields) { this.errorFields = errorFields; }

	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }
	
}