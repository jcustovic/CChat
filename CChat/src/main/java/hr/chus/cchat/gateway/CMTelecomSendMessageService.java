package hr.chus.cchat.gateway;

import hr.chus.cchat.model.db.jpa.SMSMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Implementation for sending messages using CM Telecom Gateway (www.cmtelecom.com).
 * 
 * @author Aleksander Buszka (aleksander.buszka@fxd.pl)
 *
 */
@Service
public class CMTelecomSendMessageService implements SendMessageService {
	
	private static final Logger logger = LoggerFactory.getLogger(CMTelecomSendMessageService.class);
	
	private String id = "2763";
	
    private String username = "KeYZonET";
    
    private String password = "a2D3zh53";
    
    private String sendSmsUrl = "https://secure.cm.nl/smssgateway/cm/gateway.ashx";
    
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
		
		logger.info("Sending from: " + smsMessage.getSc() + " to: " + smsMessage.getUser().getMsisdn() + " text: " + smsMessage.getText());
		
		try {
			
			logger.info("Generating XML...");
			
			String xml = this.generateCMTelecomXML(smsMessage);
			
			if (xml == null) {
				return null;
			}
			
			logger.info("Sending request...");
			
			String response = this.sendRequestToGateway(xml);
			
			if ("".equals(response)) {
				logger.info("OK");
			} else {
				logger.error("ERROR! Response is:");
				logger.error(response);
				throw new GatewayResponseError(response.toString(), String.valueOf(200));
			}
		} catch (IllegalArgumentException e) {
			logger.error("Error sending SMS!", e);
			throw new IOException(e.getMessage());
		}
		
		return null;
	}
	
	private String sendRequestToGateway(String xml) throws IOException {
        
        URL url = null;
        URLConnection uRLConnection = null;
        OutputStream outputStream = null;
        InputStream inputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        String line = null;
        String response = null;
        
        try {
            url = new URL(this.sendSmsUrl);
            uRLConnection = url.openConnection();
            uRLConnection.setDoOutput(true);
            outputStream = uRLConnection.getOutputStream();
            outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write(xml);
            outputStreamWriter.flush();
            inputStream = uRLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            
            StringBuffer responseStringBuffer = new StringBuffer();
            
            while ((line = bufferedReader.readLine()) != null) {
            	responseStringBuffer.append(line);
            }
            
            bufferedReader.close();
            inputStreamReader.close();
            outputStreamWriter.close();
            inputStream.close();
            outputStream.close();

            response = responseStringBuffer.toString();
        } catch (Exception e) {
        	logger.error("Error sending request!", e);
        	throw new IOException("Error sending request!", e);
        } finally {
        	if (bufferedReader != null) {
        		try {
        			bufferedReader.close();
        		} catch (Exception e) {}
        	}
        	if (inputStreamReader != null) {
        		try {
        			inputStreamReader.close();
        		} catch (Exception e) {}
        	}
        	if (outputStreamWriter != null) {
        		try {
        			outputStreamWriter.close();
        		} catch (Exception e) {}
        	}
        	if (inputStream != null) {
        		try {
        			inputStream.close();
        		} catch (Exception e) {}
        	}
        	if (outputStream != null) {
        		try {
        			outputStream.close();
        		} catch (Exception e) {}
        	}
        }
        
        return response;
    }
	
	private String generateCMTelecomXML(SMSMessage smsMessage) throws IllegalArgumentException {
		
		if (smsMessage == null) {
			throw new IllegalArgumentException("SMSMessage is NULL!");
		} else {
			if (smsMessage.getSc() == null || "".equals(smsMessage.getSc())) {
				throw new IllegalArgumentException("SMSMessage.getSc() is NULL or empty!");
			}
			if (smsMessage.getText() == null || "".equals(smsMessage.getText())) {
				logger.info("SMSMessage.getText() is NULL or empty, skipping...");
				return null;
			}
			if (smsMessage.getUser() == null || smsMessage.getUser().getMsisdn() == null || "".equals(smsMessage.getUser().getMsisdn())) {
				throw new IllegalArgumentException("SMSMessage.getUser().getMsisdn() is NULL or empty!");
			}
		}
		
		String text = smsMessage.getText();
		String sc = smsMessage.getSc();
		String msisdn = smsMessage.getUser().getMsisdn();
		
		text = StringEscapeUtils.escapeXml(text);
		sc = "00" + sc.trim();
		msisdn = "00" + msisdn.trim();
		
		StringBuilder xml = new StringBuilder();
		
		xml.append("<?xml version=\"1.0\"?>\n");
		xml.append("<MESSAGES>\n");
		xml.append("\t<CUSTOMER ID=\"").append(this.id).append("\" />\n");
		xml.append("\t<USER LOGIN=\"").append(this.username).append("\" PASSWORD=\"").append(this.password).append("\" />\n");
		xml.append("\t<MSG>\n");
		xml.append("\t\t<FROM>").append(sc).append("</FROM>\n");
		xml.append("\t\t<BODY TYPE=\"TEXT\">").append(text).append("</BODY>\n");
		xml.append("\t\t<TO>").append(msisdn).append("</TO>\n");
		xml.append("\t</MSG>\n");
		xml.append("</MESSAGES>\n");
		
		return xml.toString();
	}
	
	@Override
	public String sendWapPushMessage(SMSMessage smsMessage) {
		logger.error("Not implemented!");
		return null;
	}
}