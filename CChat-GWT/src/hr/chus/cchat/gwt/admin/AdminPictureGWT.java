package hr.chus.cchat.gwt.admin;

import hr.chus.cchat.gwt.i18n.Dictionary;
import hr.chus.cchat.gwt.reader.JSONCustomReader;

import com.google.gwt.core.client.GWT;
import com.gwtext.client.core.*;
import com.gwtext.client.data.*;
import com.gwtext.client.data.event.StoreListenerAdapter;
import com.gwtext.client.widgets.*;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.*;
import com.gwtext.client.widgets.form.event.FormListenerAdapter;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.grid.*;
import com.gwtext.client.widgets.grid.event.RowSelectionListenerAdapter;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;

/**
 * 
 * @author chus
 *
 */
public class AdminPictureGWT {
	
	private Panel panel = null;
	private Dictionary dictionary = (Dictionary) GWT.create(Dictionary.class);
	private Store nickStore = null; 

	/**
	 * 
	 */
	public void init() {
		if (panel != null) {
			panel.destroy();
		}
		panel = new Panel();
		panel.setBorder(false);
		panel.setPaddings(15);
		panel.setLayout(new FitLayout()); 
		panel.add(getPictureList());
		panel.setClosable(true);
		panel.setHeight(600);
		panel.setWidth(600);
	}
	
	/**
	 * 
	 * @return
	 */
	public Panel getPanel() {
		return panel;
	}
	
