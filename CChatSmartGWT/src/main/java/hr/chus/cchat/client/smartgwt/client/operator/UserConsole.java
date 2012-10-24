package hr.chus.cchat.client.smartgwt.client.operator;

import hr.chus.cchat.client.smartgwt.client.common.Constants;
import hr.chus.cchat.client.smartgwt.client.common.PanelFactory;
import hr.chus.cchat.client.smartgwt.client.common.SideNavigationMenu;
import hr.chus.cchat.client.smartgwt.client.i18n.DictionaryInstance;
import hr.chus.cchat.client.smartgwt.client.operator.ds.ConversationDS;
import hr.chus.cchat.client.smartgwt.client.operator.ds.NicksDS;
import hr.chus.cchat.client.smartgwt.client.operator.ds.OperatorsDS;
import hr.chus.cchat.client.smartgwt.client.utils.JSONUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.data.XMLTools;
import com.smartgwt.client.data.fields.DataSourceDateTimeField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.types.KeyNames;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.DateTimeItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.KeyUpEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyUpHandler;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.LeafClickEvent;

/**
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 *
 */
public class UserConsole extends HLayout {
	
	private static final String DESCRIPTION = "UserConsole";
	
	private String userId;
	private String userType;
	private String nameToDisplay;
	private double unreadMsgCount;
	private boolean displayUserForm;
	private SideNavigationMenu usersList;
	private String myUserListNodeID;
	
    private ListGrid conversationListGrid;
	private int offset = 0;
    private int fetchSize = 50;
    private IButton nextButton;
    private IButton previousButton;
    private Label listLabel;
    private Criteria criteria;
    private DynamicForm userForm;
    private DynamicForm sendMsgForm;
    private TextAreaItem smsTextArea;
    private VLayout userFormLayout;
    private String nickName;
    
    
	public static class Factory implements PanelFactory {
        
		private String id;
		private String userType;
		private String nameToDisplay;
		private boolean displayUserForm;
		private SideNavigationMenu usersList;
		private String myUserListNodeID;

        public Factory(final String p_userId, final String p_userType, final String p_nameToDisplay, final boolean p_displayUserForm, final SideNavigationMenu p_usersList, final String p_myUserListNodeID) {
			id = p_userId;
			userType = p_userType;
			nameToDisplay = p_nameToDisplay;
			displayUserForm = p_displayUserForm;
			usersList = p_usersList;
			myUserListNodeID = p_myUserListNodeID;
		}

		public Canvas create() {
			UserConsole panel = new UserConsole();
			panel.setUserId(id);
			panel.setUserType(userType);
			panel.setNameToDisplay(nameToDisplay);
			panel.setDisplayUserForm(displayUserForm);
			panel.setUsersList(usersList);
			panel.setMyUserListNodeID(myUserListNodeID);
			panel.setUnreadMsgCount(-1);
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
		conversationListGrid.setWidth(720);
		conversationListGrid.setHeight(300);
		conversationListGrid.setUseAllDataSourceFields(false);
		conversationListGrid.setFields(getConversationFields());
		conversationListGrid.setWrapCells(true);  
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
						listLabel.setContents(DictionaryInstance.dictionary.totalSmsMessagesInDB() + ": <b>" + totalCount + "&nbsp;&nbsp;(" + offset + " - " + (offset + fetchSize) + ")</b>");
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
						listLabel.setContents(DictionaryInstance.dictionary.totalSmsMessagesInDB() + ": <b>" + totalCount + "&nbsp;&nbsp;(" + offset + " - " + (offset + fetchSize) + ")</b>");
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
        
        VLayout sendMsgForm = createSendMsgForm();
        
		conversationUserInfoLayout.addMember(conversationLayout);
		userFormLayout = createUserForm();
		if (!displayUserForm) userFormLayout.setVisible(false);
		conversationUserInfoLayout.addMember(userFormLayout);
		VLayout vlayout = new VLayout(3);
		vlayout.addMember(conversationUserInfoLayout);
		vlayout.addMember(sendMsgForm);
		
		// TODO: Create picture dialog with send option
		
		canvas.addChild(vlayout);
		
		return canvas;
    }
	
