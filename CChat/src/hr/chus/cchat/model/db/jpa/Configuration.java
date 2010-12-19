package hr.chus.cchat.model.db.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.ejb.QueryHints;

/**
 * Class describes Configuration DAO model which will be used for JPA/Hibernate implementation.
 * Class defines queries, table and column names and cache.
 * 
 * Application specific name/value parameters are represented by this class. 
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
@Entity
@Table(name = "configuration")
@NamedQueries({
	@NamedQuery(name = "Configuration.getAll", query = "SELECT c FROM Configuration c ORDER BY c.name", hints = { @QueryHint(name = QueryHints.HINT_CACHEABLE, value = "true") })
})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Configuration implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String value;
	
	
	public Configuration() { }
	
	public Configuration(String name, String value) {
		this.name = name;
		this.value = value;
	}


	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (object == null) return false;
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
