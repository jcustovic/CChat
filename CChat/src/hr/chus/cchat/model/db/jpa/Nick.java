package hr.chus.cchat.model.db.jpa;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.ejb.QueryHints;

/**
 * Class describes Nikc DAO model which will be used for JPA/Hibernate implementation.
 * Class defines queries, table and column names and cache.
 * 
 * Operators can represent them selves to users using different Nicks (just like a different "personas")
 * Nick have their description and picture set.
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
@Entity
@Table(name = "nick")
@NamedQueries({
	@NamedQuery(name = "Nick.getAll", query = "SELECT n FROM Nick n ORDER BY n.name", hints = { @QueryHint(name = QueryHints.HINT_CACHEABLE, value = "true") })
	, @NamedQuery(name = "Nick.getByName", query = "SELECT n FROM Nick n WHERE n.name = :name", hints = { @QueryHint(name = QueryHints.HINT_CACHEABLE, value = "true") })
})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Nick implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;
	private String description;
	private Boolean isKeyword;
	private Set<Operator> operators;
	
	
	public Nick() { }
	
	public Nick(String name, String description, boolean isKeyword) {
		this.name = name;
		this.description = description;
		this.isKeyword = isKeyword;
	}
	
	
	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object object) {
		if (object == null) return false;
		if (this == object) return true;
		if (!(object instanceof Nick)) return false;
		Nick nick = (Nick) object;
		return nick.getId().equals(id);
	}


	// Getters & setters

	@Id
	@GeneratedValue
	@Column(name = "id")
	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }

	@Column(name = "name", length = 20, nullable = false, unique = true)
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	@Column(name = "description", length = 300, nullable = true)
	public String getDescription() {return description; }
	public void setDescription(String description) { this.description = description; }

	@Column(name = "is_keyword", nullable = false)
	public Boolean getIsKeyword() { return isKeyword; }
	public void setIsKeyword(Boolean isKeyword) { this.isKeyword = isKeyword; }
	
	@ManyToMany(mappedBy = "nicks", fetch = FetchType.LAZY)
	public Set<Operator> getOperators() { return operators; }
	public void setOperators(Set<Operator> operators) { this.operators = operators; }

}
