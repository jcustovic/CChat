package hr.chus.cchat.struts2.action.admin;

import java.util.List;

import hr.chus.cchat.db.service.OperatorService;
import hr.chus.cchat.model.db.jpa.Operator;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that returns all operators.
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class AdminOperatorList extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	
	private OperatorService operatorService;
	
	private List<Operator> operatorList;

	
	@Override
	public String execute() {
		operatorList = operatorService.getAllOperators();
		return SUCCESS;
	}
	

	// Getters & setters
	
	public void setOperatorService(OperatorService operatorService) { this.operatorService = operatorService; }

	public List<Operator> getOperatorList() { return operatorList; }
	public void setOperatorList(List<Operator> operatorList) { this.operatorList = operatorList; }

}
