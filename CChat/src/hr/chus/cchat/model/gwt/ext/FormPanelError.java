package hr.chus.cchat.model.gwt.ext;

public class FormPanelError {
	
	private String id;
	private String msg;
	

	public FormPanelError(String id, String msg) {
		this.id = id;
		this.msg = msg;
	}
	
	// Getters & setters
	
	public String getId() { return id; }
	public void setId(String id) { this.id = id; }

	public String getMsg() { return msg; }
	public void setMsg(String msg) { this.msg = msg; }
	
}
