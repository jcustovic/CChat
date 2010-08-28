package hr.chus.client.smartgwt.client.admin;

import hr.chus.client.smartgwt.client.CChatAdminSmartGWT;
import hr.chus.client.smartgwt.client.PanelFactory;
import hr.chus.client.smartgwt.client.admin.ds.MessagesDS;
import hr.chus.client.smartgwt.client.admin.ds.NicksDS;
import hr.chus.client.smartgwt.client.admin.ds.OperatorsDS;
import hr.chus.client.smartgwt.client.admin.ds.ServiceProviderDS;

import java.util.Date;
import java.util.LinkedHashMap;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.types.ListGridFieldType;
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
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class Messages extends HLayout {
	
    private static final String DESCRIPTION = "Messages";
    
    private int offset = 0;
    private int fetchSize = 0;
    private Criteria criteria = null;
    
    public static class Factory implements PanelFactory {
        
    	private String id;

        public Canvas create() {
        	Messages panel = new Messages();
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

    	NicksDS.getInstance().invalidateCache();
    	OperatorsDS.getInstance().invalidateCache();
    	ServiceProviderDS.getInstance().invalidateCache();
    	
		VLayout layout = new VLayout(20);
		layout.setWidth(1000);
//		layout.setHeight100();
		
		Label label = new Label();
		label.setHeight(10);
//		label.setWidth("70%");
		label.setContents(CChatAdminSmartGWT.dictionary.messages());
		layout.addMember(label);
						
		final Label listLabel = new Label();
		listLabel.setVisible(false);
		listLabel.setHeight("10px");

		final ListGrid listGrid = new ListGrid() {
			@Override
			protected String getBaseStyle(ListGridRecord record, int rowNum, int colNum) {
				String direction = record.getAttribute("smsMessage.direction");
				if (direction != null) {
					if (direction.equals("IN")) return "inboundMsgGridRecord";
					else if (direction.equals("OUT")) return "outboundMsgGridRecord";
					else return super.getBaseStyle(record, rowNum, colNum);
				} else {
					return super.getBaseStyle(record, rowNum, colNum);
				}
			}
		};
		listGrid.setLoadingDataMessage(CChatAdminSmartGWT.dictionary.loading());
		listGrid.setEmptyMessage(CChatAdminSmartGWT.dictionary.emptySet());
		listGrid.setLoadingMessage(CChatAdminSmartGWT.dictionary.loading());
		listGrid.setHeight(300);
		listGrid.setDataSource(MessagesDS.getInstance());
		listGrid.setAutoFetchData(false);
		listGrid.setAnimateRemoveRecord(true);
		listGrid.setUseAllDataSourceFields(false);
		listGrid.setFields(getGridFields());
		listGrid.setWrapCells(true);  
		listGrid.setCellHeight(60);
		
		final DynamicForm searchForm = new DynamicForm();
//        searchForm.setWidth("60%");
		searchForm.setHeight(120);
		searchForm.setIsGroup(true);
		
        searchForm.setGroupTitle(CChatAdminSmartGWT.dictionary.search());
        searchForm.setNumCols(6);
        searchForm.setDataSource(MessagesDS.getInstance());
        searchForm.setAutoFocus(false);

        searchForm.setFields(getSearchFormFields());
        
        final IButton nextButton = new IButton(CChatAdminSmartGWT.dictionary.next());
        nextButton.setIcon(CChatAdminSmartGWT.CONTEXT_PATH + "images/arrow_right_green.png");
        final IButton previousButton = new IButton(CChatAdminSmartGWT.dictionary.previous());
        previousButton.setIcon(CChatAdminSmartGWT.CONTEXT_PATH + "images/arrow_left_green.png");
        
        nextButton.setVisible(false);
        nextButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				offset += fetchSize;
				criteria.setAttribute("limit", fetchSize);
				criteria.setAttribute("start", offset);
				listGrid.invalidateCache();
				listGrid.fetchData(criteria, new DSCallback() {
					
					@Override
					public void execute(DSResponse response, Object rawData, DSRequest request) {
						int totalCount = (int) MessagesDS.getInstance().getTotalCount();
						listLabel.setContents("Total sms messages found in database: <b>" + totalCount + "&nbsp;&nbsp;(" + offset + " - " + (offset + fetchSize) + ")</b>");
						listLabel.setVisible(true);
						if ((offset + fetchSize) >= totalCount) nextButton.setVisible(false);
						previousButton.setVisible(true);
					}
				});
			}
        });
        
        previousButton.setVisible(false);
        previousButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				offset -= fetchSize;
				criteria.setAttribute("limit", fetchSize);
				criteria.setAttribute("start", offset);
				listGrid.invalidateCache();
				listGrid.fetchData(criteria, new DSCallback() {
					
					@Override
					public void execute(DSResponse response, Object rawData, DSRequest request) {
						int totalCount = (int) MessagesDS.getInstance().getTotalCount();
						listLabel.setContents("Total sms messages found in database: <b>" + totalCount + "&nbsp;&nbsp;(" + offset + " - " + (offset + fetchSize) + ")</b>");
						if (offset <= 0) previousButton.setVisible(false);
						listLabel.setVisible(true);
						nextButton.setVisible(true);
					}
				});
			}
        });

        IButton searchButton = new IButton(CChatAdminSmartGWT.dictionary.search());
        searchButton.setIcon(CChatAdminSmartGWT.CONTEXT_PATH + "images/find.png");
        searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				fetchSize = Integer.valueOf(searchForm.getValueAsString("limit"));
				offset = 0;
				criteria = searchForm.getValuesAsCriteria();
				listGrid.invalidateCache();
				listGrid.fetchData(searchForm.getValuesAsCriteria(), new DSCallback() {
					
					@Override
					public void execute(DSResponse response, Object rawData, DSRequest request) {
						int totalCount = (int) MessagesDS.getInstance().getTotalCount();
						listLabel.setContents("Total sms messages found in database: <b>" + totalCount + "&nbsp;&nbsp;(" + offset + " - " + (offset + fetchSize) + ")</b>");
						listLabel.setVisible(true);
						if (totalCount > fetchSize) {
							nextButton.setVisible(true);
						} else {
							nextButton.setVisible(false);
						}
						previousButton.setVisible(false);
					}
				});
			}
		});
        

        VLayout searchLayout = new VLayout(5);
        searchLayout.addMember(searchForm);
        
        HLayout searchPagingLayout = new HLayout(5);
        searchPagingLayout.addMember(searchButton);
        searchPagingLayout.addMember(previousButton);
        searchPagingLayout.addMember(nextButton);
        
        searchLayout.addMember(searchPagingLayout);
		layout.addMember(searchLayout);
		
		VLayout listLayout = new VLayout(0);
		listLayout.addMember(listLabel);
		listLayout.addMember(listGrid);
		layout.addMember(listLayout);
				
		canvas.addChild(layout);
        return canvas;
    }


	/**
     * 
     * @return
     */
    private FormItem[] getSearchFormFields() {
        TextItem msisdn = new TextItem("msisdn", CChatAdminSmartGWT.dictionary.msisdn());
        TextAreaItem text = new TextAreaItem("text", CChatAdminSmartGWT.dictionary.text());
        
        final DateTimeItem startDate = new DateTimeItem("startDate", CChatAdminSmartGWT.dictionary.receivedTimeStart());
        startDate.setInputTransformer(new FormItemInputTransformer() {
			
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
        startDate.setMaskDateSeparator(".");
        startDate.setUseMask(true);
        startDate.setUseTextField(true);
        startDate.setWidth(180);
        startDate.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
        
        DateTimeItem endDate = new DateTimeItem("endDate", CChatAdminSmartGWT.dictionary.receivedTimeEnd());
        endDate.setInputTransformer(new FormItemInputTransformer() {
			
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
        endDate.setMaskDateSeparator(".");
        endDate.setUseMask(true);
        endDate.setUseTextField(true);
        endDate.setWidth(180);
        endDate.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
        
        SelectItem operatorItem = new SelectItem("operator", CChatAdminSmartGWT.dictionary.operator());
        operatorItem.setAllowEmptyValue(true);
        operatorItem.setEmptyDisplayValue(CChatAdminSmartGWT.dictionary.notSet());
        operatorItem.setOptionDataSource(OperatorsDS.getInstance());
        operatorItem.setDisplayField("username");
        operatorItem.setValueField("id");
        
        SelectItem serviceProviderItem = new SelectItem("serviceProvider", CChatAdminSmartGWT.dictionary.serviceProvider());
        serviceProviderItem.setAllowEmptyValue(true);
        serviceProviderItem.setEmptyDisplayValue(CChatAdminSmartGWT.dictionary.notSet());
        serviceProviderItem.setOptionDataSource(ServiceProviderDS.getInstance());
        serviceProviderItem.setDisplayField("providerName");
        serviceProviderItem.setValueField("id");
        
        SelectItem direction = new SelectItem("direction", CChatAdminSmartGWT.dictionary.direction());
        LinkedHashMap<String, Object> directionMap = new LinkedHashMap<String, Object>(2);
        directionMap.put("OUT", CChatAdminSmartGWT.dictionary.sent());
        directionMap.put("IN", CChatAdminSmartGWT.dictionary.received());
        direction.setValueMap(directionMap);
        direction.setAllowEmptyValue(true);
        direction.setEmptyDisplayValue(CChatAdminSmartGWT.dictionary.notSet());
        
        SelectItem fetchSize = new SelectItem("limit", CChatAdminSmartGWT.dictionary.fetchSize());
        fetchSize.setValueMap("20", "50", "100", "200");
        fetchSize.setDefaultToFirstOption(true);
        
        return new FormItem[] { msisdn, text, startDate, endDate, operatorItem, serviceProviderItem, direction, fetchSize };
	}

	/**
     * 
     * @return
     */
    private ListGridField[] getGridFields() {
    	ListGridField msisdn = new ListGridField("smsMessage.msisdn");
    	msisdn.setWidth(110);
		ListGridField operatorUsername = new ListGridField("operator.username");
		operatorUsername.setWidth(90);
		ListGridField serviceProviderName = new ListGridField("serviceProvider.providerName");
		serviceProviderName.setWidth(100);
				
		final DateTimeFormat timeFormatter = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm");
		ListGridField time = new ListGridField("smsMessage.time");
		time.setCellFormatter(new CellFormatter() {
            public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
                if (value != null) {
                    try {
                        Date dateValue = (Date) value;
                        return timeFormatter.format(dateValue, TimeZone.createTimeZone(0));
                    } catch (Exception e) {
                        return value.toString();
                    }
                } else {
                    return "";
                }
            }
        });
		time.setType(ListGridFieldType.DATE);
		time.setAlign(Alignment.LEFT);
		time.setWidth(120);
		
		ListGridField sc = new ListGridField("smsMessage.sc");
		sc.setWidth(100);
		
		ListGridField direction = new ListGridField("smsMessage.direction");
		direction.setWidth(80);
		direction.setCellFormatter(new CellFormatter() {
			
			@Override
			public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
				if (value != null) {
                    try {
                        String direct = (String) value;
                        if (direct.equals("IN")) return CChatAdminSmartGWT.dictionary.received();
                        else if (direct.equals("OUT")) return CChatAdminSmartGWT.dictionary.sent();
                        else return value.toString();
                    } catch (Exception e) {
                        return value.toString();
                    }
                } else {
                    return "";
                }
			}
		});
		
		ListGridField text = new ListGridField("smsMessage.text");
		text.setWidth("*");
		
		return new ListGridField[] { msisdn, operatorUsername, serviceProviderName, sc, direction, time, text };
	}

}
