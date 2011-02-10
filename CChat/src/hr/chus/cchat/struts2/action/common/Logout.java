package hr.chus.cchat.struts2.action.common;

import hr.chus.cchat.ApplicationConstants;
import hr.chus.cchat.db.service.OperatorService;
import hr.chus.cchat.db.service.UserService;
import hr.chus.cchat.helper.OperatorChooser;
import hr.chus.cchat.model.db.jpa.Operator;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that logs out user (removes user session info).
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class Logout extends ActionSupport implements SessionAware {
	
	private static final long serialVersionUID = 1L;
	
	private Log log = LogFactory.getLog(getClass());
	
	private OperatorService operatorService;
	private UserService userService;
	private OperatorChooser operatorChooser;
	
	private Map<String, Object> session;
	

	@Override
	public String execute() throws Exception {
		Operator user = (Operator) session.get(ApplicationConstants.USER_SESSION);
		if (user != null) {
			user.setIsActive(false);
			operatorChooser.removeActiveOperator(user);
			userService.clearOperatorField(user);
			operatorService.updateOperator(user);
			log.info(user.getUsername() + " (id=" + user.getId() + ") logged out");
			session.remove(ApplicationConstants.USER_SESSION);
		}
		return SUCCESS;
	}
	
	
	// Getters & setters
	
	public void setSession(Map<String, Object> session) { this.session = session; }

	public void setOperatorService(OperatorService operatorService) { this.operatorService = operatorService; }

	public void setUserService(UserService userService) { this.userService = userService; }

	public void setOperatorChooser(OperatorChooser operatorChooser) { this.operatorChooser = operatorChooser; }
				
}
