package hr.chus.cchat.gwt.admin;

import hr.chus.cchat.gwt.i18n.Dictionary;

import com.google.gwt.core.client.GWT;
import com.gwtext.client.core.Connection;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.data.*;
import com.gwtext.client.data.event.StoreListenerAdapter;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.HTMLPanel;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.WindowListenerAdapter;
import com.gwtext.client.widgets.form.*;
import com.gwtext.client.widgets.form.event.FormListenerAdapter;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;

public class UploadPicture extends Window {
	
	private FormPanel formPanel = new FormPanel();
	private Grid grid = new Grid(3, 1);
	private Store store = null;
	private static Dictionary dictionary = (Dictionary) GWT.create(Dictionary.class);

	public UploadPicture(final Store pictureStore) {
		super(dictionary.pictureUploader());
		setSize(400, 350);
		setCloseAction(Window.HIDE);
		setResizable(true);
		setModal(false);
		setIconCls("");
		formPanel.setFileUpload(true);
		formPanel.setFrame(true);
		formPanel.setLabelAlign(Position.LEFT);
		formPanel.setPaddings(5);

		final TextField textField = new TextField(dictionary.picture(), "picture");
		textField.setInputType("file");
		formPanel.setAutoHeight(true);
		formPanel.setAutoWidth(true);
		formPanel.add(textField);
		formPanel.add(getNickList());
		formPanel.add(new Hidden("operation", "upload"));
		RecordDef errorRecordDef = new RecordDef(new FieldDef[]{
				new StringFieldDef("id"),
				new StringFieldDef("msg")
		});
		XmlReader errorReader = new XmlReader("field", errorRecordDef);
		errorReader.setSuccess("@success");
		formPanel.setErrorReader(errorReader);
		grid.setWidget(0, 0, formPanel);
		final Panel centerPanel = new HTMLPanel(dictionary.uploading());
		centerPanel.hide();
		centerPanel.setBorder(true);
		grid.setWidget(1, 0, centerPanel);
		final Button upload = new Button(dictionary.upload(), new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				MessageBox.confirm(dictionary.confirm(), dictionary.reqularConfirm(),
						new MessageBox.ConfirmCallback() {
							public void execute(String btnID) {
								if (btnID.equals("yes")) {
									centerPanel.show();
									if (textField.getText().length() == 0) {
										centerPanel.getEl().update("Please select a file.");
										return;
									}
									centerPanel.getEl().update(dictionary.uploading());
									formPanel.getForm().submit(GWT.getModuleBaseURL() + "AdminPictureUploadAction", null, Connection.POST, dictionary.savingData(), false);
								}
							}
				});
			}
		});
		addButton(upload);
		final Button close = new Button(dictionary.close(), new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				setSize(400, 350);
				pictureStore.reload();
				formPanel.getForm().reset();
				button.setDisabled(true);
				hide();
				centerPanel.getEl().update("");
				grid.getWidget(2, 0).setVisible(false);
			};
		});
		close.setDisabled(true);
		addButton(close);
		formPanel.getForm().addListener(new FormListenerAdapter() {
			@Override
			public void onActionComplete(Form form, int httpStatus, final String responseText) {
				Panel imagePanel = new Panel();
				imagePanel.setAutoScroll(true);
				Image image = (Image) grid.getWidget(2, 0);
				if (image == null) {
					image = new Image();
					grid.setWidget(2, 0, image);
				}
				setSize(400, 500);
				center();
				image.setUrl(responseText.trim());
				image.setVisible(true);
				close.setDisabled(false);
				centerPanel.getEl().update(responseText);
			}

			@Override
			public void onActionFailed(Form form, int httpStatus, String responseText) {
				if (httpStatus == 450) {
					com.google.gwt.user.client.Window.open(GWT.getModuleBaseURL() + "Login", "_self", "");
				} else {
					centerPanel.getEl().update(dictionary.uploadFailed());
				}
			}
		});
		add(grid);
		addListener(new WindowListenerAdapter() {
			@Override
			public void onShow(com.gwtext.client.widgets.Component component) {
				store.reload();
			};
			
			@Override
			public boolean doBeforeHide(Component component) {
				setSize(400, 350);
				pictureStore.reload();
				formPanel.getForm().reset();
				centerPanel.getEl().update("");
				if (grid.getWidget(2, 0) != null) grid.getWidget(2, 0).setVisible(false);
				return true;
			}
		});
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

		store = new Store(dataProxy, reader, true);
		store.addStoreListener(new StoreListenerAdapter() {
			@Override
			public void onLoadException(Throwable error) {
				com.google.gwt.user.client.Window.open(GWT.getModuleBaseURL() + "Login", "_self", "");
			}
		});
//		store.load();
		
		ComboBox comboBox = new ComboBox();
		comboBox.setFieldLabel(dictionary.nick());
		comboBox.setHiddenName("nick");
		comboBox.setStore(store);
		comboBox.setDisplayField("name");
		comboBox.setEmptyText(dictionary.noNick());
		comboBox.setValueField("id");
		comboBox.setAllowBlank(true);
		comboBox.setTypeAhead(true);
		comboBox.setMode(ComboBox.LOCAL);
		comboBox.setTriggerAction(ComboBox.ALL);
		comboBox.setSelectOnFocus(true);
		comboBox.setWidth(150);
		return comboBox;
	}

}
