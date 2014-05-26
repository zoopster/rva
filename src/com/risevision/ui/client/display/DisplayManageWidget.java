package com.risevision.ui.client.display;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.core.api.types.BrowserUpgradeMode;
import com.risevision.core.api.types.DisplayStatus;
import com.risevision.ui.client.UiEntryPoint;
import com.risevision.ui.client.common.ContentId;
import com.risevision.ui.client.common.controller.ConfigurationController;
import com.risevision.ui.client.common.controller.SelectedCompanyController;
import com.risevision.ui.client.common.exception.RiseAsyncCallback;
import com.risevision.ui.client.common.info.DisplayInfo;
import com.risevision.ui.client.common.info.FormValidatorInfo;
import com.risevision.ui.client.common.info.HistoryTokenInfo;
import com.risevision.ui.client.common.info.RpcResultInfo;
import com.risevision.ui.client.common.service.DisplayService;
import com.risevision.ui.client.common.service.DisplayServiceAsync;
import com.risevision.ui.client.common.widgets.ActionsWidget;
import com.risevision.ui.client.common.widgets.AddressWidget;
import com.risevision.ui.client.common.widgets.FormValidatorWidget;
import com.risevision.ui.client.common.widgets.LastModifiedWidget;
import com.risevision.ui.client.common.widgets.SpacerWidget;
import com.risevision.ui.client.common.widgets.StatusBoxWidget;
import com.risevision.ui.client.common.widgets.TooltipLabelWidget;
import com.risevision.ui.client.common.widgets.grid.FormGridWidget;
import com.risevision.ui.client.common.widgets.socialConnector.SocialConnectorDisplayWidget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class DisplayManageWidget extends Composite {
	private static final String PLAYER_RESTART_TEXT = "Restart Player";
	private static final String PLAYER_REBOOT_TEXT = "Reboot Computer";
	
	private static DisplayManageWidget instance;
	private DisplayInfo displayInfo;
	private String displayId;
	//RPC
	private final DisplayServiceAsync displayService = GWT.create(DisplayService.class);
	//UI pieces
	private ActionsWidget actionsWidget = ActionsWidget.getInstance();
	private VerticalPanel mainPanel = new VerticalPanel();
	private FormValidatorWidget formValidator = new FormValidatorWidget();
	private StatusBoxWidget statusBox = StatusBoxWidget.getInstance();
	private FormGridWidget topGrid = new FormGridWidget(4, 2);
	private FormGridWidget playerGrid = new FormGridWidget(13, 2);
	private FormGridWidget socialConnectorGrid = new FormGridWidget(1, 2);
	//UI: Display fields
	private TextBox tbName = new TextBox();
	private DisplayHeartbeatWidget heartbeatWidget = new DisplayHeartbeatWidget();
//	private Label lastUpdateLabel = new Label();
	private TooltipLabelWidget lastUpdateLabel = new TooltipLabelWidget();
	private DisplayStatusWidget wgStatus = new DisplayStatusWidget();
//	private PlayerErrorLabel playerStatusLabel = new PlayerErrorLabel();
//	private int playerStatusRow = -1;
	//private RiseListBox lsbAspectRatio = new RiseListBox();
//	private DefaultResolutionWidget resolutionWidget = new DefaultResolutionWidget();
	private Label resolutionLabel = new Label();
	private AddressWidget wgAddress = new AddressWidget(true);
	private CheckBox cbUseCompanyAddress = new CheckBox();
	private RestartWidget scheduledRebootWidget = new RestartWidget();
	private CheckBox monitoringCheckBox = new CheckBox();
//	private HorizontalPanel installPanel = new HorizontalPanel();
	private Label lbDisplayId = new Label();
	
//	private Anchor installLink = new Anchor("Download Install File");
//	private Anchor windowsLink = new Anchor("Windows");
//	private Anchor linuxLink = new Anchor("Linux");
//	private Anchor macLink = new Anchor("Mac");
	private ActionsWidget playerActions = new ActionsWidget();
	private int installWarningRow;

	private PlayerOSLabel osVersionLabel = new PlayerOSLabel();
	private Label playerVersionLabel = new Label("N/A");
	private Label viewerVersionLabel = new Label("N/A");
	private int autoupgradeWarningRow;
	private BrowserUpgradeModeWidget browserUpgradeModeWidget = new BrowserUpgradeModeWidget();
	private Anchor applyToAllButton = new Anchor("Apply To ALL Displays");
	private Label chromiumVersionLabel = new Label("N/A");
	private Label recommendedChromiumLabel = new Label();
	private Label cacheVersionLabel = new Label("N/A");
	
	private PlayerErrorListWidget playerErrorList = new PlayerErrorListWidget();
	//last modified
	private LastModifiedWidget wgLastModified = LastModifiedWidget.getInstance();
	
	private SocialConnectorDisplayWidget socialConnectorWidget = new SocialConnectorDisplayWidget();

	public DisplayManageWidget() {
		// add widgets
		topGrid.addRow("Name*:", 
				"The name of your Display - which includes the Computer that the " +
				"Rise Player software is installed on and the display hardware that is attached", 
				tbName, "rdn-TextBoxLong");
		
		HorizontalPanel statusPanel = new HorizontalPanel();
		wgStatus.addStyleName("rdn-ListBoxShort");
		statusPanel.add(wgStatus);
		statusPanel.add(new SpacerWidget());
		statusPanel.add(heartbeatWidget);
		statusPanel.add(new SpacerWidget());
		statusPanel.add(lastUpdateLabel);
		
		topGrid.addRow("Status:",
				"Inactive Displays can't be Scheduled and instead show Demonstration Content",
				statusPanel, null);
//		topGrid.addRow("Player Status:", playerStatusLabel, null);
//		playerStatusRow = topGrid.getRow();
//		topGrid.addRow("Resolution*:", "The resolution of your display hardware", resolutionWidget);
		topGrid.addRow("Resolution*:", "The resolution of your display hardware", resolutionLabel);
		topGrid.addRow("Use Company Address:", 
				"The Address of your Display's location",
				cbUseCompanyAddress, "rdn-CheckBox");
		
		socialConnectorGrid.addRow("Social Connections:", 
				"Use the default Company venue ID or select a unique venue ID for this Display", 
				socialConnectorWidget);
		
//		installPanel.add(windowsLink);
//		installPanel.add(new SpacerWidget());
//		installPanel.add(new Label("|"));
//		installPanel.add(new SpacerWidget());
//		installPanel.add(linuxLink);
//		installPanel.add(new SpacerWidget());
//		installPanel.add(new Label("|"));
//		installPanel.add(new SpacerWidget());
//		installPanel.add(macLink);
		
		playerGrid.addRow("Display ID:", "The unique ID of your Display", lbDisplayId);
		playerGrid.addRow("", playerActions);
		
		Label installWarningLabel = new Label("Warning: No Player software is installed on the Display.", true);
		playerGrid.addRow("", installWarningLabel, "rdn-Validator");
		installWarningRow = playerGrid.getRow();
		
//		playerGrid.addRow("Heartbeat:", heartbeatWidget);
//		playerGrid.addRow("Last Update:", lastUpdateLabel);
		playerGrid.addRow("Scheduled Reboot:", 
				"The time of day that your Display reboots and performs its daily maintenance", 
				scheduledRebootWidget);
		playerGrid.addRow("Monitor:", 
				"Check to send email notifications to the Users defined in your Settings when " +
				"a Display fails / recovers",
				monitoringCheckBox, "rdn-CheckBox");
		playerGrid.addRow("Operating System:", 
				"The operating system and version that the Display uses", 
				osVersionLabel);
		playerGrid.addRow("Player Version:", 
				"The version of the Player software that the Display uses", 
				playerVersionLabel);
		playerGrid.addRow("Viewer Version:", 
				"The version of the Viewer running on the Display", 
				viewerVersionLabel);
//		playerGrid.addRow("Browser and Version:", 
//				"The browser and version that the Player software uses", 
//				chromiumVersionLabel);
		
		HorizontalPanel autoupgradePanel = new HorizontalPanel();

		autoupgradePanel.add(browserUpgradeModeWidget);
		autoupgradePanel.add(new SpacerWidget());
		autoupgradePanel.add(applyToAllButton);
		
		playerGrid.addRow("Player Browser:", 
				"Auto Upgrade the Browser (recommended), or hold at the current version, " +
				"or use the previously installed version, or manually install your own browser", 
				autoupgradePanel, "rdn-CheckBox");
		Label autoupgradeWarningLabel = new Label("Warning: Using any option other than " +
				"Auto Upgrade can result in Presentation Content failures.", true);
		
		playerGrid.addRow("", autoupgradeWarningLabel, "rdn-Validator");
		autoupgradeWarningRow = playerGrid.getRow();
		playerGrid.addRow("Recommended Browser:", recommendedChromiumLabel);
		playerGrid.addRow("Installed Browser:", chromiumVersionLabel);
//		bottomGrid.addRow("Cache Version:", cacheVersionLabel);
		playerGrid.addRow("Display Errors:", playerErrorList);
		
		mainPanel.add(formValidator);
		mainPanel.add(topGrid);
		mainPanel.add(wgAddress);
		mainPanel.add(new HTML("&nbsp;"));
		mainPanel.add(socialConnectorGrid);
		mainPanel.add(new HTML("&nbsp;"));
		mainPanel.add(playerGrid);
		
		initWidget(mainPanel);

		styleControls();
		
		initHandlers();
		initValidator();
//		resolutionWidget.setValidator(formValidator);
	}

	private void styleControls() {

	}

	private void initValidator() {
		// Add widgets to validator
		formValidator.addValidationElement(tbName, "Display Name", FormValidatorInfo.requiredFieldValidator);
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
//		Command cmdPreview= new Command() {
//			public void execute() {
//				doActionPreview();
//			}
//		};
		Command cmdCancel = new Command() {
			public void execute() {
				doActionCancel();
			}
		};

		actionsWidget.clearActions();
		actionsWidget.addAction("Save", cmdSave);
		actionsWidget.addAction("Delete", cmdDelete);
		//actionsWidget.addAction("Preview", cmdPreview);
		actionsWidget.addAction("Cancel", cmdCancel);
	}
	
	private void initHandlers() {
		cbUseCompanyAddress.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				wgAddress.setEnabled(!cbUseCompanyAddress.getValue());
			}
		});
		
		browserUpgradeModeWidget.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				showAutoupgradeWarning();
			}
		});

		applyToAllButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				doActionAutoupgradeApplyAll();
			}
		});
		
