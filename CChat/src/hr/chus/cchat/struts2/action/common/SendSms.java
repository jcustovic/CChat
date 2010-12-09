package hr.chus.cchat.struts2.action.common;

import java.util.Date;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hr.chus.cchat.db.service.SMSMessageService;
import hr.chus.cchat.db.service.UserService;
import hr.chus.cchat.gateway.SendMessageService;
import hr.chus.cchat.helper.UserAware;
import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.db.jpa.SMSMessage;
import hr.chus.cchat.model.db.jpa.User;
import hr.chus.cchat.model.db.jpa.SMSMessage.Direction;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that we invoke to send messages thru message gateway.
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class SendSms extends ActionSupport implements UserAware {

	private static final long serialVersionUID = 1L;
	
	private Log log = LogFactory.getLog(getClass());
	
	private SMSMessageService smsMessageService;
	private SendMessageService sendMessageService;
	private UserService userService;
	private Operator operator;
	
	private User user;
	private String msgType;
	private String text;
	
	private Boolean status;
	private String errorMsg;
	private SMSMessage smsMessage;
	
	
	@Override
	public void validate() {
		if (operator == null) {
			errorMsg = getText("sendSms.operator.notRecognized");
		} else if (!operator.getIsActive() && !operator.getRole().getName().equals("admin")) {
			errorMsg = getText("sendSms.operator.mustBeActive");
		} else if (user == null) {
			errorMsg = getText("sendSms.user.notFound");
		} else if (user.getServiceProvider().getDisabled()) {
			errorMsg = getText("sendSms.serviceProvider.disabled", new String[] { user.getServiceProvider().getProviderName(), user.getServiceProvider().getSc() });
		} else if (user.getOperator() != null && !user.getOperator().equals(operator) && !operator.getRole().getName().equals("admin")) {
			errorMsg = getText("sendSms.user.belongsToAnotherOperator", new String[] { user.getOperator().getUsername() });
		} else if (msgType == null || msgType.isEmpty()) {
			errorMsg = getText("sendSms.msgType.notDefiend");
		} else if (text == null) {
			errorMsg = getText("sendSms.text.notNull");
		}
		// TODO: Check max msg length from configurationService
		if (errorMsg != null) {
			log.error(errorMsg);
			addActionError(errorMsg);
			status = false;
		}
	}
	
	@Override
	public String execute() throws Exception {
		log.info("Sending message to user " + user + " --> type: " + msgType + ", text: " + text);
		SMSMessage newSmsMessage = new SMSMessage(user, operator, new Date(), text, user.getServiceProvider().getSc(), user.getServiceProvider(), Direction.OUT);
		String gatewayResponse = null;
//		try {
//			if (msgType.equals("wapPush")) {
//				gatewayResponse = sendMessageService.sendWapPushMessage(newSmsMessage);
//			} else {
//				gatewayResponse = sendMessageService.sendSmsMessage(newSmsMessage);
//			}
//		} catch (HttpException e) {
//			log.error(e, e);
//			errorMsg = getText("sendSms.httpException");
//			status = false;
//			return SUCCESS;
//		} catch (Exception e) {
//			log.error(e, e);
//			errorMsg = getText("sendSms.exception", new String[] { e.getMessage() });
//			status = false;
//			return SUCCESS;
//		}
		smsMessage = smsMessageService.updateSMSMessage(newSmsMessage);
		log.info("Message " + smsMessage + " sent. Gateway response: " + gatewayResponse);
		if (user.getOperator() == null && !operator.getRole().getName().equals("admin")) {
			user.setOperator(operator);
			user = userService.editUser(user);
		}
		status = true;
		return SUCCESS;
	}

	
	// Getters & setters
	
	@Override
	public void setAuthenticatedUser(Operator operator) { this.operator = operator; }
	
	public void setSmsMessageService(SMSMessageService smsMessageService) { this.smsMessageService = smsMessageService; }
	
	public void setSendMessageService(SendMessageService sendMessageService) { this.sendMessageService = sendMessageService; }
	
	public void setUserService(UserService userService) { this.userService = userService; }

	public Boolean getStatus() { return status; }

	public String getErrorMsg() { return errorMsg; }

	public void setUser(User user) { this.user = user; }

	public void setMsgType(String msgType) { this.msgType = msgType; }

	public void setText(String text) { this.text = text; }

	public SMSMessage getSmsMessage() { return smsMessage; }
	
}
