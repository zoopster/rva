package com.risevision.ui.client.schedule;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.risevision.common.client.info.ScheduleInfo;
import com.risevision.common.client.json.PlaylistItemJsonParser;
import com.risevision.ui.client.UiEntryPoint;
import com.risevision.ui.client.common.ContentId;
import com.risevision.ui.client.common.controller.ConfigurationController;
import com.risevision.ui.client.common.controller.SelectedCompanyController;
import com.risevision.ui.client.common.exception.RiseAsyncCallback;
import com.risevision.ui.client.common.exception.ServiceFailedException;
import com.risevision.ui.client.common.info.FormValidatorInfo;
import com.risevision.ui.client.common.info.HistoryTokenInfo;
import com.risevision.ui.client.common.info.RpcResultInfo;
import com.risevision.ui.client.common.service.ScheduleService;
import com.risevision.ui.client.common.service.ScheduleServiceAsync;
import com.risevision.ui.client.common.widgets.ActionsWidget;
//import com.risevision.ui.client.common.widgets.DefaultSettingsWidget;
import com.risevision.ui.client.common.widgets.FormValidatorWidget;
import com.risevision.ui.client.common.widgets.LastModifiedWidget;
import com.risevision.ui.client.common.widgets.MessageBoxWidget;
import com.risevision.ui.client.common.widgets.StatusBoxWidget;
import com.risevision.ui.client.common.widgets.timeline.TimelineWidget;
import com.risevision.ui.client.display.DistributionWidget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ScheduleManageWidget extends Composite {
	private static ScheduleManageWidget instance;
	private ScheduleInfo scheduleInfo;
	private String scheduleId;
	//RPC
	private final ScheduleServiceAsync scheduleService = GWT
			.create(ScheduleService.class);
	private RpcGetScheduleCallBackHandler rpcGetScheduleCallBackHandler = new RpcGetScheduleCallBackHandler();
	private RpcCheckDistributionCallBackHandler rpcCheckDistributionCallBackHandler = new RpcCheckDistributionCallBackHandler();
	private RpcPutScheduleCallBackHandler rpcPutScheduleCallBackHandler = new RpcPutScheduleCallBackHandler();
	private RpcDeleteScheduleCallBackHandler rpcDeleteScheduleCallBackHandler = new RpcDeleteScheduleCallBackHandler();
//	private RpcDeleteScheduleItemsCallBackHandler rpcDeleteScheduleItemsCallBackHandler = new RpcDeleteScheduleItemsCallBackHandler();
//	private RpcPutScheduleItemCallBackHandler rpcPutScheduleItemCallBackHandler = new RpcPutScheduleItemCallBackHandler();
	//UI pieces
	private ActionsWidget actionsWidget = ActionsWidget.getInstance();
	private VerticalPanel mainPanel = new VerticalPanel();
	private FormValidatorWidget formValidator = new FormValidatorWidget();
	private StatusBoxWidget statusBox = StatusBoxWidget.getInstance();
	private Grid mainGrid = new Grid(3, 2);
	private int row = -1;
//	//UI: Schedule fields
	private Label lbScheduleId = new Label();
	private TextBox tbName = new TextBox();
	private TimelineWidget wgTimeline = new TimelineWidget();	
	private DistributionWidget wgDistribution = new DistributionWidget();	
//	private DefaultSettingsWidget wgDefaultSettings = new DefaultSettingsWidget(false, "");	
	private ScheduleItemGridWidget wgPlayList = new ScheduleItemGridWidget();
	//last modified
	private LastModifiedWidget wgLastModified = LastModifiedWidget.getInstance();

	public ScheduleManageWidget() {
		initWidget(mainPanel);
		mainPanel.add(formValidator);
		mainPanel.add(mainGrid);
//		mainPanel.add(wgDefaultSettings);
		mainPanel.add(new HTML("&nbsp;"));
		mainPanel.add(wgPlayList);

		styleControls();
		
		// add widgets
		gridAdd("Name*:", tbName, "rdn-TextBoxLong");
		gridAdd("Timeline:", wgTimeline, null);
		gridAdd("Distribution:", wgDistribution, null);

		initValidator();
	}

	private void styleControls() {
		//style the table	
		mainGrid.setCellSpacing(0);
		mainGrid.setCellPadding(0);
		mainGrid.setStyleName("rdn-Table");
	}

	private void initValidator() {
		// Add widgets to validator
		formValidator.addValidationElement(tbName, "Schedule Name", FormValidatorInfo.requiredFieldValidator);
	}

	private void initActions() {
		Command cmdSave = new Command() {
			public void execute() {
				doActionSave();
			}
		};
		Command cmdDelete = new Command() {
			public void execute() {
				doActionDelete();
			}
		};
		Command cmdPreview= new Command() {
			public void execute() {
				doActionPreview();
			}
		};
		Command cmdCancel = new Command() {
			public void execute() {
				doActionCancel();
			}
		};

		actionsWidget.clearActions();
		actionsWidget.addAction("Save", cmdSave);
		actionsWidget.addAction("Delete", cmdDelete);
		actionsWidget.addAction("Preview", cmdPreview);
		actionsWidget.addAction("Cancel", cmdCancel);
	}
	
	public static ScheduleManageWidget getInstance() {
		try {
			if (instance == null)
				instance = new ScheduleManageWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}

	protected void onLoad() {
		super.onLoad();

		initActions();
		actionsWidget.setEnabled(true);
		clearData();
		if (!(scheduleId == null || scheduleId.isEmpty()))
			loadDataRPC(scheduleId);
		
		wgPlayList.load();
		tbName.setFocus(true);
	}

	private void clearData() {
		scheduleInfo = new ScheduleInfo();
		bindData();
	}

	private void bindData() {
		if (scheduleInfo == null)
			return;
		
		
		scheduleInfo.setPlayListItems(PlaylistItemJsonParser.parsePlaylistItems(scheduleInfo.getContent()));

		lbScheduleId.setText(scheduleInfo.getId());
		tbName.setText(scheduleInfo.getName());
		wgTimeline.setTimeline(scheduleInfo.getTimeline());
		//TODO:remove descriotion
		//wgTimeline.setTimelineDescription(scheduleInfo.getTimelineDescription());
		wgDistribution.setDistributionToAll(scheduleInfo.getDistributionToAll());
		wgDistribution.setDistribution(scheduleInfo.getDistribution());
		
//		wgDefaultSettings.setTransition(scheduleInfo.getTransition());
//		wgDefaultSettings.setScale(scheduleInfo.getScale());
//		wgDefaultSettings.setPosition(scheduleInfo.getPosition());

		wgPlayList.setPlayListItems(scheduleInfo.getPlayListItems());
		
		wgLastModified.Initialize(scheduleInfo.getChangedBy(), scheduleInfo.getChangeDate());
		statusBox.clear();
	}

	private void saveData() {
		if (scheduleInfo == null)
			return;

		if (!formValidator.validate())
			return;
		
		//scheduleInfo.setId(lbScheduleId.getText()); //ID for new schedule will be generated on server (web app, not core API)
		
		scheduleInfo.setName(tbName.getText());
		scheduleInfo.setTimeline(wgTimeline.getTimeline());
		scheduleInfo.setDistributionToAll(wgDistribution.getDistributionToAll());
		scheduleInfo.setDistribution(wgDistribution.getDistribution());
		
		scheduleInfo.setPlayListItems(wgPlayList.getPlayListItems());
		scheduleInfo.setContent(PlaylistItemJsonParser.updatePlaylistItems(scheduleInfo.getPlayListItems()));

//		scheduleInfo.setTransition(wgDefaultSettings.getTransition());
//		scheduleInfo.setScale(wgDefaultSettings.getScale());
//		scheduleInfo.setPosition(wgDefaultSettings.getPosition());

		//scheduleInfo.setPlayListItems(wgPlayList.getPlayListItems());
//		scheduleInfo.setPlayListItems(null); // PlayListItems are saved separately
		
		checkDistribution();
		//saveDataRPC(scheduleInfo);
	}

	private void deleteData() {
		deleteDataRPC(scheduleId);
	}
	
	private void loadDataRPC(String scheduleId) {
		actionsWidget.setEnabled(false);
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, StatusBoxWidget.LOADING);
		scheduleService.getSchedule(SelectedCompanyController.getInstance().getSelectedCompanyId(), scheduleId, rpcGetScheduleCallBackHandler);
	}
	
	private void checkDistribution() {
		actionsWidget.setEnabled(false);
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, StatusBoxWidget.SAVING);
//		updateProgressBar(0);
		
		scheduleService.checkDistribution(SelectedCompanyController.getInstance().getSelectedCompanyId(), scheduleInfo, rpcCheckDistributionCallBackHandler);
	}
	
	private void saveDataRPC(ScheduleInfo si) {
		//we need to clear items to prevent RPC from serializing it and sending to server
//		si.setPlayListItems(null);
		//save schedule only
		scheduleService.putSchedule(SelectedCompanyController.getInstance().getSelectedCompanyId(), si, rpcPutScheduleCallBackHandler);
		//if list has changed, then we need to save every item again
//		if (wgPlayList.isListChanged())
//			for (PlaylistItemInfo item : wgPlayList.getPlayListItems())
//				item.setChanged(true);			
	}

	private void deleteDataRPC(String scheduleId) {
		actionsWidget.setEnabled(false);
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, StatusBoxWidget.DELETING);
		scheduleService.deleteSchedule(SelectedCompanyController.getInstance().getSelectedCompanyId(), scheduleId, rpcDeleteScheduleCallBackHandler);
	}

	private void gridAdd(String label, Widget widget, String styleName) {
		row++;
		mainGrid.getCellFormatter().setStyleName(row, 0, "rdn-Column1");
		mainGrid.setText(row, 0, label);
		if (widget != null)
		{
			mainGrid.setWidget(row, 1, widget);
			if (styleName != null)
				widget.setStyleName(styleName);
		}
	}

	private void doActionSave() {
		saveData();
	}

	private void doActionDelete() {
		if (Window.confirm("Are you sure you want to delete this schedule?")) {
			deleteData();
		}
	}
	
	private void doActionPreview() {
		Window.open(ConfigurationController.getInstance().getConfiguration().getViewerURL() + "Viewer.html?type=schedule&id=" + scheduleId, "_blank", "");
		//Window.open(Globals.VIEWER_URL + "Viewer.html?type=schedule&id=" + scheduleId, "_blank", "");
	}

	private void doActionCancel() {
		UiEntryPoint.loadContentStatic(ContentId.SCHEDULES);
	}

	public void setToken(HistoryTokenInfo tokenInfo) {
		scheduleId = tokenInfo.getId();
	}

