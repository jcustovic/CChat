package hr.chus.cchat.struts2.action.common;

import javax.persistence.EntityManagerFactory;

import org.hibernate.ejb.EntityManagerFactoryImpl;
import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that gets Hibernate/JPA entity manager statistics (database statistics).
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
public class EntityManagerStatistics extends ActionSupport {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private Statistics           statistics;

    @Override
    public String execute() throws Exception {
        final EntityManagerFactoryInfo emfi = (EntityManagerFactoryInfo) entityManagerFactory;
        final EntityManagerFactory emf = emfi.getNativeEntityManagerFactory();
        final EntityManagerFactoryImpl empImpl = (EntityManagerFactoryImpl) emf;
        statistics = empImpl.getSessionFactory().getStatistics();
        
        return SUCCESS;
    }

    // Getters & setters

    public Statistics getStatistics() {
        return statistics;
    }

}
