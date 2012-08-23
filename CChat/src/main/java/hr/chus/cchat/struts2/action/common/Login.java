package hr.chus.cchat.struts2.action.common;

import hr.chus.cchat.ApplicationConstants;
import hr.chus.cchat.db.service.OperatorService;
import hr.chus.cchat.model.db.jpa.Operator;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action trys to login user using username and password.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
public class Login extends ActionSupport implements SessionAware {

    private static final Logger LOG = LoggerFactory.getLogger(Login.class);

    @Autowired
    private OperatorService     operatorService;

    private Map<String, Object> session;
    private String              username;
    private String              password;

    @Override
    public String execute() throws Exception {
        Operator user = (Operator) session.get(ApplicationConstants.SESSION_USER_KEY);
        if (user != null) {
            if (user.getRole().getName().equals(ApplicationConstants.OPERATOR)) {
                return ApplicationConstants.OPERATOR;
            } else if (user.getRole().getName().equals(ApplicationConstants.ADMIN)) {
                return ApplicationConstants.ADMIN;
            } else if (user.getRole().getName().equals(ApplicationConstants.MODERATOR)) {
                return ApplicationConstants.MODERATOR;
            }
        }
        if (getUsername() == null || getPassword() == null) {
            return INPUT;
        }

        user = operatorService.authenticateUser(getUsername(), getPassword());
        if (user == null) {
            addFieldError("username", getText("login.wrongUsernameOrPassword"));
        } else {
            if (user.getDisabled()) {
                addFieldError("username", getText("login.userIsDisabled"));
            } else if (user.getRole().getName().equals(ApplicationConstants.OPERATOR)) {
                session.put(ApplicationConstants.SESSION_USER_KEY, user);
                LOG.info(user.getUsername() + " (id=" + user.getId() + ") logged in as " + ApplicationConstants.OPERATOR);
                return ApplicationConstants.OPERATOR;
            } else if (user.getRole().getName().equals(ApplicationConstants.MODERATOR)) {
                session.put(ApplicationConstants.SESSION_USER_KEY, user);
                LOG.info(user.getUsername() + " (id=" + user.getId() + ") logged in as " + ApplicationConstants.MODERATOR);
                return ApplicationConstants.MODERATOR;
            } else if (user.getRole().getName().equals(ApplicationConstants.ADMIN)) {
                session.put(ApplicationConstants.SESSION_USER_KEY, user);
                LOG.info(user.getUsername() + " (id=" + user.getId() + ") logged in as " + ApplicationConstants.ADMIN);
                return ApplicationConstants.ADMIN;
            }
        }

        return INPUT;
    }

    @Override
    public void validate() {
        if (getUsername() != null && getPassword() != null) {
            if (getUsername().length() == 0) {
                addFieldError("username", getText("login.usernameRequired"));
            }
            if (getPassword().length() == 0) {
                addFieldError("password", getText("login.passwordRequired"));
            }
        }
    }

    // Getters & setters

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
