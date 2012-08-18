package hr.chus.cchat.struts2.action.operator;

import org.springframework.beans.factory.annotation.Autowired;

import hr.chus.cchat.db.service.OperatorService;
import hr.chus.cchat.db.service.UserService;
import hr.chus.cchat.helper.OperatorChooser;
import hr.chus.cchat.helper.UserAware;
import hr.chus.cchat.model.db.jpa.Operator;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that activates or deactivates operator. When operator is active he/she can
 * receive messages.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
public class ActiveService extends ActionSupport implements UserAware {

    @Autowired
    private OperatorService operatorService;

    @Autowired
    private UserService     userService;

    @Autowired
    private OperatorChooser operatorChooser;

    private Operator        operator;
    private Boolean         newStatus;
    private boolean         active;

    @Override
    public String execute() throws Exception {
        if (newStatus != null) {
            operator.setIsActive(newStatus);
            operator = operatorService.updateOperator(operator);
            if (operator.getIsActive()) {
                operatorChooser.addActiveOperator(operator);
                userService.assignUsersWithNewMsgToOperator(operator);
            } else {
                operatorChooser.removeActiveOperator(operator);
            }
        }
        active = operator.getIsActive();
        
        return SUCCESS;
    }

    // Getters & setters

    @Override
    public void setAuthenticatedUser(Operator user) {
        this.operator = user;
    }

    public void setNewStatus(Boolean newStatus) {
        this.newStatus = newStatus;
    }

    public boolean isActive() {
        return active;
    }

}
