package hr.chus.cchat.filter;

import hr.chus.cchat.ApplicationConstants;
import hr.chus.cchat.model.db.jpa.Operator;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.MDC;

/**
 * A servlet filter that inserts username from http request (if it is present) into the MDC.
 * <p/>
 * <p/>
 * The values are removed after the request is processed.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public class MDCUsernameServletFilter implements Filter {

    private static final String USER_KEY = "username";

    public final void destroy() {
        // do nothing
    }

    public final void doFilter(ServletRequest p_request, ServletResponse p_response, FilterChain p_chain) throws IOException, ServletException {
        insertIntoMDC(p_request);
        try {
            p_chain.doFilter(p_request, p_response);
        } finally {
            clearMDC();
        }
    }

    private void insertIntoMDC(ServletRequest p_request) {
        if (p_request instanceof HttpServletRequest) {
            final HttpServletRequest httpServletRequest = (HttpServletRequest) p_request;
            final Operator user = (Operator) httpServletRequest.getSession().getAttribute(ApplicationConstants.SESSION_USER_KEY);

            if (user == null) {
                MDC.put(USER_KEY, "anonymouse");
            } else {
                MDC.put(USER_KEY, user.getUsername());
            }
        }
    }

    private void clearMDC() {
        MDC.remove(USER_KEY);
    }

    public final void init(FilterConfig p_filterConfig) throws ServletException {
        // do nothing
    }
}
