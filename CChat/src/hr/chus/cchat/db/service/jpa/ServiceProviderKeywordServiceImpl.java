package hr.chus.cchat.db.service.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import hr.chus.cchat.db.service.ServiceProviderKeywordService;
import hr.chus.cchat.model.db.jpa.ServiceProviderKeyword;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
@Transactional
public class ServiceProviderKeywordServiceImpl implements ServiceProviderKeywordService {
	
	private EntityManager entityManager;

	
	@Override
	public ServiceProviderKeyword addOrEditServiceProviderKeyword(ServiceProviderKeyword serviceProviderKeyword) {
		return entityManager.merge(serviceProviderKeyword);
	}

	@Override
	public void addServiceProviderKeyword(ServiceProviderKeyword serviceProviderKeyword) {
		entityManager.persist(serviceProviderKeyword);
	}

	@Override
	public void removeServiceProviderKeyword(ServiceProviderKeyword serviceProviderKeyword) {
		entityManager.remove(serviceProviderKeyword);
	}
	
	@Override
	public ServiceProviderKeyword getById(Integer id) {
		return entityManager.find(ServiceProviderKeyword.class, id);
	}
	
	
	// Getters & setters
	
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) { this.entityManager = entityManager; }

}
