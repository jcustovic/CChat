package hr.chus.cchat.struts2.action.admin;

import hr.chus.cchat.db.service.PictureService;
import hr.chus.cchat.model.db.jpa.Nick;
import hr.chus.cchat.model.db.jpa.Picture;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that implement picture upload function.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
public class AdminPictureUpload extends ActionSupport implements ServletRequestAware {

    private static final Logger          LOG = LoggerFactory.getLogger(AdminPictureUpload.class);

    @Autowired
    private PictureService               pictureService;

    @Value("${data.dir}")
    private transient FileSystemResource dataDir;

    private HttpServletRequest           request;

    private File                         picture;
    private String                       pictureContentType;
    private String                       pictureFileName;
    private Nick                         nick;

    private Map<String, String>          errorFields;
    private String                       status;

    @Override
    public String execute() {
        try {
            String dataPath = dataDir.getPath();
            if (dataPath == null) dataPath = "data";

            dataPath = request.getServletContext().getRealPath(dataPath);
            final File dataFolder = new File(dataPath);
            if (!dataFolder.exists()) {
                LOG.info("Data folder doesn't exist... Creating " + dataFolder.getAbsolutePath());
                dataFolder.mkdir();
            }

            final File theFile = new File(dataFolder, pictureFileName);
            LOG.info("Saving picture to " + theFile.getPath());
            FileUtils.copyFile(picture, theFile);

            Picture picture = new Picture(nick, pictureFileName, pictureContentType, this.picture.length());
            picture = pictureService.updatePicture(picture);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
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

    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    public void setPicture(File picture) {
        this.picture = picture;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    public String getPictureFileName() {
        return pictureFileName;
    }

    public void setPictureFileName(String pictureFileName) {
        this.pictureFileName = pictureFileName;
    }

    public void setNick(Nick nick) {
        this.nick = nick;
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
