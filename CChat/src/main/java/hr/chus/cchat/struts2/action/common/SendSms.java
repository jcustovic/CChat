package hr.chus.cchat.struts2.action.common;

import hr.chus.cchat.db.service.UserService;
import hr.chus.cchat.gateway.GatewayResponseError;
import hr.chus.cchat.helper.UserAware;
import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.db.jpa.SMSMessage;
import hr.chus.cchat.model.db.jpa.SMSMessage.Direction;
import hr.chus.cchat.model.db.jpa.User;
import hr.chus.cchat.service.MessageService;

import java.util.Date;

import org.apache.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

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
    private MessageService      messageService;

    @Autowired
    private UserService         userService;

    private Operator            operator;
    private User                user;
    private String              msgType;
    private String              text;
    private Boolean             status;
    private String              errorMsg;
    private SMSMessage          smsMessage;
    private Boolean             botResponse;

    @Override
    public final void validate() {
        if (operator == null) {
            errorMsg = getText("sendSms.operator.notRecognized");
        } else if (!operator.getIsActive() && !"admin".equals(operator.getRole().getName())) {
            errorMsg = getText("sendSms.operator.mustBeActive");
        } else if (user == null) {
            errorMsg = getText("sendSms.user.notFound");
        } else if (user.getServiceProvider().getDisabled()) {
            errorMsg = getText("sendSms.serviceProvider.disabled", new String[] { user.getServiceProvider().getProviderName(),
                    user.getServiceProvider().getSc() });
        } else if (user.getOperator() != null && !user.getOperator().equals(operator) && !"admin".equals(operator.getRole().getName())) {
            errorMsg = getText("sendSms.user.belongsToAnotherOperator", new String[] { user.getOperator().getName() + " " + user.getOperator().getSurname() });
        } else if (!StringUtils.hasText(msgType)) {
            errorMsg = getText("sendSms.msgType.notDefiend");
        } else if (text == null) {
            errorMsg = getText("sendSms.text.notNull");
        }

        // TODO: Check max msg length from configurationService
        if (errorMsg != null) {
            LOG.info(errorMsg);
            addActionError(errorMsg);
            status = false;
        }
    }

    @Override
    public final String execute() throws Exception {
        LOG.info("Sending message to user " + user + " --> type: " + msgType + ", text: " + text);
        final SMSMessage newSmsMessage = new SMSMessage(user, operator, new Date(), text, user.getServiceProvider().getSc(), user.getServiceProvider(),
                Direction.OUT);

        status = false;
        try {
            smsMessage = messageService.sendMessage(newSmsMessage, botResponse, user, msgType);
            LOG.info("Message {} sent. Gateway response id: {}", smsMessage, smsMessage.getGatewayId());

            // Assign operator to user if not assigned already
            if (user.getOperator() == null && !operator.getRole().getName().equals("admin")) {
                user.setOperator(operator);
                user = userService.editUser(user);
            }
            status = true;
        } catch (GatewayResponseError e) {
            LOG.error("Gateway ResponseCode: {}; ErrorMessage: {}", e.getCode(), e.getMessage());
            errorMsg = getText(e.getMessage());
        } catch (HttpException e) {
            LOG.error(e.getMessage(), e);
            errorMsg = getText("sendSms.httpException");
        } catch (BeansException e) {
            LOG.error(e.getMessage(), e);
            errorMsg = e.getMessage();
            return ERROR;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            errorMsg = getText("sendSms.exception", new String[] { e.getMessage() });
        }

        return SUCCESS;
    }

    // Getters & setters

    @Override
    public final void setAuthenticatedUser(final Operator p_operator) {
        operator = p_operator;
    }

    public final Boolean getStatus() {
        return status;
    }

    public final String getErrorMsg() {
        return errorMsg;
    }

    public final void setUser(final User p_user) {
        user = p_user;
    }

    public final void setMsgType(final String p_msgType) {
        msgType = p_msgType;
    }

    public final void setText(final String p_text) {
        text = p_text;
    }

    public final SMSMessage getSmsMessage() {
        return smsMessage;
    }

    public final void setBotResponse(final Boolean p_botResponse) {
        botResponse = p_botResponse;
    }

}
