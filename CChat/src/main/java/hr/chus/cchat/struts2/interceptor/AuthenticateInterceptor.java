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

    private String[] roles;

    @Override
    public void destroy() {}

    @Override
    public void init() {}

    @Override
    public final String intercept(ActionInvocation actionInvocation) throws Exception {
        final Map<?, ?> session = actionInvocation.getInvocationContext().getSession();
        final Operator user = (Operator) session.get(ApplicationConstants.SESSION_USER_KEY);
        if (user != null && containsRole(user.getRole().getName())) {
            Action action = (Action) actionInvocation.getAction();
            if (action instanceof UserAware) {
                ((UserAware) action).setAuthenticatedUser(user);
            }
            return actionInvocation.invoke();
        } else {
            return Action.LOGIN;
        }
    }

    private boolean containsRole(final String p_roleName) {
        for (final String role : roles) {
            if (p_roleName.equalsIgnoreCase(role)) {
                return true;
            }
        }

        return false;
    }

    // Getters & setters

    public final void setRole(final String p_role) {
        roles = p_role.split(",");
    }

}
