package hr.chus.cchat.client.smartgwt.client.common;


import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class ExplorerTreeNode extends TreeNode {
	
	private int position;

    public ExplorerTreeNode(String name, String nodeID, String parentNodeID, String icon, PanelFactory factory, boolean enabled, String idSuffix) {
    	this(name, nodeID, parentNodeID, 0, icon, factory, enabled, idSuffix);
    }
    
    public ExplorerTreeNode(String name, String nodeID, String parentNodeID, int position, String icon, PanelFactory factory, boolean enabled, String idSuffix) {
    	this.position = position;
        if (enabled) {
            setName(name);
        } else {
            setName("<span style='color:808080'>" + name + "</span>");
        }
        setNodeID(nodeID.replace("-", "_") + idSuffix);
        setParentNodeID(parentNodeID.replace("-", "_") + idSuffix);
        setIcon(icon);

        setFactory(factory);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExplorerTreeNode that = (ExplorerTreeNode) o;
        if (!getName().equals(that.getName())) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }
    
    
    // Getters & setters

    public void setFactory(PanelFactory factory) { setAttribute("factory", factory); }
    public PanelFactory getFactory() { return (PanelFactory) getAttributeAsObject("factory"); }

    public void setNodeID(String value) { setAttribute("nodeID", value); }
    public String getNodeID() { return getAttribute("nodeID"); }

	public void setParentNodeID(String value) { setAttribute("parentNodeID", value); }
	public void setName(String name) { setAttribute("name", name); }

	public String getName() { return getAttributeAsString("name"); }
	
	public void setIcon(String icon) { setAttribute("icon", icon); }
	public String getIcon() { return getAttributeAsString("icon"); }

	public void setIsOpen(boolean isOpen) { setAttribute("isOpen", isOpen); }
    public void setIconSrc(String iconSrc) { setAttribute("iconSrc", iconSrc); }

    public String getIconSrc() { return getAttributeAsString("iconSrc"); }

	public int getPosition() { return position; } 
    
}
