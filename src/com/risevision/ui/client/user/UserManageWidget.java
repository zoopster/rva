// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.user;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.core.api.settings.CompanySetting;
import com.risevision.core.api.types.UserRole;
import com.risevision.core.api.types.UserStatus;
import com.risevision.ui.client.UiEntryPoint;
import com.risevision.ui.client.common.ContentId;
import com.risevision.ui.client.common.controller.SelectedCompanyController;
import com.risevision.ui.client.common.controller.UserAccountController;
import com.risevision.ui.client.common.exception.RiseAsyncCallback;
import com.risevision.ui.client.common.info.CompanyInfo;
import com.risevision.ui.client.common.info.FormValidatorInfo;
import com.risevision.ui.client.common.info.HistoryTokenInfo;
import com.risevision.ui.client.common.info.RpcResultInfo;
import com.risevision.ui.client.common.info.UserInfo;
import com.risevision.ui.client.common.service.UserService;
import com.risevision.ui.client.common.service.UserServiceAsync;
import com.risevision.ui.client.common.widgets.ActionsWidget;
import com.risevision.ui.client.common.widgets.ConfirmDialogWidget;
import com.risevision.ui.client.common.widgets.FormValidatorWidget;
import com.risevision.ui.client.common.widgets.LastModifiedWidget;
import com.risevision.ui.client.common.widgets.RolesWidget;
import com.risevision.ui.client.common.widgets.StatusBoxWidget;
import com.risevision.ui.client.common.widgets.grid.FormGridWidget;

public class UserManageWidget extends Composite {
	private UserServiceAsync userService = (UserServiceAsync) GWT.create(UserService.class); 
//	private CompanyServiceAsync companyService = GWT.create(CompanyService.class);

	private RpcGetUserCallBackHandler rpcGetUserCallBackHandler = new RpcGetUserCallBackHandler();
	private RpcPutUserCallBackHandler rpcPutUserCallBackHandler = new RpcPutUserCallBackHandler();
	private RpcDeleteUserCallBackHandler rpcDeleteUserCallBackHandler = new RpcDeleteUserCallBackHandler();
	
	private static UserManageWidget instance;
	private UserInfo userInfo;
	private String userId;
	private boolean usersOwnProfile;
	
	private FormValidatorWidget formValidator = new FormValidatorWidget();
	private ActionsWidget actionsWidget = ActionsWidget.getInstance();
	
	private VerticalPanel mainPanel = new VerticalPanel();
	private FormGridWidget mainGrid = new FormGridWidget(10, 2);

//	private HorizontalPanel usernamePanel = new HorizontalPanel();
//	private HTML usernameInfoLabel = new HTML("&nbsp;*Google Account Email Address used to sign in");
	private TextBox usernameTextBox = new TextBox();	
	private TextBox firstNameTextBox = new TextBox();
	private TextBox lastNameTextBox = new TextBox();
	private TextBox telephoneTextBox = new TextBox();
	private TextBox contactEmailTextBox = new TextBox();
	private CheckBox mailSyncCheckBox = new CheckBox();
	private int showMailSyncRow;
	private UserStatusWidget statusWidget = UserStatusWidget.getInstance();
	
	//roles section
	private RolesWidget rolesWidget = new RolesWidget();
	
	private CheckBox showTutorialCheckBox = new CheckBox();
//	private int showTutorialRow;
	private Label lastLoginDateLabel = new Label();
	
	private LastModifiedWidget lastModified = LastModifiedWidget.getInstance();
	private StatusBoxWidget statusBox = StatusBoxWidget.getInstance();
	
	public void setToken(HistoryTokenInfo tokenInfo) {
		userId = tokenInfo.getId();
	}

