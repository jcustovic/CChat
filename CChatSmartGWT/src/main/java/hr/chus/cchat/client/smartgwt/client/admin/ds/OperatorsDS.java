package hr.chus.cchat.client.smartgwt.client.admin.ds;

import hr.chus.cchat.client.smartgwt.client.common.Constants;
import hr.chus.cchat.client.smartgwt.client.i18n.DictionaryInstance;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceBooleanField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public class OperatorsDS extends DataSource {

    private static OperatorsDS instance = null;

    public static OperatorsDS getInstance() {
        if (instance == null) {
            instance = new OperatorsDS("operatorsDS");
        }

        return instance;
    }

    public OperatorsDS(String id) {
        setID(id);
        setRecordXPath("operatorList");
        DataSourceIntegerField pkField = new DataSourceIntegerField("operator.id");
        pkField.setValueXPath("id");
        pkField.setHidden(true);
        pkField.setPrimaryKey(true);

        DataSourceTextField username = new DataSourceTextField("operator.username", DictionaryInstance.dictionary.username(), 50, true);
        username.setValueXPath("username");
        DataSourceTextField surname = new DataSourceTextField("operator.surname", DictionaryInstance.dictionary.surname(), 50, true);
        surname.setValueXPath("surname");
        DataSourceTextField name = new DataSourceTextField("operator.name", DictionaryInstance.dictionary.name(), 50, true);
        name.setValueXPath("name");
        DataSourceTextField email = new DataSourceTextField("operator.email", DictionaryInstance.dictionary.email(), 50, false);
        email.setValueXPath("email");
        DataSourceBooleanField disabled = new DataSourceBooleanField("operator.disabled", DictionaryInstance.dictionary.isDisabled());
        disabled.setValueXPath("disabled");
        DataSourceBooleanField active = new DataSourceBooleanField("operator.isActive", DictionaryInstance.dictionary.isActive());
        active.setValueXPath("isActive");
        DataSourceBooleanField external = new DataSourceBooleanField("operator.isExternal", DictionaryInstance.dictionary.external());
        external.setValueXPath("isExternal");
        DataSourceTextField password = new DataSourceTextField("operator.password", DictionaryInstance.dictionary.password(), 50, true);
        password.setValueXPath("password");

        DataSourceTextField roleId = new DataSourceTextField("operator.role", DictionaryInstance.dictionary.role(), 50, true);
        roleId.setValueXPath("role/id");

        DataSourceTextField roleName = new DataSourceTextField("role.name", DictionaryInstance.dictionary.role(), 50, true);
        roleName.setValueXPath("role/name");

        DataSourceField languagesField = new DataSourceField();
        languagesField.setName("languages");
        languagesField.setLength(50);
        languagesField.setValueXPath("languages/id");
        languagesField.setForeignKey(LanguageDS.getInstance().getID() + ".id");
        languagesField.setTypeAsDataSource(LanguageDS.getInstance());
        languagesField.setMultiple(true);

        setFields(pkField, username, name, surname, email, active, external, disabled, roleId, roleName, password, languagesField);

        setDataFormat(DSDataFormat.JSON);
        //		setDataURL("test/data/json/operatorList.json");
        setDataURL(Constants.CONTEXT_PATH + "admin/AdminOperatorListJSON");
    }

}
