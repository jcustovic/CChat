package hr.chus.cchat.model.helper.db;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class StatisticsPerOperator {
	
	private String operatorUsername;
	private Integer receivedCount;
	private Integer sentCount;
	
	
	public StatisticsPerOperator(String operatorUsername, Integer receivedCount, Integer sentCount) {
		this.operatorUsername = operatorUsername;
		this.receivedCount = receivedCount;
		this.sentCount = sentCount;
	}
	
	
	// Getters & setters
	
	public String getOperatorUsername() { return operatorUsername; }
	public void setOperatorUsername(String operatorUsername) { this.operatorUsername = operatorUsername; }
	
	public Integer getReceivedCount() { return receivedCount; }
	public void setReceivedCount(Integer receivedCount) { this.receivedCount = receivedCount; }
	
	public Integer getSentCount() { return sentCount; }
	public void setSentCount(Integer sentCount) { this.sentCount = sentCount; }

}
