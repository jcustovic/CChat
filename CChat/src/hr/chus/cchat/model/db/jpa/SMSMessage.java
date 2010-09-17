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

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
@Entity
@Table(name = "sms_messages")
@NamedQueries({
	@NamedQuery(name = "SMSMessage.getAll", query = "SELECT sms FROM SMSMessage sms")
})
public class SMSMessage implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public enum Direction {
		IN, OUT
	}
	
	private Integer id;
	private User user;
	private Operator operator;
	private Date time;
	private String text;
	private String sc;
	private ServiceProvider serviceProvider;
	private Direction direction;
	
	
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


	// Getters & Setters

	@Id
	@GeneratedValue
	@Column(name = "id")
	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	public User getUser() { return user; }
	public void setUser(User user) { this.user = user; }
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "operator_id", nullable = true)
	public Operator getOperator() { return operator; }
	public void setOperator(Operator operator) { this.operator = operator; }

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

	@Column(name = "direction", nullable = false)
	@Enumerated(EnumType.STRING)
	public Direction getDirection() { return direction; }
	public void setDirection(Direction direction) { this.direction = direction; }

}
