package hr.chus.cchat.helper;

import hr.chus.cchat.db.service.OperatorService;
import hr.chus.cchat.model.db.jpa.Operator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class is used for determining how users are going to be assigned to operators.
 * We need to keep track of active operators and assign users, that do not have assigned
 * operators, to them. Users are assigned to operators sequentially.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public class OperatorChooser {

    private static final Logger LOG = LoggerFactory.getLogger(OperatorChooser.class);

    @Autowired
    private OperatorService     operatorService;

    private Integer             lastChoosenOperatorId;
    private List<Operator>      activeOperatorList;

    public void init() {
        activeOperatorList = new LinkedList<Operator>();
        activeOperatorList.addAll(operatorService.getAllActiveOperators());
    }

    public void addActiveOperator(Operator operator) {
        if (!activeOperatorList.contains(operator)) {
            activeOperatorList.add(operator);
        }
    }

    public void removeActiveOperator(Operator operator) {
        activeOperatorList.remove(operator);
    }

    public Operator chooseOperator() {
        LOG.debug("Choosing operator...");
        Operator operator = null;
        if (activeOperatorList.size() == 0) {
            LOG.info("No active operators to choose from.");
            return null;
        } else if (activeOperatorList.size() == 1) {
            operator = activeOperatorList.get(0);
        } else if (lastChoosenOperatorId == null && activeOperatorList.size() > 1) {
            operator = activeOperatorList.get(0);
        } else {
            // Choose next operator by lowest id after last chosen id
            Integer nextOperatorId = null;
            Operator lowestOperatorById = null;
            Iterator<Operator> operatorsIterator = activeOperatorList.iterator();
            while (operatorsIterator.hasNext()) {
                Operator op = operatorsIterator.next();
                if (lowestOperatorById == null || lowestOperatorById.getId() > op.getId()) lowestOperatorById = op;
                if (op.getId() > lastChoosenOperatorId) {
                    if (nextOperatorId == null || op.getId() < nextOperatorId) {
                        nextOperatorId = op.getId();
                        operator = op;
                    }
                }
            }
            if (nextOperatorId == null) {
                operator = lowestOperatorById;
            }
        }
        if (operator != null) lastChoosenOperatorId = operator.getId();
        LOG.info("Operator " + operator + " chosen.");

        return operator;
    }

    // Getters & setters

    public Integer getLastChoosedOperatorId() {
        return lastChoosenOperatorId;
    }

    public List<Operator> getActiveOperatorList() {
        return activeOperatorList;
    }

}
