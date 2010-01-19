package hr.chus.cchat.struts2.interceptor;

import hr.chus.cchat.ApplicationConstants;
import hr.chus.cchat.helper.UserAware;
import hr.chus.cchat.model.db.jpa.Operator;

import java.util.Map;



import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 * Razred predstavlja presretač u Struts2 okviru zaduženog za autorizaciju
 * korisnika.
 * 
 * @see Operator
 * @see RoleJPA
 * @author Jan Čustović
 * 
 */
public class AuthenticateInterceptor implements Interceptor {

	private static final long serialVersionUID = 1L;
	
	private String role;
	
	public String getRole() { return role; }
	public void setRole(String role) { this.role = role; }

	@Override
	public void destroy() {
	}

	@Override
	public void init() {
	}

	@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception {
		Map<?, ?> session = actionInvocation.getInvocationContext().getSession();
		Operator user = (Operator) session.get(ApplicationConstants.USER_SESSION);
		if (user != null && user.getRole().getName().equals(getRole())) {
			Action action = (Action) actionInvocation.getAction();
			if (action instanceof UserAware) {
				((UserAware) action).setUser(user);
			}
			return actionInvocation.invoke();
		} else {
			return Action.LOGIN;
		}
	}

}
