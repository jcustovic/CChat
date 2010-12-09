package hr.chus.cchat.struts2.action.admin;

import java.io.File;
import java.util.Map;

import hr.chus.cchat.ApplicationConstants;
import hr.chus.cchat.db.service.PictureService;
import hr.chus.cchat.model.db.jpa.Picture;

import javax.servlet.ServletContext;

import org.apache.struts2.util.ServletContextAware;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that implement picture functions. Thru this web service picture can be update,
 * or deleted (dao services are invoked).
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class AdminPictureFunction extends ActionSupport implements ServletContextAware {

	private static final long serialVersionUID = 1L;
	
	private PictureService pictureService;
	private String operation;
	private Picture picture;
	private ServletContext servletContext;
	private Map<String, String> errorFields;
	private String status;
	
	@Override
	public String execute() throws Exception {
		if (operation == null) return SUCCESS;
		if (operation.equals("delete")) {
			File theFile = new File(servletContext.getRealPath(ApplicationConstants.CONTENT_PATH) + System.getProperty("file.separator") + picture.getName());
			if (theFile.exists()) {
				theFile.delete();
			}
			if (!theFile.exists()) {
				picture = pictureService.getPictureById(picture.getId());
				pictureService.removePicture(picture);
			}
		} else if (operation.equals("update")) {
			picture = pictureService.updatePicture(picture);
		}
		return SUCCESS;
	}

	
	// Getters & setters
	
	public String getOperation() { return operation; }
	public void setOperation(String operation) { this.operation = operation; }

	public Picture getPicture() { return picture; }
	public void setPicture(Picture picture) { this.picture = picture; }

	public void setPictureService(PictureService pictureService) { this.pictureService = pictureService; }

	@Override
	public void setServletContext(ServletContext servletContext) { this.servletContext = servletContext; }
	
	public Map<String, String> getErrorFields() { return errorFields; }
	public void setErrorFields(Map<String, String> errorFields) { this.errorFields = errorFields; }

	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }
	
}
