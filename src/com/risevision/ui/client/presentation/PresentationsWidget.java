// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.presentation;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HTML;
import com.risevision.common.client.info.PresentationInfo;
import com.risevision.core.api.attributes.CommonAttribute;
import com.risevision.core.api.attributes.PresentationAttribute;
import com.risevision.core.api.types.PresentationRevisionStatus;
import com.risevision.ui.client.UiEntryPoint;
import com.risevision.ui.client.common.ContentId;
import com.risevision.ui.client.common.controller.ConfigurationController;
import com.risevision.ui.client.common.controller.SelectedCompanyController;
import com.risevision.ui.client.common.data.PresentationDataController;
import com.risevision.ui.client.common.exception.RiseAsyncCallback;
import com.risevision.ui.client.common.info.HistoryTokenInfo;
import com.risevision.ui.client.common.info.PresentationsInfo;
import com.risevision.ui.client.common.info.ScrollingGridInfo;
import com.risevision.ui.client.common.widgets.ActionsWidget;
import com.risevision.ui.client.common.widgets.LastModifiedWidget;
import com.risevision.ui.client.common.widgets.StatusBoxWidget;
import com.risevision.ui.client.common.widgets.grid.ScrollingGridWidget;

public class PresentationsWidget extends Composite {
	private static PresentationsWidget instance;
	//RPC
//	private final PresentationServiceAsync presentationService = GWT.create(PresentationService.class);
	private RpcPresentationsCallBackHandler presentationsRPCCallBackHandler = new RpcPresentationsCallBackHandler();
	private RpcTemplatesCallBackHandler templatesRPCCallBackHandler = new RpcTemplatesCallBackHandler();
//	private RpcCopyCallBackHandler rpcCopyCallBackHandler = new RpcCopyCallBackHandler();

	//UI pieces
	private StatusBoxWidget statusBox = StatusBoxWidget.getInstance();
	private ActionsWidget actionsWidget;
//	private PresentationAddWidget addWidget = PresentationAddWidget.getInstance();
	
	private DeckPanel contentPanel = new DeckPanel();
	//grid
	private ScrollingGridWidget presentationsGrid;
	private ScrollingGridWidget templatesGrid;
	
	private boolean disableActions = false;
	
	private Command gridCommand;

	public PresentationsWidget() {
		initGridWidget();

		contentPanel.add(presentationsGrid);
		contentPanel.add(templatesGrid);
		
		initWidget(contentPanel);
	}

	public static PresentationsWidget getInstance() {
		try {
			if (instance == null)
				instance = new PresentationsWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}	

	private void initGridWidget(){
		gridCommand = new Command() {
			public void execute() {			
				processGridCommand();
			}
		};			
		
		String[][] presentationsHeaderDefinition = new String[][] {
				{"Name", PresentationAttribute.NAME, "100%"},
				{"Template", PresentationAttribute.TEMPLATE, "80px"},
				{"State", PresentationAttribute.REVISION_STATUS, "80px"},
				{"Last Modified", CommonAttribute.CHANGE_DATE, "180px"}
				};
		
		String[][] templatesHeaderDefinition = new String[][] {
				{"", null, "80px"},
				{"Name", PresentationAttribute.NAME, "100%"},
				{"", null, "80px"}
				};

		presentationsGrid = new ScrollingGridWidget(gridCommand, new PresentationsInfo(), presentationsHeaderDefinition);
		templatesGrid = new ScrollingGridWidget(gridCommand, new PresentationsInfo(), templatesHeaderDefinition);
	}	

	private void initActions(){
		actionsWidget = ActionsWidget.getInstance();
		Command cmdTemplates = new Command() {
			public void execute() {
				showPanel(1);
			}
		};	

		Command cmdPresentations = new Command() {
			public void execute() {
				showPanel(0);
			}
		};	
		
		Command cmdShowAdd = new Command() {
			public void execute() {
				doActionAdd();
			}
		};		

		actionsWidget.addAction("Add from Template", cmdTemplates);
		actionsWidget.addAction("View Presentations", cmdPresentations);
		actionsWidget.addAction("Add", cmdShowAdd);
	}	
	
	private void doActionAdd() {
		UiEntryPoint.loadContentStatic(ContentId.PRESENTATION_MANAGE);
	}
	
	public void showPanel(int panel) {
		actionsWidget.setVisible(panel == 0, "Add from Template");
		actionsWidget.setVisible(panel == 1, "View Presentations");
		
		contentPanel.showWidget(panel);
	}

	protected void onLoad() {
		super.onLoad();

		initActions();
		showPanel(0);
		presentationsGrid.clear();
		templatesGrid.clear();
		loadGridDataRPC();
//		addWidget.load();
	}

//	private void openAddPanel() {
//		addWidget.show();
//	}
	
	private void loadGridDataRPC() {
		PresentationDataController controller = PresentationDataController.getInstance();
		
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, "Data is loading...");
		controller.getPresentations(getPresentationsInfo(), false, presentationsRPCCallBackHandler);
		controller.getPresentations(getTemplatesInfo(), true, templatesRPCCallBackHandler);
		
//		rpc.getPresentations(getPresentationsInfo(), rpcCallBackHandler);
	}

