package hr.chus.cchat.client.smartgwt.client.admin;

import hr.chus.cchat.client.smartgwt.client.common.PanelFactory;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 *
 */
public class Settings extends HLayout {
	
    private static final String DESCRIPTION = "Settings";

    public static class Factory implements PanelFactory {
        
    	private String id;

        public Canvas create() {
        	Settings panel = new Settings();
            panel.setMargin(5);
            panel.setWidth100();
            panel.setHeight100();
            panel.addMember(panel.getViewPanel());
            id = panel.getID();
            
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
        final Canvas canvas = new Canvas();

        HTMLFlow htmlFlow = new HTMLFlow();
        htmlFlow.setOverflow(Overflow.AUTO);
        htmlFlow.setPadding(10);
        htmlFlow.setHeight("80%");
        htmlFlow.setContents("<b> Settings area </b>");
       
        canvas.addChild(htmlFlow);
        
        return canvas;
    }

}
