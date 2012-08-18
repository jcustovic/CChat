package hr.chus.cchat.struts2.converter;

import hr.chus.cchat.db.service.UserService;
import hr.chus.cchat.model.db.jpa.User;

import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Custom Struts2 User type converter (user id gets converted to user object).
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public class UserTypeConverter extends StrutsTypeConverter {

    private static final Logger LOG = LoggerFactory.getLogger(UserTypeConverter.class);

    @Autowired
    private UserService         userService;

    @Override
    public Object convertFromString(Map context, String[] values, Class toClass) {
        LOG.debug("Started converting String to User");
        User user = null;
        try {
            user = userService.getUserById(Integer.valueOf(values[0]), false);
        } catch (Exception e) {
            return null;
        }
        LOG.debug("Finished converting String to User");

        return user;
    }

    @Override
    public String convertToString(Map context, Object o) {
        return ((User) o).getId().toString();
    }

}
