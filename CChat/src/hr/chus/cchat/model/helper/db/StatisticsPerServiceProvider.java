package hr.chus.cchat.model.helper.db;

import java.io.Serializable;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class StatisticsPerServiceProvider implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String providerName;
	private String shortCode;
	private Integer receivedCount;
	private Integer sendCount;
	
	
	public StatisticsPerServiceProvider(String providerName, String shortCode, Integer receivedCount, Integer sendCount) {
		this.providerName = providerName;
		this.shortCode = shortCode;
		this.receivedCount = receivedCount;
		this.sendCount = sendCount;
	}
	
	
	// Getters & setters
	
	public String getProviderName() { return providerName; }
	public void setProviderName(String providerName) { this.providerName = providerName; }

	public String getShortCode() { return shortCode; }
	public void setShortCode(String shortCode) { this.shortCode = shortCode; }

	public Integer getReceivedCount() { return receivedCount; }
	public void setReceivedCount(Integer receivedCount) { this.receivedCount = receivedCount; }

	public Integer getSendCount() { return sendCount; }
	public void setSendCount(Integer sendCount) { this.sendCount = sendCount; }
	
}
