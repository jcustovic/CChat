package hr.chus.cchat.struts2.action.virgopass;

import hr.chus.cchat.db.service.SMSMessageService;
import hr.chus.cchat.model.db.jpa.SMSMessage.DeliveryStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

public class VirgoReceiveNotification extends ActionSupport {

    private static final long           serialVersionUID = 1L;

    private static final Logger         LOG              = LoggerFactory.getLogger(VirgoReceiveNotification.class);

    @Autowired
    private transient SMSMessageService smsMessageService;

    private Integer                     error_code;
    private String                      error_desc;
    private Integer                     mt_id;

    private String                      errorMsg;

    @Override
    public void validate() {
        if (mt_id == null) {
            errorMsg = "mt_id must be set";
        } else if (error_code == null) {
            errorMsg = "error_code must be set";
        }

        if (errorMsg != null) {
            LOG.warn("Virgopass --> Validation failed with error: {}", errorMsg);
            addActionError(errorMsg);
        }
    }

    @Override
    public String execute() throws Exception {
        LOG.debug("VirgoReceiveNotification --> mt_id: {}, error_code: {}, error_desc: {}", new Object[] { mt_id, error_code, error_desc });

        if (error_code == 0) {
            smsMessageService.updateStatus(String.valueOf(mt_id), DeliveryStatus.DELIVERED, null);
        } else {
            smsMessageService.updateStatus(String.valueOf(mt_id), DeliveryStatus.DELIVERY_FAILED, "ErrorCode " + error_code + ", ErrorDesc: " + error_desc);
        }

        return SUCCESS;
    }

    // Getters & setters

    public final void setError_code(final Integer p_error_code) {
        error_code = p_error_code;
    }

    public final void setError_desc(final String p_error_desc) {
        error_desc = p_error_desc;
    }

    public final void setMt_id(final Integer p_mt_id) {
        mt_id = p_mt_id;
    }

    public final String getErrorMsg() {
        return errorMsg;
    }

}
