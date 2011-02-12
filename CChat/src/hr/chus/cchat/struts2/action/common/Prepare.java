package hr.chus.cchat.struts2.action.common;

import java.util.Date;

import hr.chus.cchat.db.service.ConfigurationService;
import hr.chus.cchat.db.service.NickService;
import hr.chus.cchat.db.service.OperatorService;
import hr.chus.cchat.db.service.RoleService;
import hr.chus.cchat.db.service.SMSMessageService;
import hr.chus.cchat.db.service.ServiceProviderService;
import hr.chus.cchat.db.service.UserService;
import hr.chus.cchat.model.db.jpa.Configuration;
import hr.chus.cchat.model.db.jpa.Nick;
import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.db.jpa.Role;
import hr.chus.cchat.model.db.jpa.SMSMessage;
import hr.chus.cchat.model.db.jpa.SMSMessage.DeliveryStatus;
import hr.chus.cchat.model.db.jpa.SMSMessage.Direction;
import hr.chus.cchat.model.db.jpa.ServiceProvider;
import hr.chus.cchat.model.db.jpa.User;
import hr.chus.cchat.util.StringUtil;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that prepares database. Type parameter tells us what data we want to import.
 * we can define "?type=development" to import test data. Default admin username and password are "admin".
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class Prepare extends ActionSupport {
	
	private static final long serialVersionUID = 1L;

	private OperatorService operatorService;
	private RoleService roleService;
	private ServiceProviderService serviceProviderService;
	private NickService nickService;
	private UserService userService;
	private SMSMessageService smsMessageService;
	private ConfigurationService configurationService;
	
	private String type;
	private String message;
	
	
	@Override
	public String execute() {
		try {
			if (type != null && type.equals("development")) {
				developPrepare();
			} else {
				productionPrepare();
			}
		} catch (Exception e) {
			message = e.getMessage();
		}
		if (message == null) {
			message = getText("prepare.success");
		}
		return SUCCESS;
	}
	
	private void developPrepare() {
		Role admin = new Role("admin", "Administrator user type");
		roleService.addRole(admin);
		
		Role moderator = new Role("moderator", "Moderator user type");
		roleService.addRole(moderator);
		
		Role operator = new Role("operator", "Typical user type who deals with sms messages");
		roleService.addRole(operator);
		
		Operator adminUser = new Operator("admin", StringUtil.encodePassword("admin", "SHA"), admin, false, false, false);
		adminUser.setName("admin");
		adminUser.setSurname("admin");
		operatorService.addOperator(adminUser);
		
		Operator user = new Operator("user", StringUtil.encodePassword("user", "SHA"), operator, false, false, false);
		user.setName("user");
		user.setSurname("user");
		operatorService.addOperator(user);
		
		Configuration smsMaxLength = new Configuration("smsMaxLength", "160");
		configurationService.addConfiguration(smsMaxLength);
		
		ServiceProvider vipServiceProvider = new ServiceProvider("66111", "VIP", "testService", "VIP mreža", false);
		serviceProviderService.addServiceProvider(vipServiceProvider);
		
		ServiceProvider tmobServiceProvider = new ServiceProvider("66111", "Tmobile", "testService", "TMobile mreža", false);
		serviceProviderService.addServiceProvider(tmobServiceProvider);
		
		ServiceProvider tele2ServiceProvider = new ServiceProvider("66111", "Tele2", "testService", "Tele2 mreža", false);
		serviceProviderService.addServiceProvider(tele2ServiceProvider);
		
		Nick nick = new Nick("Test", "Testni nick", false);
		nickService.addNick(nick);
		
		for (int i = 0; i < 20; i++) {
			User userUser = new User(nick, user, String.valueOf(i), vipServiceProvider, "Name" + String.valueOf(i), "Surname" + String.valueOf(i), new Date());
			userUser.setLastMsg(new Date());
			userUser = userService.editUser(userUser);
			for (int j = 0; j < 2; j++) {
				SMSMessage smsMessage = new SMSMessage(userUser, user, new Date(), "Text " + i + j + "IN sfdsdafkj ghkldfhg fdkjhg dkfljhg dkljh", "12346", vipServiceProvider, Direction.IN);
				smsMessage.setDeliveryStatus(DeliveryStatus.RECEIVED);
				smsMessageService.addSMSMessage(smsMessage);
			}
			for (int j = 0; j < 2; j++) {
				SMSMessage smsMessage = new SMSMessage(userUser, user, new Date(), "Text " + i + j + "OUT sfdsdafkj ghkldfhg fdkjhg dkfljhg dkljh", "12346", vipServiceProvider, Direction.OUT);
				smsMessage.setDeliveryStatus(DeliveryStatus.SENT_TO_GATEWAY);
				smsMessageService.addSMSMessage(smsMessage);
			}
		}
		
		for (int i = 20; i < 40; i++) {
			User userUser = new User(null, adminUser, String.valueOf(i), tmobServiceProvider, "Name" + String.valueOf(i), "Surname" + String.valueOf(i), new Date());
			userUser.setLastMsg(new Date());
			userUser = userService.editUser(userUser);
			for (int j = 0; j < 2; j++) {
				SMSMessage smsMessage = new SMSMessage(userUser, user, new Date(), "Text " + i + j + "IN sfdsdafkj ghkldfhg fdkjhg dkfljhg dkljh", "12346", tmobServiceProvider, Direction.IN);
				smsMessage.setNick(nick);
				smsMessage.setDeliveryStatus(DeliveryStatus.RECEIVED);
				smsMessageService.addSMSMessage(smsMessage);
			}
			for (int j = 0; j < 2; j++) {
				SMSMessage smsMessage = new SMSMessage(userUser, user, new Date(), "Text " + i + j + "OUT sfdsdafkj ghkldfhg fdkjhg dkfljhg dkljh", "12346", tmobServiceProvider, Direction.OUT);
				smsMessage.setNick(nick);
				smsMessage.setDeliveryStatus(DeliveryStatus.SENT_TO_GATEWAY);
				smsMessageService.addSMSMessage(smsMessage);
			}
		}
		
		for (int i = 40; i < 55; i++) {
			User userUser = new User(null, adminUser, String.valueOf(i), tele2ServiceProvider, "Name" + String.valueOf(i), "Surname" + String.valueOf(i), new Date());
			userUser.setLastMsg(new Date());
			userUser = userService.editUser(userUser);
			for (int j = 0; j < 3; j++) {
				SMSMessage smsMessage = new SMSMessage(userUser, user, new Date(), "Text " + i + j + "IN sfdsdafkj ghkldfhg fdkjhg dkfljhg dkljh", "12346", tele2ServiceProvider, Direction.IN);
				smsMessage.setDeliveryStatus(DeliveryStatus.RECEIVED);
				smsMessageService.addSMSMessage(smsMessage);
			}
			for (int j = 0; j < 1; j++) {
				SMSMessage smsMessage = new SMSMessage(userUser, user, new Date(), "Text " + i + j + "OUT sfdsdafkj ghkldfhg fdkjhg dkfljhg dkljh", "12346", tele2ServiceProvider, Direction.OUT);
				smsMessage.setDeliveryStatus(DeliveryStatus.SENT_TO_GATEWAY);
				smsMessageService.addSMSMessage(smsMessage);
			}
		}
	}

	private void productionPrepare() {		
		Role admin = new Role("admin", "Administrator user type");
		roleService.addRole(admin);
		
		Role moderator = new Role("moderator", "Moderator user type");
		roleService.addRole(moderator);
		
		Role operator = new Role("operator", "Typical user type who deals with sms messages");
		roleService.addRole(operator);
		
		Operator adminUser = new Operator("admin", StringUtil.encodePassword("admin", "SHA"), admin, false, false, false);
		adminUser.setName("admin");
		adminUser.setSurname("admin");
		operatorService.addOperator(adminUser);
		
		Operator user = new Operator("user", StringUtil.encodePassword("user", "SHA"), operator, false, false, false);
		user.setName("user");
		user.setSurname("user");
		operatorService.addOperator(user);
		
		Configuration smsMaxLength = new Configuration("smsMaxLength", "160");
		configurationService.addConfiguration(smsMaxLength);
	}
	
	
	// Getters & setters
	
	public void setOperatorService(OperatorService operatorService) { this.operatorService = operatorService; }
	
	public void setRoleService(RoleService roleService) { this.roleService = roleService; }
	
	public void setServiceProviderService(ServiceProviderService serviceProviderService) { this.serviceProviderService = serviceProviderService; }
	
	public void setNickService(NickService nickService) { this.nickService = nickService; }
	
	public void setUserService(UserService userService) { this.userService = userService; }
	
	public void setSmsMessageService(SMSMessageService smsMessageService) { this.smsMessageService = smsMessageService; }
	
	public void setConfigurationService(ConfigurationService configurationService) { this.configurationService = configurationService; }

	public void setType(String type) { this.type = type; }

	public String getMessage() { return message; }
	public void setMessage(String message) { this.message = message; }	
		
}
