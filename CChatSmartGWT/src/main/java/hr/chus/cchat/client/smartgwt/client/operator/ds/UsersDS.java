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
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public class UsersDS extends DataSource {

    private static UsersDS INSTANCE = null;
    private double         totalCount;

    public static UsersDS getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UsersDS("usersDS");
        }

        return INSTANCE;
    }

    @Override
    protected void transformResponse(final DSResponse p_response, final DSRequest p_request, final Object p_data) {
        final JSONArray value = XMLTools.selectObjects(p_data, "/totalCount");
        if (value != null && value.size() > 0) {
            totalCount = ((JSONNumber) value.get(0)).doubleValue();
        }
        super.transformResponse(p_response, p_request, p_data);
    }

    @Override
    protected Object transformRequest(final DSRequest p_dsRequest) {
        return super.transformRequest(p_dsRequest);
    }

    public UsersDS(final String p_id) {
        setID(p_id);
        setRecordXPath("userList");
        final DataSourceIntegerField pkField = new DataSourceIntegerField("user.id", DictionaryInstance.dictionary.userId());
        pkField.setValueXPath("id");
        pkField.setHidden(true);
        pkField.setPrimaryKey(true);

        final DataSourceTextField nickName = new DataSourceTextField("nick.name", DictionaryInstance.dictionary.nick(), 30, false);
        nickName.setValueXPath("nick/name");
        final DataSourceTextField nickId = new DataSourceTextField("user.nick", DictionaryInstance.dictionary.nick(), 15, false);
        nickId.setValueXPath("nick/id");
        final DataSourceTextField operatorId = new DataSourceTextField("user.operator", DictionaryInstance.dictionary.operator(), 10, false);
        operatorId.setValueXPath("operator/id");
        final DataSourceTextField botId = new DataSourceTextField("user.bot", DictionaryInstance.dictionary.bot(), 10, false);
        botId.setValueXPath("bot/id");
        final DataSourceTextField operatorUsername = new DataSourceTextField("operator.username", DictionaryInstance.dictionary.operator(), 30, false);
        operatorUsername.setValueXPath("operator/username");
        final DataSourceTextField serviceProviderName = new DataSourceTextField("serviceProvider.providerName",
                DictionaryInstance.dictionary.serviceProvider(), 30, false);
        serviceProviderName.setValueXPath("serviceProvider/providerName");
        final DataSourceTextField name = new DataSourceTextField("user.name", DictionaryInstance.dictionary.name(), 30, true);
        name.setValueXPath("name");
        final DataSourceTextField surname = new DataSourceTextField("user.surname", DictionaryInstance.dictionary.surname(), 40, true);
        surname.setValueXPath("surname");
        final DataSourceTextField address = new DataSourceTextField("user.address", DictionaryInstance.dictionary.address(), 50, true);
        address.setValueXPath("address");
        final DataSourceDateField birthdate = new DataSourceDateField("user.birthdate", DictionaryInstance.dictionary.birthdate(), 50, true);
        birthdate.setValueXPath("birthdate");
        final DataSourceBooleanField deleted = new DataSourceBooleanField("user.deleted", DictionaryInstance.dictionary.deleted());
        deleted.setValueXPath("deleted");
        final DataSourceDateTimeField joined = new DataSourceDateTimeField("user.joined", DictionaryInstance.dictionary.joinedDate(), 50, true);
        joined.setValueXPath("joined");
        final DataSourceDateTimeField lastMsg = new DataSourceDateTimeField("user.lastMsg", DictionaryInstance.dictionary.lastMsgDate(), 50, true);
        lastMsg.setValueXPath("lastMsg");
        final DataSourceIntegerField unreadMsgCount = new DataSourceIntegerField("user.unreadMsgCount", DictionaryInstance.dictionary.unreadMsgCount(), 50,
                true);
        unreadMsgCount.setValueXPath("unreadMsgCount");
        final DataSourceTextField notes = new DataSourceTextField("user.notes", DictionaryInstance.dictionary.notes(), 100, true);
        notes.setValueXPath("notes");

        setFields(pkField, nickName, operatorId, botId, operatorUsername, serviceProviderName, name, surname, address, birthdate, deleted, joined, lastMsg,
                unreadMsgCount, notes, nickId);

        setDataFormat(DSDataFormat.JSON);
        setDataURL(Constants.CONTEXT_PATH + "operator/OperatorUserListJSON");
    }

    public final double getTotalCount() {
        return totalCount;
    }

}
