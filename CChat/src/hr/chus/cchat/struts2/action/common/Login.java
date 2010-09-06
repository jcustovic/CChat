package hr.chus.cchat.struts2.action.common;

import hr.chus.cchat.ApplicationConstants;
import hr.chus.cchat.db.service.OperatorService;
import hr.chus.cchat.model.db.jpa.Operator;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class Login extends ActionSupport implements SessionAware {

	private static final long serialVersionUID = 1L;
	private Log log = LogFactory.getLog(getClass());
	
	private OperatorService operatorService;
	private Map<String, Object> session;
	private String username;
	private String password;

	@Override
	public String execute() throws Exception {
		Operator user = (Operator) session.get(ApplicationConstants.USER_SESSION);
		if (user != null) {
			if (user.getRole().getName().equals(ApplicationConstants.OPERATOR)) {
				return ApplicationConstants.OPERATOR;
			} else if (user.getRole().getName().equals(ApplicationConstants.ADMIN)) {
				return ApplicationConstants.ADMIN;
			} else if (user.getRole().getName().equals(ApplicationConstants.MODERATOR)) {
				return ApplicationConstants.MODERATOR;
			}
		}
		if (getUsername() == null || getPassword() == null) {
			return INPUT;
		}
		user = operatorService.authenticateUser(getUsername(), getPassword());
		if (user != null) {
			if (user.getDisabled()) {
				addFieldError("username", getText("login.userIsDisabled"));
			} else if (user.getRole().getName().equals(ApplicationConstants.OPERATOR)) {
				session.put(ApplicationConstants.USER_SESSION, user);
				log.info(user.getUsername() + " (id=" + user.getId() + ") logged in as " + ApplicationConstants.OPERATOR);
				return ApplicationConstants.OPERATOR;
			} else if(user.getRole().getName().equals(ApplicationConstants.MODERATOR)) {
				session.put(ApplicationConstants.USER_SESSION, user);
				log.info(user.getUsername() + " (id=" + user.getId() + ") logged in as " + ApplicationConstants.MODERATOR);
				return ApplicationConstants.MODERATOR;
			} else if (user.getRole().getName().equals(ApplicationConstants.ADMIN)) {
				session.put(ApplicationConstants.USER_SESSION, user);
				log.info(user.getUsername() + " (id=" + user.getId() + ") logged in as " + ApplicationConstants.ADMIN);
				return ApplicationConstants.ADMIN;
			}
		} else {
			addFieldError("username", getText("login.wrongUsernameOrPassword"));
		}
		return INPUT;
	}
	
	@Override
	public void validate() {
		if (getUsername() != null && getPassword() != null) {
			if (getUsername().length() == 0) {
				addFieldError("username", getText("login.usernameRequired"));
			}
			if (getPassword().length() == 0) {
				addFieldError("password", getText("login.passwordRequired"));
			}
		}
	}
	
	// Getters & setters
	
	public void setSession(Map<String, Object> session) { this.session = session; }

	public void setOperatorService(OperatorService operatorService) { this.operatorService = operatorService; }

	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }
	
	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }

}
