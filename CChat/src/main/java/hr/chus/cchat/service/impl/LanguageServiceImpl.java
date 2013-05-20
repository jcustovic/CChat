package hr.chus.cchat.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.chus.cchat.db.repository.LanguageRepository;
import hr.chus.cchat.model.db.jpa.Language;
import hr.chus.cchat.service.LanguageService;

@Service
public class LanguageServiceImpl implements LanguageService {

    @Autowired
    private transient LanguageRepository languageRepository;

    @Override
    public final List<Language> findAll() {
        return languageRepository.findAll();
    }

    @Override
    public final Language save(final Language p_language) {
        return languageRepository.save(p_language);
    }

    @Override
    public final void delete(final Language p_language) {
        languageRepository.delete(p_language);
    }

    @Override
    public void delete(final Integer p_languageId) {
        languageRepository.delete(p_languageId);
    }

}
