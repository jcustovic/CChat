package hr.chus.cchat.model.db.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.ejb.QueryHints;

/**
 * Class describes Role DAO model which will be used for JPA/Hibernate implementation.
 * Class defines queries, table and column names and cache.
 * We use this class for operator access type (operator, moderator or admin).
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "operator_role")
@NamedQueries({ @NamedQuery(name = "Role.getAll", query = "SELECT r FROM Role r ORDER BY r.name", hints = { @QueryHint(name = QueryHints.HINT_CACHEABLE,
        value = "true") }) })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Role extends AbstractBaseEntity {

    @Column(name = "name", length = 20, nullable = false, unique = true, updatable = false)
    private String name;

    @Column(name = "description", length = 100, nullable = true)
    private String description;

    public Role() {}

    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (this == object) return true;
        if (!(object instanceof Role)) return false;
        final Role role = (Role) object;

        return (role.getId().equals(getId()));
    }

    // Getters & setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
