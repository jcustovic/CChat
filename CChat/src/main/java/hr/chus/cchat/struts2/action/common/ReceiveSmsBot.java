package hr.chus.cchat.struts2.action.common;

import com.opensymphony.xwork2.ActionSupport;
import hr.chus.cchat.exception.LanguageNotFound;
import hr.chus.cchat.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Date;

/**
 * Web GET or POST action that receives SMS messages and send automatic bot reply.
 *
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public class ReceiveSmsBot extends ActionSupport {

    private static final Logger LOG = LoggerFactory.getLogger(ReceiveSmsBot.class);

    @Autowired
    private MessageService messageService;

    private String msisdn;
    private String text;
    private String sc;
    private String gatewayId;
    private String serviceProviderName;

    private Integer[] messageIds;
    private String status;
    private String errorMsg;

    @Override
    public void validate() {
        if (!StringUtils.hasText(msisdn)) {
            errorMsg = "Msisdn must be set";
        } else if (!StringUtils.hasText(sc)) {
            errorMsg = "SC must be set";
        } else if (!StringUtils.hasText(serviceProviderName)) {
            errorMsg = "ServiceProviderName must be set";
        } else if (text == null) {
            errorMsg = "Text must not be null";
        }

        if (errorMsg != null) {
            LOG.debug("Validation failed with error --> {}", errorMsg);
            addActionError(errorMsg);
            status = "failed";
        }
    }

    @Override
    public String execute() throws Exception {
        LOG.info("Got message from {} (ServiceProvider: {}, SC: {}) with text --> {}", new Object[]{msisdn, serviceProviderName,
                sc, text});
        try {
            messageIds = messageService.receiveSmsAndSendAutomaticBotReply(serviceProviderName, sc, null, msisdn, text,
                    new Date(), gatewayId);
            status = "ok";
            LOG.info("Message(s) saved with ids {}", Arrays.toString(messageIds));
        } catch (final LanguageNotFound e) {
            LOG.error(e.getMessage());
            status = "failed";
        }

        return SUCCESS;
    }

    // Getters & setters

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
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

    public Integer[] getMessageIds() {
        return messageIds;
    }

    public String getStatus() {
        return status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

}
