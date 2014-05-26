// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.gadget;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.risevision.core.api.attributes.GadgetAttribute;
import com.risevision.ui.client.common.controller.SelectedCompanyController;
import com.risevision.ui.client.common.data.GadgetDataController;
import com.risevision.ui.client.common.exception.RiseAsyncCallback;
import com.risevision.ui.client.common.info.FormValidatorInfo;
import com.risevision.ui.client.common.info.GadgetInfo;
import com.risevision.ui.client.common.info.GadgetsInfo;
import com.risevision.ui.client.common.info.ScrollingGridInfo;
import com.risevision.ui.client.common.widgets.ActionsWidget;
import com.risevision.ui.client.common.widgets.FormValidatorWidget;
import com.risevision.ui.client.common.widgets.StatusBoxWidget;
import com.risevision.ui.client.common.widgets.grid.FormGridWidget;
import com.risevision.ui.client.common.widgets.grid.ScrollingGridWidget;

public class GadgetSelectWidget extends Composite {
	private String classId = "itemTypeSelect_" + System.currentTimeMillis();

	//private static GadgetSelectWidget instance;
	//RPC
//	private final GadgetServiceAsync rpc = GWT.create(GadgetService.class);
	private RpcCompanyCallBackHandler rpcCompanyCallBackHandler = new RpcCompanyCallBackHandler();
	private RpcSharedCallBackHandler rpcSharedCallBackHandler = new RpcSharedCallBackHandler();
	//UI pieces
	private VerticalPanel mainPanel = new VerticalPanel();
	private FormValidatorWidget formValidator = new FormValidatorWidget();
	private DeckPanel contentPanel = new DeckPanel();
	private StatusBoxWidget statusBox = new StatusBoxWidget();
	private HorizontalPanel typePanel = new HorizontalPanel();
	private RadioButton companySelection = new RadioButton(classId, "Your Gadgets");
	private RadioButton sharedSelection = new RadioButton(classId, "Shared Gadgets");
	private RadioButton urlSelection = new RadioButton(classId, "By URL");
	private ActionsWidget actionWidget;

	private GadgetTypeListBox gadgetType = new GadgetTypeListBox();
	private TextBox urlTextBox = new TextBox();
	//grid
	private ScrollingGridWidget companyGadgetGrid;
	private ScrollingGridWidget sharedGadgetGrid;
	private Command grCommand;
	private Command selectCommand;

	public GadgetSelectWidget(ActionsWidget actionWidget) {
		this.actionWidget = actionWidget;

		initGridWidget();
		
		typePanel.add(sharedSelection);
		typePanel.add(new HTML("&nbsp;&nbsp;"));
		typePanel.add(companySelection);
		typePanel.add(new HTML("&nbsp;&nbsp;"));
		typePanel.add(urlSelection);
		
		contentPanel.add(sharedGadgetGrid);
		contentPanel.add(companyGadgetGrid);

		SimplePanel urlPanel = new SimplePanel();
		FormGridWidget urlGrid = new FormGridWidget(2, 2);
		urlGrid.addStyleName("rdn-VerticalSpacer");

		urlGrid.addRow("Type:", gadgetType);
		urlGrid.addRow("URL:", urlTextBox);

		urlPanel.add(urlGrid);
		urlPanel.setHeight("210px");

		contentPanel.add(urlPanel);
		contentPanel.showWidget(0);
		
		mainPanel.add(statusBox);
		mainPanel.add(typePanel);
		mainPanel.add(formValidator);
		mainPanel.add(contentPanel);
		
//		mainPanel.setCellHeight(contentPanel, "210px");
		
		styleControls();
		initActions();
		initWidget(mainPanel);
	}
	
	private void styleControls() {
		urlTextBox.addStyleName("rdn-TextBoxLong");
		
		gadgetType.addStyleName("rdn-ListBoxMedium");
		
		typePanel.getElement().getStyle().setPaddingBottom(6, Unit.PX);
	}

//	public static GadgetSelectWidget getInstance() {
//		try {
//			if (instance == null)
//				instance = new GadgetSelectWidget();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return instance;
//	}	
	
