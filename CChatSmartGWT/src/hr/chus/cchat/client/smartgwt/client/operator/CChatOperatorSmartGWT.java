package hr.chus.cchat.client.smartgwt.client.operator;

import java.util.HashMap;
import java.util.LinkedList;

import hr.chus.cchat.client.smartgwt.client.common.Constants;
import hr.chus.cchat.client.smartgwt.client.common.ExplorerTreeNode;
import hr.chus.cchat.client.smartgwt.client.common.PanelFactory;
import hr.chus.cchat.client.smartgwt.client.common.SideNavigationMenu;
import hr.chus.cchat.client.smartgwt.client.i18n.DictionaryInstance;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.core.KeyIdentifier;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.XMLTools;
import com.smartgwt.client.rpc.RPCManager;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.types.TabBarControls;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.DateUtil;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemIfFunction;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.LeafClickEvent;
import com.smartgwt.client.widgets.tree.events.LeafClickHandler;

/**
 * 
 * Entry point classes define <code>onModuleLoad()</code>.
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 * 
 */
public class CChatOperatorSmartGWT extends VLayout implements EntryPoint {
	
	private TabSet mainTabSet;
	private SideNavigationMenu usersList;
	private SideNavigationMenu sideMenuNav;
	private TabSet leftTabSet;
	private Menu contextMenu;
	private ImgButton activateButton = new ImgButton();
	private boolean operatorIsActive = false;
	

