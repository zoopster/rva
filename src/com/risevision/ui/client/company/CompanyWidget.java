// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.company;

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
import com.risevision.core.api.types.CompanyNetworkOperatorStatus;
import com.risevision.core.api.types.CompanyStatus;
import com.risevision.ui.client.UiEntryPoint;
import com.risevision.ui.client.common.ContentId;
import com.risevision.ui.client.common.controller.SelectedCompanyController;
import com.risevision.ui.client.common.controller.UserAccountController;
import com.risevision.ui.client.common.exception.RiseAsyncCallback;
import com.risevision.ui.client.common.exception.ServiceFailedException;
import com.risevision.ui.client.common.info.AlertsInfo;
import com.risevision.ui.client.common.info.CompanyInfo;
import com.risevision.ui.client.common.info.FormValidatorInfo;
import com.risevision.ui.client.common.info.HistoryTokenInfo;
import com.risevision.ui.client.common.service.CompanyService;
import com.risevision.ui.client.common.service.CompanyServiceAsync;
import com.risevision.ui.client.common.widgets.ActionsWidget;
import com.risevision.ui.client.common.widgets.ConfirmDialogWidget;
import com.risevision.ui.client.common.widgets.CountryWidget;
import com.risevision.ui.client.common.widgets.FormValidatorWidget;
import com.risevision.ui.client.common.widgets.LastModifiedWidget;
import com.risevision.ui.client.common.widgets.RegionWidget;
import com.risevision.ui.client.common.widgets.SpacerWidget;
import com.risevision.ui.client.common.widgets.StatusBoxWidget;
import com.risevision.ui.client.common.widgets.TimeZoneWidget;
import com.risevision.ui.client.common.widgets.alerts.AlertsManageWidget;
import com.risevision.ui.client.common.widgets.demographics.DemographicsWidget;
import com.risevision.ui.client.common.widgets.grid.FormGridWidget;
import com.risevision.ui.client.common.widgets.socialConnector.SocialConnectorCompanyWidget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class CompanyWidget extends Composite {
	private static CompanyWidget instance;
	private CompanyInfo companyInfo;
	private boolean isBookmarkSet = false;
	//RPC
	private final CompanyServiceAsync companyService = GWT.create(CompanyService.class);
	private RpcCallBackHandler rpcCallBackHandler = new RpcCallBackHandler();
	//UI pieces
	private ActionsWidget actionsWidget = ActionsWidget.getInstance();
	private VerticalPanel mainPanel = new VerticalPanel();
	private FormValidatorWidget formValidator = new FormValidatorWidget();
	private StatusBoxWidget statusBox = StatusBoxWidget.getInstance();
	private FormGridWidget topGrid = new FormGridWidget(15, 2);
	private FormGridWidget bottomGrid = new FormGridWidget(2, 2);
	//Customer fields
//	private Label lbCompanyId = new Label();
	private Label nameLabel = new Label();
	private HorizontalPanel addCompanyPanel = new HorizontalPanel();
	private Anchor addCompanyLink = new Anchor("Add a Company");
	private Anchor moveCompanyLink = new Anchor("Move a Company");
	private Anchor switchCompanyLink = new Anchor("Switch to a Company");
	private TextBox tbName = new TextBox();
	private TextBox tbStreet = new TextBox();
	private TextBox tbUnit = new TextBox();
	private TextBox tbCity = new TextBox();
	private CountryWidget wgCountry = new CountryWidget();
	private RegionWidget wgRegion = new RegionWidget();	
	private TextBox tbPostalCode = new TextBox();
	private TimeZoneWidget wgTimeZone = new TimeZoneWidget();
	private TextBox tbTelephone = new TextBox();
	private TextBox tbFax = new TextBox();
	private TextBox monitoringEmailsTextBox = new TextBox(); 
	//PNO fields
//	private OperatorWidget operatorWidget = OperatorWidget.getInstance();	
	//private TextBox tbAccount = new TextBox();
//	private PnoStatusWidget wgPnoStatus = new PnoStatusWidget(); //-1=Cancelled; 0=No; 1=Yes
	private CompanyStatusWidget wgCompanyStatus = CompanyStatusWidget.getInstance();
	
	private AuthKeyWidget authKeyWidget = new AuthKeyWidget();
	private ClaimIdWidget claimIdWidget = new ClaimIdWidget();
	private SocialConnectorCompanyWidget socialConnectorGrid = new SocialConnectorCompanyWidget();
	
	private CheckBox allowAlertsCheckBox = new CheckBox();
	private AlertsManageWidget alertsWidget = new AlertsManageWidget();
	private DemographicsWidget demographicsWidget = new DemographicsWidget();
	
//	private CheckBox mediaLibraryCheckBox = new CheckBox();
	//last modified
	private LastModifiedWidget wgLastModified = LastModifiedWidget.getInstance();

	public CompanyWidget() {
		initWidget(mainPanel);
		mainPanel.add(formValidator);
		
		VerticalPanel namePanel = new VerticalPanel();
		namePanel.add(nameLabel);
//		namePanel.setCellWidth(nameLabel, "144px");
//		namePanel.add(new SpacerWidget());
//		namePanel.add(new HTML("-"));
//		namePanel.add(new SpacerWidget());
//		namePanel.add(wgPnoStatus);
		
		HorizontalPanel linksPanel = new HorizontalPanel();
		addCompanyPanel.add(addCompanyLink);
		addCompanyPanel.add(new SpacerWidget());
		addCompanyPanel.add(new HTML("|"));
		addCompanyPanel.add(new SpacerWidget());
		addCompanyPanel.add(moveCompanyLink);
		addCompanyPanel.add(new SpacerWidget());
		addCompanyPanel.add(new HTML("|"));
		addCompanyPanel.add(new SpacerWidget());
		linksPanel.add(addCompanyPanel);
		linksPanel.add(switchCompanyLink);
		
		namePanel.add(linksPanel);
		namePanel.setCellHeight(linksPanel, "24px");
		
		mainPanel.add(namePanel);
		mainPanel.add(topGrid);
		mainPanel.add(alertsWidget);
		mainPanel.add(bottomGrid);
//		mainPanel.add(demographicsWidget);
		
		// add widgets
//		mainGrid.addRow("", linksPanel, null);
//		gridAdd("", switchCompanyLink, null);
//		mainGrid.setWidget(0, 0, nameLabel);
		
//		if (userCanEditPnoSettings()) {
//			gridAdd("", wgPnoStatus, "rdn-ListBoxShort");
//		}
		
		//mainGrid.gridAdd("ID:", lbCompanyId, "rdn-TextBoxLong");
		topGrid.addRow("Name*:", tbName, "rdn-TextBoxLong");
		topGrid.addRow("Street:", tbStreet, "rdn-TextBoxLong");
		topGrid.addRow("Unit:", tbUnit, "rdn-TextBoxMedium");
		topGrid.addRow("City:", tbCity, "rdn-TextBoxMedium");
		topGrid.addRow("Country:", wgCountry, "rdn-TextBoxMedium");
		
		wgCountry.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				wgRegion.setCountry(wgCountry.getSelectedValue());
			}
		});
		
		topGrid.addRow("State/Province/Region:", wgRegion); //Region is composite widget, no need to style 
		topGrid.addRow("Postal Code:", tbPostalCode, "rdn-TextBoxMedium");
		topGrid.addRow("Time Zone:", wgTimeZone, "rdn-ListBoxLong");
		topGrid.addRow("Telephone:", tbTelephone, "rdn-TextBoxMedium");
		topGrid.addRow("Fax:", tbFax, "rdn-TextBoxMedium");
		
		topGrid.addRow("Status:", "If set to Inactive Users cannot login and Displays show demonstration content", 
				wgCompanyStatus, null);
		topGrid.addRow("Authentication Key:", "The key used by Applications and Gadgets to access your data. Keep it secret!", 
				authKeyWidget, null);
		topGrid.addRow("Claim ID:", "The key used by the Player to automatically register Displays to your Company.", 
				claimIdWidget, null);
		topGrid.addRow("Monitoring Emails:", "Comma separated list of Email Addresses who will receive an Email if a Display fails", 
				monitoringEmailsTextBox, "rdn-TextBoxLong");
		//mainGrid.gridAdd("Display Throttle:", tbDisplayThrottle, "rdn-TextBoxShort");

		// add PNO fields; available to the System Administrator; Rise cannot edit their own PNO settings
