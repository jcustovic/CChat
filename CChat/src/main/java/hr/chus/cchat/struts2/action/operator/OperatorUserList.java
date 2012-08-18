package hr.chus.cchat.struts2.action.operator;

import hr.chus.cchat.db.service.UserService;
import hr.chus.cchat.helper.UserAware;
import hr.chus.cchat.model.db.jpa.Nick;
import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.db.jpa.User;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that searches users using different parameters. Supports pagination.
 * User that have the role of "operators" can only search users that they are assigned to.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
public class OperatorUserList extends ActionSupport implements UserAware {

    private static final Logger LOG = LoggerFactory.getLogger(OperatorUserList.class);

    @Autowired
    private UserService         userService;

    private Operator            user;
    private Nick                nick;
    private Operator            operator;
    private String              name;
    private String              surname;
    private Integer             id;
    private int                 start;
    private int                 limit;
    private List<User>          userList;
    private Long                totalCount;

    @SuppressWarnings("unchecked")
    @Override
    public String execute() {
        if (user.getRole().getName().equals("operator")) operator = user;
        LOG.info("Searching for users from ..." + start + " for " + limit);
        Object[] result = userService.searchUsers(nick, operator, null, null, id, name, surname, false, start, limit);
        totalCount = (Long) result[0];
        userList = (List<User>) result[1];
        LOG.debug("Found " + totalCount + " users.");
        return SUCCESS;
    }

    // Getters & setters

    @Override
    public void setAuthenticatedUser(Operator user) {
        this.user = user;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public void setNick(Nick nick) {
        this.nick = nick;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

}
