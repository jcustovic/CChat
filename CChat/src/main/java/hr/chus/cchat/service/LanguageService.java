package hr.chus.cchat.service;

import hr.chus.cchat.model.db.jpa.Language;

import java.util.List;

public interface LanguageService {

    List<Language> findAll();

    Language save(Language p_language);

    void delete(Language p_language);

    void delete(Integer p_languageId);

}
