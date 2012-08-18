package hr.chus.cchat.struts2.converter;

import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom Struts2 boolean type converter.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public class BooleanTypeConverter extends StrutsTypeConverter {

    private static final Logger LOG = LoggerFactory.getLogger(BooleanTypeConverter.class);

    @Override
    public Object convertFromString(Map context, String[] values, Class toClass) {
        LOG.debug("Started converting String to Boolean");
        Boolean bool = false;
        if (values[0] != null && values[0].length() > 0 && !values[0].equalsIgnoreCase("false")) {
            bool = true;
        }
        LOG.debug("Finished converting String to Boolean");

        return bool;
    }

    @Override
    public String convertToString(Map context, Object o) {
        return ((Boolean) o).toString();
    }

}
