package hr.chus.cchat.gwt.admin;

import hr.chus.cchat.gwt.i18n.Dictionary;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Margins;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.data.Node;
import com.gwtext.client.widgets.*;
import com.gwtext.client.widgets.layout.AccordionLayout;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;
import com.gwtext.client.widgets.tree.event.TreeNodeListenerAdapter;

public class AdminGWT implements EntryPoint {
	
	private AdminOperatorGWT operatorGWT = new AdminOperatorGWT();
	private AdminNickGWT nickGWT = new AdminNickGWT();
	private AdminPictureGWT pictureGWT = new AdminPictureGWT();
	private AdminUserGWT userGWT = new AdminUserGWT();
	private Panel operatorPanel = new Panel();
	private Panel nickPanel = new Panel();
	private Panel picturePanel = new Panel();
	private Panel userPanel = new Panel();
	public static Dictionary dictionary = (Dictionary) GWT.create(Dictionary.class);

	public void onModuleLoad() {
		operatorPanel.setTitle(dictionary.operators());
		operatorPanel.setClosable(true);
		operatorPanel.setAutoScroll(true);
	
		nickPanel.setTitle(dictionary.nicks());
		nickPanel.setClosable(true);
		nickPanel.setAutoScroll(true);
		
		picturePanel.setTitle(dictionary.pictures());
		picturePanel.setClosable(true);
		picturePanel.setAutoScroll(true);
		
		userPanel.setTitle(dictionary.users());
		userPanel.setClosable(true);
		userPanel.setAutoScroll(true);

		Panel panel = new Panel();
		panel.setBorder(false);
		panel.setPaddings(15);
		panel.setLayout(new FitLayout());
		final TabPanel centerPanel = new TabPanel();

		Panel borderPanel = new Panel();
		borderPanel.setLayout(new BorderLayout());


		// add north panel
		// raw html
		final LoadHTML userInfo = new LoadHTML();
		userInfo.setUserInfoUrl(GWT.getModuleBaseURL() + "UserInfoAction");
		final BoxComponent northPanel = new BoxComponent();
		northPanel.setEl(new HTML("<center>" + dictionary.loading() + "</center>").getElement());
		northPanel.setHeight(44);
		borderPanel.add(northPanel, new BorderLayoutData(RegionPosition.NORTH));
		userInfo.loadData(northPanel);
		
	    // Setup timer to refresh list automatically.
	    Timer refreshTimer = new Timer() {
	    	@Override
	    	public void run() {
	    		userInfo.loadData(northPanel);
	    	}
	    };
	    refreshTimer.scheduleRepeating(1000 * 60);

		// add south panel
		Panel southPanel = new HTMLPanel("<p>south - generally for informational stuff," + " also could be for status bar</p>"); 
		southPanel.setHeight(75);
		southPanel.setCollapsible(true);
		southPanel.setTitle("South");

		BorderLayoutData southData = new BorderLayoutData(RegionPosition.SOUTH);
		southData.setMinSize(100);
		southData.setMaxSize(200);
		southData.setMargins(new Margins(0, 0, 0, 0));
		southData.setSplit(true);
		borderPanel.add(southPanel, southData);

		AccordionLayout accordion = new AccordionLayout(true);

		Panel westPanel = new Panel();
		westPanel.setTitle(dictionary.menu());
		westPanel.setCollapsible(true);
		westPanel.setWidth(200);
		westPanel.setLayout(accordion);

		TreeNode menuTreeNode = new TreeNode();
		TreeNode operatorNode = getOperatorTreeNode(centerPanel);
		TreeNode nickNode = getNickTreeNode(centerPanel);
		TreeNode pictureNode = getPictureTreeNode(centerPanel);
		TreeNode userNode = getUserTreeNode(centerPanel);
		menuTreeNode.appendChild(operatorNode);
		menuTreeNode.appendChild(nickNode);
		menuTreeNode.appendChild(pictureNode);
		menuTreeNode.appendChild(userNode);
		
		TreePanel menuTreePanel = new TreePanel();
		menuTreePanel.setRootNode(menuTreeNode);
		menuTreePanel.setRootVisible(false);
		menuTreePanel.setTitle(dictionary.menu());
		menuTreePanel.setIconCls("home-icon");
		menuTreePanel.setAutoHeight(true);
		westPanel.add(menuTreePanel);

		TreeNode settingsTreeNode = new TreeNode();
		
		TreePanel settingsTreePanel = new TreePanel();
		settingsTreePanel.setRootNode(settingsTreeNode);
		settingsTreePanel.setRootVisible(false);
		settingsTreePanel.setTitle(dictionary.setting());
		settingsTreePanel.setIconCls("settings-icon");
		settingsTreePanel.setAutoHeight(true);
		westPanel.add(settingsTreePanel);

		BorderLayoutData westData = new BorderLayoutData(RegionPosition.WEST);
		westData.setSplit(true);
		westData.setMinSize(150);
		westData.setMaxSize(300);
		westData.setMargins(new Margins(0, 5, 0, 0));

		borderPanel.add(westPanel, westData);

		centerPanel.setDeferredRender(false);
		centerPanel.setActiveTab(0);

		final LoadHTML adminInfo = new LoadHTML();
		adminInfo.setUserInfoUrl(GWT.getModuleBaseURL() + "AdminMain");
		Panel centerMainPage = new HTMLPanel();
		centerMainPage.setHtml("<p>" + dictionary.loading() + "</p>");
		adminInfo.loadData(centerMainPage);
		centerMainPage.setTitle(dictionary.main());
		centerMainPage.setAutoScroll(true);
		centerMainPage.setClosable(false);

		centerPanel.add(centerMainPage);

		BorderLayoutData centerData = new BorderLayoutData(RegionPosition.CENTER);
		centerData.setMargins(new Margins(0, 0, 10, 0));
		borderPanel.add(centerPanel, centerData);

		panel.add(borderPanel);

		new Viewport(panel);
	}

