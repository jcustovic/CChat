package hr.chus.cchat.client.smartgwt.client.common.ds;

import hr.chus.cchat.client.smartgwt.client.common.Constants;
import hr.chus.cchat.client.smartgwt.client.i18n.DictionaryInstance;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceBooleanField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public class BotDS extends DataSource {

    private static BotDS INSTANCE = null;

    public static BotDS getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BotDS("botDS");
        }

        return INSTANCE;
    }

    public BotDS(final String p_id) {
        setID(p_id);
        setRecordXPath("robotList");
        final DataSourceIntegerField pkField = new DataSourceIntegerField("nick.id");
        pkField.setValueXPath("id");
        pkField.setHidden(true);
        pkField.setPrimaryKey(true);

        final DataSourceTextField name = new DataSourceTextField("bot.name", DictionaryInstance.dictionary.name(), 50, true);
        name.setValueXPath("name");
        final DataSourceTextField description = new DataSourceTextField("bot.description", DictionaryInstance.dictionary.description(), 200, false);
        description.setValueXPath("description");
        final DataSourceBooleanField online = new DataSourceBooleanField("bot.online", DictionaryInstance.dictionary.keyword());
        online.setValueXPath("online");

        setFields(pkField, name, description, online);

        setDataFormat(DSDataFormat.JSON);
        setDataURL(Constants.CONTEXT_PATH + "common/RobotListJSON");
    }

}
