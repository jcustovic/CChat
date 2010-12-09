package hr.chus.cchat.struts2.converter;

import hr.chus.cchat.db.service.OperatorService;
import hr.chus.cchat.model.db.jpa.Operator;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.util.StrutsTypeConverter;

/**
 * Custom Struts2 Operator type converter (operator id gets converted to operator object).
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class OperatorTypeConverter extends StrutsTypeConverter {
	
	private Log log = LogFactory.getLog(getClass());

	private OperatorService operatorService;

	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		log.debug("Started converting String to Operator");
		Operator operator = null;
		try {
			operator = operatorService.getOperatorById(Integer.valueOf(values[0]));
		} catch (Exception e) {
			return null;
		}
		log.debug("Finished converting String to Operator");
		return operator;
	}

	@Override
	public String convertToString(Map context, Object o) {
		return ((Operator) o).getId().toString();
	}

	
	// Getters & setters
	
	public void setOperatorService(OperatorService operatorService) { this.operatorService = operatorService; }		

}
