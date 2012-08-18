package hr.chus.cchat.struts2.action.admin;

import hr.chus.cchat.db.service.UserService;
import hr.chus.cchat.model.db.jpa.User;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that implements user functions. Thru this web service users can be created,
 * updated or deleted (dao services are invoked).
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
public class AdminUserFunction extends ActionSupport {

    private static final Logger LOG = LoggerFactory.getLogger(AdminUserFunction.class);

    @Autowired
    private UserService         userService;

    private String              operation;
    private User                user;
    private Map<String, String> errorFields;
    private String              status;

    @Override
    public String execute() throws Exception {
        if (operation != null && operation.equals("update")) {
            LOG.debug("Updating user " + user + " ...");
            user = userService.editUserAdmin(user);
        }
        
        return SUCCESS;
    }

    // Getters & setters

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
