package hr.chus.cchat.gwt.admin;

import hr.chus.cchat.gwt.i18n.Dictionary;
import hr.chus.cchat.gwt.image.ImageChooser;
import hr.chus.cchat.gwt.image.ImageChooserCallback;
import hr.chus.cchat.gwt.image.ImageData;

import com.google.gwt.core.client.GWT;
import com.gwtext.client.core.Connection;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.core.UrlParam;
import com.gwtext.client.data.*;
import com.gwtext.client.data.event.StoreListenerAdapter;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.PaddedPanel;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.*;
import com.gwtext.client.widgets.form.event.FormListenerAdapter;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.RowSelectionModel;
import com.gwtext.client.widgets.grid.event.RowSelectionListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;

/**
 * 
 * @author chus
 *
 */
public class AdminNickGWT {
	
	private Panel panel = null;
	private Store pictureStore = null;
	private ImageChooser ic = null;
	private Dictionary dictionary = (Dictionary) GWT.create(Dictionary.class);

	/**
	 * 
	 */
	public void init() {
		if (panel != null) {
			panel.destroy();
		}
		createPictureStore();
		panel = new Panel();
		panel.setBorder(false);
		panel.setPaddings(15);
		panel.add(getNickList());
		panel.setClosable(true);
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
	 */
	private void createPictureStore() {
		HttpProxy dataProxy = new HttpProxy("AdminPictureListJSON");
		RecordDef recordDef = getPictureRecordDef();
		JsonReader reader = new JsonReader(recordDef);
		reader.setRoot("pictureList");
		reader.setId("id");
		
		pictureStore = new Store(dataProxy, reader, true);
		pictureStore.addStoreListener(new StoreListenerAdapter() {
			@Override
			public void onLoadException(Throwable error) {
				com.google.gwt.user.client.Window.open(GWT.getModuleBaseURL() + "Login", "_self", "");
			}
		});
	}

	/**
	 * 
	 * @return
	 */
	private Component getNickList() {
		final FormPanel formPanel = new FormPanel();
		formPanel.setFrame(true);
		formPanel.setLabelAlign(Position.LEFT);
		formPanel.setPaddings(5);
		formPanel.setWidth(400);
   
		Panel inner = new Panel();
		Panel columnOne = new Panel();
		columnOne.setLayout(new FitLayout());
        
		HttpProxy dataProxy = new HttpProxy("AdminNickListJSON");
		RecordDef recordDef = getNickRecordDef();
		JsonReader reader = new JsonReader(recordDef);
		reader.setRoot("nickList");
		reader.setId("id");
		
		final Store store = new Store(dataProxy, reader, true);
		store.addStoreListener(new StoreListenerAdapter() {
			@Override
			public void onLoadException(Throwable error) {
				com.google.gwt.user.client.Window.open(GWT.getModuleBaseURL() + "Login", "_self", "");
			}
		});
		store.load();

		ColumnModel columnModel = new ColumnModel(new ColumnConfig[] {
				new ColumnConfig(dictionary.name(), "nick.name", 100, false)
				, new ColumnConfig(dictionary.description(), "nick.description", 200, false)
		});
		
		final GridPanel gridPanel = new GridPanel();
		gridPanel.setStore(store);
		gridPanel.setColumnModel(columnModel);
		gridPanel.setTitle(dictionary.nickList());
		gridPanel.setIconCls("nicks-icon");
		gridPanel.setWidth(300);
		gridPanel.setHeight(300);
		gridPanel.stripeRows(true);
		gridPanel.setBorder(true);
		gridPanel.setFrame(true);
		gridPanel.setLoadMask(true);
		
		FieldSet nickFieldSet = getNickFieldSet();
		final PaddedPanel paddedPanel = new PaddedPanel(nickFieldSet, 5);
		paddedPanel.hide();
		
		final RowSelectionModel sm = new RowSelectionModel(true);
		sm.addListener(new RowSelectionListenerAdapter() {
			// TODO: FIX - Have to do this to deselect all. When I use JSONCustomReader RowSelectionModel(true) doesn't seem to work correctly!
			@Override
			public boolean doBeforeRowSelect(RowSelectionModel sm, int rowIndex, boolean keepExisting, Record record) {
				sm.deselectRange(0, store.getCount() - 1);
				return true;
			}
			
			public void onRowSelect(RowSelectionModel sm, int rowIndex, Record record) {
				formPanel.getForm().loadRecord(record);
				paddedPanel.show();
			}
		});
		gridPanel.setSelectionModel(sm);

		columnOne.add(gridPanel);
		columnOne.setPaddings(5);
	
		inner.add(columnOne);
		
		Button updateButton = new Button(dictionary.update(), new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				formPanel.getForm().findField("operation").setValue("save/edit");
				formPanel.getForm().submit("AdminNickFunctionJSON", null, Connection.POST, dictionary.savingData(), false);
			}
		});
		updateButton.setIconCls("edit-icon");
		paddedPanel.addButton(updateButton);
		
