package hr.chus.client.smartgwt.client.admin;

import com.google.gwt.core.client.GWT;

import hr.chus.client.smartgwt.client.CChatAdminSmartGWT;
import hr.chus.client.smartgwt.client.ExplorerTreeNode;


public class CChatAdminData {
	
	private static final String CONTEXT_PATH = GWT.getModuleBaseURL().replace(GWT.getModuleName() + "/", "");
	
	private String idSuffix;
	private ExplorerTreeNode[] data;
	

	public CChatAdminData(String idSuffix) {
		this.idSuffix = idSuffix;
	}

	private ExplorerTreeNode[] getData() {
		if (data == null) {
			data = new ExplorerTreeNode[]{
						new ExplorerTreeNode(CChatAdminSmartGWT.dictionary.operators(), "operators-category", "root", CONTEXT_PATH + "images/operators.png", new Operators.Factory(), true, idSuffix),
						new ExplorerTreeNode(CChatAdminSmartGWT.dictionary.nicks(), "nicks-category", "root", CONTEXT_PATH + "images/nicks.png", new Nicks.Factory(), true, idSuffix),
	                    new ExplorerTreeNode(CChatAdminSmartGWT.dictionary.pictures(), "pictures-category", "root", CONTEXT_PATH + "images/pictures.png", new Pictures.Factory(), true, idSuffix),
	                    new ExplorerTreeNode(CChatAdminSmartGWT.dictionary.users(), "users-category", "root", CONTEXT_PATH + "images/users.png", new Users.Factory(), true, idSuffix),
	                    new ExplorerTreeNode(CChatAdminSmartGWT.dictionary.settings(), "settings-category", "root", CONTEXT_PATH + "images/settings.png", new Settings.Factory(), true, idSuffix),
			};
		}
		return data;
	}

	public static ExplorerTreeNode[] getData(String idSuffix) {
		return new CChatAdminData(idSuffix).getData();
	}

}