	private void processGridCommand(){
		if (disableActions) {
			return;
		}
		
		int command = contentPanel.getVisibleWidget() == 0 ? presentationsGrid.getCurrentCommand() : templatesGrid.getCurrentCommand();
					
		switch (command) {
		case ScrollingGridInfo.SELECTACTION:
			if (contentPanel.getVisibleWidget() == 0) {
//				HistoryTokenInfo tokenInfo = new HistoryTokenInfo();
//				tokenInfo.setId(presentationsGrid.getCurrentKey());
//				tokenInfo.setContentId(ContentId.PRESENTATION_MANAGE);
//				
//				UiEntryPoint.loadContentStatic(tokenInfo);
			}
			else {
				String[] params = templatesGrid.getCurrentKey().split(",");

				HistoryTokenInfo tokenInfo = new HistoryTokenInfo();
				tokenInfo.setId(params[0]);
				tokenInfo.setFromCompanyId(params[1]);
				tokenInfo.setContentId(ContentId.PRESENTATION_MANAGE);
				
				UiEntryPoint.loadContentStatic(tokenInfo);
			}
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

	private void updatePresentationsTable(PresentationsInfo presentationsInfo) {
		if ((presentationsInfo != null) && (presentationsInfo.getPresentations() != null)) {
			int i = 0;
			if (presentationsGrid.getCurrentCommand() != ScrollingGridInfo.PAGEACTION) {
				presentationsGrid.clearGrid();
			}
			else {
				i = presentationsGrid.getRowCount();
			}

			for ( ; i < presentationsInfo.getPresentations().size(); i++) {
				updatePresentationsTableRow(presentationsInfo.getPresentations().get(i), i);
			}
		}
	}

	private void updatePresentationsTableRow(final PresentationInfo presentation, int row) {
//		presentationsGrid.setAction(row, 0, presentation.getName(), presentation.getId());
		presentationsGrid.setHyperlink(row, 0, presentation.getName(), ContentId.PRESENTATION_MANAGE, presentation.getId());
		presentationsGrid.setText(row, 1, presentation.isTemplate() ? "Yes" : "No");
		
		HTML revisionStatus = new HTML(presentation.getRevisionStatus() == PresentationRevisionStatus.PUBLISHED ? "Published" : 
					"<span style='color:red;'>Revised</span>");
		presentationsGrid.setWidget(row, 2, revisionStatus);
		presentationsGrid.setText(row, 3, LastModifiedWidget.getLastModified(presentation.getChangedBy(), presentation.getChangeDate()));
	}
	
	private void updateTemplatesTable(PresentationsInfo presentationsInfo) {
		if ((presentationsInfo != null) && (presentationsInfo.getPresentations() != null)) {
			int i = 0;
			if (templatesGrid.getCurrentCommand() != ScrollingGridInfo.PAGEACTION) {
				templatesGrid.clearGrid();
			}
			else {
				i = templatesGrid.getRowCount();
			}

			for ( ; i < presentationsInfo.getPresentations().size(); i++) {
				updateTemplatesTableRow(presentationsInfo.getPresentations().get(i), i);
			}
		}
	}

	private void updateTemplatesTableRow(final PresentationInfo presentation, int row) {
		templatesGrid.setAction(row, 0, "Select", presentation.getId() + "," + presentation.getCompanyId());
		templatesGrid.setText(row, 1, presentation.getName());
		templatesGrid.setWidget(row, 2, new Anchor("Preview", 
				ConfigurationController.getInstance().getConfiguration().getViewerURL() + "Viewer.html?type=presentation&id=" + presentation.getId(), 
				"_blank"));
	}

	private PresentationsInfo getPresentationsInfo(){
		PresentationsInfo gridInfo = (PresentationsInfo) presentationsGrid.getGridInfo();
		
		if (gridInfo == null){
			gridInfo = new PresentationsInfo();
		}

		gridInfo.setCompanyId(SelectedCompanyController.getInstance().getSelectedCompanyId());
		
		return gridInfo;
	}
	
	private PresentationsInfo getTemplatesInfo(){
		PresentationsInfo gridInfo = (PresentationsInfo) templatesGrid.getGridInfo();
		
		if (gridInfo == null){
			gridInfo = new PresentationsInfo();
		}

		gridInfo.setCompanyId(SelectedCompanyController.getInstance().getSelectedCompanyId());
		
		return gridInfo;
	}
	
//	private void copyPresentationTemplate() {
//		statusBox.setStatus(StatusBoxWidget.Status.WARNING, "Copying presentation data...");
//		disableActions = true;
//		presentationService.copyTemplate(SelectedCompanyController.getInstance().getSelectedCompanyId(), templatesGrid.getCurrentKey(), rpcCopyCallBackHandler);
//	}

	class RpcPresentationsCallBackHandler extends RiseAsyncCallback<PresentationsInfo> {

		public void onFailure() {
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(PresentationsInfo result) {
			statusBox.clear();
			presentationsGrid.loadGrid(result);
			updatePresentationsTable(result);
		}
	}
	
	class RpcTemplatesCallBackHandler extends RiseAsyncCallback<PresentationsInfo> {

		public void onFailure() {
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(PresentationsInfo result) {
			templatesGrid.loadGrid(result);
			updateTemplatesTable(result);
		}
	}
	
	
//	class RpcCopyCallBackHandler extends RiseAsyncCallback<RpcResultInfo> {
//		public void onFailure() {
//			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
//		}
//
//		public void onSuccess(RpcResultInfo result) {
//			if (result == null || result.getErrorMessage() != null) {
//				disableActions = false;
//				statusBox.setStatus(StatusBoxWidget.Status.ERROR, "Error copying Template data. Please try again.");
//			}
//			else {
//				disableActions = false;
//
//				String[] params = {result.getId()};			
//				UiEntryPoint.loadContentStatic(ContentId.PRESENTATION_MANAGE, params);
//			}
//		}
//	}
}