	private void initActions() {
		ValueChangeHandler<Boolean> radioSelected = new ValueChangeHandler<Boolean>() {
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				formValidator.removeValidationElement(urlTextBox);
				actionWidget.setVisible(false, "Save");
				
				if (event.getSource() == sharedSelection) {
					contentPanel.showWidget(0);
				}
				else if (event.getSource() == companySelection) {
					contentPanel.showWidget(1);
				}
				else if (event.getSource() == urlSelection) {
					formValidator.addValidationElement(urlTextBox, "URL", FormValidatorInfo.requiredFieldValidator);
					actionWidget.setVisible(true, "Save");
					
					contentPanel.showWidget(2);
				}
			}
		};
		
		sharedSelection.addValueChangeHandler(radioSelected);
		companySelection.addValueChangeHandler(radioSelected);
		urlSelection.addValueChangeHandler(radioSelected);
		sharedSelection.setValue(true, true);
	}

	private void initGridWidget(){
		grCommand = new Command() {
			public void execute() {			
				processGridCommand();
			}
		};			
		
		String[][] headerDefinition = new String[][] {
				{"", null, "60px"},
				{"", null, "100%"}
				};

		GadgetsInfo companyGadgetsInfo = new GadgetsInfo();
		GadgetsInfo sharedGadgetsInfo = new GadgetsInfo();
		
		companyGadgetsInfo.setSortByDefault(GadgetAttribute.NAME);
		companyGadgetsInfo.setSortDirection(ScrollingGridInfo.SORT_DOWN);
		
		sharedGadgetsInfo.setSortByDefault(GadgetAttribute.NAME);
		sharedGadgetsInfo.setSortDirection(ScrollingGridInfo.SORT_DOWN);		
		
		companyGadgetGrid = new ScrollingGridWidget(grCommand, companyGadgetsInfo, headerDefinition, true);
		sharedGadgetGrid = new ScrollingGridWidget(grCommand, sharedGadgetsInfo, headerDefinition, true);
		
		companyGadgetGrid.setGridHeight("400px");
		sharedGadgetGrid.setGridHeight("400px");
	}	

	public void init(Command selectCommand){
		this.selectCommand = selectCommand;
	}
	
	public void load() {
		urlTextBox.setText("http://");
		loadGridDataRPC();
		
		companySelection.setValue(false);
		urlSelection.setValue(false);
		sharedSelection.setValue(true, true);
	}

//	protected void onLoad() {
//		super.onLoad();
//		gr.clear();
//		loadGridDataRPC();
//	}
	
