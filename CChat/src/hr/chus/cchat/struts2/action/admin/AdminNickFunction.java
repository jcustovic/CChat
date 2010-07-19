package hr.chus.cchat.struts2.action.admin;

import java.util.LinkedHashMap;
import java.util.Map;

import hr.chus.cchat.db.service.NickService;
import hr.chus.cchat.model.db.jpa.Nick;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class AdminNickFunction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	private Log log = LogFactory.getLog(getClass());
	
	private NickService nickService;
	private Nick nick;
	private String operation;
	private Map<String, String> errorFields;
	private String status;
	
	
	@Override
	public String execute() throws Exception {
		log.debug("AdminNickFunction fired...");
		if (operation.equals("save/edit")) {
			nick = nickService.updateNick(nick);
		} else if (operation.equals("delete")) {
			nick = nickService.getNickById(nick.getId());
			nickService.removeNick(nick);
		} else {
			log.warn("Unsupported operation: " + operation);
		}
		return SUCCESS;
	}
	
	@Override
	public void validate() {
		log.debug("Validate...");
		errorFields = new LinkedHashMap<String, String>();
		if (nick == null) {
			errorFields.put("nick", getText("nick.null"));
		} else if (operation == null) {
		} else if (operation.equals("save/edit")) {
			if (nick.getName() == null || nick.getName().length() == 0) {
				errorFields.put("nick.name", getText("nick.name.empty"));
			} else if (nick.getName().length() > 20) {
				errorFields.put("nick.name", getText("nick.name.toLong", new String[] { "20" }));
			} else if (nickService.checkIfNameExists(nick)) {
				errorFields.put("nick.name", getText("nick.name.exists"));
			}
			if (nick.getDescription().length() > 300) {
				errorFields.put("nick.description", getText("nick.description.toLong", new String[] { "300" }));
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

	public Nick getNick() { return nick; }
	public void setNick(Nick nick) { this.nick = nick; }

	public String getOperation() { return operation; }
	public void setOperation(String operation) { this.operation = operation; }

	public void setNickService(NickService nickService) { this.nickService = nickService; }
	
	public Map<String, String> getErrorFields() { return errorFields; }
	public void setErrorFields(Map<String, String> errorFields) { this.errorFields = errorFields; }

	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }

}
