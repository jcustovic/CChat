package hr.chus.cchat.client.smartgwt.client.operator.ds;

import hr.chus.cchat.client.smartgwt.client.common.Constants;
import hr.chus.cchat.client.smartgwt.client.i18n.DictionaryInstance;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.XMLTools;
import com.smartgwt.client.data.fields.DataSourceDateTimeField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * @author Jan Čustović (jan_custovic@yahoo.com)
 */
public class ConversationDS extends DataSource {

    private static ConversationDS instance = null;
    private double                totalCount;

    public static ConversationDS getInstance() {
        if (instance == null) {
            instance = new ConversationDS("conversationDS");
        }
        return instance;
    }

    @Override
    protected void transformResponse(DSResponse response, DSRequest request, Object data) {
        JSONArray value = XMLTools.selectObjects(data, "/totalCount");
        if (value != null && value.size() > 0) {
            totalCount = ((JSONNumber) value.get(0)).doubleValue();
        }
        super.transformResponse(response, request, data);
    }

    public ConversationDS(String id) {
        setID(id);
        setRecordXPath("conversationList");
        DataSourceIntegerField pkField = new DataSourceIntegerField("messageId");
        pkField.setValueXPath("messageId");
        pkField.setHidden(true);
        pkField.setPrimaryKey(true);

        DataSourceTextField text = new DataSourceTextField("text", DictionaryInstance.dictionary.text(), 500, false);
        text.setValueXPath("text");
        DataSourceDateTimeField time = new DataSourceDateTimeField("time", DictionaryInstance.dictionary.receivedTime(), 120, true);
        time.setValueXPath("time");
        DataSourceTextField operator = new DataSourceTextField("operator", DictionaryInstance.dictionary.operator(), 100, false);
        operator.setValueXPath("operatorUsername");
        DataSourceTextField direction = new DataSourceTextField("direction", DictionaryInstance.dictionary.direction(), 30, true);
        direction.setValueXPath("direction");
        DataSourceTextField msisdn = new DataSourceTextField("msisdn", DictionaryInstance.dictionary.msisdn(), 20, true);
        msisdn.setValueXPath("msisdn");

        setFields(pkField, time, msisdn, text, operator, direction);

        setDataFormat(DSDataFormat.JSON);
        setDataURL(Constants.CONTEXT_PATH + "operator/UserConversationJSON");
    }

    // Getters & setters

    public double getTotalCount() {
        return totalCount;
    }

}
