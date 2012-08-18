package hr.chus.cchat.struts2.action.common;

import hr.chus.cchat.db.service.SMSMessageService;
import hr.chus.cchat.db.service.UserService;
import hr.chus.cchat.gateway.GatewayResponseError;
import hr.chus.cchat.gateway.SendMessageService;
import hr.chus.cchat.helper.UserAware;
import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.db.jpa.SMSMessage;
import hr.chus.cchat.model.db.jpa.SMSMessage.DeliveryStatus;
import hr.chus.cchat.model.db.jpa.SMSMessage.Direction;
import hr.chus.cchat.model.db.jpa.User;

import java.util.Date;

import org.apache.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that we invoke to send messages thru message gateway.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
public class SendSms extends ActionSupport implements UserAware {

    private static final Logger LOG = LoggerFactory.getLogger(SendSms.class);

    @Autowired
    private ApplicationContext  applicationContext;

    @Autowired
    private SMSMessageService   smsMessageService;

    @Autowired
    private SendMessageService  defaultSendMessageService;

    @Autowired
    private UserService         userService;

    private Operator            operator;
    private User                user;
    private String              msgType;
    private String              text;
    private Boolean             status;
    private String              errorMsg;
    private SMSMessage          smsMessage;

    @Override
    public void validate() {
        if (operator == null) {
            errorMsg = getText("sendSms.operator.notRecognized");
        } else if (!operator.getIsActive() && !operator.getRole().getName().equals("admin")) {
            errorMsg = getText("sendSms.operator.mustBeActive");
        } else if (user == null) {
            errorMsg = getText("sendSms.user.notFound");
        } else if (user.getServiceProvider().getDisabled()) {
            errorMsg = getText("sendSms.serviceProvider.disabled", new String[] { user.getServiceProvider().getProviderName(),
                    user.getServiceProvider().getSc() });
        } else if (user.getOperator() != null && !user.getOperator().equals(operator) && !operator.getRole().getName().equals("admin")) {
            errorMsg = getText("sendSms.user.belongsToAnotherOperator", new String[] { user.getOperator().getName() + " " + user.getOperator().getSurname() });
        } else if (msgType == null || msgType.isEmpty()) {
            errorMsg = getText("sendSms.msgType.notDefiend");
        } else if (text == null) {
            errorMsg = getText("sendSms.text.notNull");
        }
        // TODO: Check max msg length from configurationService
        if (errorMsg != null) {
            LOG.error(errorMsg);
            addActionError(errorMsg);
            status = false;
        }
    }

    @Override
    public String execute() throws Exception {
        LOG.info("Sending message to user " + user + " --> type: " + msgType + ", text: " + text);
        final SMSMessage newSmsMessage = new SMSMessage(user, operator, new Date(), text, user.getServiceProvider().getSc(), user.getServiceProvider(), Direction.OUT);
        String gatewayId = null;
        final SendMessageService sendMessageService;
        if (user.getServiceProvider().getSendServiceBeanName() != null && !user.getServiceProvider().getSendServiceBeanName().isEmpty()) {
            try {
                sendMessageService = (SendMessageService) applicationContext.getBean(user.getServiceProvider().getSendServiceBeanName());
            } catch (BeansException e) {
                LOG.error(e.getMessage(), e);
                errorMsg = e.getMessage();
                status = false;
                return ERROR;
            }
        } else {
            sendMessageService = defaultSendMessageService;
        }
        
        LOG.debug("Sending message: " + newSmsMessage);
        try {
            if ("wapPush".equals(msgType)) {
                gatewayId = sendMessageService.sendWapPushMessage(newSmsMessage);
            } else {
                gatewayId = sendMessageService.sendSmsMessage(newSmsMessage);
            }
            LOG.debug("Send message id from gateway: " + gatewayId);
            newSmsMessage.setGatewayId(gatewayId);
        } catch (GatewayResponseError e) {
            LOG.error("Gateway ResponseCode: " + e.getCode() + "; ErrorMessage: " + e.getMessage());
            errorMsg = getText(e.getMessage());
        } catch (HttpException e) {
            LOG.error(e.getMessage(), e);
            errorMsg = getText("sendSms.httpException");
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            errorMsg = getText("sendSms.exception", new String[] { e.getMessage() });
        }
        if (errorMsg != null) {
            status = false;
            return SUCCESS;
        }
        newSmsMessage.setDeliveryStatus(DeliveryStatus.SENT_TO_GATEWAY);
        smsMessage = smsMessageService.updateSMSMessage(newSmsMessage);
        LOG.info("Message " + smsMessage + " sent. Gateway response id: " + gatewayId);
        
        if (user.getOperator() == null && !operator.getRole().getName().equals("admin")) {
            user.setOperator(operator);
            user = userService.editUser(user);
        }
        status = true;
        
        return SUCCESS;
    }

    // Getters & setters

    @Override
    public void setAuthenticatedUser(Operator operator) {
        this.operator = operator;
    }

    public Boolean getStatus() {
        return status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public void setText(String text) {
        this.text = text;
    }

    public SMSMessage getSmsMessage() {
        return smsMessage;
    }

}
