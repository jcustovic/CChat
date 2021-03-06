package hr.chus.cchat.client.smartgwt.client.admin;

import hr.chus.cchat.client.smartgwt.client.admin.ds.NicksDS;
import hr.chus.cchat.client.smartgwt.client.admin.ds.OperatorsDS;
import hr.chus.cchat.client.smartgwt.client.admin.ds.ServiceProviderDS;
import hr.chus.cchat.client.smartgwt.client.admin.ds.UsersDS;
import hr.chus.cchat.client.smartgwt.client.common.Constants;
import hr.chus.cchat.client.smartgwt.client.common.PanelFactory;
import hr.chus.cchat.client.smartgwt.client.common.ds.BotDS;
import hr.chus.cchat.client.smartgwt.client.i18n.DictionaryInstance;

import java.util.Date;
import java.util.Iterator;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONString;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.XMLTools;
import com.smartgwt.client.data.fields.DataSourceDateTimeField;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.BooleanItem;
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
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public class Users extends HLayout {

    private static final String DESCRIPTION = "Users";

    private HLayout             rollOverCanvas;
    private ListGridRecord      rollOverRecord;
    private int                 offset      = 0;
    private int                 fetchSize   = 0;
    private Criteria            criteria    = null;

    public static class Factory implements PanelFactory {

        private String id;

        public final Canvas create() {
            final Users panel = new Users();
            panel.setMargin(5);
            panel.setWidth100();
            panel.setHeight100();
            panel.addMember(panel.getViewPanel());
            id = panel.getID();

            return panel;
        }

        public final String getID() {
            return id;
        }

        public final String getDescription() {
            return DESCRIPTION;
        }

    }

    public Canvas getViewPanel() {
        final Canvas canvas = new Canvas();

        NicksDS.getInstance().invalidateCache();
        OperatorsDS.getInstance().invalidateCache();
        ServiceProviderDS.getInstance().invalidateCache();

        VLayout layout = new VLayout(20);
        layout.setWidth(910);
        // layout.setHeight100();

        Label label = new Label();
        label.setHeight(10);
        // label.setWidth("70%");
        label.setContents(DictionaryInstance.dictionary.users());
        layout.addMember(label);

        final DynamicForm form = new DynamicForm();
        form.setVisible(false);
        // form.setWidth("60%");
        form.setIsGroup(true);
        form.setGroupTitle(DictionaryInstance.dictionary.update());
        form.setNumCols(4);
        form.setPadding(5);

        DataSource ds = new DataSource(Constants.CONTEXT_PATH + "admin/AdminUserFunctionJSON") {

            @Override
            protected void transformResponse(DSResponse response, DSRequest request, Object jsonData) {
                JSONArray value = XMLTools.selectObjects(jsonData, "/status");
                boolean status = false;
                if (value != null && value.size() > 0) {
                    status = ((JSONBoolean) value.get(0)).booleanValue();
                }
                if (!status) {
                    response.setStatus(RPCResponse.STATUS_VALIDATION_ERROR);
                    JSONArray errors = XMLTools.selectObjects(jsonData, "/errorFields");
                    response.setErrors(errors.getJavaScriptObject());
                }
            }
        };

        // For proper date format when submitting.
        DataSourceDateTimeField birthdate = new DataSourceDateTimeField("user.birthdate", DictionaryInstance.dictionary.birthdate());
        birthdate.setValueXPath("birthdate");
        ds.addField(birthdate);

        ds.setDataFormat(DSDataFormat.JSON);
        form.setDataSource(ds);

        form.setFields(getFormFields());

        final IButton updateButton = new IButton(DictionaryInstance.dictionary.update());
        updateButton.setVisible(false);

        final Label listLabel = new Label();
        listLabel.setVisible(false);
        listLabel.setHeight("10px");

        final ListGrid listGrid = new ListGrid() {

            @Override
            protected Canvas getRollOverCanvas(Integer rowNum, Integer colNum) {
                rollOverRecord = this.getRecord(rowNum);

                if (rollOverCanvas == null) {
                    rollOverCanvas = new HLayout(3);
                    rollOverCanvas.setSnapTo("TR");
                    rollOverCanvas.setWidth(50);
                    rollOverCanvas.setHeight(22);

                    ImgButton textImg = new ImgButton();
                    textImg.setShowDown(false);
                    textImg.setShowRollOver(false);
                    textImg.setLayoutAlign(Alignment.CENTER);
                    textImg.setSrc(Constants.CONTEXT_PATH + "images/message.png");
                    textImg.setPrompt(DictionaryInstance.dictionary.sendMessage());
                    textImg.setHeight(16);
                    textImg.setWidth(16);
                    textImg.setShowHover(true);
                    textImg.setPrompt(DictionaryInstance.dictionary.sendMessage());
                    textImg.addClickHandler(new ClickHandler() {

                        @Override
                        public void onClick(ClickEvent event) {
                            final Window window = new Window();
                            String user = null;
                            String userName = rollOverRecord.getAttribute("user.name");
                            String userSurname = rollOverRecord.getAttribute("user.surname");
                            if ((userName == null || userName.isEmpty()) && (userSurname == null || userSurname.isEmpty()))
                                user = "#" + rollOverRecord.getAttribute("user.id");
                            else
                                user = userName + userSurname;
                            window.setTitle(DictionaryInstance.dictionary.sendMessage() + " (" + user + ")");
                            window.setHeight(250);
                            window.setWidth(260);

                            DataSource ds = new DataSource(Constants.CONTEXT_PATH + "admin/SendSms");
                            ds.setDataFormat(DSDataFormat.JSON);
                            final DynamicForm sendMsgForm = new DynamicForm();
                            sendMsgForm.setPadding(5);
                            sendMsgForm.setAutoFocus(true);
                            sendMsgForm.setDataSource(ds);
                            sendMsgForm.setWidth(220);
                            sendMsgForm.setHeight(120);
                            sendMsgForm.setPadding(5);
                            sendMsgForm.setLayoutAlign(VerticalAlignment.BOTTOM);
                            HiddenItem userId = new HiddenItem("user");
                            userId.setValue(rollOverRecord.getAttribute("user.id"));
                            HiddenItem msgType = new HiddenItem("msgType");
                            msgType.setValue("sms");

                            final IntegerItem characterCount = new IntegerItem();
                            characterCount.setTitle(DictionaryInstance.dictionary.charactersAllowed());
                            characterCount.setValue(CChatAdminSmartGWT.maxMsgLength);
                            characterCount.setDisabled(true);
                            characterCount.setWidth(50);
                            final TextAreaItem smsTextArea = new TextAreaItem("textMsg", DictionaryInstance.dictionary.text());
                            String nickName = rollOverRecord.getAttribute("nick.name");
                            if (nickName != null && !nickName.isEmpty()) smsTextArea.setValue(nickName + ": ");
                            smsTextArea.setSelectOnFocus(false);
                            smsTextArea.setLength(CChatAdminSmartGWT.maxMsgLength);
                            smsTextArea.addKeyUpHandler(new KeyUpHandler() {

                                @Override
                                public void onKeyUp(KeyUpEvent event) {
                                    characterCount.setValue(CChatAdminSmartGWT.maxMsgLength - smsTextArea.getValue().toString().length());
                                }
                            });
                            sendMsgForm.setFields(userId, msgType, smsTextArea, characterCount);
                            sendMsgForm.focusInItem(smsTextArea.getName());

                            IButton sendMsg = new IButton(DictionaryInstance.dictionary.sendMessage());
                            sendMsg.setIcon(Constants.CONTEXT_PATH + "images/message.png");
                            sendMsg.setAutoFit(true);
                            sendMsg.addClickHandler(new ClickHandler() {

                                @Override
                                public void onClick(ClickEvent event) {
                                    sendMsgForm.submit(new DSCallback() {

                                        @Override
                                        public void execute(DSResponse response, Object rawData, DSRequest request) {
                                            JSONArray value = XMLTools.selectObjects(rawData, "/status");
                                            boolean status = ((JSONBoolean) value.get(0)).booleanValue();
                                            String msg = DictionaryInstance.dictionary.messageSentSuccessfully();
                                            if (!status) {
                                                value = XMLTools.selectObjects(rawData, "/errorMsg");
                                                String errorMsg = ((JSONString) value.get(0)).stringValue();
                                                msg = DictionaryInstance.dictionary.sendingMessageFailed() + " --> " + errorMsg;
                                            }
                                            SC.say(msg, new BooleanCallback() {

                                                @Override
                                                public void execute(Boolean value) {
                                                    window.destroy();
                                                }
                                            });
                                        };
                                    });
                                }
                            });
                            window.addItem(sendMsgForm);
                            window.addItem(sendMsg);

                            window.addCloseClickHandler(new CloseClickHandler() {

                                public void onCloseClick(CloseClickEvent event) {
                                    window.destroy();
                                }
                            });
                            window.setShowMinimizeButton(false);
                            window.setIsModal(true);
                            window.setShowModalMask(true);
                            window.centerInPage();
                            window.show();
                        }
                    });

                    rollOverCanvas.addMember(textImg);
                }
                return rollOverCanvas;
            }
        };
        listGrid.setShowRollOverCanvas(true);;
        listGrid.setLoadingDataMessage(DictionaryInstance.dictionary.loading());
        listGrid.setEmptyMessage(DictionaryInstance.dictionary.emptySet());
        listGrid.setLoadingMessage(DictionaryInstance.dictionary.loading());
        listGrid.setHeight(200);
        listGrid.setDataSource(UsersDS.getInstance());
        listGrid.setAutoFetchData(false);
        listGrid.setAnimateRemoveRecord(true);
        listGrid.addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                form.clearValues();
                form.setVisible(true);
                updateButton.setVisible(true);

                FormItem[] fields = form.getFields();
                form.editSelectedData(listGrid);
                Iterator<?> keySetIterator = form.getValues().keySet().iterator();
                while (keySetIterator.hasNext()) {
                    String key = (String) keySetIterator.next();
                    boolean toRemove = true;
                    for (FormItem field : fields) {
                        if (key.equals(field.getName())) {
                            toRemove = false;
                            break;
                        }
                    }
                    if (toRemove) {
                        form.clearValue(key);
                    }
                }
                form.setValue("operation", "update");
            }
        });

        listGrid.setUseAllDataSourceFields(false);
        listGrid.setFields(getGridFields());

        final DynamicForm searchForm = new DynamicForm();
        searchForm.setPadding(5);
        // searchForm.setWidth("60%");
        searchForm.setIsGroup(true);

        searchForm.setGroupTitle(DictionaryInstance.dictionary.search());
        searchForm.setNumCols(6);
        searchForm.setDataSource(UsersDS.getInstance());
        searchForm.setAutoFocus(false);

        searchForm.setFields(getSearchFormFields());

        final IButton nextButton = new IButton(DictionaryInstance.dictionary.next());
        nextButton.setIcon(Constants.CONTEXT_PATH + "images/arrow_right_green.png");
        final IButton previousButton = new IButton(DictionaryInstance.dictionary.previous());
        previousButton.setIcon(Constants.CONTEXT_PATH + "images/arrow_left_green.png");

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
                        int totalCount = (int) UsersDS.getInstance().getTotalCount();
                        listLabel.setContents(DictionaryInstance.dictionary.totalUsersInDB() + ": <b>" + totalCount + "&nbsp;&nbsp;(" + offset + " - "
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
            public void onClick(ClickEvent event) {
                offset -= fetchSize;
                criteria.setAttribute("limit", fetchSize);
                criteria.setAttribute("start", offset);
                listGrid.invalidateCache();
                listGrid.fetchData(criteria, new DSCallback() {

                    @Override
                    public void execute(DSResponse response, Object rawData, DSRequest request) {
                        int totalCount = (int) UsersDS.getInstance().getTotalCount();
                        listLabel.setContents(DictionaryInstance.dictionary.totalUsersInDB() + ": <b>" + totalCount + "&nbsp;&nbsp;(" + offset + " - "
                                + (offset + fetchSize) + ")</b>");
                        if (offset <= 0) previousButton.setVisible(false);
                        listLabel.setVisible(true);
                        nextButton.setVisible(true);
                    }
                });
            }
        });

        IButton searchButton = new IButton(DictionaryInstance.dictionary.search());
        searchButton.setIcon(Constants.CONTEXT_PATH + "images/find.png");
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
                        int totalCount = (int) UsersDS.getInstance().getTotalCount();
                        listLabel.setContents(DictionaryInstance.dictionary.totalUsersInDB() + ": <b>" + totalCount + "&nbsp;&nbsp;(" + offset + " - "
                                + (offset + fetchSize) + ")</b>");
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

        updateButton.setIcon(Constants.CONTEXT_PATH + "images/edit.png");
        updateButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (form.validate()) {
                    Iterator<?> keySetIterator = form.getValues().keySet().iterator();
                    while (keySetIterator.hasNext()) {
                        String key = (String) keySetIterator.next();
                        Object value = form.getValues().get(key);
                        if (value == null) form.clearValue(key);
                    }

                    form.submit(new DSCallback() {

                        @Override
                        public void execute(DSResponse response, Object jsonData, DSRequest request) {
                            JSONArray value = XMLTools.selectObjects(jsonData, "/status");
                            String status = null;
                            if (value != null && value.size() > 0) {
                                status = ((JSONString) value.get(0)).stringValue();
                            }
                            if (status == null || !status.equals("validation_error")) {
                                listGrid.invalidateCache();
                                listGrid.fetchData(searchForm.getValuesAsCriteria());
                                form.setVisible(false);
                                updateButton.setVisible(false);
                            }
                        }

                    });
                }
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

        layout.addMember(form);
        layout.addMember(updateButton);

        canvas.addChild(layout);

        return canvas;
    }

    private FormItem[] getFormFields() {
        final TextItem id = new TextItem("user.id", "ID");
        id.setDisabled(true);
        final TextItem msisdn = new TextItem("user.msisdn", DictionaryInstance.dictionary.msisdn());
        msisdn.setDisabled(true);
        final TextItem name = new TextItem("user.name", DictionaryInstance.dictionary.name());
        final TextItem surname = new TextItem("user.surname", DictionaryInstance.dictionary.surname());
        final TextItem address = new TextItem("user.address", DictionaryInstance.dictionary.address());
        final TextAreaItem notes = new TextAreaItem("user.notes", DictionaryInstance.dictionary.notes());

        final SelectItem nickItem = new SelectItem("user.nick", DictionaryInstance.dictionary.nick());
        nickItem.setAllowEmptyValue(true);
        nickItem.setEmptyDisplayValue(DictionaryInstance.dictionary.notSet());
        nickItem.setOptionDataSource(NicksDS.getInstance());
        nickItem.setDisplayField("name");
        nickItem.setValueField("id");

        final SelectItem operatorItem = new SelectItem("user.operator", DictionaryInstance.dictionary.operator());
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

        final SelectItem serviceProviderItem = new SelectItem("user.serviceProvider", DictionaryInstance.dictionary.serviceProvider());
        serviceProviderItem.setAllowEmptyValue(true);
        serviceProviderItem.setEmptyDisplayValue(DictionaryInstance.dictionary.notSet());
        serviceProviderItem.setOptionDataSource(ServiceProviderDS.getInstance());
        serviceProviderItem.setDisplayField("providerName");
        serviceProviderItem.setValueField("id");
        serviceProviderItem.setDisabled(true);

        final DateItem birthdate = new DateItem("user.birthdate", DictionaryInstance.dictionary.birthdate());
        birthdate.setMaskDateSeparator(".");
        birthdate.setUseMask(true);
        birthdate.setUseTextField(true);
        birthdate.setInvalidDateStringMessage(DictionaryInstance.dictionary.invalidDate());
        birthdate.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATE);

        final BooleanItem deleted = new BooleanItem();
        deleted.setDefaultValue(false);
        deleted.setName("user.deleted");
        deleted.setTitle(DictionaryInstance.dictionary.deleted());

        final DateTimeItem joined = new DateTimeItem("user.joined", DictionaryInstance.dictionary.joinedDate());
        joined.setMaskDateSeparator(".");
        joined.setUseMask(true);
        joined.setUseTextField(true);
        joined.setWidth(180);
        joined.setDisabled(true);
        joined.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);

        final DateTimeItem lastMsgDate = new DateTimeItem("user.lastMsg", DictionaryInstance.dictionary.lastMsgDate());
        lastMsgDate.setMaskDateSeparator(".");
        lastMsgDate.setUseMask(true);
        lastMsgDate.setUseTextField(true);
        lastMsgDate.setWidth(180);
        lastMsgDate.setDisabled(true);
        lastMsgDate.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);

        final IntegerItem unreadMsgCount = new IntegerItem();
        unreadMsgCount.setName("user.unreadMsgCount");
        unreadMsgCount.setTitle(DictionaryInstance.dictionary.unreadMsgCount());
        unreadMsgCount.setDisabled(true);

        return new FormItem[] { id, msisdn, name, surname, address, notes, nickItem, operatorItem, botItem, serviceProviderItem, birthdate, deleted, joined,
                lastMsgDate, unreadMsgCount };
    }

    private FormItem[] getSearchFormFields() {
        final IntegerItem userId = new IntegerItem();
        userId.setName("id");
        userId.setTitle(DictionaryInstance.dictionary.userId());
        final TextItem msisdn = new TextItem("msisdn", DictionaryInstance.dictionary.msisdn());
        final TextItem name = new TextItem("name", DictionaryInstance.dictionary.name());
        final TextItem surname = new TextItem("surname", DictionaryInstance.dictionary.surname());

        final SelectItem nickItem = new SelectItem("nick", DictionaryInstance.dictionary.nick());
        nickItem.setAllowEmptyValue(true);
        nickItem.setEmptyDisplayValue(DictionaryInstance.dictionary.notSet());
        nickItem.setOptionDataSource(NicksDS.getInstance());
        nickItem.setDisplayField("name");
        nickItem.setValueField("id");

        final SelectItem operatorItem = new SelectItem("operator", DictionaryInstance.dictionary.operator());
        operatorItem.setAllowEmptyValue(true);
        operatorItem.setEmptyDisplayValue(DictionaryInstance.dictionary.notSet());
        operatorItem.setOptionDataSource(OperatorsDS.getInstance());
        operatorItem.setDisplayField("username");
        operatorItem.setValueField("id");

        final SelectItem serviceProviderItem = new SelectItem("serviceProvider", DictionaryInstance.dictionary.serviceProvider());
        serviceProviderItem.setAllowEmptyValue(true);
        serviceProviderItem.setEmptyDisplayValue(DictionaryInstance.dictionary.notSet());
        serviceProviderItem.setOptionDataSource(ServiceProviderDS.getInstance());
        serviceProviderItem.setDisplayField("providerName");
        serviceProviderItem.setValueField("id");

        final SelectItem fetchSize = new SelectItem("limit", DictionaryInstance.dictionary.fetchSize());
        fetchSize.setValueMap("20", "50", "100", "200");
        fetchSize.setDefaultToFirstOption(true);

        return new FormItem[] { userId, msisdn, name, surname, nickItem, operatorItem, serviceProviderItem, fetchSize };
    }

    private ListGridField[] getGridFields() {
        final ListGridField id = new ListGridField("user.id");
        id.setHidden(true);
        final ListGridField msisdn = new ListGridField("user.msisdn");
        msisdn.setWidth(120);
        final ListGridField name = new ListGridField("user.name");
        name.setWidth(100);
        final ListGridField surname = new ListGridField("user.surname");
        surname.setWidth(140);
        final ListGridField address = new ListGridField("user.address");
        address.setHidden(true);
        final ListGridField notes = new ListGridField("user.notes");
        notes.setHidden(true);
        final ListGridField nickName = new ListGridField("nick.name");
        nickName.setWidth(100);
        final ListGridField operatorUsername = new ListGridField("operator.username");
        operatorUsername.setWidth(100);
        final ListGridField nick = new ListGridField("user.nick");
        nick.setHidden(true);
        final ListGridField operator = new ListGridField("user.operator");
        operator.setHidden(true);
        final ListGridField serviceProviderName = new ListGridField("serviceProvider.providerName");
        serviceProviderName.setWidth(120);
        final ListGridField bot = new ListGridField("user.bot");
        bot.setHidden(true);

        final ListGridField deleted = new ListGridField("user.deleted");
        deleted.setWidth(50);

        final DateTimeFormat joinedDateFormatter = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm");
        final ListGridField joined = new ListGridField("user.joined");
        joined.setCellFormatter(new CellFormatter() {

            public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
                if (value != null) {
                    try {
                        Date dateValue = (Date) value;
                        return joinedDateFormatter.format(dateValue);
                    } catch (Exception e) {
                        return value.toString();
                    }
                } else {
                    return "";
                }
            }
        });
        joined.setAlign(Alignment.LEFT);
        joined.setType(ListGridFieldType.DATE);
        joined.setWidth(140);

        final DateTimeFormat birthDateFormatter = DateTimeFormat.getFormat("dd.MM.yyyy");
        final ListGridField birthDate = new ListGridField("user.birthdate");
        birthDate.setCellFormatter(new CellFormatter() {

            public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
                if (value != null) {
                    try {
                        Date dateValue = (Date) value;
                        return birthDateFormatter.format(dateValue);
                    } catch (Exception e) {
                        return value.toString();
                    }
                } else {
                    return "";
                }
            }
        });
        birthDate.setType(ListGridFieldType.DATE);
        birthDate.setHidden(true);

        return new ListGridField[] { id, msisdn, name, surname, address, notes, nickName, operator, operatorUsername, serviceProviderName, bot, nick, deleted,
                joined, birthDate };
    }

}
