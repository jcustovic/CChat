package hr.chus.cchat.db.service.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hr.chus.cchat.db.service.NickService;
import hr.chus.cchat.model.db.jpa.Nick;
import hr.chus.cchat.model.db.jpa.Picture;

/**
 * JPA/Hibernate DAO implementation of nick services.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@Service
@Transactional
public class NickServiceImpl implements NickService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addNick(Nick nick) {
        entityManager.persist(nick);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Nick> getAllNicks() {
        return entityManager.createNamedQuery("Nick.getAll").getResultList();
    }

    @Override
    public void removeNick(Nick nick) {
        nick = entityManager.getReference(Nick.class, nick.getId());
        entityManager.remove(nick);
        entityManager.getEntityManagerFactory().getCache().evict(Picture.class);
    }

    @Override
    public Nick updateNick(Nick nick) {
        if (nick.getId() != null) {
            Nick ni = getNickById(nick.getId());
            ni.setDescription(nick.getDescription());
            ni.setIsKeyword(nick.getIsKeyword());
            ni.setName(nick.getName());
            return entityManager.merge(ni);
        }
        return entityManager.merge(nick);
    }

    @Override
    public Nick getNickByName(String name) {
        try {
            return (Nick) entityManager.createNamedQuery("Nick.getByName").setParameter("name", name).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public boolean checkIfNameExists(Nick nick) {
        Nick nickE = getNickByName(nick.getName());
        if (nickE == null) {
            return false;
        }
        if (nickE.getId().equals(nick.getId())) {
            return false;
        }
        return true;
    }

    @Override
    public Nick getNickById(Integer id) {
        return entityManager.find(Nick.class, id);
    }

}
