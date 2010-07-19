package hr.chus.cchat.struts2.action.admin;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hr.chus.cchat.db.service.OperatorService;
import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.util.StringUtil;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class AdminOperatorFunction extends ActionSupport implements Preparable {

	private static final long serialVersionUID = 1L;
	private Log log = LogFactory.getLog(getClass());
	
	private OperatorService operatorService;
	private Operator operator;
	private String operation;
	private Map<String, String> errorFields;
	private String status;
	
	@Override
	public void prepare() throws Exception { }
	
	@Override
	public String execute() throws Exception {
		log.debug("AdminOperatorFunction fired...");
		if (operation == null) {
			log.error("Operation must not be null.");
		} else if (operation.equals("save/edit")) {
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
		errorFields = new LinkedHashMap<String, String>();
		if (operator == null) {
			errorFields.put("operator", getText("operator.null"));
		} else if (operation == null) {
		} else if (operation.equals("save/edit")) {
			if (operator.getDisabled() == null) operator.setDisabled(false);
			if (operator.getIsActive() == null) operator.setIsActive(false);
			
			if (operator.getUsername() == null || operator.getUsername().isEmpty()) {
				errorFields.put("operator.username", getText("operator.username.empty"));
			} else if (operator.getUsername().length() > 30) {
				errorFields.put("operator.username", getText("operator.username.toLong", new String[] { "30" }));
			} else if (operatorService.checkIfUsernameExists(operator)) {
				errorFields.put("operator.username", getText("operator.username.exists"));
			}
			
			if (operator.getRole() == null) {
				errorFields.put("operator.role", getText("operator.role.empty"));
			}
			
			if (operator.getId() == null && operator.getPassword() != null && operator.getPassword().length() > 15) {
				errorFields.put("operator.password", getText("operator.password.toLong", new String[] { "15" }));
			}
			
			if (operator.getName() != null && operator.getName().length() > 20) {
				errorFields.put("operator.name", getText("operator.name.toLong", new String[] { "20" }));
			}
			
			if (operator.getSurname() != null && operator.getSurname().length() > 30) {
				errorFields.put("operator.surname", getText("operator.surname.toLong", new String[] { "30" }));
			}
			
			if (operator.getEmail() != null && operator.getEmail().length() > 50) {
				errorFields.put("operator.email", getText("operator.email.toLong", new String[] { "50" }));
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
				errorFields.put("operator.password", getText("operator.password.empty"));
			} else {
				operator.setPassword(StringUtil.encodePassword(operator.getPassword(), "SHA"));
			}
		}
		
		if (errorFields.size() == 0) {
			errorFields = null;
			status = "validation_ok";
		} else {
			addActionError(errorFields.size() + " errors found!");
			status = "validation_error";
		}
	}
	
	
	// Getters & setters
	
	public void setOperatorService(OperatorService operatorService) { this.operatorService = operatorService; }	
	
	public Operator getOperator() { return operator; }
	public void setOperator(Operator operator) { this.operator = operator; }
	
	public void setOperation(String operation) { this.operation = operation; }

	public Map<String, String> getErrorFields() { return errorFields; }
	public void setErrorFields(Map<String, String> errorFields) { this.errorFields = errorFields; }

	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }
	
}
 