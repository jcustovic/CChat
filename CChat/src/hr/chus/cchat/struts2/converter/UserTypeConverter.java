package hr.chus.cchat.struts2.converter;

import hr.chus.cchat.db.service.UserService;
import hr.chus.cchat.model.db.jpa.User;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.util.StrutsTypeConverter;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class UserTypeConverter extends StrutsTypeConverter {
	
	private Log log = LogFactory.getLog(getClass());

	private UserService userService;

	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		log.debug("Started converting String to User");
		User user = null;
		try {
			user = userService.getUserById(Integer.valueOf(values[0]));
		} catch (Exception e) {
			return null;
		}
		log.debug("Finished converting String to User");
		return user;
	}

	@Override
	public String convertToString(Map context, Object o) {
		return ((User) o).getId().toString();
	}

	
	// Getters & setters

	public void setUserService(UserService userService) { this.userService = userService; }

}
