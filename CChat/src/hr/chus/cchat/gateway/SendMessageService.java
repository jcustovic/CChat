package hr.chus.cchat.gateway;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;

import hr.chus.cchat.model.db.jpa.SMSMessage;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public interface SendMessageService {
	
	public String sendSmsMessage(SMSMessage smsMessage) throws HttpException, IOException;
	public String sendWapPushMessage(SMSMessage smsMessage) throws HttpException, IOException;
	
}
