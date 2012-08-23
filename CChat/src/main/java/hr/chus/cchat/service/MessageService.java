package hr.chus.cchat.service;

import hr.chus.cchat.model.db.jpa.SMSMessage;
import hr.chus.cchat.model.db.jpa.User;

import java.util.Date;

/**
 * Interface for receive sms implementation.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public interface MessageService {

    Integer receiveSms(String p_serviceProviderName, String p_sc, String p_serviceProviderKeyword, String p_msisdn, String p_text, Date p_date,
                       String p_gatewayId);

    SMSMessage sendMessage(SMSMessage p_smsMessage, User p_user, String p_msgType) throws Exception;

}
