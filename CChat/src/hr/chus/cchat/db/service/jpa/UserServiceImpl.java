package hr.chus.cchat.db.service.jpa;

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

	@SuppressWarnings("unchecked")
	@Override
	public List<User> searchUsers(Nick nick, Operator operator, ServiceProvider serviceProvider, String msisdn, String name, String surname, int start, int limit) {
		StringBuffer queryBuffer = new StringBuffer("SELECT u FROM User u ");
		boolean first = true;
		if (nick != null) {
			String query = "u.nick = :nick ";
			queryBuffer.append(first ? "WHERE " + query : "AND " + query);
			first = false;
		}
		if (operator != null) {
			String query = "u.operator = :operator ";
			queryBuffer.append(first ? "WHERE " + query : "AND " + query);
			first = false;
		}
		if (serviceProvider != null) {
			String query = "u.serviceProvider = :serviceProvider ";
			queryBuffer.append(first ? "WHERE " + query : "AND " + query);
			first = false;
		}
		if (msisdn != null && !msisdn.isEmpty()) {
			String query = "u.msisdn LIKE :msisdn% ";
			queryBuffer.append(first ? "WHERE " + query : "AND " + query);
			first = false;
		}
		if (name != null && !name.isEmpty()) {
			String query = "u.name LIKE :name% ";
			queryBuffer.append(first ? "WHERE " + query : "AND " + query);
			first = false;
		}
		if (surname != null && !surname.isEmpty()) {
			String query = "u.surname LIKE :surname% ";
			queryBuffer.append(first ? "WHERE " + query : "AND " + query);
			first = false;
		}
		
		queryBuffer.append("ORDER BY u.joined DESC");
		Query query = entityManager.createQuery(queryBuffer.toString());
		log.debug("Search users query: " + queryBuffer.toString());
		
		if (nick != null) query.setParameter("nick", nick);
		if (operator != null) query.setParameter("operator", operator);
		if (serviceProvider != null) query.setParameter("serviceProvider", serviceProvider);
		if (msisdn != null && !msisdn.isEmpty()) query.setParameter("msisdn", msisdn);
		if (name != null && !name.isEmpty()) query.setParameter("name", name);
		if (surname != null && !surname.isEmpty()) query.setParameter("surname", surname);
		
		return query.setFirstResult(start).setMaxResults(limit).getResultList();
	}
	
	
	// Getters & setters
	
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) { this.entityManager = entityManager; }

}
