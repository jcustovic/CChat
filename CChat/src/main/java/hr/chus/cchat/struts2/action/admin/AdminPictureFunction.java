package hr.chus.cchat.struts2.action.admin;

import hr.chus.cchat.db.service.PictureService;
import hr.chus.cchat.model.db.jpa.Picture;

import java.io.File;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that implement picture functions. Thru this web service picture can be update,
 * or deleted (dao services are invoked).
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
public class AdminPictureFunction extends ActionSupport implements ServletRequestAware {

    private static final Logger          LOG = LoggerFactory.getLogger(AdminPictureFunction.class);

    @Autowired
    private PictureService               pictureService;

    @Value("${data.dir}")
    private transient FileSystemResource dataDir;

    private HttpServletRequest           request;

    private String                       operation;
    private Picture                      picture;
    private Map<String, String>          errorFields;
    private String                       status;

    @Override
    public String execute() throws Exception {
        if ("delete".equals(operation)) {
            String dataPath = dataDir.getPath();
            if (dataPath == null) dataPath = "data";

            dataPath = request.getServletContext().getRealPath(dataPath);
            final File dataFolder = new File(dataPath);
            if (!dataFolder.exists()) {
                LOG.info("Data folder doesn't exist... Creating {}", dataFolder.getAbsolutePath());
                dataFolder.mkdir();
            }
            final File theFile = new File(dataFolder, picture.getName());
            LOG.info("Deleting picture {}", theFile.getPath());
            if (theFile.exists()) {
                theFile.delete();
            }
            picture = pictureService.getPictureById(picture.getId());
            pictureService.removePicture(picture);
        } else if ("update".equals(operation)) {
            picture = pictureService.updatePicture(picture);
        }

        return SUCCESS;
    }

    // Getters & setters

    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public Map<String, String> getErrorFields() {
        return errorFields;
    }

    public void setErrorFields(Map<String, String> errorFields) {
        this.errorFields = errorFields;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
