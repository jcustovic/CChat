package hr.chus.client.smartgwt.client.admin;

import java.util.Date;
import java.util.Iterator;

import hr.chus.client.smartgwt.client.CChatAdminSmartGWT;
import hr.chus.client.smartgwt.client.PanelFactory;
import hr.chus.client.smartgwt.client.admin.ds.NicksDS;
import hr.chus.client.smartgwt.client.admin.ds.OperatorsDS;
import hr.chus.client.smartgwt.client.admin.ds.ServiceProviderDS;
import hr.chus.client.smartgwt.client.admin.ds.UsersDS;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.XMLTools;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemInputTransformer;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.DateTimeItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class Users extends HLayout {
	
    private static final String DESCRIPTION = "Users";
    
    private HLayout rollOverCanvas;
    private ListGridRecord rollOverRecord;
    
    public static class Factory implements PanelFactory {
        
    	private String id;

        public Canvas create() {
        	Users panel = new Users();
            panel.setMargin(5);
            panel.setWidth100();
            panel.setHeight100();
            panel.addMember(panel.getViewPanel());
            id = panel.getID();
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

    	NicksDS.getInstance().invalidateCache();
    	OperatorsDS.getInstance().invalidateCache();
    	ServiceProviderDS.getInstance().invalidateCache();
    	
		VLayout layout = new VLayout(20);
		layout.setWidth(850);
//		layout.setHeight100();
		
		Label label = new Label();
		label.setHeight(10);
//		label.setWidth("70%");
		label.setContents(CChatAdminSmartGWT.dictionary.users());
		layout.addMember(label);
		
		final DynamicForm form = new DynamicForm();
		form.setVisible(false);
//		form.setWidth("60%");
		form.setIsGroup(true);
		form.setGroupTitle(CChatAdminSmartGWT.dictionary.update());
		form.setNumCols(4);
		
		DataSource ds = new DataSource(CChatAdminSmartGWT.CONTEXT_PATH + "admin/AdminUserFunctionJSON") {
			@Override
			protected void transformResponse(DSResponse response, DSRequest request, Object jsonData) {
				JSONArray value = XMLTools.selectObjects(jsonData, "/status");
				String status = ((JSONString)value.get(0)).stringValue();
				if (!status.equals("validation_ok")) {
					response.setStatus(RPCResponse.STATUS_VALIDATION_ERROR);
					JSONArray errors = XMLTools.selectObjects(jsonData, "/errorFields");
					response.setErrors(errors.getJavaScriptObject());
				}
			}
		};
		ds.setDataFormat(DSDataFormat.JSON);
		form.setDataSource(ds);
		
		form.setFields(getFormFields());
		
		final IButton updateButton = new IButton(CChatAdminSmartGWT.dictionary.update());
		updateButton.setVisible(false);
		
		final Label listLabel = new Label();
		listLabel.setVisible(false);
		listLabel.setHeight("10px");

		final ListGrid listGrid = new ListGrid() {
			
			@Override
			protected Canvas getRollOverCanvas(Integer rowNum, Integer colNum) {
				rollOverRecord = this.getRecord(rowNum);

	            if (rollOverCanvas == null) {
	                rollOverCanvas = new HLayout(3);
	                rollOverCanvas.setSnapTo("TR");
	                rollOverCanvas.setWidth(50);
	                rollOverCanvas.setHeight(22);
	
	                ImgButton textImg = new ImgButton();
	                textImg.setShowDown(false);
	                textImg.setShowRollOver(false);
	                textImg.setLayoutAlign(Alignment.CENTER);
	                textImg.setSrc(CChatAdminSmartGWT.CONTEXT_PATH + "images/message.png");
	                textImg.setPrompt(CChatAdminSmartGWT.dictionary.sendMessage());
	                textImg.setHeight(16);
	                textImg.setWidth(16);
	                textImg.addClickHandler(new ClickHandler() {
	                    public void onClick(ClickEvent event) {
	                        SC.say("Send message to : " + rollOverRecord.getAttribute("user.msisdn"));
	                    }
	                });
	
	                rollOverCanvas.addMember(textImg);
	            }
	            return rollOverCanvas;
	        }
		};
		listGrid.setShowRollOverCanvas(true);;
		listGrid.setLoadingDataMessage(CChatAdminSmartGWT.dictionary.loading());
		listGrid.setEmptyMessage(CChatAdminSmartGWT.dictionary.emptySet());
		listGrid.setLoadingMessage(CChatAdminSmartGWT.dictionary.loading());
		listGrid.setHeight(200);
		listGrid.setDataSource(UsersDS.getInstance());
		listGrid.setAutoFetchData(false);
		listGrid.setAnimateRemoveRecord(true);
		listGrid.addRecordClickHandler(new RecordClickHandler() {

			@Override
			public void onRecordClick(RecordClickEvent event) {
				form.clearValues();
				form.setVisible(true);
				updateButton.setVisible(true);
				
				FormItem[] fields = form.getFields();
				form.editSelectedData(listGrid);
				Iterator<?> keySetIterator = form.getValues().keySet().iterator();
				while (keySetIterator.hasNext()) {
					String key = (String) keySetIterator.next();
					boolean toRemove = true;
					for (FormItem field : fields) {
						if (key.equals(field.getName())) {
							toRemove = false;
							break;
						}
					}
					if (toRemove) {
						form.clearValue(key);
					}
				}
				form.setValue("operation", "update");
			}
		});

		listGrid.setUseAllDataSourceFields(false);
		listGrid.setFields(getGridFields());
		
		final DynamicForm searchForm = new DynamicForm();
//        searchForm.setWidth("60%");
		searchForm.setIsGroup(true);
        searchForm.setGroupTitle(CChatAdminSmartGWT.dictionary.search());
        searchForm.setNumCols(6);
        searchForm.setDataSource(UsersDS.getInstance());
        searchForm.setAutoFocus(false);

        searchForm.setFields(getSearchFormFields());
                
        IButton searchButton = new IButton(CChatAdminSmartGWT.dictionary.search());
        searchButton.setIcon(CChatAdminSmartGWT.CONTEXT_PATH + "images/find.png");
        searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				listGrid.invalidateCache();
				listGrid.fetchData(searchForm.getValuesAsCriteria(), new DSCallback() {
					
					@Override
					public void execute(DSResponse response, Object rawData, DSRequest request) {
						listLabel.setContents("Total user in database: <b>" + UsersDS.getInstance().getTotalCount() + "</b>");
						listLabel.setVisible(true);
					}
				});
			}
		});
        
        updateButton.setIcon(CChatAdminSmartGWT.CONTEXT_PATH + "images/edit.png");
        updateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (form.validate()) {
					form.submit(new DSCallback() {

						@Override
						public void execute(DSResponse response, Object jsonData, DSRequest request) {
							JSONArray value = XMLTools.selectObjects(jsonData, "/status");
							String status = null;
							if (value != null && value.size() > 0) {
								status = ((JSONString) value.get(0)).stringValue();
							}
							if (status == null || !status.equals("validation_error")) {
								listGrid.invalidateCache();
								listGrid.fetchData(searchForm.getValuesAsCriteria());
								form.setVisible(false);
								updateButton.setVisible(false);
							}
						}
						
					});
				}
			}
		});

        VLayout searchLayout = new VLayout(5);
        searchLayout.addMember(searchForm);
        searchLayout.addMember(searchButton);
		layout.addMember(searchLayout);
		
		VLayout listLayout = new VLayout(0);
		listLayout.addMember(listLabel);
		listLayout.addMember(listGrid);
		layout.addMember(listLayout);
		
		layout.addMember(form);
		layout.addMember(updateButton);
		
		canvas.addChild(layout);
        return canvas;
    }

    private FormItem[] getFormFields() {
    	TextItem id = new TextItem("user.id", "ID");
    	id.setDisabled(true);
    	TextItem msisdn = new TextItem("user.msisdn", CChatAdminSmartGWT.dictionary.msisdn());
    	msisdn.setDisabled(true);
        TextItem name = new TextItem("user.name", CChatAdminSmartGWT.dictionary.name());
        TextItem surname = new TextItem("user.surname", CChatAdminSmartGWT.dictionary.surname());
        TextItem address = new TextItem("user.address", CChatAdminSmartGWT.dictionary.address());
        TextAreaItem notes = new TextAreaItem("user.notes", CChatAdminSmartGWT.dictionary.notes());

        SelectItem nickItem = new SelectItem("user.nick", CChatAdminSmartGWT.dictionary.nick());
        nickItem.setAllowEmptyValue(true);
        nickItem.setEmptyDisplayValue(CChatAdminSmartGWT.dictionary.notSet());
        nickItem.setOptionDataSource(NicksDS.getInstance());
        nickItem.setDisplayField("name");
        nickItem.setValueField("id");
        
        SelectItem operatorItem = new SelectItem("user.operator", CChatAdminSmartGWT.dictionary.operator());
        operatorItem.setAllowEmptyValue(true);
        operatorItem.setEmptyDisplayValue(CChatAdminSmartGWT.dictionary.notSet());
        operatorItem.setOptionDataSource(OperatorsDS.getInstance());
        operatorItem.setDisplayField("username");
        operatorItem.setValueField("id");
        
        SelectItem serviceProviderItem = new SelectItem("user.serviceProvider", CChatAdminSmartGWT.dictionary.serviceProvider());
        serviceProviderItem.setAllowEmptyValue(true);
        serviceProviderItem.setEmptyDisplayValue(CChatAdminSmartGWT.dictionary.notSet());
        serviceProviderItem.setOptionDataSource(ServiceProviderDS.getInstance());
        serviceProviderItem.setDisplayField("providerName");
        serviceProviderItem.setValueField("id");
        serviceProviderItem.setDisabled(true);
        
        final DateItem birthdate = new DateItem("user.birthDate", CChatAdminSmartGWT.dictionary.birthDate());
//        birthdate.setAttribute("useCustomTimezone", true);
        birthdate.setInputTransformer(new FormItemInputTransformer() {
			
			@SuppressWarnings("deprecation")
			@Override
			public Object transformInput(DynamicForm form, FormItem item, Object value, Object oldValue) {
				if (value == null || !(value instanceof Date)) return null;
				Date date = (Date) value;
				Date gmt = new Date(date.getTime() - date.getTimezoneOffset() * 60 * 1000);
				birthdate.setValue(gmt);
				return CChatAdminSmartGWT.dateFormat.format(gmt);
			}
		});
        birthdate.setMaskDateSeparator(".");
        birthdate.setUseMask(true);
        birthdate.setUseTextField(true);
        birthdate.setInvalidDateStringMessage(CChatAdminSmartGWT.dictionary.invalidDate());
        birthdate.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATE);
        
        DateTimeItem joined = new DateTimeItem("user.joined", CChatAdminSmartGWT.dictionary.joinedDate());
        joined.setMaskDateSeparator(".");
        joined.setUseMask(true);
        joined.setUseTextField(true);
        joined.setWidth(180);
        joined.setDisabled(true);
        joined.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
        
        return new FormItem[] { id, msisdn, name, surname, address, notes, nickItem, operatorItem, serviceProviderItem , birthdate, joined };
	}

	/**
     * 
     * @return
     */
    private FormItem[] getSearchFormFields() {
        TextItem msisdn = new TextItem("msisdn", CChatAdminSmartGWT.dictionary.msisdn());
        TextItem name = new TextItem("name", CChatAdminSmartGWT.dictionary.name());
        TextItem surname = new TextItem("surname", CChatAdminSmartGWT.dictionary.surname());

        SelectItem nickItem = new SelectItem("nick", CChatAdminSmartGWT.dictionary.nick());
        nickItem.setAllowEmptyValue(true);
        nickItem.setEmptyDisplayValue(CChatAdminSmartGWT.dictionary.notSet());
        nickItem.setOptionDataSource(NicksDS.getInstance());
        nickItem.setDisplayField("name");
        nickItem.setValueField("id");
        
        SelectItem operatorItem = new SelectItem("operator", CChatAdminSmartGWT.dictionary.operator());
        operatorItem.setAllowEmptyValue(true);
        operatorItem.setEmptyDisplayValue(CChatAdminSmartGWT.dictionary.notSet());
        operatorItem.setOptionDataSource(OperatorsDS.getInstance());
        operatorItem.setDisplayField("username");
        operatorItem.setValueField("id");
        
        SelectItem serviceProviderItem = new SelectItem("serviceProvider", CChatAdminSmartGWT.dictionary.serviceProvider());
        serviceProviderItem.setAllowEmptyValue(true);
        serviceProviderItem.setEmptyDisplayValue(CChatAdminSmartGWT.dictionary.notSet());
        serviceProviderItem.setOptionDataSource(ServiceProviderDS.getInstance());
        serviceProviderItem.setDisplayField("providerName");
        serviceProviderItem.setValueField("id");
        
        SelectItem fetchSize = new SelectItem("limit", CChatAdminSmartGWT.dictionary.fetchSize());
        fetchSize.setValueMap("20", "50", "100", "200");
        fetchSize.setDefaultToFirstOption(true);
        
        return new FormItem[] { msisdn, name, surname, nickItem, operatorItem, serviceProviderItem, fetchSize };
	}

	/**
     * 
     * @return
     */
    private ListGridField[] getGridFields() {
    	ListGridField msisdn = new ListGridField("user.msisdn");
    	msisdn.setWidth(120);
		ListGridField name = new ListGridField("user.name");
		name.setWidth(100);
		ListGridField surname = new ListGridField("user.surname");
		surname.setWidth(140);
		ListGridField address = new ListGridField("user.address");
		address.setHidden(true);
		ListGridField notes = new ListGridField("user.notes");
		notes.setHidden(true);
		ListGridField nickName = new ListGridField("nick.name");
		nickName.setWidth(100);
		ListGridField operatorUsername = new ListGridField("operator.username");
		operatorUsername.setWidth(100);
		ListGridField nick = new ListGridField("user.nick");
		nick.setHidden(true);
		ListGridField operator = new ListGridField("user.operator");
		operator.setHidden(true);
		ListGridField serviceProviderName = new ListGridField("serviceProvider.providerName");
		serviceProviderName.setWidth(120);
		
		final DateTimeFormat joinedDateFormatter = DateTimeFormat.getFormat("dd.MM.yyyy");
		ListGridField joined = new ListGridField("user.joined");
		joined.setCellFormatter(new CellFormatter() {
            public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
                if (value != null) {
                    try {
                        Date dateValue = (Date) value;
                        return joinedDateFormatter.format(dateValue);
                    } catch (Exception e) {
                        return value.toString();
                    }
                } else {
                    return "";
                }
            }
        });
		joined.setAlign(Alignment.LEFT);
		joined.setType(ListGridFieldType.DATE);
		joined.setWidth(140);
		
		final DateTimeFormat birthDateFormatter = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm");
		ListGridField birthDate = new ListGridField("user.birthDate");
		birthDate.setCellFormatter(new CellFormatter() {
            public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
                if (value != null) {
                    try {
                        Date dateValue = (Date) value;
                        return birthDateFormatter.format(dateValue);
                    } catch (Exception e) {
                        return value.toString();
                    }
                } else {
                    return "";
                }
            }
        });
		birthDate.setType(ListGridFieldType.DATE);
		birthDate.setHidden(true);
		
		return new ListGridField[] { msisdn, name, surname, address, notes, nickName, operator, operatorUsername, serviceProviderName, nick, joined, birthDate };
	}
}