//		if (userCanEditPnoSettings()) {
//			gridAddHTML(0, "&nbsp"); //empty row
//			gridAddHTML(0, "<div class='rdn-h1'>Network Settings</div>");
			
//			topGrid.addRow("Operator:", "The Company under which this Company operates", 
//					operatorWidget, null);
//			gridAdd("Network Operator:", wgPnoStatus, "rdn-ListBoxShort");
			//gridAdd("Network Operator Reference:", tbAccount, "rdn-TextBoxLong");
//		}
		
//		if (userCanEditPnoSettings() && SelectedCompanyController.getInstance().getUserCompany().isRise()) {
//			mainGrid.addRow("Media Library:", mediaLibraryCheckBox, "rdn-CheckBox");
//		}
		
		topGrid.addRow("Allow Alerts:", "Allow Alerts to be shown on your Displays", 
				allowAlertsCheckBox, "rdn-CheckBox");

		bottomGrid.addRow("Social Connections:", socialConnectorGrid, null);
		bottomGrid.addRow("Profile:", demographicsWidget);
		
		topGrid.resizeGrid();
				
		styleControls();
		initValidator();
		initLinks();
	}

	private void styleControls() {
		nameLabel.addStyleName("rdn-h1");
	}

	private void initValidator() {
		// Add widgets to validator
		formValidator.addValidationElement(tbName, "Comany Name", FormValidatorInfo.requiredFieldValidator);
	}
	
	private void initLinks() {
		addCompanyLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				doActionAddNew();
			}
		});
		
		moveCompanyLink.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				CompanyMoveWidget.getInstance().show();
			}
		});
		
		switchCompanyLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				doActionChange();
			}
		});
		
		allowAlertsCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				showAlerts();
			}
		});
	}
	
	private void showAlerts() {
		alertsWidget.setVisible(allowAlertsCheckBox.getValue());
	}

	private void initActions() {
//		Command cmdAdd = new Command() {
//			public void execute() {
//				doActionAddNew();
//			}
//		};	
		Command cmdDelete = new Command() {
			public void execute() {
				doActionDelete();
			}
		};	
		Command cmdSave = new Command() {
			public void execute() {
				doActionSave();
			}
		};		
		Command cmdCancel = new Command() {
			public void execute() {
				doActionCancel();
			}
		};	
		
		actionsWidget.clearActions();
//		actionsWidget.addAction("Add", cmdAdd);
		actionsWidget.addAction("Save", cmdSave);
		actionsWidget.addAction("Delete", cmdDelete);
		actionsWidget.addAction("Cancel", cmdCancel);
	}
	
