package hr.chus.cchat.client.smartgwt.client.admin.ds;

import hr.chus.cchat.client.smartgwt.client.common.Constants;
import hr.chus.cchat.client.smartgwt.client.i18n.DictionaryInstance;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
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
		
		DataSourceTextField sc = new DataSourceTextField("serviceProvider.sc", DictionaryInstance.dictionary.shortCode(), 50, true);
		sc.setValueXPath("sc");
		DataSourceTextField providerName = new DataSourceTextField("serviceProvider.providerName", DictionaryInstance.dictionary.name(), 50, true);
		providerName.setValueXPath("providerName");
		DataSourceTextField description = new DataSourceTextField("serviceProvider.description", DictionaryInstance.dictionary.description(), 50, false);
		description.setValueXPath("description");

		setFields(pkField, sc, providerName, description);

		setDataFormat(DSDataFormat.JSON);
		setDataURL(Constants.CONTEXT_PATH + "admin/AdminServiceProviderListJSON");
	}

}
