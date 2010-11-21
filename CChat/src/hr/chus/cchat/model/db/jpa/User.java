package hr.chus.cchat.model.db.jpa;

import hr.chus.cchat.hibernate.DateTimeType;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
@Entity
@Table(name = "users")
@TypeDefs ({
	@TypeDef(name = "customDateTime", typeClass = DateTimeType.class)
})
@NamedQueries({
	@NamedQuery(name = "User.getAll", query = "SELECT u FROM User u")
	, @NamedQuery(name = "User.getCount", query = "SELECT COUNT(u) FROM User u")
	, @NamedQuery(name = "User.getByMsisdn", query = "SELECT u FROM User u WHERE u.msisdn = :msisdn")
	, @NamedQuery(name = "User.getByOperator", query = "SELECT u FROM User u WHERE u.operator = :operator AND u.deleted = false ORDER BY u.lastMsg DESC")
	, @NamedQuery(name = "User.getRandom", query = "SELECT u FROM User u WHERE u.deleted = false AND u.lastMsg < :lastMsgDate AND u.operator IS NULL ORDER BY RAND()")
	, @NamedQuery(name = "User.getNewest", query = "SELECT u FROM User u WHERE u.deleted = false AND u.lastMsg >= :lastMsgDate AND u.operator IS NULL ORDER BY u.lastMsg DESC")
	, @NamedQuery(name = "User.clearOperatorField", query = "UPDATE User u SET u.operator = null WHERE u.operator = :operator")
	, @NamedQuery(name = "User.assignUsersWithNewMsgToOperator", query = "UPDATE User u SET u.operator = :operator WHERE u.operator IS NULL AND u.unreadMsgCount > 0")
})
@Cache(usage = CacheConcurrencyStrategy.NONE)
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
	private Date lastMsg;
	private Integer unreadMsgCount;
	private Boolean deleted;
	private List<Picture> sentPicturesList;
	
	
	public User() { }
	
	public User(String msisdn, ServiceProvider serviceProvider) {
		Date date = new Date();
		this.msisdn = msisdn;
		this.serviceProvider = serviceProvider;
		this.joined = date;
		this.lastMsg = date;
		this.deleted = false;
		this.unreadMsgCount = 0;
	}

	public User(Nick nick, Operator operator, String msisdn, ServiceProvider serviceProvider, String name, String surname, Date joined) {
		this.nick = nick;
		this.operator = operator;
		this.msisdn = msisdn;
		this.serviceProvider = serviceProvider;
		this.name = name;
		this.surname = surname;
		this.joined = joined;
		this.deleted = false;
		this.unreadMsgCount = 0;
	}
	
	
	@Override
	public String toString() {
		return String.format("User[ID: %s, Msisdn: %s, Name: %s, Surname: %s, Deleted: %s]", new Object[] { id, msisdn, name, surname, deleted });
	}
	
	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof User)) return false;
		User user = (User) object;
		return user.getId().equals(id);
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

	@Column(name = "msisdn", length = 20, nullable = false, unique = true, updatable = false)
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

	@JSON(format = "yyyy-MM-dd'T'HH:mm:ssZ")
	@Temporal(TemporalType.DATE)
	@Column(name = "birth_date", nullable = true)
	public Date getBirthDate() { return birthDate; }
	public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }

	@Column(name = "notes", nullable = true, columnDefinition = "TEXT")
	public String getNotes() { return notes; }
	public void setNotes(String notes) { this.notes = notes; }
	
	@JSON(format = "yyyy-MM-dd'T'HH:mm:ssZ")
//	@Type(type = "customDateTime")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "joined_date", nullable = false, updatable = false)
	public Date getJoined() { return joined; }
	public void setJoined(Date joined) { this.joined = joined; }
	
	@JSON(format = "yyyy-MM-dd'T'HH:mm:ssZ")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_message", nullable = false)
	public Date getLastMsg() { return lastMsg; }
	public void setLastMsg(Date lastMsg) { this.lastMsg = lastMsg; }

	@Column(name = "unread_message_count", nullable = false)
	public Integer getUnreadMsgCount() { return unreadMsgCount; }
	public void setUnreadMsgCount(Integer unreadMsgCount) { this.unreadMsgCount = unreadMsgCount; }

	@Column(name = "deleted")
	public Boolean getDeleted() { return deleted; }
	public void setDeleted(Boolean deleted) { this.deleted = deleted; }

	@Transient
	public List<Picture> getSentPicturesList() { return sentPicturesList; }
	public void setSentPicturesList(List<Picture> sentPicturesList) { this.sentPicturesList = sentPicturesList; }
		
}
