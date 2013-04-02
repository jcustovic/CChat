package hr.chus.cchat.struts2.action.common;

import hr.chus.cchat.db.service.SMSMessageService;
import hr.chus.cchat.model.db.jpa.SMSMessage.DeliveryStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Target Media status handler service.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
public class TMStatusHandler extends ActionSupport {

    private static final Logger         LOG = LoggerFactory.getLogger(TMStatusHandler.class);

    @Autowired
    private transient SMSMessageService smsMessageService;

    private String                      id;
    private String                      reason;
    private String                      shortcode;

    @Override
    public String execute() throws Exception {
        LOG.debug("StatusHandler --> id: " + id + ", reason: " + reason + ", shortcode: " + shortcode);

        smsMessageService.updateStatus(id, DeliveryStatus.DELIVERY_FAILED, reason + " (SC: " + shortcode + ")");

        return SUCCESS;
    }

    // Getters & setters

    public void setId(String id) {
        this.id = id;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setShortcode(String shortcode) {
        this.shortcode = shortcode;
    }

}
