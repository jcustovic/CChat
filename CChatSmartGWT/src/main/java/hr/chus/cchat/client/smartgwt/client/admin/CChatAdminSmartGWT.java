package hr.chus.cchat.client.smartgwt.client.admin;

import hr.chus.cchat.client.smartgwt.client.common.Constants;
import hr.chus.cchat.client.smartgwt.client.common.ExplorerTreeNode;
import hr.chus.cchat.client.smartgwt.client.common.PanelFactory;
import hr.chus.cchat.client.smartgwt.client.common.SideNavigationMenu;
import hr.chus.cchat.client.smartgwt.client.i18n.DictionaryInstance;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.core.KeyIdentifier;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.XMLTools;
import com.smartgwt.client.rpc.RPCManager;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.TabBarControls;
import com.smartgwt.client.types.VisibilityMode;
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
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.LeafClickEvent;
import com.smartgwt.client.widgets.tree.events.LeafClickHandler;

/**
 * 
 * Entry point classes define <code>onModuleLoad()</code>.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 * 
 */
public class CChatAdminSmartGWT extends VLayout implements EntryPoint { 

	private TabSet mainTabSet;
	private SideNavigationMenu sideNav;
	private Menu contextMenu;
	
	public static Integer maxMsgLength;
	
	
	@Override
	public void onModuleLoad() {
		RPCManager.setDefaultPrompt(DictionaryInstance.dictionary.contactingServer());
		RPCManager.setFetchDataPrompt(DictionaryInstance.dictionary.findingRecordThatMatchCriteria());
		RPCManager.setRemoveDataPrompt(DictionaryInstance.dictionary.deletingRecord());
		RPCManager.setSaveDataPrompt(DictionaryInstance.dictionary.savingData());
		
		DateUtil.setDefaultDisplayTimezone("00:00");
		
		setWidth100();
		setHeight100();
		setPadding(5);
		
		getMaxMsgLength();

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
		title.setWidth(300);
		title.setAlign(Alignment.LEFT);
		topBar.addMember(title);
		
		topBar.addFill();
		
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
        imgButton.setHoverWidth(110);
        imgButton.setHoverStyle("interactImageHover");

        imgButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
           
        	@Override
        	public void onClick(ClickEvent event) {
            	SC.say(DictionaryInstance.dictionary.about(), Constants.ABOUT_TEXT);
            }
        });
        topBar.addMember(imgButton);
        topBar.addSpacer(6);
        
        loginSessionChecker();

		HLayout hlayout = new HLayout();
		hlayout.setWidth100();
		hlayout.setHeight100();
		hlayout.setMargin(5);

		SectionStack leftSideLayout = new SectionStack();
		leftSideLayout.setWidth("200px");
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
		mainHtmlFlow.setContents("<b> Admin area </b>");

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

		sideNav = createSideNavigation();
		leftSideLayout.addMember(sideNav);

		hlayout.addMember(leftSideLayout);
		hlayout.addMember(rightSideLayout);

		addMember(topBar);
		addMember(hlayout);
		
		draw();
	}

	private void getMaxMsgLength() {
		DataSource dataSource = new DataSource() {
			
			@Override
			protected void transformResponse(DSResponse response, DSRequest request, Object jsonData) {
				JSONArray object = XMLTools.selectObjects(jsonData, "/value");
				if (object != null && object.get(0).isNull() == null) 
					maxMsgLength = Integer.valueOf(((JSONString) object.get(0)).stringValue());
			}
		};
		dataSource.setDataFormat(DSDataFormat.JSON);
		dataSource.setDataURL(Constants.CONTEXT_PATH + "admin/GetConfiguration?name=smsMaxLength");
		dataSource.invalidateCache();
		dataSource.fetchData(null, null);
	}

	private void loginSessionChecker() {
		final DataSource dataSource = new DataSource() {
			@Override
			protected void transformResponse(DSResponse response, DSRequest request, Object jsonData) {
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
	    		dataSource.fetchData(null, null);
	    	}
	    };
	    refreshTimer.scheduleRepeating(1000 * 30); // 30 seconds
	}

	private SideNavigationMenu createSideNavigation() {
		String idSuffix = "";
		SideNavigationMenu sideNav = new SideNavigationMenu(idSuffix, CChatAdminData.getData(idSuffix), "<b>" + DictionaryInstance.dictionary.menu() + "</b>");
		sideNav.addLeafClickHandler(new LeafClickHandler() {
			
			@Override
			public void onLeafClick(LeafClickEvent event) {
				showMenu(event.getLeaf());
			}
		});
		
		return sideNav;
	}

	private void showMenu(TreeNode node) {
		boolean isExplorerTreeNode = node instanceof ExplorerTreeNode;
		if (isExplorerTreeNode) {
			ExplorerTreeNode explorerTreeNode = (ExplorerTreeNode) node;
			PanelFactory factory = explorerTreeNode.getFactory();
			if (factory != null) {
				String panelID = factory.getID();
				Tab tab = null;
				if (panelID != null) {
					String tabID = "tab_" + panelID;
					tab = mainTabSet.getTab(tabID);
				}
				if (tab == null) {
					Canvas panel = factory.create();
					tab = new Tab();
					tab.setID("tab_" + factory.getID());
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

}
