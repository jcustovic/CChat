package hr.chus.cchat.client.smartgwt.client.admin.ds;

import hr.chus.cchat.client.smartgwt.client.common.Constants;
import hr.chus.cchat.client.smartgwt.client.i18n.DictionaryInstance;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public class LanguageDS extends DataSource {

    private static LanguageDS instance = null;

    public static LanguageDS getInstance() {
        if (instance == null) {
            instance = new LanguageDS("languageDS");
        }

        return instance;
    }

    public LanguageDS(String id) {
        setID(id);
        setRecordXPath("languages");
        DataSourceIntegerField pkField = new DataSourceIntegerField("language.id");
        pkField.setValueXPath("id");
        pkField.setHidden(true);
        pkField.setPrimaryKey(true);

        DataSourceTextField name = new DataSourceTextField("language.name", DictionaryInstance.dictionary.name(), 90, true);
        name.setValueXPath("name");
        DataSourceTextField description = new DataSourceTextField("language.shortCode", DictionaryInstance.dictionary.langShortCode(), 10, false);
        description.setValueXPath("shortCode");

        setFields(pkField, name, description);

        setDataFormat(DSDataFormat.JSON);
        setDataURL(Constants.CONTEXT_PATH + "admin/language/findAll");
    }

}
