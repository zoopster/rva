package com.risevision.ui.client.presentation;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.risevision.common.client.info.PresentationInfo;
import com.risevision.core.api.attributes.PresentationAttribute;
import com.risevision.core.api.types.PresentationRevisionStatus;
import com.risevision.ui.client.common.controller.SelectedCompanyController;
import com.risevision.ui.client.common.data.PresentationDataController;
import com.risevision.ui.client.common.exception.RiseAsyncCallback;
import com.risevision.ui.client.common.info.PresentationsInfo;
import com.risevision.ui.client.common.info.ScrollingGridInfo;
import com.risevision.ui.client.common.widgets.StatusBoxWidget;
import com.risevision.ui.client.common.widgets.grid.ScrollingGridWidget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class PresentationSelectWidget extends VerticalPanel {
//	private boolean isTemplate = true; 
	private boolean disableActions = false;
	private boolean setMinHeight;
//	private String newPresentationId = null;
	//RPC
//	private final PresentationServiceAsync presentationService = GWT.create(PresentationService.class);
	private RpcListCallBackHandler rpcListCallBackHandler = new RpcListCallBackHandler();
//	private RpcCopyCallBackHandler rpcCopyCallBackHandler = new RpcCopyCallBackHandler();
	//UI pieces
	private VerticalPanel mainPanel = new VerticalPanel();
	private StatusBoxWidget statusBox = new StatusBoxWidget();
	
	//grid
	private ScrollingGridWidget gr;
	private Command grCommand;
	
	private Command selectCommand;
	
	public PresentationSelectWidget(boolean setMinHeight) {
		this.setMinHeight = setMinHeight;
		
		initGridWidget();
		
		mainPanel.add(statusBox);
		mainPanel.add(gr);

		add(mainPanel);
	}

	private void initGridWidget(){
		grCommand = new Command() {
			public void execute() {			
				processGridCommand();
			}
		};			
		
		String[][] headerDefinition = null;
			headerDefinition = new String[][] {
					{"", null, "60px"},
					{setMinHeight ? "": "Name", setMinHeight ? null : PresentationAttribute.NAME, "100%"},
					{setMinHeight ? "": "Status", setMinHeight ? null : PresentationAttribute.REVISION_STATUS, "80px"},
					};

		PresentationsInfo presentationsInfo = new PresentationsInfo();
		presentationsInfo.setSortByDefault(PresentationAttribute.NAME);
		presentationsInfo.setSortDirection(ScrollingGridInfo.SORT_DOWN);

		gr = new ScrollingGridWidget(grCommand, presentationsInfo, headerDefinition, true);
		
		gr.setGridHeight("300px");
	}	

//	protected void onLoad() {
//		super.onLoad();
//		gr.clear();
//		loadGridDataRPC();
//	}
	
	public void load() {
		gr.clear();
		loadGridDataRPC();
	}

	public void init(Command selectCommand){
		this.selectCommand = selectCommand;
	}
	
	private void loadGridDataRPC() {
		PresentationDataController controller = PresentationDataController.getInstance();
		
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, StatusBoxWidget.LOADING);
		controller.getPresentations(getPresentationsInfo(), false, rpcListCallBackHandler);
		
//		presentationService.getPresentations(getPresentationsInfo(), rpcListCallBackHandler);
	}

	public PresentationInfo getCurrentPresentation(){
		String currentKey;
		PresentationsInfo presentations;

		currentKey = gr.getCurrentKey();
		presentations = (PresentationsInfo) gr.getGridInfo();

		if (currentKey != null && !currentKey.isEmpty() && presentations != null && presentations.getPresentations() != null) {
			for (PresentationInfo presentation: presentations.getPresentations()) {
				if (presentation.getId() != null && !presentation.getId().isEmpty() && currentKey.equals(presentation.getId())) {
					return presentation;
				}
			}
		}
		return null;
	}
	
	private void processGridCommand(){
		int command = gr.getCurrentCommand();
					
		if (disableActions) {
			return;
		}
		
		switch (command) {
		case ScrollingGridInfo.SELECTACTION:
			if (selectCommand != null){
				selectCommand.execute();
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
	
	private void updateTable(PresentationsInfo presentationsInfo) {
		if ((presentationsInfo != null) && (presentationsInfo.getPresentations() != null)) {
			int i = 0;
			if (gr.getCurrentCommand() != ScrollingGridInfo.PAGEACTION) {
				gr.clearGrid();
			}
			else {
				i = gr.getRowCount();
			}

			for ( ; i < presentationsInfo.getPresentations().size(); i++) {
				updateTableRow(presentationsInfo.getPresentations().get(i), i);
			}
		}
	}

	private void updateTableRow(final PresentationInfo presentation, int row) {
		gr.setAction(row, 0, "Select", presentation.getId());
		gr.setText(row, 1, presentation.getName());
		
		HTML revisionStatus = new HTML(presentation.getRevisionStatus() == PresentationRevisionStatus.PUBLISHED ? "Published" : 
				"<span style='color:red;'>Revised</span>");
		gr.setWidget(row, 2, revisionStatus);

//		if (isTemplate) {
//			gr.setWidget(row, 2, new Anchor("Preview", 
//					ConfigurationController.getInstance().getConfiguration().getViewerURL() + "Viewer.html?type=presentation&id=" + presentation.getId(), 
//					"_blank"));
//		}
	}

	private PresentationsInfo getPresentationsInfo(){
		PresentationsInfo gridInfo = (PresentationsInfo) gr.getGridInfo();
		
		if (gridInfo == null){
			gridInfo = new PresentationsInfo();
		}

		gridInfo.setCompanyId(SelectedCompanyController.getInstance().getSelectedCompanyId());
		
		return gridInfo;
	}
	
	public String getCurrentKey(){
		return gr.getCurrentKey();
	}

	class RpcListCallBackHandler extends RiseAsyncCallback<PresentationsInfo> {

		public void onFailure() {
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(PresentationsInfo result) {
			statusBox.clear();
			gr.loadGrid(result);
			updateTable(result);
		}
	}

}