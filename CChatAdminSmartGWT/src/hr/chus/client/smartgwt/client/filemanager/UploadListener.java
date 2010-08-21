package hr.chus.client.smartgwt.client.filemanager;

public interface UploadListener {
	
	public void uploadError(String msg);
	public void uploadComplete(String msg);

}
