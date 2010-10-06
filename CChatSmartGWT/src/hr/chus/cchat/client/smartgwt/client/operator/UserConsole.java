package hr.chus.cchat.client.smartgwt.client.operator;

import java.util.Date;

import hr.chus.cchat.client.smartgwt.client.common.Constants;
import hr.chus.cchat.client.smartgwt.client.common.PanelFactory;
import hr.chus.cchat.client.smartgwt.client.i18n.DictionaryInstance;
import hr.chus.cchat.client.smartgwt.client.operator.ds.ConversationDS;
import hr.chus.cchat.client.smartgwt.client.operator.ds.NicksDS;
import hr.chus.cchat.client.smartgwt.client.operator.ds.OperatorsDS;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.XMLTools;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemInputTransformer;
import com.smartgwt.client.widgets.form.fields.BooleanItem;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.DateTimeItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
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
public class UserConsole extends HLayout {
	
	private static final String DESCRIPTION = "UserConsole";
	
	private String userId;
	private String userType;
	private String nameToDisplay;
	private ListGrid conversationListGrid;
	private int offset = 0;
    private int fetchSize = 50;
    private IButton nextButton;
    private IButton previousButton;
    private Label listLabel;
    private Criteria criteria;

    
	public static class Factory implements PanelFactory {
        
		private String id;
		private String userType;
		private String nameToDisplay;

        public Factory(String userId, String userType, String nameToDisplay) {
			this.id = userId;
			this.userType = userType;
			this.nameToDisplay = nameToDisplay;
		}

