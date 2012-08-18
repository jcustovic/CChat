package hr.chus.cchat.struts2.action.common;

import hr.chus.cchat.db.service.SMSMessageService;
import hr.chus.cchat.db.service.ServiceProviderKeywordService;
import hr.chus.cchat.db.service.ServiceProviderService;
import hr.chus.cchat.db.service.UserService;
import hr.chus.cchat.helper.OperatorChooser;
import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.db.jpa.SMSMessage;
import hr.chus.cchat.model.db.jpa.SMSMessage.DeliveryStatus;
import hr.chus.cchat.model.db.jpa.SMSMessage.Direction;
import hr.chus.cchat.model.db.jpa.ServiceProvider;
import hr.chus.cchat.model.db.jpa.ServiceProviderKeyword;
import hr.chus.cchat.model.db.jpa.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that receives SMS messages. Gateways should invoke this service in order for
 * this application to receive SMS messages.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
public class ReceiveSms extends ActionSupport {

    private static final Logger           LOG = LoggerFactory.getLogger(ReceiveSms.class);

    private static SimpleDateFormat       sdf = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");

    @Autowired
    private ServiceProviderService        serviceProviderService;

    @Autowired
    private UserService                   userService;

    @Autowired
    private SMSMessageService             smsMessageService;

    @Autowired
    private ServiceProviderKeywordService serviceProviderKeywordService;

    @Autowired
    private OperatorChooser               operatorChooser;

    private String                        msisdn;
    private String                        time;
    private String                        text;
    private String                        sc;
    private String                        gatewayId;
    private String                        serviceProviderName;
    private String                        serviceProviderKeyword;

    private Integer                       messageId;
    private String                        status;
    private String                        errorMsg;

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
            LOG.error(errorMsg);
            addActionError(errorMsg);
            status = "failed";
        }
    }

    @Override
    public String execute() throws Exception {
        LOG.info(String.format("Got message from %s (ServiceProvider: %s, SC: %s, Keyword: %s) with text --> %s", new Object[] { msisdn, serviceProviderName,
                sc, serviceProviderKeyword, text }));
        Date date = new Date();
        if (time != null && !time.isEmpty()) {
            try {
                date = sdf.parse(time);
            } catch (ParseException e) {
                LOG.info("Error parsing time " + time);
            }
        }

        final ServiceProvider serviceProvider = serviceProviderService.getByProviderNameAndShortCode(serviceProviderName, sc);
        if (serviceProvider == null) {
            errorMsg = "ServiceProvider not found for provider " + serviceProviderName + " and sc " + sc;
            LOG.error(errorMsg);
            status = "failed";
            return SUCCESS;
        }
        
        ServiceProviderKeyword providerKeyword = null;
        if (serviceProviderKeyword != null && !serviceProviderKeyword.isEmpty()) {
            Set<ServiceProviderKeyword> keywords = serviceProvider.getServiceProviderKeywords();
            if (keywords != null && !keywords.isEmpty()) {
                for (ServiceProviderKeyword keyword : keywords) {
                    if (serviceProviderKeyword.equalsIgnoreCase(keyword.getKeyword())) {
                        providerKeyword = keyword;
                    }
                }
            }
            if (providerKeyword == null) {
                providerKeyword = serviceProviderKeywordService.addOrEditServiceProviderKeyword(new ServiceProviderKeyword(serviceProvider,
                        serviceProviderKeyword, null));
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
            LOG.info("New user (" + user + ") registred to service " + serviceProvider.getServiceName());
        } else {
            user.setUnreadMsgCount(user.getUnreadMsgCount() + 1);
            user.setLastMsg(new Date());
            Operator operator = user.getOperator();
            if (operator == null || !operator.getIsActive()) {
                user.setOperator(operatorChooser.chooseOperator());
            }
            if (!user.getServiceProvider().equals(serviceProvider)) {
                LOG.info("User " + user + ") changed service provider from " + user.getServiceProvider() + " to " + serviceProvider);
                user.setServiceProvider(serviceProvider);
            }
            userService.editUser(user);
        }

        final SMSMessage message = new SMSMessage(user, null, date, text, sc, serviceProvider, Direction.IN);
        message.setDeliveryStatus(DeliveryStatus.RECEIVED);
        message.setGatewayId(gatewayId);
        message.setServiceProviderKeyword(providerKeyword);
        messageId = smsMessageService.updateSMSMessage(message).getId();
        status = "ok";
        LOG.info("Message saved with id {}", messageId);
        
        return SUCCESS;
    }

    // Getters & setters

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSc(String sc) {
        this.sc = sc;
    }

    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }

    public void setServiceProviderName(String serviceProviderName) {
        this.serviceProviderName = serviceProviderName;
    }

    public void setServiceProviderKeyword(String serviceProviderKeyword) {
        this.serviceProviderKeyword = serviceProviderKeyword;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public String getStatus() {
        return status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

}