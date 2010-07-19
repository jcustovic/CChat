package hr.chus.client.smartgwt.client;

import com.smartgwt.client.widgets.Canvas;

public interface PanelFactory {

	public Canvas create();

    public String getID();

    public String getDescription();
}
