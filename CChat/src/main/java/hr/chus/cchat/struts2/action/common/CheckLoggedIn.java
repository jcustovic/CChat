package hr.chus.cchat.struts2.action.common;

import hr.chus.cchat.ApplicationConstants;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that check if user session exists (is user logged in).
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
public class CheckLoggedIn extends ActionSupport implements SessionAware {

    private Map<String, Object> session;
    private boolean             loggedIn;

    @Override
    public String execute() throws Exception {
        if (session.get(ApplicationConstants.SESSION_USER_KEY) == null) {
            loggedIn = false;
        } else {
            loggedIn = true;
        }
        
        return SUCCESS;
    }

    // Getters & setters

    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

}
