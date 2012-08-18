package hr.chus.cchat.struts2.action.common;

import hr.chus.cchat.ApplicationConstants;
import hr.chus.cchat.db.service.OperatorService;
import hr.chus.cchat.db.service.UserService;
import hr.chus.cchat.helper.OperatorChooser;
import hr.chus.cchat.model.db.jpa.Operator;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that logs out user (removes user session info).
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
public class Logout extends ActionSupport implements SessionAware {

    private static final Logger LOG = LoggerFactory.getLogger(Logout.class);

    @Autowired
    private OperatorService     operatorService;

    @Autowired
    private UserService         userService;

    @Autowired
    private OperatorChooser     operatorChooser;

    private Map<String, Object> session;

    @Override
    public String execute() throws Exception {
        final Operator user = (Operator) session.get(ApplicationConstants.SESSION_USER_KEY);
        if (user != null) {
            user.setIsActive(false);
            operatorChooser.removeActiveOperator(user);
            userService.clearOperatorField(user);
            operatorService.updateOperator(user);
            LOG.info(user.getUsername() + " (id=" + user.getId() + ") logged out");
            session.remove(ApplicationConstants.SESSION_USER_KEY);
        }

        return SUCCESS;
    }

    // Getters & setters

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

}
