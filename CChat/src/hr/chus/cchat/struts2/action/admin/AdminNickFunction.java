package hr.chus.cchat.struts2.action.admin;

import java.util.LinkedList;
import java.util.List;

import hr.chus.cchat.db.service.NickService;
import hr.chus.cchat.model.db.jpa.Nick;
import hr.chus.cchat.model.gwt.ext.FormPanelError;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionSupport;

public class AdminNickFunction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	private Log log = LogFactory.getLog(getClass());
	
	private NickService nickService;
	private Nick nick;
	private String operation;
	private List<FormPanelError> errors;
	private boolean success;
	
	
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
		errors = new LinkedList<FormPanelError>();
		if (nick == null) {
			errors.add(new FormPanelError("nick", getText("nick.null")));
		} else if (operation == null) {
		} else if (operation.equals("save/edit")) {
			if (nick.getName() == null || nick.getName().length() == 0) {
				errors.add(new FormPanelError("nick.name", getText("nick.name.empty")));
			} else if (nick.getName().length() > 20) {
				errors.add(new FormPanelError("nick.name", getText("nick.name.toLong", new String[] { "20" })));
			} else if (nickService.checkIfNameExists(nick)) {
				errors.add(new FormPanelError("nick.name", getText("nick.name.exists")));
			}
			if (nick.getDescription().length() > 300) {
				errors.add(new FormPanelError("nick.description", getText("nick.description.toLong", new String[] { "300" })));
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

	public Nick getNick() { return nick; }
	public void setNick(Nick nick) { this.nick = nick; }

	public String getOperation() { return operation; }
	public void setOperation(String operation) { this.operation = operation; }

	// TODO: FIX - Should be getErrors(). Had to be done this way because of GWT JSON build in validation.
	public List<FormPanelError> geterrors() { return errors; }
	public void setErrors(List<FormPanelError> errors) { this.errors = errors; }

	public boolean isSuccess() { return success; }

	public void setNickService(NickService nickService) { this.nickService = nickService; }

}
