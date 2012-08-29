package hr.chus.cchat.struts2.action.common;

import hr.chus.cchat.ApplicationConstants;
import hr.chus.cchat.db.service.OperatorService;
import hr.chus.cchat.listener.SessionListener;
import hr.chus.cchat.model.db.jpa.Operator;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action trys to login user using username and password.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
public class Login extends ActionSupport implements SessionAware, ServletRequestAware {

    private static final Logger LOG = LoggerFactory.getLogger(Login.class);

    @Autowired
    private OperatorService     operatorService;

    @Value("${app.version}")
    private String              appVersion;

    private Map<String, Object> session;
    private HttpServletRequest  request;

    private String              username;
    private String              password;

    @Override
    public final String execute() throws Exception {
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

        // If user is not found in session try to log in
        user = operatorService.authenticateUser(getUsername(), getPassword());
        if (user == null) {
            addFieldError("username", getText("login.wrongUsernameOrPassword"));
        } else if (user.getDisabled()) {
            addFieldError("username", getText("login.userIsDisabled"));
        } else {
            final String roleName;

            if (user.getRole().getName().equals(ApplicationConstants.OPERATOR)) {
                roleName = ApplicationConstants.OPERATOR;
                SessionListener.removeSessionWithUser(user);
            } else if (user.getRole().getName().equals(ApplicationConstants.MODERATOR)) {
                roleName = ApplicationConstants.MODERATOR;
                SessionListener.removeSessionWithUser(user);
            } else if (user.getRole().getName().equals(ApplicationConstants.ADMIN)) {
                roleName = ApplicationConstants.ADMIN;
            } else {
                LOG.error("Unknown role {}", user.getRole().getName());
                return INPUT;
            }

            session.put(ApplicationConstants.SESSION_USER_KEY, user);
            LOG.info("{} (id={}, sessionId={}) logged in as {}", new Object[] { user.getUsername(), user.getId(), request.getSession(true).getId(), roleName });

            return roleName;
        }

        return INPUT;
    }

    @Override
    public final void validate() {
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
    public final void setSession(final Map<String, Object> p_session) {
        this.session = p_session;
    }

    @Override
    public final void setServletRequest(final HttpServletRequest p_request) {
        this.request = p_request;
    }

    public final String getUsername() {
        return username;
    }

    public final void setUsername(final String p_username) {
        this.username = p_username;
    }

    public final String getPassword() {
        return password;
    }

    public final void setPassword(final String p_password) {
        this.password = p_password;
    }

    public final String getAppVersion() {
        return appVersion;
    }

}
