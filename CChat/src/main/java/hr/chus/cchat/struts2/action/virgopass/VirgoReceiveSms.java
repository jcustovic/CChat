package hr.chus.cchat.struts2.action.virgopass;

import hr.chus.cchat.exception.LanguageNotFound;
import hr.chus.cchat.service.MessageService;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.opensymphony.xwork2.ActionSupport;

public class VirgoReceiveSms extends ActionSupport {

    private static final long        serialVersionUID = 1L;

    private static final Logger      LOG              = LoggerFactory.getLogger(VirgoReceiveSms.class);

    @Autowired
    private transient MessageService messageService;

    private Integer                  mo_id;
    private String                   phone_number;
    private String                   shortcode;
    private Integer                  operator_id;
    private Integer                  mcc_mnc;
    private String                   keyword;
    private String                   message;
    private Float                    end_user_price;
    private String                   end_user_price_currency;
    private String                   country;

    private String                   errorMsg;

    @Override
    public void validate() {
        if (mo_id == null) {
            errorMsg = "mo_id must be set";
        } else if (operator_id == null) {
            errorMsg = "operator_id must be set";
        } else if (!StringUtils.hasText(phone_number)) {
            errorMsg = "phone_number must be set";
        } else if (!StringUtils.hasText(keyword)) {
            errorMsg = "keyword must be set";
        } else if (!StringUtils.hasText(shortcode)) {
            errorMsg = "short_code must be set";
        } else if (message == null) {
            errorMsg = "message must not be null";
        }

        if (errorMsg != null) {
            LOG.warn("Virgopass --> Validation failed with error: {}", errorMsg);
            addActionError(errorMsg);
        }
    }

    @Override
    public String execute() throws Exception {
        LOG.info(
                "Virgopass --> got message mo_id: {}, phone_number: {}, shortcode: {}, operator_id: {}, mcc_mnc: {}, keyword: {}, message: {}, end_user_price: {}, end_user_price_currency: {}, country: {}",
                new Object[] { mo_id, phone_number, shortcode, operator_id, mcc_mnc, keyword, message, end_user_price, end_user_price_currency, country });

        try {
            messageService.receiveSms(String.valueOf(operator_id), shortcode, keyword, phone_number, String.valueOf(mcc_mnc), message, new Date(),
                String.valueOf(mo_id), end_user_price, end_user_price_currency);
        } catch (final LanguageNotFound e) {
            LOG.error(e.getMessage());
        }

        return SUCCESS;
    }

    // Getters & setters

    public final String getErrorMsg() {
        return errorMsg;
    }

    public final void setMo_id(final Integer p_mo_id) {
        mo_id = p_mo_id;
    }

    public final void setPhone_number(final String p_phone_number) {
        phone_number = p_phone_number;
    }

    public final void setShortcode(final String p_shortcode) {
        shortcode = p_shortcode;
    }

    public final void setOperator_id(final Integer p_operator_id) {
        operator_id = p_operator_id;
    }

    public final void setMcc_mnc(final Integer p_mcc_mnc) {
        mcc_mnc = p_mcc_mnc;
    }

    public final void setKeyword(final String p_keyword) {
        keyword = p_keyword;
    }

    public final void setMessage(final String p_message) {
        message = p_message;
    }

    public final void setEnd_user_price(final Float p_end_user_price) {
        end_user_price = p_end_user_price;
    }

    public final void setEnd_user_price_currency(final String p_end_user_price_currency) {
        end_user_price_currency = p_end_user_price_currency;
    }

    public final void setCountry(final String p_country) {
        country = p_country;
    }

}
