package hr.chus.cchat.model.db.jpa;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

    public ServiceProvider() {}

    public ServiceProvider(String sc, String providerName, String serviceName, String description, boolean disabled) {
        this.sc = sc;
        this.providerName = providerName;
        this.serviceName = serviceName;
        this.description = description;
        this.disabled = disabled;
    }

    @Override
    public String toString() {
        return String.format("ServiceProvider[ID: %s, ProviderName: %s, SC: %s, ServiceName: %s, Disabled: %s]", new Object[] { getId(), providerName, sc,
                serviceName, disabled });
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

    public String getSc() {
        return sc;
    }

    public void setSc(String sc) {
        this.sc = sc;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Float getBillingAmount() {
        return billingAmount;
    }

    public void setBillingAmount(Float billingAmount) {
        this.billingAmount = billingAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public String getSendServiceBeanName() {
        return sendServiceBeanName;
    }

    public void setSendServiceBeanName(String sendServiceBeanName) {
        this.sendServiceBeanName = sendServiceBeanName;
    }

    public Set<ServiceProviderKeyword> getServiceProviderKeywords() {
        return serviceProviderKeywords;
    }

    public void setServiceProviderKeywords(Set<ServiceProviderKeyword> serviceProviderKeywords) {
        this.serviceProviderKeywords = serviceProviderKeywords;
    }

}
