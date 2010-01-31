package hr.chus.cchat.gwt.admin;

import hr.chus.cchat.gwt.reader.JSONCustomReader;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.core.UrlParam;
import com.gwtext.client.data.*;
import com.gwtext.client.data.event.StoreListenerAdapter;
import com.gwtext.client.util.DateUtil;
import com.gwtext.client.widgets.*;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.grid.*;
import com.gwtext.client.widgets.grid.event.RowSelectionListenerAdapter;
import com.gwtext.client.widgets.layout.ColumnLayout;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.RowLayoutData;

/**
 * 
 * @author Jan Čustović
 *
 */
public class AdminUserGWT {

	private Panel panel = null;
	private GridPanel grid;
	
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
	public void init() {
		panel = new Panel();
		panel.setBorder(false);
		panel.setPaddings(15);
		
		Panel mainPanel = new Panel();
		mainPanel.setLayout(new ColumnLayout());
		mainPanel.setWidth(1100);
		mainPanel.setPaddings(5);
		mainPanel.setBorder(false);
		mainPanel.setFrame(true);
		
		final FormPanel formPanel = new FormPanel();
		formPanel.setFrame(true);
		formPanel.setLabelAlign(Position.LEFT);
		formPanel.setWidth(650);
		
		Panel bindPanel = new Panel();  
		bindPanel.setLayout(new RowLayout());

		HttpProxy dataProxy = new HttpProxy("AdminUserListJSON");

		final RecordDef recordDef = new RecordDef(
				new FieldDef[] { new StringFieldDef("id")
						, new StringFieldDef("nick", "nick.name")
						, new StringFieldDef("operator", "operator.username")
						, new StringFieldDef("msisdn", "msisdn")
						, new StringFieldDef("serviceProvider", "serviceProvider.providerName")
						, new DateFieldDef("joined", "joined", "Y-m-dTH:i:s")
						, new StringFieldDef("name")
						, new StringFieldDef("surname")
						, new StringFieldDef("notes")
				});
		JSONCustomReader reader = new JSONCustomReader(recordDef);
		reader.setRoot("userList");
		reader.setTotalProperty("totalCount");
		reader.setId("id");

		final Store store = new Store(dataProxy, reader, true);

		ColumnConfig idColumn = new ColumnConfig("ID", "id", 50, false);
		ColumnConfig msisdnColumn = new ColumnConfig("MSISDN", "msisdn", 150, false);
		ColumnConfig serviceProviderColumn = new ColumnConfig("Provider", "serviceProvider", 100);
		ColumnConfig nameColumn = new ColumnConfig("Name", "name", 70);
		ColumnConfig surnameColumn = new ColumnConfig("Surame", "surname", 70);
		ColumnConfig nickColumn = new ColumnConfig("Nick", "nick", 70, false, new Renderer() {
			
			@Override
			public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
				if (value == null) {
					return "None";
				}
				return (String) value;
			}
		});
		ColumnConfig operatorColumn = new ColumnConfig("Operator", "operator", 70);
		ColumnConfig joinedColumn = new ColumnConfig("Joined", "joined", 150, false, new Renderer() {
			
			@Override
			public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
				Date joinedDate = record.getAsDate("joined");
				String joinedDateString = DateUtil.format(joinedDate, "d.m.Y H:i:s");
				return joinedDateString;
			}
		});

		ColumnModel columnModel = new ColumnModel(new ColumnConfig[] { idColumn, msisdnColumn, serviceProviderColumn, nameColumn, surnameColumn, nickColumn, operatorColumn, joinedColumn });
		columnModel.setDefaultSortable(true);

		Panel gridPanel = new Panel();
		gridPanel.setLayout(new FitLayout());
		grid = new GridPanel();
		grid.setWidth(600);
		grid.setHeight(500);
		grid.setTitle("Users");
		grid.setStore(store);
		grid.setColumnModel(columnModel);
		grid.setTrackMouseOver(false);
		grid.setLoadMask(true);
		grid.setFrame(true);
		grid.setStripeRows(true);
		grid.setIconCls("users-icon");

		PagingToolbar pagingToolbar = new PagingToolbar(store);
		pagingToolbar.setPageSize(25);
		pagingToolbar.setDisplayInfo(true);
		pagingToolbar.setDisplayMsg("Displaying users {0} - {1} of {2}");
		pagingToolbar.setEmptyMsg("No users to display");
		
		pagingToolbar.addSeparator();
		grid.setBottomToolbar(pagingToolbar);

		grid.addListener(new PanelListenerAdapter() {
			public void onRender(Component component) {
//				store.load(0, 25);
			}
		});
		gridPanel.add(grid);
		
		Panel detailsPanel = new Panel();
		detailsPanel.setBorder(true);
		detailsPanel.setTitle("User details");
		detailsPanel.setLayout(new ColumnLayout());
		
		Panel leftContainer = new Panel();
		leftContainer.setBorder(false);
		leftContainer.setPaddings(5);
		Panel rightContainer = new Panel();
		rightContainer.setBorder(false);
		rightContainer.setPaddings(5);
		
		FieldSet leftFieldSet = new FieldSet();
		leftFieldSet.setBorder(false);
		leftFieldSet.setPaddings(2);
		leftFieldSet.setTitle("#1");
		
		TextField msisdnField = new TextField("MSISDN", "msisdn", 120);
		msisdnField.setDisabled(true);
		leftFieldSet.add(msisdnField);
		
		TextField providerField = new TextField("Provider", "serviceProvider", 120);
		providerField.setDisabled(true);
		leftFieldSet.add(providerField);
		
		leftFieldSet.add(new TextField("Name", "name", 120));
		leftFieldSet.add(new TextField("Surame", "surname", 120));
		leftFieldSet.add(new TextField("Address", "address", 180));
		
		FieldSet rightFieldSet = new FieldSet();
		rightFieldSet.setBorder(false);
		rightFieldSet.setPaddings(2);
		rightFieldSet.setTitle("#2");
		
		DateField joinedField = new DateField("Joined", "joined", 120);
		joinedField.setFormat("d.m.Y H:i:s");
		joinedField.setDisabled(true);
		rightFieldSet.add(joinedField);
		
		DateField birthDateField = new DateField("Birth date", "birthDate", 120);
		joinedField.setFormat("d.m.Y");
		rightFieldSet.add(birthDateField);
		rightFieldSet.add(getNickList());
		rightFieldSet.add(getOperatorList());
		TextArea notesField = new TextArea("Notes", "notes");
		notesField.setSize(160, 80);
		rightFieldSet.add(notesField);
		
		leftContainer.add(leftFieldSet);
		rightContainer.add(rightFieldSet);
		
		detailsPanel.add(leftContainer, new ColumnLayoutData(0.48));
		detailsPanel.add(rightContainer, new ColumnLayoutData(0.48));
		
		Button updateButton = new Button(AdminGWT.dictionary.update());
		updateButton.setIconCls("edit-icon");
		
		Button deleteButton = new Button(AdminGWT.dictionary.delete());
		deleteButton.setIconCls("delete-icon");
		
		detailsPanel.addButton(updateButton);
		detailsPanel.addButton(deleteButton);
		
		final PaddedPanel fieldSetPanel = new PaddedPanel(detailsPanel, 10, 0, 0, 0);
		fieldSetPanel.hide();
		
		final RowSelectionModel sm = new RowSelectionModel(true);
		sm.addListener(new RowSelectionListenerAdapter() {
			@Override
			public void onRowSelect(RowSelectionModel sm, int rowIndex, Record record) {
				formPanel.getForm().loadRecord(record);
				fieldSetPanel.show();
			}
			
			// TODO: FIX - Have to do this to deselect all. When I use JSONCustomReader RowSelectionModel(true) doesn't seem to work correctly!
			@Override
			public boolean doBeforeRowSelect(RowSelectionModel sm, int rowIndex, boolean keepExisting, Record record) {
				sm.deselectRange(0, store.getCount() - 1);
				return true;
			}
		});
		grid.setSelectionModel(sm);
		
		bindPanel.add(gridPanel, new RowLayoutData(500));  
		bindPanel.add(fieldSetPanel, new RowLayoutData(320));
		
		formPanel.add(bindPanel);
		
		final FormPanel searchFormPanel = new FormPanel();
		searchFormPanel.setFrame(true);
		searchFormPanel.setLabelAlign(Position.LEFT);
		searchFormPanel.setWidth(250);
		searchFormPanel.setPaddings(3);
		
		FieldSet searchFieldSet = new FieldSet();
		searchFieldSet.setLabelWidth(90);
		searchFieldSet.setTitle("Search");
		searchFieldSet.setAutoHeight(true);
		searchFieldSet.setBorder(false);
		// the field names msut match the data field values from the Store
		searchFieldSet.add(new TextField("MSISDN", "msisdn", 120));
		searchFieldSet.add(new TextField("Name", "name", 120));
		searchFieldSet.add(new TextField("Surname", "surname", 120));
		searchFieldSet.add(getNickList());
		searchFieldSet.add(getOperatorList());
		searchFieldSet.add(getServiceProviderList());
		
		
		PaddedPanel searchPanel = new PaddedPanel(searchFieldSet, 0);
		searchPanel.setFrame(true);
		Button search = new Button("Search");
		search.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				String[] props = searchFormPanel.getForm().getValues().split("&");
				UrlParam[] params = new UrlParam[props.length];
				for (int i = 0; i < props.length; i++) {
					String[] nameValue = props[i].split("=");
					params[i] = new UrlParam(nameValue[0], nameValue[1]);
				}
				store.setBaseParams(params);
				store.removeAll();
				store.load(0, 25);
			}
		});
		searchPanel.addButton(search);
		
		searchFormPanel.add(searchPanel);
		
		mainPanel.add(formPanel, new ColumnLayoutData(0.71));
		mainPanel.add(searchFormPanel, new ColumnLayoutData(0.28));
		
		panel.add(mainPanel);
	}

	/**
	 * 
	 * @return
	 */
	private ComboBox getNickList() {
		HttpProxy dataProxy = new HttpProxy("AdminNickListJSON");
		final RecordDef recordDef = new RecordDef(new FieldDef[] {
				new StringFieldDef("description", "description")
				, new StringFieldDef("id", "id")
				, new StringFieldDef("name", "name")
		});
		JsonReader reader = new JsonReader(recordDef);
		reader.setRoot("nickList");
		reader.setId("id");

		Store nickStore = new Store(dataProxy, reader, true);
		nickStore.addStoreListener(new StoreListenerAdapter() {
			@Override
			public void onLoadException(Throwable error) {
				com.google.gwt.user.client.Window.open(GWT.getModuleBaseURL() + "Login", "_self", "");
			}
		});
		nickStore.load();
		
		final ComboBox comboBox = new ComboBox();
		comboBox.setFieldLabel(AdminGWT.dictionary.nick());
		comboBox.setHiddenName("nick");
		comboBox.setStore(nickStore);
		comboBox.setDisplayField("name");
		comboBox.setEmptyText(AdminGWT.dictionary.noNick());
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
	
	/**
	 * 
	 */
	private ComboBox getOperatorList() {
		HttpProxy dataProxy = new HttpProxy("AdminOperatorListJSON");
		final RecordDef recordDef = new RecordDef(new FieldDef[] {
				new StringFieldDef("role", "role.name")
				, new StringFieldDef("id", "id")
				, new StringFieldDef("username", "username")
		});
		JsonReader reader = new JsonReader(recordDef);
		reader.setRoot("operatorList");
		reader.setId("id");

		Store nickStore = new Store(dataProxy, reader, true);
		nickStore.addStoreListener(new StoreListenerAdapter() {
			@Override
			public void onLoadException(Throwable error) {
				com.google.gwt.user.client.Window.open(GWT.getModuleBaseURL() + "Login", "_self", "");
			}
		});
		nickStore.load();
		
		final ComboBox comboBox = new ComboBox();
		comboBox.setFieldLabel(AdminGWT.dictionary.operators());
		comboBox.setHiddenName("operator");
		comboBox.setStore(nickStore);
		comboBox.setDisplayField("username");
		comboBox.setEmptyText(AdminGWT.dictionary.noNick());
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
	
	/**
	 * 
	 * @return
	 */
	private Component getServiceProviderList() {
		HttpProxy dataProxy = new HttpProxy("AdminServiceProviderListJSON");
		final RecordDef recordDef = new RecordDef(new FieldDef[] {
				new StringFieldDef("providerName", "providerName")
				, new StringFieldDef("id", "id")
				, new StringFieldDef("sc", "sc")
		});
		JsonReader reader = new JsonReader(recordDef);
		reader.setRoot("serviceProviderList");
		reader.setId("id");

		Store nickStore = new Store(dataProxy, reader, true);
		nickStore.addStoreListener(new StoreListenerAdapter() {
			@Override
			public void onLoadException(Throwable error) {
				com.google.gwt.user.client.Window.open(GWT.getModuleBaseURL() + "Login", "_self", "");
			}
		});
		nickStore.load();
		
		final ComboBox comboBox = new ComboBox();
		comboBox.setFieldLabel("Service provider");
		comboBox.setHiddenName("serviceProvider");
		comboBox.setStore(nickStore);
		comboBox.setDisplayField("providerName");
		comboBox.setEmptyText("No service provider");
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