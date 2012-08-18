package hr.chus.cchat.struts2.interceptor;

import hr.chus.cchat.ApplicationConstants;
import hr.chus.cchat.helper.UserAware;
import hr.chus.cchat.model.db.jpa.Operator;

import java.util.Map;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 * Struts2 custom authentication interceptor. Before executing protected actions this interceptor
 * check if there is user information in session (is user logged in) and if not redirects to
 * login page.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
public class AuthenticateInterceptor implements Interceptor {

    private String role;

    @Override
    public void destroy() {}

    @Override
    public void init() {}

    @Override
    public String intercept(ActionInvocation actionInvocation) throws Exception {
        Map<?, ?> session = actionInvocation.getInvocationContext().getSession();
        Operator user = (Operator) session.get(ApplicationConstants.SESSION_USER_KEY);
        if (user != null && user.getRole().getName().equals(getRole())) {
            Action action = (Action) actionInvocation.getAction();
            if (action instanceof UserAware) {
                ((UserAware) action).setAuthenticatedUser(user);
            }
            return actionInvocation.invoke();
        } else {
            return Action.LOGIN;
        }
    }

    // Getters & setters

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
