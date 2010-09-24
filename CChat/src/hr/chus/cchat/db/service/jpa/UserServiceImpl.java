package hr.chus.cchat.db.service.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import hr.chus.cchat.db.service.UserService;
import hr.chus.cchat.model.db.jpa.Nick;
import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.db.jpa.ServiceProvider;
import hr.chus.cchat.model.db.jpa.User;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
@Transactional
public class UserServiceImpl implements UserService {
	
	private Log log = LogFactory.getLog(getClass());
	private EntityManager entityManager;
	

	@Override
	public void addUser(User user) {
		entityManager.persist(user);
	}

	@Override
	public void deleteUser(User user) {
		entityManager.remove(user);
	}

	@Override
	public User editUser(User user) {
		return entityManager.merge(user);
	}

	@Override
	public User getUserId(Integer id) {
		return entityManager.find(User.class, id);
	}

	@Override
	public Object[] searchUsers(Nick nick, Operator operator, ServiceProvider serviceProvider, String msisdn, Integer id, String name, String surname, Boolean deleted, int start, int limit) {
		StringBuffer queryWhereBuffer = new StringBuffer();
		boolean first = true;
		if (nick != null) {
			String query = "u.nick = :nick ";
			queryWhereBuffer.append(first ? "WHERE " + query : "AND " + query);
			first = false;
		}
		if (operator != null) {
			String query = "u.operator = :operator ";
			queryWhereBuffer.append(first ? "WHERE " + query : "AND " + query);
			first = false;
		}
		if (serviceProvider != null) {
			String query = "u.serviceProvider = :serviceProvider ";
			queryWhereBuffer.append(first ? "WHERE " + query : "AND " + query);
			first = false;
		}
		if (msisdn != null && !msisdn.isEmpty()) {
			String query = "u.msisdn LIKE :msisdn ";
			queryWhereBuffer.append(first ? "WHERE " + query : "AND " + query);
			first = false;
		}
		if (id != null) {
			String query = "u.id LIKE :id ";
			queryWhereBuffer.append(first ? "WHERE " + query : "AND " + query);
			first = false;
		}
		if (name != null && !name.isEmpty()) {
			String query = "u.name LIKE :name ";
			queryWhereBuffer.append(first ? "WHERE " + query : "AND " + query);
			first = false;
		}
		if (surname != null && !surname.isEmpty()) {
			String query = "u.surname LIKE :surname ";
			queryWhereBuffer.append(first ? "WHERE " + query : "AND " + query);
			first = false;
		}
		if (deleted != null) {
			String query = "u.deleted = :deleted ";
			queryWhereBuffer.append(first ? "WHERE " + query : "AND " + query);
			first = false;
		}
		
		queryWhereBuffer.append("ORDER BY u.joined DESC");
		String whereString = queryWhereBuffer.toString();
		
		Query query = entityManager.createQuery("SELECT u FROM User u " + whereString);
		log.debug("Search users query: SELECT u FROM User u " + whereString);
		
		if (nick != null) query.setParameter("nick", nick);
		if (operator != null) query.setParameter("operator", operator);
		if (serviceProvider != null) query.setParameter("serviceProvider", serviceProvider);
		if (id != null) query.setParameter("id", id);
		if (msisdn != null && !msisdn.isEmpty()) query.setParameter("msisdn", msisdn + "%");
		if (name != null && !name.isEmpty()) query.setParameter("name", name + "%");
		if (surname != null && !surname.isEmpty()) query.setParameter("surname", surname + "%");
		if (deleted != null) query.setParameter("deleted", deleted);
		
		Object[] result = new Object[2];
		if (limit == 0) {
			result[1] = query.getResultList();
		} else {
			result[1] = query.setFirstResult(start).setMaxResults(limit).getResultList();
		}
		
		Query queryCount = entityManager.createQuery("SELECT COUNT(u) FROM User u " + whereString);
		if (nick != null) queryCount.setParameter("nick", nick);
		if (operator != null) queryCount.setParameter("operator", operator);
		if (serviceProvider != null) queryCount.setParameter("serviceProvider", serviceProvider);
		if (id != null) queryCount.setParameter("id", id);
		if (msisdn != null && !msisdn.isEmpty()) queryCount.setParameter("msisdn", msisdn + "%");
		if (name != null && !name.isEmpty()) queryCount.setParameter("name", name + "%");
		if (surname != null && !surname.isEmpty()) queryCount.setParameter("surname", surname + "%");
		if (deleted != null) queryCount.setParameter("deleted", deleted);
		result[0] = queryCount.getSingleResult();
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<User> getByOperator(Operator operator) {
		return entityManager.createNamedQuery("User.getByOperator").setParameter("operator", operator).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<User> getRandom(int count, Date lastMsgDate) {
		return entityManager.createNamedQuery("User.getRandom").setParameter("lastMsgDate", lastMsgDate).setMaxResults(count).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<User> getNewest(Date lastMsgDate) {
		return entityManager.createNamedQuery("User.getNewest").setParameter("lastMsgDate", lastMsgDate).getResultList();
	}
	
	@Override
	public void clearOperatorField(Operator operator) {
		entityManager.createNamedQuery("User.clearOperatorField").setParameter("operator", operator).executeUpdate();
	}
	
	@Override
	public void assignUsersWithNewMsgToOperator(Operator operator) {
		entityManager.createNamedQuery("User.assignUsersWithNewMsgToOperator").setParameter("operator", operator).executeUpdate();
	}
	
	@Override
	public Long getCount() {
		return (Long) entityManager.createNamedQuery("User.getCount").getSingleResult();
	}
	
	@Override
	public void updateAllMessagesRead(Integer userId) {
		entityManager.createQuery("UPDATE User u SET u.unreadMsgCount = 0 WHERE u.id = :userId").setParameter("userId", userId).executeUpdate();
	}
	
	
	// Getters & setters
	
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) { this.entityManager = entityManager; }

}
