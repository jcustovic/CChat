package hr.chus.cchat.client.smartgwt.client.admin;

import hr.chus.cchat.client.smartgwt.client.admin.ds.LanguageProviderDS;
import hr.chus.cchat.client.smartgwt.client.admin.ds.SendServiceBeanDS;
import hr.chus.cchat.client.smartgwt.client.admin.ds.ServiceProviderDS;
import hr.chus.cchat.client.smartgwt.client.common.Constants;
import hr.chus.cchat.client.smartgwt.client.common.PanelFactory;
import hr.chus.cchat.client.smartgwt.client.ds.SuccessResultsDataSource;
import hr.chus.cchat.client.smartgwt.client.i18n.DictionaryInstance;

import java.util.Iterator;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.XMLTools;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.BooleanItem;
import com.smartgwt.client.widgets.form.fields.FloatItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public class MsisdnRoutingLayout extends HLayout {

    private static final String DESCRIPTION = "MsisdnRouting";

    public static class Factory implements PanelFactory {

        private String id;

        public Canvas create() {
            MsisdnRoutingLayout panel = new MsisdnRoutingLayout();
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

        VLayout layout = new VLayout(20);
        layout.setWidth(500);
        // layout.setHeight100();

        Label label = new Label();
        label.setHeight(10);
        label.setWidth100();
        label.setContents(DictionaryInstance.dictionary.msisdnRouting());
        layout.addMember(label);

        canvas.addChild(layout);

        final DynamicForm form = new DynamicForm();
        form.setVisible(false);
        form.setWidth(400);
        form.setPadding(5);
        form.setIsGroup(true);
        form.setNumCols(4);
        form.setGroupTitle(DictionaryInstance.dictionary.addNew());

        final SuccessResultsDataSource ds = new SuccessResultsDataSource();
        ds.setDataFormat(DSDataFormat.JSON);
        form.setDataSource(ds);

        form.setFields(getFormFields());

        final IButton deleteButton = new IButton(DictionaryInstance.dictionary.delete());
        final IButton saveButton = new IButton(DictionaryInstance.dictionary.save());

        final ListGrid listGrid = new ListGrid();
        listGrid.setLoadingDataMessage(DictionaryInstance.dictionary.loading());
        listGrid.setEmptyMessage(DictionaryInstance.dictionary.emptySet());
        listGrid.setLoadingMessage(DictionaryInstance.dictionary.loading());
        listGrid.setWidth(900);
        listGrid.setHeight(200);
        listGrid.setDataSource(ServiceProviderDS.getInstance());
        listGrid.setAutoFetchData(true);
        listGrid.addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                form.setGroupTitle(DictionaryInstance.dictionary.update());
                form.clearValues();
                form.setVisible(true);
                saveButton.setDisabled(false);
                deleteButton.setDisabled(false);

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
                ds.setAlternateUrl(Constants.CONTEXT_PATH + "admin/serviceProvider/save");
            }
        });

        listGrid.setUseAllDataSourceFields(false);
        listGrid.setFields(getGridFields());

        layout.addMember(listGrid);
        layout.addMember(form);

        saveButton.setDisabled(true);
        saveButton.setShowDisabledIcon(false);
        saveButton.setIcon(Constants.CONTEXT_PATH + "images/edit.png");
        saveButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                Iterator<?> keySetIterator = form.getValues().keySet().iterator();
                while (keySetIterator.hasNext()) {
                    String key = (String) keySetIterator.next();
                    Object value = form.getValues().get(key);
                    if (value == null) form.clearValue(key);
                }

                if (form.validate()) {
                    form.submit(new DSCallback() {

                        @Override
                        public void execute(DSResponse response, Object p_jsonData, DSRequest request) {
                            final JSONArray value = XMLTools.selectObjects(p_jsonData, "/success");
                            final Boolean status = ((JSONBoolean) value.get(0)).booleanValue();
                            if (status) {
                                listGrid.invalidateCache();
                                form.setVisible(false);
                                deleteButton.setDisabled(true);
                                saveButton.setDisabled(true);
                            }
                        }

                    });
                }
            }
        });

        IButton addNewButton = new IButton(DictionaryInstance.dictionary.addNew());
        addNewButton.setIcon(Constants.CONTEXT_PATH + "images/new.png");
        addNewButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                form.setGroupTitle(DictionaryInstance.dictionary.addNew());
                form.editNewRecord();
                form.clearErrors(true);
                ds.setAlternateUrl(Constants.CONTEXT_PATH + "admin/serviceProvider/save");
                form.setVisible(true);
                deleteButton.setDisabled(true);
                saveButton.setDisabled(false);
            }
        });

        deleteButton.setDisabled(true);
        deleteButton.setShowDisabledIcon(false);
        deleteButton.setIcon(Constants.CONTEXT_PATH + "images/delete.png");
        deleteButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ds.setAlternateUrl(Constants.CONTEXT_PATH + "admin/serviceProvider/delete");
                form.submit(new DSCallback() {

                    @Override
                    public void execute(DSResponse response, Object p_jsonData, DSRequest request) {
                        final JSONArray value = XMLTools.selectObjects(p_jsonData, "/success");
                        final Boolean status = ((JSONBoolean) value.get(0)).booleanValue();
                        if (status) {
                            deleteButton.setDisabled(true);
                            saveButton.setDisabled(true);
                            form.setVisible(false);
                            listGrid.invalidateCache();
                        }
                    }

                });
            }
        });

        HLayout buttons = new HLayout(5);
        buttons.addMember(saveButton);
        buttons.addMember(addNewButton);
        buttons.addMember(deleteButton);

        layout.addMember(buttons);

        canvas.addChild(layout);

        return canvas;
    }

    private FormItem[] getFormFields() {
        HiddenItem id = new HiddenItem("serviceProvider.id");
        TextItem shortCode = new TextItem("serviceProvider.sc", DictionaryInstance.dictionary.shortCode());
        shortCode.setLength(20);
        shortCode.setRequired(true);
        TextItem providerName = new TextItem("serviceProvider.providerName", DictionaryInstance.dictionary.providerName());
        providerName.setLength(30);
        providerName.setRequired(true);
        TextItem serviceName = new TextItem("serviceProvider.serviceName", DictionaryInstance.dictionary.serviceName());
        serviceName.setLength(30);
        serviceName.setRequired(true);
        FloatItem billingAmount = new FloatItem("serviceProvider.billingAmount", DictionaryInstance.dictionary.billingAmount());
        TextItem description = new TextItem("serviceProvider.description", DictionaryInstance.dictionary.description());
        description.setLength(200);
        BooleanItem disabled = new BooleanItem();
        disabled.setDefaultValue(false);
        disabled.setName("serviceProvider.disabled");
        disabled.setTitle(DictionaryInstance.dictionary.isDisabled());
        
        SelectItem sendServiceBeanNamePickList = new SelectItem("serviceProvider.sendServiceBeanName", DictionaryInstance.dictionary.sendServiceBeanName());
        sendServiceBeanNamePickList.setOptionDataSource(SendServiceBeanDS.getInstance());
        sendServiceBeanNamePickList.setDisplayField("name");
        sendServiceBeanNamePickList.setValueField("name");
        sendServiceBeanNamePickList.setRequired(false);
        
        final SelectItem languageProviderPickList = new SelectItem("serviceProvider.languageProvider.id");
        languageProviderPickList.setTitle(DictionaryInstance.dictionary.languageProviderPrefix());
        languageProviderPickList.setPickListWidth(300);
        languageProviderPickList.setOptionDataSource(LanguageProviderDS.getInstance());
        languageProviderPickList.setDisplayField("prefix");
        languageProviderPickList.setValueField("id");

        return new FormItem[] { id, shortCode, providerName, serviceName, billingAmount, description, disabled, sendServiceBeanNamePickList, languageProviderPickList };
    }

    private ListGridField[] getGridFields() {
        ListGridField id = new ListGridField("serviceProvider.id", DictionaryInstance.dictionary.name(), 40);
        ListGridField name = new ListGridField("serviceProvider.sc", DictionaryInstance.dictionary.shortCode(), 80);
        ListGridField providerName = new ListGridField("serviceProvider.providerName", DictionaryInstance.dictionary.providerName(), 120);
        ListGridField serviceName = new ListGridField("serviceProvider.serviceName", DictionaryInstance.dictionary.serviceName(), 120);
        ListGridField billingAmount = new ListGridField("serviceProvider.billingAmount", DictionaryInstance.dictionary.billingAmount(), 80);
        ListGridField disabled = new ListGridField("serviceProvider.disabled", DictionaryInstance.dictionary.isDisabled(), 50);
        ListGridField sendServiceBeanName = new ListGridField("serviceProvider.sendServiceBeanName", DictionaryInstance.dictionary.sendServiceBeanName());
        ListGridField autoCreated = new ListGridField("serviceProvider.autoCreated", DictionaryInstance.dictionary.autoCreated(), 60);
        
        ListGridField languageProvider = new ListGridField("serviceProvider.languageProvider.id", 90);
        languageProvider.setOptionDataSource(LanguageProviderDS.getInstance());
        languageProvider.setValueField("id");
        languageProvider.setDisplayField("prefix");

        return new ListGridField[] { id, name, providerName, serviceName, billingAmount, disabled, sendServiceBeanName, autoCreated, languageProvider };
    }


}
