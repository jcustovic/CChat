package hr.chus.cchat.client.smartgwt.client.common;

import hr.chus.cchat.client.smartgwt.client.i18n.DictionaryInstance;

import com.smartgwt.client.types.SortArrow;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class SideNavigationMenu extends TreeGrid {

	private ExplorerTreeNode[] menuItems;

	public SideNavigationMenu(String idSuffix, ExplorerTreeNode[] menuItems, String title) {
		this.menuItems = menuItems;
		setWidth100();
		setHeight100();
		setCustomIconProperty("icon");
		setAnimateFolderTime(100);
		setAnimateFolders(true);
		setAnimateFolderSpeed(1000);
		setShowSortArrow(SortArrow.CORNER);
		setShowAllRecords(true);
		setLoadDataOnDemand(false);
		setCanSort(false);
		setCanResizeFields(false);

		TreeGridField field = new TreeGridField();
		field.setCanFilter(true);
		field.setName("name");
		field.setTitle(title);
		setFields(field);
		
		setEmptyMessage(DictionaryInstance.dictionary.noItemsToShow());

		Tree tree = new Tree();
		tree.setModelType(TreeModelType.PARENT);
		tree.setNameProperty("name");
		tree.setOpenProperty("isOpen");
		tree.setIdField("nodeID");
		tree.setParentIdField("parentNodeID");
		tree.setRootValue("root" + idSuffix);

		tree.setData(menuItems);

		setData(tree);
	}

	public ExplorerTreeNode[] getInterfaceData() {
		return menuItems;
	}

	public ExplorerTreeNode[] getMenuItems() { return menuItems; }
	
}
