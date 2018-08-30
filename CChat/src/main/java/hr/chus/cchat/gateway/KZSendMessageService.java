package hr.chus.cchat.gateway;

import hr.chus.cchat.model.db.jpa.SMSMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

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
 * http://127.0.0.1/Staff_User/kannel_dlr.php
 * 
 * @author Aleksander Buszka (aleksander.buszka@fxd.pl)
 *
 */
@Service
public class KZSendMessageService implements SendMessageService, BeanNameAware {
	
	protected static final Logger logger = LoggerFactory.getLogger(KZSendMessageService.class);
	
    @Value("${gateway.kz.username}")
    private String username;
    
    @Value("${gateway.kz.password}")
    private String password;
    
    @Value("${gateway.kz.sendSmsUrl}")
    private String sendSmsUrl;
    
    private String smsc;
    
    private HashMap<String, String> supidMap;
    
    private HashMap<String, String> smscReplacementMap;
    
    private Date dayMessageCounterLastSmsDate;
    
    private int dayMessageCounterLastValue;
    
    public KZSendMessageService() {
    	super();
    	
    	this.supidMap = new HashMap<String, String>();
    	
    	this.supidMap.put("42Telecom",				"552");
    	this.supidMap.put("ACL_Hin",				"559");
    	this.supidMap.put("ACL_Wan",				"558");
    	this.supidMap.put("Aicent_A2P",				"509");
    	this.supidMap.put("Aicent_P2P",				"520");
    	this.supidMap.put("AMD",					"514");
    	this.supidMap.put("BCL_HQ",					"502");
     	this.supidMap.put("Bebbicell",				"498");
     	this.supidMap.put("Bharti_A2P",				"526");
     	this.supidMap.put("BondSMS",				"529");
     	this.supidMap.put("Broadcastsys",			"554");
     	this.supidMap.put("Cellent",				"495");
     	this.supidMap.put("Cellent_2",				"497");
     	this.supidMap.put("Cequens",				"560");
     	this.supidMap.put("Channel",				"561");
     	this.supidMap.put("Citessentials_FULL",		"546");
     	this.supidMap.put("Citessentials1WAY",		"545");
     	this.supidMap.put("Clickatell",				"516");
     	this.supidMap.put("CLX_Networks_01",		"487");
     	this.supidMap.put("CLX_Networks_02",		"488");
     	this.supidMap.put("CMTelecom",				"500");
     	this.supidMap.put("CS_NETWORKS_01",			"548");
     	this.supidMap.put("CS_NETWORKS_02",			"549");
     	this.supidMap.put("Fortytwo",				"530");
     	this.supidMap.put("Fortytwo_S42",			"557");
     	this.supidMap.put("Fortytwo_WAN",			"539");
     	this.supidMap.put("Horisen",				"534");
     	this.supidMap.put("Ibasis_01",				"493");
     	this.supidMap.put("Ibasis_02",				"492");
     	this.supidMap.put("InfoBip",				"485");
     	this.supidMap.put("InfoBip_2IP",			"486");
     	this.supidMap.put("Infobip_Flash",			"524");
     	this.supidMap.put("Infobip_Special",		"522");
     	this.supidMap.put("InfoBip3_subacc",		"491");
     	this.supidMap.put("LevelSMS",				"528");
     	this.supidMap.put("Medallion",				"533");
     	this.supidMap.put("MobiWeb",				"563");
     	this.supidMap.put("NEXMO",					"310");
     	this.supidMap.put("Ngolela",				"555");
     	this.supidMap.put("PomTelecom",				"496");
     	this.supidMap.put("Procescom",				"537");
     	this.supidMap.put("Procescom_6785330067",	"550");
     	this.supidMap.put("Procescom_6851100049",	"551");
     	this.supidMap.put("Procescom_altGT",		"562");
     	this.supidMap.put("Procesom_4174279",		"543");
     	this.supidMap.put("Procesom_4179977",		"540");
     	this.supidMap.put("Procesom_4179978",		"541");
     	this.supidMap.put("Procesom_4179979",		"542");
     	this.supidMap.put("Procesom_6797000071",	"544");
     	this.supidMap.put("ReachData",				"556");
     	this.supidMap.put("RouteSMS",				"511");
     	this.supidMap.put("routesms.com",			"6");
     	this.supidMap.put("RouteSMS_flat",			"513");
     	this.supidMap.put("RouteSMSnew",			"535");
     	this.supidMap.put("Silverstreet_Eric",		"8");
     	this.supidMap.put("Silverstreet_HQ",		"547");
     	this.supidMap.put("SITmobile",				"489");
     	this.supidMap.put("SMS_Global",				"510");
     	this.supidMap.put("SMS_TRADE",				"512");
     	this.supidMap.put("SMShighway",				"553");
     	this.supidMap.put("smsRelay01",				"532");
     	this.supidMap.put("SMSrelay01",				"525");
     	this.supidMap.put("StourMarine",			"538");
     	this.supidMap.put("Sueno",					"521");
     	this.supidMap.put("Tyntec",					"7");
     	this.supidMap.put("Vertex",					"515");
     	this.supidMap.put("Virgo_Anas",				"108");
     	this.supidMap.put("Virgo_Anas_02",			"494");
     	this.supidMap.put("W2WTS",					"517");
     	this.supidMap.put("WhatsApp",				"531");
     	this.supidMap.put("ZEN",					"523");
     	this.supidMap.put("Condor",					"595");
		this.supidMap.put("KZCchat",				"659");
		
     	this.smscReplacementMap = new HashMap<String, String>();
    	
    	this.smscReplacementMap.put("42Telecom", 			"N_42Telecom");
    	this.smscReplacementMap.put("Aicent_A2P", 			"N_Aicent_A2P");
    	this.smscReplacementMap.put("AMD", 					"N_AMD");
    	this.smscReplacementMap.put("BCL_HQ", 				"N_BCL_HQ");
    	this.smscReplacementMap.put("Procesom_4174279", 	"N_BebbiCell_42");
    	this.smscReplacementMap.put("Procesom_4179977", 	"N_BebbiCell_77");
    	this.smscReplacementMap.put("Procesom_4179978", 	"N_BebbiCell_78");
    	this.smscReplacementMap.put("Procesom_4179979", 	"N_BebbiCell_79");
    	this.smscReplacementMap.put("BondSMS", 				"N_Broadcastsys");
    	this.smscReplacementMap.put("Cellent_2", 			"N_Cellent_2");
    	this.smscReplacementMap.put("Cequens", 				"N_Cequens");
    	this.smscReplacementMap.put("Channel", 				"N_Channel");
    	this.smscReplacementMap.put("CMTelecom", 			"N_CMTelecom");
    	this.smscReplacementMap.put("CS_NETWORKS_01", 		"N_CS_NETWORKS_01");
    	this.smscReplacementMap.put("Procesom_6797000071",	"N_Digicel_Fiji");
    	this.smscReplacementMap.put("Procescom", 			"N_Digicel_PNG");
    	this.smscReplacementMap.put("Procescom_6851100049", "N_Digicel_Samoa");
    	this.smscReplacementMap.put("Procescom_6785330067", "N_Digicel_Vanuatu");
    	this.smscReplacementMap.put("Ibasis_02", 			"N_iBasis_02");
    	this.smscReplacementMap.put("InfoBip", 				"N_InfoBip");
    	this.smscReplacementMap.put("Silverstreet Eric", 	"N_Silverstreet_Eric");
    	this.smscReplacementMap.put("Silverstreet_Eric", 	"N_Silverstreet_Eric");
    	this.smscReplacementMap.put("Silverstreet_HQ", 		"N_Silverstreet_HQ");
    	this.smscReplacementMap.put("Tyntec", 				"N_Tyntec");
    	this.smscReplacementMap.put("W2WTS", 				"N_W2WTS");
    	this.smscReplacementMap.put("ZEN", 					"N_ZEN");
		this.smscReplacementMap.put("Condor", 				"N_Condor");
    }
    
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
			StringBuilder dlrUrl = new StringBuilder();
			
