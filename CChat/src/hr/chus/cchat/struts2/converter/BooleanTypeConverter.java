package hr.chus.cchat.struts2.converter;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.util.StrutsTypeConverter;

public class BooleanTypeConverter extends StrutsTypeConverter {
	
	private Log log = LogFactory.getLog(getClass());
	
	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		log.debug("Started converting String to Boolean");
		Boolean bool = false;
		if (values[0] != null && values[0].length() > 0 && !values[0].equalsIgnoreCase("false")) {
			bool = true;
		}
		log.debug("Finished converting String to Boolean");
		return bool;
	}

	@Override
	public String convertToString(Map context, Object o) {
		return ((Boolean) o).toString();
	}

}
