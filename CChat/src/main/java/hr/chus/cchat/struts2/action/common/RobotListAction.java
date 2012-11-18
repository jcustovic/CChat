package hr.chus.cchat.struts2.action.common;

import hr.chus.cchat.model.db.jpa.Robot;
import hr.chus.cchat.service.RobotService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that returns all bots.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
public class RobotListAction extends ActionSupport {

    private static final Logger    LOG = LoggerFactory.getLogger(RobotListAction.class);

    @Autowired
    private transient RobotService robotService;

    private List<Robot>            robotList;

    @Override
    public final String execute() {
        robotList = robotService.findAll();
        LOG.debug("Got {} robots.", robotList.size());

        return SUCCESS;
    }

    // Getters & setters

    public final List<Robot> getRobotList() {
        return robotList;
    }

}
