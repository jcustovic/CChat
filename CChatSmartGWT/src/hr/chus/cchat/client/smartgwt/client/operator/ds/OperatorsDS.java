package hr.chus.cchat.client.smartgwt.client.operator.ds;

import hr.chus.cchat.client.smartgwt.client.common.Constants;
import hr.chus.cchat.client.smartgwt.client.i18n.DictionaryInstance;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceBooleanField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class OperatorsDS extends DataSource {
	
	private static OperatorsDS instance = null;

	public static OperatorsDS getInstance() {
		if (instance == null) {
			instance = new OperatorsDS("operatorsDS");
		}
		return instance;
	}

	public OperatorsDS(String id) {
		setID(id);
		setRecordXPath("operatorList");
		DataSourceIntegerField pkField = new DataSourceIntegerField("operator.id");
		pkField.setValueXPath("id");
		pkField.setHidden(true);
		pkField.setPrimaryKey(true);
		
		DataSourceTextField username = new DataSourceTextField("operator.username", DictionaryInstance.dictionary.username(), 50, true);
		username.setValueXPath("username");
		DataSourceTextField surname = new DataSourceTextField("operator.surname", DictionaryInstance.dictionary.surname(), 50, true);
		surname.setValueXPath("surname");
		DataSourceTextField name = new DataSourceTextField("operator.name", DictionaryInstance.dictionary.name(), 50, true);
		name.setValueXPath("name");
		DataSourceTextField email = new DataSourceTextField("operator.email", DictionaryInstance.dictionary.email(), 50, false);
		email.setValueXPath("email");
		DataSourceBooleanField disabled = new DataSourceBooleanField("operator.disabled", DictionaryInstance.dictionary.isDisabled());
		disabled.setValueXPath("disabled");
		DataSourceBooleanField active = new DataSourceBooleanField("operator.isActive", DictionaryInstance.dictionary.isActive());
		active.setValueXPath("isActive");
		DataSourceTextField password = new DataSourceTextField("operator.password", DictionaryInstance.dictionary.password(), 50, true);
		password.setValueXPath("password");
		
		DataSourceTextField roleId = new DataSourceTextField("operator.role", DictionaryInstance.dictionary.role(), 50, true);
		roleId.setValueXPath("role/id");
		
		DataSourceTextField roleName = new DataSourceTextField("role.name", DictionaryInstance.dictionary.role(), 50, true);
		roleName.setValueXPath("role/name");
		
		setFields(pkField, username, name, surname, email, active, disabled, roleId, roleName, password);

		setDataFormat(DSDataFormat.JSON);
		setDataURL(Constants.CONTEXT_PATH + "operator/OperatorOperatorListJSON");
	}
	
}
