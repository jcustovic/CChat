package hr.chus.cchat.model.db.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "bot")
public class Robot extends AbstractBaseEntity {

    @Column(name = "name", length = 50, nullable = false, unique = true)
    private String  name;

    @Column(name = "description", length = 1000)
    private String  description;

    @Column(name = "online", columnDefinition = "BIT")
    private Boolean online;

    // Getters & setters

    public final String getName() {
        return name;
    }

    public final void setName(final String p_name) {
        this.name = p_name;
    }

    public final String getDescription() {
        return description;
    }

    public final void setDescription(final String p_description) {
        this.description = p_description;
    }

    public final Boolean getOnline() {
        return online;
    }

    public final void setOnline(final Boolean p_online) {
        online = p_online;
    }

}
