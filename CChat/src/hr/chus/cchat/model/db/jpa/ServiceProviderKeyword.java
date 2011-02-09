package hr.chus.cchat.model.db.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 
 * @author Jan Čustović
 *
 */
@Entity
@Table(name = "service_provider_keyword")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ServiceProviderKeyword implements Serializable {
		
	private static final long serialVersionUID = 1L;
		
	private Integer id;
	private String keyword;
	private Float billingAmount;
	private Boolean disabled;
	private ServiceProvider serviceProvider;
	
		
	public ServiceProviderKeyword(ServiceProvider serviceProvider, String keyword, Float billingAmount) {
		this.serviceProvider = serviceProvider;
		this.keyword = keyword;
		this.billingAmount = billingAmount;
	}
	
	
	// Getters & setters
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	
	@Column(name = "keyword", length = 20, nullable = false)
	public String getKeyword() { return keyword; }
	public void setKeyword(String keyword) { this.keyword = keyword; }
	
	@Column(name = "billing_amount")
	public Float getBillingAmount() { return billingAmount; }
	public void setBillingAmount(Float billingAmount) { this.billingAmount = billingAmount; }
	
	@Column(name = "disabled", nullable = false)
	public Boolean getDisabled() { return disabled; }
	public void setDisabled(Boolean disabled) { this.disabled = disabled; }
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "service_provider_id", nullable = false)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public ServiceProvider getServiceProvider() { return serviceProvider; }
	public void setServiceProvider(ServiceProvider serviceProvider) { this.serviceProvider = serviceProvider; }

}
