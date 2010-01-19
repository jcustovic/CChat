package hr.chus.cchat.struts2.action.admin;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hr.chus.cchat.db.service.PictureService;
import hr.chus.cchat.model.db.jpa.Nick;
import hr.chus.cchat.model.db.jpa.Picture;

import com.opensymphony.xwork2.ActionSupport;

public class AdminPictureList extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private Log log = LogFactory.getLog(getClass());
	
	private List<Picture> pictureList;
	private PictureService pictureService;
	private Nick nick;
	
	@Override
	public String execute() throws Exception {
		try {
			if (nick == null) {
				pictureList = pictureService.getAllPictures();
			} else {
				pictureList = pictureService.getPicturesByNick(nick);
			}
		} catch (Exception e) {
			log.error(e, e);
		}
		log.debug("Got " + pictureList.size() + " picture(s).");
		return SUCCESS;
	}

	
	// Getters & setters
	
	public List<Picture> getPictureList() { return pictureList; }
	public void setPictureList(List<Picture> pictureList) { this.pictureList = pictureList; }

	public void setPictureService(PictureService pictureService) { this.pictureService = pictureService; }

	public Nick getNick() { return nick; }
	public void setNick(Nick nick) { this.nick = nick; }
		
}
