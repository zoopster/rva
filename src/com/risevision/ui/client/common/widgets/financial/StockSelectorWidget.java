// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.financial;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.ui.client.common.data.FinancialDataController;
import com.risevision.ui.client.common.info.FinancialItemInfo;
import com.risevision.ui.client.common.info.FinancialItemsInfo;
import com.risevision.ui.client.common.info.ScrollingGridInfo;
import com.risevision.ui.client.common.widgets.ActionsWidget;
import com.risevision.ui.client.common.widgets.StatusBoxWidget;
import com.risevision.ui.client.common.widgets.grid.ScrollingGridWidget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class StockSelectorWidget extends PopupPanel {
	private static StockSelectorWidget instance;
	//RPC
	private RpcCallBackHandler rpcCallBackHandler = new RpcCallBackHandler();
	//UI pieces
	private VerticalPanel mainPanel = new VerticalPanel();
	private StatusBoxWidget statusBox = new StatusBoxWidget();
	private Label titleLabel = new Label("Select a Stock");
	private ActionsWidget actionsWidget = new ActionsWidget();
	//grid
	private ScrollingGridWidget gr;
	private Command grCommand;
	private Command selectCommand;
	
	private boolean remoteLookup = false;
	
	public StockSelectorWidget() {
		super(true, false); //set auto-hide and modal
		
		mainPanel.add(titleLabel);
		mainPanel.setCellHeight(titleLabel, "20px");
		
		mainPanel.add(statusBox);
		mainPanel.setCellHeight(statusBox, "1px");

		initGridWidget();
		mainPanel.add(gr);
		mainPanel.add(actionsWidget);

		add(mainPanel);

		styleControls();
		initActions();	
		
		loadLocalLookupDataRPC();
	}
	
	private void styleControls() {
		setPixelSize(600, 400);
		
		titleLabel.setStyleName("rdn-Head");
		
		this.getElement().getStyle().setProperty("padding", "10px");
		getElement().getStyle().setZIndex(1001);

		actionsWidget.addStyleName("rdn-VerticalSpacer");
	
	}
	
	private void initActions() {	
		Command closeCommand = new Command() {
			@Override
			public void execute() {
				hide();
			}
		};
		
		actionsWidget.addAction("Cancel", closeCommand);

	}

	private void initGridWidget(){
		grCommand = new Command() {
			public void execute() {			
				processGridCommand();
			}
		};			
		
		String[][] headerDefinition = new String[][] {
				{"", null, "100%"},
				};

		FinancialItemsInfo financialItems = new FinancialItemsInfo();
		financialItems.setSortByDefault(FinancialItemInfo.NAME_ATTRIBUTE);
		financialItems.setSortDirection(ScrollingGridInfo.SORT_DOWN);
		
		gr = new ScrollingGridWidget(grCommand, financialItems, headerDefinition);
		
		gr.setGridHeight("400px");
	}	
	
	public static StockSelectorWidget getInstance() {
		try {
			if (instance == null)
				instance = new StockSelectorWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}	

	@Override
	public boolean onKeyDownPreview(char key, int modifiers) {
		// Use the popup's key preview hooks to close the dialog when either
		// enter or escape is pressed.
		switch (key) {
			case KeyCodes.KEY_ESCAPE:
				hide();
				break;
			}

		return true;
	}
	
//	public void load() {
//		gr.clear();
//		
//		loadGridDataRPC();
//	}
	
	public void show(Command selectCommand){
		this.selectCommand = selectCommand;
				
		statusBox.clear();
		
		gr.clearSearch();
		
		super.show();
		center();
	}
	
	public FinancialItemInfo getItem() {
		if (!RiseUtils.strIsNullOrEmpty(gr.getCurrentKey())) {
			FinancialItemsInfo financialItems = getFinancialItemsInfo();
			
			if (financialItems.getItems() != null) {
				for (FinancialItemInfo item: financialItems.getItems()) {
					if (item.getCode().equals(gr.getCurrentKey())) {
						return item;
					}
				}
			}
		}
		
		return null;
	}
	
	private void bindData(FinancialItemsInfo financialItems) {
		if (financialItems.getItems().size() == 0 && !remoteLookup) {
			remoteLookup = true;
			
			loadRemoteLookupDataRPC();
		}
		else {
			remoteLookup = false;
			
			gr.loadGrid(financialItems);
			updateTable(financialItems.getItems());
			
			if (isShowing()) {
				center();
			}
		}
	}
	
	private void loadLocalLookupDataRPC() {
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, "Data is loading...");
		
		FinancialDataController dataController = FinancialDataController.getInstance();
		
		dataController.getLocalLookup(getFinancialItemsInfo(), rpcCallBackHandler);
	}
	
	private void loadRemoteLookupDataRPC() {
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, "Data is loading from remote server...");
		
		FinancialDataController dataController = FinancialDataController.getInstance();
		
		dataController.getRemoteLookup(getFinancialItemsInfo(), rpcCallBackHandler);
	}

	private void processGridCommand(){
		int command = gr.getCurrentCommand();
					
		switch (command) {
		case ScrollingGridInfo.SELECTACTION:
			if (selectCommand != null) {
				selectCommand.execute();
			}
			hide();
			break;
		case ScrollingGridInfo.SEARCHACTION:
//		case ScrollingGridInfo.SORTACTION:
		case ScrollingGridInfo.PAGEACTION:
			loadLocalLookupDataRPC();			
			break;
		default:
			break;
		}
	}

	private void updateTable(ArrayList<FinancialItemInfo> items) {
		if (getFinancialItemsInfo().getItems() != null) {
			int i = 0;
			if (gr.getCurrentCommand() != ScrollingGridInfo.PAGEACTION) {
				gr.clearGrid();
			}
			else {
				i = gr.getRowCount();
			}

			for ( ; i < items.size(); i++) {
				updateTableRow((FinancialItemInfo) items.get(i), i);
			}
		}
	}

	private void updateTableRow(final FinancialItemInfo item, int row) {
		int col = 0;
//		gr.setAction(row, col++, "Select", mediaItem.getKey());
		gr.setAction(row, col++, RiseUtils.strIsNullOrEmpty(item.getName()) ? item.getCode() : item.getName() + " - " + item.getCode(), item.getCode());
	}
	
	private FinancialItemsInfo getFinancialItemsInfo(){
		FinancialItemsInfo gridInfo = (FinancialItemsInfo) gr.getGridInfo();
		
		return gridInfo;
	}

	class RpcCallBackHandler implements AsyncCallback<FinancialItemsInfo> {
		public void onFailure(Throwable caught) {
			remoteLookup = false;

			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}
		
		public void onSuccess(FinancialItemsInfo result) {	
			statusBox.clear();

			bindData(result);			
		}
	}

}