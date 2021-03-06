package hr.chus.cchat.struts2.action.common;

import hr.chus.cchat.ApplicationConstants;
import hr.chus.cchat.model.db.jpa.Operator;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action return user information from session.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
public class GetUserInfoAction extends ActionSupport implements SessionAware {

    private Map<String, Object> session;
    private Operator            operator;

    @Override
    public String execute() {
        operator = (Operator) session.get(ApplicationConstants.SESSION_USER_KEY);

        return SUCCESS;
    }

    // Getters & setters

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

}
