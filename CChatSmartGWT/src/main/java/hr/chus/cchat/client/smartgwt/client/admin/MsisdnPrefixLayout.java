package hr.chus.cchat.client.smartgwt.client.admin;

import hr.chus.cchat.client.smartgwt.client.admin.ds.LanguageDS;
import hr.chus.cchat.client.smartgwt.client.admin.ds.LanguageProviderDS;
import hr.chus.cchat.client.smartgwt.client.admin.ds.SendServiceBeanDS;
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

public class MsisdnPrefixLayout extends HLayout {

    private static final String DESCRIPTION = "MsisdnPrefix";

    public static class Factory implements PanelFactory {

        private String id;

        public Canvas create() {
            MsisdnPrefixLayout panel = new MsisdnPrefixLayout();
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
        label.setContents(DictionaryInstance.dictionary.msisdnPrefix());
        layout.addMember(label);

        canvas.addChild(layout);

        final DynamicForm form = new DynamicForm();
        form.setVisible(false);
        form.setWidth("30%");
        form.setPadding(5);
        form.setIsGroup(true);
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
        listGrid.setWidth(500);
        listGrid.setHeight(200);
        listGrid.setDataSource(LanguageProviderDS.getInstance());
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
                ds.setAlternateUrl(Constants.CONTEXT_PATH + "admin/languageProvider/save");
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
                ds.setAlternateUrl(Constants.CONTEXT_PATH + "admin/languageProvider/save");
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
                ds.setAlternateUrl(Constants.CONTEXT_PATH + "admin/languageProvider/delete");
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
        HiddenItem id = new HiddenItem("languageProvider.id");
        TextItem prefix = new TextItem("languageProvider.prefix", DictionaryInstance.dictionary.prefix());
        prefix.setLength(15);
        prefix.setRequired(true);
        SelectItem sendServiceBeanNamePickList = new SelectItem("languageProvider.sendServiceBeanName", DictionaryInstance.dictionary.sendServiceBeanName());
        sendServiceBeanNamePickList.setOptionDataSource(SendServiceBeanDS.getInstance());
        sendServiceBeanNamePickList.setDisplayField("name");
        sendServiceBeanNamePickList.setValueField("name");
        sendServiceBeanNamePickList.setRequired(false);

        final SelectItem languagePickList = new SelectItem("languageProvider.language.id");
        languagePickList.setTitle(DictionaryInstance.dictionary.language());
        languagePickList.setPickListWidth(300);
        languagePickList.setOptionDataSource(LanguageDS.getInstance());
        languagePickList.setDisplayField("shortCode");
        languagePickList.setValueField("id");

        return new FormItem[] { id, prefix, sendServiceBeanNamePickList, languagePickList };
    }

    private ListGridField[] getGridFields() {
        ListGridField prefix = new ListGridField("languageProvider.prefix", DictionaryInstance.dictionary.prefix(), 90);
        ListGridField sendServiceBeanName = new ListGridField("languageProvider.sendServiceBeanName", DictionaryInstance.dictionary.sendServiceBeanName());
        
        ListGridField languageField = new ListGridField("languageProvider.language.id", 60);
        languageField.setOptionDataSource(LanguageDS.getInstance());
        languageField.setValueField("id");
        languageField.setDisplayField("shortCode");

        return new ListGridField[] { prefix, sendServiceBeanName, languageField };
    }

}
