package hr.chus.cchat.model.db.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.ejb.QueryHints;

/**
 * Class describes Picture DAO model which will be used for JPA/Hibernate implementation.
 * Class defines queries, table and column names and cache.
 * Pictures that are uploaded are represented by this class.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "picture")
@NamedQueries({
        @NamedQuery(name = "Picture.getAll", query = "SELECT p FROM Picture p", hints = { @QueryHint(name = QueryHints.HINT_CACHEABLE, value = "true") }),
        @NamedQuery(name = "Picture.getByName", query = "SELECT p FROM Picture p WHERE p.name = :name", hints = { @QueryHint(name = QueryHints.HINT_CACHEABLE,
                value = "true") }),
        @NamedQuery(name = "Picture.getByNick", query = "SELECT p FROM Picture p WHERE p.nick = :nick", hints = { @QueryHint(name = QueryHints.HINT_CACHEABLE,
                value = "true") }) })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Picture extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nick_id", nullable = true)
    private Nick   nick;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "file_type", length = 15, nullable = false)
    private String type;

    @Column(name = "file_size", nullable = false)
    private Long   length;

    @Transient
    private String url;

    // @ManyToMany(mappedBy = "sentPictures", fetch = FetchType.LAZY)
    // private Set<User> userList;

    public Picture() {}

    public Picture(Nick nick, String name, String type, Long length) {
        this.nick = nick;
        this.name = name;
        this.type = type;
        this.length = length;
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (this == object) return true;
        if (!(object instanceof Picture)) return false;
        final Picture picture = (Picture) object;

        return (picture.getId().equals(getId()));
    }

    // Getters & setters

    public Nick getNick() {
        return nick;
    }

    public void setNick(Nick nick) {
        this.nick = nick;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    //	@JSON(deserialize = false, serialize = false)
    //	public Set<User> getUserList() { return userList; }
    //	public void setUserList(Set<User> userList) { this.userList = userList; }

}
