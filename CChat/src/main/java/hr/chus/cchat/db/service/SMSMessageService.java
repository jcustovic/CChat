package hr.chus.cchat.db.service;

import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.db.jpa.SMSMessage;
import hr.chus.cchat.model.db.jpa.SMSMessage.DeliveryStatus;
import hr.chus.cchat.model.db.jpa.SMSMessage.Direction;
import hr.chus.cchat.model.db.jpa.ServiceProvider;
import hr.chus.cchat.model.db.jpa.User;

import java.util.Date;

/**
 * SMS message services that DAO needs to implement.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public interface SMSMessageService {

    void addSMSMessage(SMSMessage p_smsMessage);

    void removeSMSMessage(SMSMessage p_smsMessage);

    SMSMessage updateSMSMessage(SMSMessage p_smsMessage);

    SMSMessage getById(Integer p_id);

    Object[] search(Operator p_operator, ServiceProvider p_serviceProvider, Direction p_direction, Integer p_userId, String p_userName,
                           String userSurname, String p_msisdn, Date p_startDate, Date p_endDate, String p_text, int p_start, int p_limit);

    Object[] getConversationByUserId(Integer p_userId, int p_start, int p_limit);

    void updateSMSMessageOperatorIfNull(Integer p_operatorId, Integer p_userId);

    SMSMessage getLastReceivedMessage(User p_user);

    SMSMessage getByGatewayId(String p_gatewayId);

    void updateStatus(String p_gatewayId, DeliveryStatus p_status, String p_message);
    
    SMSMessage getLastSentMessage(User p_user);

}
