package hr.chus.cchat.gateway;

import hr.chus.cchat.model.db.jpa.SMSMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Implementation for sending messages using CGateway.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@Service("defaultSendMessageService")
public class CGatewaySendMessageService implements SendMessageService {

    private static final Logger LOG = LoggerFactory.getLogger(CGatewaySendMessageService.class);

    @Value("${gateway.username}")
    private String              username;

    @Value("${gateway.password}")
    private String              password;

    @Value("${gateway.sendSmsUrl}")
    private String              sendSmsUrl;

    @Value("${gateway.sendWapPushUrl}")
    private String              sendWapPushUrl;

    @Value("${gateway.testSending}")
    private boolean             testSending;

    /**
     * @param smsMessage
     * @return
     * @throws HttpException
     * @throws IOException
     * @throws GatewayResponseError
     */
    @Override
    public String sendSmsMessage(SMSMessage smsMessage) throws HttpException, IOException, GatewayResponseError {
        if (testSending) return "testSmsResponseId";

        final HttpPost post = new HttpPost(sendSmsUrl);
        final StringBuffer response = new StringBuffer();
        try {
            final DefaultHttpClient client = new DefaultHttpClient();
            client.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(5, false));

            final List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("sender", smsMessage.getSc()));
            params.add(new BasicNameValuePair("receiver", smsMessage.getUser().getMsisdn()));
            params.add(new BasicNameValuePair("content", smsMessage.getText()));

            final UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            post.setEntity(entity);

            final HttpResponse httpReponse = client.execute(post);
            final int returnCode = httpReponse.getStatusLine().getStatusCode();
            if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
                LOG.error("The Post method is not implemented by URI: " + sendSmsUrl);
            } else {
                if (httpReponse.getEntity() != null) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpReponse.getEntity().getContent()));
                    String readLine = null;
                    while ((readLine = br.readLine()) != null) {
                        response.append(readLine);
                    }
                }
            }

            if (returnCode == HttpStatus.SC_OK) {
                // TODO: For now gateway id is not returned.
                return null;
            } else {
                throw new GatewayResponseError(response.toString(), String.valueOf(returnCode));
            }
        } finally {
            post.releaseConnection();
        }
    }

    @Override
    public String sendWapPushMessage(SMSMessage smsMessage) {
        if (testSending) return "testWapPushResponseId";
        throw new UnsupportedOperationException("Sending wap push is not supported yet");
    }

    // Getters & setters

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSendSmsUrl(String sendSmsUrl) {
        this.sendSmsUrl = sendSmsUrl;
    }

    public void setSendWapPushUrl(String sendWapPushUrl) {
        this.sendWapPushUrl = sendWapPushUrl;
    }

    public void setTestSending(boolean testSending) {
        this.testSending = testSending;
    }

}