		public Canvas create() {
			UserConsole panel = new UserConsole();
			panel.setUserId(id);
			panel.setUserType(userType);
			panel.setNameToDisplay(nameToDisplay);
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
		VLayout vlayout = new VLayout(20);
		
		VLayout conversationLayout = new VLayout(8);
		HLayout conversationUserInfoLayout = new HLayout(20);
		
		listLabel = new Label();
		listLabel.setVisible(false);
		listLabel.setWidth(600);
		listLabel.setHeight("10px");
		
		conversationListGrid = new ListGrid() {
			@Override
			protected String getBaseStyle(ListGridRecord record, int rowNum, int colNum) {
				String direction = record.getAttribute("direction");
				if (direction != null) {
					if (direction.equals("IN")) return "inboundMsgGridRecord";
					else if (direction.equals("OUT")) return "outboundMsgGridRecord";
					else return super.getBaseStyle(record, rowNum, colNum);
				} else {
					return super.getBaseStyle(record, rowNum, colNum);
				}
			}
		};
		conversationListGrid.setLoadingDataMessage(DictionaryInstance.dictionary.loading());
		conversationListGrid.setEmptyMessage(DictionaryInstance.dictionary.emptySet());
		conversationListGrid.setLoadingMessage(DictionaryInstance.dictionary.loading());
		conversationListGrid.setDataSource(ConversationDS.getInstance());
		conversationListGrid.setAutoFetchData(false);
		conversationListGrid.setWidth(620);
		conversationListGrid.setHeight(300);
		conversationListGrid.setUseAllDataSourceFields(false);
		conversationListGrid.setFields(getConversationFields());
		conversationListGrid.setFixedRecordHeights(false);
		
		nextButton = new IButton(DictionaryInstance.dictionary.next());
        nextButton.setIcon(Constants.CONTEXT_PATH + "images/arrow_right_green.png");
        previousButton = new IButton(DictionaryInstance.dictionary.previous());
        previousButton.setIcon(Constants.CONTEXT_PATH + "images/arrow_left_green.png");
        
        nextButton.setVisible(false);
        nextButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				offset += fetchSize;
				criteria.setAttribute("start", offset);
				conversationListGrid.invalidateCache();
				conversationListGrid.fetchData(criteria, new DSCallback() {
					
					@Override
					public void execute(DSResponse response, Object rawData, DSRequest request) {
						int totalCount = (int) ConversationDS.getInstance().getTotalCount();
						listLabel.setContents(DictionaryInstance.dictionary.totalUsersInDB() + ": <b>" + totalCount + "&nbsp;&nbsp;(" + offset + " - " + (offset + fetchSize) + ")</b>");
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
				criteria.setAttribute("start", offset);
				conversationListGrid.invalidateCache();
				conversationListGrid.fetchData(criteria, new DSCallback() {
					
					@Override
					public void execute(DSResponse response, Object rawData, DSRequest request) {
						int totalCount = (int) ConversationDS.getInstance().getTotalCount();
						listLabel.setContents(DictionaryInstance.dictionary.totalUsersInDB() + ": <b>" + totalCount + "&nbsp;&nbsp;(" + offset + " - " + (offset + fetchSize) + ")</b>");
						listLabel.setVisible(true);
						if (offset <= 0) previousButton.setVisible(false);
						nextButton.setVisible(true);
					}
				});
			}
        });
        
		Label userFormLabel = new Label();
		userFormLabel.setHeight(10);
		userFormLabel.setWidth(200);
		userFormLabel.setContents(DictionaryInstance.dictionary.editUser());
        
        final DynamicForm userForm = new DynamicForm();
        userForm.setIsGroup(true);
        userForm.setGroupTitle(DictionaryInstance.dictionary.update());
        userForm.setNumCols(4);
        userForm.setFields(getFormFields());
		
		DataSource ds = new DataSource(Constants.CONTEXT_PATH + "operator/OperatorUserFunctionJSON") {
						
			@Override
			protected void transformResponse(DSResponse response, DSRequest request, Object jsonData) {
//				JSONArray value = XMLTools.selectObjects(jsonData, "/status");
//				String status = ((JSONString) value.get(0)).stringValue();
//				if (!status.equals("validation_ok")) {
//					response.setStatus(RPCResponse.STATUS_VALIDATION_ERROR);
//					JSONArray errors = XMLTools.selectObjects(jsonData, "/errorFields");
//					response.setErrors(errors.getJavaScriptObject());
//				}
				JSONArray value = XMLTools.selectObjects(jsonData, "/user");
				JSONObject user = (JSONObject) value.get(0);
				Record userRecord = new Record();
				for (String key : user.keySet()) {
					JavaScriptObject attribute = JSOHelper.getAttributeAsJavaScriptObject(user.getJavaScriptObject(), key);
					userRecord.setAttribute("user." + key, attribute);
				}
				
				userForm.editRecord(userRecord);
				SC.say(userRecord.getAttribute("user.operator"));
				super.transformResponse(response, request, jsonData);
			}
		};
		ds.setDataFormat(DSDataFormat.JSON);
//		ds.setRecordXPath(null);
		userForm.setDataSource(ds);
		userForm.setUseAllDataSourceFields(false);
		
		Criteria userFormCriteria = new Criteria("user", userId);
		userFormCriteria.addCriteria("operation", "get");
		ds.fetchData(userFormCriteria);
//		userForm.setInitialCriteria(userFormCriteria);
//		userForm.setAutoFetchData(true);
		
//		ds.fetchData(userFormCriteria);
		
		VLayout userFormlistLayout = new VLayout(5);
		userFormlistLayout.addMember(userFormLabel);
		userFormlistLayout.addMember(userForm);
        
        HLayout searchPagingLayout = new HLayout(5);
        searchPagingLayout.addMember(previousButton);
        searchPagingLayout.addMember(nextButton);
        
        conversationLayout.addMember(searchPagingLayout);
        
        VLayout listLayout = new VLayout(0);
		listLayout.addMember(listLabel);
		listLayout.addMember(conversationListGrid);
		
        conversationLayout.addMember(listLayout);
        
		conversationUserInfoLayout.addMember(conversationLayout);
		conversationUserInfoLayout.addMember(userFormlistLayout);
		
		vlayout.addMember(conversationUserInfoLayout);
		
		HTMLFlow mainHtmlFlow = new HTMLFlow();
		mainHtmlFlow.setOverflow(Overflow.AUTO);
		mainHtmlFlow.setPadding(10);
		mainHtmlFlow.setHeight("50");
		mainHtmlFlow.setContents("<b> " + userId + " </b>");
		
		vlayout.addMember(mainHtmlFlow);
		
		canvas.addChild(vlayout);
		return canvas;
    }
	
	private ListGridField[] getConversationFields() {
		ListGridField text = new ListGridField("text");
		text.setAlign(Alignment.CENTER);
    	text.setWidth(380);
    	
    	ListGridField operator = new ListGridField("operator");
    	operator.setAlign(Alignment.CENTER);
    	operator.setWidth(100);
    	
		final DateTimeFormat dateTime = DateTimeFormat.getFormat("dd.MM.yyyy hh:mm:ss");
		ListGridField time = new ListGridField("time");
		time.setAlign(Alignment.LEFT);
		time.setCellFormatter(new CellFormatter() {
            public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
                if (value != null) {
                    try {
                        Date dateValue = (Date) value;
                        return dateTime.format(dateValue);
                    } catch (Exception e) {
                        return value.toString();
                    }
                } else {
                    return "";
                }
            }
        });
		time.setWidth(120);
		
		return new ListGridField[] { time, operator, text };
	}

