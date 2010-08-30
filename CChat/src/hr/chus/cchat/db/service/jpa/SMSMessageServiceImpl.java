package hr.chus.cchat.db.service.jpa;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import hr.chus.cchat.db.service.SMSMessageService;
import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.db.jpa.SMSMessage;
import hr.chus.cchat.model.db.jpa.SMSMessage.Direction;
import hr.chus.cchat.model.db.jpa.ServiceProvider;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
@Transactional
public class SMSMessageServiceImpl implements SMSMessageService {

	private Log log = LogFactory.getLog(getClass());
	private EntityManager entityManager;
	

	@Override
	public void addSMSMessage(SMSMessage smsMessage) {
		entityManager.persist(smsMessage);
	}

	@Override
	public void removeSMSMessage(SMSMessage smsMessage) {
		entityManager.remove(smsMessage);
	}

	@Override
	public SMSMessage updateSMSMessage(SMSMessage smsMessage) {
		return entityManager.merge(smsMessage);
	}

	@Override
	public SMSMessage getById(Integer id) {
		return entityManager.find(SMSMessage.class, id);
	}

	@Override
	public Object[] search(Operator operator, ServiceProvider serviceProvider, Direction direction, String msisdn, Date startDate, Date endDate, String text, int start, int limit) {
		StringBuffer queryWhereBuffer = new StringBuffer();
		boolean first = true;
		if (operator != null) {
			String query = "sms.operator = :operator ";
			queryWhereBuffer.append(first ? "WHERE " + query : "AND " + query);
			first = false;
		}
		if (serviceProvider != null) {
			String query = "sms.serviceProvider = :serviceProvider ";
			queryWhereBuffer.append(first ? "WHERE " + query : "AND " + query);
			first = false;
		}
		if (msisdn != null && !msisdn.isEmpty()) {
			String query = "sms.user.msisdn LIKE :msisdn ";
			queryWhereBuffer.append(first ? "WHERE " + query : "AND " + query);
			first = false;
		}
		if (text != null && !text.isEmpty()) {
			String query = "sms.text LIKE :text ";
			queryWhereBuffer.append(first ? "WHERE " + query : "AND " + query);
			first = false;
		}
		if (direction != null) {
			String query = "sms.direction = :direction ";
			queryWhereBuffer.append(first ? "WHERE " + query : "AND " + query);
			first = false;
		}
		if (startDate != null) {
			String query = "sms.time >= :startDate ";
			queryWhereBuffer.append(first ? "WHERE " + query : "AND " + query);
			first = false;
		}
		if (endDate != null) {
			String query = "sms.time <= :endDate ";
			queryWhereBuffer.append(first ? "WHERE " + query : "AND " + query);
			first = false;
		}
		
		queryWhereBuffer.append("ORDER BY sms.time DESC");
		String whereString = queryWhereBuffer.toString();
		
		Query query = entityManager.createQuery("SELECT sms FROM SMSMessage sms " + whereString);
		log.debug("Search sms messages query: SELECT sms FROM SMSMessage sms " + whereString);
		
		if (operator != null) query.setParameter("operator", operator);
		if (serviceProvider != null) query.setParameter("serviceProvider", serviceProvider);
		if (msisdn != null && !msisdn.isEmpty()) query.setParameter("msisdn", msisdn + "%");
		if (text != null && !text.isEmpty()) query.setParameter("text", "%" + text + "%");
		if (direction != null) query.setParameter("direction", direction);
		if (startDate != null) query.setParameter("startDate", startDate);
		if (endDate != null) query.setParameter("endDate", endDate);
		
		Object[] result = new Object[2];
		if (limit == 0) {
			result[1] = query.getResultList();
		} else {
			result[1] = query.setFirstResult(start).setMaxResults(limit).getResultList();
		}
		
		Query queryCount = entityManager.createQuery("SELECT COUNT(sms) FROM SMSMessage sms " + whereString);
		if (operator != null) queryCount.setParameter("operator", operator);
		if (serviceProvider != null) queryCount.setParameter("serviceProvider", serviceProvider);
		if (msisdn != null && !msisdn.isEmpty()) queryCount.setParameter("msisdn", msisdn + "%");
		if (text != null && !text.isEmpty()) queryCount.setParameter("text", "%" + text + "%");
		if (direction != null) queryCount.setParameter("direction", direction);
		if (startDate != null) queryCount.setParameter("startDate", startDate);
		if (endDate != null) queryCount.setParameter("endDate", endDate);
		result[0] = queryCount.getSingleResult();
		
		return result;
	}

	
	// Getters & setters
	
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) { this.entityManager = entityManager; }
	
}