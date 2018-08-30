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
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Implementation for sending messages using ambasms.com.
 * 
 * http://www.ambasms.com:13013/cgi-bin/sendsms?username={}&password={}&from={}&to={}&smsc={}&coding={}&charset={}&text={}
 * 
 * @author Aleksander Buszka (aleksander.buszka@fxd.pl)
 *
 */
@Service
public class AmbasmsSendMessageService implements SendMessageService, BeanNameAware {
	
	protected static final Logger logger = LoggerFactory.getLogger(AmbasmsSendMessageService.class);
	
    @Value("${gateway.ambasms.username}")
    private String username;
    
    @Value("${gateway.ambasms.password}")
    private String password;
    
    @Value("${gateway.ambasms.sendSmsUrl}")
    private String sendSmsUrl;
    
    private String smsc;
    
	/**
	 * 
	 * @param smsMessage
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 * @throws GatewayResponseError 
	 */
	@Override
	public String sendSmsMessage(SMSMessage smsMessage) throws HttpException, IOException, GatewayResponseError {
		
		//String smsc = this.getSmsc(smsMessage.getServiceProvider().getSendServiceBeanName());
		
		if(this.smsc == null) {
			logger.error("SMSC not set!");
			throw new IOException("SMSC not set!");
		}
		
		logger.info("Sending from: " + smsMessage.getSc() + " to: " + smsMessage.getUser().getMsisdn() + " text: " + smsMessage.getText());
		logger.info("Using SMSC: " + this.smsc);
		
		GetMethod get = new GetMethod();
		get.setURI(new URI(this.sendSmsUrl, false, "UTF-8"));
		
		StringBuffer response = new StringBuffer();
		try {
			HttpClient client = new HttpClient();
			client.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(5, true));
			client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			NameValuePair[] data = new NameValuePair[] {
						new NameValuePair("username", this.username),
						new NameValuePair("password", this.password),
						new NameValuePair("from", "+" + smsMessage.getSc()),
						new NameValuePair("to", "+" + smsMessage.getUser().getMsisdn()),
						new NameValuePair("smsc", this.smsc),
						new NameValuePair("coding", "2"),
						new NameValuePair("charset", "UTF-8"),
						new NameValuePair("text", smsMessage.getText())
						//new NameValuePair("text", URLEncoder.encode(smsMessage.getText(),"UTF-8"))
			};
			get.setQueryString(data);
			int returnCode = client.executeMethod(get);
			if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
				logger.error("The Get method is not implemented by URI: "+ sendSmsUrl);
			} else {
				BufferedReader br = new BufferedReader(new InputStreamReader(get.getResponseBodyAsStream()));
		        String readLine = null;
		        while ((readLine = br.readLine()) != null) {
		        	response.append(readLine);
		        }
			}
			
			if (returnCode == HttpStatus.SC_OK || returnCode == HttpStatus.SC_ACCEPTED) {
				logger.info("HttpStatus : " + returnCode);
				logger.info("Response   : " + response.toString());
				// TODO: For now gateway id is not returned.
				return null;
			} else {
				logger.error("HTTP error code is: " + returnCode);
				throw new GatewayResponseError(response.toString(), String.valueOf(returnCode));
			}
		} finally {
			get.releaseConnection();
		}
	}
	
	@Override
	public String sendWapPushMessage(SMSMessage smsMessage) {
		logger.error("Not implemented!");
		return null;
	}
	
//	public String getSmsc(String beanId) {
//		if (beanId != null) {
//			String subBeanId = null;
//			
//			if (beanId.length() >= 12) {
//				subBeanId = beanId.substring(0, 12);
//				
//				if ("kannel_amba_".equals(subBeanId)) {
//					return beanId.substring(12, beanId.length());
//				}
//			}
//		}
//		
//		return null;
//	}
	
	@Override
	public void setBeanName(String name) {
		logger.info("Bean name: {}", name);
		
		if (name != null) {
			String subBeanId = null;
			
			if (name.length() >= 12) {
				subBeanId = name.substring(0, 12);
				
				if ("kannel_amba_".equals(subBeanId)) {
					this.smsc = name.substring(12, name.length());
					logger.info("SMSC = {}", this.smsc);
				}
			}
		} else {
			logger.info("SMSC = null");
		}
	}
}
