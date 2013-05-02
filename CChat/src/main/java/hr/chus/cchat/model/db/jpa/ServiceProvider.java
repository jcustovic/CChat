package hr.chus.cchat.model.db.jpa;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.ejb.QueryHints;

/**
 * Class describes ServiceProvider DAO model which will be used for JPA/Hibernate implementation.
 * Class defines queries, table and column names and cache.
 * Service providers are telcos which we use to send messages. Every user has its service provider.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "service_provider")
@NamedQueries({
        @NamedQuery(name = "ServiceProvider.getAll", query = "SELECT sp FROM ServiceProvider sp", hints = { @QueryHint(name = QueryHints.HINT_CACHEABLE,
                value = "true") }),
        @NamedQuery(name = "ServiceProvider.getByNameAndSc", query = "SELECT sp FROM ServiceProvider sp WHERE sp.sc = :sc AND sp.providerName = :providerName",
                hints = { @QueryHint(name = QueryHints.HINT_CACHEABLE, value = "true") }) })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ServiceProvider extends AbstractBaseEntity {

    @Column(name = "sc", length = 20, nullable = false)
    private String                      sc;

    @Column(name = "provider_name", length = 30, nullable = false)
    private String                      providerName;

    @Column(name = "service_name", length = 30, nullable = false)
    private String                      serviceName;

    @Column(name = "billing_amount")
    private Float                       billingAmount;

    @Column(name = "description", length = 200, nullable = true)
    private String                      description;

    @Column(name = "disabled", nullable = false, columnDefinition = "BIT")
    private Boolean                     disabled;

    @Column(name = "send_service_bean", length = 30)
    private String                      sendServiceBeanName;

    @OneToMany(mappedBy = "serviceProvider", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<ServiceProviderKeyword> serviceProviderKeywords;

    @Column(name = "auto_created", columnDefinition = "BIT")
    private Boolean                     autoCreated = Boolean.FALSE;

    @ManyToOne
    @JoinColumn(name = "language_provider_id")
    private LanguageProvider            languageProvider;

    public ServiceProvider() {
        // Default
    }

    public ServiceProvider(final String p_sc, final String p_providerName, final String p_serviceName, final String p_description, final boolean p_disabled) {
        sc = p_sc;
        providerName = p_providerName;
        serviceName = p_serviceName;
        description = p_description;
        disabled = p_disabled;
    }

    @Override
    public String toString() {
        String lang = "";
        Integer langProvId = null;
        if (languageProvider != null) {
            lang = languageProvider.getLanguage().getShortCode();
            langProvId = languageProvider.getId();
        }
        return String.format("ServiceProvider[ID: %s, ProviderName: %s, SC: %s, ServiceName: %s, Disabled: %s, LanguageProviderId: %s, Lang: %s]",
                new Object[] { getId(), providerName, sc, serviceName, disabled, langProvId, lang });
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (this == object) return true;
        if (!(object instanceof ServiceProvider)) return false;
        final ServiceProvider serviceProvider = (ServiceProvider) object;

        return (serviceProvider.getId().equals(getId()));
    }

    // Getters & setters

    public final String getSc() {
        return sc;
    }

    public final void setSc(String p_sc) {
        sc = p_sc;
    }

    public final String getProviderName() {
        return providerName;
    }

    public final void setProviderName(final String p_providerName) {
        providerName = p_providerName;
    }

    public final String getServiceName() {
        return serviceName;
    }

    public final void setServiceName(final String p_serviceName) {
        serviceName = p_serviceName;
    }

    public final Float getBillingAmount() {
        return billingAmount;
    }

    public final void setBillingAmount(final Float p_billingAmount) {
        billingAmount = p_billingAmount;
    }

    public final String getDescription() {
        return description;
    }

    public final void setDescription(final String p_description) {
        description = p_description;
    }

    public final Boolean getDisabled() {
        return disabled;
    }

    public final void setDisabled(final Boolean p_disabled) {
        disabled = p_disabled;
    }

    public final String getSendServiceBeanName() {
        return sendServiceBeanName;
    }

    public final void setSendServiceBeanName(final String p_sendServiceBeanName) {
        sendServiceBeanName = p_sendServiceBeanName;
    }

    public final Set<ServiceProviderKeyword> getServiceProviderKeywords() {
        return serviceProviderKeywords;
    }

    public final void setServiceProviderKeywords(Set<ServiceProviderKeyword> p_serviceProviderKeywords) {
        serviceProviderKeywords = p_serviceProviderKeywords;
    }

    public final Boolean getAutoCreated() {
        return autoCreated;
    }

    public final void setAutoCreated(final Boolean p_autoCreated) {
        autoCreated = p_autoCreated;
    }

    public final LanguageProvider getLanguageProvider() {
        return languageProvider;
    }

    public final void setLanguageProvider(final LanguageProvider p_languageProvder) {
        languageProvider = p_languageProvder;
    }

}
