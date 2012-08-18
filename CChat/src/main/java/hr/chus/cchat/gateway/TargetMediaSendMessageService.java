package hr.chus.cchat.gateway;

import hr.chus.cchat.db.service.SMSMessageService;
import hr.chus.cchat.model.db.jpa.SMSMessage;
import hr.chus.cchat.model.db.jpa.ServiceProviderKeyword;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.PostConstruct;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public class TargetMediaSendMessageService implements SendMessageService {

    private static final Logger LOG      = LoggerFactory.getLogger(TargetMediaSendMessageService.class);

    @Autowired
    private SMSMessageService   smsMessageService;

    private Map<String, String> statusCodeMap;
    
    @Value("${targetMedia.gateway.username}")
    private String              username;
    
    @Value("${targetMedia.gateway.handle}")
    private String              handle;
    
    @Value("${targetMedia.gateway.url}")
    private String              url;
    
    private String              returnid = "yes";

    @PostConstruct
    public void init() {
        statusCodeMap = new HashMap<String, String>();
        statusCodeMap.put("45000", "OK");
        statusCodeMap.put("45001", "Incorrect phone number");
        statusCodeMap.put("45002", "Incorrect message");
        statusCodeMap.put("45003", "Incorrect shortcode");
        statusCodeMap.put("45004", "Incorrect mo_messageid");
        statusCodeMap.put("45005", "MO record not found for this mo_messageid or maximum number of messages exceeded");
        statusCodeMap.put("45006", "Invalid end-user rate (tariff)");
        statusCodeMap.put("45007", "Username doesn't match keyword owner");
        statusCodeMap.put("45013", "End-user rate (tariff) was higher than the registered price");
        statusCodeMap.put("45019", "Unknown member");
        statusCodeMap.put("45020", "Maximum number of messages exceeded");
        statusCodeMap.put("45022", "Incorrect URL");
        statusCodeMap.put("45023", "WAP description too long");
        statusCodeMap.put("45024", "URL too long");
        statusCodeMap.put("45025", "End-user rate must be zero (000) when sending a WAP Push message in this country");
        statusCodeMap.put("45030", "No handle specified");
        statusCodeMap.put("45031", "Incorrect handle");
    }

    @Override
    public String sendSmsMessage(SMSMessage smsMessage) throws HttpException, IOException, GatewayResponseError {
        final SMSMessage lastReceivedMessage = smsMessageService.getLastReceivedMessage(smsMessage.getUser());
        final ServiceProviderKeyword keyword = lastReceivedMessage.getServiceProviderKeyword();
        smsMessage.setServiceProviderKeyword(keyword);

        final List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("handle", handle));
        params.add(new BasicNameValuePair("shortkey", keyword == null ? null : keyword.getKeyword()));
        params.add(new BasicNameValuePair("shortcode", smsMessage.getSc()));
        params.add(new BasicNameValuePair("sendto", smsMessage.getUser().getMsisdn()));
        params.add(new BasicNameValuePair("mo_messageid", lastReceivedMessage.getGatewayId()));
        params.add(new BasicNameValuePair("message", smsMessage.getText()));
        /* Amount to be billed (Numeric). The rate that will be charged to the end-user for receiving the 
         * message. The rate is in cents, local currency and must be equal to or lower than the rate you 
         * set when registering the keyword.
         * Note 1: In some countries (like Belgium) the end-user rate is fixed per shortcode, the value 
         * for 'tariff' will be ignored.
         * Note 2: Service- or error messages in the Netherlands may not be charged for more than 25 eurocents.
         */
        params.add(new BasicNameValuePair("tariff", (keyword == null || keyword.getBillingAmount() == null) ? null : String.valueOf(keyword.getBillingAmount()
                .intValue())));
        params.add(new BasicNameValuePair("returnid", returnid));

        return sendRequest(params);
    }

    @Override
    public String sendWapPushMessage(SMSMessage smsMessage) throws HttpException, IOException, GatewayResponseError {
        final SMSMessage lastReceivedMessage = smsMessageService.getLastReceivedMessage(smsMessage.getUser());
        final ServiceProviderKeyword keyword = lastReceivedMessage.getServiceProviderKeyword();
        smsMessage.setServiceProviderKeyword(keyword);

        final List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("handle", handle));
        params.add(new BasicNameValuePair("shortkey", keyword == null ? null : keyword.getKeyword()));
        params.add(new BasicNameValuePair("shortcode", smsMessage.getSc()));
        params.add(new BasicNameValuePair("sendto", smsMessage.getUser().getMsisdn()));
        params.add(new BasicNameValuePair("mo_messageid", lastReceivedMessage.getGatewayId()));
        params.add(new BasicNameValuePair("message", "URL"));
        params.add(new BasicNameValuePair("tariff", (keyword == null || keyword.getBillingAmount() == null) ? null : String.valueOf(keyword.getBillingAmount()
                .intValue())));
        /* WAP push message ('0', '1').
         * A WAP push is a bookmark for a mobile phone. Enter '1' for this parameter when a WAP push SMS message 
         * has to be sent. In AU, UK and IE the end-user rate must be zero
         */
        params.add(new BasicNameValuePair("push", "1"));
        /* WAP push URL. Set the URL of the WAP push message here (only applicable when push=1)
         */
        params.add(new BasicNameValuePair("purl", smsMessage.getText()));
        params.add(new BasicNameValuePair("returnid", returnid));

        return sendRequest(params);
    }

    /**
     * @param data
     * @return
     * @throws HttpException
     * @throws IOException
     * @throws GatewayResponseError
     */
    public String sendRequest(final List<NameValuePair> params) throws HttpException, IOException, GatewayResponseError {
        final HttpPost post = new HttpPost(url);
        final StringBuffer response = new StringBuffer();

        try {
            final DefaultHttpClient client = new DefaultHttpClient();
            client.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(5, false));

            final UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            post.setEntity(entity);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Invoking TargetMedia gateway send URL (POST): " + post.getURI() + " (Params: " + URLEncodedUtils.format(params, "UTF-8") + ")");
            }

            final HttpResponse httpReponse = client.execute(post);
            final int returnCode = httpReponse.getStatusLine().getStatusCode();

            if (httpReponse.getEntity() != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(httpReponse.getEntity().getContent()));
                String readLine = null;
                while ((readLine = br.readLine()) != null) {
                    response.append(readLine);
                }
            }
            if (returnCode == HttpStatus.SC_OK) {
                StringTokenizer token = new StringTokenizer(response.toString(), " ");
                String statusCode = token.nextToken();
                if (statusCode.equals("45000")) {
                    return token.nextToken();
                } else {
                    throw new GatewayResponseError(statusCodeMap.get(statusCode), statusCode);
                }
            } else {
                LOG.warn("StatusCode: " + returnCode + "; Response text: " + response.toString());
                throw new HttpException("Gateway error code " + returnCode);
            }
        } finally {
            post.releaseConnection();
        }
    }

    // Getters & setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getReturnid() {
        return returnid;
    }

    public void setReturnid(String returnid) {
        this.returnid = returnid;
    }

}
