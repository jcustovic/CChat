package hr.chus.cchat.struts2.action.admin;

import java.io.File;
import java.util.Map;

import hr.chus.cchat.db.service.PictureService;
import hr.chus.cchat.model.db.jpa.Picture;
import hr.chus.cchat.spring.override.OpenPropertyPlaceholderConfigurer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that implement picture functions. Thru this web service picture can be update,
 * or deleted (dao services are invoked).
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class AdminPictureFunction extends ActionSupport implements ApplicationContextAware {

	private static final long serialVersionUID = 1L;
	
	private Log log = LogFactory.getLog(getClass());
	
	private OpenPropertyPlaceholderConfigurer propertyConfigurer;
	private ApplicationContext applicationContext;
	private PictureService pictureService;
	
	private String operation;
	private Picture picture;
	private Map<String, String> errorFields;
	private String status;
	
	
	@Override
	public String execute() throws Exception {
		if (operation == null) return SUCCESS;
		if (operation.equals("delete")) {
			String dataPath = (String) propertyConfigurer.getMergedProperties().get("data.dir");
			if (dataPath == null) dataPath = "data";
			File dataFolder = applicationContext.getResource(dataPath).getFile();
			if (!dataFolder.exists()) {
				log.info("Data folder doesn't exist... Creating " + dataFolder.getAbsolutePath());
				dataFolder.mkdir();
			}
			File theFile = new File(dataFolder, picture.getName());
			log.debug("Deleting picture " + theFile.getPath());
			if (theFile.exists()) {
				theFile.delete();
			}
			picture = pictureService.getPictureById(picture.getId());
			pictureService.removePicture(picture);
		} else if (operation.equals("update")) {
			picture = pictureService.updatePicture(picture);
		}
		return SUCCESS;
	}

	
	// Getters & setters
	
	public void setPropertyConfigurer(OpenPropertyPlaceholderConfigurer propertyConfigurer) { this.propertyConfigurer = propertyConfigurer; }
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException { this.applicationContext = applicationContext; }
	
	public String getOperation() { return operation; }
	public void setOperation(String operation) { this.operation = operation; }

	public Picture getPicture() { return picture; }
	public void setPicture(Picture picture) { this.picture = picture; }

	public void setPictureService(PictureService pictureService) { this.pictureService = pictureService; }

	public Map<String, String> getErrorFields() { return errorFields; }
	public void setErrorFields(Map<String, String> errorFields) { this.errorFields = errorFields; }

	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }
	
}
