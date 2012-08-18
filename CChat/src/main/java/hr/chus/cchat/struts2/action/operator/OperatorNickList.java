package hr.chus.cchat.struts2.action.operator;

import hr.chus.cchat.db.service.NickService;
import hr.chus.cchat.model.db.jpa.Nick;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that returns all nicks.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
public class OperatorNickList extends ActionSupport {

    private static final Logger LOG = LoggerFactory.getLogger(OperatorNickList.class);

    @Autowired
    private NickService         nickService;

    private List<Nick>          nickList;

    @Override
    public String execute() {
        nickList = nickService.getAllNicks();
        LOG.debug("Got " + nickList.size() + " nicks.");

        return SUCCESS;
    }

    // Getters & setters

    public List<Nick> getNickList() {
        return nickList;
    }

    public void setNickList(List<Nick> nickList) {
        this.nickList = nickList;
    }

}