//	private boolean userCanEditPnoSettings() {
//		boolean result = false;
//		try {
//			result = UserAccountController.getInstance().userHasRoleSystemAdministrator()
//					&& SelectedCompanyController.getInstance().getUserCompany().isRise()
//					;
//		} catch (Exception e) {}
//		
//		return result;
//	}

	private boolean selectedCompanyIsRise() {
		boolean result = false;
		try {
			result = SelectedCompanyController.getInstance().getSelectedCompany().isRise() && companyInfo.getId() != null;
		} catch (Exception e) {}
		
		return result;
	}

	public static CompanyWidget getInstance() {
		try {
			if (instance == null)
				instance = new CompanyWidget();
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
		loadDataRPC(SelectedCompanyController.getInstance().getSelectedCompanyId());
		
		if (!isBookmarkSet) {
			tbName.setFocus(true);
		}
		else {
			demographicsWidget.setFocus(true);
		}
	}
	
	private void clearData() {
		companyInfo = new CompanyInfo();
		bindData();
	}

	private void bindData() {
		if (companyInfo == null)
			return;
		
		if (companyInfo.getId() != null && !companyInfo.getId().isEmpty() 
				&& !SelectedCompanyController.getInstance().getSelectedCompanyId().equals(companyInfo.getId())) {
			SelectedCompanyController.getInstance().setSelectedCompany(companyInfo);
			SelectedCompanyController.getInstance().bindData();
		}
		
//		lbCompanyId.setText(companyInfo.getId());
		nameLabel.setText(companyInfo.getName());
		tbName.setText(companyInfo.getName());
		//address
		wgCountry.setSelectedValue(companyInfo.getCountry());
		wgRegion.setCountry(companyInfo.getCountry());
		wgRegion.setRegion(companyInfo.getProvince());
		wgTimeZone.setSelectedValue(companyInfo.getTimeZone());
		tbStreet.setText(companyInfo.getStreet());
		tbUnit.setText(companyInfo.getUnit());
		tbCity.setText(companyInfo.getCity());
		tbPostalCode.setText(companyInfo.getPostalCode());
		tbTelephone.setText(companyInfo.getTelephone());
		tbFax.setText(companyInfo.getFax());
		
		monitoringEmailsTextBox.setText(RiseUtils.listToString(companyInfo.getDisplayMonitorEmailRecipients(), ","));

		//tbAccount.setText(companyInfo.getReference());

//		wgPnoStatus.setStatus(companyInfo.getPnoStatus());
		wgCompanyStatus.setValue(companyInfo.getCustomerStatus(), companyInfo.getCustomerStatusChangeDate());
		wgLastModified.Initialize(companyInfo.getChangedBy(), companyInfo.getChangeDate());
		
		socialConnectorGrid.initWidget(companyInfo.getId());
		
		authKeyWidget.initWidget(companyInfo.getAuthKey());
		claimIdWidget.initWidget(companyInfo.getClaimId());
		
		AlertsInfo alertsSettings = companyInfo.getAlertsSettings();
		
		allowAlertsCheckBox.setValue(alertsSettings.isAllowAlerts());
		alertsWidget.initWidget(alertsSettings);
		showAlerts();
		
		demographicsWidget.initWidget(companyInfo.getDemographicsInfo());
		demographicsWidget.initValidator(formValidator, UserAccountController.getInstance().userHasRoleUserAdministrator()
				&& SelectedCompanyController.getInstance().isUserCompanySelected()
				&& RiseUtils.diffDaysFromNow(companyInfo.getCreationDate()) > 10);
		
//		if (userCanEditPnoSettings() && ((companyInfo.getId() == null && companyInfo.getParentId() != null) 
//				|| !SelectedCompanyController.getInstance().isUserCompanySelected())) {
//			operatorWidget.loadPnoCompanies(companyInfo);
//		}
		
//		if (userCanEditPnoSettings() && SelectedCompanyController.getInstance().getUserCompany().isRise()) {
//			EnabledFeaturesInfo features = new EnabledFeaturesInfo(companyInfo.getEnabledFeaturesJson());
//			mediaLibraryCheckBox.setValue(features.isFeatureEnabled(EnabledFeaturesInfo.FEATURE_MEDIA_LIBRARY));
//		}
		
		applySecurity();
	}
	
	private void applySecurity() {
		// Kick user off the page if they're not in role to see it; should not happen as user should
		// not have access to this page in the first place.
		boolean userIsUserAdmin = UserAccountController.getInstance().userHasRoleUserAdministrator();
		if (!userIsUserAdmin) {
			doActionCancel();
		}
		
//		wgPnoStatus.setEditable(userCanEditPnoSettings());
		
//		if (SelectedCompanyController.getInstance().getSelectedCompany().isPno() 
		if (companyInfo.isPno()
				&& companyInfo.getId() != null && !companyInfo.getId().isEmpty()) {
			addCompanyPanel.setVisible(true);
//			actionsWidget.setVisible(true, "Add");
		}
		else {
			addCompanyPanel.setVisible(false);
//			actionsWidget.setVisible(false, "Add");
		}
		
		switchCompanyLink.setVisible(SelectedCompanyController.getInstance().getUserCompany() != null && SelectedCompanyController.getInstance().getUserCompany().isPno());
		
		// company cannot change it's own Status
		wgCompanyStatus.setEnabled(!SelectedCompanyController.getInstance().isUserCompanySelected() || companyInfo.getId() == null);
		// or delete themselves
		actionsWidget.setVisible(!selectedCompanyIsRise() || /* !isCompanyOwnProfile() || */ companyInfo.getId() == null, "Delete");
		// assign Parent should not be visible for Rise
//		operatorWidget.setVisible(!selectedCompanyIsRise() && (companyInfo.getId() == null 
//				|| !SelectedCompanyController.getInstance().isUserCompanySelected()));
	}

	private void saveData() {
		if (companyInfo == null)
			return;

		if (!formValidator.validate())
			return;
		
		//companyInfo.setId(lbCompanyId.getText()); //ID for new company will be generated on server (web app, not core API)
		
		companyInfo.setName(tbName.getText());
		//address
		companyInfo.setStreet(tbStreet.getText());
		companyInfo.setUnit(tbUnit.getText());
		companyInfo.setCity(tbCity.getText());

		companyInfo.setCountry(wgCountry.getSelectedValue());
		companyInfo.setProvince(wgRegion.getRegion());
		companyInfo.setPostalCode(tbPostalCode.getText());
		companyInfo.setTimeZone(wgTimeZone.getSelectedValue());

		companyInfo.setTelephone(tbTelephone.getText());
		companyInfo.setFax(tbFax.getText());
		
		companyInfo.setDisplayMonitorEmailRecipients(RiseUtils.stringToList(monitoringEmailsTextBox.getText(), ","));
		
		//companyInfo.setReference(tbAccount.getText());
		companyInfo.setCustomerStatus(RiseUtils.strToInt(wgCompanyStatus.getValue(), CompanyStatus.ACTIVE));

		AlertsInfo alertsSettings = companyInfo.getAlertsSettings();
		if (alertsSettings == null) {
			alertsSettings = new AlertsInfo();
			companyInfo.setAlertsSettings(alertsSettings);
		}
		
		companyInfo.setAuthKey(authKeyWidget.getAuthKey());
		companyInfo.setClaimId(claimIdWidget.getClaimId());
		
		alertsWidget.saveData();
		alertsSettings.setAllowAlerts(alertsSettings.isAllowAlerts() && allowAlertsCheckBox.getValue());
		
//		if (userCanEditPnoSettings() && (!selectedCompanyIsRise())) {
			// selectedCompanyIsRise && isCompanyOwnProfile serve the same purpose if the Company is Rise
//			if (!SelectedCompanyController.getInstance().isUserCompanySelected()) {
//				companyInfo.setParentId(operatorWidget.getOperatorId());
//			}
			
//			companyInfo.setPnoStatus(wgPnoStatus.getStatus());
//		} 
		
//		if (userCanEditPnoSettings() && SelectedCompanyController.getInstance().getUserCompany().isRise()) {
//			if (mediaLibraryCheckBox.getValue()) {
//				EnabledFeaturesInfo features = new EnabledFeaturesInfo();
//				features.addEnabledFeature(EnabledFeaturesInfo.FEATURE_MEDIA_LIBRARY);
//				companyInfo.setEnabledFeaturesJson(features.getEnabledFeaturesString());
//			}
//			else {
//				companyInfo.setEnabledFeaturesJson("");
//			}
//		}
		
		if (selectedCompanyIsRise()) {
			companyInfo.setParentId("");
			companyInfo.setPnoStatus(CompanyNetworkOperatorStatus.SUBSCRIBED);
			companyInfo.setCustomerStatus(CompanyStatus.ACTIVE);
		}
		
		socialConnectorGrid.save();
		
		demographicsWidget.saveData();

		saveDataRPC(companyInfo);
	}

	private void loadDataRPC(String companyId) {
		actionsWidget.setEnabled(false);
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, StatusBoxWidget.LOADING);
		companyService.getCompany(companyId, rpcCallBackHandler);
	}

	private void saveDataRPC(CompanyInfo ci) {
		actionsWidget.setEnabled(false);
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, StatusBoxWidget.SAVING);
		companyService.saveCompany(ci, rpcCallBackHandler);
	}

	private void doActionAddNew() {
		companyInfo = new CompanyInfo();
		
//		if (UserAccountController.getInstance().getUserInfo() != null) {
//			companyInfo.setParentId(UserAccountController.getInstance().getUserInfo().getCompany());
//		}

		companyInfo.setParentId(SelectedCompanyController.getInstance().getSelectedCompanyId());

		bindData();
	}
	
	private void doActionChange() {
		CompaniesWidget.getInstance().show();
	}

	private void doActionSave() {
//		verifyPNOStatus();
		saveData();
	}
	
