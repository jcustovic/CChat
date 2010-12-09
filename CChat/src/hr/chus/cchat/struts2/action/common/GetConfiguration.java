package hr.chus.cchat.struts2.action.common;

import hr.chus.cchat.db.service.ConfigurationService;
import hr.chus.cchat.model.db.jpa.Configuration;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that gets configuration parameter by name.
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class GetConfiguration extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	
	private ConfigurationService configurationService;
	
	private String name;
	private String value;
	
	
	@Override
	public String execute() throws Exception {
		Configuration configuration = configurationService.getByName(name);
		if (configuration != null) {
			value = configuration.getValue();
		}
		return SUCCESS;
	}

	
	// Getters & setters
	
	public void setConfigurationService(ConfigurationService configurationService) { this.configurationService = configurationService; }

	public String getValue() { return value; }

	public void setName(String name) { this.name = name; }

}
