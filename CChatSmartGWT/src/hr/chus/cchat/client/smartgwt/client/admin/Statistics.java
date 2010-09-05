package hr.chus.cchat.client.smartgwt.client.admin;

import java.util.Date;
import java.util.LinkedHashMap;

import hr.chus.cchat.client.smartgwt.client.admin.ds.OperatorsDS;
import hr.chus.cchat.client.smartgwt.client.common.PanelFactory;
import hr.chus.cchat.client.smartgwt.client.i18n.DictionaryInstance;

import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HorizontalPanel;
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
    private VLayout verticalPanel;
    private Timer refreshTimer;
    
    
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
        ds.setAutoConvertRelativeDates(false);
        ds.setDataFormat(DSDataFormat.JSON);
        final DynamicForm statisticsForm = new DynamicForm();
        statisticsForm.setWidth(300);
//		statisticsForm.setHeight(120);
		statisticsForm.setIsGroup(true);
		statisticsForm.setGroupTitle(DictionaryInstance.dictionary.statistics());
		statisticsForm.setNumCols(2);
		statisticsForm.setDataSource(ds);
		statisticsForm.setAutoFocus(false);
		statisticsForm.setUseAllDataSourceFields(false);


		statisticsForm.setFields(getStatisticsFormFields());
		
		final IButton searchButton = new IButton(DictionaryInstance.dictionary.search());
        searchButton.setIcon(CChatAdminSmartGWT.CONTEXT_PATH + "images/find.png");
        searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (verticalPanel != null) layout.removeMember(verticalPanel);
				if (statisticsForm.getValueAsString("statisticsType").equals("LiveStatistics")) {
					if (refreshTimer != null) refreshTimer.cancel();
					refreshTimer = new Timer() {
						
						@Override
						public void run() {
							searchButton.fireEvent(new ClickEvent(null));
						}
					};
					refreshTimer.schedule(1000 * 60 * 2); // 2 minutes
				}
				statisticsForm.submit(new DSCallback() {
					@Override
					public void execute(DSResponse response, Object jsonData, DSRequest request) {
						JSONArray value = XMLTools.selectObjects(jsonData, "/statisticsPerServiceProviders");
						if (value != null && value.size() > 0) {
							if (statisticsForm.getValueAsString("statisticsType").equals("LiveStatistics")) {
								
								verticalPanel = drawStatisticsPerServiceProviders(value, null, null);
							} else {
								verticalPanel = drawStatisticsPerServiceProviders(value, (Date) statisticsForm.getValue("fromDate"), (Date) statisticsForm.getValue("toDate"));
							}
							layout.addMember(verticalPanel);
							return;
						}
						value = XMLTools.selectObjects(jsonData, "/statisticsPerOperators");
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
     * @param toDate 
     * @param fromDate 
     * @return 
     */
    private VLayout drawStatisticsPerServiceProviders(JSONArray value, Date fromDate, Date toDate) {
    	String fromDateString = null;
    	String toDateString = null;
    	if (fromDate != null && toDate != null) {
	    	fromDateString = CChatAdminSmartGWT.dateTimeFormat.format(fromDate, TimeZone.createTimeZone(0));
	    	toDateString = CChatAdminSmartGWT.dateTimeFormat.format(toDate, TimeZone.createTimeZone(0));
    	}
    	VLayout vlayout = new VLayout();
    	vlayout.setWidth100();
    	vlayout.setHeight100();
    	
    	HorizontalPanel horizontalPanel = new HorizontalPanel();
    	horizontalPanel.setWidth("100%");
    	horizontalPanel.setHeight("100%");
    	
    	Options sentOptions = Options.create();
    	sentOptions.setWidth(400);
    	sentOptions.setHeight(240);
    	sentOptions.set3D(true);
    	if (fromDateString != null && toDateString != null) {
    		sentOptions.setTitle(DictionaryInstance.dictionary.sent() + " " + DictionaryInstance.dictionary.statisticsPerSP() + " (" + fromDateString + " - " + toDateString + ")");
    	} else {
    		sentOptions.setTitle(DictionaryInstance.dictionary.sent() + " " + DictionaryInstance.dictionary.statisticsPerSP() + " (" + DictionaryInstance.dictionary.today() + ")");
    	}
    	
    	Options receivedOptions = Options.create();
    	receivedOptions.setWidth(400);
    	receivedOptions.setHeight(240);
    	receivedOptions.set3D(true);
    	if (fromDateString != null && toDateString != null) {
    		receivedOptions.setTitle(DictionaryInstance.dictionary.received() + " " + DictionaryInstance.dictionary.statisticsPerSP() + " (" + fromDateString + " - " + toDateString + ")");
    	} else {
    		receivedOptions.setTitle(DictionaryInstance.dictionary.received() + " " + DictionaryInstance.dictionary.statisticsPerSP() + " (" + DictionaryInstance.dictionary.today() + ")");
    	}
		
    	DataTable allData = DataTable.create();
    	allData.addColumn(ColumnType.STRING, DictionaryInstance.dictionary.serviceProvider());
    	allData.addColumn(ColumnType.NUMBER, DictionaryInstance.dictionary.sent() + " " + DictionaryInstance.dictionary.messages());
    	allData.addColumn(ColumnType.NUMBER, DictionaryInstance.dictionary.received() + " " + DictionaryInstance.dictionary.messages());
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
		vlayout.addMember(table);
		
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
		sumOptions.setTitle(DictionaryInstance.dictionary.sent() + "/" + DictionaryInstance.dictionary.received());
		
		DataTable sumData = DataTable.create();
		sumData.addColumn(ColumnType.STRING, DictionaryInstance.dictionary.direction());
		sumData.addColumn(ColumnType.NUMBER, DictionaryInstance.dictionary.sum());
		sumData.addRows(2);
		sumData.setValue(0, 0, DictionaryInstance.dictionary.sent());
		sumData.setValue(0, 1, sentTotalCount);
		sumData.setValue(1, 0, DictionaryInstance.dictionary.received());
		sumData.setValue(1, 1, receivedTotalCount);
		horizontalPanel.add(new PieChart(sumData, sumOptions));
		
		vlayout.addMember(horizontalPanel);
		
		return vlayout;
	}
    
    /**
     * 
     * @param value
     * @param fromDate
     * @param toDate
     * @return
     */
	private VLayout drawStatisticsPerOperator(JSONArray value, Date fromDate, Date toDate) {
		String fromDateString = CChatAdminSmartGWT.dateTimeFormat.format(fromDate, TimeZone.createTimeZone(0));
    	String toDateString = CChatAdminSmartGWT.dateTimeFormat.format(toDate, TimeZone.createTimeZone(0));
    	VLayout vlayout = new VLayout();
    	vlayout.setWidth100();
    	vlayout.setHeight100();
    	
    	HorizontalPanel horizontalPanel = new HorizontalPanel();
    	horizontalPanel.setWidth("100%");
    	horizontalPanel.setHeight("100%");
    	
    	Options sentOptions = Options.create();
    	sentOptions.setWidth(400);
    	sentOptions.setHeight(240);
    	sentOptions.set3D(true);
    	sentOptions.setTitle(DictionaryInstance.dictionary.sent() + " " + DictionaryInstance.dictionary.statisticsPerOperator() + " (" + fromDateString + " - " + toDateString + ")");
    	
    	Options receivedOptions = Options.create();
    	receivedOptions.setWidth(400);
    	receivedOptions.setHeight(240);
    	receivedOptions.set3D(true);
    	receivedOptions.setTitle(DictionaryInstance.dictionary.received() + " " + DictionaryInstance.dictionary.statisticsPerOperator() + " (" + fromDateString + " - " + toDateString + ")");
    	
    	if (value.size() == 1) {
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
			horizontalPanel.add(new PieChart(allData, sumOptions));
	    	
    	} else {
	    	DataTable allData = DataTable.create();
	    	allData.addColumn(ColumnType.STRING, DictionaryInstance.dictionary.operator());
	    	allData.addColumn(ColumnType.NUMBER, DictionaryInstance.dictionary.sent() + " " + DictionaryInstance.dictionary.messages());
	    	allData.addColumn(ColumnType.NUMBER, DictionaryInstance.dictionary.received() + " " + DictionaryInstance.dictionary.messages());
	    	allData.addRows(value.size());
	    	
			for (int i = 0; i < value.size(); i++) {
				JSONObject object = (JSONObject) value.get(i);
				String operatorUsername = ((JSONString) object.get("operatorUsername")).stringValue();
				double receivedCount = ((JSONNumber) object.get("receivedCount")).doubleValue();
				double sentCount = ((JSONNumber) object.get("sentCount")).doubleValue();
				allData.setValue(i, 0, operatorUsername);
				allData.setValue(i, 1, sentCount);
				allData.setValue(i, 2, receivedCount);
			}
			Table table = new Table(allData, Table.Options.create());
			table.setWidth("350px");
			vlayout.addMember(table);
			
			DataView sentPieChartDW = DataView.create(allData);
			sentPieChartDW.setColumns(new int[] {0, 1});
			horizontalPanel.add(new PieChart(sentPieChartDW, sentOptions));
			
			DataView receivedPieChartDW = DataView.create(allData);
			receivedPieChartDW.setColumns(new int[] {0, 2});
			horizontalPanel.add(new PieChart(receivedPieChartDW, receivedOptions));
    	}
		
		vlayout.addMember(horizontalPanel);
    	
		return vlayout;
	}

    /**
     * 
     * @return
     */
	private FormItem[] getStatisticsFormFields() {
		
		final DateTimeItem fromDate = new DateTimeItem("fromDate", DictionaryInstance.dictionary.fromDate());
		final DateTimeItem toDate = new DateTimeItem("toDate", DictionaryInstance.dictionary.toDate());
		final SelectItem statisticsType = new SelectItem("statisticsType", DictionaryInstance.dictionary.statisticsType());
		final SelectItem operatorItem = new SelectItem("operator", DictionaryInstance.dictionary.operator());
		
		statisticsType.setWidth(180);
		statisticsType.addChangedHandler(new ChangedHandler() {
		
			@Override
			public void onChanged(ChangedEvent event) {
				String statisticsType = (String) event.getValue();
				if (event.getValue() == null) {
					if (refreshTimer != null) refreshTimer.cancel();
					fromDate.setDisabled(true);
					toDate.setDisabled(true);
					operatorItem.setDisabled(true);
				} else if (statisticsType.equals("LiveStatistics")) {
					fromDate.setValue(new Date());
					toDate.setValue(new Date());
					fromDate.setDisabled(true);
					toDate.setDisabled(true);
					operatorItem.setDisabled(true);
				} else {
					if (refreshTimer != null) refreshTimer.cancel();
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
		valueMap.put("StatisticsPerServiceProvider", DictionaryInstance.dictionary.statisticsPerSP());
		valueMap.put("StatisticsPerOperator", DictionaryInstance.dictionary.statisticsPerOperator());
		valueMap.put("LiveStatistics", DictionaryInstance.dictionary.liveStatistics());
		statisticsType.setValueMap(valueMap);
		statisticsType.setAllowEmptyValue(true);
		statisticsType.setEmptyDisplayValue(DictionaryInstance.dictionary.notSet());
		
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
					gmt = new Date(date.getTime() - date.getTimezoneOffset() * 60000);
				}
				return CChatAdminSmartGWT.dateTimeFormat.format(gmt, TimeZone.createTimeZone(0));
			}
		});
        fromDate.setMaskDateSeparator(".");
        fromDate.setUseMask(true);
        fromDate.setUseTextField(true);
        fromDate.setRequired(true);
        fromDate.setRequiredMessage(DictionaryInstance.dictionary.fieldIsRequired());
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
					gmt = new Date(date.getTime() - date.getTimezoneOffset() * 60000);
				}
				return CChatAdminSmartGWT.dateTimeFormat.format(gmt, TimeZone.createTimeZone(0));
			}
		});
        toDate.setMaskDateSeparator(".");
        toDate.setUseMask(true);
        toDate.setUseTextField(true);
        toDate.setRequired(true);
        toDate.setRequiredMessage(DictionaryInstance.dictionary.fieldIsRequired());
        toDate.setWidth(180);
        toDate.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
        
        operatorItem.setDisabled(true);
        operatorItem.setAllowEmptyValue(true);
        operatorItem.setEmptyDisplayValue(DictionaryInstance.dictionary.notSet());
        operatorItem.setOptionDataSource(OperatorsDS.getInstance());
        operatorItem.setDisplayField("username");
        operatorItem.setValueField("id");
	        
		return new FormItem[] { statisticsType, fromDate, toDate, operatorItem };
	}

}
