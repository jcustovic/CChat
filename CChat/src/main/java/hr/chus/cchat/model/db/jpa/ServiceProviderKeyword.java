package hr.chus.cchat.model.db.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "service_provider_keyword")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ServiceProviderKeyword extends AbstractBaseEntity {

    @Column(name = "keyword", length = 20, nullable = false)
    private String          keyword;

    @Column(name = "billing_amount")
    private Float           billingAmount;

    @Column(name = "disabled", nullable = false, columnDefinition = "BIT")
    private Boolean         disabled;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_provider_id", nullable = false)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private ServiceProvider serviceProvider;

    public ServiceProviderKeyword() {}

    public ServiceProviderKeyword(ServiceProvider serviceProvider, String keyword, Float billingAmount) {
        this.serviceProvider = serviceProvider;
        this.keyword = keyword;
        this.billingAmount = billingAmount;
        this.disabled = false;
    }

    // Getters & setters

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Float getBillingAmount() {
        return billingAmount;
    }

    public void setBillingAmount(Float billingAmount) {
        this.billingAmount = billingAmount;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

}
