package hr.chus.cchat.db.repository;

import hr.chus.cchat.model.db.jpa.LanguageProvider;
import hr.chus.cchat.model.db.jpa.ServiceProvider;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@Transactional(readOnly = true)
public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Integer> {

    ServiceProvider findByProviderNameAndScAndLanguageProvider(String p_serviceProviderName, String p_sc, LanguageProvider p_langProvider);

}
