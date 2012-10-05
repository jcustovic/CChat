package hr.chus.cchat.client.smartgwt.client.i18n;

import com.google.gwt.core.client.GWT;

/**
 * 
 * Entry point classes define <code>onModuleLoad()</code>.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 * 
 */
public class DictionaryInstance {
	
	public static Dictionary dictionary = (Dictionary) GWT.create(Dictionary.class);
	
}
