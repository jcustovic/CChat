package hr.chus.client.smartgwt.client.admin.ds;

import hr.chus.client.smartgwt.client.CChatAdminSmartGWT;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class PicturesDS extends DataSource {

	private static PicturesDS instance = null;

	public static PicturesDS getInstance() {
		if (instance == null) {
			instance = new PicturesDS("picturesDS");
		}
		return instance;
	}

	public PicturesDS(String id) {
		setID(id);
		setRecordXPath("pictureList");
		DataSourceIntegerField pkField = new DataSourceIntegerField("picture.id");
		pkField.setValueXPath("id");
		pkField.setHidden(true);
		pkField.setPrimaryKey(true);
		
		DataSourceTextField nickName = new DataSourceTextField("nick.name", CChatAdminSmartGWT.dictionary.nick(), 50, false);
		nickName.setValueXPath("nick/name");
		DataSourceTextField nickId = new DataSourceTextField("picture.nick", CChatAdminSmartGWT.dictionary.nick(), 50, false);
		nickId.setValueXPath("nick/id");
		DataSourceTextField name = new DataSourceTextField("picture.name", CChatAdminSmartGWT.dictionary.name(), 50, true);
		name.setValueXPath("name");
		DataSourceIntegerField length = new DataSourceIntegerField("picture.length", CChatAdminSmartGWT.dictionary.sizeBytes(), 50, true);
		length.setValueXPath("length");
		DataSourceTextField type = new DataSourceTextField("picture.type", CChatAdminSmartGWT.dictionary.type(), 50, true);
		type.setValueXPath("type");
		DataSourceTextField url = new DataSourceTextField("picture.url", "URL", 100, true);
		url.setValueXPath("url");
		
		setFields(pkField, nickName, nickId, name, length, type, url);

		setDataFormat(DSDataFormat.JSON);
		setDataURL(CChatAdminSmartGWT.CONTEXT_PATH + "admin/AdminPictureListJSON");
	}
	
}
