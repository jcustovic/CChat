package hr.chus.cchat.struts2.action.operator;

import java.util.List;

import hr.chus.cchat.db.service.OperatorService;
import hr.chus.cchat.helper.UserAware;
import hr.chus.cchat.model.db.jpa.Operator;

import com.opensymphony.xwork2.ActionSupport;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class OperatorOperatorList extends ActionSupport implements UserAware {
	
	private static final long serialVersionUID = 1L;
	
	private Operator user;
	private OperatorService operatorService;
	private List<Operator> operatorList;

	@SuppressWarnings("unchecked")
	@Override
	public String execute() {
		if (user.getRole().getName().equals("operator")) {
			operatorList = Arrays.asList(new Operator[] { user });
		} else {
			operatorList = operatorService.getAllOperators();
		}
		return SUCCESS;
	}
	

	
	// Getters & setters
	
	@Override
	public void setAuthenticatedUser(Operator user) { this.user = user; }
	
	public void setOperatorService(OperatorService operatorService) { this.operatorService = operatorService; }

	public List<Operator> getOperatorList() { return operatorList; }
	public void setOperatorList(List<Operator> operatorList) { this.operatorList = operatorList; }

}