	@Override
	public void onModuleLoad() {
		checkIfActive(null);
		RPCManager.setDefaultPrompt(DictionaryInstance.dictionary.contactingServer());
		RPCManager.setFetchDataPrompt(DictionaryInstance.dictionary.findingRecordThatMatchCriteria());
		RPCManager.setRemoveDataPrompt(DictionaryInstance.dictionary.deletingRecord());
		RPCManager.setSaveDataPrompt(DictionaryInstance.dictionary.savingData());
		
		DateUtil.setDefaultDisplayTimezone("00:00");
		
		setWidth100();
		setHeight100();
		setPadding(5);
		
		ToolStrip topBar = new ToolStrip();
		topBar.setHeight(33);
		topBar.setWidth100();
		
		ImgButton sgwtHomeButton = new ImgButton();
        sgwtHomeButton.setSrc(Constants.CONTEXT_PATH + "images/cchat.png");
        sgwtHomeButton.setWidth(24);
        sgwtHomeButton.setHeight(24);
        sgwtHomeButton.setPrompt(DictionaryInstance.dictionary.chatApp());
        sgwtHomeButton.setHoverStyle("interactImageHover");
        sgwtHomeButton.setShowRollOver(false);
        sgwtHomeButton.setShowDownIcon(false);
        sgwtHomeButton.setShowDown(false);
   
        topBar.addMember(sgwtHomeButton);
        topBar.addSpacer(6);
        
		Label title = new Label("CChat");
		title.setStyleName("application.title");
		title.setAlign(Alignment.LEFT);
		topBar.addMember(title);
		topBar.addSpacer(6);
        
		Layout layout = new Layout();
		layout.setWidth("*");
		layout.setHeight(24);
		layout.setAlign(Alignment.CENTER);
        activateButton.setWidth(24);
        activateButton.setAlign(Alignment.CENTER);
        activateButton.setHeight(24);
        activateButton.setShowFocused(false);
        activateButton.setShowFocusedIcon(false);
        activateButton.setShowRollOver(false);
        activateButton.setShowDownIcon(false);
        activateButton.setShowDown(false);
        activateButton.setHoverWidth(90);
        activateButton.setHoverStyle("interactImageHover");

        activateButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
           
        	@Override
        	public void onClick(ClickEvent event) {
        		checkIfActive(!operatorIsActive);
            }
        });
        layout.addMember(activateButton);
        topBar.addMember(layout);
		
        ImgButton imgButton = new ImgButton();
        imgButton.setWidth(24);
        imgButton.setHeight(24);
        imgButton.setSrc(Constants.CONTEXT_PATH + "images/about.png");
        imgButton.setShowFocused(false);
        imgButton.setShowFocusedIcon(false);
        imgButton.setShowRollOver(false);
        imgButton.setShowDownIcon(false);
        imgButton.setShowDown(false);
        imgButton.setPrompt(DictionaryInstance.dictionary.about());
        imgButton.setHoverWidth(90);
        imgButton.setHoverStyle("interactImageHover");

        imgButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
           
        	@Override
        	public void onClick(ClickEvent event) {
            	SC.say(DictionaryInstance.dictionary.about(), "Author: Jan Čustović, Email: jan_custovic@yahoo.com <br /><br /> Icons thanks to http://pixel-mixer.com");
            }
        });
        topBar.addMember(imgButton);
        
        loginSessionChecker();

		HLayout hlayout = new HLayout();
		hlayout.setWidth100();
		hlayout.setHeight100();
		hlayout.setMargin(5);

		SectionStack leftSideLayout = new SectionStack();
		leftSideLayout.setWidth("250px");
		leftSideLayout.setShowResizeBar(true);
		leftSideLayout.setVisibilityMode(VisibilityMode.MULTIPLE);
		leftSideLayout.setAnimateSections(true);

		SectionStack rightSideLayout = new SectionStack();
		rightSideLayout.setVisibilityMode(VisibilityMode.MULTIPLE);
		rightSideLayout.setAnimateSections(true);

		final HTMLFlow headerHtmlFlow = new HTMLFlow();
		headerHtmlFlow.setContentsURL(Constants.CONTEXT_PATH + "UserInfoAction");
		headerHtmlFlow.setOverflow(Overflow.AUTO);
		headerHtmlFlow.setPadding(5);
		headerHtmlFlow.setHeight(30);

		SectionStackSection header = new SectionStackSection("&nbsp;" + DictionaryInstance.dictionary.header());
		header.setItems(headerHtmlFlow);
		header.setExpanded(true);
		header.setCanCollapse(false);

		HTMLFlow mainHtmlFlow = new HTMLFlow();
		mainHtmlFlow.setOverflow(Overflow.AUTO);
		mainHtmlFlow.setPadding(10);
		mainHtmlFlow.setHeight("86%");
		mainHtmlFlow.setContents("<b> Operator area </b>");

		mainTabSet = createTabSet();
		contextMenu = createContextMenu();

		Tab tab = new Tab();
		tab.setTitle(DictionaryInstance.dictionary.home() + "&nbsp;&nbsp;");
		tab.setWidth(80);
		tab.setIcon(Constants.CONTEXT_PATH + "images/home.png");
		tab.setPane(mainHtmlFlow);

		mainTabSet.addTab(tab);

		SectionStackSection main = new SectionStackSection("&nbsp;" + DictionaryInstance.dictionary.main());
		main.setItems(mainTabSet);
		main.setExpanded(true);
		main.setCanCollapse(false);

		HTMLFlow footerHtmlFlow = new HTMLFlow();
		footerHtmlFlow.setOverflow(Overflow.AUTO);
		footerHtmlFlow.setPadding(10);
		footerHtmlFlow.setHeight("7%");
		footerHtmlFlow.setContents("<b> " + DictionaryInstance.dictionary.footer() + " </b>");

		SectionStackSection footer = new SectionStackSection("&nbsp;" + DictionaryInstance.dictionary.footer());
		footer.setItems(footerHtmlFlow);
		footer.setExpanded(false);

		rightSideLayout.setSections(header, main, footer);

		usersList = createSideUserListNavigation();
		getUsers();
		sideMenuNav = createSideNavigationMenu();
		
		leftTabSet = new TabSet();
		leftTabSet.setTabBarPosition(Side.LEFT);
		leftTabSet.setWidth100();
		leftTabSet.setHeight100();
		
		Tab menuTab = new Tab();
		menuTab.setIcon(Constants.CONTEXT_PATH + "images/home.png");
		menuTab.setPane(sideMenuNav);
		
		Tab usersListTab = new Tab();
		usersListTab.setIcon(Constants.CONTEXT_PATH + "images/users.png");
		usersListTab.setPane(usersList);
		
		leftTabSet.addTab(menuTab);
		leftTabSet.addTab(usersListTab);
		leftTabSet.selectTab(1);
		
		leftSideLayout.addMember(leftTabSet);

		hlayout.addMember(leftSideLayout);
		hlayout.addMember(rightSideLayout);

		addMember(topBar);
		addMember(hlayout);
		RootPanel.get().add(this);		
	}
	
	/**
	 * 
	 */
	private void checkIfActive(final Boolean newActiveStatus) {
		DataSource dataSource = new DataSource() {
			@Override
			protected void transformResponse(DSResponse response, DSRequest request, Object jsonData) {
				JSONArray value = XMLTools.selectObjects(jsonData, "/active");
				boolean active = ((JSONBoolean) value.get(0)).booleanValue();
				operatorIsActive = active;
				if (active) {
					activateButton.setSrc(Constants.CONTEXT_PATH + "images/active.jpg");
			        activateButton.setPrompt(DictionaryInstance.dictionary.active());
				} else {
					activateButton.setSrc(Constants.CONTEXT_PATH + "images/notActive.jpg");
			        activateButton.setPrompt(DictionaryInstance.dictionary.notActive());
			        if (newActiveStatus == null) {
				        SC.ask(DictionaryInstance.dictionary.wouldYouLikeToActivateYourself(), new BooleanCallback() {
							
							@Override
							public void execute(Boolean value) {
								if (value) {
									checkIfActive(true);
								} else {
									leftTabSet.selectTab(0);
								}
							}
						});
			        }
				}
			}
		};
		if (newActiveStatus != null) {
			HashMap<String, Object> params = new HashMap<String, Object>(1);
			params.put("newStatus", newActiveStatus);
			dataSource.setDefaultParams(params);
		}
		dataSource.setDataFormat(DSDataFormat.JSON);
		dataSource.setDataURL(Constants.CONTEXT_PATH + "operator/ActiveService");
		dataSource.fetchData();
	}
	
	/**
	 * 
	 */
	private void loginSessionChecker() {
		final DataSource dataSource = new DataSource() {
			@Override
			protected void transformResponse(DSResponse response, DSRequest request, Object jsonData) {
				if (response.getStatus() < 0) {
					Window.Location.reload();
					return;
				}
				JSONArray value = XMLTools.selectObjects(jsonData, "/loggedIn");
				boolean loggedIn = ((JSONBoolean) value.get(0)).booleanValue();
				if (!loggedIn) {
					Window.Location.reload();
				}
			}
		};
		dataSource.setDataFormat(DSDataFormat.JSON);
		dataSource.setDataURL(Constants.CONTEXT_PATH + "CheckLoggedIn");
		
		Timer refreshTimer = new Timer() {
	    	@Override
	    	public void run() {
	    		dataSource.invalidateCache();
	    		dataSource.fetchData();
	    	}
	    };
	    refreshTimer.scheduleRepeating(1000 * 30); // 30 seconds
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	private TabSet createTabSet() {
		TabSet mainTabSet = new TabSet();

		Layout paneContainerProperties = new Layout();
		paneContainerProperties.setLayoutMargin(0);
		paneContainerProperties.setLayoutTopMargin(1);
		mainTabSet.setPaneContainerProperties(paneContainerProperties);

		mainTabSet.setWidth100();
		mainTabSet.setHeight100();
		mainTabSet.addTabSelectedHandler(new TabSelectedHandler() {
			public void onTabSelected(TabSelectedEvent event) {
				Tab selectedTab = event.getTab();
				String historyToken = selectedTab.getAttribute("historyToken");
				if (historyToken != null) {
					History.newItem(historyToken, false);
				} else {
					History.newItem("main", false);
				}
			}
		});

		LayoutSpacer layoutSpacer = new LayoutSpacer();
		layoutSpacer.setWidth(5);

		mainTabSet.setTabBarControls(TabBarControls.TAB_SCROLLER, TabBarControls.TAB_PICKER, layoutSpacer, null);

		return mainTabSet;
	}

	/**
	 * 
	 * @return
	 */
	private Menu createContextMenu() {
		Menu menu = new Menu();
		menu.setWidth(140);

		MenuItemIfFunction enableCondition = new MenuItemIfFunction() {
			public boolean execute(Canvas target, Menu menu, MenuItem item) {
				int selectedTab = mainTabSet.getSelectedTabNumber();
				return selectedTab != 0;
			}
		};

		MenuItem closeItem = new MenuItem("<u>C</u>lose");
		closeItem.setEnableIfCondition(enableCondition);
		closeItem.setKeyTitle("Alt+C");
		KeyIdentifier closeKey = new KeyIdentifier();
		closeKey.setAltKey(true);
		closeKey.setKeyName("C");
		closeItem.setKeys(closeKey);
		closeItem.addClickHandler(new ClickHandler() {
			public void onClick(MenuItemClickEvent event) {
				int selectedTab = mainTabSet.getSelectedTabNumber();
				mainTabSet.removeTab(selectedTab);
				mainTabSet.selectTab(selectedTab - 1);
			}
		});

		MenuItem closeAllButCurrent = new MenuItem("Close All But Current");
		closeAllButCurrent.setEnableIfCondition(enableCondition);
		closeAllButCurrent.addClickHandler(new ClickHandler() {
			public void onClick(MenuItemClickEvent event) {
				int selected = mainTabSet.getSelectedTabNumber();
				Tab[] tabs = mainTabSet.getTabs();
				int[] tabsToRemove = new int[tabs.length - 2];
				int cnt = 0;
				for (int i = 1; i < tabs.length; i++) {
					if (i != selected) {
						tabsToRemove[cnt] = i;
						cnt++;
					}
				}
				mainTabSet.removeTabs(tabsToRemove);
			}
		});

		MenuItem closeAll = new MenuItem("Close All");
		closeAll.setEnableIfCondition(enableCondition);
		closeAll.addClickHandler(new ClickHandler() {
			public void onClick(MenuItemClickEvent event) {
				Tab[] tabs = mainTabSet.getTabs();
				int[] tabsToRemove = new int[tabs.length - 1];

				for (int i = 1; i < tabs.length; i++) {
					tabsToRemove[i - 1] = i;
				}
				mainTabSet.removeTabs(tabsToRemove);
				mainTabSet.selectTab(0);
			}
		});

		menu.setItems(closeItem, closeAllButCurrent, closeAll);
		return menu;
	}
	
	/**
	 * 
	 * @return
	 */
	private SideNavigationMenu createSideUserListNavigation() {
		SideNavigationMenu usersNav = new SideNavigationMenu("", null, "<b>" + DictionaryInstance.dictionary.users() + "</b>");
		usersNav.addLeafClickHandler(new LeafClickHandler() {
			
			@Override
			public void onLeafClick(LeafClickEvent event) {
				showMenu(event.getLeaf());
			}
		});
		return usersNav;
	}
	
	/**
	 * 
	 * @return
	 */
	private void getUsers() {
		final DataSource dataSource = new DataSource() {
			
			@Override
			protected void transformResponse(DSResponse response, DSRequest request, Object jsonData) {
				LinkedList<ExplorerTreeNode> usersDate = new LinkedList<ExplorerTreeNode>();
				usersDate.add(new ExplorerTreeNode(DictionaryInstance.dictionary.myUsers(), "operatorUserList", "root", Constants.CONTEXT_PATH + "images/users.png", null, true, ""));
				usersDate.add(new ExplorerTreeNode(DictionaryInstance.dictionary.last48HourUsers(), "newestUserList", "root", Constants.CONTEXT_PATH + "images/users.png", null, true, ""));
				usersDate.add(new ExplorerTreeNode(DictionaryInstance.dictionary.randomUsers(), "randomUserList", "root", Constants.CONTEXT_PATH + "images/users.png", null, true, ""));
				
				String[] userTypesArray = new String[] { "operatorUserList", "newestUserList", "randomUserList" };
				for (String userType : userTypesArray) {
					JSONArray users = XMLTools.selectObjects(jsonData, "/" + userType);
					if (users != null && users.size() > 0) {
						for (int i = 0; i < users.size(); i++) {
							JSONObject user = (JSONObject) users.get(i);
							String userId = ((JSONNumber) user.get("id")).toString();
							String userName = ((JSONString) user.get("name")).stringValue();
							String userSurname = ((JSONString) user.get("surname")).stringValue();
							String nameToDisplay = "#" + userId;
							double unreadMsgCount = ((JSONNumber) user.get("unreadMsgCount")).doubleValue();
							if (userName != null) nameToDisplay = userName;
							if (userSurname != null) nameToDisplay += " " + userSurname;
							
							String iconPath = null;
							if (unreadMsgCount > 0) {
								iconPath = Constants.CONTEXT_PATH + "images/new_call.gif";
							} else {
								iconPath = Constants.CONTEXT_PATH + "images/operators.png";
							}
							usersDate.add(new ExplorerTreeNode(nameToDisplay, userId, userType, iconPath, null, true, ""));
						}
					}
				}
				
				Tree tree = new Tree();
				tree.setModelType(TreeModelType.PARENT);
				tree.setNameProperty("name");
				tree.setOpenProperty("isOpen");
				tree.setIdField("nodeID");
				tree.setParentIdField("parentNodeID");
				tree.setRootValue("root");
				tree.setData(usersDate.toArray(new ExplorerTreeNode[0]));
				
				usersList.setData(tree);
				usersList.getData().openAll();
			}
		};
		dataSource.setDataFormat(DSDataFormat.JSON);
		dataSource.setDataURL(Constants.CONTEXT_PATH + "operator/UserListJSON");
		dataSource.fetchData();
		
		Timer refreshTimer = new Timer() {
	    	@Override
	    	public void run() {
	    		dataSource.invalidateCache();
	    		dataSource.fetchData();
	    	}
	    };
	    refreshTimer.scheduleRepeating(1000 * 90); // 90 seconds
	}

	/**
	 * 
	 * @return
	 */
	private SideNavigationMenu createSideNavigationMenu() {
		String idSuffix = "";
		SideNavigationMenu sideNav = new SideNavigationMenu(idSuffix, CChatOperatorData.getData(idSuffix), "<b>" + DictionaryInstance.dictionary.menu() + "</b>");
		sideNav.addLeafClickHandler(new LeafClickHandler() {
			
			@Override
			public void onLeafClick(LeafClickEvent event) {
				showMenu(event.getLeaf());
			}
		});
		return sideNav;
	}
	
	

	/**
	 * 
	 * @param node
	 */
	private void showMenu(TreeNode node) {
		boolean isExplorerTreeNode = node instanceof ExplorerTreeNode;
		if (isExplorerTreeNode) {
			ExplorerTreeNode explorerTreeNode = (ExplorerTreeNode) node;
			PanelFactory factory = explorerTreeNode.getFactory();
			if (factory != null) {
				String panelID = factory.getID();
				Tab tab = null;
				if (panelID != null) {
					String tabID = panelID + "_tab";
					tab = mainTabSet.getTab(tabID);
				}
				if (tab == null) {
					Canvas panel = factory.create();
					tab = new Tab();
					tab.setID(factory.getID() + "_tab");
					// store history token on tab so that when an already open
					// is selected, one can retrieve the
					// history token and update the URL
					tab.setAttribute("historyToken", explorerTreeNode.getNodeID());
					tab.setContextMenu(contextMenu);

					String sampleName = explorerTreeNode.getName();
					String icon = explorerTreeNode.getIcon();
					String imgHTML = Canvas.imgHTML(icon, 16, 16);
					tab.setTitle("<span>" + imgHTML + "&nbsp;" + sampleName + "</span>");
					tab.setPane(panel);
					tab.setCanClose(true);
					mainTabSet.addTab(tab);
					mainTabSet.selectTab(tab);
				} else {
					mainTabSet.selectTab(tab);
				}
				History.newItem(explorerTreeNode.getNodeID(), false);
			}
		}
	}
	
}