package hr.chus.cchat.client.smartgwt.client.admin;

import java.util.Iterator;

import hr.chus.cchat.client.smartgwt.client.admin.ds.NicksDS;
import hr.chus.cchat.client.smartgwt.client.admin.ds.PicturesDS;
import hr.chus.cchat.client.smartgwt.client.common.Constants;
import hr.chus.cchat.client.smartgwt.client.common.PanelFactory;
import hr.chus.cchat.client.smartgwt.client.filemanager.Upload;
import hr.chus.cchat.client.smartgwt.client.filemanager.UploadListener;
import hr.chus.cchat.client.smartgwt.client.i18n.DictionaryInstance;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.XMLTools;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Img;
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
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 *
 */
public class Pictures extends HLayout {
	
    private static final String DESCRIPTION = "Pictures";

    public static class Factory implements PanelFactory {
        
    	private String id;

        public Canvas create() {
        	Pictures panel = new Pictures();
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

    	NicksDS.getInstance().invalidateCache();
    	
		VLayout layout = new VLayout(20);
		layout.setWidth("95%");
		
		Label label = new Label();
		label.setHeight(10);
		label.setWidth("70%");
		label.setContents(DictionaryInstance.dictionary.pictures());
		layout.addMember(label);
		
		final DynamicForm form = new DynamicForm();
		form.setDisabled(true);
		form.setWidth("550px");
		form.setIsGroup(true);
		form.setGroupTitle(DictionaryInstance.dictionary.update());
		form.setNumCols(4);
		
		DataSource ds = new DataSource(Constants.CONTEXT_PATH + "admin/AdminPictureFunctionJSON") {
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
		
		final IButton deleteButton = new IButton(DictionaryInstance.dictionary.delete());
		final IButton saveButton = new IButton(DictionaryInstance.dictionary.save());

		HLayout hlayout = new HLayout(10);
		
		final Img img = new Img();
		img.setHeight(150);
		img.setWidth(150);
		img.setLeft(180);
		img.setTop(180);
		img.setHoverWidth(120);
		img.setHoverOpacity(75);
		img.setHoverStyle("interactImageHover");
		img.setVisible(false);
		
		final ListGrid listGrid = new ListGrid();
		listGrid.setLoadingDataMessage(DictionaryInstance.dictionary.loading());
		listGrid.setEmptyMessage(DictionaryInstance.dictionary.emptySet());
		listGrid.setLoadingMessage(DictionaryInstance.dictionary.loading());
		listGrid.setWidth("620px");
		listGrid.setHeight(200);
		listGrid.setShowFilterEditor(true);  
		listGrid.setFilterOnKeypress(true);
		listGrid.setDataSource(PicturesDS.getInstance());
		listGrid.setAutoFetchData(true);
		listGrid.setAnimateRemoveRecord(true);
		listGrid.addRecordClickHandler(new RecordClickHandler() {

			@Override
			public void onRecordClick(RecordClickEvent event) {
				form.clearValues();
				form.setDisabled(false);
				saveButton.setDisabled(false);
				deleteButton.setDisabled(false);
				
				FormItem[] fields = form.getFields();
				ListGridRecord selected = listGrid.getSelectedRecord();
				for (FormItem field : fields) {
					String attribute = selected.getAttribute(field.getName());
					if (field.getName().equals("picture.url")) {
						img.setVisible(true);
						img.setPrompt(Constants.CONTEXT_PATH + attribute);
						img.setSrc(Constants.CONTEXT_PATH + attribute);
					}
					if (attribute != null) {
						form.setValue(field.getName(), attribute);
					}
				}
				form.setValue("operation", "update");
			}
		});
		
		listGrid.setUseAllDataSourceFields(false);
		listGrid.setFields(getGridFields());

		hlayout.addMember(listGrid);
		hlayout.addMember(img);
		
		layout.addMember(hlayout);
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
						public void execute(DSResponse response, Object jsonData, DSRequest request) {
							JSONArray value = XMLTools.selectObjects(jsonData, "/status");
							String status = null;
							if (value != null && value.size() > 0) {
								status = ((JSONString) value.get(0)).stringValue();
							}
							if (status == null || !status.equals("validation_error")) {
								listGrid.invalidateCache();
								form.setDisabled(true);
								deleteButton.setDisabled(true);
								saveButton.setDisabled(true);
							}
						}
						
					});
				}
			}
		});
				
		deleteButton.setDisabled(true);
		deleteButton.setShowDisabledIcon(false);
		deleteButton.setIcon(Constants.CONTEXT_PATH + "images/delete.png");
		deleteButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				form.setValue("operation", "delete");
				form.submit(new DSCallback() {

					@Override
					public void execute(DSResponse response, Object jsonData, DSRequest request) {
						JSONArray value = XMLTools.selectObjects(jsonData, "/status");
						String status = null;
						if (value != null && value.size() > 0) {
							status = ((JSONString) value.get(0)).stringValue();
						}
						if (status == null || !status.equals("validation_error")) {
							deleteButton.setDisabled(true);
							saveButton.setDisabled(true);
							form.setDisabled(true);
							img.setVisible(false);
							listGrid.invalidateCache();
						}
					}
					
				});
			}
		});
		
		HLayout buttons = new HLayout(5);
		buttons.setHeight("20px");
		buttons.addMember(saveButton);
		buttons.addMember(deleteButton);
		
		layout.addMember(buttons);
		
		SelectItem selectNick = new SelectItem("nick", DictionaryInstance.dictionary.nick());
		selectNick.setEmptyDisplayValue(DictionaryInstance.dictionary.notSet());
		selectNick.setAllowEmptyValue(true);
		selectNick.setOptionDataSource(NicksDS.getInstance());
		selectNick.setDisplayField("name");
		selectNick.setValueField("id");
		selectNick.setAnimatePickList(true);
		selectNick.setPickListWidth(250);
		
		Upload upload = new Upload(Constants.CONTEXT_PATH + "admin/AdminPictureUploadAction", "picture", DictionaryInstance.dictionary.picture(), selectNick);
		upload.setBorder("1px solid gray");
		upload.setHeight("60px");
		upload.setWidth("400px");
		upload.setUploadListener(new UploadListener() {
			
			@Override
			public void uploadError(String msg) {
				SC.warn(msg);
			}
			
			@Override
			public void uploadComplete(String msg) {
				listGrid.invalidateCache();
			}
		});
		layout.addMember(upload);
		
		canvas.addChild(layout);
		
		return canvas;
	}

	private FormItem[] getFormFields() {
		HiddenItem id = new HiddenItem("picture.id");
		TextItem name = new TextItem("picture.name", DictionaryInstance.dictionary.name());
		name.setDisabled(true);
		TextItem length = new TextItem("picture.length", DictionaryInstance.dictionary.sizeBytes());
		length.setDisabled(true);
		TextItem type = new TextItem("picture.type", DictionaryInstance.dictionary.type());
		type.setDisabled(true);
		TextItem url = new TextItem("picture.url", "URL");
		url.setVisible(false);
				
		final SelectItem selectNick = new SelectItem("picture.nick", DictionaryInstance.dictionary.nick());
		selectNick.setEmptyDisplayValue(DictionaryInstance.dictionary.notSet());
		selectNick.setAllowEmptyValue(true);
		selectNick.setOptionDataSource(NicksDS.getInstance());
		selectNick.setDisplayField("name");
		selectNick.setValueField("id");
		selectNick.setAnimatePickList(true);
		selectNick.setPickListWidth(250);
		
		return new FormItem[] { id, name, length, type, selectNick, url };
	}

	private ListGridField[] getGridFields() {
		ListGridField name = new ListGridField("picture.name");
		ListGridField length = new ListGridField("picture.length");
		ListGridField type = new ListGridField("picture.type");
		ListGridField nickName = new ListGridField("nick.name");
		ListGridField nick = new ListGridField("picture.nick");
		nick.setHidden(true);
		ListGridField url = new ListGridField("picture.url");
		url.setHidden(true);
		
		return new ListGridField[] { name, length, type, nickName, url, nick };
	}
}