//	private void verifyPNOStatus() {
//		// If PNO status is about to be cancelled, notify user
//		if (companyInfo.getPnoStatus() == CompanyNetworkOperatorStatus.SUBSCRIBED 
//				&& wgPnoStatus.getStatus() == CompanyNetworkOperatorStatus.NO) {
//			ConfirmDialogWidget.getInstance().show(new Command() {
//				@Override
//				public void execute() {
//					saveData();
//				}
//			}, "Cancel Operator Subscription Confirmation", 
//			"Any Sub Companies under this Company will no longer be accessible. This cannot be undone.", "CONFIRM");
//		}
//		else {
//			saveData();
//		}
//	}
	
	private void doActionDelete() {		
		String deleteMessage = companyInfo.getPnoStatus() == CompanyNetworkOperatorStatus.SUBSCRIBED ? 
				"Any Sub Companies under this Company will no longer be accessible. This cannot be undone." : "";
		ConfirmDialogWidget.getInstance().show(new Command() {
			@Override
			public void execute() {
				deleteCompany();
			}
		}, "Delete Company Confirmation", deleteMessage, "DELETE");
	}
	
	private void deleteCompany() {
//		if (Window.confirm("Are you sure you want to delete this company?")) {
			companyService.deleteCompany(SelectedCompanyController.getInstance().getSelectedCompanyId(), new RpcDeleteCallBackHandler());
//		}
	}
	
	private void doActionCancel() {
		statusBox.clear();
		
		UiEntryPoint.loadContentStatic(ContentId.HOME);
	}
	
	public void setToken(HistoryTokenInfo tokenInfo) {
		isBookmarkSet = !RiseUtils.strIsNullOrEmpty(tokenInfo.getBookmark());
	}

	class RpcCallBackHandler extends RiseAsyncCallback<CompanyInfo> {
		public void onFailure() {
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
			actionsWidget.setEnabled(true);
		}

		public void onSuccess(CompanyInfo result) {
			companyInfo = result;
			bindData();
			actionsWidget.setEnabled(true);
			statusBox.clear();
		}
	}
	
	class RpcDeleteCallBackHandler extends RiseAsyncCallback<Void> {
		public void onFailure() {
			if (caught instanceof ServiceFailedException && ((ServiceFailedException)caught).getReason() == ServiceFailedException.CONFLICT) {
				// Delete failed (409 error)
				statusBox.setStatus(StatusBoxWidget.Status.ERROR, "Company delete failed; please ensure all subcompanies are deleted!");
			}
			else {
				statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
			}
		}

		public void onSuccess(Void result) {
//			if (result.getErrorMessage() != null && !result.getErrorMessage().isEmpty()) {
//				statusBox.setStatus(Status.ERROR, result.getErrorMessage());
//			}
//			else {
				if (SelectedCompanyController.getInstance().getSelectedCompanyId().equals(UserAccountController.getInstance().getUserInfo().getCompany())) {
					Window.Location.replace("");
				}
				else {
					SelectedCompanyController.getInstance().reset();
				}
//			}
		}
	}
	
}