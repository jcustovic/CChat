package hr.chus.cchat.client.smartgwt.client.operator;

import hr.chus.cchat.client.smartgwt.client.common.Constants;
import hr.chus.cchat.client.smartgwt.client.common.PanelFactory;
import hr.chus.cchat.client.smartgwt.client.common.SideNavigationMenu;
import hr.chus.cchat.client.smartgwt.client.common.ds.BotDS;
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
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
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
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public class UserConsole extends HLayout {

    private static final String DESCRIPTION = "UserConsole";

    private String              userId;
    private String              userType;
    private String              nameToDisplay;
    private double              unreadMsgCount;
    private boolean             displayUserForm;
    private SideNavigationMenu  usersList;
    private String              myUserListNodeID;

    private ListGrid            conversationListGrid;
    private int                 offset      = 0;
    private int                 fetchSize   = 50;
    private IButton             nextButton;
    private IButton             previousButton;
    private IButton             sendBotMsgButton;
    private Label               listLabel;
    private Criteria            criteria;
    private DynamicForm         userForm;
    private DynamicForm         sendMsgForm;
    private TextAreaItem        smsTextArea;
    private TextAreaItem        botResponseMsgArea;
    private VLayout             userFormLayout;
    private String              nickName;

    public static class Factory implements PanelFactory {

        private String             id;
        private String             userType;
        private String             nameToDisplay;
        private boolean            displayUserForm;
        private SideNavigationMenu usersList;
        private String             myUserListNodeID;

        public Factory(final String p_userId, final String p_userType, final String p_nameToDisplay, final boolean p_displayUserForm,
                final SideNavigationMenu p_usersList, final String p_myUserListNodeID) {
            id = p_userId;
            userType = p_userType;
            nameToDisplay = p_nameToDisplay;
            displayUserForm = p_displayUserForm;
            usersList = p_usersList;
            myUserListNodeID = p_myUserListNodeID;
        }

        public Canvas create() {
            final UserConsole panel = new UserConsole();
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

        public final String getID() {
            return id;
        }

        public final String getDescription() {
            return DESCRIPTION;
        }

    }

    public final Canvas getViewPanel() {
        final Canvas canvas = new Canvas();
        final HLayout conversationUserInfoLayout = new HLayout(20);

        listLabel = new Label();
        listLabel.setVisible(false);
        listLabel.setWidth(600);
        listLabel.setHeight("10px");

        conversationListGrid = new ListGrid() {

            @Override
            protected String getBaseStyle(final ListGridRecord p_record, final int p_rowNum, final int p_colNum) {
                final String direction = p_record.getAttribute("direction");
                if (direction != null) {
                    if (direction.equals("IN"))
                        return "inboundMsgGridRecord";
                    else if (direction.equals("OUT"))
                        return "outboundMsgGridRecord";
                    else
                        return super.getBaseStyle(p_record, p_rowNum, p_colNum);
                } else {
                    return super.getBaseStyle(p_record, p_rowNum, p_colNum);
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
                    public void execute(final DSResponse p_response, final Object p_rawData, final DSRequest p_request) {
                        int totalCount = (int) ConversationDS.getInstance().getTotalCount();
                        listLabel.setContents(DictionaryInstance.dictionary.totalSmsMessagesInDB() + ": <b>" + totalCount + "&nbsp;&nbsp;(" + offset + " - "
                                + (offset + fetchSize) + ")</b>");
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
            public void onClick(final ClickEvent p_event) {
                offset -= fetchSize;
                criteria.setAttribute("start", offset);
                conversationListGrid.invalidateCache();
                conversationListGrid.fetchData(criteria, new DSCallback() {

                    @Override
                    public void execute(final DSResponse p_response, final Object p_rawData, final DSRequest p_request) {
                        int totalCount = (int) ConversationDS.getInstance().getTotalCount();
                        listLabel.setContents(DictionaryInstance.dictionary.totalSmsMessagesInDB() + ": <b>" + totalCount + "&nbsp;&nbsp;(" + offset + " - "
                                + (offset + fetchSize) + ")</b>");
                        listLabel.setVisible(true);
                        if (offset <= 0) previousButton.setVisible(false);
                        nextButton.setVisible(true);
                    }
                });
            }
        });

        final HLayout searchPagingLayout = new HLayout(5);
        searchPagingLayout.addMember(previousButton);
        searchPagingLayout.addMember(nextButton);

        final VLayout listLayout = new VLayout(0);
        listLayout.addMember(listLabel);
        listLayout.addMember(conversationListGrid);

        final VLayout conversationLayout = new VLayout(8);
        conversationLayout.addMember(searchPagingLayout);
        conversationLayout.addMember(listLayout);

        conversationUserInfoLayout.addMember(conversationLayout);
        userFormLayout = createUserForm();
        if (!displayUserForm) userFormLayout.setVisible(false);
        conversationUserInfoLayout.addMember(userFormLayout);

        final HLayout sendMsgLayout = new HLayout(20);
        sendMsgLayout.addMember(createSendMsgForm());
        sendMsgLayout.addMember(createSendBotMsgForm());

        final VLayout vlayout = new VLayout(3);
        vlayout.addMember(conversationUserInfoLayout);
        vlayout.addMember(sendMsgLayout);

        // TODO: Create picture dialog with send option

        canvas.addChild(vlayout);

        return canvas;
    }

    private VLayout createSendMsgForm() {
        final DataSource sendSmsDS = new DataSource(Constants.CONTEXT_PATH + "operator/SendSms");
        sendSmsDS.setDataFormat(DSDataFormat.JSON);

        sendMsgForm = new DynamicForm();
        sendMsgForm.setAutoFocus(true);
        sendMsgForm.setIsGroup(true);
        sendMsgForm.setPadding(5);
        sendMsgForm.setGroupTitle(DictionaryInstance.dictionary.sendMessage());
        sendMsgForm.setDataSource(sendSmsDS);
        sendMsgForm.setWidth(350);
        sendMsgForm.setHeight(120);
        sendMsgForm.setPadding(5);
        sendMsgForm.setLayoutAlign(VerticalAlignment.BOTTOM);
        final HiddenItem userItem = new HiddenItem("user");
        userItem.setValue(userId);
        final HiddenItem msgType = new HiddenItem("msgType");
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
        sendMsgForm.setFields(userItem, msgType, smsTextArea, characterCount);

        sendMsgButton.setIcon(Constants.CONTEXT_PATH + "images/message.png");
        sendMsgButton.setAutoFit(true);
        sendMsgButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                final Iterator<?> keySetIterator = sendMsgForm.getValues().keySet().iterator();
                while (keySetIterator.hasNext()) {
                    final String key = (String) keySetIterator.next();
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
                    public void execute(final DSResponse p_response, final Object p_rawData, final DSRequest p_request) {
                        JSONArray value = XMLTools.selectObjects(p_rawData, "/status");
                        boolean status = ((JSONBoolean) value.get(0)).booleanValue();
                        if (status) {
                            sendMsgForm.reset();
                            if (nickName != null && !nickName.isEmpty()) smsTextArea.setValue(nickName + ": ");
                            userForm.getDataSource().invalidateCache();
                            final Criteria userFormCriteria = new Criteria("user", (String) userItem.getValue());
                            userFormCriteria.addCriteria("operation", "get");
                            userForm.getDataSource().fetchData(userFormCriteria, null);
                            userFormLayout.setVisible(true);
                            final JSONObject sms = XMLTools.selectObjects(p_rawData, "/smsMessage").get(0).isObject();
                            // Add new msg to conversationList
                            final RecordList recordList = new RecordList(conversationListGrid.getRecordList().toArray());
                            final ListGridRecord conversationRecord = new ListGridRecord();
                            conversationRecord.setAttribute("text", ((JSONString) sms.get("text")).stringValue());
                            conversationRecord.setAttribute("time", JSONUtils.getDate(((JSONString) sms.get("time")).stringValue()));
                            conversationRecord.setAttribute("operator", ((JSONString) ((JSONObject) sms.get("operator")).get("username")).stringValue());
                            conversationRecord.setAttribute("direction", "OUT");
                            conversationRecord.setAttribute("messageId", ((JSONNumber) sms.get("id")).doubleValue());
                            conversationRecord.setCustomStyle("outboundMsgGridRecord");
                            recordList.addAt(conversationRecord, 0);
                            conversationListGrid.setData(recordList);
                            conversationListGrid.scrollToRow(0);
                        } else {
                            value = XMLTools.selectObjects(p_rawData, "/errorMsg");
                            final String errorMsg = ((JSONString) value.get(0)).stringValue();
                            SC.warn(DictionaryInstance.dictionary.sendingMessageFailed() + " --> " + errorMsg);
                        }

                        switchToNextUser();
                    };
                });
            }
        });

        final VLayout formLayout = new VLayout(5);
        formLayout.addMember(sendMsgForm);
        formLayout.addMember(sendMsgButton);

        return formLayout;
    }

    private Canvas createSendBotMsgForm() {
        final DataSource sendSmsDS = new DataSource(Constants.CONTEXT_PATH + "operator/SendSms");
        sendSmsDS.setDataFormat(DSDataFormat.JSON);

        final DynamicForm sendBotMsgForm = new DynamicForm();
        sendBotMsgForm.setAutoFocus(true);
        sendBotMsgForm.setPadding(5);
        sendBotMsgForm.setIsGroup(true);
        sendBotMsgForm.setGroupTitle(DictionaryInstance.dictionary.sendBotMessage());
        sendBotMsgForm.setDataSource(sendSmsDS);
        sendBotMsgForm.setWidth(350);
        sendBotMsgForm.setHeight(120);
        sendBotMsgForm.setPadding(5);
        sendBotMsgForm.setLayoutAlign(VerticalAlignment.BOTTOM);
        final HiddenItem userItem = new HiddenItem("user");
        userItem.setValue(userId);
        final HiddenItem botResponse = new HiddenItem("botResponse");
        botResponse.setValue(true);
        final HiddenItem msgType = new HiddenItem("msgType");
        msgType.setValue("sms");

        sendBotMsgButton = new IButton(DictionaryInstance.dictionary.sendBotMessage());

        botResponseMsgArea = new TextAreaItem("text", DictionaryInstance.dictionary.bot());
        botResponseMsgArea.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(final ChangeEvent p_event) {

            }
        });
        botResponseMsgArea.setWidth(280);
        botResponseMsgArea.setSelectOnFocus(false);
        botResponseMsgArea.setDisabled(true);

        sendBotMsgForm.setFields(userItem, msgType, botResponse, botResponseMsgArea);

        sendBotMsgButton.setDisabled(true);
        sendBotMsgButton.setIcon(Constants.CONTEXT_PATH + "images/message.png");
        sendBotMsgButton.setShowDisabledIcon(false);
        sendBotMsgButton.setAutoFit(true);
        sendBotMsgButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                final Iterator<?> keySetIterator = sendBotMsgForm.getValues().keySet().iterator();
                while (keySetIterator.hasNext()) {
                    final String key = (String) keySetIterator.next();
                    if (sendBotMsgForm.getField(key) == null) {
                        sendBotMsgForm.clearValue(key);
                    } else {
                        Object value = sendBotMsgForm.getValues().get(key);
                        if (value == null) sendBotMsgForm.clearValue(key);
                    }
                }
                botResponseMsgArea.setValue(botResponseMsgArea.getDisplayValue().trim());
                sendBotMsgForm.submit(new DSCallback() {

                    @Override
                    public void execute(final DSResponse p_response, final Object p_rawData, final DSRequest p_request) {
                        JSONArray value = XMLTools.selectObjects(p_rawData, "/status");
                        boolean status = ((JSONBoolean) value.get(0)).booleanValue();
                        if (status) {
                            sendBotMsgForm.reset();
                            botResponseMsgArea.clearValue();
                            sendBotMsgButton.setDisabled(true);
                            userForm.getDataSource().invalidateCache();
                            final Criteria userFormCriteria = new Criteria("user", (String) userItem.getValue());
                            userFormCriteria.addCriteria("operation", "get");
                            userForm.getDataSource().fetchData(userFormCriteria, null);
                            userFormLayout.setVisible(true);
                            final JSONObject sms = XMLTools.selectObjects(p_rawData, "/smsMessage").get(0).isObject();
                            // Add new msg to conversationList
                            final RecordList recordList = new RecordList(conversationListGrid.getRecordList().toArray());
                            final ListGridRecord conversationRecord = new ListGridRecord();
                            conversationRecord.setAttribute("text", ((JSONString) sms.get("text")).stringValue());
                            conversationRecord.setAttribute("time", JSONUtils.getDate(((JSONString) sms.get("time")).stringValue()));
                            conversationRecord.setAttribute("operator", ((JSONString) ((JSONObject) sms.get("operator")).get("username")).stringValue());
                            conversationRecord.setAttribute("direction", "OUT");
                            conversationRecord.setAttribute("messageId", ((JSONNumber) sms.get("id")).doubleValue());
                            conversationRecord.setCustomStyle("outboundMsgGridRecord");
                            recordList.addAt(conversationRecord, 0);
                            conversationListGrid.setData(recordList);
                            conversationListGrid.scrollToRow(0);
                        } else {
                            value = XMLTools.selectObjects(p_rawData, "/errorMsg");
                            final String errorMsg = ((JSONString) value.get(0)).stringValue();
                            SC.warn(DictionaryInstance.dictionary.sendingMessageFailed() + " --> " + errorMsg);
                        }

                        switchToNextUser();
                    }

                });
            }
        });

        final VLayout formLayout = new VLayout(5);
        formLayout.addMember(sendBotMsgForm);
        formLayout.addMember(sendBotMsgButton);

        return formLayout;
    }

    private void switchToNextUser() {
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

    private VLayout createUserForm() {
        final Label userFormLabel = new Label();
        userFormLabel.setHeight(10);
        userFormLabel.setWidth(200);
        userFormLabel.setContents(DictionaryInstance.dictionary.editUser());

        userForm = new DynamicForm();
        userForm.setPadding(5);
        userForm.setIsGroup(true);
        userForm.setGroupTitle(DictionaryInstance.dictionary.update());
        userForm.setNumCols(4);

        final IButton updateButton = new IButton(DictionaryInstance.dictionary.update());
        final DataSource ds = new DataSource(Constants.CONTEXT_PATH + "operator/OperatorUserFunctionJSON") {

            @Override
            protected void transformResponse(final DSResponse p_response, final DSRequest p_request, final Object p_jsonData) {
                final JSONArray value = XMLTools.selectObjects(p_jsonData, "/status");
                boolean status = false;
                updateButton.setDisabled(false);
                if (value != null && value.size() > 0) {
                    status = ((JSONBoolean) value.get(0)).booleanValue();
                }
                if (!status) {
                    p_response.setStatus(RPCResponse.STATUS_VALIDATION_ERROR);
                    final JSONArray errors = XMLTools.selectObjects(p_jsonData, "/errorFields");
                    p_response.setErrors(errors.getJavaScriptObject());
                } else {
                    p_response.setStatus(RPCResponse.STATUS_SUCCESS);
                    final Record[] rs = p_response.getData();
                    userForm.editRecord(rs[0]);
                    populateSendMsgForm(p_jsonData);
                }
            }

        };
        ds.setRecordXPath("user");
        ds.setFields(getDSFields());

        ds.setDataFormat(DSDataFormat.JSON);
        userForm.setDataSource(ds);

        userForm.setUseAllDataSourceFields(false);
        userForm.setFields(getFormFields());

        final Criteria userFormCriteria = new Criteria("user", userId);
        userFormCriteria.addCriteria("operation", "get");
        ds.fetchData(userFormCriteria, null);

        updateButton.setIcon(Constants.CONTEXT_PATH + "images/edit.png");
        updateButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent p_event) {
                if (userForm.validate()) {
                    updateButton.setDisabled(true);
                    final Iterator<?> keySetIterator = userForm.getValues().keySet().iterator();
                    while (keySetIterator.hasNext()) {
                        final String key = (String) keySetIterator.next();
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

        final VLayout formLayout = new VLayout(5);
        formLayout.addMember(userFormLabel);
        formLayout.addMember(userForm);
        formLayout.addMember(updateButton);

        return formLayout;
    }

    private void populateSendMsgForm(final Object p_jsonData) {
        // Edit smsTextArea with nick name
        JSONArray value = XMLTools.selectObjects(p_jsonData, "user/nick/name");
        sendMsgForm.reset();
        if (value != null && value.size() > 0) {
            nickName = ((JSONString) value.get(0)).stringValue();
            smsTextArea.setValue(nickName + ": ");
        } else {
            smsTextArea.setValue("");
            nickName = null;
        }

        value = XMLTools.selectObjects(p_jsonData, "user/bot/name");
        if (value == null || value.size() == 0) {
            botResponseMsgArea.setValue(DictionaryInstance.dictionary.noBotSelected());
            sendBotMsgButton.disable();
        }

        sendMsgForm.focusInItem(smsTextArea.getName());
    }

    private ListGridField[] getConversationFields() {
        final ListGridField text = new ListGridField("text");
        text.setAlign(Alignment.CENTER);
        text.setWidth(380);

        final ListGridField msisdn = new ListGridField("msisdn");
        msisdn.setAlign(Alignment.CENTER);
        msisdn.setWidth(100);

        final ListGridField operator = new ListGridField("operator");
        operator.setAlign(Alignment.CENTER);
        operator.setWidth(100);

        final ListGridField time = new ListGridField("time");
        time.setAlign(Alignment.LEFT);
        time.setCellFormatter(new CellFormatter() {

            public String format(final Object p_value, final ListGridRecord p_record, final int p_rowNum, final int p_colNum) {
                if (p_value != null) {
                    try {
                        final Date dateValue = (Date) p_value;
                        return Constants.dateTimeFormat.format(dateValue, TimeZone.createTimeZone(0));
                    } catch (Exception e) {
                        return p_value.toString();
                    }
                } else {
                    return "";
                }
            }
        });
        time.setWidth(120);

        return new ListGridField[] { time, msisdn, operator, text };
    }

    public void loadConversation(final boolean p_setMessagesAsRead) {
        unreadMsgCount = 0;
        criteria = new Criteria("userId", userId);
        criteria.setAttribute("limit", fetchSize);
        criteria.setAttribute("start", 0);
        criteria.setAttribute("setMessagesAsRead", p_setMessagesAsRead);
        if (!conversationListGrid.willFetchData(criteria)) conversationListGrid.invalidateCache();
        conversationListGrid.fetchData(criteria, new DSCallback() {

            @Override
            public void execute(final DSResponse p_response, final Object p_rawData, final DSRequest p_request) {
                int totalCount = (int) ConversationDS.getInstance().getTotalCount();
                listLabel.setContents(DictionaryInstance.dictionary.totalSmsMessagesInDB() + ": <b>" + totalCount + "&nbsp;&nbsp;(" + offset + " - "
                        + (offset + fetchSize) + ")</b>");
                listLabel.setVisible(true);
                if (totalCount > fetchSize) {
                    nextButton.setVisible(true);
                } else {
                    nextButton.setVisible(false);
                }
                previousButton.setVisible(false);

                // We have new messages therefore new bot response.
                if (ConversationDS.getInstance().getBotResponse() != null && ConversationDS.getInstance().getBotResponse().length() > 1) {
                    botResponseMsgArea.setValue(ConversationDS.getInstance().getBotResponse());
                    sendBotMsgButton.enable();
                } else {
                    sendBotMsgButton.disable();
                }
            }
        });
    }

    private DataSourceField[] getDSFields() {
        final DataSourceIntegerField pkField = new DataSourceIntegerField("user.id");
        pkField.setValueXPath("id");

        final DataSourceTextField name = new DataSourceTextField("user.name", DictionaryInstance.dictionary.name());
        name.setValueXPath("name");

        final DataSourceTextField surname = new DataSourceTextField("user.surname", DictionaryInstance.dictionary.surname());
        surname.setValueXPath("surname");

        final DataSourceTextField address = new DataSourceTextField("user.address", DictionaryInstance.dictionary.address());
        address.setValueXPath("address");

        final DataSourceTextField notes = new DataSourceTextField("user.notes", DictionaryInstance.dictionary.notes());
        notes.setValueXPath("notes");

        final DataSourceIntegerField nickId = new DataSourceIntegerField("user.nick", DictionaryInstance.dictionary.nick());
        nickId.setValueXPath("nick/id");

        final DataSourceIntegerField operatorId = new DataSourceIntegerField("user.operator", DictionaryInstance.dictionary.operator());
        operatorId.setValueXPath("operator/id");

        final DataSourceIntegerField botId = new DataSourceIntegerField("user.bot", DictionaryInstance.dictionary.bot());
        botId.setValueXPath("bot/id");

        final DataSourceDateTimeField birthdate = new DataSourceDateTimeField("user.birthdate", DictionaryInstance.dictionary.birthdate());
        birthdate.setValueXPath("birthdate");

        final DataSourceDateTimeField joined = new DataSourceDateTimeField("user.joined", DictionaryInstance.dictionary.joinedDate(), 50, true);
        joined.setValueXPath("joined");

        final DataSourceDateTimeField lastMsg = new DataSourceDateTimeField("user.lastMsg", DictionaryInstance.dictionary.lastMsgDate(), 50, true);
        lastMsg.setValueXPath("lastMsg");

        final DataSourceIntegerField unreadMsgCount = new DataSourceIntegerField("user.unreadMsgCount", DictionaryInstance.dictionary.unreadMsgCount(), 50,
                true);
        unreadMsgCount.setValueXPath("unreadMsgCount");

        return new DataSourceField[] { pkField, name, surname, address, notes, nickId, operatorId, botId, birthdate, joined, lastMsg, unreadMsgCount };
    }

    private FormItem[] getFormFields() {
        final TextItem id = new TextItem("user.id", "ID");
        id.setDisabled(true);
        final TextItem name = new TextItem("user.name", DictionaryInstance.dictionary.name());
        final TextItem surname = new TextItem("user.surname", DictionaryInstance.dictionary.surname());
        final TextItem address = new TextItem("user.address", DictionaryInstance.dictionary.address());
        final TextAreaItem notes = new TextAreaItem("user.notes", DictionaryInstance.dictionary.notes());

        final SelectItem nickItem = new SelectItem("user.nick", DictionaryInstance.dictionary.nick());
        nickItem.setAttribute("jsonPath", "user/nick/id");
        nickItem.setAllowEmptyValue(true);
        nickItem.setEmptyDisplayValue(DictionaryInstance.dictionary.notSet());
        nickItem.setOptionDataSource(NicksDS.getInstance());
        nickItem.setDisplayField("name");
        nickItem.setValueField("id");

        final SelectItem operatorItem = new SelectItem("user.operator", DictionaryInstance.dictionary.operator());
        operatorItem.setAttribute("jsonPath", "user/operator/id");
        operatorItem.setAllowEmptyValue(true);
        operatorItem.setEmptyDisplayValue(DictionaryInstance.dictionary.notSet());
        operatorItem.setOptionDataSource(OperatorsDS.getInstance());
        operatorItem.setDisplayField("username");
        operatorItem.setValueField("id");

        final SelectItem botItem = new SelectItem("user.bot", DictionaryInstance.dictionary.bot());
        botItem.setAllowEmptyValue(true);
        botItem.setEmptyDisplayValue(DictionaryInstance.dictionary.notSet());
        botItem.setOptionDataSource(BotDS.getInstance());
        botItem.setDisplayField("name");
        botItem.setValueField("id");

        final DateItem birthdate = new DateItem("user.birthdate", DictionaryInstance.dictionary.birthdate());
        birthdate.setAttribute("jsonPath", "user/birthdate");
        birthdate.setMaskDateSeparator(".");
        birthdate.setUseMask(true);
        birthdate.setUseTextField(true);
        birthdate.setInvalidDateStringMessage(DictionaryInstance.dictionary.invalidDate());
        birthdate.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATE);

        final DateTimeItem joined = new DateTimeItem("user.joined", DictionaryInstance.dictionary.joinedDate());
        joined.setAttribute("jsonPath", "user/joined");
        joined.setMaskDateSeparator(".");
        joined.setUseMask(true);
        joined.setUseTextField(true);
        joined.setWidth(180);
        joined.setDisabled(true);
        joined.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);

        final DateTimeItem lastMsgDate = new DateTimeItem("user.lastMsg", DictionaryInstance.dictionary.lastMsgDate());
        lastMsgDate.setAttribute("jsonPath", "user/lastMsg");
        lastMsgDate.setMaskDateSeparator(".");
        lastMsgDate.setUseMask(true);
        lastMsgDate.setUseTextField(true);
        lastMsgDate.setWidth(180);
        lastMsgDate.setDisabled(true);
        lastMsgDate.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);

        final IntegerItem unreadMsgCount = new IntegerItem();
        unreadMsgCount.setAttribute("jsonPath", "user/unreadMsgCount");
        unreadMsgCount.setName("user.unreadMsgCount");
        unreadMsgCount.setTitle(DictionaryInstance.dictionary.unreadMsgCount());
        unreadMsgCount.setDisabled(true);

        return new FormItem[] { id, name, surname, address, notes, nickItem, operatorItem, botItem, birthdate, joined, lastMsgDate, unreadMsgCount };
    }

    // Getters & setters

    public final String getUserId() {
        return userId;
    }

    public final void setUserId(final String p_userId) {
        userId = p_userId;
    }

    public final String getUserType() {
        return userType;
    }

    public final void setUserType(final String p_userType) {
        userType = p_userType;
    }

    public final String getNameToDisplay() {
        return nameToDisplay;
    }

    public final void setNameToDisplay(final String p_nameToDisplay) {
        nameToDisplay = p_nameToDisplay;
    }

    public final boolean isDisplayUserForm() {
        return displayUserForm;
    }

    public final void setDisplayUserForm(final boolean p_displayUserForm) {
        displayUserForm = p_displayUserForm;
    }

    public final void setUnreadMsgCount(final double p_unreadMsgCount) {
        unreadMsgCount = p_unreadMsgCount;
    }

    public final double getUnreadMsgCount() {
        return unreadMsgCount;
    }

    public final void setUsersList(final SideNavigationMenu p_usersList) {
        usersList = p_usersList;
    }

    public final void setMyUserListNodeID(final String p_myUserListNodeID) {
        myUserListNodeID = p_myUserListNodeID;
    }

}
