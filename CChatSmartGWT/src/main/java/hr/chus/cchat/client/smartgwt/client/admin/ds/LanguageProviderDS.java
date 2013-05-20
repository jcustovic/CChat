package hr.chus.cchat.client.smartgwt.client.admin.ds;

import hr.chus.cchat.client.smartgwt.client.common.Constants;
import hr.chus.cchat.client.smartgwt.client.i18n.DictionaryInstance;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;


public class LanguageProviderDS extends DataSource {

    private static LanguageProviderDS instance = null;

    public static LanguageProviderDS getInstance() {
        if (instance == null) {
            instance = new LanguageProviderDS("LanguageProvider");
        }

        return instance;
    }

    public LanguageProviderDS(String id) {
        setID(id);
        setRecordXPath("languageProviders");
        DataSourceIntegerField pkField = new DataSourceIntegerField("languageProvider.id");
        pkField.setValueXPath("id");
        pkField.setHidden(true);
        pkField.setPrimaryKey(true);

        DataSourceTextField name = new DataSourceTextField("languageProvider.sendServiceBeanName", DictionaryInstance.dictionary.sendServiceBeanName(), 90, false);
        name.setValueXPath("sendServiceBeanName");
        DataSourceTextField description = new DataSourceTextField("languageProvider.prefix", DictionaryInstance.dictionary.prefix(), 40, true);
        description.setValueXPath("prefix");
        
        DataSourceField languageField = new DataSourceField();
        languageField.setName("languageProvider.language.id");
        languageField.setTitle(DictionaryInstance.dictionary.language());
        languageField.setLength(50);
        languageField.setValueXPath("language/id");
        languageField.setForeignKey(LanguageDS.getInstance().getID() + ".id");
        languageField.setTypeAsDataSource(LanguageDS.getInstance());

        setFields(pkField, name, description, languageField);

        setDataFormat(DSDataFormat.JSON);
        setDataURL(Constants.CONTEXT_PATH + "admin/languageProvider/findAll");
    }

}
