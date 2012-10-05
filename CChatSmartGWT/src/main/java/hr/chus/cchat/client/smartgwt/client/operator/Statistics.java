package hr.chus.cchat.client.smartgwt.client.operator;

import hr.chus.cchat.client.smartgwt.client.common.Constants;
import hr.chus.cchat.client.smartgwt.client.common.PanelFactory;
import hr.chus.cchat.client.smartgwt.client.i18n.DictionaryInstance;

import java.util.Date;

import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.PieChart;
import com.google.gwt.visualization.client.visualizations.PieChart.Options;
import com.google.gwt.visualization.client.visualizations.Table;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.XMLTools;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.DateTimeItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 *
 */
public class Statistics extends HLayout {
	
    private static final String DESCRIPTION = "Statistics";
    private Canvas verticalPanel;

    
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
        final VLayout layout = new VLayout(5);
        
        final DataSource ds = new DataSource(Constants.CONTEXT_PATH + "operator/OperatorStatisticsListJSON");
        ds.setAutoConvertRelativeDates(false);
        ds.setDataFormat(DSDataFormat.JSON);
        final DynamicForm statisticsForm = new DynamicForm();
        statisticsForm.setWidth(300);
//		statisticsForm.setHeight(120);
		statisticsForm.setIsGroup(true);
		statisticsForm.setGroupTitle(DictionaryInstance.dictionary.statistics());
		statisticsForm.setNumCols(2);
		statisticsForm.setAutoFocus(false);
		statisticsForm.setUseAllDataSourceFields(false);

		statisticsForm.setFields(getStatisticsFormFields());
		
		final IButton searchButton = new IButton(DictionaryInstance.dictionary.search());
        searchButton.setIcon(Constants.CONTEXT_PATH + "images/find.png");
        searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) { 
				if (verticalPanel != null) layout.removeMember(verticalPanel);
				ds.fetchData(statisticsForm.getValuesAsCriteria(), new DSCallback() {
                    @Override
                    public void execute(DSResponse response, Object jsonData, DSRequest request) {
                        JSONArray value = XMLTools.selectObjects(jsonData, "/statistics");
                        if (value != null && value.size() > 0) {
                            verticalPanel = drawStatisticsPerOperator(value, (Date) statisticsForm.getValue("fromDate"), (Date) statisticsForm.getValue("toDate"));
                            layout.addMember(verticalPanel);
                            return;
                        }
                    }
                });
			}
		});
        
        final Label label = new Label();
        Runnable onLoadCallback = new Runnable() {
        	public void run() {
        		label.setContents(DictionaryInstance.dictionary.graphicsLoaded());
        	}
        };
			
		label.setHeight(10);
		label.setWidth("70%");
		label.setContents(DictionaryInstance.dictionary.graphicsLoading());
		
		layout.addMember(label);
		layout.addMember(statisticsForm);
		layout.addMember(searchButton);
		
		VisualizationUtils.loadVisualizationApi(onLoadCallback, PieChart.PACKAGE, Table.PACKAGE);
		
		return layout;
	}
    
    
    /**
     * 
     * @param value
     * @param fromDate
     * @param toDate
     * @return
     */
	private Canvas drawStatisticsPerOperator(JSONArray value, Date fromDate, Date toDate) { 
		String fromDateString = Constants.dateTimeFormat.format(fromDate, TimeZone.createTimeZone(0));
    	String toDateString = Constants.dateTimeFormat.format(toDate, TimeZone.createTimeZone(0));
    	VLayout vlayout = new VLayout();
    	vlayout.setWidth100();
    	vlayout.setHeight100();
    	
    	HorizontalPanel horizontalPanel = new HorizontalPanel();
    	horizontalPanel.setWidth("100%");
    	horizontalPanel.setHeight("100%");
    	
    	PieChart.Options sentOptions = PieChart.Options.create();
    	sentOptions.setWidth(400);
    	sentOptions.setHeight(240);
    	sentOptions.set3D(true);
    	sentOptions.setTitle(DictionaryInstance.dictionary.sent() + " " + DictionaryInstance.dictionary.statisticsPerOperator() + " (" + fromDateString + " - " + toDateString + ")");
    	
    	Options receivedOptions = Options.create();
    	receivedOptions.setWidth(400);
    	receivedOptions.setHeight(240);
    	receivedOptions.set3D(true);
    	receivedOptions.setTitle(DictionaryInstance.dictionary.received() + " " + DictionaryInstance.dictionary.statisticsPerOperator() + " (" + fromDateString + " - " + toDateString + ")");
    	
		Options sumOptions = Options.create();
		sumOptions.setWidth(400);
		sumOptions.setHeight(240);
		sumOptions.set3D(true);
		
		DataTable allData = DataTable.create();
    	allData.addColumn(ColumnType.STRING, DictionaryInstance.dictionary.direction());
    	allData.addColumn(ColumnType.NUMBER, DictionaryInstance.dictionary.sum());
    	allData.addRows(2);
    	
    	JSONObject object = (JSONObject) value.get(0);
		String operatorUsername = ((JSONString) object.get("operatorUsername")).stringValue();
		sumOptions.setTitle(operatorUsername + " - " + DictionaryInstance.dictionary.sent() + "/" + DictionaryInstance.dictionary.received());
		double receivedCount = ((JSONNumber) object.get("receivedCount")).doubleValue();
		double sentCount = ((JSONNumber) object.get("sentCount")).doubleValue();
		allData.setValue(0, 0, DictionaryInstance.dictionary.sent());
		allData.setValue(0, 1, sentCount);
		allData.setValue(1, 0, DictionaryInstance.dictionary.received());
		allData.setValue(1, 1, receivedCount);
		
		Table table = new Table(allData, Table.Options.create());
    	table.setWidth("350px");
    		
		horizontalPanel.add(new PieChart(allData, sumOptions));
	
		vlayout.addMember(table);
		vlayout.addMember(horizontalPanel);
    	
		return vlayout;
	}
	
	private FormItem[] getStatisticsFormFields() {
		final DateTimeItem fromDate = new DateTimeItem("fromDate", DictionaryInstance.dictionary.fromDate());
		final DateTimeItem toDate = new DateTimeItem("toDate", DictionaryInstance.dictionary.toDate());
		
        fromDate.setMaskDateSeparator(".");
        fromDate.setUseMask(true);
        fromDate.setUseTextField(true);
        fromDate.setRequired(true);
        fromDate.setRequiredMessage(DictionaryInstance.dictionary.fieldIsRequired());
        fromDate.setWidth(180);
        fromDate.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
        
        toDate.setMaskDateSeparator(".");
        toDate.setUseMask(true);
        toDate.setUseTextField(true);
        toDate.setRequired(true);
        toDate.setRequiredMessage(DictionaryInstance.dictionary.fieldIsRequired());
        toDate.setWidth(180);
        toDate.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
        
		return new FormItem[] { fromDate, toDate };
	}

}
