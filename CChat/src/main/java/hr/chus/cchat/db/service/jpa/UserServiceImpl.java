package hr.chus.cchat.db.service.jpa;

import hr.chus.cchat.db.repository.UserRepository;
import hr.chus.cchat.db.service.UserService;
import hr.chus.cchat.model.db.jpa.Nick;
import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.db.jpa.Picture;
import hr.chus.cchat.model.db.jpa.ServiceProvider;
import hr.chus.cchat.model.db.jpa.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * JPA/Hibernate DAO implementation of user services.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @PersistenceContext
    private EntityManager       entityManager;

    @Autowired
    private UserRepository      userRepository;

    @Override
    public void addUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public void deleteUser(User user) {
        user = entityManager.getReference(User.class, user.getId());
        entityManager.remove(user);
    }

    @Override
    public User editUserAdmin(User user) {
        if (user.getId() != null) {
            User us = getUserById(user.getId(), false);
            us.setAddress(user.getAddress());
            us.setBirthdate(user.getBirthdate());
            us.setDeleted(user.getDeleted());
            us.setName(user.getName());
            us.setNick(user.getNick());
            us.setNotes(user.getNotes());
            us.setOperator(user.getOperator());
            us.setSurname(user.getSurname());
            us.setUnreadMsgCount(user.getUnreadMsgCount());
            return editUser(us);
        }
        return editUser(user);
    }

    @Override
    public User editUserOperator(User user) {
        if (user.getId() != null) {
            User us = getUserById(user.getId(), false);
            us.setAddress(user.getAddress());
            us.setBirthdate(user.getBirthdate());
            us.setName(user.getName());
            us.setNick(user.getNick());
            us.setNotes(user.getNotes());
            us.setSurname(user.getSurname());
            return editUser(us);
        }
        
        return editUser(user);
    }

    @Override
    public User editUser(User user) {
        return entityManager.merge(user);
    }

    @Override
    public User getUserById(Integer id, boolean loadSentPictures) {
        User user = entityManager.find(User.class, id);
        if (loadSentPictures) user.getSentPictures().size();
        return user;
    }

    @Override
    public User getByMsisdnAndServiceName(String msisdn, String serviceName, boolean loadSentPictures) {
        try {
            User user = (User) entityManager.createNamedQuery("User.getByMsisdnAndServiceName").setParameter("msisdn", msisdn)
                    .setParameter("serviceName", serviceName).getSingleResult();
            if (loadSentPictures) user.getSentPictures().size();
            return user;
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Object[] searchUsers(Nick nick, Operator operator, ServiceProvider serviceProvider, String msisdn, Integer id, String name, String surname,
                                Boolean deleted, int start, int limit) {
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
        LOG.debug("Search users query: SELECT u FROM User u " + whereString);

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
    public List<User> getRandom(Date lastMsgDate, int count) {
        return entityManager.createNamedQuery("User.getRandom").setParameter("lastMsgDate", lastMsgDate).setMaxResults(count).getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> getNewest(Date lastMsgDate, int count) {
        return entityManager.createNamedQuery("User.getNewest").setParameter("lastMsgDate", lastMsgDate).setMaxResults(count).getResultList();
    }

    @Override
    public void clearOperatorField(Operator operator) {
        entityManager.createNamedQuery("User.clearOperatorField").setParameter("operator", operator).executeUpdate();
    }

    @Override
    public void clearOperatorField(Operator p_operator, Date p_date) {
        userRepository.clearOperatorField(p_operator, p_date);
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

    @SuppressWarnings("unchecked")
    public List<Picture> getSentPictureList(Integer userId) {
        return entityManager.createNamedQuery("User.getSentPictureList").setParameter("userId", userId).getResultList();
    }

    @Override
    public void addPicture(Integer userId, Picture picture) {
        User user = getUserById(userId, false);
        user.getSentPictures().add(picture);
        entityManager.merge(user);
    }

    @Override
    public final Long countByOperatorAndUnread(final Operator p_operator) {
        return userRepository.countByOperatorAndUnread(p_operator);
    }

    @Override
    public List<User> findUnassigned(int p_count) {
        final List<User> results;
        if (p_count > 0) {
            final PageRequest pageRequest = new PageRequest(0, p_count);
            results = userRepository.findUnassigned(pageRequest);
        } else {
            results = new ArrayList<User>(0);
        }

        return results;
    }

}
