package hr.chus.cchat.struts2.action.common;

import javax.persistence.EntityManagerFactory;

import org.hibernate.ejb.EntityManagerFactoryImpl;
import org.hibernate.stat.Statistics;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class EntityManagerStatistics extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	
	private EntityManagerFactory entityManagerFactory;
	private Statistics statistics;
	
	
	@Override
	public String execute() throws Exception {
		EntityManagerFactoryInfo emfi = (EntityManagerFactoryInfo) entityManagerFactory;
		EntityManagerFactory emf = emfi.getNativeEntityManagerFactory();
		EntityManagerFactoryImpl empImpl = (EntityManagerFactoryImpl)emf;
		statistics = empImpl.getSessionFactory().getStatistics();
		return SUCCESS;
	}


	// Getters & setters
	
	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) { this.entityManagerFactory = entityManagerFactory; }
	
	public Statistics getStatistics() { return statistics; }
		
}
