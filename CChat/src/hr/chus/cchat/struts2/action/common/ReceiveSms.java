package hr.chus.cchat.struts2.action.common;

import hr.chus.cchat.db.service.SMSMessageService;
import hr.chus.cchat.db.service.ServiceProviderService;
import hr.chus.cchat.db.service.UserService;
import hr.chus.cchat.helper.OperatorChooser;
import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.db.jpa.SMSMessage;
import hr.chus.cchat.model.db.jpa.ServiceProvider;
import hr.chus.cchat.model.db.jpa.User;
import hr.chus.cchat.model.db.jpa.SMSMessage.Direction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	private OperatorChooser operatorChooser;
	
	private String msisdn;
	private String time;
	private String text;
	private String sc;
	private String serviceProviderName;
	private Boolean status;
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
			status = false;
		}
	}
	
	@Override
	public String execute() throws Exception {
		log.info("Got message from " + msisdn + " (ServiceProvider: " + serviceProviderName + ", SC: " + sc + ") with text --> " + text);
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
			log.error("ServiceProvider not found for provider " + serviceProviderName + " and sc " + sc);
			errorMsg = "ServiceProvider not found for provider " + serviceProviderName + " and sc " + sc;
			status = false;
			return SUCCESS;
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
		
		SMSMessage message = new SMSMessage(user, user.getOperator(), date, text, sc, serviceProvider, Direction.IN);
		smsMessageService.addSMSMessage(message);
		status = true;
		return SUCCESS;
	}


	// Getters & setters
	
	public Boolean getStatus() { return status; }

	public String getErrorMsg() { return errorMsg; }

	public void setServiceProviderService(ServiceProviderService serviceProviderService) { this.serviceProviderService = serviceProviderService; }

	public void setUserService(UserService userService) { this.userService = userService; }

	public void setSmsMessageService(SMSMessageService smsMessageService) { this.smsMessageService = smsMessageService; }
	
	public void setOperatorChooser(OperatorChooser operatorChooser) { this.operatorChooser = operatorChooser; }

	public void setMsisdn(String msisdn) { this.msisdn = msisdn; }

	public void setTime(String time) { this.time = time; }

	public void setText(String text) { this.text = text; }

	public void setSc(String sc) { this.sc = sc; }

	public void setServiceProviderName(String serviceProviderName) { this.serviceProviderName = serviceProviderName; }

}
