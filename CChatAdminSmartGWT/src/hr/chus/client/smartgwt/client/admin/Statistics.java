package hr.chus.client.smartgwt.client.admin;

import java.util.Date;
import java.util.LinkedHashMap;

import hr.chus.client.smartgwt.client.CChatAdminSmartGWT;
import hr.chus.client.smartgwt.client.PanelFactory;
import hr.chus.client.smartgwt.client.admin.ds.OperatorsDS;

import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.DataView;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.PieChart;
import com.google.gwt.visualization.client.visualizations.Table;
import com.google.gwt.visualization.client.visualizations.PieChart.Options;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.XMLTools;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemInputTransformer;
import com.smartgwt.client.widgets.form.fields.DateTimeItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * 
 * @author Jan Čustović
 *
 */
public class Statistics extends HLayout {
	
    private static final String DESCRIPTION = "Statistics";
    private VerticalPanel verticalPanel;
    
    
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
        
        DataSource ds = new DataSource(CChatAdminSmartGWT.CONTEXT_PATH + "admin/AdminStatisticsListJSON");
        ds.setDataFormat(DSDataFormat.JSON);
        final DynamicForm statisticsForm = new DynamicForm();
        statisticsForm.setWidth(300);
//		statisticsForm.setHeight(120);
		statisticsForm.setIsGroup(true);
		statisticsForm.setGroupTitle(CChatAdminSmartGWT.dictionary.statistics());
		statisticsForm.setNumCols(2);
		statisticsForm.setDataSource(ds);
		statisticsForm.setAutoFocus(false);

		statisticsForm.setFields(getStatisticsFormFields());
		
