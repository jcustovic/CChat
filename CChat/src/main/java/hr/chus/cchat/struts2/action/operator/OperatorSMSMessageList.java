package hr.chus.cchat.struts2.action.operator;

import hr.chus.cchat.db.service.SMSMessageService;
import hr.chus.cchat.helper.UserAware;
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
 * User that have the role of "operators" can only search sms messages of users that they are assigned to.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
public class OperatorSMSMessageList extends ActionSupport implements UserAware {

    private static final Logger LOG = LoggerFactory.getLogger(OperatorSMSMessageList.class);

    @Autowired
    private SMSMessageService   smsMessageService;

    private Operator            user;
    private Operator            operator;
    private ServiceProvider     serviceProvider;
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
        if ("operator".equals(user.getRole().getName())) {
            operator = user;
        }
        LOG.debug("Searching for sms messages from ..." + start + " for " + limit);
        final Object[] result = smsMessageService.search(operator, serviceProvider, direction, userId, userName, userSurname, null, startDate, endDate, text,
                start, limit);
        totalCount = (Long) result[0];
        smsMessageList = (List<SMSMessage>) result[1];
        LOG.debug("Found " + totalCount + " sms messages.");

        return SUCCESS;
    }

    // Getters & setters

    @Override
    public void setAuthenticatedUser(Operator user) {
        this.user = user;
    }

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
