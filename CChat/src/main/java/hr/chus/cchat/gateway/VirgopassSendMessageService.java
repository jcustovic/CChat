package hr.chus.cchat.gateway;

import hr.chus.cchat.db.service.SMSMessageService;
import hr.chus.cchat.model.db.jpa.SMSMessage;
import hr.chus.cchat.model.db.jpa.ServiceProviderKeyword;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
import org.springframework.stereotype.Service;

/**
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@Service("virgopassSendMessageService")
public class VirgopassSendMessageService implements SendMessageService {

    private static final Logger         LOG = LoggerFactory.getLogger(VirgopassSendMessageService.class);

    @Autowired
    private transient SMSMessageService smsMessageService;

    @Value("${virgopass.gateway.login}")
    private String                      login;

    @Value("${virgopass.gateway.password}")
    private String                      password;

    @Value("${virgopass.gateway.url}")
    private String                      url;

    @Override
    public final String sendSmsMessage(final SMSMessage p_smsMessage) throws HttpException, IOException, GatewayResponseError {
        final SMSMessage lastReceivedMessage = smsMessageService.getLastReceivedMessage(p_smsMessage.getUser());
        final ServiceProviderKeyword keyword = lastReceivedMessage.getServiceProviderKeyword();
        p_smsMessage.setServiceProviderKeyword(keyword);

        final List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("login", login));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("mo_id", lastReceivedMessage.getGatewayId()));
        params.add(new BasicNameValuePair("mt_text", p_smsMessage.getText()));
        params.add(new BasicNameValuePair("free", "0"));

        return sendRequest(params);
    }

    @Override
    public final String sendWapPushMessage(final SMSMessage p_smsMessage) throws HttpException, IOException, GatewayResponseError {
        throw new UnsupportedOperationException("Sending wap push is not supported");
    }

    public String sendRequest(final List<NameValuePair> p_params) throws HttpException, IOException, GatewayResponseError {
        final HttpPost post = new HttpPost(url);
        final StringBuffer response = new StringBuffer();

        try {
            final DefaultHttpClient client = new DefaultHttpClient();
            client.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(5, false));

            final UrlEncodedFormEntity entity = new UrlEncodedFormEntity(p_params, "UTF-8");
            post.setEntity(entity);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Invoking Virgopass gateway send URL (POST): " + post.getURI() + " (Params: " + URLEncodedUtils.format(p_params, "UTF-8") + ")");
            }

            final HttpResponse httpReponse = client.execute(post);
            final int returnCode = httpReponse.getStatusLine().getStatusCode();

            if (httpReponse.getEntity() != null) {
                final BufferedReader br = new BufferedReader(new InputStreamReader(httpReponse.getEntity().getContent()));
                String readLine = null;
                while ((readLine = br.readLine()) != null) {
                    response.append(readLine + "\n");
                }
            }
            if (returnCode == HttpStatus.SC_OK) {
                final Scanner scanner = new Scanner(response.toString());
                final String errorCodeLine = scanner.nextLine();
                final Integer errorCode = Integer.valueOf(getValue(errorCodeLine));
                final String errorDesc = getValue(scanner.nextLine());
                if (errorCode == 0) {
                    return getValue(scanner.nextLine());
                } else {
                    throw new GatewayResponseError(errorDesc, errorCode.toString());
                }
            } else {
                LOG.warn("StatusCode: " + returnCode + "; Response text: " + response.toString());
                throw new HttpException("Gateway error code " + returnCode);
            }
        } finally {
            post.releaseConnection();
        }
    }

    private String getValue(final String p_line) {
        return p_line.split(":")[1].trim();
    }

    // Getters & setters

    public final void setLogin(final String p_login) {
        login = p_login;
    }

    public final void setPassword(final String p_password) {
        password = p_password;
    }

    public final void setUrl(final String p_url) {
        url = p_url;
    }

}
