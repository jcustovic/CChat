package hr.chus.cchat.struts2.action.operator;

import hr.chus.cchat.db.service.UserService;
import hr.chus.cchat.helper.UserAware;
import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.db.jpa.User;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that return users that are assigned to operator (users that operator communicates with),
 * newest user that do not have assigned operator that contacted this application in the last 48 hours so operators
 * can contact them and random users that are randomly grabbed from the database so operators can contact them.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
public class UserList extends ActionSupport implements UserAware {

    private static final Logger LOG         = LoggerFactory.getLogger(UserList.class);

    private static final long   NEWEST_TIME = 172800000L;                             // 48 hours

    @Autowired
    private UserService         userService;

    private Operator            operator;
    private String              errorMsg;
    private List<User>          operatorUserList;
    private List<User>          newestUserList;
    private List<User>          randomUserList;

    @Override
    public String execute() {
        if (operator.getDisabled()) { // This should not happen. When user is disabled he must not have active session!
            errorMsg = getText("operator.disabled");
            return ERROR;
        }
        if (!operator.getIsActive()) {
            errorMsg = getText("operator.notActive");
            return ERROR;
        }
        LOG.debug("Operator {} fetching new user list...", operator.getUsername());

        operatorUserList = userService.getByOperator(operator);
        Date lastMsgDate = new Date(new Date().getTime() - NEWEST_TIME);
        newestUserList = userService.getNewest(lastMsgDate, 500);
        randomUserList = userService.getRandom(lastMsgDate, 10);

        LOG.debug("Operator {} done fetching.", operator.getUsername());

        return SUCCESS;
    }

    // Getters & setters

    @Override
    public void setAuthenticatedUser(Operator user) {
        operator = user;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public List<User> getOperatorUserList() {
        return operatorUserList;
    }

    public List<User> getNewestUserList() {
        return newestUserList;
    }

    public List<User> getRandomUserList() {
        return randomUserList;
    }

}
