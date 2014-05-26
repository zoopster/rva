// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.company;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.core.api.attributes.CompanyAttribute;
import com.risevision.ui.client.common.controller.PrerequisitesController;
import com.risevision.ui.client.common.controller.SelectedCompanyController;
import com.risevision.ui.client.common.directory.CompanyDataController;
import com.risevision.ui.client.common.exception.RiseAsyncCallback;
import com.risevision.ui.client.common.info.CompaniesInfo;
import com.risevision.ui.client.common.info.CompanyInfo;
import com.risevision.ui.client.common.info.ScrollingGridInfo;
import com.risevision.ui.client.common.widgets.ActionsWidget;
import com.risevision.ui.client.common.widgets.StatusBoxWidget;
import com.risevision.ui.client.common.widgets.grid.ScrollingGridWidget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class CompaniesWidget extends PopupPanel {

	private static CompaniesWidget instance;
	//RPC
//	private final CompanyServiceAsync companyService = GWT.create(CompanyService.class);
	private RpcCallBackHandler rpcCallBackHandler = new RpcCallBackHandler();
	//UI pieces
	private VerticalPanel mainPanel = new VerticalPanel();
	private StatusBoxWidget statusBox = new StatusBoxWidget();
	private Label lbTitle = new Label("Select Company");
	private ActionsWidget actionsWidget = new ActionsWidget();
	//grid
	private ScrollingGridWidget gr;
	private Command grCommand;

	public CompaniesWidget() {
		super(true, false); //set auto-hide and modal
		
		mainPanel.add(lbTitle);
		mainPanel.setCellHeight(lbTitle, "20px");
		
		mainPanel.add(statusBox);
		mainPanel.setCellHeight(statusBox, "1px");

		initGridWidget();
		mainPanel.add(gr);
//		mainPanel.setCellHeight(gr, "340px");
		mainPanel.add(actionsWidget);

		add(mainPanel);
		
		initActions();
		styleControls();
		
	}
	
	private void styleControls() {
		setPixelSize(600, 400);

		lbTitle.setStyleName("rdn-Head");

		actionsWidget.addStyleName("rdn-VerticalSpacer");
		
		//style table
		mainPanel.setSpacing(0);
		
		this.getElement().getStyle().setProperty("padding", "10px");
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
	
	public static CompaniesWidget getInstance() {
		try {
			if (instance == null)
				instance = new CompaniesWidget();
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
	
	protected void onLoad() {
		super.onLoad();
		gr.clear();
		loadGridDataRPC();
	}

	public void show(){
		super.show();
		center();
	}
	
	private void loadGridDataRPC() {
		CompanyDataController controller = CompanyDataController.getInstance();

		statusBox.setStatus(StatusBoxWidget.Status.WARNING, "Data is loading...");
		
		String parentCompanyId;
		CompanyInfo selectedCompany = SelectedCompanyController.getInstance().getSelectedCompany();
		if (selectedCompany.isPno())
			parentCompanyId = selectedCompany.getId();
		else
			parentCompanyId = selectedCompany.getParentId();
		// parentCompanyId = "" for the top level PNO only.
		if (RiseUtils.strIsNullOrEmpty(parentCompanyId))
			parentCompanyId = SelectedCompanyController.getInstance().getUserCompanyId();

		controller.getCompanies(parentCompanyId, getCompaniesInfo(), rpcCallBackHandler);

//		companyService.getCompanies(parentCompanyId, getCompaniesInfo(), rpcCallBackHandler);
	}

	private void processGridCommand(){
		int command = gr.getCurrentCommand();
					
		switch (command) {
		case ScrollingGridInfo.SELECTACTION:
			PrerequisitesController.getInstance().showProgressBar();
			SelectedCompanyController.getInstance().setSelectedCompany(gr.getCurrentKey());
			hide();
			//onCompanyChange.execute();
			break;
		case ScrollingGridInfo.SEARCHACTION:
		case ScrollingGridInfo.PAGEACTION:
		case ScrollingGridInfo.SORTACTION:
			loadGridDataRPC();			
			break;
		default:
			break;
		}
	}

	private void updateTable(CompaniesInfo companiesInfo) {
		if ((companiesInfo != null) && (companiesInfo.getCompanies() != null)) {
			int i = 0;
			if (gr.getCurrentCommand() != ScrollingGridInfo.PAGEACTION) {
				gr.clearGrid();
			}
			else {
				i = gr.getRowCount();
			}

			for ( ; i < companiesInfo.getCompanies().size(); i++) {
				updateTableRow(companiesInfo.getCompanies().get(i), i);
			}
		}
	}

	private void updateTableRow(final CompanyInfo company, int row) {
		gr.setAction(row, 0, "Select", company.getId());
		gr.setText(row, 1, company.getName());
		gr.setText(row, 2, company.getAddress());
	}

	private CompaniesInfo getCompaniesInfo() {
		CompaniesInfo gridInfo = (CompaniesInfo) gr.getGridInfo();
		
		if (gridInfo == null){
			gridInfo = new CompaniesInfo(SelectedCompanyController.getInstance().getUserCompanyId(), CompanyAttribute.NAME);
		}
		
		return gridInfo;
	}

	class RpcCallBackHandler extends RiseAsyncCallback<CompaniesInfo> {
		public void onFailure() {
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}
		
		public void onSuccess(CompaniesInfo result) {	
			statusBox.clear();
			gr.loadGrid(result);
			updateTable(result);
		}
	}
}