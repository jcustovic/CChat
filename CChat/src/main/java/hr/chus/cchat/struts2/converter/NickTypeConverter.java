package hr.chus.cchat.struts2.converter;

import hr.chus.cchat.db.service.NickService;
import hr.chus.cchat.model.db.jpa.Nick;

import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Custom Struts2 Nick type converter (nick id gets converted to nick object).
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public class NickTypeConverter extends StrutsTypeConverter {

    private static final Logger LOG = LoggerFactory.getLogger(NickTypeConverter.class);

    @Autowired
    private NickService         nickService;

    @Override
    public Object convertFromString(Map context, String[] values, Class toClass) {
        LOG.debug("Started converting String to Nick");
        Nick nick = null;
        try {
            nick = nickService.getNickById(Integer.valueOf(values[0]));
        } catch (Exception e) {
            return null;
        }
        LOG.debug("Finished converting String to Nick");

        return nick;
    }

    @Override
    public String convertToString(Map context, Object o) {
        return ((Nick) o).getId().toString();
    }

}