		Button deleteButton = new Button(dictionary.delete(), new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				MessageBox.confirm(dictionary.confirm(), dictionary.nickDeleteWarning(),
						new MessageBox.ConfirmCallback() {
							public void execute(String btnID) {
								if (btnID.equals("yes")) {
									formPanel.getForm().findField("operation").setValue("delete");
									formPanel.getForm().submit("AdminNickFunctionJSON", null, Connection.POST, dictionary.savingData(), false);
								}
							}
				});
			}
		});
		deleteButton.setIconCls("delete-icon");
		paddedPanel.addButton(deleteButton);
		
		Button showImages = new Button(dictionary.showPictures(), new ButtonListenerAdapter() {
			public void onClick(final Button button, EventObject e) {
				int nickId = sm.getSelected().getAsInteger("nick.id");
				pictureStore.reload(new UrlParam[] { new UrlParam("nick", String.valueOf(nickId)) });
				if (ic == null) {
					ic = new ImageChooser(dictionary.pictures(), 515, 400, pictureStore);
				}

				ic.show(new ImageChooserCallback() {
					public void onImageSelection(ImageData data) {
						button.focus();
					}
				});
			}
		});
		showImages.setIconCls("pictures-icon");
		paddedPanel.addButton(showImages);
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
				if (operation.equals("save/edit")) {
					form.updateRecord(gridPanel.getSelectionModel().getSelected());
				} else if (operation.equals("delete")) {
					store.remove(gridPanel.getSelectionModel().getSelected());
					paddedPanel.hide();
				}
			}
		});
		
		final FormPanel addFormPanel = new FormPanel();
		addFormPanel.setFrame(true);
		addFormPanel.setLabelAlign(Position.LEFT);
		addFormPanel.setPaddings(5);
		addFormPanel.setWidth(400);
		final Window window = new Window();
		window.setTitle(dictionary.addNewNick());
		window.setIconCls("");
		window.setWidth(400);
		window.setResizable(true);
		window.setModal(false);
		window.setCloseAction(Window.HIDE);
		final FieldSet addNickFieldSet = getNickFieldSet();
		addFormPanel.add(new PaddedPanel(addNickFieldSet, 10, 10, 10, 10));
		addFormPanel.addButton(new Button(dictionary.save(), new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				addFormPanel.getForm().findField("operation").setValue("save/edit");
				addFormPanel.getForm().submit("AdminNickFunctionJSON", null, Connection.POST, dictionary.savingData(), false);
			}
		}));
		addFormPanel.getForm().addListener(new FormListenerAdapter() {
			@Override
			public void onActionFailed(Form form, int httpStatus, String responseText) {
				if (httpStatus == 450) {
					com.google.gwt.user.client.Window.open(GWT.getModuleBaseURL() + "Login", "_self", "");
				}
			}
			
			@Override
			public void onActionComplete(Form form, int httpStatus, String responseText) {
				String operation = form.findField("operation").getValueAsString();
				if (operation.equals("save/edit")) {
					store.reload();
					window.hide();
				}
			}
		});
		window.add(addFormPanel);
		
		Button addNick = new Button(dictionary.addNew(), new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				window.show();
			}
		});
		addNick.setIconCls("new-icon");
		formPanel.addButton(addNick);
		
		window.setAnimateTarget(addNick.getId());
		
		return formPanel;
	}

	/**
	 * 
	 * @return
	 */
	private RecordDef getNickRecordDef() {
		return new RecordDef(new FieldDef[] {
				new IntegerFieldDef("nick.id", "id")
				, new StringFieldDef("nick.name", "name")
				, new StringFieldDef("nick.description", "description")
		});
	}
	
	/**
	 * 
	 * @return
	 */
	private RecordDef getPictureRecordDef() {
		return new RecordDef(new FieldDef[] {
				new StringFieldDef("id", "id")
				, new StringFieldDef("url", "url")
				, new StringFieldDef("name", "name")
				, new StringFieldDef("type", "type")
				, new IntegerFieldDef("size", "length")
		});
	}
	
	/**
	 * 
	 * @return
	 */
	private FieldSet getNickFieldSet() {
		FieldSet fieldSet = new FieldSet();
		fieldSet.setLabelWidth(90);
		fieldSet.setTitle(dictionary.editNick());
		fieldSet.setAutoHeight(true);
		fieldSet.setBorder(false);

		// the field names must match the data field values from the Store
		fieldSet.add(new Hidden("operation", ""));
		fieldSet.add(new Hidden("nick.id", ""));
		fieldSet.add(new TextField(dictionary.name(), "nick.name", 200));
		TextArea description = new TextArea(dictionary.description(), "nick.description");
		description.setWidth(200);
		description.setGrow(false);
		description.setGrowMin(30);
		description.setGrowMax(30);
		fieldSet.add(description);
		
		return fieldSet;
	}

}
