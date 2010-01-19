package hr.chus.cchat.gwt.admin;

import hr.chus.cchat.gwt.i18n.Dictionary;

import com.google.gwt.core.client.GWT;
import com.gwtext.client.core.Connection;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.data.*;
import com.gwtext.client.data.event.StoreListenerAdapter;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.PaddedPanel;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.*;
import com.gwtext.client.widgets.form.event.FormListenerAdapter;
import com.gwtext.client.widgets.grid.*;
import com.gwtext.client.widgets.grid.event.RowSelectionListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;

/**
 * 
 * @author chus
 *
 */
public class AdminOperatorGWT {
	
	private Store operatorRoleStore = null;
	private Panel panel = null;
	private Dictionary dictionary = (Dictionary) GWT.create(Dictionary.class);

	/**
	 * 
	 */
	public void init() {
		if (panel != null) {
			panel.destroy();
		}
		panel = new Panel();
		operatorRoleStore = getOperatorRole();
		panel.setBorder(false);
		panel.setPaddings(15);
		panel.add(getOperatorList());
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
	 * @return
	 */
	private Store getOperatorRole() {
		HttpProxy dataProxy = new HttpProxy("AdminRoleListJSON");
		final RecordDef recordDef = new RecordDef(new FieldDef[] {
				new StringFieldDef("description", "description")
				, new StringFieldDef("id", "id")
				, new StringFieldDef("name", "name")
		});
		JsonReader reader = new JsonReader(recordDef);
		reader.setRoot("roleList");
		reader.setId("id");

		final Store store = new Store(dataProxy, reader, true);
		store.addStoreListener(new StoreListenerAdapter() {
			@Override
			public void onLoadException(Throwable error) {
				com.google.gwt.user.client.Window.open(GWT.getModuleBaseURL() + "Login", "_self", "");
			}
		});
		store.load();
		return store;
	}

	/**
	 * 
	 * @return
	 */
	private FormPanel getOperatorList() {
		final FormPanel formPanel = new FormPanel();
		formPanel.setFrame(true);
		formPanel.setLabelAlign(Position.LEFT);
		formPanel.setPaddings(5);
		formPanel.setWidth(660);
   
		Panel inner = new Panel();
		Panel columnOne = new Panel();
		columnOne.setLayout(new FitLayout());
        
		HttpProxy dataProxy = new HttpProxy("AdminOperatorListJSON");
		RecordDef recordDef = getOperatorRecordDef();
		JsonReader reader = new JsonReader(recordDef);
		reader.setRoot("operatorList");
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
				new ColumnConfig(dictionary.username(), "operator.username", 80, false)
				, new ColumnConfig(dictionary.name(), "operator.name", 75, false)
				, new ColumnConfig(dictionary.surname(), "operator.surname", 100, false)
				, new ColumnConfig(dictionary.email(), "operator.email", 120, false) 
				, new ColumnConfig(dictionary.role(), "role.name", 70, false)
				, new ColumnConfig(dictionary.isDisabled(), "operator.disabled", 72, false, new Renderer() {
					
					@Override
					public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
						if ((Boolean) value) {
							return dictionary.yes();
						} else {
							return dictionary.no();
						}
					}
				})
				, new ColumnConfig(dictionary.isActive(), "operator.isActive", 70, false, new Renderer() {
					
					@Override
					public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
						if ((Boolean) value) {
							return dictionary.yes();
						} else {
							return dictionary.no();
						}
					}
				})
		});

		final GridPanel gridPanel = new GridPanel();
		gridPanel.setStore(store);
		gridPanel.setColumnModel(columnModel);
		gridPanel.setTitle(dictionary.operatorsList());
		gridPanel.setIconCls("operators-icon");
		gridPanel.setWidth(600);
		gridPanel.setHeight(250);
		gridPanel.stripeRows(true);
		gridPanel.setBorder(true);
		gridPanel.setFrame(true);
		gridPanel.setLoadMask(true);
		
		FieldSet operatorFieldSet = getOperatorFieldSet();
		final PaddedPanel paddedPanel = new PaddedPanel(operatorFieldSet, 5);
		paddedPanel.hide();
		
		RowSelectionModel sm = new RowSelectionModel(true);
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
				formPanel.getForm().submit("AdminOperatorFunctionJSON", null, Connection.POST, dictionary.savingData(), false);
			}
		});
		updateButton.setIconCls("edit-icon");
		paddedPanel.addButton(updateButton);
		
		Button deleteButton = new Button(dictionary.delete(), new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				formPanel.getForm().findField("operation").setValue("delete");
				formPanel.getForm().submit("AdminOperatorFunctionJSON", null, Connection.POST, dictionary.savingData(), false);
			}
		});
		deleteButton.setIconCls("delete-icon");
		paddedPanel.addButton(deleteButton);
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
		addFormPanel.setWidth(300);
		final Window window = new Window();
		window.setTitle(dictionary.addNewOperator());
		window.setIconCls("");
		window.setWidth(300);
		window.setResizable(true);
		window.setModal(false);
		window.setCloseAction(Window.HIDE);
		final FieldSet addOperatorFieldSet = getOperatorFieldSet();
		addOperatorFieldSet.setTitle("Operator");
		addFormPanel.add(new PaddedPanel(addOperatorFieldSet, 10, 10, 10, 10));
		addFormPanel.addButton(new Button("Save", new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				addFormPanel.getForm().findField("operation").setValue("save/edit");
				addFormPanel.getForm().submit("AdminOperatorFunctionJSON", null, Connection.POST, dictionary.savingData(), false);
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
		
		Button addOperator = new Button(dictionary.addNewOperator(), new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				window.show();
			}
		});
		addOperator.setIconCls("new-icon");
		formPanel.addButton(addOperator);
		
		window.setAnimateTarget(addOperator.getId());
		
		return formPanel;
	}

	/**
	 * 
	 * @return
	 */
	private RecordDef getOperatorRecordDef() {
		return new RecordDef(new FieldDef[] {
				new StringFieldDef("operator.username", "username")
				, new StringFieldDef("operator.name", "name")
				, new StringFieldDef("operator.surname", "surname")
				, new StringFieldDef("operator.email", "email")
				, new StringFieldDef("operator.role", "role.id")
				, new StringFieldDef("role.name", "role.name")
				, new StringFieldDef("operator.password", "password")
				, new BooleanFieldDef("operator.disabled", "disabled")
				, new BooleanFieldDef("operator.isActive", "isActive")
				, new IntegerFieldDef("operator.id", "id")
		});
	}

	/**
	 * 
	 * @return
	 */
	private FieldSet getOperatorFieldSet() {
		FieldSet fieldSet = new FieldSet();
		fieldSet.setLabelWidth(90);
		fieldSet.setTitle(dictionary.editOperator());
		fieldSet.setAutoHeight(true);
		fieldSet.setBorder(false);

		// the field names must match the data field values from the Store
		fieldSet.add(new Hidden("operation", ""));
		fieldSet.add(new Hidden("operator.id", ""));
		fieldSet.add(new TextField(dictionary.username(), "operator.username", 120));
		fieldSet.add(new TextField(dictionary.name(), "operator.name", 120));
		fieldSet.add(new TextField(dictionary.surname(), "operator.surname", 120));
		fieldSet.add(new TextField(dictionary.email(), "operator.email", 120));
		TextField password = new TextField(dictionary.password(), "operator.password", 120);
		password.setPassword(true);
		fieldSet.add(password);
		
		ComboBox comboBox = new ComboBox();
		comboBox.setFieldLabel(dictionary.role());
		comboBox.setHiddenName("operator.role");
		comboBox.setStore(operatorRoleStore);
		comboBox.setDisplayField("name");
		comboBox.setValueField("id");
		comboBox.setTypeAhead(true);
		comboBox.setMode(ComboBox.LOCAL);
		comboBox.setTriggerAction(ComboBox.ALL);
		comboBox.setSelectOnFocus(true);
		comboBox.setWidth(120);
		fieldSet.add(comboBox);
		
		fieldSet.add(new Checkbox(dictionary.isDisabled(), "operator.disabled"));
		fieldSet.add(new Checkbox(dictionary.isActive(), "operator.isActive"));
		
		return fieldSet;
	}
	
}