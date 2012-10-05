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
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 *
 */
public class MessagesDS extends DataSource {

	private static MessagesDS instance = null;
	private double totalCount;

	public static MessagesDS getInstance() {
		if (instance == null) {
			instance = new MessagesDS("messagesDS");
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
	
	@Override
	protected Object transformRequest(DSRequest dsRequest) {
		return super.transformRequest(dsRequest);
	}

	public MessagesDS(String id) {
		setID(id);
		setRecordXPath("smsMessageList");
		DataSourceIntegerField pkField = new DataSourceIntegerField("smsMessage.id");
		pkField.setValueXPath("id");
		pkField.setHidden(true);
		pkField.setPrimaryKey(true);
		
		DataSourceTextField nickId = new DataSourceTextField("smsMessage.nick", DictionaryInstance.dictionary.nick(), 15, false);
		nickId.setValueXPath("nick/id");
		DataSourceTextField operatorId = new DataSourceTextField("smsMessage.operator", DictionaryInstance.dictionary.operator(), 10, false);
		operatorId.setValueXPath("operator/id");
		DataSourceTextField operatorUsername = new DataSourceTextField("operator.username", DictionaryInstance.dictionary.operator(), 30, false);
		operatorUsername.setValueXPath("operator/username");
		DataSourceTextField serviceProviderId = new DataSourceTextField("smsMessage.serviceProvider", DictionaryInstance.dictionary.serviceProvider(), 15, false);
		serviceProviderId.setValueXPath("serviceProvider/id");
		DataSourceTextField serviceProviderName = new DataSourceTextField("serviceProvider.providerName", DictionaryInstance.dictionary.serviceProvider(), 30, false);
		serviceProviderName.setValueXPath("serviceProvider/providerName");
		DataSourceTextField text = new DataSourceTextField("smsMessage.text", DictionaryInstance.dictionary.text(), 30, true);
		text.setValueXPath("text");
		DataSourceTextField name = new DataSourceTextField("smsMessage.userName", DictionaryInstance.dictionary.name(), 30, true);
		name.setValueXPath("user/name");
		DataSourceTextField surname = new DataSourceTextField("smsMessage.userSurname", DictionaryInstance.dictionary.surname(), 30, true);
		surname.setValueXPath("user/surname");
		DataSourceTextField userId = new DataSourceTextField("smsMessage.userId", "ID", 30, true);
		userId.setValueXPath("user/id");
		DataSourceTextField sc = new DataSourceTextField("smsMessage.sc", DictionaryInstance.dictionary.shortCode(), 40, true);
		sc.setValueXPath("sc");
		DataSourceTextField direction = new DataSourceTextField("smsMessage.direction", DictionaryInstance.dictionary.direction(), 50, true);
		direction.setValueXPath("direction");
		DataSourceDateTimeField time = new DataSourceDateTimeField("smsMessage.time", DictionaryInstance.dictionary.receivedTime(), 50, true);
		time.setValueXPath("time");
		
		setFields(pkField, operatorId, operatorUsername, serviceProviderId, serviceProviderName, text, name, surname, userId, sc, direction, time, nickId);

		setDataFormat(DSDataFormat.JSON);
		setDataURL(Constants.CONTEXT_PATH + "operator/OperatorSMSMessageListJSON");
	}

	public double getTotalCount() { return totalCount; }

}
