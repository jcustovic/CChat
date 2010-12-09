package hr.chus.cchat.struts2.action.admin;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hr.chus.cchat.db.service.UserService;
import hr.chus.cchat.model.db.jpa.Nick;
import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.db.jpa.ServiceProvider;
import hr.chus.cchat.model.db.jpa.User;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that searches users using different parameters. Supports pagination.
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class AdminUserList extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	
	private Log log = LogFactory.getLog(getClass());
	
	private UserService userService;
	private Nick nick;
	private Operator operator;
	private ServiceProvider serviceProvider;
	private String msisdn;
	private String name;
	private String surname;
	private Boolean deleted;
	private Integer id;
	private int start;
	private int limit;
	
	private List<User> userList;
	private Long totalCount;


	@SuppressWarnings("unchecked")
	@Override
	public String execute() {
		log.info("Searching for users from ..." + start + " for " + limit);
		Object[] result = userService.searchUsers(nick, operator, serviceProvider, msisdn, id, name, surname, deleted, start, limit);
		totalCount = (Long) result[0];
		userList = (List<User>) result[1];
		log.debug("Found " + totalCount + " users.");
		return SUCCESS;
	}

	
	// Getters & setters
	
	public void setUserService(UserService userService) { this.userService = userService; }

	public List<User> getUserList() { return userList; }
	public void setUserList(List<User> userList) { this.userList = userList; }

	public void setNick(Nick nick) { this.nick = nick; }

	public void setOperator(Operator operator) { this.operator = operator; }
	
	public void setServiceProvider(ServiceProvider serviceProvider) { this.serviceProvider = serviceProvider; }

	public void setMsisdn(String msisdn) { this.msisdn = msisdn; }

	public void setName(String name) { this.name = name; }

	public void setSurname(String surname) { this.surname = surname; }
	
	public void setDeleted(Boolean deleted) { this.deleted = deleted; }

	public void setId(Integer id) { this.id = id; }

	public void setStart(int start) { this.start = start; }

	public void setLimit(int limit) { this.limit = limit; }

	public Long getTotalCount() { return totalCount; }
	public void setTotalCount(Long totalCount) { this.totalCount = totalCount; }	
					
}
