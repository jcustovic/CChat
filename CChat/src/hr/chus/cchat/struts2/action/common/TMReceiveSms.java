package hr.chus.cchat.struts2.action.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that receives SMS messages base upon Target Media SMS Gateway. Only used for catching properties
 * and then request is redirected to ReceiveSms.java.
 * 
 * @see ReceiveSms
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class TMReceiveSms extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	
	private Log log = LogFactory.getLog(getClass());
	
	private String MO_MessageId;
	private String ShortCode;
	private String MO_ShortKey;
	private String Message;
	private String SendTo;
	private String operator;
	
	@Override
	public String execute() throws Exception {
		log.debug("MO_MessageId: " + MO_MessageId + "; ShortCode: " + ShortCode + "; MO_ShortKey: " + MO_ShortKey + "; Message: " + Message + "; SendTo: " + SendTo + "; operator: " + operator);
		return SUCCESS;
	}
	

	// Getters & setters

	public String getMO_MessageId() { return MO_MessageId; }
	public void setMO_MessageId(String mOMessageId) { MO_MessageId = mOMessageId; }

	public String getShortCode() { return ShortCode; }
	public void setShortCode(String shortCode) { ShortCode = shortCode; }

	public String getMO_ShortKey() { return MO_ShortKey; }
	public void setMO_ShortKey(String mOShortKey) { MO_ShortKey = mOShortKey; }

	public String getMessage() { return Message; }
	public void setMessage(String message) { Message = message; }

	public String getSendTo() { return SendTo; }
	public void setSendTo(String sendTo) { SendTo = sendTo; }

	public String getOperator() { return operator; }
	public void setOperator(String operator) { this.operator = operator; }
	
}
