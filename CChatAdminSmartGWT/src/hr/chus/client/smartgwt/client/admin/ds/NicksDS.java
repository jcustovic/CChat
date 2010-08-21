package hr.chus.client.smartgwt.client.admin.ds;

import hr.chus.client.smartgwt.client.CChatAdminSmartGWT;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class NicksDS extends DataSource {
	
	private static NicksDS instance = null;

	public static NicksDS getInstance() {
		if (instance == null) {
			instance = new NicksDS("nicksDS");
		}
		return instance;
	}

	public NicksDS(String id) {
		setID(id);
		setRecordXPath("nickList");
		DataSourceIntegerField pkField = new DataSourceIntegerField("nick.id");
		pkField.setValueXPath("id");
		pkField.setHidden(true);
		pkField.setPrimaryKey(true);
		
		DataSourceTextField name = new DataSourceTextField("nick.name", CChatAdminSmartGWT.dictionary.name(), 50, true);
		name.setValueXPath("name");
		DataSourceTextField description = new DataSourceTextField("nick.description", CChatAdminSmartGWT.dictionary.description(), 200, false);
		description.setValueXPath("description");

		setFields(pkField, name, description);

		setDataFormat(DSDataFormat.JSON);
//		setDataURL("test/data/json/nickList.json");
		setDataURL(CChatAdminSmartGWT.CONTEXT_PATH + "admin/AdminNickListJSON");
	}

}
