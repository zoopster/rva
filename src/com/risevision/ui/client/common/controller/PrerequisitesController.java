// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.controller;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.ui.client.UiControlBinder;
import com.risevision.ui.client.UiEntryPoint;
import com.risevision.ui.client.common.service.CompanyService;
import com.risevision.ui.client.common.service.CompanyServiceAsync;
import com.risevision.ui.client.common.data.DataAccessController;
import com.risevision.ui.client.common.exception.RiseAsyncCallback;
import com.risevision.ui.client.common.exception.ServiceFailedException;
import com.risevision.ui.client.common.info.CompanyInfo;
import com.risevision.ui.client.common.info.EmailInfo;
import com.risevision.ui.client.common.info.PrerequisitesInfo;
import com.risevision.ui.client.common.info.RoleInfo;
import com.risevision.ui.client.common.info.RpcResultInfo;
import com.risevision.ui.client.common.info.UserInfo;
import com.risevision.ui.client.common.service.UserService;
import com.risevision.ui.client.common.service.UserServiceAsync;
import com.risevision.ui.client.common.widgets.MenuWidget;
import com.risevision.ui.client.common.widgets.messages.SystemMessagesWidget;
import com.risevision.ui.client.company.CompanySelectorWidget;
import com.risevision.ui.client.user.UserAccountWidget;
import com.risevision.ui.client.user.UserLoginHelpWidget;
import com.risevision.ui.client.user.UserTermsWidget;

public class PrerequisitesController {
//	private static final String WELCOME_EMAIL_TEXT = "Welcome to the Rise Vision community!" +
//			"\n\nLogin: %lg" +
//			"\n\nGoogle Account: %un" +
//			"" +
//			"\n\nTo get started be sure to check out our tutorials and resources on our " +
//			"help page at http://www.risevision.com/help/ and join the discussions on our forum " +
//			"at http://community.risevision.com/ - a lively place to get " +
//			"your questions answered, share ideas and learn best practices." +
//			"" +
//			"\n\nAll the best with your creative endeavours on Rise Vision - a web platform for digital signage.";
	
	private static final String WELCOME_EMAIL_TEXT = "Welcome to the Rise Vision community!" +
			"\n\nLogin: %lg" +
			"\n\nAccount: %un" +
			"" +
			"\n\nIf you have questions be sure to check out the help page at www.risevision.com/help " +
			"and join the discussions on our forum at http://community.risevision.com - a lively " +
			"place to get your questions answered, share ideas and learn best practices." +
			"" +
			"\n\nIf you need further assistance please check out our Partners at www.risevision.com/partners " +
			"for any of the following additional services that they offer:" +
			"\n\nNetwork Management www.risevision.com/partners/?tag%5B%5D=network-management" +
			"\nSupport www.risevision.com/partners/?tag%5B%5D=support" +
			"\nOn-Site Service www.risevision.com/partners/?tag%5B%5D=on-site-service" +
			"\nTraining www.risevision.com/partners/?tag%5B%5D=training" +
			"\nCreative Design www.risevision.com/partners/?tag%5B%5D=creative-design" +
			"\nContent www.risevision.com/partners/?tag%5B%5D=content" +
			"\nProject Management www.risevision.com/partners/?tag%5B%5D=project-management" +
			"\nInstallation www.risevision.com/partners/?tag%5B%5D=installation" +
			"\nHardware www.risevision.com/partners/?tag%5B%5D=hardware" +
			"\nSoftware Development www.risevision.com/partners/?tag%5B%5D=software-development" +
			"" +
			"\n\nIf you would like to be a Partner or have questions about partnering with " +
			"Rise Vision please check out our overview at www.risevision.com/partners/become-a-partner." +
			"" +
			"\n\nAll the best with your creative endeavours on Rise Vision!";
	
	private static final String PARAM_PARENT_ID = "parentId=";
	
	private static PrerequisitesController instance;
	private Command cmdLoadedCallBack;
	private static boolean loaded = false; 
	private UserServiceAsync userService = GWT.create(UserService.class);
	private CompanyServiceAsync companyService = GWT.create(CompanyService.class);
	private UiControlBinder uiControlBinder = UiControlBinder.getInstance();
	
	private UserTermsWidget termsWidget = new UserTermsWidget();
	
