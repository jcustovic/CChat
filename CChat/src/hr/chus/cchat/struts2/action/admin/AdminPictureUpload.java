package hr.chus.cchat.struts2.action.admin;

import hr.chus.cchat.db.service.PictureService;
import hr.chus.cchat.model.db.jpa.Nick;
import hr.chus.cchat.model.db.jpa.Picture;
import hr.chus.cchat.spring.override.OpenPropertyPlaceholderConfigurer;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that implement picture upload function.
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class AdminPictureUpload extends ActionSupport implements ApplicationContextAware {

	private static final long serialVersionUID = 1L;
	private Log log = LogFactory.getLog(getClass());
	
	private File picture;
	private String pictureContentType;
	private String pictureFileName;
	private Nick nick;
	private PictureService pictureService;
	private ApplicationContext applicationContext;
	private OpenPropertyPlaceholderConfigurer propertyConfigurer;
	
	private Map<String, String> errorFields;
	private String status;
	

	@Override
	public String execute() {
		try {
			String dataPath = (String) propertyConfigurer.getMergedProperties().get("data.dir");
			if (dataPath == null) dataPath = "data";
			File dataFolder = applicationContext.getResource(dataPath).getFile();
			if (!dataFolder.exists()) {
				log.info("Data folder doesn't exist... Creating " + dataFolder.getAbsolutePath());
				dataFolder.mkdir();
			}
			File theFile = new File(dataFolder, pictureFileName);
			log.debug("Saving picture to " + theFile.getPath());
			FileUtils.copyFile(picture, theFile);
			
			Picture picture = new Picture(nick, pictureFileName, pictureContentType, this.picture.length());
			picture = pictureService.updatePicture(picture);
		} catch (Exception e) {
			log.error(e, e);
			return INPUT;
		}
		return SUCCESS;
	}

	@Override
	public void validate() {
		errorFields = new LinkedHashMap<String, String>();
		
		List<String> pictureErrors = getFieldErrors().get("picture");
		if (pictureErrors != null && pictureErrors.size() > 0) {
			errorFields.put("picture", pictureErrors.get(0));
		} else if (pictureFileName != null) {
			if (pictureService.checkIfPictureNameExists(pictureFileName)) {
				errorFields.put("picture", getText("admin.picture.upload.alreadyExists"));
				addActionError(getText("admin.picture.upload.alreadyExists"));
			}
		}
		
		if (errorFields.size() == 0) {
			errorFields = null;
			status = "validation_ok";
		} else {
			addActionError(errorFields.size() + " errors found!");
			status = "validation_error";
		}
	}
	
	
	// Getters & setters
	
	public void setPicture(File picture) { this.picture = picture; }

	public void setPictureContentType(String pictureContentType) { this.pictureContentType = pictureContentType; }

	public String getPictureFileName() { return pictureFileName; }
	public void setPictureFileName(String pictureFileName) { this.pictureFileName = pictureFileName; }

	public void setNick(Nick nick) { this.nick = nick; }
	
	public void setPictureService(PictureService pictureService) { this.pictureService = pictureService; }
	
	public Map<String, String> getErrorFields() { return errorFields; }
	public void setErrorFields(Map<String, String> errorFields) { this.errorFields = errorFields; }

	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }
	
	public void setPropertyConfigurer(OpenPropertyPlaceholderConfigurer propertyConfigurer) { this.propertyConfigurer = propertyConfigurer; }

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
}
