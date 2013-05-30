package hr.chus.cchat.service.impl;

import hr.chus.cchat.db.repository.LanguageProviderRepository;
import hr.chus.cchat.model.db.jpa.LanguageProvider;
import hr.chus.cchat.service.LanguageProviderService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
    public final List<LanguageProvider> findMatchByPrefix(final String p_msisdn) {
        return languageProviderRepository.findBestMatchByPrefix(normalizeMsisdn(p_msisdn));
    }

    @Override
    public final LanguageProvider findBestMatchByPrefix(final String p_msisdn) {
        final List<LanguageProvider> languageProviders = languageProviderRepository.findBestMatchByPrefix(normalizeMsisdn(p_msisdn), new PageRequest(0, 1));
        if (languageProviders.isEmpty()) {
            return null;
        } else {
            return languageProviders.get(0);
        }
    }

    private String normalizeMsisdn(final String p_msisdn) {
        String msisdn = p_msisdn;
        if (p_msisdn.startsWith("+")) {
            msisdn = p_msisdn.substring(1);
        }

        return msisdn;
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
