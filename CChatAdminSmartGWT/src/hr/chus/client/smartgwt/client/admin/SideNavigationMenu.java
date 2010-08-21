package hr.chus.client.smartgwt.client.admin;

import hr.chus.client.smartgwt.client.ExplorerTreeNode;

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

	private String idSuffix = "";

	private ExplorerTreeNode[] menuItems = CChatAdminData.getData(idSuffix);

	public SideNavigationMenu() {
		setWidth100();
		setHeight100();
		setCustomIconProperty("icon");
		setAnimateFolderTime(100);
		setAnimateFolders(true);
		setAnimateFolderSpeed(1000);
//		setNodeIcon("silk/application_view_list.png");
		setShowSortArrow(SortArrow.CORNER);
		setShowAllRecords(true);
		setLoadDataOnDemand(false);
		setCanSort(false);
		setCanResizeFields(false);

		TreeGridField field = new TreeGridField();
		field.setCanFilter(true);
		field.setName("name");
		field.setTitle("<b>Menu</b>");
		setFields(field);

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

	public ExplorerTreeNode[] getShowcaseData() {
		return menuItems;
	}

}
