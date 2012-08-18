package hr.chus.cchat.struts2.converter;

import hr.chus.cchat.db.service.RoleService;
import hr.chus.cchat.model.db.jpa.Role;

import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Custom Struts2 Role type converter (role id gets converted to role object).
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public class RoleTypeConverter extends StrutsTypeConverter {

    private static final Logger LOG = LoggerFactory.getLogger(RoleTypeConverter.class);

    @Autowired
    private RoleService         roleService;

    @Override
    public Object convertFromString(Map context, String[] values, Class toClass) {
        LOG.debug("Started converting String to Role");
        Role role = null;
        try {
            role = roleService.getRoleById(Integer.valueOf(values[0]));
        } catch (Exception e) {
            return null;
        }
        LOG.debug("Finished converting String to Role");

        return role;
    }

    @Override
    public String convertToString(Map context, Object o) {
        return ((Role) o).getId().toString();
    }

}
