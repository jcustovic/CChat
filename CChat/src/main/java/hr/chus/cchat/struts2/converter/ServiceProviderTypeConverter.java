package hr.chus.cchat.struts2.converter;

import hr.chus.cchat.db.service.ServiceProviderService;
import hr.chus.cchat.model.db.jpa.ServiceProvider;

import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Custom Struts2 Service provider type converter (serviceProvider id gets converted to serviceProvider object).
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public class ServiceProviderTypeConverter extends StrutsTypeConverter {

    private static final Logger    LOG = LoggerFactory.getLogger(ServiceProviderTypeConverter.class);

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Override
    public Object convertFromString(Map context, String[] values, Class toClass) {
        LOG.debug("Started converting String to ServiceProvider");
        ServiceProvider serviceProvider = null;
        try {
            serviceProvider = serviceProviderService.getById(Integer.valueOf(values[0]));
        } catch (Exception e) {
            return null;
        }
        LOG.debug("Finished converting String to ServiceProvider");

        return serviceProvider;
    }

    @Override
    public String convertToString(Map context, Object o) {
        return ((ServiceProvider) o).getId().toString();
    }

}
