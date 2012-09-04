package hr.chus.cchat.model.helper;

import hr.chus.cchat.model.db.jpa.Operator;

/**
 * Object containing operator and total users that have unreadMsgCount > 0.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public class OperatorWrapper {

    private Operator operator;
    private Long     unreadUserCount;

    public OperatorWrapper(final Operator p_operator, final Long p_unreadUserCount) {
        super();
        this.operator = p_operator;
        this.unreadUserCount = p_unreadUserCount;
    }

    // Getters & setters

    public final Operator getOperator() {
        return operator;
    }

    public final void setOperator(final Operator p_operator) {
        this.operator = p_operator;
    }

    public final Long getUnreadUserCount() {
        return unreadUserCount;
    }

    public final void setUnreadUserCount(final Long p_unreadUserCount) {
        this.unreadUserCount = p_unreadUserCount;
    }

}
