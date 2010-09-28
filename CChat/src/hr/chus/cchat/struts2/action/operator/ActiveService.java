package hr.chus.cchat.struts2.action.operator;

import hr.chus.cchat.db.service.OperatorService;
import hr.chus.cchat.db.service.UserService;
import hr.chus.cchat.helper.UserAware;
import hr.chus.cchat.model.db.jpa.Operator;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class ActiveService extends ActionSupport implements UserAware {

	private static final long serialVersionUID = 1L;

	private Operator operator;
	private OperatorService operatorService;
	private UserService userService;
	private Boolean newStatus;
	private boolean active;

	
	@Override
	public String execute() throws Exception {
		if (newStatus != null) {
			operator.setIsActive(newStatus);
			operator = operatorService.updateOperator(operator);
			if (operator.getIsActive()) {
				userService.assignUsersWithNewMsgToOperator(operator);
			}
		}
		active = operator.getIsActive();
		return SUCCESS;
	}


	// Getters & setters

	@Override
	public void setAuthenticatedUser(Operator user) { this.operator = user; }
	
	public void setOperatorService(OperatorService operatorService) { this.operatorService = operatorService; }
	
	public void setUserService(UserService userService) { this.userService = userService; }

	public void setNewStatus(Boolean newStatus) { this.newStatus = newStatus; }

	public boolean isActive() { return active; }

}
