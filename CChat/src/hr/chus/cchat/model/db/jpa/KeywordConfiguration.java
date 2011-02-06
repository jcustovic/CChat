package hr.chus.cchat.model.db.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
@Entity
@Table(name = "keyword")
public class KeywordConfiguration {
	
	private Integer id;
	private Nick nick;
	private Operator operator;
	private ServiceProvider serviceProvider;
	
		
	public KeywordConfiguration(Nick nick, Operator operator, ServiceProvider serviceProvider) {
		this.nick = nick;
		this.operator = operator;
		this.serviceProvider = serviceProvider;
	}
	
	
	// Getters & setters
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "nick_id", nullable = false)
	@Fetch(FetchMode.JOIN)
	public Nick getNick() { return nick; }
	public void setNick(Nick nick) { this.nick = nick; }
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "operator_id", nullable = false)
	@Fetch(FetchMode.JOIN)
	public Operator getOperator() { return operator; }
	public void setOperator(Operator operator) { this.operator = operator; }
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "service_provider_id", nullable = false)
	@Fetch(FetchMode.JOIN)
	public ServiceProvider getServiceProvider() { return serviceProvider; }
	public void setServiceProvider(ServiceProvider serviceProvider) { this.serviceProvider = serviceProvider; }
	
}
