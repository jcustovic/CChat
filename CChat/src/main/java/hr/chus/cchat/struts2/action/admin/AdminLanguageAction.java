package hr.chus.cchat.struts2.action.admin;

import hr.chus.cchat.model.db.jpa.Language;
import hr.chus.cchat.service.LanguageService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 *
 */
public class AdminLanguageAction extends ActionSupport {

    private static final long         serialVersionUID = 1L;

    @Autowired
    private transient LanguageService languageService;

    private Language                  language;
    private List<Language>            languages;

    public final String findAll() {
        languages = languageService.findAll();

        return SUCCESS;
    }

    public final String save() {
        language = languageService.save(language);

        return SUCCESS;
    }

    public final String delete() {
        try {
            languageService.delete(language.getId());
        } catch (DataIntegrityViolationException e) {
            addActionError(e.getMessage());
        }

        return SUCCESS;
    }

    // Getters & setters

    public final Language getLanguage() {
        return language;
    }

    public final void setLanguage(final Language p_language) {
        language = p_language;
    }

    public final List<Language> getLanguages() {
        return languages;
    }

    // For JSON response

    public final boolean isSuccess() {
        return !hasErrors();
    }

    public final Collection<String> getError() {
        return getActionErrors();
    }

    @Override
    public final Map<String, List<String>> getFieldErrors() {
        return super.getFieldErrors();
    }

}