	public void loadConversation(boolean setMessagesAsRead) {
		criteria = new Criteria("userId", userId);
		criteria.setAttribute("limit", fetchSize);
		criteria.setAttribute("start", 0);
		criteria.setAttribute("setMessagesAsRead", setMessagesAsRead);
		if (!conversationListGrid.willFetchData(criteria)) conversationListGrid.invalidateCache();
		conversationListGrid.fetchData(criteria, new DSCallback() {
			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				int totalCount = (int) ConversationDS.getInstance().getTotalCount();
				listLabel.setContents(DictionaryInstance.dictionary.totalUsersInDB() + ": <b>" + totalCount + "&nbsp;&nbsp;(" + offset + " - " + (offset + fetchSize) + ")</b>");
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
	
    private FormItem[] getFormFields() {
    	TextItem id = new TextItem("user.id", "ID");
//    	id.setDataPath("user/id");
    	id.setDisabled(true);
        TextItem name = new TextItem("user.name", DictionaryInstance.dictionary.name());
//        name.setDataPath("user/name");
        TextItem surname = new TextItem("user.surname", DictionaryInstance.dictionary.surname());
//        surname.setDataPath("user/surname");
        TextItem address = new TextItem("user.address", DictionaryInstance.dictionary.address());
//        address.setDataPath("user/address");
        TextAreaItem notes = new TextAreaItem("user.notes", DictionaryInstance.dictionary.notes());
//        notes.setDataPath("user/notes");

        SelectItem nickItem = new SelectItem("user.nick", DictionaryInstance.dictionary.nick());
//        nickItem.setDataPath("user/nick");
        nickItem.setAllowEmptyValue(true);
        nickItem.setEmptyDisplayValue(DictionaryInstance.dictionary.notSet());
        nickItem.setOptionDataSource(NicksDS.getInstance());
        nickItem.setDisplayField("name");
        nickItem.setValueField("id");
        
        SelectItem operatorItem = new SelectItem("user.operator", DictionaryInstance.dictionary.operator());
        operatorItem.setDataPath("user/operator");
        operatorItem.setAllowEmptyValue(true);
        operatorItem.setEmptyDisplayValue(DictionaryInstance.dictionary.notSet());
        operatorItem.setOptionDataSource(OperatorsDS.getInstance());
        operatorItem.setDisplayField("username");
        operatorItem.setValueField("id");
        
        TextItem serviceProvider = new TextItem("user.serviceProvider");
//        serviceProvider.setDataPath("user/serviceProvider");
        serviceProvider.setVisible(false);
        
        final DateItem birthdate = new DateItem("user.birthDate", DictionaryInstance.dictionary.birthDate());
        birthdate.setInputTransformer(new FormItemInputTransformer() {
			
			@SuppressWarnings("deprecation")
			@Override
			public Object transformInput(DynamicForm form, FormItem item, Object value, Object oldValue) {
				if (value == null || !(value instanceof Date)) return null;
				Date date = (Date) value;
				Date gmt = new Date(date.getTime() - date.getTimezoneOffset() * 60 * 1000);
				return Constants.dateTimeFormat.format(gmt);
			}
		});
//        birthdate.setDataPath("user/birthDate");
        birthdate.setMaskDateSeparator(".");
        birthdate.setUseMask(true);
        birthdate.setUseTextField(true);
        birthdate.setInvalidDateStringMessage(DictionaryInstance.dictionary.invalidDate());
        birthdate.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATE);
                
        BooleanItem deleted = new BooleanItem();
//        deleted.setDataPath("user/deleted");
        deleted.setDefaultValue(false);
        deleted.setName("user.deleted");
        deleted.setTitle(DictionaryInstance.dictionary.deleted());
        deleted.setDisabled(true);
        
        DateTimeItem joined = new DateTimeItem("user.joined", DictionaryInstance.dictionary.joinedDate());
//        joined.setDataPath("user/joined");
        joined.setMaskDateSeparator(".");
        joined.setUseMask(true);
        joined.setUseTextField(true);
        joined.setWidth(180);
        joined.setDisabled(true);
        joined.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
        
        DateTimeItem lastMsgDate = new DateTimeItem("user.lastMsg", DictionaryInstance.dictionary.lastMsgDate());
//        lastMsgDate.setDataPath("user/lastMsg");
        lastMsgDate.setMaskDateSeparator(".");
        lastMsgDate.setUseMask(true);
        lastMsgDate.setUseTextField(true);
        lastMsgDate.setWidth(180);
        lastMsgDate.setDisabled(true);
        lastMsgDate.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
        
        IntegerItem unreadMsgCount = new IntegerItem();
//        unreadMsgCount.setDataPath("user/unreadMsgCount");
        unreadMsgCount.setName("user.unreadMsgCount");
        unreadMsgCount.setTitle(DictionaryInstance.dictionary.unreadMsgCount());
        unreadMsgCount.setDisabled(true);
        
        return new FormItem[] { id, name, surname, address, notes, serviceProvider, nickItem, operatorItem , birthdate, deleted, joined, lastMsgDate, unreadMsgCount };
	}

	
	// Getters & setters
	
	public String getUserId() { return userId; }
	public void setUserId(String userId) { this.userId = userId; }

	public String getUserType() { return userType; }
	public void setUserType(String userType) { this.userType = userType; }

	public String getNameToDisplay() { return nameToDisplay; }
	public void setNameToDisplay(String nameToDisplay) { this.nameToDisplay = nameToDisplay; }
		
}
