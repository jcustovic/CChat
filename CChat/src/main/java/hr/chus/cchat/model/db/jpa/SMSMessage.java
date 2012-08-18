package hr.chus.cchat.model.db.jpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Class describes SMSMessage DAO model which will be used for JPA/Hibernate implementation.
 * Class defines queries, table and column names and cache.
 * SMS messages are represented by this class.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "sms_message")
@NamedQueries({
        @NamedQuery(name = "SMSMessage.getAll", query = "SELECT sms FROM SMSMessage sms"),
        @NamedQuery(name = "SMSMessage.getByDirectionAndUser",
                query = "SELECT sms FROM SMSMessage sms WHERE sms.direction = :direction AND sms.user = :user ORDER BY sms.time DESC"),
        @NamedQuery(name = "SMSMessage.getByGatewayId", query = "SELECT sms FROM SMSMessage sms WHERE sms.gatewayId = :gatewayId") })
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class SMSMessage extends AbstractBaseEntity {

    public enum Direction {
        IN, OUT
    }

    // Max 15 chars
    public enum DeliveryStatus {
        DELIVERED, RECEIVED, SENT_TO_GATEWAY, SEND_FAILED, DELIVERY_FAILED
    }

    @Column(name = "gateway_id", nullable = true, length = 36)
    private String                 gatewayId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User                   user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "operator_id", nullable = true)
    private Operator               operator;

    @Column(name = "time", nullable = false, columnDefinition = "DATETIME")
    private Date                   time;

    @Column(name = "text", nullable = false)
    private String                 text;

    @Column(name = "sc", nullable = false)
    private String                 sc;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_provider_id", nullable = false)
    private ServiceProvider        serviceProvider;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_provider_keyword_id", nullable = true)
    private ServiceProviderKeyword serviceProviderKeyword;

    @Column(name = "direction", nullable = false, columnDefinition = "ENUM")
    @Enumerated(EnumType.STRING)
    private Direction              direction;

    @Column(name = "delivery_status")
    @Enumerated(EnumType.STRING)
    private DeliveryStatus         deliveryStatus;

    @Column(name = "delivery_msg", length = 200)
    private String                 deliveryMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nick_id", nullable = true)
    private Nick                   nick;

    public SMSMessage() {}

    public SMSMessage(User user, Operator operator, Date time, String text, String sc, ServiceProvider serviceProvider, Direction direction) {
        this.user = user;
        this.operator = operator;
        this.time = time;
        this.text = text;
        this.sc = sc;
        this.serviceProvider = serviceProvider;
        this.direction = direction;
    }

    @Override
    public String toString() {
        return String.format("SMSMessage[ID: %s, Msisdn: %s, Operator: %s, Text: %s, SC: %s, ServiceName: %s]", new Object[] { getId(), user.getMsisdn(),
                operator.getName(), text, sc, serviceProvider.getServiceName() });
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (this == object) return true;
        if (!(object instanceof SMSMessage)) return false;
        final SMSMessage smsMessage = (SMSMessage) object;

        return (smsMessage.getId().equals(getId()));
    }

    // Getters & Setters

    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    @JSON(format = "yyyy-MM-dd'T'HH:mm:ssZ")
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSc() {
        return sc;
    }

    public void setSc(String sc) {
        this.sc = sc;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public ServiceProviderKeyword getServiceProviderKeyword() {
        return serviceProviderKeyword;
    }

    public void setServiceProviderKeyword(ServiceProviderKeyword serviceProviderKeyword) {
        this.serviceProviderKeyword = serviceProviderKeyword;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getDeliveryMessage() {
        return deliveryMessage;
    }

    public void setDeliveryMessage(String deliveryMessage) {
        this.deliveryMessage = deliveryMessage;
    }

    public Nick getNick() {
        return nick;
    }

    public void setNick(Nick nick) {
        this.nick = nick;
    }

}
