// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.company;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.core.api.attributes.CompanyAttribute;
import com.risevision.ui.client.common.controller.UserAccountController;
import com.risevision.ui.client.common.directory.CompanyDataController;
import com.risevision.ui.client.common.exception.RiseAsyncCallback;
import com.risevision.ui.client.common.info.CompaniesInfo;
import com.risevision.ui.client.common.info.CompanyInfo;
import com.risevision.ui.client.common.info.ScrollingGridInfo;
import com.risevision.ui.client.common.lists.SearchSortable;
import com.risevision.ui.client.common.widgets.ActionsWidget;
import com.risevision.ui.client.common.widgets.StatusBoxWidget;
import com.risevision.ui.client.common.widgets.grid.ScrollingGridWidget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
@Deprecated
public class OperatorSelectWidget extends PopupPanel {
	private static OperatorSelectWidget instance;
	//UI pieces
	private VerticalPanel mainPanel = new VerticalPanel();
	private StatusBoxWidget statusBox = new StatusBoxWidget();
	private Label titleLabel = new Label("Select Company");
	private ActionsWidget actionsWidget = new ActionsWidget();
	
	private CompanyInfo companyInfo;
	
	//RPC
//	private final CompanyServiceAsync companyService = GWT.create(CompanyService.class);
	private RpcCallBackHandler rpcPnoCallBackHandler = new RpcCallBackHandler();
	
	//grid
	private ScrollingGridWidget gr;
	private Command grCommand;
	private Command selectCommand;
	
	public OperatorSelectWidget() {
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
	}
	
	private void styleControls() {
		setPixelSize(600, 400);
		
		titleLabel.setStyleName("rdn-Head");
		
		this.getElement().getStyle().setProperty("padding", "10px");

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
				{"", null, "50px"},
				{"Company Name", CompanyAttribute.NAME, "100%"},
				{"Address", CompanyAttribute.STREET, "250px"}
				};

		CompaniesInfo companiesInfo = new CompaniesInfo();
		companiesInfo.setSortByDefault(CompanyAttribute.NAME);
		companiesInfo.setSortDirection(ScrollingGridInfo.SORT_DOWN);
		gr = new ScrollingGridWidget(grCommand, companiesInfo, headerDefinition);
		
		gr.setGridHeight("400px");
	}	
	
	public static OperatorSelectWidget getInstance() {
		try {
			if (instance == null)
				instance = new OperatorSelectWidget();
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
	
	public void init(CompanyInfo companyInfo, Command selectCommand) {
		this.selectCommand = selectCommand;

		gr.clear();
//		gr.setCurrentKey(companyInfo.getParentId());
		
		this.companyInfo = companyInfo;
		
		statusBox.clear();
		loadGridDataRPC();
	}

	public void show(){
//		statusBox.clear();

		super.show();
		center();
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
		case ScrollingGridInfo.SORTACTION:
		case ScrollingGridInfo.PAGEACTION:
//			performSearchSort();
			loadGridDataRPC();			
			break;
		default:
			break;
		}
	}
	
//	private void performSearchSort() {
//		CompaniesInfo companies = getCompaniesInfo();
//		ArrayList<? extends SearchSortable> items = companies.getCompanies();
//		String searchFor = companies.getSearchFor() != null ? companies.getSearchFor() : "";
//		
//		items = SearchSortController.search(items, searchFor);
//		items = SearchSortController.sort(items, companies.getSortBy(), companies.getSortDirection().equals(ScrollingGridInfo.SORT_UP));
//		
//		updateTable(items);
//	}
	
	private void updateTable(ArrayList<? extends SearchSortable> items) {
		if (getCompaniesInfo().getCompanies() != null) {
			int i = 0;
			if (gr.getCurrentCommand() != ScrollingGridInfo.PAGEACTION) {
				gr.clearGrid();
			}
			else {
				i = gr.getRowCount();
			}

			for ( ; i < items.size(); i++) {
				updateTableRow((CompanyInfo) items.get(i), i);
			}
		}
	}

	private void updateTableRow(final CompanyInfo company, int row) {
		gr.setAction(row, 0, "Select", company.getId());
		gr.setText(row, 1, company.getName());
		gr.setText(row, 2, company.getAddress());
	}
	
	public CompanyInfo getCurrentCompany() {
		if (!RiseUtils.strIsNullOrEmpty(gr.getCurrentKey())) {
			for (CompanyInfo company: getCompaniesInfo().getCompanies()) {
				if (company.getId().equals(gr.getCurrentKey())) {
					return company;
				}
			}
		}
		
		return null;
	}
	
	private void loadGridDataRPC() {
//		String userCompanyId = null; 
		String excludeCompanyId = null;
		CompaniesInfo companiesInfo = getCompaniesInfo();
		
		if (UserAccountController.getInstance().getUserInfo() != null) {
			companiesInfo.setCompanyId(UserAccountController.getInstance().getUserInfo().getCompany());
		}
		if (companyInfo.getId() != null) {
			excludeCompanyId = companyInfo.getId();
		}
		
		CompanyDataController controller = CompanyDataController.getInstance();
		
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, "PNO list is loading...");
		
		controller.getNetworkOperators(excludeCompanyId, companiesInfo, rpcPnoCallBackHandler);
//		companyService.getPnoCompanies(userCompanyId, excludeCompanyId, rpcPnoCallBackHandler);
	}
	
	private CompaniesInfo getCompaniesInfo(){
		CompaniesInfo gridInfo = (CompaniesInfo) gr.getGridInfo();
		
//		if (gridInfo == null){
//			gridInfo = new CompaniesInfo(SelectedCompanyController.getInstance().getUserCompanyId(),0,CompanyAttribute.NAME);
//		}
		
		return gridInfo;
	}

	class RpcCallBackHandler extends RiseAsyncCallback<CompaniesInfo> {
		public void onFailure() {
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}
		
		public void onSuccess(CompaniesInfo result) {
			if (result == null || result.getCompanies() == null) {
				statusBox.setStatus(StatusBoxWidget.Status.ERROR, "Error retrieving PNO list.");
			} else {
				statusBox.clear();
				gr.loadGrid(result);
				updateTable(result.getCompanies());
				
				// Call select command to update the company name
//				if (selectCommand != null) {
//					selectCommand.execute();
//				}
			}
		}
	}

}