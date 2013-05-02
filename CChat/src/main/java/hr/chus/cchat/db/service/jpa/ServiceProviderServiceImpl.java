package hr.chus.cchat.db.service.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hr.chus.cchat.db.repository.ServiceProviderRepository;
import hr.chus.cchat.db.service.ServiceProviderService;
import hr.chus.cchat.model.db.jpa.LanguageProvider;
import hr.chus.cchat.model.db.jpa.ServiceProvider;

/**
 * JPA/Hibernate DAO implementation of service provider services.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@Service
@Transactional
public class ServiceProviderServiceImpl implements ServiceProviderService {

    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    @PersistenceContext
    private EntityManager             entityManager;

    @Override
    public void addServiceProvider(ServiceProvider serviceProvider) {
        entityManager.persist(serviceProvider);
    }

    @Override
    public void deleteServiceProvider(ServiceProvider serviceProvider) {
        serviceProvider = entityManager.getReference(ServiceProvider.class, serviceProvider.getId());
        entityManager.remove(serviceProvider);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ServiceProvider> getAll() {
        return entityManager.createNamedQuery("ServiceProvider.getAll").getResultList();
    }

    @Override
    public ServiceProvider getById(Integer id) {
        return entityManager.find(ServiceProvider.class, id);
    }

    @Override
    public ServiceProvider updateServiceProvider(ServiceProvider serviceProvider) {
        return entityManager.merge(serviceProvider);
    }

    @Override
    public ServiceProvider getByProviderNameAndShortCode(String providerName, String sc) {
        try {
            return (ServiceProvider) entityManager.createNamedQuery("ServiceProvider.getByNameAndSc").setParameter("providerName", providerName)
                    .setParameter("sc", sc).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public ServiceProvider findByProviderNameAndShortCodeAndProviderLanguage(String p_serviceProviderName, String p_sc, LanguageProvider p_langProvider) {
        return serviceProviderRepository.findByProviderNameAndScAndLanguageProvider(p_serviceProviderName, p_sc, p_langProvider);
    }

}
