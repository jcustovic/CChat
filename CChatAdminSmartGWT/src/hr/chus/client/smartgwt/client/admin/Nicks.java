package hr.chus.client.smartgwt.client.admin;

import hr.chus.client.smartgwt.client.CChatAdminSmartGWT;
import hr.chus.client.smartgwt.client.PanelFactory;
import hr.chus.client.smartgwt.client.admin.ds.NicksDS;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.XMLTools;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class Nicks extends HLayout {
	
    private static final String DESCRIPTION = "Nicks";

    public static class Factory implements PanelFactory {
        
    	private String id;

        public Canvas create() {
        	Nicks panel = new Nicks();
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
		Canvas canvas = new Canvas();

		VLayout layout = new VLayout(10);
		layout.setWidth("35%");
		layout.setHeight100();

		Label label = new Label();
		label.setHeight(10);
		label.setWidth100();
		label.setContents(CChatAdminSmartGWT.dictionary.nicks());
		layout.addMember(label);

		final DynamicForm form = new DynamicForm();
		form.setVisible(false);
		form.setWidth("30%");
		form.setIsGroup(true);
		form.setGroupTitle(CChatAdminSmartGWT.dictionary.addNew());
		
//		DataSource ds = new DataSource("test/data/json/empty.json") {
		DataSource ds = new DataSource(CChatAdminSmartGWT.CONTEXT_PATH + "admin/AdminNickFunctionJSON") {
			@Override
			protected void transformResponse(DSResponse response, DSRequest request, Object jsonData) {
				JSONArray value = XMLTools.selectObjects(jsonData, "/status");
				String status = ((JSONString)value.get(0)).stringValue();
				if (!status.equals("validation_ok")) {
					response.setStatus(RPCResponse.STATUS_VALIDATION_ERROR);
					JSONArray errors = XMLTools.selectObjects(jsonData, "/errorFields");
					response.setErrors(errors.getJavaScriptObject());
				}
			}
		};
		ds.setDataFormat(DSDataFormat.JSON);
		form.setDataSource(ds);
		
		form.setFields(getFormFields());
//		form.setShowInlineErrors(false);
//		form.setErrorsPreamble(CChatAdminSmartGWT.dictionary.errors());
		
		final IButton deleteButton = new IButton(CChatAdminSmartGWT.dictionary.delete());
		final IButton saveButton = new IButton(CChatAdminSmartGWT.dictionary.save());

		final ListGrid listGrid = new ListGrid();
		listGrid.setLoadingDataMessage(CChatAdminSmartGWT.dictionary.loading());
		listGrid.setEmptyMessage(CChatAdminSmartGWT.dictionary.emptySet());
		listGrid.setLoadingMessage(CChatAdminSmartGWT.dictionary.loading());
		listGrid.setWidth100();
		listGrid.setHeight(200);
		listGrid.setDataSource(NicksDS.getInstance());
		listGrid.setAutoFetchData(true);
		listGrid.addRecordClickHandler(new RecordClickHandler() {

			@Override
			public void onRecordClick(RecordClickEvent event) {
				form.setGroupTitle(CChatAdminSmartGWT.dictionary.update());
				form.clearValues();
				form.setVisible(true);
				saveButton.setDisabled(false);
				deleteButton.setDisabled(false);
				
				FormItem[] fields = form.getFields();
				ListGridRecord selected = listGrid.getSelectedRecord();
				for (FormItem field : fields) {
					String attribute = selected.getAttribute(field.getName());
					if (attribute != null) {
						form.setValue(field.getName(), attribute);
					}
				}
				form.setValue("operation", "save/edit");
			}
		});
		
		listGrid.setUseAllDataSourceFields(false);
		listGrid.setFields(getGridFields());

		layout.addMember(listGrid);
		layout.addMember(form);

		saveButton.setDisabled(true);
		saveButton.setShowDisabledIcon(false);
		saveButton.setIcon(CChatAdminSmartGWT.CONTEXT_PATH + "images/edit.png");
		saveButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (form.validate()) {
					form.submit(new DSCallback() {

						@Override
						public void execute(DSResponse response, Object jsonData, DSRequest request) {
							JSONArray value = XMLTools.selectObjects(jsonData, "/status");
							String status = null;
							if (value != null && value.size() > 0) {
								status = ((JSONString)value.get(0)).stringValue();
							}
							if (status == null || !status.equals("validation_error")) {
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
		
		IButton addNewButton = new IButton(CChatAdminSmartGWT.dictionary.addNew());
		addNewButton.setIcon(CChatAdminSmartGWT.CONTEXT_PATH + "images/new.png");
		addNewButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				form.setGroupTitle(CChatAdminSmartGWT.dictionary.addNew());
				form.editNewRecord();
				form.clearErrors(true);
				form.setValue("operation", "save/edit");
				form.setVisible(true);
				deleteButton.setDisabled(true);
				saveButton.setDisabled(false);
			}
		});
		
		deleteButton.setDisabled(true);
		deleteButton.setShowDisabledIcon(false);
		deleteButton.setIcon(CChatAdminSmartGWT.CONTEXT_PATH + "images/delete.png");
		deleteButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				form.setValue("operation", "delete");
				form.submit();
				deleteButton.setDisabled(true);
				saveButton.setDisabled(true);
				form.setVisible(false);
				listGrid.invalidateCache();
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

    /**
     * 
     * @return
     */
	public String getIntro() {
        return DESCRIPTION;
    }
	
	private FormItem[] getFormFields() {
    	HiddenItem id = new HiddenItem("nick.id");
    	TextItem name = new TextItem("nick.name", CChatAdminSmartGWT.dictionary.name());
    	name.setRequired(true);
    	TextItem description = new TextItem("nick.description", CChatAdminSmartGWT.dictionary.description());
		return new FormItem[] { id, name, description };
	}
	
	/**
	 * 
	 * @return
	 */
	private ListGridField[] getGridFields() {
		ListGridField name = new ListGridField("nick.name", CChatAdminSmartGWT.dictionary.name());
		ListGridField description = new ListGridField("nick.description", CChatAdminSmartGWT.dictionary.description());
		return new ListGridField[] { name, description };
	}
}
