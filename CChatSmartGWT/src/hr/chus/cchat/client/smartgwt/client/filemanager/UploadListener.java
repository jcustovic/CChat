package hr.chus.cchat.client.smartgwt.client.filemanager;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public interface UploadListener {
	
	public void uploadError(String msg);
	public void uploadComplete(String msg);

}
