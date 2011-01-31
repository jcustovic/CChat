package hr.chus.cchat.model.db.jpa;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.ejb.QueryHints;

/**
 * Class describes Picture DAO model which will be used for JPA/Hibernate implementation.
 * Class defines queries, table and column names and cache.
 * 
 * Pictures that are uploaded are represented by this class.
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */

@Entity
@Table(name = "picture")
@NamedQueries({
	@NamedQuery(name = "Picture.getAll", query = "SELECT p FROM Picture p", hints = { @QueryHint(name = QueryHints.HINT_CACHEABLE, value = "true") })
	, @NamedQuery(name = "Picture.getByName", query = "SELECT p FROM Picture p WHERE p.name = :name", hints = { @QueryHint(name = QueryHints.HINT_CACHEABLE, value = "true") })
	, @NamedQuery(name = "Picture.getByNick", query = "SELECT p FROM Picture p WHERE p.nick = :nick", hints = { @QueryHint(name = QueryHints.HINT_CACHEABLE, value = "true") })
})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Picture implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Nick nick;
	private String name;
	private String type;
	private Long length;
	private String url;
	private Set<User> userList;
	
	
	public Picture() { }

	public Picture(Nick nick, String name, String type, Long length) {
		this.nick = nick;
		this.name = name;
		this.type = type;
		this.length = length;
	}
	
	
	@Override
	public int hashCode() {
		return id;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object == null) return false;
		if (this == object) return true;
		if (!(object instanceof Picture) ) return false;
		Picture picture = (Picture) object;
		return (picture.getId().equals(id));
	}


	// Getters & setters
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "nick_id", nullable = true)
	public Nick getNick() { return nick; }
	public void setNick(Nick nick) { this.nick = nick; }

	@Column(name = "name", length = 50, nullable = false)
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	@Column(name = "file_type", length = 15, nullable = false)
	public String getType() { return type; }
	public void setType(String type) { this.type = type; }

	@Column(name = "file_size", nullable = false)
	public Long getLength() { return length; }
	public void setLength(Long length) { this.length = length; }

	@Transient
	public String getUrl() { return url; }
	public void setUrl(String url) { this.url = url; }

	@JSON(deserialize = false, serialize = false)
	@ManyToMany(mappedBy = "sentPictures", fetch = FetchType.LAZY)
	public Set<User> getUserList() { return userList; }
	public void setUserList(Set<User> userList) { this.userList = userList; }
		
}
