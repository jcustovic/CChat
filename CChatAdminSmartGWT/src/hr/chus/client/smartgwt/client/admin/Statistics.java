package hr.chus.client.smartgwt.client.admin;

import java.util.Iterator;

import hr.chus.client.smartgwt.client.CChatAdminSmartGWT;
import hr.chus.client.smartgwt.client.PanelFactory;

import com.google.gwt.layout.client.Layout.Alignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.PieChart;
import com.google.gwt.visualization.client.visualizations.PieChart.Options;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * 
 * @author Jan Čustović
 *
 */
public class Statistics extends HLayout {
	
    private static final String DESCRIPTION = "Statistics";

    public static class Factory implements PanelFactory {
        
    	private String id;

        public Canvas create() {
        	Statistics panel = new Statistics();
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

        final VLayout layout = new VLayout();
        final Label label = new Label();
        Runnable onLoadCallback = new Runnable() {
        	public void run() {
        		Graphic graph = new Graphic();
        		layout.addMember(graph);
        		label.setContents("TestLK");
        	}
        };
			
		label.setHeight(10);
		label.setWidth("70%");
		label.setContents("Test");
		
		layout.addMember(label);
		
		VisualizationUtils.loadVisualizationApi(onLoadCallback, PieChart.PACKAGE);
		
		return layout;
	}

}
