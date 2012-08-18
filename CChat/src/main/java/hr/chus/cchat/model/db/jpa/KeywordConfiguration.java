package hr.chus.cchat.model.db.jpa;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "keyword_conf")
public class KeywordConfiguration extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nick_id", nullable = false)
    @Fetch(FetchMode.JOIN)
    private Nick            nick;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "operator_id", nullable = false)
    @Fetch(FetchMode.JOIN)
    private Operator        operator;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_provider_id", nullable = false)
    @Fetch(FetchMode.JOIN)
    private ServiceProvider serviceProvider;

    public KeywordConfiguration() {}

    public KeywordConfiguration(Nick nick, Operator operator, ServiceProvider serviceProvider) {
        this.nick = nick;
        this.operator = operator;
        this.serviceProvider = serviceProvider;
    }

    // Getters & setters

    public Nick getNick() {
        return nick;
    }

    public void setNick(Nick nick) {
        this.nick = nick;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

}
