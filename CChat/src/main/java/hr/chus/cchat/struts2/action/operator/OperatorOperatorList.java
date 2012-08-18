package hr.chus.cchat.struts2.action.operator;

import hr.chus.cchat.db.service.OperatorService;
import hr.chus.cchat.helper.UserAware;
import hr.chus.cchat.model.db.jpa.Operator;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that returns all operators. If operator role is "operator" he will not see
 * other operators (action will return only one operator - him/here).
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
public class OperatorOperatorList extends ActionSupport implements UserAware {

    @Autowired
    private OperatorService operatorService;

    private Operator        user;
    private List<Operator>  operatorList;

    @Override
    public String execute() {
        if (user.getRole().getName().equals("operator")) {
            operatorList = Arrays.asList(new Operator[] { user });
        } else {
            operatorList = operatorService.getAllOperators();
        }

        return SUCCESS;
    }

    // Getters & setters

    @Override
    public void setAuthenticatedUser(Operator user) {
        this.user = user;
    }

    public List<Operator> getOperatorList() {
        return operatorList;
    }

    public void setOperatorList(List<Operator> operatorList) {
        this.operatorList = operatorList;
    }

}
