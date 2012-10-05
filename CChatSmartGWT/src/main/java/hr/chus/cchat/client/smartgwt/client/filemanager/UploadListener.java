package hr.chus.cchat.client.smartgwt.client.filemanager;

/**
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 *
 */
public interface UploadListener {
	
	void uploadError(String msg);
	
	void uploadComplete(String msg);

}
