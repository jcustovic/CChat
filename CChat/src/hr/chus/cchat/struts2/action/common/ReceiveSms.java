package hr.chus.cchat.struts2.action.common;

import hr.chus.cchat.db.service.SMSMessageService;
import hr.chus.cchat.db.service.ServiceProviderKeywordService;
import hr.chus.cchat.db.service.ServiceProviderService;
import hr.chus.cchat.db.service.UserService;
import hr.chus.cchat.helper.OperatorChooser;
import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.db.jpa.SMSMessage;
import hr.chus.cchat.model.db.jpa.SMSMessage.DeliveryStatus;
import hr.chus.cchat.model.db.jpa.ServiceProvider;
import hr.chus.cchat.model.db.jpa.ServiceProviderKeyword;
import hr.chus.cchat.model.db.jpa.User;
import hr.chus.cchat.model.db.jpa.SMSMessage.Direction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that receives SMS messages. Gateways should invoke this service in order for
 * this application to receive SMS messages.
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class ReceiveSms extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	
	private Log log = LogFactory.getLog(getClass());
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
	
	private ServiceProviderService serviceProviderService;
	private UserService userService;
	private SMSMessageService smsMessageService;
	private ServiceProviderKeywordService serviceProviderKeywordService;
	private OperatorChooser operatorChooser;
	
	private String msisdn;
	private String time;
	private String text;
	private String sc;
	private String gatewayId;
	private String serviceProviderName;
	private String serviceProviderKeyword;
	
	private Integer messageId;
	private String status;
	private String errorMsg;
	
	
	@Override
	public void validate() {
		if (msisdn == null || msisdn.isEmpty()) {
			errorMsg = "Msisdn must be set";
		} else if (sc == null || sc.isEmpty()) {
			errorMsg = "SC must be set";
		} else if (serviceProviderName == null || serviceProviderName.isEmpty()) {
			errorMsg = "ServiceProviderName must be set";
		} else if (text == null) {
			errorMsg = "Text must not be null";
		}
		if (errorMsg != null) {
			log.error(errorMsg);
			addActionError(errorMsg);
			status = "failed";
		}
	}
	
	@Override
	public String execute() throws Exception {
		log.info(String.format("Got message from %s (ServiceProvider: %s, SC: %s, Keyword: %s) with text --> %s", new Object[] { msisdn, serviceProviderName, sc, serviceProviderKeyword, text }));
		Date date = new Date();
		if (time != null && !time.isEmpty()) {
			try {
				date = sdf.parse(time);
			} catch (ParseException e) {
				log.info("Error parsing time " + time);
			}
		}
		
		ServiceProvider serviceProvider = serviceProviderService.getByProviderNameAndShortCode(serviceProviderName, sc);
		if (serviceProvider == null) {
			errorMsg = "ServiceProvider not found for provider " + serviceProviderName + " and sc " + sc;
			log.error(errorMsg);
			status = "failed";
			return SUCCESS;
		}
		ServiceProviderKeyword providerKeyword = null;
		if (serviceProviderKeyword != null && !serviceProviderKeyword.isEmpty()) {
			Set<ServiceProviderKeyword> keywords = serviceProvider.getServiceProviderKeywords();
			if (keywords != null && !keywords.isEmpty()) {
				for (ServiceProviderKeyword keyword : keywords) {
					if (serviceProviderKeyword.equals(keyword.getKeyword())) {
						providerKeyword = keyword;
					}
				}
			}
			if (providerKeyword == null) {
				providerKeyword = serviceProviderKeywordService.addOrEditServiceProviderKeyword(new ServiceProviderKeyword(serviceProvider, serviceProviderKeyword, null));
				serviceProvider.getServiceProviderKeywords().add(providerKeyword);
				serviceProviderService.updateServiceProvider(serviceProvider);
			}
		}
		
		User user = userService.getByMsisdnAndServiceName(msisdn, serviceProvider.getServiceName(), false);
		if (user == null) {
			user = new User(msisdn, serviceProvider);
			user.setUnreadMsgCount(1);
			user.setOperator(operatorChooser.chooseOperator());
			user = userService.editUser(user);
			log.info("New user (" + user + ") registred to service " + serviceProvider.getServiceName());
		} else {
			user.setUnreadMsgCount(user.getUnreadMsgCount() + 1);
			user.setLastMsg(new Date());
			Operator operator = user.getOperator();
			if (operator == null || !operator.getIsActive()) {
				user.setOperator(operatorChooser.chooseOperator());
			}
			if (!user.getServiceProvider().equals(serviceProvider)) {
				log.info("User " + user + ") changed service provider from " + user.getServiceProvider() + " to " + serviceProvider);
				user.setServiceProvider(serviceProvider);
			}
			userService.editUser(user);
		}
		
		SMSMessage message = new SMSMessage(user, null, date, text, sc, serviceProvider, Direction.IN);
		message.setDeliveryStatus(DeliveryStatus.RECEIVED);
		message.setGatewayId(gatewayId);
		message.setServiceProviderKeyword(providerKeyword);
		messageId = smsMessageService.updateSMSMessage(message).getId();
		status = "ok";
		return SUCCESS;
	}


	// Getters & setters
	
	public void setServiceProviderService(ServiceProviderService serviceProviderService) { this.serviceProviderService = serviceProviderService; }

	public void setUserService(UserService userService) { this.userService = userService; }

	public void setSmsMessageService(SMSMessageService smsMessageService) { this.smsMessageService = smsMessageService; }
	
	public void setServiceProviderKeywordService( ServiceProviderKeywordService serviceProviderKeywordService) { this.serviceProviderKeywordService = serviceProviderKeywordService; }

	public void setOperatorChooser(OperatorChooser operatorChooser) { this.operatorChooser = operatorChooser; }

	public void setMsisdn(String msisdn) { this.msisdn = msisdn; }

	public void setTime(String time) { this.time = time; }

	public void setText(String text) { this.text = text; }

	public void setSc(String sc) { this.sc = sc; }
	
	public void setGatewayId(String gatewayId) { this.gatewayId = gatewayId; }

	public void setServiceProviderName(String serviceProviderName) { this.serviceProviderName = serviceProviderName; }
	
	public void setServiceProviderKeyword(String serviceProviderKeyword) { this.serviceProviderKeyword = serviceProviderKeyword; }
	
	public Integer getMessageId() { return messageId; }

	public String getStatus() { return status; }

	public String getErrorMsg() { return errorMsg; }

}
