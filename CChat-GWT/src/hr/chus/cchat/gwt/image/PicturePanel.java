package hr.chus.cchat.gwt.image;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Frame;
import com.gwtext.client.core.*;
import com.gwtext.client.data.*;
import com.gwtext.client.util.Format;
import com.gwtext.client.widgets.*;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.event.WindowListenerAdapter;
import com.gwtext.client.widgets.grid.*;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;

import java.util.Date;

public abstract class PicturePanel extends Panel {

	private Window sourceWindow = null;
	private String sourceIconCls = "source-icon";
	private ToolbarButton sourceButton;
	private Toolbar toolbar;

	protected static final String EVENT = "event";
	protected static final String MESSAGE = "message";

	private RecordDef recordDef;
	private Store store;
	protected Panel panel;

	protected PicturePanel() {
		setTitle(getTitle());
		setClosable(true);
		setTopToolbar(new Toolbar());
		setPaddings(20);
		setLayout(new FitLayout());
		setBorder(false);
		setAutoScroll(true);
		addListener(new PanelListenerAdapter() {
			public void onActivate(Panel panel) {
				PicturePanel.this.onActivate();
			}
		});
	}

	protected void onActivate() {
		Panel viewPanel = getViewPanel();
		if (viewPanel instanceof Window) {
			((Window) viewPanel).show();
		}
	}

