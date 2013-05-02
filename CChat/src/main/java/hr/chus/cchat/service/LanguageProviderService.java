package hr.chus.cchat.service;

import hr.chus.cchat.model.db.jpa.LanguageProvider;

import java.util.List;

/**
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public interface LanguageProviderService {
    
    List<LanguageProvider> findBestMatchByPrefix(String p_p_msisdn);

}
