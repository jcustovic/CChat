package hr.chus.cchat.struts2.action.admin;

import hr.chus.cchat.model.db.jpa.LanguageProvider;
import hr.chus.cchat.service.LanguageProviderService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public class AdminLanguageProviderAction extends ActionSupport {

    private static final long                 serialVersionUID = 1L;

    @Autowired
    private transient LanguageProviderService languageProviderService;

    private LanguageProvider                  languageProvider;
    private List<LanguageProvider>            languageProviders;

    public final String findAll() {
        languageProviders = languageProviderService.findAll();

        return SUCCESS;
    }

    public final String save() {
        languageProvider = languageProviderService.save(languageProvider);

        return SUCCESS;
    }

    public final String delete() {
        try {
            languageProviderService.delete(languageProvider.getId());
        } catch (DataIntegrityViolationException e) {
            addActionError(e.getMessage());
        }

        return SUCCESS;
    }

    // Getters & setters

    public final LanguageProvider getLanguage() {
        return languageProvider;
    }
    
    
    public final LanguageProvider getLanguageProvider() {
        return languageProvider;
    }

    public final void setLanguageProvider(final LanguageProvider p_languageProvider) {
        languageProvider = p_languageProvider;
    }

    public final List<LanguageProvider> getLanguageProviders() {
        return languageProviders;
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
