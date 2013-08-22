package hr.chus.cchat.db.service;

import java.util.Date;

import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.db.jpa.SMSMessage;
import hr.chus.cchat.model.db.jpa.SMSMessage.DeliveryStatus;
import hr.chus.cchat.model.db.jpa.SMSMessage.Direction;
import hr.chus.cchat.model.db.jpa.ServiceProvider;
import hr.chus.cchat.model.db.jpa.User;

/**
 * SMS message services that DAO needs to implement.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public interface SMSMessageService {

    public void addSMSMessage(SMSMessage p_smsMessage);

    public void removeSMSMessage(SMSMessage p_smsMessage);

    public SMSMessage updateSMSMessage(SMSMessage p_smsMessage);

    public SMSMessage getById(Integer p_id);

    public Object[] search(Operator p_operator, ServiceProvider p_serviceProvider, Direction p_direction, Integer p_userId, String p_userName,
                           String userSurname, String p_msisdn, Date p_startDate, Date p_endDate, String p_text, int p_start, int p_limit);

    public Object[] getConversationByUserId(Integer p_userId, int p_start, int p_limit);

    public void updateSMSMessageOperatorIfNull(Integer p_operatorId, Integer p_userId);

    public SMSMessage getLastReceivedMessage(User p_user);

    public SMSMessage getByGatewayId(String p_gatewayId);

    public void updateStatus(String p_gatewayId, DeliveryStatus p_status, String p_message);
    
    public SMSMessage getLastSentMessage(User p_user);

}