	public static UserManageWidget getInstance() {
		try {

			if (instance == null)
				instance = new UserManageWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}
	
	public UserManageWidget() {		
		initWidget(mainPanel);
		mainPanel.add(formValidator);
		mainPanel.add(mainGrid);

		styleControls();
		
//		usernameTextBox.setStyleName("rdn-TextBoxMedium");
//		usernamePanel.add(usernameTextBox);
//		usernamePanel.add(usernameInfoLabel);
		mainGrid.addRow("Username*:", 
				"Google Account Email Address used to sign in", 
				usernameTextBox, "rdn-TextBoxMedium");
		
		mainGrid.addRow("First Name:", firstNameTextBox, "rdn-TextBoxMedium");
		mainGrid.addRow("Last Name:", lastNameTextBox, "rdn-TextBoxMedium");
		mainGrid.addRow("Telephone:", telephoneTextBox, "rdn-TextBoxMedium");
		mainGrid.addRow("Email*:", 
				"An Email address we can use to reach you at", 
				contactEmailTextBox, "rdn-TextBoxMedium");
		mainGrid.addRow("Subscribe to Email updates:", mailSyncCheckBox, "rdn-CheckBox"); 
		showMailSyncRow = mainGrid.getRow();
		mainGrid.addRow("Status:", statusWidget, "rdn-ListBoxShort");
		
		mainGrid.addRow("Roles:", rolesWidget);
		mainGrid.addRow("Show Tutorial:", showTutorialCheckBox, "rdn-CheckBox");
//		showTutorialRow = mainGrid.getRow();
		//mainGrid.addRow("Notifications:", notificationsWidget);
		mainGrid.addRow("Last Login:", lastLoginDateLabel);
			
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
		formValidator.addValidationElement(usernameTextBox, "Username", FormValidatorInfo.requiredFieldValidator);
		formValidator.addValidationElement(usernameTextBox, "Username", FormValidatorInfo.emailValidator);		
		formValidator.addValidationElement(contactEmailTextBox, "Email", FormValidatorInfo.requiredFieldValidator);
		formValidator.addValidationElement(contactEmailTextBox, "Email", FormValidatorInfo.emailValidator);
//		formValidator.addValidationElement(telephoneTextBox, "Telephone", FormValidatorInfo.phoneNumberValidator);
	}

	protected void onLoad() {
		super.onLoad();

		initActions();
		clearData();
		
		applyPNOSettings();
		
		if (userId != null && !userId.isEmpty())
			loadDataRPC();
	}
	
	private void clearData() {
		userInfo = new UserInfo();
		bindData();
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
		
		Command cmdCancel = new Command() {
			public void execute() {
				doActionCancel();
			}			
		};
	
		actionsWidget.addAction("Save", cmdSave);
		actionsWidget.addAction("Delete", cmdDelete);
		actionsWidget.addAction("Cancel", cmdCancel);
		
	}

	private void doActionSave(){
		if (!formValidator.validate())
			return;
		
		UserInfo user = new UserInfo();
		
		if (userId != null && !userId.isEmpty()){
			user.setId(userId);
		}
		user.setCompany(SelectedCompanyController.getInstance().getSelectedCompanyId());
		
		user.setUserName(usernameTextBox.getText());
		user.setFirstName(firstNameTextBox.getText());
		user.setLastName(lastNameTextBox.getText());
		user.setTelephone(telephoneTextBox.getText());
		user.setEmail(contactEmailTextBox.getText());
		user.setMailSyncEnabled(mailSyncCheckBox.getValue());
		
		user.setStatus(RiseUtils.strToInt(statusWidget.getSelectedValue(), UserStatus.ACTIVE));
		
		user.setRoles(rolesWidget.getRoles());
		user.setShowTutorial(showTutorialCheckBox.getValue());
		
		saveDataRPC(user);
	}
	
	private void saveDataRPC(UserInfo user){
		actionsWidget.setEnabled(false);
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, StatusBoxWidget.SAVING);
		
		userService.putUser(SelectedCompanyController.getInstance().getSelectedCompanyId(), user, rpcPutUserCallBackHandler);
	}
	
	private void doActionDelete(){
		ConfirmDialogWidget.getInstance().show(new Command() {
			@Override
			public void execute() {
				deleteUser();
			}
		}, "Delete User Confirmation", "", "DELETE");
	}
	
	private void deleteUser() {
//		if (Window.confirm("Are you sure you want to delete this user?")) {
			actionsWidget.setEnabled(false);
			
			if (userId == null || userId.isEmpty()){
				// Show the error message to the user
				statusBox.setStatus(StatusBoxWidget.Status.ERROR, "Error: Cannot delete user");
				return;
			}
		
			userService.deleteUser(SelectedCompanyController.getInstance().getSelectedCompanyId(), userId, rpcDeleteUserCallBackHandler);
//		}
	}
	
	private void doActionCancel(){
		UiEntryPoint.loadContentStatic(ContentId.USERS);
	}
	
	private void loadDataRPC() {	
		actionsWidget.setEnabled(false);
		CompanyInfo company = SelectedCompanyController.getInstance().getSelectedCompany();
//		applyPNOSettings(company);

		statusBox.setStatus(StatusBoxWidget.Status.WARNING, StatusBoxWidget.LOADING);
		userService.getUser(company.getId(), userId, rpcGetUserCallBackHandler);	
		
//		companyService.getPnoSettings(companyId, new RpcGetPNOSettingsCallBackHandler());
	}

