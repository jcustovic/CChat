package hr.chus.cchat.db.service.jpa;

import hr.chus.cchat.db.service.SMSMessageService;
import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.db.jpa.SMSMessage;
import hr.chus.cchat.model.db.jpa.SMSMessage.Direction;
import hr.chus.cchat.model.db.jpa.ServiceProvider;
import hr.chus.cchat.model.db.jpa.User;
import hr.chus.cchat.model.helper.db.Conversation;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.ejb.QueryHints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * JPA/Hibernate DAO implementation of SMS message services.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@Service
@Transactional
public class SMSMessageServiceImpl implements SMSMessageService {

    private static final Logger LOG = LoggerFactory.getLogger(SMSMessageServiceImpl.class);

    @PersistenceContext
    private EntityManager       entityManager;

    @Override
    public void addSMSMessage(SMSMessage smsMessage) {
        entityManager.persist(smsMessage);
    }

    @Override
    public void removeSMSMessage(SMSMessage smsMessage) {
        smsMessage = entityManager.getReference(SMSMessage.class, smsMessage.getId());
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
    public Object[] search(Operator operator, ServiceProvider serviceProvider, Direction direction, Integer userId, String userName, String userSurname,
                           String msisdn, Date startDate, Date endDate, String text, int start, int limit) {
        final StringBuffer queryWhereBuffer = new StringBuffer();
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
        if (userId != null) {
            String query = "sms.user.id = :userId ";
            queryWhereBuffer.append(first ? "WHERE " + query : "AND " + query);
            first = false;
        }
        if (userName != null && !userName.isEmpty()) {
            String query = "sms.user.name LIKE :userName ";
            queryWhereBuffer.append(first ? "WHERE " + query : "AND " + query);
            first = false;
        }
        if (userSurname != null && !userSurname.isEmpty()) {
            String query = "sms.user.surname LIKE :userSurname ";
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
        final String whereString = queryWhereBuffer.toString();

        final Query query = entityManager.createQuery("SELECT sms FROM SMSMessage sms " + whereString);
        LOG.debug("Search sms messages query: SELECT sms FROM SMSMessage sms " + whereString);

        if (operator != null) query.setParameter("operator", operator);
        if (serviceProvider != null) query.setParameter("serviceProvider", serviceProvider);
        if (userId != null) query.setParameter("userId", userId);
        if (userName != null && !userName.isEmpty()) query.setParameter("userName", userName + "%");
        if (userSurname != null && !userSurname.isEmpty()) query.setParameter("userSurname", msisdn + "%");
        if (msisdn != null && !msisdn.isEmpty()) query.setParameter("msisdn", msisdn + "%");
        if (text != null && !text.isEmpty()) query.setParameter("text", "%" + text + "%");
        if (direction != null) query.setParameter("direction", direction);
        if (startDate != null) query.setParameter("startDate", startDate);
        if (endDate != null) query.setParameter("endDate", endDate);

        final Object[] result = new Object[2];
        if (limit == 0) {
            result[1] = query.getResultList();
        } else {
            result[1] = query.setFirstResult(start).setMaxResults(limit).getResultList();
        }

        final Query queryCount = entityManager.createQuery("SELECT COUNT(sms.id) FROM SMSMessage sms " + whereString);
        if (operator != null) queryCount.setParameter("operator", operator);
        if (serviceProvider != null) queryCount.setParameter("serviceProvider", serviceProvider);
        if (userId != null) queryCount.setParameter("userId", userId);
        if (userName != null && !userName.isEmpty()) queryCount.setParameter("userName", userName + "%");
        if (userSurname != null && !userSurname.isEmpty()) queryCount.setParameter("userSurname", msisdn + "%");
        if (msisdn != null && !msisdn.isEmpty()) queryCount.setParameter("msisdn", msisdn + "%");
        if (text != null && !text.isEmpty()) queryCount.setParameter("text", "%" + text + "%");
        if (direction != null) queryCount.setParameter("direction", direction);
        if (startDate != null) queryCount.setParameter("startDate", startDate);
        if (endDate != null) queryCount.setParameter("endDate", endDate);
        result[0] = queryCount.getSingleResult();

        return result;
    }

    @Override
    public Object[] getConversationByUserId(Integer userId, int start, int limit) {
        Object count = entityManager.createQuery("SELECT COUNT(sms.id) FROM SMSMessage sms WHERE sms.user.id = :userId ORDER BY sms.time DESC")
                .setHint(QueryHints.HINT_CACHEABLE, false).setParameter("userId", userId).getSingleResult();
        List<?> resultList = entityManager
                .createQuery(
                        "SELECT sms.id, sms.text, sms.time, operator.username, sms.direction, _user.msisdn"
                                + " FROM SMSMessage AS sms LEFT JOIN sms.operator AS operator LEFT JOIN sms.user AS _user"
                                + " WHERE sms.user.id = :userId ORDER BY sms.time DESC").setParameter("userId", userId).setFirstResult(start)
                .setMaxResults(limit).setHint(QueryHints.HINT_CACHEABLE, false).getResultList();
        List<Conversation> conversationList = new LinkedList<Conversation>();
        for (Object object : resultList) {
            Object[] row = (Object[]) object;
            conversationList.add(new Conversation((Integer) row[0], (String) row[5], (Date) row[2], (String) row[1], (String) row[3], (Direction) row[4]));
        }
        return new Object[] { count, conversationList };
    }

    @Override
    public void updateSMSMessageOperatorIfNull(Integer operatorId, Integer userId) {
        entityManager.createQuery("UPDATE SMSMessage sms SET sms.operator.id = :operatorId WHERE sms.user.id = :userId AND sms.operator IS NULL")
                .setParameter("operatorId", operatorId).setParameter("userId", userId).executeUpdate();
    }

    @Override
    public SMSMessage getLastReceivedMessage(User user) {
        @SuppressWarnings("unchecked")
        List<SMSMessage> result = entityManager.createNamedQuery("SMSMessage.getByDirectionAndUser").setParameter("direction", Direction.IN)
                .setParameter("user", user).setMaxResults(1).getResultList();
        if (result.size() == 1) {
            return result.get(0);
        } else {
            return null;
        }
    }

    @Override
    public SMSMessage getByGatewayId(String gatewayId) {
        try {
            return (SMSMessage) entityManager.createNamedQuery("SMSMessage.getByGatewayId").setParameter("gatewayId", gatewayId).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
