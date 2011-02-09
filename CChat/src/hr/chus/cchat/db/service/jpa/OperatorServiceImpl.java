package hr.chus.cchat.db.service.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import hr.chus.cchat.db.service.OperatorService;
import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.util.StringUtil;

/**
 * JPA/Hibernate DAO implementation of operator services.
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
@Transactional
public class OperatorServiceImpl implements OperatorService {
	
	private EntityManager entityManager;

	
	@Override
	public void addOperator(Operator operator) {
		entityManager.persist(operator);
	}

	@Override
	public Operator authenticateUser(String username, String password) {
		Operator user = getOperatorByUsername(username);
		if (user == null) {
			return null;
		}
		if (user.getUsername().equals(username) && StringUtil.encodePassword(password, "SHA").equals(user.getPassword())) {
			return user;
		} else {
			return null;
		}
	}

	@Override
	public void removeOperator(Operator operator) {
		operator = entityManager.getReference(Operator.class, operator.getId());
		entityManager.remove(operator);
	}

	@Override
	public Operator updateOperator(Operator operator) {
		return entityManager.merge(operator);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Operator getOperatorByUsername(String username) {
		try {
			return (Operator) entityManager.createNamedQuery("Operator.getByUsername").setParameter("username", username).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Operator> getAllOperators() {
		return entityManager.createNamedQuery("Operator.getAll").getResultList();
	}

	@Override
	@Transactional(readOnly = true)
	public boolean checkIfUsernameExists(Operator operator) {
		Operator operatorE = getOperatorByUsername(operator.getUsername());
		if (operatorE == null) {
			return false;
		}
		if (operatorE.getId().equals(operator.getId())) {
			return false;
		}
		return true;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Operator getOperatorById(Integer id) {
		return entityManager.find(Operator.class, id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Operator> getAllActiveOperators() {
		return entityManager.createNamedQuery("Operator.getAllByActiveFlag").setParameter("active", true).getResultList();
	}
	
	@Override
	public int setOperatorActiveFlag(boolean active) {
		return entityManager.createNamedQuery("Operator.setOperatorActiveFlag").setParameter("active", active).executeUpdate();
	}
	
	
	// Getters & setters
	
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) { this.entityManager = entityManager; }

}
