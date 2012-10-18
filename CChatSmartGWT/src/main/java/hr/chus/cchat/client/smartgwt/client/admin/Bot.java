package hr.chus.cchat.client.smartgwt.client.admin;

import hr.chus.cchat.client.smartgwt.client.common.Constants;
import hr.chus.cchat.client.smartgwt.client.common.PanelFactory;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.XMLTools;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public class Bot extends HLayout {

    private static final String DESCRIPTION = "Bot layout";

    public static class Factory implements PanelFactory {

        private String id;

        public Canvas create() {
            Bot panel = new Bot();
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

        final ListGrid messageDataGrid = new ListGrid();
        messageDataGrid.setWidth(500);
        messageDataGrid.setHeight(200);
        messageDataGrid.setCanEdit(false);

        final ListGridField msgField = new ListGridField("text", "Conversation");
        messageDataGrid.setFields(msgField);

        final DynamicForm sendMsgForm = new DynamicForm();

        final DataSource dataSource = new DataSource() {

            @Override
            protected void transformResponse(DSResponse response, DSRequest request, Object jsonData) {
                final JSONArray value = XMLTools.selectObjects(jsonData, "/response");
                final String message = ((JSONString) value.get(0)).stringValue();

                messageDataGrid.getRecordList().addAt(new MessageRecord("Bot: " + message), 0);
                
                response.setData(new Record[] { new Record(sendMsgForm.getValues()) });
                messageDataGrid.scrollToRow(0);
            }
        };
        
        dataSource.setDataFormat(DSDataFormat.JSON);
        dataSource.setDataURL(Constants.CONTEXT_PATH + "admin/TalkToBot");

        final DataSourceTextField botIdField = new DataSourceTextField("botId", "Bot", 40, true);
        final DataSourceTextField userNameField = new DataSourceTextField("message", "Message", 100, true);

        dataSource.setFields(botIdField, userNameField);

        sendMsgForm.setDataSource(dataSource);
        sendMsgForm.setAddDropValues(false);
        sendMsgForm.setUseAllDataSourceFields(false);
        sendMsgForm.setGroupTitle("Talk to bot");
        sendMsgForm.setIsGroup(true);
        sendMsgForm.setWidth(300);
        sendMsgForm.setHeight(180);
        sendMsgForm.setNumCols(2);
        sendMsgForm.setColWidths(60, "*");
        sendMsgForm.setPadding(5);

        final TextItem botIdItem = new TextItem();
        botIdItem.setName("botId");
        botIdItem.setValue("AliceBot");
        botIdItem.setTitle("Bot");
        botIdItem.setTitleAlign(Alignment.LEFT);
        botIdItem.setDisabled(true);
        botIdItem.setWidth("*");

        final TextAreaItem messageItem = new TextAreaItem();
        messageItem.setTitle("Say");
        messageItem.setTitleAlign(Alignment.LEFT);
        messageItem.setName("message");
        messageItem.setLength(100);
        messageItem.setColSpan(2);
        messageItem.setWidth("*");
        messageItem.setHeight("*");

        sendMsgForm.setFields(botIdItem, messageItem);

        final IButton sendButton = new IButton("Send");
        sendButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                if (sendMsgForm.validate()) {
                    messageDataGrid.getRecordList().addAt(new MessageRecord("You: " + messageItem.getValueAsString()), 0);
//                    messageDataGrid.addData(new MessageRecord("You: " + messageItem.getValueAsString()));
                    sendMsgForm.submit();
                    messageItem.clearValue();
                }
            }
        });

        final VLayout layout = new VLayout(8);
        layout.addMember(messageDataGrid);
        layout.addMember(sendMsgForm);
        layout.addMember(sendButton);

        canvas.addChild(layout);

        return canvas;
    }

    class MessageRecord extends ListGridRecord {

        public MessageRecord() {
            super();
        }

        public MessageRecord(String p_text) {
            super();
            setText(p_text);
        }

        public String getText() {
            return getAttribute("text");
        }

        public void setText(String p_text) {
            setAttribute("text", p_text);
        }

    }

}
