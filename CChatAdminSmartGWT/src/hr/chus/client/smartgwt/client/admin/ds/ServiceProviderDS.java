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
public class ServiceProviderDS extends DataSource {
	
	private static ServiceProviderDS instance = null;

	public static ServiceProviderDS getInstance() {
		if (instance == null) {
			instance = new ServiceProviderDS("serviceProviderDS");
		}
		return instance;
	}

	public ServiceProviderDS(String id) {
		setID(id);
		setRecordXPath("serviceProviderList");
		DataSourceIntegerField pkField = new DataSourceIntegerField("serviceProvider.id");
		pkField.setHidden(true);
		pkField.setPrimaryKey(true);
		
		DataSourceTextField sc = new DataSourceTextField("serviceProvider.sc", CChatAdminSmartGWT.dictionary.shortCode(), 50, true);
		sc.setValueXPath("sc");
		DataSourceTextField providerName = new DataSourceTextField("serviceProvider.providerName", CChatAdminSmartGWT.dictionary.name(), 50, true);
		providerName.setValueXPath("providerName");
		DataSourceTextField description = new DataSourceTextField("serviceProvider.description", CChatAdminSmartGWT.dictionary.description(), 50, true);
		description.setValueXPath("description");

		setFields(pkField, sc, providerName, description);

		setDataFormat(DSDataFormat.JSON);
		setDataURL(CChatAdminSmartGWT.CONTEXT_PATH + "admin/AdminServiceProviderListJSON");
	}

}
