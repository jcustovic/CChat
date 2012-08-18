package hr.chus.cchat.struts2.converter;

import hr.chus.cchat.db.service.OperatorService;
import hr.chus.cchat.model.db.jpa.Operator;

import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Custom Struts2 Operator type converter (operator id gets converted to operator object).
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public class OperatorTypeConverter extends StrutsTypeConverter {

    private static final Logger LOG = LoggerFactory.getLogger(OperatorTypeConverter.class);

    @Autowired
    private OperatorService     operatorService;

    @Override
    public Object convertFromString(Map context, String[] values, Class toClass) {
        LOG.debug("Started converting String to Operator");
        Operator operator = null;
        try {
            operator = operatorService.getOperatorById(Integer.valueOf(values[0]));
        } catch (Exception e) {
            return null;
        }
        LOG.debug("Finished converting String to Operator");

        return operator;
    }

    @Override
    public String convertToString(Map context, Object o) {
        return ((Operator) o).getId().toString();
    }

}
