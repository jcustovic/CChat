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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.ejb.QueryHints;

/**
 * Class describes Operator DAO model which will be used for JPA/Hibernate implementation.
 * Class defines queries, table and column names and cache.
 * User that uses this application and replays to SMS messages.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "operator")
@NamedQueries({
        @NamedQuery(name = "Operator.getAll", query = "SELECT o FROM Operator o ORDER BY o.username", hints = { @QueryHint(name = QueryHints.HINT_CACHEABLE,
                value = "true") }),
        @NamedQuery(name = "Operator.getByUsername", query = "SELECT o FROM Operator o WHERE o.username = :username", hints = { @QueryHint(
                name = QueryHints.HINT_CACHEABLE, value = "true") }),
        @NamedQuery(name = "Operator.getAllByActiveFlag", query = "SELECT o FROM Operator o WHERE o.isActive = :active", hints = { @QueryHint(
                name = QueryHints.HINT_CACHEABLE, value = "true") }) })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Operator extends AbstractBaseEntity {

    @Column(name = "username", length = 30, nullable = false, unique = true)
    private String  username;

    @Column(name = "pass", length = 50, nullable = false)
    private String  password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "operator_role_id", nullable = false)
    @Fetch(FetchMode.JOIN)
    private Role    role;

    @Column(name = "name", length = 20, nullable = true)
    private String  name;

    @Column(name = "surname", length = 30, nullable = true)
    private String  surname;

    @Column(name = "email", length = 50, nullable = true)
    private String  email;

    @Column(name = "is_active", columnDefinition = "BIT")
    private Boolean isActive;

    @Column(name = "is_external", columnDefinition = "BIT")
    private Boolean isExternal;

    @Column(name = "disabled", columnDefinition = "BIT")
    private Boolean disabled;

    public Operator() {}

    public Operator(String username, String password, Role role, Boolean isActive, Boolean disabled, Boolean isExternal) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.isActive = isActive;
        this.disabled = disabled;
        this.isExternal = isExternal;
    }

    @Override
    public String toString() {
        return String.format("Operator[ID: %s, Username: %s, Name: %s, Surname: %s, Disabled: %s, Active: %s]", new Object[] { getId(), username, name,
                surname, disabled, isActive });
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (this == object) return true;
        if (!(object instanceof Operator)) return false;
        final Operator operator = (Operator) object;

        return (operator.getId().equals(getId()));
    }

    // Getters & setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsExternal() {
        return isExternal;
    }

    public void setIsExternal(Boolean isExternal) {
        this.isExternal = isExternal;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

}
