package hr.chus.cchat.struts2.action.common;

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
public class NickList extends ActionSupport {

    private static final Logger   LOG = LoggerFactory.getLogger(NickList.class);

    @Autowired
    private transient NickService nickService;

    private List<Nick>            nickList;

    @Override
    public final String execute() {
        nickList = nickService.getAllNicks();
        LOG.debug("Got {} nicks.", nickList.size());

        return SUCCESS;
    }

    // Getters & setters

    public final List<Nick> getNickList() {
        return nickList;
    }

}
