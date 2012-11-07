package hr.chus.cchat.model.db.jpa;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Class describes User DAO model which will be used for JPA/Hibernate implementation.
 * Class defines queries, table and column names and cache.
 * Mobile user who send SMS messages to SC is represented by this class.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "users")
@NamedQueries({
        @NamedQuery(name = "User.getAll", query = "SELECT u FROM User u"),
        @NamedQuery(name = "User.getCount", query = "SELECT COUNT(u) FROM User u"),
        @NamedQuery(name = "User.getByMsisdnAndServiceName",
                query = "SELECT u FROM User u WHERE u.msisdn = :msisdn AND serviceProvider.serviceName = :serviceName"),
        @NamedQuery(name = "User.getByOperator", query = "SELECT u FROM User u WHERE u.operator = :operator AND u.deleted = false ORDER BY u.lastMsg DESC"),
        @NamedQuery(name = "User.getRandom",
                query = "SELECT u FROM User u WHERE u.deleted = false AND u.lastMsg < :lastMsgDate AND u.operator IS NULL ORDER BY RAND()"),
        @NamedQuery(name = "User.getNewest",
                query = "SELECT u FROM User u WHERE u.deleted = false AND u.lastMsg >= :lastMsgDate AND u.operator IS NULL ORDER BY u.lastMsg DESC"),
        @NamedQuery(name = "User.clearOperatorField", query = "UPDATE User u SET u.operator = null WHERE u.operator = :operator"),
        @NamedQuery(name = "User.assignUsersWithNewMsgToOperator",
                query = "UPDATE User u SET u.operator = :operator WHERE u.operator IS NULL AND u.unreadMsgCount > 0"),
        @NamedQuery(name = "User.getSentPictureList", query = "SELECT u.sentPictures FROM User u WHERE u.id = :userId") })
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class User extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nick_id", nullable = true)
    private Nick            nick;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "operator_id", nullable = true)
    private Operator        operator;

    @Column(name = "msisdn", length = 20, nullable = false, updatable = false)
    private String          msisdn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_provider_id", nullable = false)
    private ServiceProvider serviceProvider;

    @Column(name = "name", length = 20, nullable = true)
    private String          name;

    @Column(name = "surname", length = 30, nullable = true)
    private String          surname;

    @Column(name = "address", length = 100, nullable = true)
    private String          address;

    @Temporal(TemporalType.DATE)
    @Column(name = "birth_date", nullable = true)
    private Date            birthdate;

    @Column(name = "notes", nullable = true, columnDefinition = "TEXT")
    private String          notes;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "joined_date", nullable = false, updatable = false)
    private Date            joined;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_message", nullable = false)
    private Date            lastMsg;

    @Column(name = "unread_message_count", nullable = false)
    private Integer         unreadMsgCount;

    @Column(name = "deleted", columnDefinition = "BIT")
    private Boolean         deleted;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_picture", joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(
            name = "picture_id", referencedColumnName = "id") })
    private Set<Picture>    sentPictures;

    public User() {}

    public User(final String p_msisdn, final ServiceProvider p_serviceProvider) {
        final Date date = new Date();
        this.msisdn = p_msisdn;
        this.serviceProvider = p_serviceProvider;
        this.joined = date;
        this.lastMsg = date;
        this.deleted = false;
        this.unreadMsgCount = 0;
    }

    public User(final Nick p_nick, final Operator p_operator, final String p_msisdn, final ServiceProvider p_serviceProvider, final String p_name, final String p_surname, final Date p_joined) {
        this.nick = p_nick;
        this.operator = p_operator;
        this.msisdn = p_msisdn;
        this.serviceProvider = p_serviceProvider;
        this.name = p_name;
        this.surname = p_surname;
        this.joined = p_joined;
        this.deleted = false;
        this.unreadMsgCount = 0;
    }

    @Override
    public final String toString() {
        return String.format("User[ID: %s, Msisdn: %s, Name: %s, Surname: %s, Deleted: %s]", new Object[] { getId(), msisdn, name, surname, deleted });
    }

    @Override
    public final boolean equals(final Object p_object) {
        if (p_object == null) return false;
        if (this == p_object) return true;
        if (!(p_object instanceof User)) return false;
        final User user = (User) p_object;

        return user.getId().equals(getId());
    }

    // Getters && setters

    public Nick getNick() {
        return nick;
    }

    public void setNick(Nick nick) {
        this.nick = nick;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @JSON(format = "yyyy-MM-dd'T'HH:mm:ssZ")
    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @JSON(format = "yyyy-MM-dd'T'HH:mm:ssZ")
    public Date getJoined() {
        return joined;
    }

    public void setJoined(Date joined) {
        this.joined = joined;
    }

    @JSON(format = "yyyy-MM-dd'T'HH:mm:ssZ")
    public Date getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(Date lastMsg) {
        this.lastMsg = lastMsg;
    }

    public Integer getUnreadMsgCount() {
        return unreadMsgCount;
    }

    public void setUnreadMsgCount(Integer unreadMsgCount) {
        this.unreadMsgCount = unreadMsgCount;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Set<Picture> getSentPictures() {
        return sentPictures;
    }

    public void setSentPictures(Set<Picture> sentPictures) {
        this.sentPictures = sentPictures;
    }

}
