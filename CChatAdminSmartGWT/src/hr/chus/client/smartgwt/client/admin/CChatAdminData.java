package hr.chus.client.smartgwt.client.admin;

import hr.chus.client.smartgwt.client.CChatAdminSmartGWT;
import hr.chus.client.smartgwt.client.ExplorerTreeNode;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class CChatAdminData {
	
	private String idSuffix;
	private ExplorerTreeNode[] data;
	

	public CChatAdminData(String idSuffix) {
		this.idSuffix = idSuffix;
	}

	private ExplorerTreeNode[] getData() {
		if (data == null) {
			data = new ExplorerTreeNode[]{
						new ExplorerTreeNode(CChatAdminSmartGWT.dictionary.operators(), "operators-category", "root", CChatAdminSmartGWT.CONTEXT_PATH + "images/operators.png", new Operators.Factory(), true, idSuffix),
						new ExplorerTreeNode(CChatAdminSmartGWT.dictionary.nicks(), "nicks-category", "root", CChatAdminSmartGWT.CONTEXT_PATH + "images/nicks.png", new Nicks.Factory(), true, idSuffix),
	                    new ExplorerTreeNode(CChatAdminSmartGWT.dictionary.pictures(), "pictures-category", "root", CChatAdminSmartGWT.CONTEXT_PATH + "images/pictures.png", new Pictures.Factory(), true, idSuffix),
	                    new ExplorerTreeNode(CChatAdminSmartGWT.dictionary.users(), "users-category", "root", CChatAdminSmartGWT.CONTEXT_PATH + "images/users.png", new Users.Factory(), true, idSuffix),
	                    new ExplorerTreeNode(CChatAdminSmartGWT.dictionary.settings(), "settings-category", "root", CChatAdminSmartGWT.CONTEXT_PATH + "images/settings.png", new Settings.Factory(), true, idSuffix),
	                    new ExplorerTreeNode(CChatAdminSmartGWT.dictionary.messages(), "messages-category", "root", CChatAdminSmartGWT.CONTEXT_PATH + "images/message_g.gif", new Messages.Factory(), true, idSuffix),
			};
		}
		return data;
	}

	public static ExplorerTreeNode[] getData(String idSuffix) {
		return new CChatAdminData(idSuffix).getData();
	}

}
