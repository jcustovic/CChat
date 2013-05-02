package hr.chus.cchat.model.db.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "language")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Language extends AbstractBaseEntity {

    private static final int SHORT_CODE_MAX_LENGTH = 2;
    private static final int NAME_MAX_LENGTH       = 80;

    @Column(name = "short_code", length = SHORT_CODE_MAX_LENGTH, unique = true, nullable = false, columnDefinition = "CHAR")
    private String           shortCode;

    @Column(name = "name", length = NAME_MAX_LENGTH, nullable = false)
    private String           name;

    // Getters & setters

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(final String p_shortCode) {
        shortCode = p_shortCode;
    }

    public String getName() {
        return name;
    }

    public void setName(final String p_name) {
        name = p_name;
    }

}
