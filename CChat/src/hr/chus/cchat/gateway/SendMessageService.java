package hr.chus.cchat.gateway;

import hr.chus.cchat.model.db.jpa.SMSMessage;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class SendMessageService {
	
	private String sendSmsUrlTemplate;
	private String sendWapPushUrlTemplate;
	
	
	public String sendSmsMessage(SMSMessage smsMessage) {
		// TODO: Implement
		return null;
	}
	
	public String sendWapPushMessage(SMSMessage smsMessage) {
		// TODO: Implement
		return null;
	}

	
	// Getters & setters
	
	public void setSendSmsUrlTemplate(String sendSmsUrlTemplate) { this.sendSmsUrlTemplate = sendSmsUrlTemplate; }

	public void setSendWapPushUrlTemplate(String sendWapPushUrlTemplate) { this.sendWapPushUrlTemplate = sendWapPushUrlTemplate; }
	
}
