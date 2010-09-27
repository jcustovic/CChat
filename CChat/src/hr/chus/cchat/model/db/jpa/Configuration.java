package hr.chus.cchat.model.db.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
@Entity
@Table(name = "configuration")
@NamedQueries({
	@NamedQuery(name = "Configuration.getAll", query = "SELECT c FROM Configuration c ORDER BY c.name")
})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Configuration implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String value;
	
	
	public Configuration() { }
	
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (!(object instanceof Configuration)) return false;
		Configuration configuration = (Configuration) object;
		return configuration.getName().equals(name);
	}
	
	
	// Getters & setters
	
	@Id
	@Column(name = "name", length = 50, nullable = false)
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	@Column(name = "value", length = 100)
	public String getValue() { return value; }
	public void setValue(String value) { this.value = value; }
	
}
