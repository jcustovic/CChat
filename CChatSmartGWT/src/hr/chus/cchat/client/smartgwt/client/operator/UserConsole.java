package hr.chus.cchat.client.smartgwt.client.operator;

import java.util.Date;

import hr.chus.cchat.client.smartgwt.client.common.Constants;
import hr.chus.cchat.client.smartgwt.client.common.PanelFactory;
import hr.chus.cchat.client.smartgwt.client.i18n.DictionaryInstance;
import hr.chus.cchat.client.smartgwt.client.operator.ds.ConversationDS;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * 
 * @author Jan Čustović
 *
 */
public class UserConsole extends HLayout {
	
	private static final String DESCRIPTION = "UserConsole";
	
	private String userId;
	private String userType;
	private String nameToDisplay;
	private ListGrid conversationListGrid;
	private int offset = 0;
    private int fetchSize = 50;
    private IButton nextButton;
    private IButton previousButton;
    private Label listLabel;
    private Criteria criteria;

	public static class Factory implements PanelFactory {
        
		private String id;
		private String userType;
		private String nameToDisplay;

        public Factory(String userId, String userType, String nameToDisplay) {
			this.id = userId;
			this.userType = userType;
			this.nameToDisplay = nameToDisplay;
		}

		public Canvas create() {
			UserConsole panel = new UserConsole();
			panel.setUserId(id);
			panel.setUserType(userType);
			panel.setNameToDisplay(nameToDisplay);
            panel.setMargin(5);
            panel.setWidth100();
            panel.setHeight100();
            panel.addMember(panel.getViewPanel());
            return panel;
        }

        public String getID() {
            return id;
        }

        public String getDescription() {
            return DESCRIPTION;
        }
        
	}

