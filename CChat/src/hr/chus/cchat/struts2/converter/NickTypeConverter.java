package hr.chus.cchat.struts2.converter;

import java.util.Map;

import hr.chus.cchat.db.service.NickService;
import hr.chus.cchat.model.db.jpa.Nick;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.util.StrutsTypeConverter;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class NickTypeConverter extends StrutsTypeConverter {
	
	private Log log = LogFactory.getLog(getClass());

	private NickService nickService;

	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		log.debug("Started converting String to Nick");
		Nick nick = null;
		try {
			nick = nickService.getNickById(Integer.valueOf(values[0]));
		} catch (Exception e) {
			return null;
		}
		log.debug("Finished converting String to Nick");
		return nick;
	}

	@Override
	public String convertToString(Map context, Object o) {
		return ((Nick) o).getId().toString();
	}
	
	
	// Getters & setters
	
	public void setNickService(NickService nickService) { this.nickService = nickService; }

}
