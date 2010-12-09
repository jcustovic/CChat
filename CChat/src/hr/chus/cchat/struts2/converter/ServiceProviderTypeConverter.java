package hr.chus.cchat.struts2.converter;

import hr.chus.cchat.db.service.ServiceProviderService;
import hr.chus.cchat.model.db.jpa.ServiceProvider;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.util.StrutsTypeConverter;

/**
 * Custom Struts2 Service provider type converter (serviceProvider id gets converted to serviceProvider object).
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class ServiceProviderTypeConverter extends StrutsTypeConverter {
	
	private Log log = LogFactory.getLog(getClass());

	private ServiceProviderService serviceProviderService;

	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		log.debug("Started converting String to ServiceProvider");
		ServiceProvider serviceProvider = null;
		try {
			serviceProvider = serviceProviderService.getById(Integer.valueOf(values[0]));
		} catch (Exception e) {
			return null;
		}
		log.debug("Finished converting String to ServiceProvider");
		return serviceProvider;
	}

	@Override
	public String convertToString(Map context, Object o) {
		return ((ServiceProvider) o).getId().toString();
	}

	
	// Getters & setters
	
	public void setServiceProviderService(ServiceProviderService serviceProviderService) { this.serviceProviderService = serviceProviderService; }	

}
