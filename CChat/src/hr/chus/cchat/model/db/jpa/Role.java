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

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
@Entity
@Table(name = "Operator_Roles")
@NamedQueries({
	@NamedQuery(name = "Role.getAll", query = "SELECT r FROM Role r ORDER BY r.name")
})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Role implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;
	private String description;
	
	public Role() { }

	public Role(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}


	@Id
	@GeneratedValue
	@Column(name = "id")
	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }

	@Column(name = "name", length = 20, nullable = false, unique = true)
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	@Column(name = "description", length = 100, nullable = true)
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	
}
