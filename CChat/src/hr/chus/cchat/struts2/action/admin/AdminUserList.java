package hr.chus.cchat.struts2.action.admin;

import java.util.List;

import hr.chus.cchat.db.service.UserService;
import hr.chus.cchat.model.db.jpa.Nick;
import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.db.jpa.ServiceProvider;
import hr.chus.cchat.model.db.jpa.User;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author Jan Čustović
 *
 */
public class AdminUserList extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	
	private UserService userService;
	private List<User> userList;
	private Nick nick;
	private Operator operator;
	private ServiceProvider serviceProvider;
	private String msisdn;
	private String name;
	private String surname;
	private int start;
	private int limit;


	@Override
	public String execute() throws Exception {
		userList = userService.searchUsers(nick, operator, serviceProvider, msisdn, name, surname, start, limit);
		return SUCCESS;
	}

	
	// Getters & setters
	
	public UserService getUserService() { return userService; }
	public void setUserService(UserService userService) { this.userService = userService; }

	public List<User> getUserList() { return userList; }
	public void setUserList(List<User> userList) { this.userList = userList; }

	public Nick getNick() { return nick; }
	public void setNick(Nick nick) { this.nick = nick; }

	public Operator getOperator() { return operator; }
	public void setOperator(Operator operator) { this.operator = operator; }
	
	public ServiceProvider getServiceProvider() { return serviceProvider; }
	public void setServiceProvider(ServiceProvider serviceProvider) { this.serviceProvider = serviceProvider; }

	public String getMsisdn() { return msisdn; }
	public void setMsisdn(String msisdn) { this.msisdn = msisdn; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getSurname() { return surname; }
	public void setSurname(String surname) { this.surname = surname; }

	public int getStart() { return start; }
	public void setStart(int start) { this.start = start; }

	public int getLimit() { return limit; }
	public void setLimit(int limit) { this.limit = limit; }	
			
}
