package hr.chus.cchat.client.smartgwt.client.operator;

import hr.chus.cchat.client.smartgwt.client.common.Constants;
import hr.chus.cchat.client.smartgwt.client.common.ExplorerTreeNode;
import hr.chus.cchat.client.smartgwt.client.i18n.DictionaryInstance;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class CChatOperatorData {
	
	private String idSuffix;
	private ExplorerTreeNode[] data;
	

	public CChatOperatorData(String idSuffix) {
		this.idSuffix = idSuffix;
	}

	private ExplorerTreeNode[] getData() {
		if (data == null) {
			data = new ExplorerTreeNode[]{
	                    new ExplorerTreeNode(DictionaryInstance.dictionary.users(), "users-category", "root", Constants.CONTEXT_PATH + "images/users.png", new Users.Factory(), true, idSuffix),
	                    new ExplorerTreeNode(DictionaryInstance.dictionary.messages(), "messages-category", "root", Constants.CONTEXT_PATH + "images/message_g.png", new Messages.Factory(), true, idSuffix),
	                    new ExplorerTreeNode(DictionaryInstance.dictionary.statistics(), "statistics-category", "root", Constants.CONTEXT_PATH + "images/statistics.png", new Statistics.Factory(), true, idSuffix),
			};
		}
		return data;
	}

	public static ExplorerTreeNode[] getData(String idSuffix) {
		return new CChatOperatorData(idSuffix).getData();
	}

}
