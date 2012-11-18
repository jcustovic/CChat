package hr.chus.cchat.client.smartgwt.client.operator.ds;

import hr.chus.cchat.client.smartgwt.client.common.Constants;
import hr.chus.cchat.client.smartgwt.client.i18n.DictionaryInstance;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONString;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.XMLTools;
import com.smartgwt.client.data.fields.DataSourceDateTimeField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public class ConversationDS extends DataSource {

    private static ConversationDS INSTANCE = null;

    private double                totalCount;
    private String                botResponse;

    public static ConversationDS getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConversationDS("conversationDS");
        }

        return INSTANCE;
    }

    @Override
    protected void transformResponse(final DSResponse p_response, final DSRequest p_request, final Object p_data) {
        JSONArray value = XMLTools.selectObjects(p_data, "/totalCount");
        if (value != null && value.size() > 0) {
            totalCount = ((JSONNumber) value.get(0)).doubleValue();
        }
        value = XMLTools.selectObjects(p_data, "/botMsg");
        if (value != null && value.size() > 0) {
            botResponse = ((JSONString) value.get(0)).stringValue();
        } else {
            botResponse = "";
        }

        super.transformResponse(p_response, p_request, p_data);
    }

    public ConversationDS(final String p_id) {
        setID(p_id);
        setRecordXPath("conversationList");
        final DataSourceIntegerField pkField = new DataSourceIntegerField("messageId");
        pkField.setValueXPath("messageId");
        pkField.setHidden(true);
        pkField.setPrimaryKey(true);

        final DataSourceTextField text = new DataSourceTextField("text", DictionaryInstance.dictionary.text(), 500, false);
        text.setValueXPath("text");
        final DataSourceDateTimeField time = new DataSourceDateTimeField("time", DictionaryInstance.dictionary.receivedTime(), 120, true);
        time.setValueXPath("time");
        final DataSourceTextField operator = new DataSourceTextField("operator", DictionaryInstance.dictionary.operator(), 100, false);
        operator.setValueXPath("operatorUsername");
        final DataSourceTextField direction = new DataSourceTextField("direction", DictionaryInstance.dictionary.direction(), 30, true);
        direction.setValueXPath("direction");
        final DataSourceTextField msisdn = new DataSourceTextField("msisdn", DictionaryInstance.dictionary.msisdn(), 20, true);
        msisdn.setValueXPath("msisdn");

        setFields(pkField, time, msisdn, text, operator, direction);

        setDataFormat(DSDataFormat.JSON);
        setDataURL(Constants.CONTEXT_PATH + "operator/UserConversationJSON");
    }

    // Getters & setters

    public final double getTotalCount() {
        return totalCount;
    }

    public final String getBotResponse() {
        return botResponse;
    }

}
