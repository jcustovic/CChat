package hr.chus.cchat.struts2.action.common;

import hr.chus.cchat.exception.LanguageNotFound;
import hr.chus.cchat.service.MessageService;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that receives SMS messages base upon Target Media SMS Gateway v1.3.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
public class TMReceiveSms extends ActionSupport {

    private static final Logger LOG = LoggerFactory.getLogger(TMReceiveSms.class);

    @Autowired
    private MessageService      messageService;

    private String              MO_MessageId;
    private String              ShortCode;
    private String              MO_ShortKey;
    private String              Message;
    private String              SendTo;
    private String              operator;

    private String              status;
    private String              errorMsg;

    @Override
    public void validate() {
        if (!StringUtils.hasText(SendTo)) {
            errorMsg = "SendTo must be set";
        } else if (!StringUtils.hasText(ShortCode)) {
            errorMsg = "ShortCode must be set";
        } else if (!StringUtils.hasText(MO_MessageId)) {
            errorMsg = "MO_MessageId must be set";
        } else if (Message == null) {
            errorMsg = "Message must not be null";
        }

        if (errorMsg != null) {
            LOG.debug("Validation failed with error --> {}", errorMsg);
            addActionError(errorMsg);
            status = "failed";
        }
    }

    @Override
    public String execute() throws Exception {
        LOG.debug("MO_MessageId: " + MO_MessageId + "; ShortCode: " + ShortCode + "; MO_ShortKey: " + MO_ShortKey + "; Message: " + Message + "; SendTo: "
                + SendTo + "; operator: " + operator);

        try {
            messageService.receiveSms(operator, ShortCode, MO_ShortKey, SendTo, Message, new Date(), MO_MessageId);
        } catch (final LanguageNotFound e) {
            LOG.error(e.getMessage());
            return INPUT;
        }

        return SUCCESS;
    }

    // Getters & setters

    public String getMO_MessageId() {
        return MO_MessageId;
    }

    public void setMO_MessageId(final String mOMessageId) {
        MO_MessageId = mOMessageId;
    }

    public String getShortCode() {
        return ShortCode;
    }

    public void setShortCode(final String shortCode) {
        ShortCode = shortCode;
    }

    public String getMO_ShortKey() {
        return MO_ShortKey;
    }

    public void setMO_ShortKey(final String mOShortKey) {
        MO_ShortKey = mOShortKey;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(final String message) {
        Message = message;
    }

    public String getSendTo() {
        return SendTo;
    }

    public void setSendTo(final String sendTo) {
        SendTo = sendTo;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(final String operator) {
        this.operator = operator;
    }

    public final String getStatus() {
        return status;
    }

    public final void setStatus(final String p_status) {
        status = p_status;
    }

    public final String getErrorMsg() {
        return errorMsg;
    }

    public final void setErrorMsg(final String p_errorMsg) {
        errorMsg = p_errorMsg;
    }

}
