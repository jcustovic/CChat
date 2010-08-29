package hr.chus.cchat.db.service.jpa;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import hr.chus.cchat.db.service.StatisticsService;
import hr.chus.cchat.model.db.jpa.SMSMessage.Direction;
import hr.chus.cchat.model.helper.db.StatisticsPerServiceProvider;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;


/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
@Transactional
public class StatisticsServiceImpl implements StatisticsService {
	
	private EntityManager entityManager;
		
	
	@Override
	public List<StatisticsPerServiceProvider> getStatisticsPerServiceProvider(Date from, Date to) {
		List<StatisticsPerServiceProvider> result = new LinkedList<StatisticsPerServiceProvider>();
		Query query = entityManager.createNativeQuery("SELECT sp.provider_name, sp.sc, SUM(sms.direction = :directionIn), SUM(sms.direction = :directionOut) FROM sms_messages sms JOIN service_provider sp ON sms.service_provider_id = sp.id "
				+ "WHERE sms.time >= :from AND sms.time <= :to "
				+ "GROUP BY sp.id");
		query.setParameter("directionIn", Direction.IN.toString());
		query.setParameter("directionOut", Direction.OUT.toString());
		query.setParameter("from", from);
		query.setParameter("to", to);
		
		List<?> results = query.getResultList();
		for (Object object : results) {
			Object[] row = (Object[]) object;
			result.add(new StatisticsPerServiceProvider((String) row[0], (String) row[1], ((BigDecimal) row[2]).intValue(), ((BigDecimal) row[3]).intValue()));
		}
		return result;
	}
	
//	SELECT o.username
//	, SUM(sms.direction = 'IN')
//	, SUM(sms.direction = 'OUT')
//FROM sms_messages sms JOIN operators o ON sms.operator_id = o.id  
//GROUP BY o.id;
	
	
	// Getters & setters
	
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) { this.entityManager = entityManager; }

}
