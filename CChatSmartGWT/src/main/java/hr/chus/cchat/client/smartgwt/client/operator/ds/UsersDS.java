package hr.chus.cchat.client.smartgwt.client.operator.ds;

import hr.chus.cchat.client.smartgwt.client.common.Constants;
import hr.chus.cchat.client.smartgwt.client.i18n.DictionaryInstance;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.XMLTools;
import com.smartgwt.client.data.fields.DataSourceBooleanField;
import com.smartgwt.client.data.fields.DataSourceDateField;
import com.smartgwt.client.data.fields.DataSourceDateTimeField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 *
 */
public class UsersDS extends DataSource {

	private static UsersDS instance = null;
	private double totalCount;

	public static UsersDS getInstance() {
		if (instance == null) {
			instance = new UsersDS("usersDS");
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

	public UsersDS(String id) {
		setID(id);
		setRecordXPath("userList");
		DataSourceIntegerField pkField = new DataSourceIntegerField("user.id", DictionaryInstance.dictionary.userId());
		pkField.setValueXPath("id");
		pkField.setHidden(true);
		pkField.setPrimaryKey(true);
		
		DataSourceTextField nickName = new DataSourceTextField("nick.name", DictionaryInstance.dictionary.nick(), 30, false);
		nickName.setValueXPath("nick/name");
		DataSourceTextField nickId = new DataSourceTextField("user.nick", DictionaryInstance.dictionary.nick(), 15, false);
		nickId.setValueXPath("nick/id");
		DataSourceTextField operatorId = new DataSourceTextField("user.operator", DictionaryInstance.dictionary.operator(), 10, false);
		operatorId.setValueXPath("operator/id");
		DataSourceTextField operatorUsername = new DataSourceTextField("operator.username", DictionaryInstance.dictionary.operator(), 30, false);
		operatorUsername.setValueXPath("operator/username");
		DataSourceTextField serviceProviderName = new DataSourceTextField("serviceProvider.providerName", DictionaryInstance.dictionary.serviceProvider(), 30, false);
		serviceProviderName.setValueXPath("serviceProvider/providerName");
		DataSourceTextField name = new DataSourceTextField("user.name", DictionaryInstance.dictionary.name(), 30, true);
		name.setValueXPath("name");
		DataSourceTextField surname = new DataSourceTextField("user.surname", DictionaryInstance.dictionary.surname(), 40, true);
		surname.setValueXPath("surname");
		DataSourceTextField address = new DataSourceTextField("user.address", DictionaryInstance.dictionary.address(), 50, true);
		address.setValueXPath("address");
		DataSourceDateField birthdate = new DataSourceDateField("user.birthdate", DictionaryInstance.dictionary.birthdate(), 50, true);
		birthdate.setValueXPath("birthdate");
		DataSourceBooleanField deleted = new DataSourceBooleanField("user.deleted", DictionaryInstance.dictionary.deleted());
		deleted.setValueXPath("deleted");
		DataSourceDateTimeField joined = new DataSourceDateTimeField("user.joined", DictionaryInstance.dictionary.joinedDate(), 50, true);
		joined.setValueXPath("joined");
		DataSourceDateTimeField lastMsg = new DataSourceDateTimeField("user.lastMsg", DictionaryInstance.dictionary.lastMsgDate(), 50, true);
		lastMsg.setValueXPath("lastMsg");
		DataSourceIntegerField unreadMsgCount = new DataSourceIntegerField("user.unreadMsgCount", DictionaryInstance.dictionary.unreadMsgCount(), 50, true);
		unreadMsgCount.setValueXPath("unreadMsgCount");
		DataSourceTextField notes = new DataSourceTextField("user.notes", DictionaryInstance.dictionary.notes(), 100, true);
		notes.setValueXPath("notes");
		
		setFields(pkField, nickName, operatorId, operatorUsername, serviceProviderName, name, surname, address, birthdate, deleted, joined, lastMsg, unreadMsgCount, notes, nickId);

		setDataFormat(DSDataFormat.JSON);
		setDataURL(Constants.CONTEXT_PATH + "operator/OperatorUserListJSON");
	}

	public double getTotalCount() { return totalCount; }
	
}
