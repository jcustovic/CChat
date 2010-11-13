package hr.chus.cchat.db.service.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import hr.chus.cchat.db.service.ConfigurationService;
import hr.chus.cchat.model.db.jpa.Configuration;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
@Transactional
public class ConfigurationServiceImpl implements ConfigurationService {
	
	private EntityManager entityManager;
	

	@Override
	public void addConfiguration(Configuration configuration) {
		entityManager.persist(configuration);
	}

	@Override
	public void removeConfiguration(Configuration configuration) {
		entityManager.remove(configuration);
	}

	@Override
	public Configuration editConfiguration(Configuration configuration) {
		return entityManager.merge(configuration);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Configuration> getAll() {
		return entityManager.createNamedQuery("Configuration.getAll").getResultList();
	}

	@Override
	public Configuration getByName(String name) {
		return entityManager.find(Configuration.class, name);
	}
	
	
	// Getters & setters
	
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) { this.entityManager = entityManager; }

}
