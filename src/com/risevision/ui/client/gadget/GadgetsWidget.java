package com.risevision.ui.client.gadget;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.risevision.core.api.attributes.CommonAttribute;
import com.risevision.core.api.attributes.GadgetAttribute;
import com.risevision.ui.client.UiEntryPoint;
import com.risevision.ui.client.common.ContentId;
import com.risevision.ui.client.common.controller.SelectedCompanyController;
import com.risevision.ui.client.common.data.GadgetDataController;
import com.risevision.ui.client.common.exception.RiseAsyncCallback;
import com.risevision.ui.client.common.info.GadgetInfo;
import com.risevision.ui.client.common.info.GadgetsInfo;
import com.risevision.ui.client.common.info.ScrollingGridInfo;
import com.risevision.ui.client.common.widgets.ActionsWidget;
import com.risevision.ui.client.common.widgets.LastModifiedWidget;
import com.risevision.ui.client.common.widgets.StatusBoxWidget;
import com.risevision.ui.client.common.widgets.grid.ScrollingGridWidget;

public class GadgetsWidget extends Composite {

	private static GadgetsWidget instance;
	//RPC
//	private final GadgetServiceAsync rpc = GWT.create(GadgetService.class);
	private RpcCallBackHandler rpcCallBackHandler = new RpcCallBackHandler();
	//UI pieces
	private StatusBoxWidget statusBox = StatusBoxWidget.getInstance();
	private ActionsWidget actionsWidget = ActionsWidget.getInstance();
	//grid
	private ScrollingGridWidget gr;
	private Command grCommand;

	public GadgetsWidget() {
		initGridWidget();
		initWidget(gr);
	}

	public static GadgetsWidget getInstance() {
		try {
			if (instance == null)
				instance = new GadgetsWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}	

	private void initGridWidget(){
		grCommand = new Command() {
			public void execute() {			
				processGridCommand();
			}
		};			
		
		String[][] headerDefinition = new String[][] {
				{"Name", GadgetAttribute.NAME, "400px"},
				{"Description", GadgetAttribute.DESCRIPTION, "100%"},
				{"Type", GadgetAttribute.GADGET_TYPE, "100px"},
				{"Shared", GadgetAttribute.SHARED, "60px"},
				{"Last Modified", CommonAttribute.CHANGE_DATE, "180px"}
				};

		gr = new ScrollingGridWidget(grCommand, new GadgetsInfo(), headerDefinition);
	}	

	private void initActions(){
		Command cmdAdd = new Command() {
			public void execute() {
				doActionAdd();
			}
		};		

		actionsWidget.addAction("Add", cmdAdd);
	}	

	protected void onLoad() {
		super.onLoad();
		initActions();
		gr.clear();
		loadGridDataRPC();
	}

	private void doActionAdd() {
		UiEntryPoint.loadContentStatic(ContentId.GADGET_MANAGE);
	}
	
	private void loadGridDataRPC() {
		GadgetDataController controller = GadgetDataController.getInstance();
		
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, "Data is loading...");
		controller.getGadgets(getGadgetsInfo(), false, rpcCallBackHandler);
		
//		rpc.getGadgets(getGadgetsInfo(), rpcCallBackHandler);
	}

	private void processGridCommand(){
		int command = gr.getCurrentCommand();
					
		switch (command) {
//		case ScrollingGridInfo.SELECTACTION:
//			HistoryTokenInfo tokenInfo = new HistoryTokenInfo();
//			tokenInfo.setId(gr.getCurrentKey());
//			tokenInfo.setContentId(ContentId.GADGET_MANAGE);
//			
//			UiEntryPoint.loadContentStatic(tokenInfo);
//			break;
		case ScrollingGridInfo.SEARCHACTION:
		case ScrollingGridInfo.PAGEACTION:
		case ScrollingGridInfo.SORTACTION:
			loadGridDataRPC();			
			break;
		default:
			break;
		}
	}

	private void updateTable(GadgetsInfo gadgetsInfo) {
		gr.clearGrid();
		if ((gadgetsInfo != null) && (gadgetsInfo.getGadgets() != null))
			for (int i = 0; i < gadgetsInfo.getGadgets().size(); i++)
				updateTableRow(gadgetsInfo.getGadgets().get(i), i);
	}

	private void updateTableRow(final GadgetInfo gadget, int row) {
//		gr.setAction(row, 0, gadget.getName(), gadget.getId());
		gr.setHyperlink(row, 0, gadget.getName(), ContentId.GADGET_MANAGE, gadget.getId());
		gr.setText(row, 1, gadget.getDescription());
		gr.setText(row, 2, gadget.getType());
		gr.setText(row, 3, gadget.isShared() ? "Yes" : "No");
		gr.setText(row, 4, LastModifiedWidget.getLastModified(gadget.getChangedBy(), gadget.getChangeDate()));
	}

	private GadgetsInfo getGadgetsInfo(){
		GadgetsInfo gridInfo = (GadgetsInfo) gr.getGridInfo();
		
		if (gridInfo == null){
			gridInfo = new GadgetsInfo();
		}

		gridInfo.setCompanyId(SelectedCompanyController.getInstance().getSelectedCompanyId());
		
		return gridInfo;
	}

	class RpcCallBackHandler extends RiseAsyncCallback<GadgetsInfo> {
		public void onFailure() {
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(GadgetsInfo result) {
			statusBox.clear();
			gr.loadGrid(result);
			updateTable(result);
		}
	}
}