package hr.chus.cchat.struts2.action.admin;

import java.io.File;
import java.util.List;

import hr.chus.cchat.ApplicationConstants;
import hr.chus.cchat.db.service.PictureService;
import hr.chus.cchat.model.db.jpa.Picture;
import hr.chus.cchat.model.gwt.ext.FormPanelError;

import javax.servlet.ServletContext;

import com.opensymphony.xwork2.ActionSupport;

public class AdminPictureFunction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	
	private PictureService pictureService;
	private String operation;
	private Picture picture;
	private ServletContext servletContext;
	private List<FormPanelError> errors;
	private boolean success;
	
	@Override
	public String execute() throws Exception {
		success = true;
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

	public void setServletContext(ServletContext servletContext) { this.servletContext = servletContext; }
	
	// TODO: FIX - Should be getErrors(). Had to be done this way because of GWT JSON build in validation.
	public List<FormPanelError> geterrors() { return errors; }
	public void setErrors(List<FormPanelError> errors) { this.errors = errors; }

	public boolean isSuccess() { return success; }
	public void setSuccess(boolean success) { this.success = success; }
	
}
