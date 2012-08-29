package hr.chus.cchat.scheduler;

import hr.chus.cchat.db.service.OperatorService;
import hr.chus.cchat.db.service.UserService;
import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.db.jpa.User;
import hr.chus.cchat.model.helper.OperatorWrapper;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Scheduler that assigns users that have unread messages to active operators which are not in full load.
 * Users are distributed to operators with lowest load.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@Component
public class UnreadMsgAssignerScheduler {

    private static final Logger LOG               = LoggerFactory.getLogger(UnreadMsgAssignerScheduler.class);

    // 1 minute
    private static final long   SCHEDULE_INTERVAL = 60000;

    // Operator can have a maximum number of unread users assigned to him
    private static final long   MAX_UNREAD_USERS  = 100;

    // Amount of unread users to fetch in one database go
    private static final int    BATCH_SIZE        = 50;

    @Autowired
    private OperatorService     operatorService;

    @Autowired
    private UserService         userService;

    @Transactional
    @Scheduled(fixedDelay = SCHEDULE_INTERVAL)
    public void run() {
        LOG.debug("Runnnig UnreadMsgAssignerScheduler...");
        final List<Operator> activeOperators = operatorService.getAllActiveOperators();

        // Find operators that can handle more messages
        final List<OperatorWrapper> operators = new LinkedList<OperatorWrapper>();
        for (final Operator operator : activeOperators) {
            final Long unreadUserCount = userService.countByOperatorAndUnread(operator);
            if (unreadUserCount >= MAX_UNREAD_USERS) {
                continue;
            }

            operators.add(new OperatorWrapper(operator, unreadUserCount));
        }

        if (operators.isEmpty()) {
            LOG.debug("No active/available operators found to assign users to.");
        } else {
            boolean hasOperators = true;
            // Try to assign as much users to operators as possible
            while (hasOperators) {
                final List<User> unassignedUserList = userService.findUnassigned(BATCH_SIZE);
                LOG.debug("Processing batch of {} unread users...", unassignedUserList.size());

                if (unassignedUserList.isEmpty()) {
                    break;
                }
                for (final User user : unassignedUserList) {
                    final Operator bestMatchOperator = findUserByLoadLowest(operators);
                    if (bestMatchOperator == null) {
                        LOG.info("No available/free operators to assign users.");
                        hasOperators = false;
                        break;
                    } else {
                        user.setOperator(bestMatchOperator);
                        userService.editUser(user);
                    }
                }

            }
        }

        LOG.debug("UnreadMsgAssignerScheduler done executing");
    }

    /**
     * Method for finding operator with lowest load (unread user count).
     * 
     * @param p_operators Operator list with user count.
     * @return Operator with lowest load.
     */
    private Operator findUserByLoadLowest(final List<OperatorWrapper> p_operators) {
        OperatorWrapper bestOperator = null;
        long lowestLoad = MAX_UNREAD_USERS;
        for (OperatorWrapper operatorWrapper : p_operators) {
            if (operatorWrapper.getUnreadUserCount() < lowestLoad) {
                lowestLoad = operatorWrapper.getUnreadUserCount();
                bestOperator = operatorWrapper;
            }
        }

        if (bestOperator != null) {
            // Add one more user to selected operator
            bestOperator.setUnreadUserCount(bestOperator.getUnreadUserCount() + 1);

            return bestOperator.getOperator();
        }

        return null;
    }

}