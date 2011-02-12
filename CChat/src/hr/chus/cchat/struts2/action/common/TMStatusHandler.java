package hr.chus.cchat.struts2.action.common;

import hr.chus.cchat.db.service.SMSMessageService;
import hr.chus.cchat.model.db.jpa.SMSMessage;
import hr.chus.cchat.model.db.jpa.SMSMessage.DeliveryStatus;
import hr.chus.cchat.model.db.jpa.SMSMessage.Direction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Target Media status handler service.
 *  
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class TMStatusHandler extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	
	private Log log = LogFactory.getLog(getClass());
	
	private SMSMessageService smsMessageService;
	
	private String id;
	private String reason;
	private String shortcode;
	
	
	@Override
	public String execute() throws Exception {
		log.debug("StatusHandler --> id: " + id + ", reason: " + reason + ", shortcode: " + shortcode);
		try {
			SMSMessage sms = smsMessageService.getByGatewayId(id);
			if (sms == null) {
				log.warn("Message with gateway id " + id + " not found.");
			} else if (sms.getDirection() == Direction.OUT) {
				sms.setDeliveryStatus(DeliveryStatus.DELIVERY_FAILED);
				sms.setDeliveryMessage(reason + " (SC: " + shortcode + ")");
				smsMessageService.updateSMSMessage(sms);
			} else {
				log.debug("Message with gateway id " + id + " is incoming msg and status will not be updated.");
			}
		} catch (Exception e) {
			log.error(e);
		}
		return SUCCESS;
	}

	
	// Getters & setters
	
	public void setSmsMessageService(SMSMessageService smsMessageService) {	this.smsMessageService = smsMessageService; }	

	public void setId(String id) { this.id = id; }

	public void setReason(String reason) { this.reason = reason; }

	public void setShortcode(String shortcode) { this.shortcode = shortcode; }
	
}
