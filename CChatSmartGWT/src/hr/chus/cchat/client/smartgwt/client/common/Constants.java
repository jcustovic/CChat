package hr.chus.cchat.client.smartgwt.client.common;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.smartgwt.client.util.DateDisplayFormatter;
import com.smartgwt.client.util.DateInputFormatter;

/**
 * 
 * @author Jan Čustović
 *
 */
public class Constants {
	
	public static final String CONTEXT_PATH = GWT.getModuleBaseURL().replace(GWT.getModuleName() + "/", "");
	
	public static DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss");
	public static DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd.MM.yyyy");

	public static DateDisplayFormatter dateDisplayFormatter = new DateDisplayFormatter() {
        public String format(Date date) {
            if (date == null) return null;
            return dateTimeFormat.format(date);
        }
    };

    public static DateInputFormatter dateInputFormatter = new DateInputFormatter() {
        public Date parse(String s) {
            if (s == null) return null;
            return dateTimeFormat.parse(s);
        }
    };

}
