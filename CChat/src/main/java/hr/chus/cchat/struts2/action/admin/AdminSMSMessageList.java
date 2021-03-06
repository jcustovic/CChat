package hr.chus.cchat.struts2.action.admin;

import hr.chus.cchat.db.service.SMSMessageService;
import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.db.jpa.SMSMessage;
import hr.chus.cchat.model.db.jpa.SMSMessage.Direction;
import hr.chus.cchat.model.db.jpa.ServiceProvider;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that searches SMS messages using different parameters. Supports pagination.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
public class AdminSMSMessageList extends ActionSupport {

    private static final Logger LOG = LoggerFactory.getLogger(AdminSMSMessageList.class);

    @Autowired
    private SMSMessageService   smsMessageService;

    private Operator            operator;
    private ServiceProvider     serviceProvider;
    private String              msisdn;
    private String              userName;
    private String              userSurname;
    private Integer             userId;
    private Direction           direction;
    private String              text;
    private Date                startDate;
    private Date                endDate;
    private int                 start;
    private int                 limit;

    private List<SMSMessage>    smsMessageList;
    private Long                totalCount;

    @SuppressWarnings("unchecked")
    @Override
    public String execute() throws Exception {
        LOG.debug("Searching for sms messages from ..." + start + " for " + limit);
        Object[] result = smsMessageService.search(operator, serviceProvider, direction, userId, userName, userSurname, msisdn, startDate, endDate, text,
                start, limit);
        totalCount = (Long) result[0];
        smsMessageList = (List<SMSMessage>) result[1];
        LOG.debug("Found {} sms messages.", totalCount);

        return SUCCESS;
    }

    // Getters & setters

    public List<SMSMessage> getSmsMessageList() {
        return smsMessageList;
    }

    public void setSmsMessageList(List<SMSMessage> smsMessageList) {
        this.smsMessageList = smsMessageList;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

}
