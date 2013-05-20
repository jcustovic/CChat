package hr.chus.cchat.service.impl;

import hr.chus.cchat.db.repository.LanguageProviderRepository;
import hr.chus.cchat.model.db.jpa.LanguageProvider;
import hr.chus.cchat.service.LanguageProviderService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Default implementation of {@link LanguageProviderService}
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@Service
public class LanguageProviderServiceImpl implements LanguageProviderService {

    // private static final Logger        LOG = LoggerFactory.getLogger(LanguageProviderServiceImpl.class);

    @Autowired
    private LanguageProviderRepository languageProviderRepository;

    @Override
    public final List<LanguageProvider> findBestMatchByPrefix(final String p_msisdn) {
        String msisdn = p_msisdn;
        if (p_msisdn.startsWith("+")) {
            msisdn = p_msisdn.substring(1);
        }

        return languageProviderRepository.findBestMatchByPrefix(msisdn);
    }

    @Override
    public final List<LanguageProvider> findAll() {
        return languageProviderRepository.findAll();
    }

    @Override
    public final LanguageProvider save(final LanguageProvider p_languageProvider) {
        return languageProviderRepository.save(p_languageProvider);
    }

    @Override
    public final void delete(final Integer p_id) {
        languageProviderRepository.delete(p_id);
    }

}
