package hr.chus.cchat.struts2.action.admin;

import hr.chus.cchat.db.service.ServiceProviderService;
import hr.chus.cchat.model.db.jpa.ServiceProvider;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionSupport;

public class AdminServiceProviderList  extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private Log log = LogFactory.getLog(getClass());
	
	private List<ServiceProvider> serviceProviderList;
	private ServiceProviderService servicProviderService;
	
	@Override
	public String execute() throws Exception {
		serviceProviderList = servicProviderService.getAll();
		return SUCCESS;
	}

	
	// Getters & setters
	
	public List<ServiceProvider> getServiceProviderList() { return serviceProviderList; }
	public void setServiceProviderList(List<ServiceProvider> serviceProviderList) { this.serviceProviderList = serviceProviderList; }

	public void setServicProviderService(ServiceProviderService servicProviderService) { this.servicProviderService = servicProviderService; }

}
