package hr.chus.cchat.client.smartgwt.client.admin.ds;

import hr.chus.cchat.client.smartgwt.client.common.Constants;
import hr.chus.cchat.client.smartgwt.client.i18n.DictionaryInstance;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceBooleanField;
import com.smartgwt.client.data.fields.DataSourceFloatField;
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
		setRecordXPath("serviceProviders");
		DataSourceIntegerField pkField = new DataSourceIntegerField("serviceProvider.id");
		pkField.setValueXPath("id");
		pkField.setHidden(true);
		pkField.setPrimaryKey(true);
		
		DataSourceTextField sc = new DataSourceTextField("serviceProvider.sc", DictionaryInstance.dictionary.shortCode(), 50, true);
		sc.setValueXPath("sc");
		DataSourceTextField providerName = new DataSourceTextField("serviceProvider.providerName", DictionaryInstance.dictionary.providerName(), 50, true);
		providerName.setValueXPath("providerName");
		DataSourceTextField serviceName = new DataSourceTextField("serviceProvider.serviceName", DictionaryInstance.dictionary.serviceName(), 50, false);
		serviceName.setValueXPath("serviceName");
		DataSourceFloatField billingAmount = new DataSourceFloatField("serviceProvider.billingAmount", DictionaryInstance.dictionary.billingAmount(), 50, false);
		billingAmount.setValueXPath("billingAmount");
        DataSourceTextField description = new DataSourceTextField("serviceProvider.description", DictionaryInstance.dictionary.description(), 50, false);
        description.setValueXPath("description");
        DataSourceBooleanField disabled = new DataSourceBooleanField("serviceProvider.disabled", DictionaryInstance.dictionary.isDisabled(), 10, true);
        disabled.setValueXPath("disabled");
        DataSourceTextField sendServiceBeanName = new DataSourceTextField("serviceProvider.sendServiceBeanName", DictionaryInstance.dictionary.sendServiceBeanName(), 70, false);
        sendServiceBeanName.setValueXPath("sendServiceBeanName");
        DataSourceBooleanField autoCreated = new DataSourceBooleanField("serviceProvider.autoCreated", DictionaryInstance.dictionary.autoCreated(), 10, true);
        autoCreated.setValueXPath("autoCreated");
        
        DataSourceField languageProvider = new DataSourceField();
        languageProvider.setName("serviceProvider.languageProvider.id");
        languageProvider.setTitle(DictionaryInstance.dictionary.languageProviderPrefix());
        languageProvider.setLength(50);
        languageProvider.setValueXPath("languageProvider/id");
        languageProvider.setForeignKey(LanguageProviderDS.getInstance().getID() + ".id");
        languageProvider.setTypeAsDataSource(LanguageProviderDS.getInstance());
        
		setFields(pkField, sc, providerName, serviceName, billingAmount, description, disabled, sendServiceBeanName, autoCreated, languageProvider);

		setDataFormat(DSDataFormat.JSON);
		setDataURL(Constants.CONTEXT_PATH + "admin/serviceProvider/findAll");
	}

}
