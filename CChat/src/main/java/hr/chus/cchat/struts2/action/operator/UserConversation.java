package hr.chus.cchat.struts2.action.operator;

import hr.chus.cchat.db.service.SMSMessageService;
import hr.chus.cchat.db.service.UserService;
import hr.chus.cchat.helper.UserAware;
import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.db.jpa.SMSMessage.Direction;
import hr.chus.cchat.model.helper.db.Conversation;
import hr.chus.cchat.service.RobotService;

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

    private static final Logger         LOG = LoggerFactory.getLogger(UserConversation.class);

    @Autowired
    private transient SMSMessageService smsMessageService;

    @Autowired
    private transient UserService       userService;

    @Autowired
    private transient RobotService      robotService;

    private Operator                    operator;
    private Integer                     userId;
    private int                         start;
    private int                         limit;
    private boolean                     setMessagesAsRead;
    private List<Conversation>          conversationList;
    private Long                        totalCount;
    private String                      botMsg;

    @SuppressWarnings("unchecked")
    @Override
    public final String execute() {
        LOG.info("Fetching conversation for user with id " + userId + " (start: " + start + " limit: " + limit + ")");
        if (setMessagesAsRead) {
            userService.updateAllMessagesRead(userId);
            smsMessageService.updateSMSMessageOperatorIfNull(operator.getId(), userId);
        }
        Object[] result = smsMessageService.getConversationByUserId(userId, start, limit);
        totalCount = (Long) result[0];
        conversationList = (List<Conversation>) result[1];

        // Get bot response only for last IN message
        if (start == 0 && !conversationList.isEmpty()) {
            final Conversation msg = conversationList.get(0);
            if (Direction.IN.equals(msg.getDirection())) {
                botMsg = robotService.responde(msg.getText(), userId);
            }
        }
        if (botMsg == null) {
            // Try to get any kind of response
            botMsg = robotService.responde(" ", userId);
        }

        LOG.info("Returning messages... Total count: {}", totalCount);

        return SUCCESS;
    }

    // Getters & setters

    @Override
    public final void setAuthenticatedUser(final Operator p_operator) {
        operator = p_operator;
    }

    public final void setUserService(final UserService p_userService) {
        userService = p_userService;
    }

    public final void setUserId(final Integer p_userId) {
        userId = p_userId;
    }

    public final void setStart(final int p_start) {
        start = p_start;
    }

    public final void setLimit(final int p_limit) {
        limit = p_limit;
    }

    public final List<Conversation> getConversationList() {
        return conversationList;
    }

    public final Long getTotalCount() {
        return totalCount;
    }

    public final void setTotalCount(final Long p_totalCount) {
        totalCount = p_totalCount;
    }

    public final void setSetMessagesAsRead(final boolean p_setMessagesAsRead) {
        setMessagesAsRead = p_setMessagesAsRead;
    }

    public final String getBotMsg() {
        return botMsg;
    }

}
