package hr.chus.cchat.db.service.jpa;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hr.chus.cchat.db.repository.ServiceProviderRepository;
import hr.chus.cchat.db.service.ServiceProviderService;
import hr.chus.cchat.gateway.SendMessageService;
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
    private ServiceProviderRepository    serviceProviderRepository;

    @PersistenceContext
    private EntityManager                entityManager;

    @Autowired
    private transient ApplicationContext applicationContext;

    @Override
    public final void addServiceProvider(ServiceProvider serviceProvider) {
        entityManager.persist(serviceProvider);
    }

    @Override
    public final void deleteServiceProvider(ServiceProvider serviceProvider) {
        serviceProvider = entityManager.getReference(ServiceProvider.class, serviceProvider.getId());
        entityManager.remove(serviceProvider);
    }

    @Override
    public final List<ServiceProvider> findAll() {
        return entityManager.createNamedQuery("ServiceProvider.getAll", ServiceProvider.class).getResultList();
    }

    @Override
    public final ServiceProvider getById(Integer id) {
        return entityManager.find(ServiceProvider.class, id);
    }

    @Override
    public final ServiceProvider updateServiceProvider(ServiceProvider serviceProvider) {
        return entityManager.merge(serviceProvider);
    }

    @Override
    public final ServiceProvider getByProviderNameAndShortCode(String providerName, String sc) {
        try {
            return (ServiceProvider) entityManager.createNamedQuery("ServiceProvider.getByNameAndSc").setParameter("providerName", providerName)
                    .setParameter("sc", sc).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public final ServiceProvider findByProviderNameAndShortCodeAndProviderLanguage(String p_serviceProviderName, String p_sc, LanguageProvider p_langProvider) {
        return serviceProviderRepository.findByProviderNameAndScAndLanguageProvider(p_serviceProviderName, p_sc, p_langProvider);
    }

    @Cacheable(value = "hr.chus.cchat.db.service.jpa.ServiceProviderServiceImpl.findAllSenderBeanNames")
    @Override
    public final List<String> findAllSenderBeanNames() {
        final String[] sendMessageBeans = applicationContext.getBeanNamesForType(SendMessageService.class);
        
        return Arrays.asList(sendMessageBeans);
    }

    @Override
    public final void delete(final Integer p_id) {
        serviceProviderRepository.delete(p_id);
    }

}
