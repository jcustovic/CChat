package hr.chus.cchat.model.db.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
@MappedSuperclass
public abstract class AbstractBaseEntity implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Integer id;

    @Override
    public int hashCode() {
        return getId() == null ? 0 : getId().hashCode();
    }

    @Override
    public boolean equals(final Object p_object) {
        if (this == p_object) {
            return true;
        }
        if (p_object == null) {
            return false;
        }
        if (!(p_object instanceof AbstractBaseEntity)) {
            return false;
        }

        final AbstractBaseEntity be = (AbstractBaseEntity) p_object;

        return be.getId() != null && be.getId().equals(getId());
    }

    // Getters & setters

    public final Integer getId() {
        return id;
    }

    public final void setId(final Integer p_id) {
        id = p_id;
    }

}
