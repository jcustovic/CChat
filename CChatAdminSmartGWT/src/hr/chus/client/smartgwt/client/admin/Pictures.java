package hr.chus.client.smartgwt.client.admin;

import hr.chus.client.smartgwt.client.PanelFactory;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.layout.HLayout;

public class Pictures extends HLayout {
	
    private static final String DESCRIPTION = "Pictures";

    public static class Factory implements PanelFactory {
        
    	private String id;

        public Canvas create() {
        	Pictures panel = new Pictures();
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
        Canvas canvas = new Canvas();

        HTMLFlow htmlFlow = new HTMLFlow();
        htmlFlow.setOverflow(Overflow.AUTO);
        htmlFlow.setPadding(10);
        htmlFlow.setHeight("80%");
        htmlFlow.setContents("<b> Pictures area </b>");
       
        canvas.addChild(htmlFlow);
        
        return canvas;
    }

    public String getIntro() {
        return DESCRIPTION;
    }
}