	public Canvas getViewPanel() {
		Canvas canvas = new Canvas();
		VLayout vlayout = new VLayout(20);
		
		VLayout conversationLayout = new VLayout(8);
		HLayout conversationUserInfoLayout = new HLayout(20);
		
		listLabel = new Label();
		listLabel.setVisible(false);
		listLabel.setWidth(600);
		listLabel.setHeight("10px");
		
		conversationListGrid = new ListGrid() {
			@Override
			protected String getBaseStyle(ListGridRecord record, int rowNum, int colNum) {
				String direction = record.getAttribute("direction");
				if (direction != null) {
					if (direction.equals("IN")) return "inboundMsgGridRecord";
					else if (direction.equals("OUT")) return "outboundMsgGridRecord";
					else return super.getBaseStyle(record, rowNum, colNum);
				} else {
					return super.getBaseStyle(record, rowNum, colNum);
				}
			}
		};
		conversationListGrid.setLoadingDataMessage(DictionaryInstance.dictionary.loading());
		conversationListGrid.setEmptyMessage(DictionaryInstance.dictionary.emptySet());
		conversationListGrid.setLoadingMessage(DictionaryInstance.dictionary.loading());
		conversationListGrid.setDataSource(ConversationDS.getInstance());
		conversationListGrid.setAutoFetchData(false);
		conversationListGrid.setWidth(620);
		conversationListGrid.setHeight(300);
		conversationListGrid.setUseAllDataSourceFields(false);
		conversationListGrid.setFields(getConversationFields());
		conversationListGrid.setFixedRecordHeights(false);
		
		nextButton = new IButton(DictionaryInstance.dictionary.next());
        nextButton.setIcon(Constants.CONTEXT_PATH + "images/arrow_right_green.png");
        previousButton = new IButton(DictionaryInstance.dictionary.previous());
        previousButton.setIcon(Constants.CONTEXT_PATH + "images/arrow_left_green.png");
        
        nextButton.setVisible(false);
        nextButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				offset += fetchSize;
				criteria.setAttribute("start", offset);
				conversationListGrid.invalidateCache();
				conversationListGrid.fetchData(criteria, new DSCallback() {
					
					@Override
					public void execute(DSResponse response, Object rawData, DSRequest request) {
						int totalCount = (int) ConversationDS.getInstance().getTotalCount();
						listLabel.setContents(DictionaryInstance.dictionary.totalUsersInDB() + ": <b>" + totalCount + "&nbsp;&nbsp;(" + offset + " - " + (offset + fetchSize) + ")</b>");
						listLabel.setVisible(true);
						if ((offset + fetchSize) >= totalCount) nextButton.setVisible(false);
						previousButton.setVisible(true);
					}
				});
			}
        });
        
        previousButton.setVisible(false);
        previousButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				offset -= fetchSize;
				criteria.setAttribute("start", offset);
				conversationListGrid.invalidateCache();
				conversationListGrid.fetchData(criteria, new DSCallback() {
					
					@Override
					public void execute(DSResponse response, Object rawData, DSRequest request) {
						int totalCount = (int) ConversationDS.getInstance().getTotalCount();
						listLabel.setContents(DictionaryInstance.dictionary.totalUsersInDB() + ": <b>" + totalCount + "&nbsp;&nbsp;(" + offset + " - " + (offset + fetchSize) + ")</b>");
						listLabel.setVisible(true);
						if (offset <= 0) previousButton.setVisible(false);
						nextButton.setVisible(true);
					}
				});
			}
        });
        
        HLayout searchPagingLayout = new HLayout(5);
        searchPagingLayout.addMember(previousButton);
        searchPagingLayout.addMember(nextButton);
        
        conversationLayout.addMember(searchPagingLayout);
        
        VLayout listLayout = new VLayout(0);
		listLayout.addMember(listLabel);
		listLayout.addMember(conversationListGrid);
		
        conversationLayout.addMember(listLayout);
        
        
		conversationUserInfoLayout.addMember(conversationLayout);
		
		vlayout.addMember(conversationUserInfoLayout);
		
		HTMLFlow mainHtmlFlow = new HTMLFlow();
		mainHtmlFlow.setOverflow(Overflow.AUTO);
		mainHtmlFlow.setPadding(10);
		mainHtmlFlow.setHeight("50");
		mainHtmlFlow.setContents("<b> " + userId + " </b>");
		
		vlayout.addMember(mainHtmlFlow);
		
		canvas.addChild(vlayout);
		return canvas;
    }
	
	private ListGridField[] getConversationFields() {
		ListGridField text = new ListGridField("text");
		text.setAlign(Alignment.CENTER);
    	text.setWidth(380);
    	
    	ListGridField operator = new ListGridField("operator");
    	operator.setAlign(Alignment.CENTER);
    	operator.setWidth(100);
    	
		final DateTimeFormat dateTime = DateTimeFormat.getFormat("dd.MM.yyyy hh:mm:ss");
		ListGridField time = new ListGridField("time");
		time.setAlign(Alignment.LEFT);
		time.setCellFormatter(new CellFormatter() {
            public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
                if (value != null) {
                    try {
                        Date dateValue = (Date) value;
                        return dateTime.format(dateValue);
                    } catch (Exception e) {
                        return value.toString();
                    }
                } else {
                    return "";
                }
            }
        });
		time.setWidth(120);
		
		return new ListGridField[] { time, operator, text };
	}

	public void loadConversation(boolean setMessagesAsRead) {
		criteria = new Criteria("userId", userId);
		criteria.setAttribute("limit", fetchSize);
		criteria.setAttribute("start", 0);
		criteria.setAttribute("setMessagesAsRead", setMessagesAsRead);
		if (!conversationListGrid.willFetchData(criteria)) conversationListGrid.invalidateCache();
		conversationListGrid.fetchData(criteria, new DSCallback() {
			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				int totalCount = (int) ConversationDS.getInstance().getTotalCount();
				listLabel.setContents(DictionaryInstance.dictionary.totalUsersInDB() + ": <b>" + totalCount + "&nbsp;&nbsp;(" + offset + " - " + (offset + fetchSize) + ")</b>");
				listLabel.setVisible(true);
				if (totalCount > fetchSize) {
					nextButton.setVisible(true);
				} else {
					nextButton.setVisible(false);
				}
				previousButton.setVisible(false);
			}
		});
	}

	
	// Getters & setters
	
	public String getUserId() { return userId; }
	public void setUserId(String userId) { this.userId = userId; }

	public String getUserType() { return userType; }
	public void setUserType(String userType) { this.userType = userType; }

	public String getNameToDisplay() { return nameToDisplay; }
	public void setNameToDisplay(String nameToDisplay) { this.nameToDisplay = nameToDisplay; }
		
}
