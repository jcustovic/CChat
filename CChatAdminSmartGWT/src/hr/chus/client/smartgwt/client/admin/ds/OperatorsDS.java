package hr.chus.client.smartgwt.client.admin.ds;

import hr.chus.client.smartgwt.client.CChatAdminSmartGWT;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceBooleanField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

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
		
		DataSourceTextField username = new DataSourceTextField("operator.username", CChatAdminSmartGWT.dictionary.username(), 50, true);
		username.setValueXPath("username");
		DataSourceTextField surname = new DataSourceTextField("operator.surname", CChatAdminSmartGWT.dictionary.surname(), 50, true);
		surname.setValueXPath("surname");
		DataSourceTextField name = new DataSourceTextField("operator.name", CChatAdminSmartGWT.dictionary.name(), 50, true);
		name.setValueXPath("name");
		DataSourceTextField email = new DataSourceTextField("operator.email", CChatAdminSmartGWT.dictionary.email(), 50, false);
		email.setValueXPath("email");
		DataSourceBooleanField disabled = new DataSourceBooleanField("operator.disabled", CChatAdminSmartGWT.dictionary.isDisabled());
		disabled.setValueXPath("disabled");
		DataSourceBooleanField active = new DataSourceBooleanField("operator.isActive", CChatAdminSmartGWT.dictionary.isActive());
		active.setValueXPath("isActive");
		DataSourceTextField password = new DataSourceTextField("operator.password", CChatAdminSmartGWT.dictionary.password(), 50, true);
		password.setValueXPath("password");
		
		DataSourceTextField roleId = new DataSourceTextField("operator.role", CChatAdminSmartGWT.dictionary.role(), 50, true);
		roleId.setValueXPath("role/id");
		
		DataSourceTextField roleName = new DataSourceTextField("role.name", CChatAdminSmartGWT.dictionary.role(), 50, true);
		roleName.setValueXPath("role/name");
		
		setFields(pkField, username, name, surname, email, active, disabled, roleId, roleName, password);

		setDataFormat(DSDataFormat.JSON);
//		setDataURL("test/data/json/operatorList.json");
		setDataURL(CChatAdminSmartGWT.CONTEXT_PATH + "admin/AdminOperatorListJSON");
	}
	
}
