package hr.chus.cchat.client.smartgwt.client.admin.ds;

import hr.chus.cchat.client.smartgwt.client.admin.CChatAdminSmartGWT;
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
 * @author Jan Čustović (jan_custovic@yahoo.com)
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
		DataSourceIntegerField pkField = new DataSourceIntegerField("user.id");
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
		DataSourceTextField serviceProviderId = new DataSourceTextField("user.serviceProvider", DictionaryInstance.dictionary.serviceProvider(), 15, false);
		serviceProviderId.setValueXPath("serviceProvider/id");
		DataSourceTextField serviceProviderName = new DataSourceTextField("serviceProvider.providerName", DictionaryInstance.dictionary.serviceProvider(), 30, false);
		serviceProviderName.setValueXPath("serviceProvider/providerName");
		DataSourceTextField msisdn = new DataSourceTextField("user.msisdn", DictionaryInstance.dictionary.msisdn(), 30, true);
		msisdn.setValueXPath("msisdn");
		DataSourceTextField name = new DataSourceTextField("user.name", DictionaryInstance.dictionary.name(), 30, true);
		name.setValueXPath("name");
		DataSourceTextField surname = new DataSourceTextField("user.surname", DictionaryInstance.dictionary.surname(), 40, true);
		surname.setValueXPath("surname");
		DataSourceTextField address = new DataSourceTextField("user.address", DictionaryInstance.dictionary.address(), 50, true);
		address.setValueXPath("address");
		DataSourceDateField birthDate = new DataSourceDateField("user.birthDate", DictionaryInstance.dictionary.birthDate(), 50, true);
		birthDate.setValueXPath("birthDate");
		DataSourceBooleanField deleted = new DataSourceBooleanField("user.deleted", DictionaryInstance.dictionary.deleted());
		deleted.setValueXPath("deleted");
		DataSourceDateTimeField joined = new DataSourceDateTimeField("user.joined", DictionaryInstance.dictionary.joinedDate(), 50, true);
		joined.setValueXPath("joined");
		DataSourceTextField notes = new DataSourceTextField("user.notes", DictionaryInstance.dictionary.notes(), 100, true);
		notes.setValueXPath("notes");
		
		setFields(pkField, nickName, operatorId, operatorUsername, serviceProviderId, serviceProviderName, msisdn, name, surname, address, birthDate, deleted, joined, notes, nickId);

		setDataFormat(DSDataFormat.JSON);
		setDataURL(CChatAdminSmartGWT.CONTEXT_PATH + "admin/AdminUserListJSON");
	}

	public double getTotalCount() { return totalCount; }
	
}
