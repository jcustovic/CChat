package hr.chus.cchat.struts2.action.common;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class Test extends ActionSupport implements ServletRequestAware {
	
	private static final long serialVersionUID = 1L;
	
	private Log log = LogFactory.getLog(getClass());
	
	private HttpServletRequest request;
	
	private String test;
	
	@Override
	public String execute() throws Exception {
		log.info(request.getCharacterEncoding());
		log.info(test);
		log.info(new String(test.getBytes(), "UTF-8"));
		log.info(new String(test.getBytes("ISO-8859-1"), "UTF-8"));
		return SUCCESS;
	}

	
	// Getters & setters
	
	public String getTest() { return test; }
	public void setTest(String test) { this.test = test; }

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	
}
