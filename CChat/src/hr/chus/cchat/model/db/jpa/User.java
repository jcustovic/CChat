package hr.chus.cchat.model.db.jpa;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "Users")
@NamedQueries({
	@NamedQuery(name = "User.getAll", query = "SELECT u FROM User u")
	, @NamedQuery(name = "User.getCount", query = "SELECT COUNT(u) FROM User u")
})
public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Nick nick;
	private Operator operator;
	private String msisdn;
	private ServiceProvider serviceProvider;
	private String name;
	private String surname;
	private String address;
	private Date birthDate;
	private String notes;
	private Date joined;
	private List<Picture> sentPicturesList;
	
	public User() { }

	public User(Nick nick, Operator operator, String msisdn, ServiceProvider serviceProvider, String name, String surname, Date joined) {
		this.nick = nick;
		this.operator = operator;
		this.msisdn = msisdn;
		this.serviceProvider = serviceProvider;
		this.name = name;
		this.surname = surname;
		this.joined = joined;
	}


	// Getters && setters

	@Id
	@GeneratedValue
	@Column(name = "id")
	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "nick_id", nullable = true)
	public Nick getNick() { return nick; }
	public void setNick(Nick nick) { this.nick = nick; }

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "operator_id", nullable = true)
	public Operator getOperator() { return operator; }
	public void setOperator(Operator operator) { this.operator = operator; }

	@Column(name = "msisdn", length = 20, nullable = false)
	public String getMsisdn() { return msisdn; }
	public void setMsisdn(String msisdn) { this.msisdn = msisdn; }

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "service_provider_id", nullable = false)
	public ServiceProvider getServiceProvider() { return serviceProvider; }
	public void setServiceProvider(ServiceProvider serviceProvider) { this.serviceProvider = serviceProvider; }

	@Column(name = "name", length = 20, nullable = true)
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	@Column(name = "surname", length = 30, nullable = true)
	public String getSurname() { return surname; }
	public void setSurname(String surname) { this.surname = surname; }

	@Column(name = "address", length = 100, nullable = true)
	public String getAddress() { return address; }
	public void setAddress(String address) { this.address = address; }

	@Column(name = "birth_date", nullable = true, columnDefinition = "DATE")
	public Date getBirthDate() { return birthDate; }
	public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }

	@Column(name = "notes", nullable = true, columnDefinition = "TEXT")
	public String getNotes() { return notes; }
	public void setNotes(String notes) { this.notes = notes; }
	
	@Column(name = "joined_date", nullable = false, columnDefinition = "DATETIME")
	public Date getJoined() { return joined; }
	public void setJoined(Date joined) { this.joined = joined; }


	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "Users_Pictures"
			, joinColumns = {
				@JoinColumn(name = "user_id")
			}
			, inverseJoinColumns = {
				@JoinColumn(name = "picture_id")
			})
	public List<Picture> getSentPicturesList() { return sentPicturesList; }
	public void setSentPicturesList(List<Picture> sentPicturesList) { this.sentPicturesList = sentPicturesList; }
		
}
