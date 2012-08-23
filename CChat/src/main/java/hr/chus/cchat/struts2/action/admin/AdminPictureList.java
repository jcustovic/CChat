package hr.chus.cchat.struts2.action.admin;

import hr.chus.cchat.db.service.PictureService;
import hr.chus.cchat.model.db.jpa.Nick;
import hr.chus.cchat.model.db.jpa.Picture;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action that returns all pictures or if nick is provided just pictures for that nick.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@SuppressWarnings("serial")
public class AdminPictureList extends ActionSupport {

    private static final Logger          LOG = LoggerFactory.getLogger(AdminPictureList.class);

    @Autowired
    private PictureService               pictureService;

    @Value("${data.dir}")
    private transient FileSystemResource dataDir;

    private List<Picture>                pictureList;
    private Nick                         nick;

    @Override
    public String execute() throws Exception {
        String dataPath = dataDir.getPath();
        if (dataPath == null) dataPath = "data";

        if (nick == null) {
            pictureList = pictureService.getAllPictures();
        } else {
            pictureList = pictureService.getPicturesByNick(nick);
        }
        for (Picture picture : pictureList) {
            picture.setUrl(dataPath + "/" + picture.getName());
        }
        LOG.debug("Got {} picture(s).", pictureList.size());

        return SUCCESS;
    }

    // Getters & setters

    public List<Picture> getPictureList() {
        return pictureList;
    }

    public void setPictureList(List<Picture> pictureList) {
        this.pictureList = pictureList;
    }

    public Nick getNick() {
        return nick;
    }

    public void setNick(Nick nick) {
        this.nick = nick;
    }

}
