package hr.chus.cchat.client.smartgwt.client.admin;

import hr.chus.cchat.client.smartgwt.client.common.ExplorerTreeNode;
import hr.chus.cchat.client.smartgwt.client.i18n.DictionaryInstance;

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
						new ExplorerTreeNode(DictionaryInstance.dictionary.operators(), "operators-category", "root", CChatAdminSmartGWT.CONTEXT_PATH + "images/operators.png", new Operators.Factory(), true, idSuffix),
						new ExplorerTreeNode(DictionaryInstance.dictionary.nicks(), "nicks-category", "root", CChatAdminSmartGWT.CONTEXT_PATH + "images/nicks.png", new Nicks.Factory(), true, idSuffix),
	                    new ExplorerTreeNode(DictionaryInstance.dictionary.pictures(), "pictures-category", "root", CChatAdminSmartGWT.CONTEXT_PATH + "images/pictures.png", new Pictures.Factory(), true, idSuffix),
	                    new ExplorerTreeNode(DictionaryInstance.dictionary.users(), "users-category", "root", CChatAdminSmartGWT.CONTEXT_PATH + "images/users.png", new Users.Factory(), true, idSuffix),
	                    new ExplorerTreeNode(DictionaryInstance.dictionary.messages(), "messages-category", "root", CChatAdminSmartGWT.CONTEXT_PATH + "images/message_g.png", new Messages.Factory(), true, idSuffix),
	                    new ExplorerTreeNode(DictionaryInstance.dictionary.statistics(), "statistics-category", "root", CChatAdminSmartGWT.CONTEXT_PATH + "images/statistics.png", new Statistics.Factory(), true, idSuffix),
	                    new ExplorerTreeNode(DictionaryInstance.dictionary.settings(), "settings-category", "root", CChatAdminSmartGWT.CONTEXT_PATH + "images/settings.png", new Settings.Factory(), true, idSuffix),
			};
		}
		return data;
	}

	public static ExplorerTreeNode[] getData(String idSuffix) {
		return new CChatAdminData(idSuffix).getData();
	}

}
