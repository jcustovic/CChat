package hr.chus.cchat.struts2.action.operator;

import java.util.List;

import hr.chus.cchat.db.service.SMSMessageService;
import hr.chus.cchat.model.helper.db.Conversation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class UserConversation extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	
	private Log log = LogFactory.getLog(getClass());
	
	private SMSMessageService smsMessageService;
	private Integer userId;
	private int start;
	private int limit;
	
	private List<Conversation> conversationList;
	
	
	@Override
	public String execute() throws Exception {
		log.info("Fetching conversation for user with id " + userId + " (start: " + start + " limit: " + limit + ")");
		conversationList = smsMessageService.getConversationByUserId(userId, start, limit);
		return SUCCESS;
	}
	
	
	// Getters & setters
	
	public void setSmsMessageService(SMSMessageService smsMessageService) { this.smsMessageService = smsMessageService; }

	public void setUserId(Integer userId) { this.userId = userId; }	
	
	public void setStart(int start) { this.start = start; }

	public void setLimit(int limit) { this.limit = limit; }
	
	public List<Conversation> getConversationList() { return conversationList; }

}
