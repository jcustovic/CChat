package hr.chus.cchat.struts2.action.operator;

import hr.chus.cchat.db.service.UserService;
import hr.chus.cchat.helper.UserAware;
import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.db.jpa.User;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that implements user functions. Thru this web service users can be created,
 * updated or deleted (dao services are invoked).
 * User that have the role of "operators" can only update users that they are assigned to.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
public class OperatorUserFunction extends ActionSupport implements UserAware {

    private static final Logger LOG = LoggerFactory.getLogger(OperatorUserFunction.class);

    @Autowired
    private UserService         userService;

    private String              operation;
    private User                user;
    private Map<String, String> errorFields;
    private Boolean             status;
    private String              errorMsg;
    private Operator            operator;

    @Override
    public void validate() {
        if (operation != null && operation.equals("get")) return;
        errorFields = new HashMap<String, String>();
        if (operation == null) {
            LOG.warn("Operation must not be null.");
            errorFields.put("operation", getText("operation.empty"));
        } else if (!(operation.equals("update") || operation.equals("get"))) {
            LOG.warn("Unsupported operation: " + operation);
            errorFields.put("operation", getText("operation.notSupported"));
        } else if (user == null) {
            errorMsg = "User must be set";
        } else if (operator.getRole().getName().equals("operator") && (user.getOperator() == null || !user.getOperator().equals(operator))) {
            errorMsg = "Operator " + operator.getUsername() + " can't change/get user with id " + user.getId() + " because he isn't assigned to that user.";
            LOG.warn("Operator " + operator.getUsername() + " can't change/get user with id " + user.getId() + " because he isn't assigned to that user.");
            errorFields.put("user.operator", getText("user.notAllowedToEdit"));
        }
        if (errorMsg != null || errorFields.size() > 0) {
            addActionError(errorMsg + "; Error count: " + errorFields.size());
            status = false;
        }
    }

    @Override
    public String execute() throws Exception {
        if (operation.equals("update")) {
            LOG.debug("Updating user " + user + " ...");
            user = userService.editUserOperator(user);
        }
        status = true;
        return SUCCESS;
    }

    // Getters & setters

    @Override
    public void setAuthenticatedUser(Operator operator) {
        this.operator = operator;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Map<String, String> getErrorFields() {
        return errorFields;
    }

    public void setErrorFields(Map<String, String> errorFields) {
        this.errorFields = errorFields;
    }

    public Boolean getStatus() {
        return status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

}
