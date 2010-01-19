package hr.chus.cchat.struts2.converter;

import java.util.Map;

import hr.chus.cchat.db.service.PictureService;
import hr.chus.cchat.model.db.jpa.Picture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.util.StrutsTypeConverter;

public class PictureTypeConverter extends StrutsTypeConverter {
	
	private Log log = LogFactory.getLog(getClass());

	private PictureService pictureService;
	
	
	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		log.debug("Started converting String to Picture");
		Picture picture = null;
		try {
			picture = pictureService.getPictureById(Integer.valueOf(values[0]));
		} catch (Exception e) {
			return null;
		}
		log.debug("Finished converting String to Picture");
		return picture;
	}

	@Override
	public String convertToString(Map context, Object o) {
		return ((Picture) o).getId().toString();
	}
	
	
	// Getters & setters
	
	public void setPictureService(PictureService pictureService) { this.pictureService = pictureService; }
	
}
