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
 * Class describes Nikc DAO model which will be used for JPA/Hibernate implementation.
 * Class defines queries, table and column names and cache.
 * Operators can represent them selves to users using different Nicks (just like a different "personas")
 * Nick have their description and picture set.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "nick")
@NamedQueries({
        @NamedQuery(name = "Nick.getAll", query = "SELECT n FROM Nick n ORDER BY n.name",
                hints = { @QueryHint(name = QueryHints.HINT_CACHEABLE, value = "true") }),
        @NamedQuery(name = "Nick.getByName", query = "SELECT n FROM Nick n WHERE n.name = :name", hints = { @QueryHint(name = QueryHints.HINT_CACHEABLE,
                value = "true") }) })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Nick extends AbstractBaseEntity {

    @Column(name = "name", length = 20, nullable = false, unique = true)
    private String  name;

    @Column(name = "description", length = 300, nullable = true)
    private String  description;

    @Column(name = "is_keyword", nullable = false, columnDefinition = "BIT")
    private Boolean isKeyword;

    public Nick() {}

    public Nick(String name, String description, boolean isKeyword) {
        this.name = name;
        this.description = description;
        this.isKeyword = isKeyword;
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (this == object) return true;
        if (!(object instanceof Nick)) return false;
        final Nick nick = (Nick) object;

        return nick.getId().equals(getId());
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

    public Boolean getIsKeyword() {
        return isKeyword;
    }

    public void setIsKeyword(Boolean isKeyword) {
        this.isKeyword = isKeyword;
    }

}
