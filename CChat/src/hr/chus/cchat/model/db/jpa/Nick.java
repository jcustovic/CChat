package hr.chus.cchat.model.db.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
@Entity
@Table(name = "Nicks")
@NamedQueries({
	@NamedQuery(name = "Nick.getAll", query = "SELECT n FROM Nick n ORDER BY n.name")
	, @NamedQuery(name = "Nick.getByName", query = "SELECT n FROM Nick n WHERE n.name = :name")
})
public class Nick implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;
	private String description;
	
	public Nick() { }
	

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

}