	/**
	 * 
	 * @param centerPanel
	 * @return
	 */
	private TreeNode getOperatorTreeNode(final TabPanel centerPanel) {
		TreeNode operatorNode = new TreeNode(dictionary.operators());
		operatorNode.addListener(new TreeNodeListenerAdapter() {
			@Override
			public void onClick(Node node, EventObject e) {
				if (centerPanel.getComponent(operatorPanel.getId()) == null) {
					operatorPanel.removeAll();
					operatorGWT.init();
					operatorPanel.add(operatorGWT.getPanel());
					centerPanel.add(operatorPanel);
				}
				centerPanel.setActiveItemID(operatorPanel.getId());
			}
		});
		operatorNode.setIconCls("operators-icon");
		return operatorNode;
	}
	
	/**
	 * 
	 * @param centerPanel
	 * @return
	 */
	private TreeNode getNickTreeNode(final TabPanel centerPanel) {
		TreeNode nickNode = new TreeNode(dictionary.nicks());
		nickNode.addListener(new TreeNodeListenerAdapter() {
			@Override
			public void onClick(Node node, EventObject e) {
				if (centerPanel.getComponent(nickPanel.getId()) == null) {
					nickPanel.removeAll();
					nickGWT.init();
					nickPanel.add(nickGWT.getPanel());
					centerPanel.add(nickPanel);
				}
				centerPanel.setActiveItemID(nickPanel.getId());
			}
		});
		nickNode.setIconCls("nicks-icon");
		return nickNode;
	}
	
	/**
	 * 
	 * @param centerPanel
	 * @return
	 */
	private TreeNode getPictureTreeNode(final TabPanel centerPanel) {
		TreeNode pictureNode = new TreeNode(dictionary.pictures());
		pictureNode.addListener(new TreeNodeListenerAdapter() {
			@Override
			public void onClick(Node node, EventObject e) {
				if (centerPanel.getComponent(picturePanel.getId()) == null) {
					picturePanel.removeAll();
					pictureGWT.init();
					picturePanel.add(pictureGWT.getPanel());
					centerPanel.add(picturePanel);
				}
				centerPanel.setActiveItemID(picturePanel.getId());
			}
		});
		pictureNode.setIconCls("pictures-icon");
		return pictureNode;
	}
	
	/**
	 * 
	 * @param centerPanel
	 * @return
	 */
	private TreeNode getUserTreeNode(final TabPanel centerPanel) {
		TreeNode userNode = new TreeNode(dictionary.users());
		userNode.addListener(new TreeNodeListenerAdapter() {
			@Override
			public void onClick(Node node, EventObject e) {
				if (centerPanel.getComponent(userPanel.getId()) == null) {
					userPanel.removeAll();
					userGWT.init();
					userPanel.add(userGWT.getPanel());
					centerPanel.add(userPanel);
				}
				centerPanel.setActiveItemID(userPanel.getId());
			}
		});
		userNode.setIconCls("users-icon");
		return userNode;
	}

}
