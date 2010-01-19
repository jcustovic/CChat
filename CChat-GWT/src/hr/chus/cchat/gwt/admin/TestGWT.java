package hr.chus.cchat.gwt.admin;

import hr.chus.cchat.gwt.image.ImageChooserSample;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class TestGWT implements EntryPoint {

	@Override
	public void onModuleLoad() {
		ImageChooserSample ics = new ImageChooserSample();
		RootPanel.get().add(ics.getViewPanel());
	}

}
