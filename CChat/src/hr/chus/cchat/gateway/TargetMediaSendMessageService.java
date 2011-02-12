package hr.chus.cchat.gateway;

import hr.chus.cchat.db.service.SMSMessageService;
import hr.chus.cchat.model.db.jpa.SMSMessage;
import hr.chus.cchat.model.db.jpa.ServiceProviderKeyword;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.EncodingUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class TargetMediaSendMessageService implements SendMessageService {
	
	private Log log = LogFactory.getLog(getClass());
	
	private SMSMessageService smsMessageService;
	
	private Map<String, String> statusCodeMap;
	private String username;
	private String handle;
	private String url;
	private String returnid = "yes";
	
	
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
		SMSMessage lastReceivedMessage = smsMessageService.getLastReceivedMessage(smsMessage.getUser());
		ServiceProviderKeyword keyword = lastReceivedMessage.getServiceProviderKeyword();
		NameValuePair[] data = new NameValuePair[] {
				new NameValuePair("username", username)
				, new NameValuePair("handle", handle)
				, new NameValuePair("shortkey", keyword == null ? null : keyword.getKeyword())
				, new NameValuePair("shortcode", smsMessage.getSc())
				, new NameValuePair("sendto", smsMessage.getUser().getMsisdn())
				, new NameValuePair("mo_messageid", lastReceivedMessage.getGatewayId())
				, new NameValuePair("message", smsMessage.getText())
				/* Amount to be billed (Numeric). The rate that will be charged to the end-user for receiving the 
				 * message. The rate is in cents, local currency and must be equal to or lower than the rate you 
				 * set when registering the keyword.
				 * Note 1: In some countries (like Belgium) the end-user rate is fixed per shortcode, the value 
				 * for 'tariff' will be ignored.
				 * Note 2: Service- or error messages in the Netherlands may not be charged for more than 25 eurocents.
				 */
				, new NameValuePair("tariff", keyword.getBillingAmount() == null ? null : String.valueOf(keyword.getBillingAmount().intValue()))
				, new NameValuePair("returnid", returnid)
		};
		return sendRequest(data);
	}

	@Override	
	public String sendWapPushMessage(SMSMessage smsMessage) throws HttpException, IOException, GatewayResponseError {
		SMSMessage lastReceivedMessage = smsMessageService.getLastReceivedMessage(smsMessage.getUser());
		ServiceProviderKeyword keyword = lastReceivedMessage.getServiceProviderKeyword();
		NameValuePair[] data = new NameValuePair[] {
				new NameValuePair("username", username)
				, new NameValuePair("handle", handle)
				, new NameValuePair("shortkey", keyword == null ? null : keyword.getKeyword())
				, new NameValuePair("shortcode", smsMessage.getSc())
				, new NameValuePair("sendto", smsMessage.getUser().getMsisdn())
				, new NameValuePair("mo_messageid", lastReceivedMessage.getGatewayId())
				, new NameValuePair("message", "URL")
				, new NameValuePair("tariff", keyword.getBillingAmount() == null ? null : String.valueOf(keyword.getBillingAmount().intValue()))
				/* WAP push message ('0', '1').
				 * A WAP push is a bookmark for a mobile phone. Enter '1' for this parameter when a WAP push SMS message 
				 * has to be sent. In AU, UK and IE the end-user rate must be zero
				 */
				, new NameValuePair("push", "1")
				/* WAP push URL. Set the URL of the WAP push message here (only applicable when push=1)
				 */
				, new NameValuePair("purl", smsMessage.getText())
				, new NameValuePair("returnid", returnid)
		};
		return sendRequest(data);
	}
	
	/**
	 * 
	 * @param data
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 * @throws GatewayResponseError
	 */
	public String sendRequest(NameValuePair[] data) throws HttpException, IOException, GatewayResponseError {
		PostMethod post = new PostMethod(url);
		StringBuffer response = new StringBuffer();
		try {
			HttpClient client = new HttpClient();
			client.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(5, true));
			post.setRequestBody(data);
			if (log.isDebugEnabled()) {
				log.debug("Invoking TargetMedia gateway send URL (POST): " + post.getURI() + " (Params: " +  EncodingUtil.formUrlEncode(data, "UTF-8") + ")");
			}
			
			int returnCode = client.executeMethod(post);
			BufferedReader br = new BufferedReader(new InputStreamReader(post.getResponseBodyAsStream()));
	        String readLine = null;
	        while ((readLine = br.readLine()) != null) {
	        	response.append(readLine);
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
				log.warn("StatusCode: " + returnCode + "; Response text: " + response.toString());
				throw new HttpException("Gateway error code " + returnCode);
			}
		} finally {
			post.releaseConnection();
		}
	}

	
	// Getters & setters
	
	public void setSmsMessageService(SMSMessageService smsMessageService) { this.smsMessageService = smsMessageService; }
	
	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }

	public String getHandle() { return handle; }
	public void setHandle(String handle) { this.handle = handle; }
	
	public String getUrl() { return url; }
	public void setUrl(String url) { this.url = url; }

	public String getReturnid() { return returnid; }
	public void setReturnid(String returnid) { this.returnid = returnid; }
	
}
