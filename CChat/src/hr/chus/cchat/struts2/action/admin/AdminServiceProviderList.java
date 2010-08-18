package hr.chus.cchat.struts2.action.admin;

import hr.chus.cchat.db.service.ServiceProviderService;
import hr.chus.cchat.model.db.jpa.ServiceProvider;

import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class AdminServiceProviderList  extends ActionSupport {

	private static final long serialVersionUID = 1L;
	
	private List<ServiceProvider> serviceProviderList;
	private ServiceProviderService serviceProviderService;
	
	@Override
	public String execute() throws Exception {
		serviceProviderList = serviceProviderService.getAll();
		return SUCCESS;
	}

	
	// Getters & setters
	
	public List<ServiceProvider> getServiceProviderList() { return serviceProviderList; }
	public void setServiceProviderList(List<ServiceProvider> serviceProviderList) { this.serviceProviderList = serviceProviderList; }

	public void setServiceProviderService(ServiceProviderService serviceProviderService) { this.serviceProviderService = serviceProviderService; }

}
