package hr.chus.client.smartgwt.client.admin.ds;

import hr.chus.client.smartgwt.client.CChatAdminSmartGWT;

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
 * @author Jan Čustović (jan_custovic@yahoo.com)
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
		
		DataSourceTextField nickId = new DataSourceTextField("smsMessage.nick", CChatAdminSmartGWT.dictionary.nick(), 15, false);
		nickId.setValueXPath("nick/id");
		DataSourceTextField operatorId = new DataSourceTextField("smsMessage.operator", CChatAdminSmartGWT.dictionary.operator(), 10, false);
		operatorId.setValueXPath("operator/id");
		DataSourceTextField operatorUsername = new DataSourceTextField("operator.username", CChatAdminSmartGWT.dictionary.operator(), 30, false);
		operatorUsername.setValueXPath("operator/username");
		DataSourceTextField serviceProviderId = new DataSourceTextField("smsMessage.serviceProvider", CChatAdminSmartGWT.dictionary.serviceProvider(), 15, false);
		serviceProviderId.setValueXPath("serviceProvider/id");
		DataSourceTextField serviceProviderName = new DataSourceTextField("serviceProvider.providerName", CChatAdminSmartGWT.dictionary.serviceProvider(), 30, false);
		serviceProviderName.setValueXPath("serviceProvider/providerName");
		DataSourceTextField msisdn = new DataSourceTextField("smsMessage.msisdn", CChatAdminSmartGWT.dictionary.msisdn(), 30, true);
		msisdn.setValueXPath("user/msisdn");
		DataSourceTextField text = new DataSourceTextField("smsMessage.text", CChatAdminSmartGWT.dictionary.text(), 30, true);
		text.setValueXPath("text");
		DataSourceTextField sc = new DataSourceTextField("smsMessage.sc", CChatAdminSmartGWT.dictionary.shortCode(), 40, true);
		sc.setValueXPath("sc");
		DataSourceTextField direction = new DataSourceTextField("smsMessage.direction", CChatAdminSmartGWT.dictionary.direction(), 50, true);
		direction.setValueXPath("direction");
		DataSourceDateTimeField time = new DataSourceDateTimeField("smsMessage.time", CChatAdminSmartGWT.dictionary.receivedTime(), 50, true);
		time.setValueXPath("time");
		
		setFields(pkField, operatorId, operatorUsername, serviceProviderId, serviceProviderName, msisdn, text, sc, direction, time, nickId);

		setDataFormat(DSDataFormat.JSON);
		setDataURL(CChatAdminSmartGWT.CONTEXT_PATH + "admin/AdminSMSMessageListJSON");
	}

	public double getTotalCount() { return totalCount; }

}
