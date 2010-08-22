package hr.chus.cchat.struts2.action.admin;

import java.util.Date;
import java.util.List;

import hr.chus.cchat.db.service.SMSMessageService;
import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.db.jpa.SMSMessage;
import hr.chus.cchat.model.db.jpa.SMSMessage.Direction;
import hr.chus.cchat.model.db.jpa.ServiceProvider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class AdminSMSMessageList extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	
	private Log log = LogFactory.getLog(getClass());
	
	private SMSMessageService smsMessageService;
	private Operator operator;
	private ServiceProvider serviceProvider;
	private String msisdn;
	private Direction direction;
	private String text;
	private Date startDate;
	private Date endDate;
	private int start;
	private int limit;
	
	private List<SMSMessage> smsMessageList;
	private Long totalCount;
	
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		log.info("Searching for sms messages from ..." + start + " for " + limit);
		Object[] result = smsMessageService.search(operator, serviceProvider, direction, msisdn, startDate, endDate, text, start, limit);
		totalCount = (Long) result[0];
		smsMessageList = (List<SMSMessage>) result[1];
		log.debug("Found " + totalCount + " sms messages.");
		return SUCCESS;
	}

	
	// Getters & setters
	
	public List<SMSMessage> getSmsMessageList() { return smsMessageList; }
	public void setSmsMessageList(List<SMSMessage> smsMessageList) { this.smsMessageList = smsMessageList; }

	public Long getTotalCount() { return totalCount; }
	public void setTotalCount(Long totalCount) { this.totalCount = totalCount; }

	public void setSmsMessageService(SMSMessageService smsMessageService) { this.smsMessageService = smsMessageService; }

	public void setOperator(Operator operator) { this.operator = operator; }

	public void setServiceProvider(ServiceProvider serviceProvider) { this.serviceProvider = serviceProvider; }

	public void setMsisdn(String msisdn) { this.msisdn = msisdn; }

	public void setDirection(Direction direction) { this.direction = direction; }
	
	public void setText(String text) { this.text = text; }

	public void setStartDate(Date startDate) { this.startDate = startDate; }

	public void setEndDate(Date endDate) { this.endDate = endDate; }

	public void setStart(int start) { this.start = start; }

	public void setLimit(int limit) { this.limit = limit; }

}
