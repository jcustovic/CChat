package hr.chus.programd.multiplexor.jpa;

import hr.chus.cchat.model.db.jpa.AbstractBaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@SuppressWarnings("serial")
@Entity
@Table(name = "programd_user", uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "bot_id" }))
public class PredicateUser extends AbstractBaseEntity {

    @Column(name = "user_id", length = 128, nullable = true)
    private String userId;

    @Column(name = "bot_id", length = 128, nullable = true)
    private String botId;

    @Column(name = "password", length = 128)
    private String password;

    // Getters & setters
    public final String getUserId() {
        return userId;
    }

    public final void setUserId(final String p_userId) {
        this.userId = p_userId;
    }

    public final String getBotId() {
        return botId;
    }

    public final void setBotId(String p_botId) {
        this.botId = p_botId;
    }

    public final String getPassword() {
        return password;
    }

    public final void setPassword(String p_password) {
        this.password = p_password;
    }

}
