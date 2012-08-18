package hr.chus.cchat.struts2.converter;

import hr.chus.cchat.db.service.PictureService;
import hr.chus.cchat.model.db.jpa.Picture;

import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Custom Struts2 Picture type converter (picture id gets converted to picture object).
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public class PictureTypeConverter extends StrutsTypeConverter {

    private static final Logger LOG = LoggerFactory.getLogger(PictureTypeConverter.class);

    @Autowired
    private PictureService      pictureService;

    @Override
    public Object convertFromString(Map context, String[] values, Class toClass) {
        LOG.debug("Started converting String to Picture");
        Picture picture = null;
        try {
            picture = pictureService.getPictureById(Integer.valueOf(values[0]));
        } catch (Exception e) {
            return null;
        }
        LOG.debug("Finished converting String to Picture");

        return picture;
    }

    @Override
    public String convertToString(Map context, Object o) {
        return ((Picture) o).getId().toString();
    }

}
