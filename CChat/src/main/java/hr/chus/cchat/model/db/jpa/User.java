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
    @JoinColumn(name = "nick_id")
    private Nick            nick;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "operator_id")
    private Operator        operator;

    @Column(name = "msisdn", length = 20, nullable = false, updatable = false)
    private String          msisdn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_provider_id", nullable = false)
    private ServiceProvider serviceProvider;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bot_id")
    private Robot           bot;

    @Column(name = "name", length = 20)
    private String          name;

    @Column(name = "surname", length = 30)
    private String          surname;

    @Column(name = "address", length = 100)
    private String          address;

    @Temporal(TemporalType.DATE)
    @Column(name = "birth_date")
    private Date            birthdate;

    @Column(name = "notes", columnDefinition = "TEXT")
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
        msisdn = p_msisdn;
        serviceProvider = p_serviceProvider;
        joined = date;
        lastMsg = date;
        deleted = false;
        unreadMsgCount = 0;
    }

    public User(final Nick p_nick, final Operator p_operator, final String p_msisdn, final ServiceProvider p_serviceProvider, final String p_name,
            final String p_surname, final Date p_joined) {
        nick = p_nick;
        operator = p_operator;
        msisdn = p_msisdn;
        serviceProvider = p_serviceProvider;
        name = p_name;
        surname = p_surname;
        joined = p_joined;
        deleted = false;
        unreadMsgCount = 0;
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

    public final Nick getNick() {
        return nick;
    }

    public final void setNick(final Nick p_nick) {
        nick = p_nick;
    }

    public final Operator getOperator() {
        return operator;
    }

    public final void setOperator(final Operator p_operator) {
        operator = p_operator;
    }

    public final String getMsisdn() {
        return msisdn;
    }

    public final void setMsisdn(final String p_msisdn) {
        msisdn = p_msisdn;
    }

    public final ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public final void setServiceProvider(final ServiceProvider p_serviceProvider) {
        serviceProvider = p_serviceProvider;
    }

    public final Robot getBot() {
        return bot;
    }

    public final void setBot(final Robot p_bot) {
        bot = p_bot;
    }

    public final String getName() {
        return name;
    }

    public final void setName(final String p_name) {
        name = p_name;
    }

    public final String getSurname() {
        return surname;
    }

    public final void setSurname(final String p_surname) {
        surname = p_surname;
    }

    public final String getAddress() {
        return address;
    }

    public final void setAddress(final String p_address) {
        address = p_address;
    }

    @JSON(format = "yyyy-MM-dd'T'HH:mm:ssZ")
    public final Date getBirthdate() {
        return birthdate;
    }

    public final void setBirthdate(final Date p_birthdate) {
        birthdate = p_birthdate;
    }

    public final String getNotes() {
        return notes;
    }

    public final void setNotes(final String p_notes) {
        notes = p_notes;
    }

    @JSON(format = "yyyy-MM-dd'T'HH:mm:ssZ")
    public final Date getJoined() {
        return joined;
    }

    public final void setJoined(final Date p_joined) {
        joined = p_joined;
    }

    @JSON(format = "yyyy-MM-dd'T'HH:mm:ssZ")
    public final Date getLastMsg() {
        return lastMsg;
    }

    public final void setLastMsg(final Date p_lastMsg) {
        lastMsg = p_lastMsg;
    }

    public final Integer getUnreadMsgCount() {
        return unreadMsgCount;
    }

    public final void setUnreadMsgCount(final Integer p_unreadMsgCount) {
        unreadMsgCount = p_unreadMsgCount;
    }

    public final Boolean getDeleted() {
        return deleted;
    }

    public final void setDeleted(final Boolean p_deleted) {
        deleted = p_deleted;
    }

    public final Set<Picture> getSentPictures() {
        return sentPictures;
    }

    public final void setSentPictures(final Set<Picture> p_sentPictures) {
        sentPictures = p_sentPictures;
    }

}
