package hr.chus.cchat.struts2.action.common;

import java.util.Date;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import hr.chus.cchat.db.service.SMSMessageService;
import hr.chus.cchat.db.service.UserService;
import hr.chus.cchat.gateway.GatewayResponseError;
import hr.chus.cchat.gateway.SendMessageService;
import hr.chus.cchat.helper.UserAware;
import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.db.jpa.SMSMessage;
import hr.chus.cchat.model.db.jpa.User;
import hr.chus.cchat.model.db.jpa.SMSMessage.DeliveryStatus;
import hr.chus.cchat.model.db.jpa.SMSMessage.Direction;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that we invoke to send messages thru message gateway.
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class SendSms extends ActionSupport implements UserAware, ApplicationContextAware {

	private static final long serialVersionUID = 1L;
	
	private Log log = LogFactory.getLog(getClass());
	
	private ApplicationContext applicationContext;
	private SMSMessageService smsMessageService;
	private SendMessageService defaultSendMessageService;
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
			errorMsg = getText("sendSms.user.belongsToAnotherOperator", new String[] { user.getOperator().getName() + " " + user.getOperator().getSurname() });
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
		String gatewayId = null;
		SendMessageService sendMessageService = defaultSendMessageService;
		if (user.getServiceProvider().getSendServiceBeanName() != null && !user.getServiceProvider().getSendServiceBeanName().isEmpty()) {
			try {
				sendMessageService = (SendMessageService) applicationContext.getBean(user.getServiceProvider().getSendServiceBeanName());
			} catch (BeansException e) {
				log.error(e, e);
				errorMsg = e.getMessage();
				status = false;
				return ERROR;
			}
		}
		log.debug("Sending message: " + newSmsMessage);
		try {
			if (msgType.equals("wapPush")) gatewayId = sendMessageService.sendWapPushMessage(newSmsMessage);
			else gatewayId = sendMessageService.sendSmsMessage(newSmsMessage);
			log.debug("Send message id from gateway: " + gatewayId);
			newSmsMessage.setGatewayId(gatewayId);
		} catch (GatewayResponseError e) {
			log.error("Gateway ResponseCode: " + e.getCode() + "; ErrorMessage: " + e.getMessage());
			errorMsg = getText(e.getMessage());
		} catch (HttpException e) {
			log.error(e, e);
			errorMsg = getText("sendSms.httpException");
		} catch (Exception e) {
			log.error(e, e);
			errorMsg = getText("sendSms.exception", new String[] { e.getMessage() });
		}
		if (errorMsg != null) {
			status = false;
			return SUCCESS;
		}
		newSmsMessage.setDeliveryStatus(DeliveryStatus.SENT_TO_GATEWAY);
		smsMessage = smsMessageService.updateSMSMessage(newSmsMessage);
		log.info("Message " + smsMessage + " sent. Gateway response id: " + gatewayId);
		if (user.getOperator() == null && !operator.getRole().getName().equals("admin")) {
			user.setOperator(operator);
			user = userService.editUser(user);
		}
		status = true;
		return SUCCESS;
	}

	
	// Getters & setters
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException { this.applicationContext = applicationContext; }
	
	@Override
	public void setAuthenticatedUser(Operator operator) { this.operator = operator; }
	
	public void setSmsMessageService(SMSMessageService smsMessageService) { this.smsMessageService = smsMessageService; }
	
	public void setDefaultSendMessageService(SendMessageService defaultSendMessageService) { this.defaultSendMessageService = defaultSendMessageService; }

	public void setUserService(UserService userService) { this.userService = userService; }

	public Boolean getStatus() { return status; }

	public String getErrorMsg() { return errorMsg; }

	public void setUser(User user) { this.user = user; }

	public void setMsgType(String msgType) { this.msgType = msgType; }

	public void setText(String text) { this.text = text; }

	public SMSMessage getSmsMessage() { return smsMessage; }

}
