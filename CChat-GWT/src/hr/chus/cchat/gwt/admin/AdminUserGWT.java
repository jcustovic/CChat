package hr.chus.cchat.gwt.admin;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.core.SortDir;
import com.gwtext.client.core.TextAlign;
import com.gwtext.client.core.UrlParam;
import com.gwtext.client.data.*;
import com.gwtext.client.util.DateUtil;
import com.gwtext.client.util.Format;
import com.gwtext.client.widgets.*;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.grid.*;
import com.gwtext.client.widgets.grid.event.RowSelectionListenerAdapter;
import com.gwtext.client.widgets.layout.ColumnLayout;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.RowLayoutData;

import java.util.Date;

public class AdminUserGWT {

	private Panel panel = null;
	private GridPanel grid;
	private boolean showPreview = true;

	private Renderer renderTopic = new Renderer() {
		public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
			return Format.format("<b><a href=\"http://extjs.com/forum/showthread.php?t={2}\"  "
									+ "target=\"_blank\">{0}</a></b>  "
									+ "<a href=\"http://extjs.com/forum/forumdisplay.php?f={3}\" target=\"_blank\">{1} Forum</a>",
							new String[] { (String) value,
									record.getAsString("forumtitle"),
									record.getId(),
									record.getAsString("forumid"), });
		}
	};

	private Renderer renderLast = new Renderer() {
		public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
			Date lastPost = record.getAsDate("lastpost");
			String lastPostStr = DateUtil.format(lastPost, "M j, Y, g:i a");
			return Format.format("{0}<br/>by {1}", new String[] { lastPostStr, record.getAsString("lastposter") });
		}
	};
	
	/**
	 * 
	 * @return
	 */
	public Panel getPanel() {
		return panel;
	}

	public void init() {
		panel = new Panel();
		panel.setBorder(false);
		panel.setPaddings(15);
		
		Panel mainPanel = new Panel();
		mainPanel.setLayout(new ColumnLayout());
		mainPanel.setWidth(1100);
		mainPanel.setPaddings(5);
		mainPanel.setBorder(false);
		mainPanel.setFrame(true);
		
		final FormPanel formPanel = new FormPanel();
		formPanel.setFrame(true);
		formPanel.setLabelAlign(Position.LEFT);
		formPanel.setWidth(650);
		
		Panel bindPanel = new Panel();  
		bindPanel.setLayout(new RowLayout());

		DataProxy dataProxy = new ScriptTagProxy("http://extjs.com/forum/topics-browse-remote.php");

		final RecordDef recordDef = new RecordDef(
				new FieldDef[] { new StringFieldDef("title"),
						new StringFieldDef("forumtitle"),
						new StringFieldDef("forumid"),
						new StringFieldDef("author"),
						new IntegerFieldDef("replycount"),
						new DateFieldDef("lastpost", "lastpost", "timestamp"),
						new StringFieldDef("lastposter"),
						new StringFieldDef("excerpt") });
		JsonReader reader = new JsonReader(recordDef);
		reader.setRoot("topics");
		reader.setTotalProperty("totalCount");
		reader.setId("threadid");

		final Store store = new Store(dataProxy, reader, true);
		store.setDefaultSort("lastpost", SortDir.DESC);

		ColumnConfig topicColumn = new ColumnConfig("Topic", "title", 420, false, renderTopic, "topic");
		topicColumn.setCss("white-space:normal;");

		ColumnConfig authorColumn = new ColumnConfig("Author", "author", 100);
		authorColumn.setHidden(true);

		ColumnConfig repliesColumn = new ColumnConfig("Replies", "replycount", 70);
		repliesColumn.setAlign(TextAlign.RIGHT);

		ColumnConfig lastPostColumn = new ColumnConfig("Last Post", "lastPost", 150, true, renderLast, "last");

		ColumnModel columnModel = new ColumnModel(new ColumnConfig[] { topicColumn, authorColumn, repliesColumn, lastPostColumn });
		columnModel.setDefaultSortable(true);

		Panel gridPanel = new Panel();
		gridPanel.setLayout(new FitLayout());
		grid = new GridPanel();
		grid.setWidth(600);
		grid.setHeight(500);
		grid.setTitle("Remote Paging Grid ");
		grid.setStore(store);
		grid.setColumnModel(columnModel);
		grid.setTrackMouseOver(false);
		grid.setLoadMask(true);
		grid.setSelectionModel(new RowSelectionModel());
		grid.setFrame(true);
		grid.setStripeRows(true);
		grid.setIconCls("grid-icon");

		GridView view = new GridView() {
			public String getRowClass(Record record, int index, RowParams rowParams, Store store) {
				if (showPreview) {
					rowParams.setBody(Format.format("<p>{0}</p>", record.getAsString("excerpt")));
					return "x-grid3-row-expanded";
				} else {
					return "x-grid3-row-collapsed";
				}
			}
		};
		view.setForceFit(true);
		view.setEnableRowBody(true);
		grid.setView(view);

		PagingToolbar pagingToolbar = new PagingToolbar(store);
		pagingToolbar.setPageSize(25);
		pagingToolbar.setDisplayInfo(true);
		pagingToolbar.setDisplayMsg("Displaying topics {0} - {1} of {2}");
		pagingToolbar.setEmptyMsg("No topics to display");

		pagingToolbar.addSeparator();
		ToolbarButton toolbarButton = new ToolbarButton("Show Preview");
		toolbarButton.setPressed(showPreview);
		toolbarButton.setEnableToggle(true);
		toolbarButton.setCls("");
		toolbarButton.addListener(new ButtonListenerAdapter() {
			public void onToggle(Button button, boolean pressed) {
				toggleDetails(pressed);
			}
		});

		pagingToolbar.addButton(toolbarButton);
		grid.setBottomToolbar(pagingToolbar);

		grid.addListener(new PanelListenerAdapter() {
			public void onRender(Component component) {
				store.load(0, 25);
			}
		});
		gridPanel.add(grid);
		
		FieldSet fieldSet = new FieldSet();
		fieldSet.setLabelWidth(90);
		fieldSet.setTitle("Company Details");
		fieldSet.setAutoHeight(true);
		fieldSet.setBorder(false);
		// the field names msut match the data field values from the Store
		fieldSet.add(new TextField("Title", "title", 120));
		fieldSet.add(new TextField("Author", "author", 120));
		
		final PaddedPanel fieldSetPanel = new PaddedPanel(fieldSet, 10, 0, 0, 0);
		fieldSetPanel.hide();
		
		final RowSelectionModel sm = new RowSelectionModel(true);
		sm.addListener(new RowSelectionListenerAdapter() {
			public void onRowSelect(RowSelectionModel sm, int rowIndex, Record record) {
				formPanel.getForm().loadRecord(record);
				fieldSetPanel.show();
			}
		});
		grid.setSelectionModel(sm);
		
		bindPanel.add(gridPanel, new RowLayoutData(500));  
		bindPanel.add(fieldSetPanel, new RowLayoutData(200));
		
		formPanel.add(bindPanel);
		
		final FormPanel searchFormPanel = new FormPanel();
		searchFormPanel.setFrame(true);
		searchFormPanel.setLabelAlign(Position.LEFT);
		searchFormPanel.setWidth(250);
		searchFormPanel.setPaddings(3);
		
		FieldSet searchFieldSet = new FieldSet();
		searchFieldSet.setLabelWidth(90);
		searchFieldSet.setTitle("Search");
		searchFieldSet.setAutoHeight(true);
		searchFieldSet.setBorder(false);
		// the field names msut match the data field values from the Store
		searchFieldSet.add(new TextField("Title", "title", 120));
		searchFieldSet.add(new TextField("Author", "author", 120));
		
		PaddedPanel searchPanel = new PaddedPanel(searchFieldSet, 0);
		searchPanel.setFrame(true);
		Button search = new Button("Search");
		search.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				String[] props = searchFormPanel.getForm().getValues().split("&");
				UrlParam[] params = new UrlParam[props.length];
				for (int i = 0; i < props.length; i++) {
					String[] nameValue = props[i].split("=");
					params[i] = new UrlParam(nameValue[0], nameValue[1]);
				}
				store.setBaseParams(params);
				store.load(0, 25);
			}
		});
		searchPanel.addButton(search);
		
		searchFormPanel.add(searchPanel);
		
		mainPanel.add(formPanel, new ColumnLayoutData(0.74));
		mainPanel.add(searchFormPanel, new ColumnLayoutData(0.25));
		
		panel.add(mainPanel);
	}

	private void toggleDetails(boolean pressed) {
		showPreview = pressed;
		grid.getView().refresh();
	}
}