	/**
	 * 
	 * @return
	 */
	private Component getPictureList() {
		initNickStore();
		
		Panel borderPanel = new Panel();
		borderPanel.setLayout(new BorderLayout());
		
		final FormPanel formPanel = new FormPanel();
		formPanel.setFrame(true);
		formPanel.setLabelAlign(Position.LEFT);
		formPanel.setPaddings(5);
		formPanel.setWidth(400);
   
		final Panel eastPanel = new Panel();
		eastPanel.setCollapsible(false);

		final PaddedPanel paddedPanelEast = new PaddedPanel(eastPanel, 5);
		paddedPanelEast.setWidth(150);
		
		Panel inner = new Panel();
		Panel columnOne = new Panel();
		columnOne.setLayout(new FitLayout());
        
		HttpProxy dataProxy = new HttpProxy("AdminPictureListJSON");
		RecordDef recordDef = getPictureRecordDef();
		JSONCustomReader reader = new JSONCustomReader(recordDef);
		reader.setRoot("pictureList");
		reader.setId("id");
		
		final Store store = new Store(dataProxy, reader, true);
		store.addStoreListener(new StoreListenerAdapter() {
			
			@Override
			public void onLoadException(Throwable error) {
				com.google.gwt.user.client.Window.open(GWT.getModuleBaseURL() + "Login", "_self", "");
			}
		});
		store.load();
		ColumnConfig nickId = new ColumnConfig(dictionary.assignedNick(), "picture.nick", 50, false);
		nickId.setHidden(true);
		ColumnModel columnModel = new ColumnModel(new ColumnConfig[] {
				new ColumnConfig(dictionary.name(), "picture.name", 100, false)
				, new ColumnConfig(dictionary.type(), "picture.type", 70, false)
				, new ColumnConfig(dictionary.sizeBytes(), "picture.length", 100, false)
				, new ColumnConfig(dictionary.assignedNick(), "picture.nick.name", 100, false)
				, nickId
		});
		
		final GridPanel gridPanel = new GridPanel();
		gridPanel.setStore(store);
		gridPanel.setColumnModel(columnModel);
		gridPanel.setTitle(dictionary.pictureList());
		gridPanel.setIconCls("pictures-icon");
		gridPanel.setWidth(320);
		gridPanel.setHeight(300);
		gridPanel.stripeRows(true);
		gridPanel.setBorder(true);
		gridPanel.setFrame(true);
		gridPanel.setLoadMask(true);
		
		FieldSet pictureFieldSet = getPictureFieldSet();
		final PaddedPanel paddedPanel = new PaddedPanel(pictureFieldSet, 5);
		paddedPanel.hide();
		
		String detailTemplate[] = new String[] { "<tpl for='.'>",
				"<div class='details'><img src='{url}'>",
				"<div class='details-info'><b>" + dictionary.imageName() + ":</b>",
				"<span>{name}</span><b>" + dictionary.sizeBytes() + ":</b>",
				"<span>{sizeString}</span><b>" + dictionary.type() + ":</b>",
				"<span>{type}</span></div></div>", "</tpl>",
				"<div class='x-clear'></div>" };
		final XTemplate detailsTemplate = new XTemplate(detailTemplate);
		detailsTemplate.compile();
		
		RowSelectionModel sm = new RowSelectionModel(true);
		sm.addListener(new RowSelectionListenerAdapter() {
			// TODO: FIX - Have to do this to deselect all. When I use JSONCustomReader RowSelectionModel(true) doesn't seem to work correctly!
			@Override
			public boolean doBeforeRowSelect(RowSelectionModel sm, int rowIndex, boolean keepExisting, Record record) {
				sm.deselectRange(0, store.getCount() - 1);
				return true;
			}
			
			@Override
			public void onRowSelect(RowSelectionModel sm, int rowIndex, Record record) {
				ExtElement detailEl = eastPanel.getEl();
				detailEl.hide();
				formPanel.getForm().loadRecord(record);
				paddedPanel.show();
				NameValuePair vals[] = new NameValuePair[4];
				vals[0] = new NameValuePair("name", record.getAsString("picture.name"));
				vals[1] = new NameValuePair("url", GWT.getModuleBaseURL() + "../pictures/" + record.getAsString("picture.name"));
				vals[2] = new NameValuePair("sizeString", record.getAsString("picture.length"));
				vals[3] = new NameValuePair("type", record.getAsString("picture.type"));
				String html = detailsTemplate.applyTemplate(vals);
				detailEl.update(html);
				detailEl.slideIn();
			}
		});
		gridPanel.setSelectionModel(sm);

		columnOne.add(gridPanel);
		columnOne.setPaddings(5);
	
		inner.add(columnOne);
		
		Button deleteButton = new Button(dictionary.delete(), new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				formPanel.getForm().findField("operation").setValue("delete");
				formPanel.getForm().submit("AdminPictureFunctionJSON", null, Connection.POST, dictionary.savingData(), false);
			}
		});
		deleteButton.setIconCls("delete-icon");
		paddedPanel.addButton(deleteButton);
		
		Button updateButton = new Button(dictionary.update(), new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				formPanel.getForm().findField("operation").setValue("update");
				formPanel.getForm().submit("AdminPictureFunctionJSON", null, Connection.POST, dictionary.savingData(), false);
			}
		});
		updateButton.setIconCls("edit-icon");
		paddedPanel.addButton(updateButton);
		paddedPanel.setAutoHeight(true);
		
		inner.add(paddedPanel);
		formPanel.add(inner);
		
		formPanel.getForm().addListener(new FormListenerAdapter() {
			@Override
			public void onActionFailed(Form form, int httpStatus, String responseText) {
				if (httpStatus == 450) {
					com.google.gwt.user.client.Window.open(GWT.getModuleBaseURL() + "Login", "_self", "");
				}
			}
			
			@Override
			public void onActionComplete(Form form, int httpStatus, String responseText) {
				String operation = form.findField("operation").getValueAsString();
				if (operation.equals("update")) {
					String nickId = form.findField("picture.nick").getValueAsString();
					if (nickId == null || nickId.isEmpty()) {
						gridPanel.getSelectionModel().getSelected().set("picture.nick.name", "");
					} else {
						Record rec = nickStore.getAt(nickStore.find("id", nickId, 0, false, true));
						gridPanel.getSelectionModel().getSelected().set("picture.nick.name", rec.getAsString("name"));						
					}
					form.updateRecord(gridPanel.getSelectionModel().getSelected());
				} else if (operation.equals("delete")) {
					store.remove(gridPanel.getSelectionModel().getSelected());
					paddedPanel.hide();
					ExtElement detailEl = eastPanel.getEl();
					detailEl.update("");
				}
			}
		});
		
		final Window window = new UploadPicture(store);
		Button addPicture = new Button(dictionary.addNew(), new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				window.show();
			}
		});
		addPicture.setIconCls("new-icon");
		formPanel.addButton(addPicture);
		
		window.setAnimateTarget(addPicture.getId());
		
		borderPanel.add(formPanel, new BorderLayoutData(RegionPosition.CENTER));
		borderPanel.add(paddedPanelEast, new BorderLayoutData(RegionPosition.EAST, new Margins(0, 5, 0, 0)));
		
		return borderPanel;
	}

	/**
	 * 
	 * @return
	 */
	private RecordDef getPictureRecordDef() {
		return new RecordDef(new FieldDef[] {
				new StringFieldDef("picture.name", "name")
				, new StringFieldDef("picture.id", "id")
				, new StringFieldDef("picture.length", "length")
				, new StringFieldDef("picture.type", "type")
				, new StringFieldDef("picture.nick.name", "nick.name")
				, new StringFieldDef("picture.nick.description", "nick.description")
				, new StringFieldDef("picture.nick", "nick.id")
		});
	}
	
	private void initNickStore() {
		HttpProxy dataProxy = new HttpProxy("AdminNickListJSON");
		final RecordDef recordDef = new RecordDef(new FieldDef[] {
				new StringFieldDef("description", "description")
				, new StringFieldDef("id", "id")
				, new StringFieldDef("name", "name")
		});
		JsonReader reader = new JsonReader(recordDef);
		reader.setRoot("nickList");
		reader.setId("id");

		nickStore = new Store(dataProxy, reader, true);
		nickStore.addStoreListener(new StoreListenerAdapter() {
			@Override
			public void onLoadException(Throwable error) {
				com.google.gwt.user.client.Window.open(GWT.getModuleBaseURL() + "Login", "_self", "");
			}
		});
		nickStore.load();
	}
	
	/**
	 * 
	 * @return
	 */
	private FieldSet getPictureFieldSet() {
		FieldSet fieldSet = new FieldSet();
		fieldSet.setLabelWidth(90);
		fieldSet.setTitle(dictionary.editPicture());
		fieldSet.setAutoHeight(true);
		fieldSet.setBorder(false);
		
		// the field names must match the data field values from the Store
		fieldSet.add(new Hidden("operation", ""));
		fieldSet.add(new Hidden("picture.id", ""));
		TextField name = new TextField(dictionary.name(), "picture.name", 100);
		name.setReadOnly(true);
		fieldSet.add(name);
		TextField type = new TextField(dictionary.type(), "picture.type", 100);
		type.setReadOnly(true);
		fieldSet.add(type);
		TextField length = new TextField(dictionary.sizeBytes(), "picture.length", 100);
		length.setReadOnly(true);
		fieldSet.add(length);
		fieldSet.add(getNickList());
		
		return fieldSet;
	}
	
	private ComboBox getNickList() {
		final ComboBox comboBox = new ComboBox();
		comboBox.setFieldLabel(dictionary.nick());
		comboBox.setHiddenName("picture.nick");
		comboBox.setStore(nickStore);
		comboBox.setDisplayField("name");
		comboBox.setEmptyText(dictionary.noNick());
		comboBox.setValueField("id");
		comboBox.setAllowBlank(true);
		comboBox.setTypeAhead(true);
		comboBox.setMode(ComboBox.LOCAL);
		comboBox.setTriggerAction(ComboBox.ALL);
		comboBox.setWidth(150);
		comboBox.addListener(new TextFieldListenerAdapter() {		
			@Override
			public void onBlur(Field field) {
				super.onBlur(field);
				if (field.getRawValue().isEmpty()) {
					comboBox.setValue(null);
				} 
			}
		});
		comboBox.setSelectOnFocus(true);
		return comboBox;
	}

}
