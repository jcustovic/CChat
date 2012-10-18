package hr.chus.programd.multiplexor.jpa;

import hr.chus.cchat.model.db.jpa.AbstractBaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "programd_predicate")
public class Predicate extends AbstractBaseEntity {

    @Column(name = "user_id", length = 128, nullable = false)
    private String userId;

    @Column(name = "bot_id", length = 128, nullable = false)
    private String botId;

    @Column(name = "name", length = 128, nullable = true)
    private String name;

    @Lob
    @Column(name = "value")
    private String value;

    // Getters & setters

    public final String getUserId() {
        return userId;
    }

    public final void setUserId(String p_userId) {
        this.userId = p_userId;
    }

    public final String getBotId() {
        return botId;
    }

    public final void setBotId(String p_botId) {
        this.botId = p_botId;
    }

    public final String getName() {
        return name;
    }

    public final void setName(String p_name) {
        this.name = p_name;
    }

    public final String getValue() {
        return value;
    }

    public final void setValue(String p_value) {
        this.value = p_value;
    }

}
