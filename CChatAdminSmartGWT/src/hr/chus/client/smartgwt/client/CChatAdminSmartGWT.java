package hr.chus.client.smartgwt.client;

import java.util.Date;

import hr.chus.client.smartgwt.client.admin.SideNavigationMenu;
import hr.chus.client.smartgwt.client.i18n.Dictionary;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.core.KeyIdentifier;
import com.smartgwt.client.rpc.RPCManager;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.TabBarControls;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.util.DateDisplayFormatter;
import com.smartgwt.client.util.DateInputFormatter;
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
 * @author Jan Čustović (jan_custovic@yahoo.com)
 * 
 */
public class CChatAdminSmartGWT extends VLayout implements EntryPoint {
	
	public static final String CONTEXT_PATH = GWT.getModuleBaseURL().replace(GWT.getModuleName() + "/", ""); 

	private TabSet mainTabSet;
	private SideNavigationMenu sideNav;
	private Menu contextMenu;
	public static Dictionary dictionary = (Dictionary) GWT.create(Dictionary.class);
	
	public static DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm");

	public static DateDisplayFormatter dateDisplayFormatter = new DateDisplayFormatter() {
        public String format(Date date) {
            if (date == null) return null;
            return dateFormat.format(date);
        }
    };

    public static DateInputFormatter dateInputFormatter = new DateInputFormatter() {
        public Date parse(String s) {
            if (s == null) return null;
            return dateFormat.parse(s);
        }
    };



	@Override
	public void onModuleLoad() {
		RPCManager.setDefaultPrompt(dictionary.contactingServer());
		RPCManager.setFetchDataPrompt(dictionary.findingRecordThatMatchCriteria());
		RPCManager.setRemoveDataPrompt(dictionary.deletingRecord());
		RPCManager.setSaveDataPrompt(dictionary.savingData());
		
		DateUtil.setDefaultDisplayTimezone("00:00");
//		DateUtil.setShortDateDisplayFormatter(dateDisplayFormatter);
//		DateUtil.setNormalDateDisplayFormatter(dateDisplayFormatter);
//		DateUtil.setDateInputFormatter(dateInputFormatter);
		
		setWidth100();
		setHeight100();

		ToolStrip topBar = new ToolStrip();
		topBar.setHeight(33);
		topBar.setWidth100();
		
		ImgButton sgwtHomeButton = new ImgButton();
        sgwtHomeButton.setSrc(CONTEXT_PATH + "images/cchat.png");
        sgwtHomeButton.setWidth(24);
        sgwtHomeButton.setHeight(24);
        sgwtHomeButton.setPrompt(dictionary.chatApp());
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
        imgButton.setSrc(CONTEXT_PATH + "images/about.png");
        imgButton.setShowFocused(false);
        imgButton.setShowFocusedIcon(false);
        imgButton.setShowRollOver(false);
        imgButton.setShowDownIcon(false);
        imgButton.setShowDown(false);
        imgButton.setPrompt(dictionary.about());
        imgButton.setHoverWidth(110);
        imgButton.setHoverStyle("interactImageHover");

        imgButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
           
        	@Override
        	public void onClick(ClickEvent event) {
            	SC.say(dictionary.about(), "Author: Jan Čustović, Email: jan_custovic@yahoo.com <br /><br /> Icons thanks to http://pixel-mixer.com");
            }
        });
        topBar.addMember(imgButton);
        topBar.addSpacer(6);
        

		HLayout hlayout = new HLayout();
		hlayout.setWidth100();
		hlayout.setHeight100();
		hlayout.setMargin(5);

		SectionStack leftSideLayout = new SectionStack();
		leftSideLayout.setWidth("10%");
		leftSideLayout.setShowResizeBar(true);
		leftSideLayout.setVisibilityMode(VisibilityMode.MULTIPLE);
		leftSideLayout.setAnimateSections(true);

		SectionStack rightSideLayout = new SectionStack();
		rightSideLayout.setVisibilityMode(VisibilityMode.MULTIPLE);
		rightSideLayout.setAnimateSections(true);

		final HTMLFlow headerHtmlFlow = new HTMLFlow();
		headerHtmlFlow.setContentsURL(CONTEXT_PATH + "admin/UserInfoAction");
		headerHtmlFlow.setOverflow(Overflow.AUTO);
		headerHtmlFlow.setPadding(10);
		headerHtmlFlow.setHeight("10%");
		
		Timer refreshTimer = new Timer() {
	    	@Override
	    	public void run() {
	    		headerHtmlFlow.setContentsURL(null);
	    	}
	    };
	    refreshTimer.scheduleRepeating(1000 * 60 * 5); // 5 minutes

		SectionStackSection header = new SectionStackSection("Header");
		header.setItems(headerHtmlFlow);
		header.setExpanded(true);

		HTMLFlow mainHtmlFlow = new HTMLFlow();
		mainHtmlFlow.setOverflow(Overflow.AUTO);
		mainHtmlFlow.setPadding(10);
		mainHtmlFlow.setHeight("80%");
		mainHtmlFlow.setContents("<b> Admin area </b>");

		mainTabSet = createTabSet();
		contextMenu = createContextMenu();

		Tab tab = new Tab();
		tab.setTitle("Home&nbsp;&nbsp;");
		tab.setWidth(80);
		tab.setIcon(CONTEXT_PATH + "images/home.png");
		tab.setPane(mainHtmlFlow);

		mainTabSet.addTab(tab);

		SectionStackSection main = new SectionStackSection("Main");
		main.setItems(mainTabSet);
		main.setExpanded(true);
		main.setCanCollapse(false);

		HTMLFlow footerHtmlFlow = new HTMLFlow();
		footerHtmlFlow.setOverflow(Overflow.AUTO);
		footerHtmlFlow.setPadding(10);
		footerHtmlFlow.setHeight("10%");
		footerHtmlFlow.setContents("<b> Footer </b>");

		SectionStackSection footer = new SectionStackSection("Footer");
		footer.setItems(footerHtmlFlow);
		footer.setExpanded(true);

		rightSideLayout.setSections(header, main, footer);

		sideNav = createSideNavigation();
		leftSideLayout.addMember(sideNav);

		hlayout.addMember(leftSideLayout);
		hlayout.addMember(rightSideLayout);

		addMember(topBar);
		addMember(hlayout);
		RootPanel.get().add(this);
	}

	/**
	 * 
	 * @return
	 */
	private SideNavigationMenu createSideNavigation() {
		SideNavigationMenu sideNav = new SideNavigationMenu();
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
					if (icon == null) {
						icon = "silk/plugin.png";
					}
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

		mainTabSet.setTabBarControls(TabBarControls.TAB_SCROLLER,
				TabBarControls.TAB_PICKER, layoutSpacer, null);

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

}
