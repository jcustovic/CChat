package hr.chus.cchat.struts2.action.common;

import hr.chus.cchat.ApplicationConstants;
import hr.chus.cchat.model.db.jpa.Operator;

import java.util.Date;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public class GetUserInfoAction extends ActionSupport implements SessionAware {
	
	private static final long serialVersionUID = 1L;
	
	private Map session;
	private Operator operator;
	private Date date;
	
	@Override
	public String execute() {
		operator = (Operator) session.get(ApplicationConstants.USER_SESSION);
		date = new Date();
		return SUCCESS;
	}

	
	
	// Getters & setters
	
	@Override
	public void setSession(Map<String, Object> session) { this.session = session; }

	public Operator getOperator() { return operator; }
	public void setOperator(Operator operator) { this.operator = operator; }

	public Date getDate() { return date; }
	public void setDate(Date date) { this.date = date; }
		
}