	public static PrerequisitesController getInstance() {
		try {
			if (instance == null)
				instance = new PrerequisitesController();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}
	
	public void load(Command cmdCallBack){
		if (!loaded){
			cmdLoadedCallBack = cmdCallBack;
			load();
		}
	}
	
	private void load() {
		userService.getCurrent(Location.getHref(), new RpcGetPrereqCallBackHandler());
		//userService.getCurrent(Globals.LOGOUT_URL, new RpcGetUserInfoCallBackHandler());		
	}
	
	private void processPrereqInfo(PrerequisitesInfo result) {
		//Initialize the Configuration Controller.
		ConfigurationController configController = ConfigurationController.getInstance();
		configController.setConfiguration(result);
		
		DataAccessController.correctTimestamp(result.getServerTimestamp());
		
		UserAccountWidget accountWidget = UserAccountWidget.getInstance();
		accountWidget.setRedirectUrl(result.getConfiguration().getLogoutURL());
		
		if (result.getCurrentUserStatus() == PrerequisitesInfo.STATUS_ERROR) {
			if (Window.confirm("Your account information could not be retrieved. We apologize for the unexpected downtime. " +
					"Would you like to retry?")) {
				load();
			}
			else {
				accountWidget.logoutUser();
			}
		}
		else if (result.getCurrentUserStatus() == PrerequisitesInfo.STATUS_WRONG_USERNAME) {
//			if (Window.confirm("We think your account authentication got mixed up with another account. " +
//							"If you want to login with " + result.getCurrentUser().getUserName() + " press okay and " +
//							"make sure you authorize that account. If this is wrong, or it doesn't work, " +
//							"we're sorry about this but your browser is confused, and the only way to fix " +
//							"it is to clear your browser cache and try again.")) {
//				RiseAsyncCallback.reAuthenticateUser();
//			}
//			else {
//				accountWidget.logoutUser();
//			}
			
			new UserLoginHelpWidget().show(result.getCurrentUser().getUserName());
		}
		else {
			verifyTerms(result);
		}
	}
	
	private void verifyTerms(final PrerequisitesInfo result) {
		if (result != null && result.getCurrentUser() != null) {
			hideProgressBar();

			if (result.getCurrentUser().isTermsAccepted()) {
				initPrerequisites(result);
			}
			else {
				Command termsCommand = new Command() {
					@Override
					public void execute() {
						if (termsWidget.getAcceptance()) {
							showProgressBar();
							
							result.getCurrentUser().setTermsAcceptedDate(new Date());
							result.getCurrentUser().setTermsAccepted(true);
							
							if (result.getCurrentUser().getId() != null) {
								// save Terms Accepted Date
//								userService.putUser(result.getCurrentUser().getCompany(), result.getCurrentUser(), new RpcSaveUserCallBackHandler());

								initPrerequisites(result);
							}
							else {
								initNewUserCompany(result);
							}
						}
						else {
							UiEntryPoint.trackPageview("User_Registration_Failed");
							UserAccountWidget.getInstance().logoutUser();
//							Window.Location.replace(ConfigurationController.getInstance().getConfiguration().getLogoutURL());
						}
					}
				};

				termsWidget.show(termsCommand, result.getCurrentUserStatus());
			}
		}
	}
	
	private void initPrerequisites(PrerequisitesInfo result) {		
		userService.updateLastLogin(result.getCurrentUser().getCompany(), result.getCurrentUser(), new RpcSaveUserCallBackHandler());
		
		//Initialize the Account Controller.
		initUserController(result.getCurrentUser());
	
//		if (result.getCurrentUser().getCompany() != null){
//			companyService.getCompany(result.getCurrentUser().getCompany(), new RpcGetCompanyInfoCallBackHandler());
//			companyService.getPnoSettings(result.getCurrentUser().getCompany(), new RpcGetPNOSettingsCallBackHandler());
//			companyService.getCompanyAuthKey(result.getCurrentUser().getCompany(), new RpcGetCompanyAuthKeyCallBackHandler());
//		}
		
		initCompany(result.getCurrentUserCompany());
		
		SystemMessagesWidget.getInstance().init(result.getSystemMessages());

		hideProgressBar();
	}
	
	private void initNewUserCompany(PrerequisitesInfo result) {
		CompanyInfo	company = new CompanyInfo();

		String parentId = "";
		if (result != null) {
			// Unregistered user, need to create Company and User records
			company.setName(result.getCurrentUser().getUserName() + "'s Company");
			
			// Retrieve the Parent ID param from either the Querystring or the Hash
			parentId = RiseUtils.getFromQueryString(Location.getQueryString(), PARAM_PARENT_ID);
			if (RiseUtils.strIsNullOrEmpty(parentId)) {
				parentId = getFromHash(Location.getHash(), PARAM_PARENT_ID);
			}
			
			UserInfo user = result.getCurrentUser();
			user.setRoles(RoleInfo.getStandardRoles());
			
			initUserController(user);
		}
		else {
			company.setName(UserAccountController.getInstance().getUserInfo().getUserName() + "'s Company");
		}
		
		// if the parentId parameter is provided, than try to register under that ID
		if (!RiseUtils.strIsNullOrEmpty(parentId)) {
			company.setParentId(parentId);
//			company.setPnoStatus(CompanyNetworkOperatorStatus.NO);
		}
		else {
			company.setParentId(ConfigurationController.getInstance().getConfiguration().getRiseId());
//			company.setPnoStatus(CompanyNetworkOperatorStatus.SUBSCRIBED);
		}
		
		companyService.saveCompany(company, new RpcSaveCompanyInfoCallBackHandler());
	}
	
	private String getFromHash(String hash, String param) {
		int paramIndex = hash.toLowerCase().indexOf(param.toLowerCase());
		
		if(!hash.isEmpty() && hash.charAt(0) == '#' && paramIndex != -1){
			int endIndex = hash.indexOf("/", paramIndex);
			if (endIndex != -1)
				return URL.decode(hash.substring(paramIndex + param.length(), endIndex));
			else
				return URL.decode(hash.substring(paramIndex + param.length()));
		}
		
		return null;
	}
	
	private void initUserController(UserInfo user) {
		// initialize the Account Controller
		UserAccountController.getInstance().setUserInfo(user);
		UserAccountWidget.getInstance().setUsername(user);
		
//		UserAccountWidget.getInstance().setLogoutUrl(ConfigurationController.getInstance().getConfiguration().getLogoutURL());
		uiControlBinder.bindAccountWidget();
	}
	
	private void companyRegistrationFailed() {
		// if the user is coming from a preview, do not show him the notification
		if (getFromHash(Location.getHash(), UiEntryPoint.HASH_PARAM_FROM_COMPANY_ID) != null 
				|| Window.confirm("Requested Company is not accepting registrations. Would you " +
						"like to register a regular account?")) {
			initNewUserCompany(null);
		}
		else {
			UiEntryPoint.trackPageview("User_Registration_Failed");
			UserAccountWidget.getInstance().logoutUser();
//			Window.Location.replace(ConfigurationController.getInstance().getConfiguration().getLogoutURL());
		}
	}
	
	private void initNewUser(CompanyInfo company) {
		UserInfo userInfo = UserAccountController.getInstance().getUserInfo();
		userInfo.setCompany(company.getId());
		
//		initCompanyController(company);
		userService.putUser(company.getId(), userInfo, new RpcSaveNewUserCallBackHandler());
	}
	
	private void getNewUserCompanySettings() {
		UserInfo userInfo = UserAccountController.getInstance().getUserInfo();
//		userInfo.setTermsAccepted(true);
		userInfo.setLastLogin(new Date());
		
		UiEntryPoint.trackPageview("User_Registration");
		UiEntryPoint.trackConversion();
		
		emailUser();
	
		userService.putUser(userInfo.getCompany(), userInfo, new RpcSaveUserCallBackHandler());
		companyService.getCompany(userInfo.getCompany(), new RpcGetCompanyInfoCallBackHandler());
//		companyService.getPnoSettings(userInfo.getCompany(), new RpcGetPNOSettingsCallBackHandler());
//		companyService.getCompanyAuthKey(userInfo.getCompany(), new RpcGetCompanyAuthKeyCallBackHandler());
	}
	
	private void emailUser() {
		UserInfo user = UserAccountController.getInstance().getUserInfo();
		
		if (user != null) {
			EmailInfo email = new EmailInfo();

			String message = WELCOME_EMAIL_TEXT;
			message = message.replace("%lg", "http://" + Window.Location.getHostName());
			message = message.replace("%un", user.getUserName());
			
			email.setToAddressString(user.getUserName());
			email.setFromString("support@risevision.com");
			email.setSubjectString("Rise Vision - A Web Platform for Digital Signage");
			email.setMessageString(message);
			
			userService.sendEmail(email, new RpcSendUserEmailCallBackHandler());
		}
	}
	
	private void prerequisitesLoaded() {
		loaded = true;
		cmdLoadedCallBack.execute();
	}
	
	private void initCompany(CompanyInfo company) {
		initCompanyController(company);
		
		uiControlBinder.initCompany(company);
	}
	
	private void initCompanyController(CompanyInfo company) {
		SelectedCompanyController selectedCompany = SelectedCompanyController.getInstance();
		// we only need to assign userCompany once
		if (selectedCompany.getSelectedCompany() == null) {
			selectedCompany.setUserCompany(company);
			selectedCompany.setSelectedCompany(company);
		}
		
//		if (company.isPno() && company.getParentId() != null && !company.getParentId().isEmpty()) {
//			companyService.getPnoSettings(company.getParentId(), new RpcGetParentPNOSettingsCallBackHandler());
//		}
//		else {
//			UiControlBinder.getInstance().loadParentPNOSettings(null);
//		}
		
		uiControlBinder.bindCompanySelector();
		loadCompanySelector();
		MenuWidget.getInstance().refreshMenu();
		
		prerequisitesLoaded();
		hideProgressBar();
		
//		if (UserAccountController.getInstance().userHasRoleUserAdministrator()
//				&& SelectedCompanyController.getInstance().isUserCompanySelected()
//				&& RiseUtils.diffDaysFromNow(company.getCreationDate()) > 10 
//				&& company.getDemographicsInfo() != null
//				&& RiseUtils.strIsNullOrEmpty(company.getDemographicsInfo().getCompanyType())) {
//			MessageBoxWidget.getInstance().show("Please fill in some additional information regarding " +
//					"your Digital Signage at the bottom of the Company Settings page.", 
//			new Command() {
//				@Override
//				public void execute() {
//					HistoryTokenInfo tokenInfo = new HistoryTokenInfo();
//					tokenInfo.setContentId(ContentId.COMPANY_MANAGE);
//					tokenInfo.setBookmark("true");
//					UiEntryPoint.loadContentStatic(tokenInfo);
//				}
//			});
//		}
	}

	private void loadCompanySelector() {
		Command onCompanyChange = new Command() {
			public void execute() {
				MenuWidget.getInstance().refreshMenu();
				CompanySelectorWidget.getInstance().bindData();
				hideProgressBar();
				UiEntryPoint.getInstance().reloadContent();
			}
		};
		
		SelectedCompanyController.getInstance().setDataLoadedCallBack(onCompanyChange);
	}
	
	public void showProgressBar() {
		RootPanel.get("progress").getElement().getStyle().setProperty("display","inline");
		RootPanel.get("main").getElement().getStyle().setProperty("display","none");
	}

	public void hideProgressBar() {
		RootPanel.get("progress").getElement().getStyle().setProperty("display","none");
		RootPanel.get("main").getElement().getStyle().setProperty("display","inline");
	}
	
	class RpcGetPrereqCallBackHandler extends RiseAsyncCallback<PrerequisitesInfo> {
		public void onFailure() {
			// TODO: Show the RPC error message to the user

		}

		public void onSuccess(PrerequisitesInfo result) {
			if (result != null) {
				processPrereqInfo(result);
			}
		}
	}
	
	class RpcSaveNewUserCallBackHandler implements AsyncCallback<RpcResultInfo> {
		public void onFailure(Throwable caught) {
		}

		public void onSuccess(RpcResultInfo result) {
			if (result != null) {
				UserInfo userInfo = UserAccountController.getInstance().getUserInfo();
				
				userInfo.setId(result.getId());
				getNewUserCompanySettings();
			}
		}
	}
	
	class RpcSaveUserCallBackHandler implements AsyncCallback<RpcResultInfo> {
		public void onFailure(Throwable caught) {
		
		}

		public void onSuccess(RpcResultInfo result) {

		}
	}
	
	class RpcGetCompanyInfoCallBackHandler implements AsyncCallback<CompanyInfo> {
		public void onFailure(Throwable caught) {
		}

		public void onSuccess(CompanyInfo result) {
			if (result != null) {
				initCompany(result);
			}
		}
	}
	
	class RpcSaveCompanyInfoCallBackHandler implements AsyncCallback<CompanyInfo> {
		public void onFailure(Throwable caught) {
			if (caught instanceof ServiceFailedException && ((ServiceFailedException)caught).getReason() == ServiceFailedException.METHOD_NOT_ALLOWED) {
				companyRegistrationFailed();
			}
		}

		public void onSuccess(CompanyInfo result) {
			if (result != null) {
				initNewUser(result);
			}
		}
	}
	
//	class RpcGetPNOSettingsCallBackHandler implements AsyncCallback<CompanyInfo> {
//		public void onFailure(Throwable caught) {
//		}
//
//		public void onSuccess(CompanyInfo result) {
//			if (result != null) {
//				loadPNOSettings(result);
//			}
//		}
//	}
	
//	class RpcGetParentPNOSettingsCallBackHandler implements AsyncCallback<CompanyInfo> {
//		public void onFailure(Throwable caught) {
//		}
//
//		public void onSuccess(CompanyInfo result) {
//			if (result != null) {
//				loadParentPNOSettings(result);
//			}
//		}
//	}
	
//	class RpcGetCompanyAuthKeyCallBackHandler implements AsyncCallback<String> {
//		public void onFailure(Throwable caught) {
//		}
//
//		public void onSuccess(String result) {
//			if (result != null && !result.isEmpty()) {
//				updateAuthKey(result);
//			}
//		}
//	}
	
	class RpcSendUserEmailCallBackHandler implements AsyncCallback<Void> {
		public void onFailure(Throwable caught) {
		}

		public void onSuccess(Void result) {

		}
	}
}
