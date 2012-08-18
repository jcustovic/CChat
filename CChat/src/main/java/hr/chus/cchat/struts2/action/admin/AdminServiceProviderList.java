package hr.chus.cchat.struts2.action.admin;

import hr.chus.cchat.db.service.ServiceProviderService;
import hr.chus.cchat.model.db.jpa.ServiceProvider;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that returns all service providers.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
public class AdminServiceProviderList extends ActionSupport {

    @Autowired
    private ServiceProviderService serviceProviderService;

    private List<ServiceProvider>  serviceProviderList;

    @Override
    public String execute() throws Exception {
        serviceProviderList = serviceProviderService.getAll();
        
        return SUCCESS;
    }

    // Getters & setters

    public List<ServiceProvider> getServiceProviderList() {
        return serviceProviderList;
    }

    public void setServiceProviderList(List<ServiceProvider> serviceProviderList) {
        this.serviceProviderList = serviceProviderList;
    }

}