//		windowsLink.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				doActionInstallWindows();
//			}
//		});
	
//		linuxLink.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				doActionInstallLinux();
//			}
//		});
		
//		macLink.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				doActionInstallMac();
//			}
//		});
		
		Command cmdInstall = new Command() {
			public void execute() {
				doActionInstall();
			}
		};
		
		Command cmdRestart = new Command() {
			public void execute() {
				doActionRestart();
			}
		};
		
		Command cmdReboot = new Command() {
			public void execute() {
				doActionReboot();
			}
		};
		
		playerActions.addAction("Download Player", cmdInstall);
		playerActions.addAction(PLAYER_RESTART_TEXT, cmdRestart);
		playerActions.addAction(PLAYER_REBOOT_TEXT, cmdReboot);
	}

	public static DisplayManageWidget getInstance() {
		try {
			if (instance == null)
				instance = new DisplayManageWidget();
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
		if (!(displayId == null || displayId.isEmpty())) {
			loadDataRPC(displayId);
		}
		socialConnectorWidget.setDisplayId(displayId);
		tbName.setFocus(true);
	}
	
	private void clearData() {
		displayInfo = new DisplayInfo();
		bindData();
	}

	private void bindData() {
		if (displayInfo == null)
			return;

		tbName.setText(displayInfo.getName());
		wgStatus.setSelectedValue(displayInfo.getSubscriptionStatus());
//		resolutionWidget.setValue(displayInfo.getWidth(), displayInfo.getHeight());
		resolutionLabel.setText(displayInfo.getWidth() + "x" + displayInfo.getHeight());
		cbUseCompanyAddress.setValue(displayInfo.getUseCompanyAddress());
		//wgRestart.setRestartEnabled(displayInfo.getRestartEnabled());
		//wgRestart.setRestartTime(displayInfo.getRestartTime());
		//wgRestart.setEnabled(true);
		wgAddress.setEnabled(!cbUseCompanyAddress.getValue());

		wgAddress.loadData(displayInfo);
		scheduledRebootWidget.loadData(displayInfo.getRestartEnabled(), displayInfo.getRestartTime());
		monitoringCheckBox.setValue(displayInfo.isMonitoringEnabled());

		heartbeatWidget.setStatus(displayInfo.getDisplayStatus() /*, displayInfo.getBlockExpiryDate() */);
		lastUpdateLabel.setTooltip(RiseUtils.dateToString(displayInfo.getLastConnectionDate()),
				"If Online last time it was updated, if Offline the time when it disconnected");
		
//		playerStatusLabel.setStatus(displayInfo.getPlayerStatus());
//		topGrid.getRowFormatter().setVisible(playerStatusRow, displayInfo.getPlayerStatus() > 0);
		
		lbDisplayId.setText(displayInfo.getId());
		osVersionLabel.setVersion(displayInfo.getOsVersion());
		
		String playerVersion = "";
		playerVersion = RiseUtils.strIsNullOrEmpty(displayInfo.getPlayerName()) ? "" : (displayInfo.getPlayerName() + " ");
		playerVersion += RiseUtils.strIsNullOrEmpty(displayInfo.getPlayerVersion()) ? "" : " " + displayInfo.getPlayerVersion();
		playerVersionLabel.setText(playerVersion);
		viewerVersionLabel.setText(displayInfo.getViewerVersion());
				
		browserUpgradeModeWidget.setSelectedValue(displayInfo.getBrowserUpgradeMode());
		recommendedChromiumLabel.setText(displayInfo.getRecommendedBrowserVersion());
		String browserVersion = "";
		browserVersion = RiseUtils.strIsNullOrEmpty(displayInfo.getBrowserName()) ? "" : displayInfo.getBrowserName();
		browserVersion += RiseUtils.strIsNullOrEmpty(displayInfo.getChromiumVersion()) ? "" : " " + displayInfo.getChromiumVersion();
		chromiumVersionLabel.setText(browserVersion);
		showAutoupgradeWarning();
		
		cacheVersionLabel.setText(displayInfo.getCacheVersion());
		if (displayId != null && !displayId.isEmpty()) {
//			mainPanel.add(playerGrid);
			playerGrid.setVisible(true);
//			actionsWidget.setVisible(true, "Restart");
//			actionsWidget.setVisible(true, "Reboot");
		} else {
//			mainPanel.remove(playerGrid);
			playerGrid.setVisible(false);
//			actionsWidget.setVisible(false, "Restart");
//			actionsWidget.setVisible(false, "Reboot");
		}
		
		showInstallWarning();
		
		playerErrorList.initWidget(displayInfo.getId());

		wgLastModified.Initialize(displayInfo.getChangedBy(), displayInfo.getChangeDate());
		statusBox.clear();
	}

	private void saveData() {
		if (displayInfo == null)
			return;

		if (!formValidator.validate())
			return;
		
		displayInfo.setName(tbName.getText());
		displayInfo.setSubscriptionStatus(RiseUtils.strToInt(wgStatus.getSelectedValue(), DisplayStatus.ACTIVE));
//		displayInfo.setWidth(resolutionWidget.getWidth());
//		displayInfo.setHeight(resolutionWidget.getHeight());
		displayInfo.setUseCompanyAddress(cbUseCompanyAddress.getValue());
		displayInfo.setBrowserUpgradeMode(RiseUtils.strToInt(browserUpgradeModeWidget.getSelectedValue(), BrowserUpgradeMode.AUTO));
		displayInfo.setRestartEnabled(scheduledRebootWidget.getRestartEnabled());
		displayInfo.setRestartTime(scheduledRebootWidget.getRestartTime());
		displayInfo.setMonitoringEnabled(monitoringCheckBox.getValue());

		wgAddress.saveData(displayInfo);
		
		socialConnectorWidget.save();
		saveDataRPC(displayInfo);
	}

	private void deleteData() {
		deleteDataRPC(displayId);
	}
	
	private void loadDataRPC(String displayId) {
		actionsWidget.setEnabled(false);
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, StatusBoxWidget.LOADING);
		displayService.getDisplay(SelectedCompanyController.getInstance().getSelectedCompanyId(), displayId, new RpcGetDisplayCallBackHandler());
	}
	
	private void restartPlayerRPC() {
		actionsWidget.setEnabled(false);
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, "Sending Restart Command...");

		displayService.restartPlayer(SelectedCompanyController.getInstance().getSelectedCompanyId(), displayId, 
				new RpcPlayerCommandCallBackHandler());
	}
	
	private void rebootPlayerRPC() {
		actionsWidget.setEnabled(false);
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, "Sending Reboot Command...");
		
		displayService.rebootPlayer(SelectedCompanyController.getInstance().getSelectedCompanyId(), displayId, 
				new RpcPlayerCommandCallBackHandler());
	}

	private void saveDataRPC(DisplayInfo si) {
		//we need to clear items to prevent RPC from serializing it and sending to server
		actionsWidget.setEnabled(false);
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, StatusBoxWidget.SAVING);
		//save display only
		displayService.putDisplay(SelectedCompanyController.getInstance().getSelectedCompanyId(), si, 
				new RpcPutDisplayCallBackHandler());
	}

	private void deleteDataRPC(String displayId) {
		actionsWidget.setEnabled(false);
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, StatusBoxWidget.DELETING);
		displayService.deleteDisplay(SelectedCompanyController.getInstance().getSelectedCompanyId(), displayId, 
				new RpcDeleteDisplayCallBackHandler());
	}
	
	private void autoupgradeApplyAllRPC() {
		actionsWidget.setEnabled(false);
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, "Setting Upgrade Mode");
		displayService.putBrowserUpgradeMode(SelectedCompanyController.getInstance().getSelectedCompanyId(), 
				RiseUtils.strToInt(browserUpgradeModeWidget.getSelectedValue(), BrowserUpgradeMode.AUTO),
				osVersionLabel.getText(), 
				new RpcPlayerCommandCallBackHandler());
	}
	
	private void showAutoupgradeWarning() {
		boolean show = RiseUtils.strToInt(browserUpgradeModeWidget.getSelectedValue(), BrowserUpgradeMode.AUTO) 
				!= BrowserUpgradeMode.AUTO;
		
		playerGrid.getRowFormatter().setVisible(autoupgradeWarningRow, show);
		playerGrid.getRowFormatter().setVisible(autoupgradeWarningRow + 1, show);
	}
	
	private void doActionAutoupgradeApplyAll() {
		if (Window.confirm("This Player Browser selection will be applied to All Displays within this " +
				"Company Account. Are you sure you want to do this?")) {
			autoupgradeApplyAllRPC();
		}
	}

	private void doActionInstall() {
//		DisplayInstallWidget.getInstance().show();
		Window.open(ConfigurationController.getInstance().getConfiguration().getInstallerURL(), "_blank", "");
	}
	
	private void showInstallWarning() {
		boolean show = RiseUtils.strIsNullOrEmpty(playerVersionLabel.getText());
		
		playerActions.setEnabled(!show, PLAYER_RESTART_TEXT);
		playerActions.setEnabled(!show, PLAYER_REBOOT_TEXT);
		
		playerGrid.getRowFormatter().setVisible(installWarningRow, show);
		
		// Also gray out the Browser Autoupgrade options fields
		browserUpgradeModeWidget.setEnabled(!show);
		// Set upgrade mode to Auto just in case
		if (show) {
			browserUpgradeModeWidget.setSelectedValue(BrowserUpgradeMode.AUTO);
		}
		applyToAllButton.setVisible(!show);
	}
	
