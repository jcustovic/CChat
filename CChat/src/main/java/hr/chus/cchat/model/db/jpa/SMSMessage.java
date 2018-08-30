package hr.chus.cchat.model.db.jpa;

import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

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
        @NamedQuery(name = "SMSMessage.getByGatewayId", query = "SELECT sms FROM SMSMessage sms WHERE sms.gatewayId = :gatewayId")})
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class SMSMessage extends AbstractBaseEntity {

    public enum Direction {
        IN, OUT
    }

    // Max 15 chars
    public enum DeliveryStatus {
        DELIVERED, RECEIVED, SENT_TO_GATEWAY, SEND_FAILED, DELIVERY_FAILED
    }

    @Column(name = "gateway_id", length = 36)
    private String gatewayId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "operator_id")
    private Operator operator;

    @Column(name = "time", nullable = false, columnDefinition = "DATETIME")
    private Date time;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "sc", nullable = false)
    private String sc;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_provider_id", nullable = false)
    private ServiceProvider serviceProvider;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_provider_keyword_id")
    private ServiceProviderKeyword serviceProviderKeyword;

    @Column(name = "end_user_price")
    private Float endUserPrice;

    @Column(name = "end_user_price_currency")
    private String endUserPriceCurrency;

    @Column(name = "direction", nullable = false, columnDefinition = "ENUM")
    @Enumerated(EnumType.STRING)
    private Direction direction;

    @Column(name = "delivery_status")
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Column(name = "delivery_msg", length = 200)
    private String deliveryMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nick_id")
    private Nick nick;

    @Column(name = "bot_response", columnDefinition = "BIT")
    private Boolean botResponse = Boolean.FALSE;

    @Column(name = "used_send_bean", length = 30)
    private String usedBean;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_to_id")
    private SMSMessage responseTo;

    public SMSMessage() {
    }

    public SMSMessage(final User p_user, final Operator p_operator, final Date p_time, final String p_text, final String p_sc,
                      final ServiceProvider p_serviceProvider, final Direction p_direction) {
        user = p_user;
        operator = p_operator;
        time = p_time;
        text = p_text;
        sc = p_sc;
        serviceProvider = p_serviceProvider;
        direction = p_direction;
    }

    @Override
    public final String toString() {
        return String.format("SMSMessage[ID: %s, Msisdn: %s, Operator: %s, Text: %s, SC: %s, ServiceName: %s]",
                getId(), user.getMsisdn(), operator == null ? "" : operator.getName(), text, sc, serviceProvider.getServiceName());
    }

    @Override
    public final boolean equals(final Object object) {
        if (object == null) return false;
        if (this == object) return true;
        if (!(object instanceof SMSMessage)) return false;
        final SMSMessage smsMessage = (SMSMessage) object;

        return (smsMessage.getId().equals(getId()));
    }

    // Getters & Setters

    public final String getGatewayId() {
        return gatewayId;
    }

    public final void setGatewayId(final String p_gatewayId) {
        gatewayId = p_gatewayId;
    }

    public final User getUser() {
        return user;
    }

    public final void setUser(final User p_user) {
        user = p_user;
    }

    public final Operator getOperator() {
        return operator;
    }

    public final void setOperator(final Operator p_operator) {
        operator = p_operator;
    }

    @JSON(format = "yyyy-MM-dd'T'HH:mm:ssZ")
    public final Date getTime() {
        return time;
    }

    public final void setTime(final Date p_time) {
        time = p_time;
    }

    public final String getText() {
        return text;
    }

    public final void setText(final String p_text) {
        text = p_text;
    }

    public final String getSc() {
        return sc;
    }

    public final void setSc(String p_sc) {
        sc = p_sc;
    }

    public final ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public final void setServiceProvider(final ServiceProvider p_serviceProvider) {
        serviceProvider = p_serviceProvider;
    }

    public final ServiceProviderKeyword getServiceProviderKeyword() {
        return serviceProviderKeyword;
    }

    public final void setServiceProviderKeyword(final ServiceProviderKeyword p_serviceProviderKeyword) {
        serviceProviderKeyword = p_serviceProviderKeyword;
    }

    public final Float getEndUserPrice() {
        return endUserPrice;
    }

    public final void setEndUserPrice(final Float p_endUserPrice) {
        endUserPrice = p_endUserPrice;
    }

    public final String getEndUserPriceCurrency() {
        return endUserPriceCurrency;
    }

    public final void setEndUserPriceCurrency(final String p_endUserPriceCurrency) {
        endUserPriceCurrency = p_endUserPriceCurrency;
    }

    public final Direction getDirection() {
        return direction;
    }

    public final void setDirection(final Direction p_direction) {
        direction = p_direction;
    }

    public final DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public final void setDeliveryStatus(final DeliveryStatus p_deliveryStatus) {
        deliveryStatus = p_deliveryStatus;
    }

    public final String getDeliveryMessage() {
        return deliveryMessage;
    }

    public final void setDeliveryMessage(final String p_deliveryMessage) {
        deliveryMessage = p_deliveryMessage;
    }

    public final Nick getNick() {
        return nick;
    }

    public final void setNick(final Nick p_nick) {
        nick = p_nick;
    }

    public final Boolean getBotResponse() {
        return botResponse;
    }

    public final void setBotResponse(final Boolean p_botResponse) {
        botResponse = p_botResponse;
    }

    public final String getUsedBean() {
        return usedBean;
    }

    public final void setUsedBean(final String p_usedBean) {
        usedBean = p_usedBean;
    }

    public final SMSMessage getResponseTo() {
        return responseTo;
    }

    public final void setResponseTo(final SMSMessage p_responseTo) {
        responseTo = p_responseTo;
    }

}
