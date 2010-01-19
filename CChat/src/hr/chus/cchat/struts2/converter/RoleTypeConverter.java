package hr.chus.cchat.struts2.converter;

import hr.chus.cchat.db.service.RoleService;
import hr.chus.cchat.model.db.jpa.Role;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.util.StrutsTypeConverter;

/**
 * 
 * @author chus
 *
 */
public class RoleTypeConverter extends StrutsTypeConverter {
	
	private Log log = LogFactory.getLog(getClass());

	private RoleService roleService;

	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		log.debug("Started converting String to Role");
		Role role = null;
		try {
			role = roleService.getRoleById(Integer.valueOf(values[0]));
		} catch (Exception e) {
			return null;
		}
		log.debug("Finished converting String to Role");
		return role;
	}

	@Override
	public String convertToString(Map context, Object o) {
		return ((Role) o).getId().toString();
	}

	
	// Getters & setters
	
	public void setRoleService(RoleService roleService) { this.roleService = roleService; }	

}