	private void bindData(){
		if (userInfo == null)
			return;

		usersOwnProfile = UserAccountController.getInstance().getUserInfo().getId().equals(userInfo.getId());
		
		usernameTextBox.setText(userInfo.getUserName());
		if (userInfo.getLastLogin() == null) usernameTextBox.setFocus(true);
		else firstNameTextBox.setFocus(true);
		usernameTextBox.setReadOnly(userInfo.getLastLogin() != null);
		firstNameTextBox.setText(userInfo.getFirstName());
		lastNameTextBox.setText(userInfo.getLastName());
		telephoneTextBox.setText(userInfo.getTelephone());
		contactEmailTextBox.setText(userInfo.getEmail());
		mailSyncCheckBox.setValue(userInfo.isMailSyncEnabled());
		
		statusWidget.setSelectedValue(userInfo.getStatus());
		
		rolesWidget.setRoles(userInfo.getRoles());
		
		showTutorialCheckBox.setValue(userInfo.isShowTutorial());

		if (userInfo.getLastLogin() != null)
			lastLoginDateLabel.setText(RiseUtils.dateToString(userInfo.getLastLogin()));
		else
			lastLoginDateLabel.setText("N/A");	
		
		lastModified.Initialize(userInfo.getChangedBy(), userInfo.getChangedDate());
		formValidator.clear();
		
		applySecurity();
		statusBox.clear();
	}
	
	private void applySecurity() {
		boolean userIsUserAdmin = UserAccountController.getInstance().userHasRoleUserAdministrator();
		rolesWidget.setEnabled(userIsUserAdmin);
		if (userIsUserAdmin)
			rolesWidget.enableRole(UserRole.USER_ADMINISTRATOR, !usersOwnProfile);
		
		boolean companyIsRise = SelectedCompanyController.getInstance().getUserCompany().isRise() 
				&& SelectedCompanyController.getInstance().getSelectedCompany().isRise();
		
		rolesWidget.showRole(UserRole.BILLING_ADMINISTRATOR, companyIsRise);
		rolesWidget.enableRole(UserRole.BILLING_ADMINISTRATOR, userIsUserAdmin);
		
		rolesWidget.showRole(UserRole.SYSTEM_ADMINISTRATOR, companyIsRise);
		rolesWidget.enableRole(UserRole.SYSTEM_ADMINISTRATOR, userIsUserAdmin);
		
		statusWidget.setEnabled(userIsUserAdmin && !usersOwnProfile);
		
//		actionsWidget.setEnabled(!usersOwnProfile, "Delete");
	}
	
	private void applyPNOSettings() {
//		if (result.getSettings() != null && result.getSettings().containsKey(ManageSettingsInfo.SHOW_TUTORIAL)) {
//			// show tutorial checkbox only if PNO enabled the tutorial
//			mainGrid.getRowFormatter().setVisible(showTutorialRow, 
//					!"false".equals(result.getSettings().get(ManageSettingsInfo.SHOW_TUTORIAL)));
//		}
		
		CompanyInfo company = SelectedCompanyController.getInstance().getSelectedCompany();

		mainGrid.getRowFormatter().setVisible(showMailSyncRow, 
				company.getParentSettings() != null && 
				company.getParentSettings().containsKey(CompanySetting.MAIL_SYNC_ENABLED) &&
				!"false".equals(company.getParentSettings().get(CompanySetting.MAIL_SYNC_ENABLED)));
		
	}

	//--------- RPC CLASSES ---------------//
	class RpcGetUserCallBackHandler extends RiseAsyncCallback<UserInfo> {

		public void onFailure() {
			actionsWidget.setEnabled(true);
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(UserInfo result) {
			actionsWidget.setEnabled(true);
			if (result == null)
				statusBox.setStatus(StatusBoxWidget.Status.ERROR, "Error retrieving User data. Please try again.");
			else {
				userInfo = result;
				bindData();
			}
		}
	}

	class RpcPutUserCallBackHandler extends RiseAsyncCallback<RpcResultInfo> {

		public void onFailure() {
			actionsWidget.setEnabled(true);
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}
		
		public void onSuccess(RpcResultInfo result) {
			actionsWidget.setEnabled(true);
			if (result == null)
				statusBox.setStatus(StatusBoxWidget.Status.ERROR, "Error saving User data. Please try again.");
			else {
				if (result.getId().isEmpty()) {
					// Submit failed / duplicate user (400 error or something else)
					// Show the RPC error message to the user
					statusBox.setStatus(StatusBoxWidget.Status.ERROR, "Error: Duplicate Username");
				}
				else {
					//update ID
					userId = result.getId();
					loadDataRPC();
					statusBox.clear();
				}
			}
		}
	}

	class RpcDeleteUserCallBackHandler extends RiseAsyncCallback<RpcResultInfo> {

		public void onFailure() {
			actionsWidget.setEnabled(true);
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(RpcResultInfo result) {
			if (usersOwnProfile) {
				Window.Location.replace("");
			}
			else {
				//if Delete action succeeds, simply close the page
				doActionCancel();
				actionsWidget.setEnabled(true);
			}
		}
	}
	
//	class RpcGetPNOSettingsCallBackHandler implements AsyncCallback<CompanyInfo> {
//		public void onFailure(Throwable caught) {
//		}
//
//		public void onSuccess(CompanyInfo result) {
//			if (result != null) {
//				applyPNOSettings(result);
//			}
//		}
//	}
	
}
