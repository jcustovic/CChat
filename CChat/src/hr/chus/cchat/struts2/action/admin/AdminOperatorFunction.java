package hr.chus.cchat.struts2.action.admin;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hr.chus.cchat.db.service.OperatorService;
import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.gwt.ext.FormPanelError;
import hr.chus.cchat.util.StringUtil;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * 
 * @author chus
 *
 */
public class AdminOperatorFunction extends ActionSupport implements Preparable {

	private static final long serialVersionUID = 1L;
	private Log log = LogFactory.getLog(getClass());
	
	private OperatorService operatorService;
	private Operator operator;
	private String operation;
	private List<FormPanelError> errors;
	private boolean success;
	
	@Override
	public void prepare() throws Exception { }
	
	@Override
	public String execute() throws Exception {
		log.debug("AdminOperatorFunction fired...");
		if (operation.equals("save/edit")) {
			operator = operatorService.updateOperator(operator);
		} else if (operation.equals("delete")) {
			operator = operatorService.getOperatorById(operator.getId());
			operatorService.removeOperator(operator);
		} else {
			log.warn("Unsupported operation: " + operation);
		}
		return SUCCESS;
	}
	
	@Override
	public void validate() {
		log.debug("Validate...");
		errors = new LinkedList<FormPanelError>();
		if (operator == null) {
			errors.add(new FormPanelError("operator", getText("operator.null")));
		} else if (operation == null) {
		} else if (operation.equals("save/edit")) {
			if (operator.getDisabled() == null) operator.setDisabled(false);
			if (operator.getIsActive() == null) operator.setIsActive(false);
			
			if (operator.getUsername() == null || operator.getUsername().isEmpty()) {
				errors.add(new FormPanelError("operator.username", getText("operator.username.empty")));
			} else if (operator.getUsername().length() > 30) {
				errors.add(new FormPanelError("operator.username", getText("operator.username.toLong", new String[] { "30" })));
			} else if (operatorService.checkIfUsernameExists(operator)) {
				errors.add(new FormPanelError("operator.username", getText("operator.username.exists")));
			}
			
			if (operator.getRole() == null) {
				errors.add(new FormPanelError("operator.role", getText("operator.role.empty")));
			}
			
			if (operator.getId() == null && operator.getPassword() != null && operator.getPassword().length() > 15) {
				errors.add(new FormPanelError("operator.password", getText("operator.password.toLong", new String[] { "15" })));
			}
			
			if (operator.getName() != null && operator.getName().length() > 20) {
				errors.add(new FormPanelError("operator.name", getText("operator.name.toLong", new String[] { "20" })));
			}
			
			if (operator.getSurname() != null && operator.getSurname().length() > 30) {
				errors.add(new FormPanelError("operator.surname", getText("operator.surname.toLong", new String[] { "30" })));
			}
			
			if (operator.getEmail() != null && operator.getEmail().length() > 50) {
				errors.add(new FormPanelError("operator.email", getText("operator.email.toLong", new String[] { "50" })));
			}
		}
		
		if (operator.getId() != null) {
			String currentPassword = operatorService.getOperatorById(operator.getId()).getPassword();
			if (operator.getPassword() != null && !operator.getPassword().equals(currentPassword)) {
				operator.setPassword(StringUtil.encodePassword(operator.getPassword(), "SHA"));
			} else {
				operator.setPassword(currentPassword);
			}
		} else {
			if (operator.getPassword() == null || operator.getPassword().length() == 0) {
				errors.add(new FormPanelError("operator.password", getText("operator.password.empty")));
			} else {
				operator.setPassword(StringUtil.encodePassword(operator.getPassword(), "SHA"));
			}
		}
		
		if (errors.size() == 0) {
			errors = null;
			success = true;
		} else {
			addActionError(errors.size() + " errors found!");
			success = false;
		}
	}
	
	
	// Getters & setters
	
	public void setOperatorService(OperatorService operatorService) { this.operatorService = operatorService; }	
	
	public Operator getOperator() { return operator; }
	public void setOperator(Operator operator) { this.operator = operator; }

	// TODO: FIX - Should be getErrors(). Had to be done this way because of GWT JSON build in validation.
	public List<FormPanelError> geterrors() { return errors; }
	public void setErrors(List<FormPanelError> errors) { this.errors = errors; }

	public void setOperation(String operation) { this.operation = operation; }

	public boolean isSuccess() { return success; }
	public void setSuccess(boolean success) { this.success = success; }
	
}
 