	private VLayout createSendMsgForm() {
		DataSource sendSmsDS = new DataSource(Constants.CONTEXT_PATH + "operator/SendSms");
    	sendSmsDS.setDataFormat(DSDataFormat.JSON);
    	sendMsgForm = new DynamicForm();
    	sendMsgForm.setAutoFocus(true);
    	sendMsgForm.setIsGroup(true);
    	sendMsgForm.setGroupTitle(DictionaryInstance.dictionary.sendMessage());
    	sendMsgForm.setDataSource(sendSmsDS);
    	sendMsgForm.setWidth(350);
    	sendMsgForm.setHeight(120);
    	sendMsgForm.setPadding(5);
    	sendMsgForm.setLayoutAlign(VerticalAlignment.BOTTOM);
    	final HiddenItem userId = new HiddenItem("user");
    	userId.setValue(this.userId);
    	HiddenItem msgType = new HiddenItem("msgType");
    	msgType.setValue("sms");
    	
    	final IntegerItem characterCount = new IntegerItem();
    	characterCount.setName("msgLength");
    	characterCount.setTitle(DictionaryInstance.dictionary.charactersAllowed());
    	characterCount.setValue(CChatOperatorSmartGWT.maxMsgLength);
    	characterCount.setDisabled(true);
    	characterCount.setWidth(50);
    	
    	final IButton sendMsgButton = new IButton(DictionaryInstance.dictionary.sendMessage());
    	
    	smsTextArea = new TextAreaItem("text", DictionaryInstance.dictionary.text());
    	smsTextArea.setWidth(280);
    	smsTextArea.setSelectOnFocus(false);
    	smsTextArea.setLength(CChatOperatorSmartGWT.maxMsgLength);
    	smsTextArea.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
			    int textLength = 0;
			    if (smsTextArea.getValue() != null) {
			        textLength = smsTextArea.getValue().toString().length();
			    }
				characterCount.setValue(CChatOperatorSmartGWT.maxMsgLength - textLength);
				if (event.getKeyName().equals(KeyNames.ENTER)) sendMsgButton.fireEvent(new ClickEvent(null));
			} 
		});
    	sendMsgForm.setFields(userId, msgType, smsTextArea, characterCount);
    	
    	sendMsgButton.setIcon(Constants.CONTEXT_PATH + "images/message.png");
    	sendMsgButton.setAutoFit(true);
    	sendMsgButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Iterator<?> keySetIterator = sendMsgForm.getValues().keySet().iterator();
				while (keySetIterator.hasNext()) {
					String key = (String) keySetIterator.next();
					if (sendMsgForm.getField(key) == null) {
						sendMsgForm.clearValue(key);
					} else {
						Object value = sendMsgForm.getValues().get(key);
						if (value == null) sendMsgForm.clearValue(key);
					}
				}
				smsTextArea.setValue(smsTextArea.getDisplayValue().trim());
				sendMsgForm.submit(new DSCallback() {
					
					@Override
					public void execute(DSResponse response, Object rawData, DSRequest request) {
						JSONArray value = XMLTools.selectObjects(rawData, "/status");
						boolean status = ((JSONBoolean) value.get(0)).booleanValue();
						if (status) {
							sendMsgForm.reset();
							if (nickName != null && !nickName.isEmpty()) smsTextArea.setValue(nickName + ": ");
							userForm.getDataSource().invalidateCache();
							Criteria userFormCriteria = new Criteria("user", (String) userId.getValue());
							userFormCriteria.addCriteria("operation", "get");
							userForm.getDataSource().fetchData(userFormCriteria, null);
							userFormLayout.setVisible(true);
							JSONObject sms = XMLTools.selectObjects(rawData, "/smsMessage").get(0).isObject();
							// Add new msg to conversationList
							RecordList recordList = new RecordList(conversationListGrid.getRecordList().toArray());
							ListGridRecord conversationRecord = new ListGridRecord();
							conversationRecord.setAttribute("text", ((JSONString) sms.get("text")).stringValue());
							conversationRecord.setAttribute("time", JSONUtils.getDate(((JSONString) sms.get("time")).stringValue()));
							conversationRecord.setAttribute("operator", ((JSONString)((JSONObject) sms.get("operator")).get("username")).stringValue());
							conversationRecord.setAttribute("direction", "OUT");
							conversationRecord.setAttribute("messageId", ((JSONNumber) sms.get("id")).doubleValue());
							conversationRecord.setCustomStyle("outboundMsgGridRecord");
							recordList.addAt(conversationRecord, 0);
							conversationListGrid.setData(recordList);
							conversationListGrid.scrollToRow(0);
						} else {
							value = XMLTools.selectObjects(rawData, "/errorMsg");
							String errorMsg = ((JSONString) value.get(0)).stringValue();
							SC.warn(DictionaryInstance.dictionary.sendingMessageFailed() + " --> " + errorMsg);
						}
						
                        // Open next user that has unread messages
						final TreeNode parentNode = usersList.getData().findById(myUserListNodeID);
						final TreeNode[] nodes;
						if (parentNode == null) {
						    nodes = usersList.getData().getAllNodes();    
						} else {
						    nodes = usersList.getData().getAllNodes(parentNode);
						}
                        int recordNum = 0;
                        for (int i = nodes.length - 1; i > 0; i--) {
                            final TreeNode treeNode = nodes[i];
                            final Boolean unread = treeNode.getAttributeAsBoolean(CChatOperatorSmartGWT.UNREAD_ATTRIBUTE);
                            if (unread != null && unread) {
                                // Hack: Manually created LeafClickEvent
                                final Map<String, Object> leafClickEvent = new HashMap<String, Object>();
                                leafClickEvent.put("viewer", usersList.getJsObj());
                                leafClickEvent.put("recordNum", recordNum);
                                leafClickEvent.put("leaf", treeNode.getJsObj());
                                final JavaScriptObject jsObj = JSOHelper.convertMapToJavascriptObject(leafClickEvent);
                                LeafClickEvent.fire(usersList, jsObj);
                                break;
                            }
                            recordNum++;
                        }
					};
				});
			}
    	});
    	
    	VLayout formLayout = new VLayout(5);
        formLayout.addMember(sendMsgForm);
        formLayout.addMember(sendMsgButton);
        
        return formLayout;
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
		
        final IButton updateButton = new IButton(DictionaryInstance.dictionary.update());
		final DataSource ds = new DataSource(Constants.CONTEXT_PATH + "operator/OperatorUserFunctionJSON") {
			
			@Override
			protected void transformResponse(DSResponse response, DSRequest request, Object jsonData) {
				JSONArray value = XMLTools.selectObjects(jsonData, "/status");
				boolean status = false;
				updateButton.setDisabled(false);
				if (value != null && value.size() > 0) {
					status = ((JSONBoolean) value.get(0)).booleanValue();
				}
				if (!status) {
					response.setStatus(RPCResponse.STATUS_VALIDATION_ERROR);
					JSONArray errors = XMLTools.selectObjects(jsonData, "/errorFields");
					response.setErrors(errors.getJavaScriptObject());
				} else {
				    response.setStatus(RPCResponse.STATUS_SUCCESS);
				    Record[] rs = response.getData();
				    userForm.editRecord(rs[0]);
					populateSendMsgForm(jsonData);
				}
			}

		};
		ds.setRecordXPath("user");
		ds.setFields(getDSFields());
		
		ds.setDataFormat(DSDataFormat.JSON);
		userForm.setDataSource(ds);
		
		userForm.setUseAllDataSourceFields(false);
		userForm.setFields(getFormFields());
		
		Criteria userFormCriteria = new Criteria("user", userId);
		userFormCriteria.addCriteria("operation", "get");
		ds.fetchData(userFormCriteria, null);
		
		updateButton.setIcon(Constants.CONTEXT_PATH + "images/edit.png");
        updateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (userForm.validate()) {
					updateButton.setDisabled(true);
					Iterator<?> keySetIterator = userForm.getValues().keySet().iterator();
					while (keySetIterator.hasNext()) {
						String key = (String) keySetIterator.next();
						if (userForm.getField(key) == null) {
							userForm.clearValue(key);
						} else {
							Object value = userForm.getValues().get(key);
							if (value == null) userForm.clearValue(key);
						}
					}
					userForm.setValue("operation", "update");
					userForm.saveData();
				}
			}
		});
        
        VLayout formLayout = new VLayout(5);
        formLayout.addMember(userFormLabel);
        formLayout.addMember(userForm);
        formLayout.addMember(updateButton);
        
        return formLayout;
	}
	
    private void populateSendMsgForm(Object jsonData) {
		// Edit smsTextArea with nick name
		JSONArray value = XMLTools.selectObjects(jsonData, "user/nick/name");
		sendMsgForm.reset();
		if (value != null && value.size() > 0) {
			nickName = ((JSONString) value.get(0)).stringValue();
			smsTextArea.setValue(nickName + ": ");
		} else {
			smsTextArea.setValue("");
			nickName = null;
		}
		
		sendMsgForm.focusInItem(smsTextArea.getName());
	}

	private ListGridField[] getConversationFields() {
		ListGridField text = new ListGridField("text");
		text.setAlign(Alignment.CENTER);
    	text.setWidth(380);
    	
    	ListGridField msisdn = new ListGridField("msisdn");
        msisdn.setAlign(Alignment.CENTER);
        msisdn.setWidth(100);
    	
    	ListGridField operator = new ListGridField("operator");
    	operator.setAlign(Alignment.CENTER);
    	operator.setWidth(100);
    	
		ListGridField time = new ListGridField("time");
		time.setAlign(Alignment.LEFT);
		time.setCellFormatter(new CellFormatter() {
            public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
                if (value != null) {
                    try {
                        Date dateValue = (Date) value;
                        return Constants.dateTimeFormat.format(dateValue, TimeZone.createTimeZone(0));
                    } catch (Exception e) {
                        return value.toString();
                    }
                } else {
                    return "";
                }
            }
        });
		time.setWidth(120);
		
		return new ListGridField[] { time, msisdn, operator, text };
	}

	public void loadConversation(boolean setMessagesAsRead) {
		unreadMsgCount = 0;
		criteria = new Criteria("userId", userId);
		criteria.setAttribute("limit", fetchSize);
		criteria.setAttribute("start", 0);
		criteria.setAttribute("setMessagesAsRead", setMessagesAsRead);
		if (!conversationListGrid.willFetchData(criteria)) conversationListGrid.invalidateCache();
		conversationListGrid.fetchData(criteria, new DSCallback() {
			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				int totalCount = (int) ConversationDS.getInstance().getTotalCount();
				listLabel.setContents(DictionaryInstance.dictionary.totalSmsMessagesInDB() + ": <b>" + totalCount + "&nbsp;&nbsp;(" + offset + " - " + (offset + fetchSize) + ")</b>");
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
	
    private DataSourceField[] getDSFields() {
        DataSourceIntegerField pkField = new DataSourceIntegerField("user.id");
        pkField.setValueXPath("id");
        
        DataSourceTextField name = new DataSourceTextField("user.name", DictionaryInstance.dictionary.name());
        name.setValueXPath("name");
        
        DataSourceTextField surname = new DataSourceTextField("user.surname", DictionaryInstance.dictionary.surname());
        surname.setValueXPath("surname");
        
        DataSourceTextField address = new DataSourceTextField("user.address", DictionaryInstance.dictionary.address());
        address.setValueXPath("address");
        
        DataSourceTextField notes = new DataSourceTextField("user.notes", DictionaryInstance.dictionary.notes());
        notes.setValueXPath("notes");
        
        DataSourceIntegerField nickId = new DataSourceIntegerField("user.nick", DictionaryInstance.dictionary.nick());
        nickId.setValueXPath("nick/id");
        
        DataSourceIntegerField operatorId = new DataSourceIntegerField("user.operator", DictionaryInstance.dictionary.nick());
        operatorId.setValueXPath("operator/id");
        
        DataSourceDateTimeField birthdate = new DataSourceDateTimeField("user.birthdate", DictionaryInstance.dictionary.birthdate());
        birthdate.setValueXPath("birthdate");
        
        DataSourceDateTimeField joined = new DataSourceDateTimeField("user.joined", DictionaryInstance.dictionary.joinedDate(), 50, true);
        joined.setValueXPath("joined");
        
        DataSourceDateTimeField lastMsg = new DataSourceDateTimeField("user.lastMsg", DictionaryInstance.dictionary.lastMsgDate(), 50, true);
        lastMsg.setValueXPath("lastMsg");
        
        DataSourceIntegerField unreadMsgCount = new DataSourceIntegerField("user.unreadMsgCount", DictionaryInstance.dictionary.unreadMsgCount(), 50, true);
        unreadMsgCount.setValueXPath("unreadMsgCount");
        
        return new DataSourceField[] { pkField, name, surname, address, notes, nickId, operatorId, birthdate, joined, lastMsg, unreadMsgCount };
    }
	
    private FormItem[] getFormFields() {
    	TextItem id = new TextItem("user.id", "ID");
    	id.setDisabled(true);
        TextItem name = new TextItem("user.name", DictionaryInstance.dictionary.name());
        TextItem surname = new TextItem("user.surname", DictionaryInstance.dictionary.surname());
        TextItem address = new TextItem("user.address", DictionaryInstance.dictionary.address());
        TextAreaItem notes = new TextAreaItem("user.notes", DictionaryInstance.dictionary.notes());

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
        
        final DateItem birthdate = new DateItem("user.birthdate", DictionaryInstance.dictionary.birthdate());
        birthdate.setAttribute("jsonPath", "user/birthdate");
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
        
        return new FormItem[] { id, name, surname, address, notes, nickItem, operatorItem , birthdate, joined, lastMsgDate, unreadMsgCount };
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

	public void setUnreadMsgCount(double unreadMsgCount) { this.unreadMsgCount = unreadMsgCount; }
	public double getUnreadMsgCount() { return unreadMsgCount; }
	
    public final void setUsersList(SideNavigationMenu usersList) { this.usersList = usersList; }

    public final void setMyUserListNodeID(final String p_myUserListNodeID) { myUserListNodeID = p_myUserListNodeID; }
				
}
