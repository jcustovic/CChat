package hr.chus.cchat.client.smartgwt.client.operator.ds;

import hr.chus.cchat.client.smartgwt.client.common.Constants;
import hr.chus.cchat.client.smartgwt.client.i18n.DictionaryInstance;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
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

        DataSourceTextField name = new DataSourceTextField("nick.name", DictionaryInstance.dictionary.name(), 50, true);
        name.setValueXPath("name");
        DataSourceTextField description = new DataSourceTextField("nick.description", DictionaryInstance.dictionary.description(), 200, false);
        description.setValueXPath("description");

        setFields(pkField, name, description);

        setDataFormat(DSDataFormat.JSON);
        setDataURL(Constants.CONTEXT_PATH + "operator/CommonNickListJSON");
    }

}
