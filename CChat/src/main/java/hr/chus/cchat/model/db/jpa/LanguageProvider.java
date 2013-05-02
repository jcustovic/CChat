package hr.chus.cchat.model.db.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "language_provider")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LanguageProvider extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "language_id", nullable = false)
    @Fetch(FetchMode.JOIN)
    private Language language;

    @Column(name = "prefix", length = 15, nullable = false)
    private String   prefix;

    @Column(name = "send_service_bean", length = 30)
    private String   sendServiceBeanName;

    // Getters & setters

    public final Language getLanguage() {
        return language;
    }

    public final void setLanguage(final Language p_language) {
        language = p_language;
    }

    public final String getPrefix() {
        return prefix;
    }

    public final void setPrefix(final String p_prefix) {
        prefix = p_prefix;
    }

    public final String getSendServiceBeanName() {
        return sendServiceBeanName;
    }

    public final void setSendServiceBeanName(final String p_sendServiceBeanName) {
        sendServiceBeanName = p_sendServiceBeanName;
    }

}
