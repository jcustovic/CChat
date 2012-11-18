package hr.chus.cchat.struts2.converter;

import hr.chus.cchat.model.db.jpa.Robot;
import hr.chus.cchat.service.RobotService;

import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Custom Struts2 Robot type converter (bot id gets converted to robot object).
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public class RobotTypeConverter extends StrutsTypeConverter {

    private static final Logger    LOG = LoggerFactory.getLogger(RobotTypeConverter.class);

    @Autowired
    private transient RobotService robotService;

    @Override
    public final Object convertFromString(final Map p_context, final String[] p_values, final Class p_toClass) {
        LOG.debug("Started converting String {} to Robot", p_values[0]);
        Robot bot = null;
        try {
            bot = robotService.findOne(Integer.valueOf(p_values[0]));
        } catch (Exception e) {
            return null;
        }
        LOG.debug("Finished converting String to Robot");

        return bot;
    }

    @Override
    public final String convertToString(final Map p_context, final Object p_object) {
        return ((Robot) p_object).getId().toString();
    }

}
