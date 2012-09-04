package hr.chus.cchat.gateway;

/**
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
public class GatewayResponseError extends Exception {

    private String code;

    public GatewayResponseError(String message, String code) {
        super(message);
        this.code = code;
    }

    // Getters & setters

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
