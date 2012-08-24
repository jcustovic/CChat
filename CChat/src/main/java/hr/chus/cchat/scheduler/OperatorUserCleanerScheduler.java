package hr.chus.cchat.scheduler;

import hr.chus.cchat.db.service.OperatorService;
import hr.chus.cchat.db.service.UserService;
import hr.chus.cchat.model.db.jpa.Operator;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduler that removes operators assigned to users. Operators which haven't communicated for a long period of time
 * with specified user are removed from that user.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@Component
public class OperatorUserCleanerScheduler {

    private static final Logger LOG               = LoggerFactory.getLogger(OperatorUserCleanerScheduler.class);

    // 10 minutes
    private static final long   SCHEDULE_INTERVAL = 60000;

    // 8 hours
    private static final long   HOURS_8           = 28800000;

    @Autowired
    private OperatorService     operatorService;

    @Autowired
    private UserService         userService;

    @Scheduled(fixedDelay = SCHEDULE_INTERVAL)
    public void run() {
        LOG.debug("Runnnig OperatorUserCleanerScheduler...");
        final List<Operator> activeOperators = operatorService.getAllActiveOperators();
        final Date beforeEigthHours = new Date(new Date().getTime() - HOURS_8);
        for (final Operator operator : activeOperators) {
            LOG.debug("Cleaning operator {}", operator.getUsername());
            userService.clearOperatorField(operator, beforeEigthHours);
        }
        LOG.debug("OperatorUserCleanerScheduler done executing");
    }
}
