package hr.chus.cchat.struts2.action.admin;

import hr.chus.cchat.db.service.NickService;
import hr.chus.cchat.model.db.jpa.Nick;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that implements nick functions. Thru this web service nick can be created,
 * updated or deleted (dao services are invoked).
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
public class AdminNickFunction extends ActionSupport {

    private static final Logger LOG = LoggerFactory.getLogger(AdminNickFunction.class);

    @Autowired
    private NickService         nickService;

    private Nick                nick;
    private String              operation;
    private Map<String, String> errorFields;
    private String              status;

    @Override
    public String execute() throws Exception {
        if ("save/edit".equals(operation)) {
            nick = nickService.updateNick(nick);
        } else if ("delete".equals(operation)) {
            nick = nickService.getNickById(nick.getId());
            nickService.removeNick(nick);
        } else {
            LOG.warn("Unsupported operation: " + operation);
        }

        return SUCCESS;
    }

    @Override
    public void validate() {
        errorFields = new LinkedHashMap<String, String>();
        if (nick == null) {
            errorFields.put("nick", getText("nick.null"));
        } else if (!StringUtils.hasText(operation)) {
            LOG.warn("Operation must not be empty.");
            errorFields.put("operation", getText("operation.empty"));
        } else if (!("save/edit".equals(operation) || "delete".equals(operation))) {
            LOG.warn("Unsupported operation: " + operation);
            errorFields.put("operation", getText("operation.notSupported"));
        } else if ("save/edit".equals(operation)) {
            if (!StringUtils.hasText(nick.getName())) {
                errorFields.put("nick.name", getText("nick.name.empty"));
            } else if (nick.getName().length() > 20) {
                errorFields.put("nick.name", getText("nick.name.toLong", new String[] { "20" }));
            } else if (nickService.checkIfNameExists(nick)) {
                errorFields.put("nick.name", getText("nick.name.exists"));
            }
            if (nick.getDescription() != null && nick.getDescription().length() > 300) {
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

    public Nick getNick() {
        return nick;
    }

    public void setNick(Nick nick) {
        this.nick = nick;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Map<String, String> getErrorFields() {
        return errorFields;
    }

    public void setErrorFields(Map<String, String> errorFields) {
        this.errorFields = errorFields;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
