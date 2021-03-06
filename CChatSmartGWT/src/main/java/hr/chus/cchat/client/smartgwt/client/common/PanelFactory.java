package hr.chus.cchat.client.smartgwt.client.common;

import com.smartgwt.client.widgets.Canvas;

/**
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 *
 */
public interface PanelFactory {

	public Canvas create();

    public String getID();

    public String getDescription();
    
}
