package hr.chus.cchat.gateway;

import hr.chus.cchat.model.db.jpa.SMSMessage;

/**
 * This is the interface that must be implemented for sending messages.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public interface SendMessageService {

    public String sendSmsMessage(SMSMessage smsMessage) throws Exception;

    public String sendWapPushMessage(SMSMessage smsMessage) throws Exception;

}
