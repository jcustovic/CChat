package hr.chus.cchat.struts2.action.common;

import hr.chus.cchat.db.service.ConfigurationService;
import hr.chus.cchat.model.db.jpa.Configuration;

import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that gets configuration parameter by name.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
public class GetConfiguration extends ActionSupport {

    @Autowired
    private ConfigurationService configurationService;

    private String               name;
    private String               value;

    @Override
    public String execute() throws Exception {
        final Configuration configuration = configurationService.getByName(name);
        if (configuration != null) {
            value = configuration.getValue();
        }

        return SUCCESS;
    }

    // Getters & setters

    public String getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

}
