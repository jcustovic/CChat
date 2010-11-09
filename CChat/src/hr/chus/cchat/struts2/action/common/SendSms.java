package hr.chus.cchat.struts2.action.common;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hr.chus.cchat.db.service.SMSMessageService;
import hr.chus.cchat.gateway.SendMessageService;
import hr.chus.cchat.helper.UserAware;
import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.db.jpa.SMSMessage;
import hr.chus.cchat.model.db.jpa.User;
import hr.chus.cchat.model.db.jpa.SMSMessage.Direction;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class SendSms extends ActionSupport implements UserAware {

	private static final long serialVersionUID = 1L;
	
	private Log log = LogFactory.getLog(getClass());
	
	private SMSMessageService smsMessageService;
	private SendMessageService sendMessageService;
	private Operator operator;
	
	private User user;
	private String msgType;
	private String text;
	
	private Boolean status;
	private String errorMsg;
	
	
	@Override
	public void validate() {
		if (operator == null) {
			errorMsg = "Operator not recognized";
		} else if (user == null) {
			errorMsg = "User not found";
		} else if (msgType == null || msgType.isEmpty()) {
			errorMsg = "MsgType must be set";
		} else if (text == null) {
			errorMsg = "Text must not be null";
		}
		if (errorMsg != null) {
			log.error(errorMsg);
			addActionError(errorMsg);
			status = false;
		}
	}
	
	@Override
	public String execute() throws Exception {
		log.info("Sending message to user " + user + " --> type: " + msgType + ", text: " + text);
		SMSMessage smsMessage = new SMSMessage(user, operator, new Date(), text, user.getServiceProvider().getSc(), user.getServiceProvider(), Direction.OUT);
		try {
			if (msgType.equals("wapPush")) {
				sendMessageService.sendWapPushMessage(smsMessage);
			} else {
				sendMessageService.sendSmsMessage(smsMessage);
			}
		} catch (Exception e) {
			log.error(e, e);
			errorMsg = e.getMessage();
			status = false;
			return SUCCESS;
		}
		smsMessage = smsMessageService.updateSMSMessage(smsMessage);
		log.info("Message " + smsMessage + " sent.");
		status = true;
		return SUCCESS;
	}

	
	// Getters & setters
	
	@Override
	public void setAuthenticatedUser(Operator operator) { this.operator = operator; }
	
	public void setSmsMessageService(SMSMessageService smsMessageService) { this.smsMessageService = smsMessageService; }
	
	public void setSendMessageService(SendMessageService sendMessageService) { this.sendMessageService = sendMessageService; }

	public Boolean getStatus() { return status; }

	public String getErrorMsg() { return errorMsg; }

	public void setUser(User user) { this.user = user; }

	public void setMsgType(String msgType) { this.msgType = msgType; }

	public void setText(String text) { this.text = text; }

}
