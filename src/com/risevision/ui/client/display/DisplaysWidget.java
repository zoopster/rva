package com.risevision.ui.client.display;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.risevision.core.api.attributes.CommonAttribute;
import com.risevision.core.api.attributes.DisplayAttribute;
import com.risevision.ui.client.UiEntryPoint;
import com.risevision.ui.client.common.ContentId;
import com.risevision.ui.client.common.controller.ConfigurationController;
import com.risevision.ui.client.common.controller.SelectedCompanyController;
import com.risevision.ui.client.common.directory.DisplayDataController;
import com.risevision.ui.client.common.exception.RiseAsyncCallback;
import com.risevision.ui.client.common.info.DisplayInfo;
import com.risevision.ui.client.common.info.DisplaysInfo;
import com.risevision.ui.client.common.info.ScrollingGridInfo;
import com.risevision.ui.client.common.widgets.ActionsWidget;
import com.risevision.ui.client.common.widgets.LastModifiedWidget;
import com.risevision.ui.client.common.widgets.StatusBoxWidget;
import com.risevision.ui.client.common.widgets.grid.ScrollingGridWidget;

public class DisplaysWidget extends Composite {

	private static DisplaysWidget instance;
	//RPC
//	private final DisplayServiceAsync rpc = GWT.create(DisplayService.class);
	private RpcCallBackHandler rpcCallBackHandler = new RpcCallBackHandler();
	//UI pieces
	private StatusBoxWidget statusBox = StatusBoxWidget.getInstance();
	private ActionsWidget actionsWidget = ActionsWidget.getInstance();
	//grid
	private ScrollingGridWidget gr;
	private Command grCommand;

	public DisplaysWidget() {
		initGridWidget();	
		initWidget(gr);
	}

	public static DisplaysWidget getInstance() {
		try {
			if (instance == null)
				instance = new DisplaysWidget();
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
				{"", null, "20px"},
				{"Name", DisplayAttribute.NAME, "100%"},
				//{"ID", DisplayAttribute.ID, "100px"},
				{"Status", DisplayAttribute.STATUS, "60px"},
				{"Auto Upgrade", null, "90px"},
				{"Resolution", DisplayAttribute.WIDTH, "80px"},
				{"Address", DisplayAttribute.ADDRESS, "250px"},
				//{"PNO's", DisplayAttribute.CONTROLLED, "20px"},
				//{"Player Version", DisplayAttribute.PLAYERVERSION, "50px"},
				{"Last Modified", CommonAttribute.CHANGE_DATE, "180px"}
				};

		gr = new ScrollingGridWidget(grCommand, new DisplaysInfo(), headerDefinition);
	}	

	private void initActions(){
		Command cmdDownload = new Command() {
			public void execute() {
//				DisplayInstallWidget.getInstance().show();
				Window.open(ConfigurationController.getInstance().getConfiguration().getInstallerURL(), "_blank", "");
			}
		};
		Command cmdAdd = new Command() {
			public void execute() {
				doActionAdd();
			}
		};		

		actionsWidget.addAction("Download Player", cmdDownload);
		actionsWidget.addAction("Add Display", cmdAdd);
	}	

	protected void onLoad() {
		super.onLoad();
		initActions();
		gr.clear();
		loadGridDataRPC();
	}

	private void doActionAdd() {
		UiEntryPoint.loadContentStatic(ContentId.DISPLAY_MANAGE);
	}
	
	private void loadGridDataRPC() {
		DisplayDataController controller = DisplayDataController.getInstance();
		controller.getDisplays(getDisplaysInfo(), false, rpcCallBackHandler);

		statusBox.setStatus(StatusBoxWidget.Status.WARNING, "Data is loading...");
//		rpc.getDisplays(getDisplaysInfo(), false, rpcCallBackHandler);
	}
	
//	public void setData(DisplaysInfo result) {
//		statusBox.clear();
//		gr.loadGrid(result);
//		updateTable(result);
//	}

	private void processGridCommand(){
		int command = gr.getCurrentCommand();
					
		switch (command) {
//		case ScrollingGridInfo.SELECTACTION:
//			String[] params = gr.getCurrentKey().split(",");
//
//			HistoryTokenInfo tokenInfo = new HistoryTokenInfo();
//			tokenInfo.setId(params[0]);
//			if (params.length >= 2 && !params[1].isEmpty()) {
//				tokenInfo.setCompanyId(params[1]);
//			}
//			tokenInfo.setContentId(ContentId.DISPLAY_MANAGE);
//			
//			UiEntryPoint.loadContentStatic(tokenInfo);
//
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

	private void updateTable(DisplaysInfo displaysInfo) {
		if ((displaysInfo != null) && (displaysInfo.getDisplays() != null)) {
			int i = 0;
			if (gr.getCurrentCommand() != ScrollingGridInfo.PAGEACTION) {
				gr.clearGrid();
			}
			else {
				i = gr.getRowCount();
			}

			for ( ; i < displaysInfo.getDisplays().size(); i++) {
				updateTableRow(displaysInfo.getDisplays().get(i), i);
			}
		}
	}

	private void updateTableRow(final DisplayInfo display, int row) {
		int col = 0;
		gr.setWidget(row, col++, new DisplayHeartbeatWidget(display.getDisplayStatus() /*, display.getBlockExpiryDate() */));
		gr.getCellFormatter().getElement(row, 0).getStyle().setOverflow(Overflow.VISIBLE);
//		gr.setAction(row, 1, display.getName(), display.getId() + "," + display.getCompanyId());
		gr.setHyperlink(row, col++, display.getName(), ContentId.DISPLAY_MANAGE, display.getId(), display.getCompanyId());
		//gr.setText(row, 1, display.getId());
		gr.setText(row, col++, display.getSubscriptionStatusName());

//		HTML upgradeMode = new HTML(display.getBrowserUpgradeMode() == 0 ? display.getBrowserUpgradeModeString() :
//			"<span style='color:red;'>" + display.getBrowserUpgradeModeString() + "</span>");
		HTML upgradeMode = new HTML(display.getBrowserUpgradeMode() == 0 ? "Yes" :
			"<span style='color:red;'>No</span>");
		
		gr.setWidget(row, col++, upgradeMode);
//		gr.setText(row, col++, display.getBrowserUpgradeModeString());
		
		gr.setText(row, col++, display.getWidth() + "x" + display.getHeight());
		gr.setText(row, col++, display.getAddress());
		//gr.setText(row, 5, display.getPnoControlled() ? "Y" : "N");
		//gr.setText(row, 6, display.getPlayerVersion());
		//gr.setText(row, 5, display.getCompanyName());
		gr.setText(row, col++, LastModifiedWidget.getLastModified(display.getChangedBy(), display.getChangeDate()));
	}

	private DisplaysInfo getDisplaysInfo(){
		DisplaysInfo gridInfo = (DisplaysInfo) gr.getGridInfo();
		
//		if (gridInfo == null){
//			gridInfo = new DisplaysInfo();
//		}

		gridInfo.setCompanyId(SelectedCompanyController.getInstance().getSelectedCompanyId());
		
		return gridInfo;
	}

	class RpcCallBackHandler extends RiseAsyncCallback<DisplaysInfo> {

		public void onFailure() {
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(DisplaysInfo result) {
			statusBox.clear();
			gr.loadGrid(result);
			updateTable(result);
		}
	}
}