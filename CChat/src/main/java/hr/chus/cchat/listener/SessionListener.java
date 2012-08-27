package hr.chus.cchat.listener;

import hr.chus.cchat.ApplicationConstants;
import hr.chus.cchat.db.service.OperatorService;
import hr.chus.cchat.db.service.UserService;
import hr.chus.cchat.model.db.jpa.Operator;

import java.util.HashMap;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * This class keeps track of server sessions (created or destroyed). We are interested in the part when
 * session gets destroyed so we can logout user and deactivate him if he forgets to logout or is
 * inactive for session timeout period.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public class SessionListener implements HttpSessionListener {

    private static final Logger                       LOG        = LoggerFactory.getLogger(SessionListener.class);

    private static final HashMap<String, HttpSession> sessionMap = new HashMap<String, HttpSession>();

    /**
     * @param p_userToRemove
     */
    public static void removeSessionWithUser(final Operator p_userToRemove) {
        for (final String sessionId : sessionMap.keySet()) {
            final HttpSession session = sessionMap.get(sessionId);
            final Operator user = (Operator) session.getAttribute(ApplicationConstants.SESSION_USER_KEY);
            if (user != null && user.getId().equals(p_userToRemove.getId())) {
                LOG.info("{} (id={}, sessionId={}) logged out", new Object[] { user.getUsername(), user.getId(), session.getId() });
                session.removeAttribute(ApplicationConstants.SESSION_USER_KEY);
            }
        }
    }

    /**
     * @param p_userToUpdate
     */
    public static void updateSessionWithUser(final Operator p_userToUpdate) {
        for (final String sessionId : sessionMap.keySet()) {
            final HttpSession session = sessionMap.get(sessionId);
            final Operator user = (Operator) session.getAttribute(ApplicationConstants.SESSION_USER_KEY);
            if (user != null && user.getId().equals(p_userToUpdate.getId())) {
                session.setAttribute(ApplicationConstants.SESSION_USER_KEY, p_userToUpdate);
            }
        }
    }

    @Override
    public final void sessionCreated(final HttpSessionEvent p_sessionEvent) {
        final HttpSession session = p_sessionEvent.getSession();
        LOG.debug("Creating session with id " + session.getId());
        sessionMap.put(session.getId(), session);
    }

    @Override
    public final void sessionDestroyed(final HttpSessionEvent p_sessionEvent) {
        final HttpSession session = p_sessionEvent.getSession();
        LOG.debug("Destroying session with id " + session.getId());
        sessionMap.remove(session.getId());

        final Operator loggedOp = (Operator) session.getAttribute(ApplicationConstants.SESSION_USER_KEY);
        if (loggedOp != null) {
            final ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
            final OperatorService operatorService = (OperatorService) ctx.getBean("operatorService");

            // Deactivate operator
            final Operator operator = operatorService.getOperatorById(loggedOp.getId());
            operator.setIsActive(false);
            operatorService.updateOperator(operator);

            // Clear operator field from users that are assigned to operator
            final UserService userService = (UserService) ctx.getBean("userService");
            userService.clearOperatorField(operator);

            LOG.info("{} (id={}, sessionId={}) logged out because session is destroyed",
                    new Object[] { operator.getUsername(), operator.getId(), session.getId() });

            session.removeAttribute(ApplicationConstants.SESSION_USER_KEY);
        }
    }

}
