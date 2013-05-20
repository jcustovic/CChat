package hr.chus.cchat.struts2.action.admin;

import hr.chus.cchat.db.service.ServiceProviderService;
import hr.chus.cchat.model.db.jpa.ServiceProvider;
import hr.chus.cchat.model.helper.BeanNameWrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that returns all service providers.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public class AdminServiceProviderAction extends ActionSupport {

    private static final long      serialVersionUID = 1L;

    @Autowired
    private ServiceProviderService serviceProviderService;

    private List<ServiceProvider>  serviceProviders;
    private List<BeanNameWrapper>  sendBeans;
    private ServiceProvider        serviceProvider;

    public final String findAll() {
        serviceProviders = serviceProviderService.findAll();

        return SUCCESS;
    }

    public final String findAllSendBeans() {
        final List<String> sendBeanNames = serviceProviderService.findAllSenderBeanNames();
        if (!sendBeanNames.isEmpty()) {
            sendBeans = new ArrayList<BeanNameWrapper>(sendBeanNames.size());
            for (final String beanName : sendBeanNames) {
                sendBeans.add(new BeanNameWrapper(beanName));
            }
        }

        return SUCCESS;
    }

    public final String save() {
        serviceProvider = serviceProviderService.updateServiceProvider(serviceProvider);

        return SUCCESS;
    }

    public final String delete() {
        try {
            serviceProviderService.delete(serviceProvider.getId());
        } catch (DataIntegrityViolationException e) {
            addActionError(e.getMessage());
        }

        return SUCCESS;
    }

    // Getters & setters

    public final List<ServiceProvider> getServiceProviders() {
        return serviceProviders;
    }

    public final List<BeanNameWrapper> getSendBeans() {
        return sendBeans;
    }

    public final ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public final void setServiceProvider(final ServiceProvider p_serviceProvider) {
        serviceProvider = p_serviceProvider;
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
