package hr.chus.cchat.client.smartgwt.client.admin.ds;

import hr.chus.cchat.client.smartgwt.client.common.Constants;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
public class SendServiceBeanDS extends DataSource {

    private static SendServiceBeanDS instance = null;

    public static SendServiceBeanDS getInstance() {
        if (instance == null) {
            instance = new SendServiceBeanDS("SendServiceBeanDS");
        }

        return instance;
    }

    public SendServiceBeanDS(String id) {
        setID(id);
        setRecordXPath("sendBeans");
        DataSourceTextField pkField = new DataSourceTextField("name");
        pkField.setValueXPath("name");
        //pkField.setHidden(true);
        pkField.setPrimaryKey(true);

        setFields(pkField);

        setDataFormat(DSDataFormat.JSON);
        setDataURL(Constants.CONTEXT_PATH + "admin/serviceProvider/findAllSendBeans");
    }

}