			dlrUrl.append("http://127.0.0.1/Staff_User/kannel_dlr.php?version=cchat");
			dlrUrl.append("&myId=").append("" + (long)(System.currentTimeMillis() / 1000L));
			dlrUrl.append("&idx=").append("" + this.getDayMessageCounter());
			dlrUrl.append("&nbrchar=").append("");
			dlrUrl.append("&coding=").append("2");
			dlrUrl.append("&charset=").append("UTF16-BE");
			dlrUrl.append("&sup=").append(this.getSupid());
			dlrUrl.append("&type=%d&reply=%A&ts=%t&message=%a&from=%p&to=%P");
			
			logger.info("DLR-URL: " + dlrUrl.toString());
			
			HttpClient client = new HttpClient();
			client.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(5, true));
			client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			NameValuePair[] data = new NameValuePair[] {
						new NameValuePair("username", this.username),
						new NameValuePair("password", this.password),
						new NameValuePair("from", "+" + smsMessage.getSc()),
						new NameValuePair("to", "+" + smsMessage.getUser().getMsisdn()),
						new NameValuePair("smsc", this.getSmsc()),
						new NameValuePair("coding", "2"),
						new NameValuePair("charset", "UTF-8"),
						new NameValuePair("text", smsMessage.getText()),
						//new NameValuePair("text", URLEncoder.encode(smsMessage.getText(),"UTF-8"))
						new NameValuePair("dlr-mask", "31"),
						new NameValuePair("dlr-url", dlrUrl.toString()),
			};
			get.setQueryString(data);
			
			
			
			try { // TODO
				logger.info("QueryString: " + get.getQueryString());
			} catch (Exception e) {}
			
			
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
	
	public String getSmsc(String beanId) {
		if (beanId != null) {
			String subBeanId = null;
			
			if(beanId.length() > 10) {
				subBeanId = beanId.substring(0, 10);
				
				if ("kannel_KZ_".equals(subBeanId)) {
					return beanId.substring(10, beanId.length());
				}
			}
		}
		
		return null;
	}
	
	@Override
	public void setBeanName(String name) {
		logger.info("Bean name: {}", name);
		
		if (name != null) {
			String subBeanId = null;
			
			if(name.length() > 10) {
				subBeanId = name.substring(0, 10);
				
				if ("kannel_KZ_".equals(subBeanId)) {
					this.smsc = name.substring(10, name.length());
					
					if (this.smsc.toLowerCase().contains("Procescom_".toLowerCase())) {
						this.smsc = this.smsc.replaceAll("Procescom_", "Procesom_");
					}
					logger.info("SMSC = {}", this.smsc);
				}
			}
		} else {
			logger.info("SMSC = null");
		}
	}
	
	private int getDayMessageCounter() {
		if(this.dayMessageCounterLastSmsDate == null) {
			this.dayMessageCounterLastSmsDate = new Date();
			this.dayMessageCounterLastValue = 0;
		}
		
		Date now = new Date();

		Calendar counterCalendar = Calendar.getInstance();
		Calendar nowCalendar = Calendar.getInstance();
		
		counterCalendar.setTime(this.dayMessageCounterLastSmsDate);
		nowCalendar.setTime(now);
		
		boolean sameDay = counterCalendar.get(Calendar.YEAR) == nowCalendar.get(Calendar.YEAR) && counterCalendar.get(Calendar.DAY_OF_YEAR) == nowCalendar.get(Calendar.DAY_OF_YEAR);
		
		if (!sameDay) {
			this.dayMessageCounterLastValue = 0;
		}
		
		this.dayMessageCounterLastSmsDate = now;
		this.dayMessageCounterLastValue++;
		
		return this.dayMessageCounterLastValue;
	}
	
	private String getSupid() {
		if (this.supidMap.containsKey(this.smsc)) {
			return this.supidMap.get(this.smsc);
		}
		
		logger.error("SUPID for " + this.smsc + " not found!");
		
		return "";
	}
	
	private String getSmsc() {
		String newSmsc = this.smsc;
		
		if (this.smscReplacementMap.containsKey(this.smsc)) {
			newSmsc = this.smscReplacementMap.get(this.smsc);
			logger.info("Replacing SMSC: " + this.smsc + " => " + newSmsc);
		}
		
		return newSmsc;
	}
}
