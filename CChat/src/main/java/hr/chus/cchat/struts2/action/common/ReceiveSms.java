package hr.chus.cchat.struts2.action.common;

import hr.chus.cchat.exception.LanguageNotFound;
import hr.chus.cchat.service.MessageService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that receives SMS messages. Gateways should invoke this service in order for
 * this application to receive SMS messages.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
public class ReceiveSms extends ActionSupport {

    private static final Logger     LOG = LoggerFactory.getLogger(ReceiveSms.class);

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");

    @Autowired
    private MessageService          messageService;

    private String                  msisdn;
    private String                  time;
    private String                  text;
    private String                  sc;
    private String                  gatewayId;
    private String                  serviceProviderName;
    private String                  serviceProviderKeyword;

    private Integer[]               messageIds;
    private String                  status;
    private String                  errorMsg;

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
        LOG.info("Got message from {} (ServiceProvider: {}, SC: {}, Keyword: {}) with text --> {}", new Object[] { msisdn, serviceProviderName, sc,
                serviceProviderKeyword, text });
        Date date = new Date();
        if (StringUtils.hasText(time)) {
            try {
                date = sdf.parse(time);
            } catch (ParseException e) {
                LOG.info("Error parsing time " + time);
            }
        }

        try {
            messageIds = messageService.receiveSms(serviceProviderName, sc, serviceProviderKeyword, msisdn, text, date, gatewayId);
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
