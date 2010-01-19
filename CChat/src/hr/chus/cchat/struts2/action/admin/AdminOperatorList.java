package hr.chus.cchat.struts2.action.admin;

import java.util.List;

import hr.chus.cchat.db.service.OperatorService;
import hr.chus.cchat.model.db.jpa.Operator;

import com.opensymphony.xwork2.ActionSupport;
/**
 * 
 * @author chus
 *
 */
public class AdminOperatorList extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	
	private OperatorService operatorService;
	private List<Operator> operatorList;

	@Override
	public String execute() {
		operatorList = operatorService.getAllOperetors();
		return SUCCESS;
	}
	

	
	// Getters & setters
	
	public void setOperatorService(OperatorService operatorService) { this.operatorService = operatorService; }

	public List<Operator> getOperatorList() { return operatorList; }
	public void setOperatorList(List<Operator> operatorList) { this.operatorList = operatorList; }

}
