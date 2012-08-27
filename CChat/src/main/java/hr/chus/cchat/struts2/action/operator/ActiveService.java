package hr.chus.cchat.struts2.action.operator;

import hr.chus.cchat.db.service.OperatorService;
import hr.chus.cchat.helper.OperatorChooser;
import hr.chus.cchat.helper.UserAware;
import hr.chus.cchat.model.db.jpa.Operator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that activates or deactivates operator. When operator is active he/she can
 * receive messages.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
public class ActiveService extends ActionSupport implements UserAware {

    private static final Logger LOG = LoggerFactory.getLogger(ActiveService.class);

    @Autowired
    private OperatorService     operatorService;

    @Autowired
    private OperatorChooser     operatorChooser;

    private Operator            operator;
    private Boolean             newStatus;
    private boolean             active;

    @Override
    public String execute() throws Exception {
        if (newStatus != null) {
            operator.setIsActive(newStatus);
            operator = operatorService.updateOperator(operator);
            if (operator.getIsActive()) {
                LOG.debug("Operator activated");
                operatorChooser.addActiveOperator(operator);
            } else {
                LOG.debug("Operator deactivated");
                operatorChooser.removeActiveOperator(operator);
            }
        }
        active = operator.getIsActive();

        return SUCCESS;
    }

    // Getters & setters

    @Override
    public final void setAuthenticatedUser(final Operator p_user) {
        this.operator = p_user;
    }

    public final void setNewStatus(final Boolean p_newStatus) {
        this.newStatus = p_newStatus;
    }

    public final boolean isActive() {
        return active;
    }

}
