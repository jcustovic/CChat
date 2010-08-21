package hr.chus.client.smartgwt.client;

import com.smartgwt.client.widgets.Canvas;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public interface PanelFactory {

	public Canvas create();

    public String getID();

    public String getDescription();
}
