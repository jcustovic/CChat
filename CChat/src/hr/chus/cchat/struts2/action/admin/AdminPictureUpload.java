package hr.chus.cchat.struts2.action.admin;

import hr.chus.cchat.ApplicationConstants;
import hr.chus.cchat.db.service.PictureService;
import hr.chus.cchat.model.db.jpa.Nick;
import hr.chus.cchat.model.db.jpa.Picture;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.util.ServletContextAware;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author Jan Čustović
 *
 */
public class AdminPictureUpload extends ActionSupport implements ServletContextAware {

	private static final long serialVersionUID = 1L;
	private Log log = LogFactory.getLog(getClass());
	
	private File picture;
	private String pictureContentType;
	private String pictureFileName;
	private Nick nick;
	private PictureService pictureService;
	private String operation;
	private ServletContext servletContext;

	@Override
	public String execute() {
		if (operation.equals("upload")) {
			Picture picture = new Picture();
			try {
				File theFile = new File(servletContext.getRealPath(ApplicationConstants.CONTENT_PATH) + System.getProperty("file.separator") + getPictureFileName());
				log.debug("Saving picture to " + theFile.getPath());
				FileUtils.copyFile(getPicture(), theFile);
				picture.setLength(getPicture().length());
				picture.setName(getPictureFileName());
				picture.setType(getPictureContentType());
				picture.setNick(getNick());
				picture = pictureService.updatePicture(picture);
			} catch (IOException e) {
				return INPUT;
			}
		}
		return SUCCESS;
	}

	@Override
	public void validate() {
		if (getPictureFileName() != null) {
			if (pictureService.checkIfPictureNameExists(getPictureFileName())) {
				addActionError("Picture name exists!");
			}
		}
	}
	
	// Getters & setters
	
	public File getPicture() { return picture; }
	public void setPicture(File picture) { this.picture = picture; }

	public String getPictureContentType() { return pictureContentType; }
	public void setPictureContentType(String pictureContentType) { this.pictureContentType = pictureContentType; }

	public String getPictureFileName() { return pictureFileName; }
	public void setPictureFileName(String pictureFileName) { this.pictureFileName = pictureFileName; }

	public Nick getNick() { return nick; }
	public void setNick(Nick nick) { this.nick = nick; }
	
	public void setPictureService(PictureService pictureService) { this.pictureService = pictureService; }
	
	public String getOperation() { return operation; }
	public void setOperation(String operation) { this.operation = operation; }

	@Override
	public void setServletContext(ServletContext servletContext) { this.servletContext = servletContext; }
			
}
