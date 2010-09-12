package hr.chus.cchat.struts2.action.operator;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionSupport;

import hr.chus.cchat.db.service.NickService;
import hr.chus.cchat.model.db.jpa.Nick;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class OperatorNickList extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private Log log = LogFactory.getLog(getClass());
	
	private List<Nick> nickList;
	private NickService nickService;
	
	@Override
	public String execute() {
		nickList = nickService.getAllNicks();
		log.debug("Got " + nickList.size() + " nicks.");
		return SUCCESS;
	}

	
	// Getters & setters
	
	public List<Nick> getNickList() { return nickList; }
	public void setNickList(List<Nick> nickList) { this.nickList = nickList; }

	public void setNickService(NickService nickService) { this.nickService = nickService; }
	
}
