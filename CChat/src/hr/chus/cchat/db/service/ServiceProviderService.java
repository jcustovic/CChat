package hr.chus.cchat.db.service;

import java.util.List;

import hr.chus.cchat.model.db.jpa.ServiceProvider;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public interface ServiceProviderService {
	
	public void addServiceProvider(ServiceProvider serviceProvider);
	public void deleteServiceProvider(ServiceProvider serviceProvider);
	public ServiceProvider updateServiceProvider(ServiceProvider serviceProvider);
	public ServiceProvider getById(Integer id);
	public List<ServiceProvider> getAll();

}
