package hr.chus.client.smartgwt.client.admin.ds;

import hr.chus.client.smartgwt.client.CChatAdminSmartGWT;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class RolesDS extends DataSource {
	
	private static RolesDS instance = null;

	public static RolesDS getInstance() {
		if (instance == null) {
			instance = new RolesDS("rolesDS");
		}
		return instance;
	}

	public RolesDS(String id) {
		setID(id);
		setRecordXPath("roleList");
		DataSourceIntegerField pkField = new DataSourceIntegerField("id");
		pkField.setHidden(true);
		pkField.setPrimaryKey(true);
		
		DataSourceTextField name = new DataSourceTextField("name", CChatAdminSmartGWT.dictionary.name(), 50, true);
		name.setValueXPath("name");
		DataSourceTextField description = new DataSourceTextField("description", CChatAdminSmartGWT.dictionary.description(), 200, false);
		description.setValueXPath("description");

		setFields(pkField, name, description);

		setDataFormat(DSDataFormat.JSON);
//		setDataURL("test/data/json/roleList.json");
		setDataURL(CChatAdminSmartGWT.CONTEXT_PATH + "admin/AdminRoleListJSON");
	}
}