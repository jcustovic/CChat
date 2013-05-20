package hr.chus.cchat.db.service;

import java.util.List;

import hr.chus.cchat.model.db.jpa.LanguageProvider;
import hr.chus.cchat.model.db.jpa.ServiceProvider;

/**
 * Service provider services that DAO needs to implement.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public interface ServiceProviderService {

    void addServiceProvider(ServiceProvider p_serviceProvider);

    void deleteServiceProvider(ServiceProvider p_serviceProvider);

    ServiceProvider updateServiceProvider(ServiceProvider p_serviceProvider);

    ServiceProvider getById(Integer p_id);

    List<ServiceProvider> findAll();

    ServiceProvider getByProviderNameAndShortCode(String p_providerName, String p_sc);

    ServiceProvider findByProviderNameAndShortCodeAndProviderLanguage(String p_serviceProviderName, String p_sc, LanguageProvider p_bestMatchedLangProvider);

    List<String> findAllSenderBeanNames();

    void delete(Integer p_id);

}
