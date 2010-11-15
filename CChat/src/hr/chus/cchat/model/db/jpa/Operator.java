package hr.chus.cchat.model.db.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
@Entity
@Table(name = "operators")
@NamedQueries({
	@NamedQuery(name = "Operator.getAll", query = "SELECT o FROM Operator o ORDER BY o.username")
	, @NamedQuery(name = "Operator.getByUsername", query = "SELECT o FROM Operator o WHERE o.username = :username")
	, @NamedQuery(name = "Operator.getAllByActiveFlag", query = "SELECT o FROM Operator o WHERE o.isActive = :active")
})
public class Operator implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String username;
	private String password;
	private Role role;
	private String name;
	private String surname;
	private String email;
	private Boolean isActive;
	private Boolean disabled;
	
	
	public Operator() { }
	
	public Operator(String username, String password, Role role, Boolean isActive, Boolean disabled) {
		this.username = username;
		this.password = password;
		this.role = role;
		this.isActive = isActive;
		this.disabled = disabled;
	}
	
	
	@Override
	public String toString() {
		return String.format("Operator[ID: %s, Username: %s, Name: %s, Surname: %s, Disabled: %s, Active: %s]", new Object[] { id, username, name, surname, disabled, isActive });
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (!(object instanceof Operator) ) return false;
		Operator operator = (Operator) object;
		return (operator.getId().equals(id));
	}

	
	// Getters & setters

	@Id
	@GeneratedValue
	@Column(name = "id")
	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	
	@Column(name = "username", length = 30, nullable = false, unique = true)
	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }
	
	@Column(name = "pass", length = 50, nullable = false)
	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "operator_role_id", nullable = false)
	public Role getRole() { return  role; }
	public void setRole(Role role) { this.role = role; }
	
	@Column(name = "name", length = 20, nullable = true)
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	@Column(name = "surname", length = 30, nullable = true)
	public String getSurname() { return surname; }
	public void setSurname(String surname) { this.surname = surname; }
	
	@Column(name = "email", length = 50, nullable = true)
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	@Column(name = "is_active")
	public Boolean getIsActive() { return isActive; }
	public void setIsActive(Boolean isActive) { this.isActive = isActive; }

	@Column(name = "disabled")
	public Boolean getDisabled() { return disabled; }
	public void setDisabled(Boolean disabled) { this.disabled = disabled;}
		
}