		IButton searchButton = new IButton(CChatAdminSmartGWT.dictionary.search());
        searchButton.setIcon(CChatAdminSmartGWT.CONTEXT_PATH + "images/find.png");
        searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (verticalPanel != null) verticalPanel.removeFromParent();
				statisticsForm.submit(new DSCallback() {
					@Override
					public void execute(DSResponse response, Object jsonData, DSRequest request) {
						JSONArray value = XMLTools.selectObjects(jsonData, "/statisticsPerServiceProviders");
						if (value != null && value.size() > 0) {
							verticalPanel = drawStatisticsPerServiceProviders(value, (Date) statisticsForm.getValue("fromDate"), (Date) statisticsForm.getValue("toDate"));
							layout.addMember(verticalPanel);
						}
					}
				});
			}
		});
        
        final Label label = new Label();
        Runnable onLoadCallback = new Runnable() {
        	public void run() {
        		label.setContents(CChatAdminSmartGWT.dictionary.graphicsLoaded());
        	}
        };
			
		label.setHeight(10);
		label.setWidth("70%");
		label.setContents(CChatAdminSmartGWT.dictionary.graphicsLoading());
		
		layout.addMember(label);
		layout.addMember(statisticsForm);
		layout.addMember(searchButton);
		
		VisualizationUtils.loadVisualizationApi(onLoadCallback, PieChart.PACKAGE, Table.PACKAGE);
		
		return layout;
	}
    
    /**
     * 
     * @param value
     * @param toDate 
     * @param fromDate 
     * @return 
     */
    private VerticalPanel drawStatisticsPerServiceProviders(JSONArray value, Date fromDate, Date toDate) {
    	String fromDateString = CChatAdminSmartGWT.dateFormat.format(fromDate, TimeZone.createTimeZone(0));
    	String toDateString = CChatAdminSmartGWT.dateFormat.format(toDate, TimeZone.createTimeZone(0));
    	VerticalPanel verticalPanel = new VerticalPanel();
    	verticalPanel.setWidth("100%");
    	verticalPanel.setHeight("100%");
    	
    	HorizontalPanel horizontalPanel = new HorizontalPanel();
    	horizontalPanel.setWidth("100%");
    	horizontalPanel.setHeight("100%");
    	
    	Options sentOptions = Options.create();
    	sentOptions.setWidth(400);
    	sentOptions.setHeight(240);
    	sentOptions.set3D(true);
    	sentOptions.setTitle(CChatAdminSmartGWT.dictionary.sent() + " " + CChatAdminSmartGWT.dictionary.statisticsPerSP() + " (" + fromDateString + " - " + toDateString + ")");
    	
    	Options receivedOptions = Options.create();
    	receivedOptions.setWidth(400);
    	receivedOptions.setHeight(240);
    	receivedOptions.set3D(true);
    	receivedOptions.setTitle(CChatAdminSmartGWT.dictionary.received() + " " + CChatAdminSmartGWT.dictionary.statisticsPerSP() + " (" + fromDateString + " - " + toDateString + ")");
		
    	DataTable allData = DataTable.create();
    	allData.addColumn(ColumnType.STRING, CChatAdminSmartGWT.dictionary.serviceProvider());
    	allData.addColumn(ColumnType.NUMBER, CChatAdminSmartGWT.dictionary.sent() + " " + CChatAdminSmartGWT.dictionary.messages());
    	allData.addColumn(ColumnType.NUMBER, CChatAdminSmartGWT.dictionary.received() + " " + CChatAdminSmartGWT.dictionary.messages());
    	allData.addRows(value.size());
    	
    	int sentTotalCount = 0;
    	int receivedTotalCount = 0;
		for (int i = 0; i < value.size(); i++) {
			JSONObject object = (JSONObject) value.get(i);
			String providerName = ((JSONString) object.get("providerName")).stringValue();
			String shortCode = ((JSONString) object.get("shortCode")).stringValue();
			double receivedCount = ((JSONNumber) object.get("receivedCount")).doubleValue();
			double sentCount = ((JSONNumber) object.get("sentCount")).doubleValue();
			sentTotalCount += sentCount;
			receivedTotalCount += receivedCount;
			allData.setValue(i, 0, providerName + " - " + shortCode);
			allData.setValue(i, 1, sentCount);
			allData.setValue(i, 2, receivedCount);
		}
		Table table = new Table(allData, Table.Options.create());
		table.setWidth("350px");
		verticalPanel.add(table);
		
		DataView sentPieChartDW = DataView.create(allData);
		sentPieChartDW.setColumns(new int[] {0, 1});
		horizontalPanel.add(new PieChart(sentPieChartDW, sentOptions));
		
		DataView receivedPieChartDW = DataView.create(allData);
		receivedPieChartDW.setColumns(new int[] {0, 2});
		horizontalPanel.add(new PieChart(receivedPieChartDW, receivedOptions));
		
		Options sumOptions = Options.create();
		sumOptions.setWidth(400);
		sumOptions.setHeight(240);
		sumOptions.set3D(true);
		sumOptions.setTitle(CChatAdminSmartGWT.dictionary.sent() + "/" + CChatAdminSmartGWT.dictionary.received());
		
		DataTable sumData = DataTable.create();
		sumData.addColumn(ColumnType.STRING, CChatAdminSmartGWT.dictionary.direction());
		sumData.addColumn(ColumnType.NUMBER, CChatAdminSmartGWT.dictionary.sum());
		sumData.addRows(2);
		sumData.setValue(0, 0, CChatAdminSmartGWT.dictionary.sent());
		sumData.setValue(0, 1, sentTotalCount);
		sumData.setValue(1, 0, CChatAdminSmartGWT.dictionary.received());
		sumData.setValue(1, 1, receivedTotalCount);
		horizontalPanel.add(new PieChart(sumData, sumOptions));
		
		verticalPanel.add(horizontalPanel);
		
		return verticalPanel;
	}

    /**
     * 
     * @return
     */
	private FormItem[] getStatisticsFormFields() {
		
		final DateTimeItem fromDate = new DateTimeItem("fromDate", CChatAdminSmartGWT.dictionary.fromDate());
		final DateTimeItem toDate = new DateTimeItem("toDate", CChatAdminSmartGWT.dictionary.toDate());
		final SelectItem statisticsType = new SelectItem("statisticsType", CChatAdminSmartGWT.dictionary.statisticsType());
		final SelectItem operatorItem = new SelectItem("operator", CChatAdminSmartGWT.dictionary.operator());
		
		statisticsType.setWidth(180);
		statisticsType.addChangedHandler(new ChangedHandler() {
		
			@Override
			public void onChanged(ChangedEvent event) {
				String statisticsType = (String) event.getValue();
				if (event.getValue() == null || statisticsType.equals("LiveStatistics")) {
					fromDate.setDisabled(true);
					toDate.setDisabled(true);
					operatorItem.setDisabled(true);
				} else {
					if (statisticsType.equals("StatisticsPerServiceProvider")) {
						fromDate.setDisabled(false);
						toDate.setDisabled(false);
						operatorItem.setDisabled(true);
					} else if (statisticsType.equals("StatisticsPerOperator")){
						fromDate.setDisabled(false);
						toDate.setDisabled(false);
						operatorItem.setDisabled(false);
					} else {
						SC.warn("Type: " + statisticsType + " is not known!");
					}
				}
			}
		});
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put("StatisticsPerServiceProvider", CChatAdminSmartGWT.dictionary.statisticsPerSP());
		valueMap.put("StatisticsPerOperator", CChatAdminSmartGWT.dictionary.statisticsPerOperator());
		valueMap.put("LiveStatistics", CChatAdminSmartGWT.dictionary.liveStatistics());
		statisticsType.setValueMap(valueMap);
		statisticsType.setAllowEmptyValue(true);
		statisticsType.setEmptyDisplayValue(CChatAdminSmartGWT.dictionary.notSet());
		
		fromDate.setDisabled(true);
        fromDate.setInputTransformer(new FormItemInputTransformer() {
			
			@SuppressWarnings("deprecation")
			@Override
			public Object transformInput(DynamicForm form, FormItem item, Object value, Object oldValue) {
				if (value == null || !(value instanceof Date)) return null;
				Date gmt = null;
				if (oldValue != null) {
					gmt = (Date) value;
				} else {
					Date date = (Date) value;
					gmt = new Date(date.getTime() - date.getTimezoneOffset() * 60 * 1000);
				}
				return CChatAdminSmartGWT.dateFormat.format(gmt, TimeZone.createTimeZone(0));
			}
		});
        fromDate.setMaskDateSeparator(".");
        fromDate.setUseMask(true);
        fromDate.setUseTextField(true);
        fromDate.setRequired(true);
        fromDate.setWidth(180);
        fromDate.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
        
        toDate.setDisabled(true);
        toDate.setInputTransformer(new FormItemInputTransformer() {
			
			@SuppressWarnings("deprecation")
			@Override
			public Object transformInput(DynamicForm form, FormItem item, Object value, Object oldValue) {
				if (value == null || !(value instanceof Date)) return null;
				Date gmt = null;
				if (oldValue != null) {
					gmt = (Date) value;
				} else {
					Date date = (Date) value;
					gmt = new Date(date.getTime() - date.getTimezoneOffset() * 60 * 1000);
				}
				return CChatAdminSmartGWT.dateFormat.format(gmt, TimeZone.createTimeZone(0));
			}
		});
        toDate.setMaskDateSeparator(".");
        toDate.setUseMask(true);
        toDate.setUseTextField(true);
        toDate.setRequired(true);
        toDate.setWidth(180);
        toDate.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
        
        operatorItem.setDisabled(true);
        operatorItem.setAllowEmptyValue(true);
        operatorItem.setEmptyDisplayValue(CChatAdminSmartGWT.dictionary.notSet());
        operatorItem.setOptionDataSource(OperatorsDS.getInstance());
        operatorItem.setDisplayField("username");
        operatorItem.setValueField("id");
	        
		return new FormItem[] { statisticsType, fromDate, toDate, operatorItem };
	}

}
