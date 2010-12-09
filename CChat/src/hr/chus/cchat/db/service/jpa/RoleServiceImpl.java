package hr.chus.cchat.db.service.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import hr.chus.cchat.db.service.RoleService;
import hr.chus.cchat.model.db.jpa.Role;

/**
 * JPA/Hibernate DAO implementation of role services.
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
@Transactional
public class RoleServiceImpl implements RoleService {
	
	private EntityManager entityManager;

	@Override
	public void addRole(Role role) {
		entityManager.persist(role);
	}

	@Override
	public void removeRole(Role role) {
		role = entityManager.getReference(Role.class, role.getId());
		entityManager.remove(role);
	}

	@Override
	public Role updateRole(Role role) {
		return entityManager.merge(role);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Role> getAll() {
		return entityManager.createNamedQuery("Role.getAll").getResultList();
	}
	
	@Override
	public Role getRoleById(Integer id) {
		return entityManager.find(Role.class, id);
	}
	
	
	// Getters & setters
	
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) { this.entityManager = entityManager; }

}
