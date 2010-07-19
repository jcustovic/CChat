package hr.chus.cchat.db.service;

import java.util.List;

import hr.chus.cchat.model.db.jpa.Nick;
import hr.chus.cchat.model.db.jpa.Picture;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public interface PictureService {
	
	public void addPicture(Picture picture);
	public void removePicture(Picture picture);
	public Picture updatePicture(Picture picture);
	public Picture getPictureById(Integer id);
	public List<Picture> getAllPictures();
	public boolean checkIfPictureNameExists(String pictureName);
	public List<Picture> getPicturesByNick(Nick nick);

}
