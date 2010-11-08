package hr.chus.cchat.client.smartgwt.client.operator;

import java.util.Date;
import java.util.Iterator;

import hr.chus.cchat.client.smartgwt.client.common.Constants;
import hr.chus.cchat.client.smartgwt.client.common.PanelFactory;
import hr.chus.cchat.client.smartgwt.client.i18n.DictionaryInstance;
import hr.chus.cchat.client.smartgwt.client.operator.ds.ConversationDS;
import hr.chus.cchat.client.smartgwt.client.operator.ds.NicksDS;
import hr.chus.cchat.client.smartgwt.client.operator.ds.OperatorsDS;
import hr.chus.cchat.client.smartgwt.client.utils.JSONUtils;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.XMLTools;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.types.Overflow;
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
	private boolean displayUserForm;
	private ListGrid conversationListGrid;
	private int offset = 0;
    private int fetchSize = 50;
    private IButton nextButton;
    private IButton previousButton;
    private Label listLabel;
    private Criteria criteria;
    private DynamicForm userForm;

    
	public static class Factory implements PanelFactory {
        
		private String id;
		private String userType;
		private String nameToDisplay;
		private boolean displayUserForm;

        public Factory(String userId, String userType, String nameToDisplay, boolean displayUserForm) {
			this.id = userId;
			this.userType = userType;
			this.nameToDisplay = nameToDisplay;
			this.displayUserForm = displayUserForm;
		}

		public Canvas create() {
			UserConsole panel = new UserConsole();
			panel.setUserId(id);
			panel.setUserType(userType);
			panel.setNameToDisplay(nameToDisplay);
			panel.setDisplayUserForm(displayUserForm);
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
        
        HLayout searchPagingLayout = new HLayout(5);
        searchPagingLayout.addMember(previousButton);
        searchPagingLayout.addMember(nextButton);
        
        conversationLayout.addMember(searchPagingLayout);
        
        VLayout listLayout = new VLayout(0);
		listLayout.addMember(listLabel);
		listLayout.addMember(conversationListGrid);
		
        conversationLayout.addMember(listLayout);
        
		conversationUserInfoLayout.addMember(conversationLayout);
		if (displayUserForm) conversationUserInfoLayout.addMember(createUserForm());
		
		vlayout.addMember(conversationUserInfoLayout);
		
		HTMLFlow mainHtmlFlow = new HTMLFlow();
		mainHtmlFlow.setOverflow(Overflow.AUTO);
		mainHtmlFlow.setPadding(10);
		mainHtmlFlow.setHeight("50");
		mainHtmlFlow.setContents("<b> " + userId + " </b>");
		vlayout.addMember(mainHtmlFlow);
		
		// TODO: Create send message dialog
		
		// TODO: Create picture dialog with send option
		
		canvas.addChild(vlayout);
		return canvas;
    }
	
	private VLayout createUserForm() {
		Label userFormLabel = new Label();
		userFormLabel.setHeight(10);
		userFormLabel.setWidth(200);
		userFormLabel.setContents(DictionaryInstance.dictionary.editUser());
		
		userForm = new DynamicForm();
        userForm.setIsGroup(true);
        userForm.setGroupTitle(DictionaryInstance.dictionary.update());
        userForm.setNumCols(4);
		
		DataSource ds = new DataSource(Constants.CONTEXT_PATH + "operator/OperatorUserFunctionJSON") {
			
			@Override
			protected Object transformRequest(DSRequest dsRequest) {
				dsRequest.setContentType("application/json; charset=utf-8");
				return super.transformRequest(dsRequest);
			}
			
			@Override
			protected void transformResponse(DSResponse response, DSRequest request, Object jsonData) {
				populateUserForm(jsonData);
			}

		};
		ds.setDataFormat(DSDataFormat.JSON);
		userForm.setDataSource(ds);
		
		userForm.setUseAllDataSourceFields(false);
		userForm.setFields(getFormFields());
		
		Criteria userFormCriteria = new Criteria("user", userId);
		userFormCriteria.addCriteria("operation", "get");
		ds.fetchData(userFormCriteria);
		
		final IButton updateButton = new IButton(DictionaryInstance.dictionary.update());
		updateButton.setIcon(Constants.CONTEXT_PATH + "images/edit.png");
        updateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (userForm.validate()) {
					updateButton.setDisabled(true);
					Iterator<?> keySetIterator = userForm.getValues().keySet().iterator();
					while (keySetIterator.hasNext()) {
						String key = (String) keySetIterator.next();
						Object value = userForm.getValues().get(key);
						if (value == null) userForm.clearValue(key);
					}
					userForm.setValue("operation", "update");
					
					userForm.submit(new DSCallback() {

						@Override
						public void execute(DSResponse response, Object jsonData, DSRequest request) {
							JSONArray value = XMLTools.selectObjects(jsonData, "/status");
							boolean status = false;
							updateButton.setDisabled(false);
							if (value != null && value.size() > 0) {
								status = ((JSONBoolean) value.get(0)).booleanValue();
							}
							if (status) {
								populateUserForm(XMLTools.selectObjects(jsonData, "/user"));
							}
						}
						
					});
				}
			}
		});
        
        VLayout formLayout = new VLayout(5);
        formLayout.addMember(userFormLabel);
        formLayout.addMember(userForm);
        formLayout.addMember(updateButton);
        
        return formLayout;
	}
	
	private void populateUserForm(Object jsonData) {
		for (FormItem formItem : userForm.getFields())  {
			JSONArray value = XMLTools.selectObjects(jsonData, formItem.getAttribute("jsonPath"));
			if (value == null || value.size() == 0) continue;
			if (formItem instanceof DateTimeItem) {
				userForm.setValue(formItem.getName(), Constants.dateTimeFormat.format(JSONUtils.getDate(value.getJavaScriptObject().toString())));
			} else if (formItem instanceof DateItem) {
				Date date = JSONUtils.getDate(value.getJavaScriptObject().toString());
				Date gmt = new Date(date.getTime() - date.getTimezoneOffset() * 60 * 1000);
				userForm.setValue(formItem.getName(), Constants.dateTimeFormat.format(gmt));
			} else if (formItem instanceof BooleanItem) {
				userForm.setValue(formItem.getName(), value.get(0).isBoolean().booleanValue());
			} else {
				userForm.setValue(formItem.getName(), value.getJavaScriptObject());
			}
		}
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
    	id.setAttribute("jsonPath", "user/id");
    	id.setDisabled(true);
        TextItem name = new TextItem("user.name", DictionaryInstance.dictionary.name());
        name.setAttribute("jsonPath", "user/name");
        TextItem surname = new TextItem("user.surname", DictionaryInstance.dictionary.surname());
        surname.setAttribute("jsonPath", "user/surname");
        TextItem address = new TextItem("user.address", DictionaryInstance.dictionary.address());
        address.setAttribute("jsonPath", "user/address");
        TextAreaItem notes = new TextAreaItem("user.notes", DictionaryInstance.dictionary.notes());
        notes.setAttribute("jsonPath", "user/notes");

        SelectItem nickItem = new SelectItem("user.nick", DictionaryInstance.dictionary.nick());
        nickItem.setAttribute("jsonPath", "user/nick/id");
        nickItem.setAllowEmptyValue(true);
        nickItem.setEmptyDisplayValue(DictionaryInstance.dictionary.notSet());
        nickItem.setOptionDataSource(NicksDS.getInstance());
        nickItem.setDisplayField("name");
        nickItem.setValueField("id");
        
        SelectItem operatorItem = new SelectItem("user.operator", DictionaryInstance.dictionary.operator());
        operatorItem.setAttribute("jsonPath", "user/operator/id");
        operatorItem.setAllowEmptyValue(true);
        operatorItem.setEmptyDisplayValue(DictionaryInstance.dictionary.notSet());
        operatorItem.setOptionDataSource(OperatorsDS.getInstance());
        operatorItem.setDisplayField("username");
        operatorItem.setValueField("id");
        
        TextItem serviceProvider = new TextItem("user.serviceProvider");
        serviceProvider.setAttribute("jsonPath", "user/serviceProvider/id");
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
        birthdate.setAttribute("jsonPath", "user/birthDate");
        birthdate.setMaskDateSeparator(".");
        birthdate.setUseMask(true);
        birthdate.setUseTextField(true);
        birthdate.setInvalidDateStringMessage(DictionaryInstance.dictionary.invalidDate());
        birthdate.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATE);
                
        DateTimeItem joined = new DateTimeItem("user.joined", DictionaryInstance.dictionary.joinedDate());
        joined.setAttribute("jsonPath", "user/joined");
        joined.setMaskDateSeparator(".");
        joined.setUseMask(true);
        joined.setUseTextField(true);
        joined.setWidth(180);
        joined.setDisabled(true);
        joined.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
        
        DateTimeItem lastMsgDate = new DateTimeItem("user.lastMsg", DictionaryInstance.dictionary.lastMsgDate());
        lastMsgDate.setAttribute("jsonPath", "user/lastMsg");
        lastMsgDate.setMaskDateSeparator(".");
        lastMsgDate.setUseMask(true);
        lastMsgDate.setUseTextField(true);
        lastMsgDate.setWidth(180);
        lastMsgDate.setDisabled(true);
        lastMsgDate.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
        
        IntegerItem unreadMsgCount = new IntegerItem();
        unreadMsgCount.setAttribute("jsonPath", "user/unreadMsgCount");
        unreadMsgCount.setName("user.unreadMsgCount");
        unreadMsgCount.setTitle(DictionaryInstance.dictionary.unreadMsgCount());
        unreadMsgCount.setDisabled(true);
        
        BooleanItem deleted = new BooleanItem();
        deleted.setDefaultValue(false);
        deleted.setAttribute("jsonPath", "user/deleted");
        deleted.setName("user.deleted");
        deleted.setTitle(DictionaryInstance.dictionary.deleted());
        deleted.setVisible(false);
        
        return new FormItem[] { id, name, surname, address, notes, serviceProvider, nickItem, operatorItem , birthdate, joined, lastMsgDate, unreadMsgCount, deleted };
	}

	
	// Getters & setters
	
	public String getUserId() { return userId; }
	public void setUserId(String userId) { this.userId = userId; }

	public String getUserType() { return userType; }
	public void setUserType(String userType) { this.userType = userType; }

	public String getNameToDisplay() { return nameToDisplay; }
	public void setNameToDisplay(String nameToDisplay) { this.nameToDisplay = nameToDisplay; }

	public boolean isDisplayUserForm() { return displayUserForm; }
	public void setDisplayUserForm(boolean displayUserForm) { this.displayUserForm = displayUserForm; }
			
}