//	public void saveNextScheduleItem() {
//		for (PlaylistItemInfo item : wgPlayList.getPlayListItems())
//			if (item.isChanged()) {
//				scheduleService.putScheduleItem(SelectedCompanyController.getInstance().getSelectedCompanyId(), scheduleId, item, rpcPutScheduleItemCallBackHandler);
//				return;
//			}
//		// all items are saved
//		statusBox.clear();
//		wgPlayList.setListChanged(false);
//		actionsWidget.setEnabled(true);
//	}
	
//	private int calcPercentOfSavedScheduleItems() {
//		if (wgPlayList.getPlayListItems().size() == 0)
//			return 100;
//		
//		int numberOfChangedItems = 0;
//		
//		for (PlaylistItemInfo item : wgPlayList.getPlayListItems())
//			if (item.isChanged()) 
//				numberOfChangedItems++;
//
//		return 100 - ((70 / wgPlayList.getPlayListItems().size()) * numberOfChangedItems);
//	}

//	private void markScheduleItemAsSaved(String id) {
//		for (PlaylistItemInfo item : wgPlayList.getPlayListItems())
//			if (id.equals(item.getId())) {
//				item.setChanged(false);
//				return;
//			}
//	}

//	public void updateProgressBar(int percent) {
//		statusBox.setStatus(StatusBoxWidget.Status.WARNING, "Saving in progress " + Integer.toString(percent) + "%");
//	}
	
	//--------- RPC CLASSES ---------------//
	
	class RpcGetScheduleCallBackHandler extends RiseAsyncCallback<ScheduleInfo> {

		public void onFailure() {
			if (caught instanceof ServiceFailedException 
					&& ((ServiceFailedException)caught).getReason() == ServiceFailedException.NOT_FOUND) {
//				statusBox.setStatus(StatusBoxWidget.Status.ERROR, "The Schedule was not found, or " +
//						"you do not have access to it from your Company.");
				
				statusBox.clear();
				
				MessageBoxWidget.getInstance().show("Very sorry but we can't find that Schedule. " +
						"It has either been deleted or you don't have access to it from your Company. ");
				UiEntryPoint.loadContentStatic(ContentId.HOME);
			}
			else {
				actionsWidget.setEnabled(true);
				statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
			}	
		}

		public void onSuccess(ScheduleInfo result) {
			actionsWidget.setEnabled(true);
			if (result == null)
				statusBox.setStatus(StatusBoxWidget.Status.ERROR, "Error retrieving Schedule data. Please try again.");
			else {
				scheduleInfo = result;
				bindData();
			}
		}
	}

	class RpcCheckDistributionCallBackHandler extends RiseAsyncCallback<RpcResultInfo> {

		public void onFailure() {
			actionsWidget.setEnabled(true);
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(RpcResultInfo result) {
			if (result == null)
				statusBox.setStatus(StatusBoxWidget.Status.ERROR, "Error verifying Distribution data. Please try again.");
			else {
				if (result.getErrorMessage() != null) {
					actionsWidget.setEnabled(true);
					statusBox.setStatus(StatusBoxWidget.Status.ERROR, result.getErrorMessage());
				}
				else {
//					updateProgressBar(50);
					saveDataRPC(scheduleInfo);
				}
			}
		}
	}
	
	class RpcPutScheduleCallBackHandler extends RiseAsyncCallback<RpcResultInfo> {

		public void onFailure() {
			actionsWidget.setEnabled(true);
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(RpcResultInfo result) {
			if (result == null)
				statusBox.setStatus(StatusBoxWidget.Status.ERROR, "Error retrieving Schedule data. Please try again.");
			else {
				//update ID
				scheduleId = result.getId();
				scheduleInfo.setId(scheduleId);
				
				statusBox.clear();
				actionsWidget.setEnabled(true);

//				updateProgressBar(20);
//				if (wgPlayList.isListChanged())
//					scheduleService.deleteScheduleItems(SelectedCompanyController.getInstance().getSelectedCompanyId(), scheduleId, rpcDeleteScheduleItemsCallBackHandler);
//				else
//					saveNextScheduleItem();
			}
		}
	}

	class RpcDeleteScheduleCallBackHandler extends RiseAsyncCallback<RpcResultInfo> {

		public void onFailure() {
			actionsWidget.setEnabled(true);
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(RpcResultInfo result) {
			//if Delete action succeeds, simply close the page
			doActionCancel();
			actionsWidget.setEnabled(true);
		}
	}
	
//	class RpcDeleteScheduleItemsCallBackHandler extends RiseAsyncCallback<RpcResultInfo> {
//
//		public void onFailure() {
//			actionsWidget.setEnabled(true);
//			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
//		}
//
//		public void onSuccess(RpcResultInfo result) {
//			updateProgressBar(30);
//			saveNextScheduleItem();
//		}
//	}

//	class RpcPutScheduleItemCallBackHandler extends RiseAsyncCallback<RpcResultInfo> {
//
//		public void onFailure() {
//			actionsWidget.setEnabled(true);
//			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
//		}
//
//		public void onSuccess(RpcResultInfo result) {
//			markScheduleItemAsSaved(result.getId());
//			updateProgressBar(calcPercentOfSavedScheduleItems());
//			saveNextScheduleItem();
//		}
//	}
}