package hr.chus.cchat.struts2.action.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that receives SMS messages base upon Target Media SMS Gateway. Only used for catching
 * properties
 * and then request is redirected to ReceiveSms.java.
 * 
 * @see ReceiveSms
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
public class TMReceiveSms extends ActionSupport {

    private static final Logger LOG = LoggerFactory.getLogger(TMReceiveSms.class);

    private String              MO_MessageId;
    private String              ShortCode;
    private String              MO_ShortKey;
    private String              Message;
    private String              SendTo;
    private String              operator;

    @Override
    public String execute() throws Exception {
        LOG.debug("MO_MessageId: " + MO_MessageId + "; ShortCode: " + ShortCode + "; MO_ShortKey: " + MO_ShortKey + "; Message: " + Message + "; SendTo: "
                + SendTo + "; operator: " + operator);
        
        return SUCCESS;
    }

    // Getters & setters

    public String getMO_MessageId() {
        return MO_MessageId;
    }

    public void setMO_MessageId(String mOMessageId) {
        MO_MessageId = mOMessageId;
    }

    public String getShortCode() {
        return ShortCode;
    }

    public void setShortCode(String shortCode) {
        ShortCode = shortCode;
    }

    public String getMO_ShortKey() {
        return MO_ShortKey;
    }

    public void setMO_ShortKey(String mOShortKey) {
        MO_ShortKey = mOShortKey;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getSendTo() {
        return SendTo;
    }

    public void setSendTo(String sendTo) {
        SendTo = sendTo;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

}
