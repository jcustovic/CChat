package hr.chus.cchat.client.smartgwt.client.operator;

import hr.chus.cchat.client.smartgwt.client.common.PanelFactory;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 * 
 * @author Jan Čustović
 *
 */
public class UserConsole extends HLayout {
	
	private static final String DESCRIPTION = "UserConsole";
	
	private String userId;

	public static class Factory implements PanelFactory {
        
		private String id;

        public Factory(String userId) {
			this.id = userId;
		}

		public Canvas create() {
			UserConsole panel = new UserConsole();
			panel.setUserId(id);
            panel.setMargin(5);
            panel.setWidth100();
            panel.setHeight100();
            panel.addMember(panel.getViewPanel());
            return panel;
        }

        public String getID() {
            return id;
        }

        public String getDescription() {
            return DESCRIPTION;
        }
        
	}

	public Canvas getViewPanel() {
		Canvas canvas = new Canvas();
		
		HTMLFlow mainHtmlFlow = new HTMLFlow();
		mainHtmlFlow.setOverflow(Overflow.AUTO);
		mainHtmlFlow.setPadding(10);
		mainHtmlFlow.setHeight("86%");
		mainHtmlFlow.setContents("<b> " + userId + " </b>");
		 
		canvas.addChild(mainHtmlFlow);
		return canvas;
    }

	// Getters & setters
	
	public String getUserId() { return userId; }
	public void setUserId(String userId) { this.userId = userId; }
	
}
