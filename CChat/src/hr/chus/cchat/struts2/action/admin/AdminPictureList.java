package hr.chus.cchat.struts2.action.admin;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import hr.chus.cchat.db.service.PictureService;
import hr.chus.cchat.model.db.jpa.Nick;
import hr.chus.cchat.model.db.jpa.Picture;
import hr.chus.cchat.spring.override.OpenPropertyPlaceholderConfigurer;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class AdminPictureList extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private Log log = LogFactory.getLog(getClass());
	
	private List<Picture> pictureList;
	private PictureService pictureService;
	private Nick nick;
	
	private OpenPropertyPlaceholderConfigurer propertyConfigurer;
	
	
	@Override
	public String execute() throws Exception {
		
		String dataPath = (String) propertyConfigurer.getMergedProperties().get("data.dir");
		if (dataPath == null) dataPath = "data";

		if (nick == null) {
			pictureList = pictureService.getAllPictures();
		} else {
			pictureList = pictureService.getPicturesByNick(nick);
		}
		for (Picture picture : pictureList) {
			picture.setUrl(dataPath + "/" + picture.getName());
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

	public void setPropertyConfigurer(OpenPropertyPlaceholderConfigurer propertyConfigurer) { this.propertyConfigurer = propertyConfigurer; }
		
}
