package hr.chus.cchat.listener;

import java.util.HashMap;

import hr.chus.cchat.ApplicationConstants;
import hr.chus.cchat.db.service.OperatorService;
import hr.chus.cchat.db.service.UserService;
import hr.chus.cchat.model.db.jpa.Operator;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * This class keeps track of Tomcat sessions (created or destroyed). We are interested in the part when
 * session gets destroyed so we can logout user and deactivate him if he forgets to logout or is 
 * inactive for session timeout period. 
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class SessionListener implements HttpSessionListener {
	
	private Log log = LogFactory.getLog(getClass());
	
	public static HashMap<String, HttpSession> sessionMap = new HashMap<String, HttpSession>();
	
	
	/**
	 * 
	 * @param userToRemove
	 */
	public static void removeSessionWithUser(Operator userToRemove) {
		for (String sessionId : sessionMap.keySet()) {
			HttpSession session = sessionMap.get(sessionId);
			Operator user = (Operator) session.getAttribute(ApplicationConstants.USER_SESSION);
			if (user != null && user.getId().equals(userToRemove.getId())) {
				session.invalidate();
				return;
			}
		}
	}
	
	/**
	 * 
	 * @param userToUpdate
	 */
	public static void updateSessionWithUser(Operator userToUpdate) {
		for (String sessionId : sessionMap.keySet()) {
			HttpSession session = sessionMap.get(sessionId);
			Operator user = (Operator) session.getAttribute(ApplicationConstants.USER_SESSION);
			if (user != null && user.getId().equals(userToUpdate.getId())) {
				session.setAttribute(ApplicationConstants.USER_SESSION, userToUpdate);
				return;
			}
		}
	}
	

	@Override
	public void sessionCreated(HttpSessionEvent sessionEvent) {
		HttpSession session = sessionEvent.getSession();
		log.debug("Creating session with id " + session.getId());
		sessionMap.put(session.getId(), session);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		HttpSession session = sessionEvent.getSession();
		log.debug("Destroying session with id " + session.getId());
		sessionMap.remove(session.getId());
		Operator user = (Operator) session.getAttribute(ApplicationConstants.USER_SESSION);
		if (user != null) {
			ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
			OperatorService operatorService = (OperatorService) ctx.getBean("operatorService");
			UserService userService = (UserService) ctx.getBean("userService");
			user.setIsActive(false);
			operatorService.updateOperator(user);
			userService.clearOperatorField(user);
			log.info(user.getUsername() + " (id=" + user.getId() + ") logged out because session is destroyed.");
			session.removeAttribute(ApplicationConstants.USER_SESSION);
		}
	}

}
