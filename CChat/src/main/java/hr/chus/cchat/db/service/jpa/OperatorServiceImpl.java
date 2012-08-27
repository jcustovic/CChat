package hr.chus.cchat.db.service.jpa;

import hr.chus.cchat.db.service.OperatorService;
import hr.chus.cchat.model.db.jpa.Operator;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * JPA/Hibernate DAO implementation of operator services.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@Service("operatorService")
@Transactional
public class OperatorServiceImpl implements OperatorService {

    @PersistenceContext
    private EntityManager                entityManager;

    @Autowired
    private transient ShaPasswordEncoder shaPasswordEncoder;

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
        if (user.getUsername().equals(username) && shaPasswordEncoder.encodePassword(password, null).equals(user.getPassword())) {
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

}
