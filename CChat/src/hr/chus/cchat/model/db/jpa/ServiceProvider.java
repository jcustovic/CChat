package hr.chus.cchat.model.db.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "Service_Provider")
@NamedQueries({
	@NamedQuery(name = "ServiceProvider.getAll", query = "SELECT sp FROM ServiceProvider sp")
})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ServiceProvider implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String sc;
	private String providerName;
	private String description;
	private Boolean disabled;
	
	public ServiceProvider() { }

	
	@Id
	@GeneratedValue
	@Column(name = "id")
	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }

	@Column(name = "sc", length = 20, nullable = false)
	public String getSc() { return sc; }
	public void setSc(String sc) { this.sc = sc; }

	@Column(name = "provider_name", length = 30, nullable = false)
	public String getProviderName() { return providerName; }
	public void setProviderName(String providerName) { this.providerName = providerName; }

	@Column(name = "description", length = 200, nullable = true)
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }

	@Column(name = "disabled")
	public Boolean getDisabled() { return disabled; }
	public void setDisabled(Boolean disabled) { this.disabled = disabled; }
		
}
