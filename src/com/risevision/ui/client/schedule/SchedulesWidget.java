package com.risevision.ui.client.schedule;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.risevision.common.client.info.ScheduleInfo;
import com.risevision.core.api.attributes.CommonAttribute;
import com.risevision.core.api.attributes.ScheduleAttribute;
import com.risevision.ui.client.UiEntryPoint;
import com.risevision.ui.client.common.ContentId;
import com.risevision.ui.client.common.controller.SelectedCompanyController;
import com.risevision.ui.client.common.data.ScheduleDataController;
import com.risevision.ui.client.common.exception.RiseAsyncCallback;
import com.risevision.ui.client.common.info.SchedulesInfo;
import com.risevision.ui.client.common.info.ScrollingGridInfo;
import com.risevision.ui.client.common.widgets.ActionsWidget;
import com.risevision.ui.client.common.widgets.LastModifiedWidget;
import com.risevision.ui.client.common.widgets.StatusBoxWidget;
import com.risevision.ui.client.common.widgets.grid.ScrollingGridWidget;

public class SchedulesWidget extends Composite {
	private static SchedulesWidget instance;
	//RPC
//	private final ScheduleServiceAsync rpc = GWT.create(ScheduleService.class);
//	private RpcCallBackHandler rpcCallBackHandler = new RpcCallBackHandler();
	//UI pieces
	private StatusBoxWidget statusBox = StatusBoxWidget.getInstance();
	private ActionsWidget actionsWidget = ActionsWidget.getInstance();;
	//grid
	private ScrollingGridWidget gr;
	private Command grCommand;

	public SchedulesWidget() {
		initGridWidget();	
		initWidget(gr);
	}

	public static SchedulesWidget getInstance() {
		try {
			if (instance == null)
				instance = new SchedulesWidget();
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
				{"Name", ScheduleAttribute.NAME, "100%"},
				{"Last Modified", CommonAttribute.CHANGE_DATE, "180px"}
				};

		gr = new ScrollingGridWidget(grCommand, new SchedulesInfo(), headerDefinition);
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
//		HistoryTokenInfo tokenInfo = new HistoryTokenInfo();
//		tokenInfo.setId("");
//		tokenInfo.setContentId(ContentId.SCHEDULE_MANAGE);
		
//		String[] params = {""};
		UiEntryPoint.loadContentStatic(ContentId.SCHEDULE_MANAGE);
	}
	
	private void loadGridDataRPC() {
		ScheduleDataController controller = ScheduleDataController.getInstance();
		
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, "Data is loading...");
		controller.getSchedules(getSchedulesInfo(), new RpcCallBackHandler());
		
//		rpc.getSchedules(getSchedulesInfo(), rpcCallBackHandler);
	}

	private void processGridCommand(){
		int command = gr.getCurrentCommand();
					
		switch (command) {
//		case ScrollingGridInfo.SELECTACTION:
//			HistoryTokenInfo tokenInfo = new HistoryTokenInfo();
//			tokenInfo.setId(gr.getCurrentKey());
//			tokenInfo.setContentId(ContentId.SCHEDULE_MANAGE);
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

	private void updateTable(SchedulesInfo schedulesInfo) {
		if ((schedulesInfo != null) && (schedulesInfo.getSchedules() != null)) {
			int i = 0;
			if (gr.getCurrentCommand() != ScrollingGridInfo.PAGEACTION) {
				gr.clearGrid();
			}
			else {
				i = gr.getRowCount();
			}

			for ( ; i < schedulesInfo.getSchedules().size(); i++) {
				updateTableRow(schedulesInfo.getSchedules().get(i), i);
			}
		}
	}

	private void updateTableRow(final ScheduleInfo schedule, int row) {
//		gr.setAction(row, 0, schedule.getName(), schedule.getId());
		gr.setHyperlink(row, 0, schedule.getName(), ContentId.SCHEDULE_MANAGE, schedule.getId());
		gr.setText(row, 1, LastModifiedWidget.getLastModified(schedule.getChangedBy(), schedule.getChangeDate()));
	}

	private SchedulesInfo getSchedulesInfo(){
		SchedulesInfo gridInfo = (SchedulesInfo) gr.getGridInfo();
		
		if (gridInfo == null){
			gridInfo = new SchedulesInfo();
		}

		gridInfo.setCompanyId(SelectedCompanyController.getInstance().getSelectedCompanyId());
		
		return gridInfo;
	}
	
	class RpcCallBackHandler extends RiseAsyncCallback<SchedulesInfo> {

		public void onFailure() {
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(SchedulesInfo result) {
			statusBox.clear();
			gr.loadGrid(result);
			updateTable(result);
		}
	}
}