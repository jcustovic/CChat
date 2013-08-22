package hr.chus.cchat.db.service.jpa;

import hr.chus.cchat.db.repository.SMSMessageRepository;
import hr.chus.cchat.db.service.SMSMessageService;
import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.db.jpa.SMSMessage;
import hr.chus.cchat.model.db.jpa.SMSMessage.DeliveryStatus;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    private static final Logger            LOG = LoggerFactory.getLogger(SMSMessageServiceImpl.class);

    @PersistenceContext
    private transient EntityManager        entityManager;

    @Autowired
    private transient SMSMessageRepository smsMessageRepository;

    @Override
    public final void addSMSMessage(final SMSMessage p_smsMessage) {
        entityManager.persist(p_smsMessage);
    }

    @Override
    public final void removeSMSMessage(final SMSMessage p_smsMessage) {
        final SMSMessage smsMessage = entityManager.getReference(SMSMessage.class, p_smsMessage.getId());
        entityManager.remove(smsMessage);
    }

    @Override
    public final SMSMessage updateSMSMessage(final SMSMessage p_smsMessage) {
        return entityManager.merge(p_smsMessage);
    }

    @Override
    public final SMSMessage getById(final Integer p_id) {
        return entityManager.find(SMSMessage.class, p_id);
    }

    @Override
    public final Object[] search(final Operator p_operator, final ServiceProvider p_serviceProvider, final Direction p_direction, final Integer p_userId,
                                 final String p_username, final String p_userSurname, final String p_msisdn, final Date p_startDate, final Date p_endDate,
                                 final String p_text, final int p_start, final int p_limit) {
        final StringBuffer queryWhereBuffer = new StringBuffer();
        boolean first = true;

        if (p_operator != null) {
            String query = "sms.operator = :operator ";
            queryWhereBuffer.append(first ? "WHERE " + query : "AND " + query);
            first = false;
        }
        if (p_serviceProvider != null) {
            String query = "sms.serviceProvider = :serviceProvider ";
            queryWhereBuffer.append(first ? "WHERE " + query : "AND " + query);
            first = false;
        }
        if (p_userId != null) {
            String query = "sms.user.id = :userId ";
            queryWhereBuffer.append(first ? "WHERE " + query : "AND " + query);
            first = false;
        }
        if (p_username != null && !p_username.isEmpty()) {
            String query = "sms.user.name LIKE :userName ";
            queryWhereBuffer.append(first ? "WHERE " + query : "AND " + query);
            first = false;
        }
        if (p_userSurname != null && !p_userSurname.isEmpty()) {
            String query = "sms.user.surname LIKE :userSurname ";
            queryWhereBuffer.append(first ? "WHERE " + query : "AND " + query);
            first = false;
        }
        if (p_msisdn != null && !p_msisdn.isEmpty()) {
            String query = "sms.user.msisdn LIKE :msisdn ";
            queryWhereBuffer.append(first ? "WHERE " + query : "AND " + query);
            first = false;
        }
        if (p_text != null && !p_text.isEmpty()) {
            String query = "sms.text LIKE :text ";
            queryWhereBuffer.append(first ? "WHERE " + query : "AND " + query);
            first = false;
        }
        if (p_direction != null) {
            String query = "sms.direction = :direction ";
            queryWhereBuffer.append(first ? "WHERE " + query : "AND " + query);
            first = false;
        }
        if (p_startDate != null) {
            String query = "sms.time >= :startDate ";
            queryWhereBuffer.append(first ? "WHERE " + query : "AND " + query);
            first = false;
        }
        if (p_endDate != null) {
            String query = "sms.time <= :endDate ";
            queryWhereBuffer.append(first ? "WHERE " + query : "AND " + query);
            first = false;
        }

        queryWhereBuffer.append("ORDER BY sms.time DESC");
        final String whereString = queryWhereBuffer.toString();

        final Query query = entityManager.createQuery("SELECT sms FROM SMSMessage sms " + whereString);
        LOG.debug("Search sms messages query: SELECT sms FROM SMSMessage sms " + whereString);

        if (p_operator != null) query.setParameter("operator", p_operator);
        if (p_serviceProvider != null) query.setParameter("serviceProvider", p_serviceProvider);
        if (p_userId != null) query.setParameter("userId", p_userId);
        if (p_username != null && !p_username.isEmpty()) query.setParameter("userName", p_username + "%");
        if (p_userSurname != null && !p_userSurname.isEmpty()) query.setParameter("userSurname", p_msisdn + "%");
        if (p_msisdn != null && !p_msisdn.isEmpty()) query.setParameter("msisdn", p_msisdn + "%");
        if (p_text != null && !p_text.isEmpty()) query.setParameter("text", "%" + p_text + "%");
        if (p_direction != null) query.setParameter("direction", p_direction);
        if (p_startDate != null) query.setParameter("startDate", p_startDate);
        if (p_endDate != null) query.setParameter("endDate", p_endDate);

        final Object[] result = new Object[2];
        if (p_limit == 0) {
            result[1] = query.getResultList();
        } else {
            result[1] = query.setFirstResult(p_start).setMaxResults(p_limit).getResultList();
        }

        final Query queryCount = entityManager.createQuery("SELECT COUNT(sms.id) FROM SMSMessage sms " + whereString);
        if (p_operator != null) queryCount.setParameter("operator", p_operator);
        if (p_serviceProvider != null) queryCount.setParameter("serviceProvider", p_serviceProvider);
        if (p_userId != null) queryCount.setParameter("userId", p_userId);
        if (p_username != null && !p_username.isEmpty()) queryCount.setParameter("userName", p_username + "%");
        if (p_userSurname != null && !p_userSurname.isEmpty()) queryCount.setParameter("userSurname", p_msisdn + "%");
        if (p_msisdn != null && !p_msisdn.isEmpty()) queryCount.setParameter("msisdn", p_msisdn + "%");
        if (p_text != null && !p_text.isEmpty()) queryCount.setParameter("text", "%" + p_text + "%");
        if (p_direction != null) queryCount.setParameter("direction", p_direction);
        if (p_startDate != null) queryCount.setParameter("startDate", p_startDate);
        if (p_endDate != null) queryCount.setParameter("endDate", p_endDate);
        result[0] = queryCount.getSingleResult();

        return result;
    }

    @Override
    public final Object[] getConversationByUserId(final Integer p_userId, final int p_start, final int p_limit) {
        final Object count = entityManager.createQuery("SELECT COUNT(sms.id) FROM SMSMessage sms WHERE sms.user.id = :userId ORDER BY sms.time DESC")
                .setHint(QueryHints.HINT_CACHEABLE, false).setParameter("userId", p_userId).getSingleResult();
        final List<?> resultList = entityManager
                .createQuery(
                        "SELECT sms.id, sms.text, sms.time, operator.username, sms.direction, _user.msisdn"
                                + " FROM SMSMessage AS sms LEFT JOIN sms.operator AS operator LEFT JOIN sms.user AS _user"
                                + " WHERE sms.user.id = :userId ORDER BY sms.time DESC").setParameter("userId", p_userId).setFirstResult(p_start)
                .setMaxResults(p_limit).setHint(QueryHints.HINT_CACHEABLE, false).getResultList();
        final List<Conversation> conversationList = new LinkedList<Conversation>();
        for (Object object : resultList) {
            final Object[] row = (Object[]) object;
            conversationList.add(new Conversation((Integer) row[0], (String) row[5], (Date) row[2], (String) row[1], (String) row[3], (Direction) row[4]));
        }

        return new Object[] { count, conversationList };
    }

    @Override
    public final void updateSMSMessageOperatorIfNull(final Integer p_operatorId, final Integer p_userId) {
        entityManager.createQuery("UPDATE SMSMessage sms SET sms.operator.id = :operatorId WHERE sms.user.id = :userId AND sms.operator IS NULL")
                .setParameter("operatorId", p_operatorId).setParameter("userId", p_userId).executeUpdate();
    }

    @Override
    public final SMSMessage getLastReceivedMessage(final User p_user) {
        final PageRequest pageable = new PageRequest(0, 1);
        final List<SMSMessage> messages = smsMessageRepository.findByUserAndDirection(p_user.getId(), Direction.IN, pageable);

        if (messages.size() > 0) {
            return messages.get(0);
        } else {
            return null;
        }
    }

    @Override
    public final SMSMessage getByGatewayId(final String p_gatewayId) {
        try {
            return (SMSMessage) entityManager.createNamedQuery("SMSMessage.getByGatewayId").setParameter("gatewayId", p_gatewayId).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public final void updateStatus(final String p_gatewayId, final DeliveryStatus p_status, final String p_message) {
        final SMSMessage sms = getByGatewayId(p_gatewayId);
        if (sms == null) {
            LOG.warn("Message with gateway id {} not found.", p_gatewayId);
        } else if (sms.getDirection() == Direction.OUT) {
            sms.setDeliveryStatus(p_status);
            sms.setDeliveryMessage(p_message);
            entityManager.merge(sms);
        } else {
            LOG.warn("Message with gateway id {} is incoming msg and status will not be updated.", p_gatewayId);
        }
    }

    @Override
    public final SMSMessage getLastSentMessage(final User p_user) {
        final PageRequest pageable = new PageRequest(0, 1);
        final List<SMSMessage> messages = smsMessageRepository.findByUserAndDirection(p_user.getId(), Direction.OUT, pageable);

        if (messages.size() > 0) {
            return messages.get(0);
        } else {
            return null;
        }
    }

}