	protected void afterRender() {
		ButtonListenerAdapter listener = new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				Window window = getSourceWindow();
				window.show(sourceButton.getId());
			}
		};
		sourceButton = new ToolbarButton("View Source", listener);
		sourceButton.setIconCls(sourceIconCls);

		if (getSourceUrl() == null) {
			sourceButton.setDisabled(true);
		}
		toolbar = getTopToolbar();

		toolbar.addFill();
		toolbar.addButton(sourceButton);

		addViewPanel();
	}

	private void addViewPanel() {
		Panel mainPanel = new Panel();
		mainPanel.setBorder(false);
		mainPanel.setLayout(new BorderLayout());

		Panel viewPanel = getViewPanel();
		if (viewPanel instanceof Window) {
			viewPanel.show();
			viewPanel = new Panel();
		}
		viewPanel.setAutoScroll(true);
		viewPanel.setBorder(false);

		BorderLayoutData centerLayoutData = new BorderLayoutData(RegionPosition.CENTER);
		// centerLayoutData.setMargins(new Margins(15, 15, 15, 15));
		mainPanel.add(viewPanel, centerLayoutData);

		boolean hasIntro = getIntro() != null;

		if (hasIntro) {
			Panel rightPanel = new Panel();
			rightPanel.setPaddings(0, 15, 0, 0);
			rightPanel.setWidth(210);
			rightPanel.setLayout(new VerticalLayout(15));

			Panel introPanel = new Panel();
			introPanel.setIconCls("book-icon");
			introPanel.setFrame(true);
			introPanel.setTitle("Overview");
			introPanel.setHtml("<p>" + getIntro() + "</p>");
			// introPanel.setPaddings(0, 0, 0, 15);
			rightPanel.add(introPanel);
			if (showEvents()) {
				GridPanel loggerGrid = getLoggerGrid();
				loggerGrid.setWidth(195);
				loggerGrid.setHeight(300);
				rightPanel.add(loggerGrid);
			}

			BorderLayoutData eastLayoutData = new BorderLayoutData(RegionPosition.EAST);
			// eastLayoutData.setMargins(new Margins(0, 15, 0, 0));
			mainPanel.add(rightPanel, eastLayoutData);
		}
		add(mainPanel);
	}

	protected void log(String eventType, String messsage) {
		if (recordDef != null) {
			Record record = recordDef.createRecord(new Object[] { new Date(), eventType, messsage });
			store.insert(0, record);
		}
	}

	protected boolean showEvents() {
		return false;
	}

	private Window getSourceWindow() {
		if (sourceWindow == null) {
			sourceWindow = new Window();
			sourceWindow.setTitle("Source");
			sourceWindow.setLayout(new FitLayout());
			sourceWindow.setIconCls(sourceIconCls);

			int width = 640;
			int userWidth = Ext.getBody().getWidth(true) - 20;
			if (userWidth < width) {
				width = userWidth;
			}
			sourceWindow.setWidth(width);

			int height = 600;
			int userHeight = Ext.getBody().getHeight(true) - 96;
			if (userHeight < height) {
				height = userHeight;
			}
			sourceWindow.setHeight(height);

			sourceWindow.setAnimateTarget(sourceButton.getId());

			int[] panelXY = getPosition();

			int windowTop = panelXY[1] + toolbar.getEl().getHeight(true) + 20;
			int windowLeft = Ext.getBody().getWidth(true) - (sourceWindow.getWidth() + 20);
			sourceWindow.setPosition(windowLeft, windowTop);
			sourceWindow.addListener(new WindowListenerAdapter() {
				public void onClose(Panel panel) {
					sourceWindow = null;
				}
			});
			TabPanel sourceTabPanel = new TabPanel();
			sourceTabPanel.setActiveTab(0);

			String sourceUrl = getSourceUrl();

			if (sourceUrl != null) {
				Panel sourceTab = new Panel("Java");
				sourceTab.setLayout(new FitLayout());
				sourceTab.setIconCls("java-icon");
				Frame frame = new Frame(sourceUrl);
				sourceTab.add(frame);
				sourceTabPanel.add(sourceTab);
			}

			String htmlUrl = getHtmlUrl();
			if (htmlUrl != null) {
				Panel htmlTab = new Panel("HTML");
				htmlTab.setLayout(new FitLayout());
				Frame frame = new Frame(htmlUrl);
				htmlTab.setIconCls("html-icon");
				sourceTabPanel.add(frame);
			}

			String cssUrl = getCssUrl();
			if (cssUrl != null) {
				Panel cssTab = new Panel("CSS");
				cssTab.setLayout(new FitLayout());
				cssTab.setIconCls("css-icon");
				Frame frame = new Frame(cssUrl);
				cssTab.add(frame);
				sourceTabPanel.add(cssTab);
			}

			String xmlDataUrl = getXmlDataUrl();
			if (xmlDataUrl != null) {
				Panel xmlTab = new Panel("XML");
				xmlTab.setLayout(new FitLayout());
				xmlTab.setIconCls("xml-icon");
				Frame frame = new Frame(xmlDataUrl);
				xmlTab.add(frame);
				sourceTabPanel.add(xmlTab);
			}
			String jsonDataUrl = getJsonDataUrl();
			if (jsonDataUrl != null) {
				Panel jsonTab = new Panel("Json");
				jsonTab.setLayout(new FitLayout());
				jsonTab.setIconCls("json-icon");
				Frame frame = new Frame(jsonDataUrl);
				jsonTab.add(frame);
				sourceTabPanel.add(jsonTab);
			}
			sourceWindow.add(sourceTabPanel);
		}
		return sourceWindow;
	}

	private GridPanel getLoggerGrid() {
		recordDef = new RecordDef(new FieldDef[] { new DateFieldDef("date")
				, new StringFieldDef("type")
				, new StringFieldDef("message") });

		store = new Store(recordDef);
		ColumnModel columnModel = new ColumnModel(new ColumnConfig[] {
				new ColumnConfig("&nbsp", "type", 3, true, new Renderer() {
					public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
						if (value.equals(EVENT)) {
							cellMetadata.setHtmlAttribute("style=\"background:#FFFF88;padding:5px\"");
						} else {
							cellMetadata.setHtmlAttribute("style=\"background:#4096EE;padding:5px\"");
						}
						return "";
					}
				}),

				new ColumnConfig("Time", "date", 70, true, new Renderer() {
					public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
						DateTimeFormat formatter = DateTimeFormat.getFormat("hh:mm:ss");
						return formatter.format((Date) value);
					}
				}) });
		GridPanel gridPanel = new GridPanel(store, columnModel);
		gridPanel.setTitle("Logger");
		gridPanel.setAutoExpandColumn("date");
		gridPanel.addTool(new Tool(Tool.MINUS, new Function() {
			public void execute() {
				store.removeAll();
				store.commitChanges();
			}
		}, "Clear Log"));

		GridView view = new GridView() {
			public String getRowClass(Record record, int index, RowParams rowParams, Store store) {
				rowParams.setBody(Format.format("<p>{0}</p>", record.getAsString("message")));
				return "";
			}
		};

		view.setEnableRowBody(true);
		view.setForceFit(true);
		gridPanel.setView(view);

		return gridPanel;
	}

	// Getters & setters
	
	public String getSourceUrl() { return null; }
	
	public String getHtmlUrl() { return null; }

	public String getXmlDataUrl() { return null; }
	
	public String getJsonDataUrl() { return null; }

	public String getCssUrl() { return null; }
	
	public String getIntro() { return null; }

	public abstract Panel getViewPanel();
	
}
