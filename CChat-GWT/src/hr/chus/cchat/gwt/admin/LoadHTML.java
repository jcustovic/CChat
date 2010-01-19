package hr.chus.cchat.gwt.admin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.user.client.Window;
import com.gwtext.client.widgets.BoxComponent;

public class LoadHTML {
	
	private String userInfoUrl; 
	
	public void loadData(final BoxComponent panel) {
		// Send request to server and catch any errors.
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, userInfoUrl);
		try {
			builder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					Window.open(GWT.getModuleBaseURL() + "Login", "_self", "");
				}

				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						panel.getEl().update(response.getText());
					} else if (450 == response.getStatusCode()) {
						Window.open(GWT.getModuleBaseURL() + "Login", "_self", "");
					}
				}
			});
		} catch (RequestException e) { }
	}

	
	// Getters & setters
	
		
	public String getUserInfoUrl() { return userInfoUrl; }
	public void setUserInfoUrl(String userInfoUrl) { this.userInfoUrl = userInfoUrl; }
		
}
