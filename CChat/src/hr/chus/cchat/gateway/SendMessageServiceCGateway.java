package hr.chus.cchat.gateway;

import hr.chus.cchat.model.db.jpa.SMSMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class SendMessageServiceCGateway implements SendMessageService {
	
	private Log log = LogFactory.getLog(getClass());
	
	private String username;
	private String password;
	private String sendSmsUrl;
	private String sendWapPushUrl;
	
	
	/**
	 * 
	 * @param smsMessage
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	@Override
	public String sendSmsMessage(SMSMessage smsMessage) throws HttpException, IOException {
		PostMethod post = new PostMethod(sendSmsUrl);
		StringBuffer sb = new StringBuffer();
		try {
			HttpClient client = new HttpClient();
			client.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(5, true));
			NameValuePair[] data = new NameValuePair[] {
						new NameValuePair("username", username),
						new NameValuePair("password", password),
						new NameValuePair("sender", smsMessage.getSc()),
						new NameValuePair("receiver", smsMessage.getUser().getMsisdn()),
						new NameValuePair("content", smsMessage.getText())
			};
			post.setRequestBody(data);
			int returnCode = client.executeMethod(post);
			if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
				log.error("The Post method is not implemented by URI: "+ sendSmsUrl);
			} else {
				BufferedReader br = new BufferedReader(new InputStreamReader(post.getResponseBodyAsStream()));
		        String readLine = null;
		        while ((readLine = br.readLine()) != null) {
		        	sb.append(readLine);
		        }
			}
			
			if (returnCode == HttpStatus.SC_OK) {
				return sb.toString();
			} else {
				log.warn("StatusCode: " + returnCode + "; Response text: " + sb.toString());
				throw new HttpException("Gateway error code " + returnCode);
			}
		} finally {
			post.releaseConnection();
		}
	}
	
	@Override
	public String sendWapPushMessage(SMSMessage smsMessage) {
		// TODO: Implement
		throw new NotImplementedException("Sending wap push is not supported yet");
	}


	// Getters & setters
	
	public void setUsername(String username) { this.username = username; }

	public void setPassword(String password) { this.password = password; }

	public void setSendSmsUrl(String sendSmsUrl) { this.sendSmsUrl = sendSmsUrl; }

	public void setSendWapPushUrl(String sendWapPushUrl) { this.sendWapPushUrl = sendWapPushUrl; }

}