//	protected void onUnload() {
//		super.onUnload();
//		
//		actionWidget.setVisible(false, "Okay");
//	}
	
	public void show() {
		actionWidget.setVisible(urlSelection.getValue(), "Save");
		actionWidget.setVisible(true, "Cancel");
	}
	
	private void loadGridDataRPC() {
		loadCompanyGridDataRPC();
		loadSharedGridDataRPC();
				
//		rpc.getGadgets(getGadgetsInfo(), rpcCompanyCallBackHandler);
//		rpc.getSharedGadgets(getSharedGadgetsInfo(), rpcSharedCallBackHandler);
	}
	
	private void loadCompanyGridDataRPC() {
		GadgetDataController controller = GadgetDataController.getInstance();
		
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, "Data is loading...");
		
		controller.getGadgets(getGadgetsInfo(), false, rpcCompanyCallBackHandler);
	}
	
	private void loadSharedGridDataRPC() {
		GadgetDataController controller = GadgetDataController.getInstance();
		
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, "Data is loading...");
		
		controller.getGadgets(getSharedGadgetsInfo(), true, rpcSharedCallBackHandler);
	}

	public GadgetInfo getCurrentGadget(){
		if (urlSelection.getValue()) {
			if (formValidator.validate()) {
				GadgetInfo gadgetInfo = new GadgetInfo();
				gadgetInfo.setName("Custom URL");
				gadgetInfo.setUrl(urlTextBox.getText());
				gadgetInfo.setType(gadgetType.getSelectedValue());
				
				return gadgetInfo;
			}
			else {
				return null;
			}
		}
		else {
			String currentKey;
			GadgetsInfo gadgets;
			if (companySelection.getValue()) {
				currentKey = companyGadgetGrid.getCurrentKey();
				gadgets = (GadgetsInfo) companyGadgetGrid.getGridInfo();
			}
			else {
				currentKey = sharedGadgetGrid.getCurrentKey();
				gadgets = (GadgetsInfo) sharedGadgetGrid.getGridInfo();
			}
	
			if (currentKey != null && !currentKey.isEmpty() && gadgets != null && gadgets.getGadgets() != null) {
				for (GadgetInfo gadget: gadgets.getGadgets()) {
					if (gadget.getId() != null && !gadget.getId().isEmpty() && currentKey.equals(gadget.getId())) {
						return gadget;
					}
				}
			}
			return null;
		}
	}

	private void processGridCommand(){
		int command = companySelection.getValue() ? companyGadgetGrid.getCurrentCommand() : sharedGadgetGrid.getCurrentCommand();
					
		switch (command) {
		case ScrollingGridInfo.SELECTACTION:
			if (selectCommand != null){
				selectCommand.execute();
			}
			break;
		case ScrollingGridInfo.SEARCHACTION:
		case ScrollingGridInfo.PAGEACTION:
		case ScrollingGridInfo.SORTACTION:
			if (companySelection.getValue()) {
				loadCompanyGridDataRPC();
			}
			else {
				loadSharedGridDataRPC();
			}
//			loadGridDataRPC();			
			break;
		default:
			break;
		}
	}

	private void updateCompanyTable(GadgetsInfo gadgetsInfo) {
		if ((gadgetsInfo != null) && (gadgetsInfo.getGadgets() != null)) {
			int i = 0;
			if (companyGadgetGrid.getCurrentCommand() != ScrollingGridInfo.PAGEACTION) {
				companyGadgetGrid.clearGrid();
			}
			else {
				i = companyGadgetGrid.getRowCount();
			}

			for ( ; i < gadgetsInfo.getGadgets().size(); i++) {
				updateCompanyTableRow(gadgetsInfo.getGadgets().get(i), i);
			}
		}
	}

	private void updateCompanyTableRow(final GadgetInfo gadget, int row) {
		companyGadgetGrid.setAction(row, 0, "Select", gadget.getId());
		companyGadgetGrid.setText(row, 1, gadget.getName());
	}
	
	private void updateSharedTable(GadgetsInfo gadgetsInfo) {
		if (gadgetsInfo != null && gadgetsInfo.getGadgets() != null) {
			int i = 0;
			if (sharedGadgetGrid.getCurrentCommand() != ScrollingGridInfo.PAGEACTION) {
				sharedGadgetGrid.clearGrid();
			}
			else {
				i = sharedGadgetGrid.getRowCount();
			}
			
			if (sharedGadgetGrid.getCurrentCommand() == ScrollingGridInfo.SEARCHACTION || gadgetsInfo.getGadgets().size() > 0) {
				for ( ; i < gadgetsInfo.getGadgets().size(); i++) {
					updateSharedTableRow(gadgetsInfo.getGadgets().get(i), i);
				}
			}
			else {
				companySelection.setValue(true, true);
			}
		}
	}

	private void updateSharedTableRow(final GadgetInfo gadget, int row) {
		sharedGadgetGrid.setAction(row, 0, "Select", gadget.getId());
		sharedGadgetGrid.setText(row, 1, gadget.getName());
	}

	private GadgetsInfo getGadgetsInfo(){
		GadgetsInfo gridInfo = (GadgetsInfo) companyGadgetGrid.getGridInfo();
		
		if (gridInfo == null){
			gridInfo = new GadgetsInfo();
		}

		gridInfo.setCompanyId(SelectedCompanyController.getInstance().getSelectedCompanyId());
		
		return gridInfo;
	}
	
	private GadgetsInfo getSharedGadgetsInfo(){
		GadgetsInfo gridInfo = (GadgetsInfo) sharedGadgetGrid.getGridInfo();
		
		if (gridInfo == null){
			gridInfo = new GadgetsInfo();
		}

		gridInfo.setCompanyId(SelectedCompanyController.getInstance().getSelectedCompanyId());
		
		return gridInfo;
	}

	class RpcCompanyCallBackHandler extends RiseAsyncCallback<GadgetsInfo> {

		public void onFailure() {
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(GadgetsInfo result) {
			statusBox.clear();
			companyGadgetGrid.loadGrid(result);
			updateCompanyTable(result);
		}
	}
	
	class RpcSharedCallBackHandler extends RiseAsyncCallback<GadgetsInfo> {

		public void onFailure() {
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(GadgetsInfo result) {
			statusBox.clear();
			sharedGadgetGrid.loadGrid(result);
			updateSharedTable(result);
		}
	}
}