//	private void doActionInstallWindows() {
//		DisplayInstallWidget.getInstance().show(displayInfo, "Windows");
//	}
	
//	private void doActionInstallLinux() {
//		DisplayInstallWidget.getInstance().show(displayInfo, "Linux");
//	}
	
//	private void doActionInstallMac() {
//		DisplayInstallWidget.getInstance().show(displayInfo, "Mac");
//	}
	
	private void doActionRestart() {
		if (Window.confirm("The Rise Player on the Display's Computer will restart " +
				"and the currently Scheduled Content will be interrupted. Do you wish to proceed?")) {
			restartPlayerRPC();
		}
	}
	
	private void doActionReboot() {
		if (Window.confirm("The Display's Computer will be rebooted and the currently " +
				"Scheduled content will be interrupted. Do you wish to proceed?")) {
			rebootPlayerRPC();
		}
	}
	
	private void doActionSave() {
		saveData();
	}

	private void doActionDelete() {
		if (Window.confirm("Are you sure you want to delete this display?")) {
			deleteData();
		}
	}
	
//	private void doActionPreview() {
//		Window.open(Globals.VIEWER_URL + "Viewer.html?type=display&id=" + displayId, "_blank", "");
//	}

	private void doActionCancel() {
		UiEntryPoint.loadContentStatic(ContentId.DISPLAYS);
	}

	public void setToken(HistoryTokenInfo tokenInfo) {
		displayId = tokenInfo.getId();
	}

	//--------- RPC CLASSES ---------------//	
	class RpcGetDisplayCallBackHandler extends RiseAsyncCallback<DisplayInfo> {
		public void onFailure() {
			actionsWidget.setEnabled(true);
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(DisplayInfo result) {
			actionsWidget.setEnabled(true);
			if (result == null)
				statusBox.setStatus(StatusBoxWidget.Status.ERROR, "Error retrieving Display data. Please try again.");
			else {
				displayInfo = result;
				bindData();
			}
		}
	}

	class RpcPutDisplayCallBackHandler extends RiseAsyncCallback<RpcResultInfo> {
		public void onFailure() {
			actionsWidget.setEnabled(true);
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(RpcResultInfo result) {
			actionsWidget.setEnabled(true);
			if (result == null)
				statusBox.setStatus(StatusBoxWidget.Status.ERROR, "Error saving Display data. Please try again.");
			else {
				if (result != null) {
					// save Social Connector for a new display by display Id
					if (displayId == null || displayId.isEmpty()) {
						socialConnectorWidget.setDisplayId(result.getId());
						socialConnectorWidget.save();
					}
					
					//update ID
					displayId = result.getId();
					displayInfo.setId(displayId);
					bindData();
				}
			}
		}
	}

	class RpcDeleteDisplayCallBackHandler extends RiseAsyncCallback<RpcResultInfo> {
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
	
	class RpcPlayerCommandCallBackHandler extends RiseAsyncCallback<RpcResultInfo> {
		public void onFailure() {
			actionsWidget.setEnabled(true);
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(RpcResultInfo result) {
			actionsWidget.setEnabled(true);
			statusBox.clear();
		}
	}
}