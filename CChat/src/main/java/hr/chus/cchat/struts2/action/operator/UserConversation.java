package hr.chus.cchat.struts2.action.operator;

import hr.chus.cchat.db.service.SMSMessageService;
import hr.chus.cchat.db.service.UserService;
import hr.chus.cchat.helper.UserAware;
import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.helper.db.Conversation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that return conversation with specified user.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
public class UserConversation extends ActionSupport implements UserAware {

    private static final Logger LOG = LoggerFactory.getLogger(UserConversation.class);

    @Autowired
    private SMSMessageService   smsMessageService;

    @Autowired
    private UserService         userService;

    private Operator            operator;
    private Integer             userId;
    private int                 start;
    private int                 limit;
    private boolean             setMessagesAsRead;
    private List<Conversation>  conversationList;
    private Long                totalCount;

    @SuppressWarnings("unchecked")
    @Override
    public String execute() throws Exception {
        LOG.info("Fetching conversation for user with id " + userId + " (start: " + start + " limit: " + limit + ")");
        if (setMessagesAsRead) {
            userService.updateAllMessagesRead(userId);
            smsMessageService.updateSMSMessageOperatorIfNull(operator.getId(), userId);
        }
        Object[] result = smsMessageService.getConversationByUserId(userId, start, limit);
        totalCount = (Long) result[0];
        conversationList = (List<Conversation>) result[1];
        LOG.info("Returning messages... Total count: {}", totalCount);
        return SUCCESS;
    }

    // Getters & setters

    @Override
    public void setAuthenticatedUser(Operator operator) {
        this.operator = operator;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<Conversation> getConversationList() {
        return conversationList;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public void setSetMessagesAsRead(boolean setMessagesAsRead) {
        this.setMessagesAsRead = setMessagesAsRead;
    }

}
