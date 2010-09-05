package hr.chus.cchat.client.smartgwt.client.filemanager;

import hr.chus.cchat.client.smartgwt.client.i18n.DictionaryInstance;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.NamedFrame;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VStack;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class Upload extends Layout {
	
	private static final String TARGET = "uploadTarget";

	private DynamicForm uploadForm;
	private UploadItem fileItem;
	private UploadListener uploadListener;
	private NamedFrame frame;

	
	public Upload(String action, String fieldName, String title, FormItem... formItems) {
		initComplete(this);
		List<FormItem> items = new ArrayList<FormItem>();
		
		Label label = new Label(DictionaryInstance.dictionary.upload());
		label.setHeight(10);
		
		ValuesManager vm = new ValuesManager();
		uploadForm = new DynamicForm();
		uploadForm.setEncoding(Encoding.MULTIPART);
		uploadForm.setTarget(TARGET);
		uploadForm.setValuesManager(vm);
		uploadForm.setAction(action);
		
		fileItem = new UploadItem(fieldName);
		fileItem.setTitle(title);
		fileItem.setWidth(300);
		fileItem.setRequired(true);
		items.add(fileItem);
		
		for (FormItem formItem : formItems) {
			items.add(formItem);
		}

		Button uploadButton = new Button(DictionaryInstance.dictionary.upload());
		uploadButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent e) {
				Object obj = fileItem.getDisplayValue();
				if (obj != null && !obj.toString().isEmpty()) {
					uploadForm.submitForm();
				} else
					SC.say(DictionaryInstance.dictionary.pleaseSelectAFile());
			}
		});
		VStack stack = new VStack();
		stack.setMembersMargin(10);
		stack.setDefaultLayoutAlign(Alignment.CENTER);

		frame = new NamedFrame(TARGET);
		frame.getElement().getInnerText();
		frame.setWidth("1");
		frame.setHeight("1");
		frame.setVisible(false);

		VStack mainLayout = new VStack();
		mainLayout.addMember(label);

		FormItem[] fitems = new FormItem[items.size()];
		items.toArray(fitems);
		uploadForm.setItems(fitems);
		stack.addMember(uploadForm);
		stack.addMember(uploadButton);
		mainLayout.addMember(stack);
		mainLayout.addMember(frame);
		
		addMember(mainLayout);
	}

	/**
	 * 
	 * @param msg
	 */
	public void uploadComplete(String msg) {
		if (uploadListener != null) {
			uploadListener.uploadComplete(msg);
		}
	}
	
	/**
	 * 
	 * @param msg
	 */
	public void uploadError(String msg) {
		if (uploadListener != null) {
			uploadListener.uploadError(msg);
		}
	}
	
	/**
	 * 
	 * @param upload
	 */
	private native void initComplete(Upload upload) /*-{
		$wnd.uploadComplete = function (msg) {
			upload.@hr.chus.cchat.client.smartgwt.client.filemanager.Upload::uploadComplete(Ljava/lang/String;)(msg);
		};
		$wnd.uploadError = function (msg) {
			upload.@hr.chus.cchat.client.smartgwt.client.filemanager.Upload::uploadError(Ljava/lang/String;)(msg);
		};
	}-*/;
	
	
	// Getters & setters
	
	public void setUploadListener(UploadListener uploadListener) { this.uploadListener = uploadListener; }

}
