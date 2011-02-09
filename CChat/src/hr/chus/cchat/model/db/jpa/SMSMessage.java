package hr.chus.cchat.model.db.jpa;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
 * 
 * SMS messages are represented by this class.
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
@Entity
@Table(name = "sms_message")
@NamedQueries({
	@NamedQuery(name = "SMSMessage.getAll", query = "SELECT sms FROM SMSMessage sms")
	, @NamedQuery(name = "SMSMessage.getByDirectionAndUser", query = "SELECT sms FROM SMSMessage sms WHERE sms.direction = :direction AND sms.user = :user ORDER BY sms.time DESC")
})
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class SMSMessage implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public enum Direction {
		IN, OUT
	}
	
	private Integer id;
	private String gatewayId;
	private User user;
	private Operator operator;
	private Date time;
	private String text;
	private String sc;
	private ServiceProvider serviceProvider;
	private ServiceProviderKeyword serviceProviderKeyword;
	private Direction direction;
	private Nick nick;
	
	
	public SMSMessage() { }
	
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
		return String.format("SMSMessage[ID: %s, Msisdn: %s, Operator: %s, Text: %s, SC: %s, ServiceName: %s]", new Object[] { id, user.getMsisdn(), operator.getName(), text, sc, serviceProvider.getServiceName() });
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public boolean equals(Object object) {
		if (object == null) return false;
		if (this == object) return true;
		if (!(object instanceof SMSMessage) ) return false;
		SMSMessage smsMessage = (SMSMessage) object;
		return (smsMessage.getId().equals(id));
	}
	

	// Getters & Setters

	@Id
	@GeneratedValue
	@Column(name = "id")
	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	
	@Column(name = "gateway_id", nullable = true, length = 36)
	public String getGatewayId() { return gatewayId; }
	public void setGatewayId(String gatewayId) { this.gatewayId = gatewayId; }

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	public User getUser() { return user; }
	public void setUser(User user) { this.user = user; }
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "operator_id", nullable = true)
	public Operator getOperator() { return operator; }
	public void setOperator(Operator operator) { this.operator = operator; }

	@JSON(format = "yyyy-MM-dd'T'HH:mm:ssZ")
	@Column(name = "time", nullable = false, columnDefinition = "DATETIME")
	public Date getTime() { return time; }
	public void setTime(Date time) { this.time = time; }

	@Column(name = "text", nullable = false)
	public String getText() { return text; }
	public void setText(String text) { this.text = text; }

	@Column(name = "sc", nullable = false)
	public String getSc() { return sc; }
	public void setSc(String sc) { this.sc = sc; }

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "service_provider_id", nullable = false)
	public ServiceProvider getServiceProvider() { return serviceProvider; }
	public void setServiceProvider(ServiceProvider serviceProvider) { this.serviceProvider = serviceProvider; }
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "service_provider_keyword_id", nullable = true)
	public ServiceProviderKeyword getServiceProviderKeyword() { return serviceProviderKeyword; }
	public void setServiceProviderKeyword(ServiceProviderKeyword serviceProviderKeyword) { this.serviceProviderKeyword = serviceProviderKeyword; }

	@Column(name = "direction", nullable = false)
	@Enumerated(EnumType.STRING)
	public Direction getDirection() { return direction; }
	public void setDirection(Direction direction) { this.direction = direction; }

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "nick_id", nullable = true)
	public Nick getNick() { return nick; }
	public void setNick(Nick nick) { this.nick = nick; }

}
