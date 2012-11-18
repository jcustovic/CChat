package hr.chus.cchat.service;

import hr.chus.cchat.gateway.GatewayResponseError;
import hr.chus.cchat.model.db.jpa.SMSMessage;
import hr.chus.cchat.model.db.jpa.User;

import java.io.IOException;
import java.util.Date;

import org.apache.http.HttpException;

/**
 * Interface for receive sms implementation.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public interface MessageService {

    Integer[] receiveSms(String p_serviceProviderName, String p_sc, String p_serviceProviderKeyword, String p_msisdn, String p_text, Date p_date,
                         String p_gatewayId);

    SMSMessage sendMessage(SMSMessage p_smsMessage, Boolean p_botResponse, User p_user, String p_msgType) throws HttpException, IOException,
            GatewayResponseError;

}
