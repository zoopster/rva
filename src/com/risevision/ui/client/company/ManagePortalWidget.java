// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.company;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.risevision.core.api.settings.CompanySetting;
import com.risevision.ui.client.UiEntryPoint;
import com.risevision.ui.client.common.ContentId;
import com.risevision.ui.client.common.controller.SelectedCompanyController;
import com.risevision.ui.client.common.exception.RiseAsyncCallback;
import com.risevision.ui.client.common.info.CompanyInfo;
import com.risevision.ui.client.common.info.ManageSettingsInfo;
import com.risevision.ui.client.common.service.CompanyService;
import com.risevision.ui.client.common.service.CompanyServiceAsync;
import com.risevision.ui.client.common.widgets.ActionsWidget;
import com.risevision.ui.client.common.widgets.FormValidatorWidget;
import com.risevision.ui.client.common.widgets.LastModifiedWidget;
import com.risevision.ui.client.common.widgets.RiseListBox;
import com.risevision.ui.client.common.widgets.SpacerWidget;
import com.risevision.ui.client.common.widgets.StatusBoxWidget;
import com.risevision.ui.client.common.widgets.DefaultSettingWidget;
import com.risevision.ui.client.common.widgets.colorPicker.ColorPickerTextBox;
import com.risevision.ui.client.common.widgets.demoContent.DemoContentGridWidget;
import com.risevision.ui.client.common.widgets.grid.FormGridWidget;
import com.risevision.ui.client.common.widgets.mediaLibrary.GooglePickerViewId;
import com.risevision.ui.client.common.widgets.mediaLibrary.MediaLibraryTextBox;
import com.risevision.ui.client.presentation.PresentationSelectPopupWidget;
import com.risevision.ui.client.presentation.common.PresentationSelectorWidget;

public class ManagePortalWidget extends Composite {
	private static ManagePortalWidget instance;
	
	//RPC
	private final CompanyServiceAsync companyService = GWT.create(CompanyService.class);
	private String companyId;
	//UI pieces
	private ActionsWidget actionsWidget = ActionsWidget.getInstance();
	private VerticalPanel mainPanel = new VerticalPanel();
	private FormValidatorWidget formValidator = new FormValidatorWidget();
	private StatusBoxWidget statusBox = StatusBoxWidget.getInstance();
	private FormGridWidget mainGrid = new FormGridWidget(24, 2);
//	private int descriptionRow;
	
	private DefaultSettingWidget logoURLTextBox = new DefaultSettingWidget(new MediaLibraryTextBox(GooglePickerViewId.DOCS_IMAGES));
	private DefaultSettingWidget logoTargetTextBox = new DefaultSettingWidget();
	private CheckBox useAdsenseCheckBox = new CheckBox();
	private int bannerRow;
	private DefaultSettingWidget bannerURLTextBox = new DefaultSettingWidget(new MediaLibraryTextBox(GooglePickerViewId.DOCS_IMAGES));
	private DefaultSettingWidget bannerTargetURLTextBox = new DefaultSettingWidget();
	private TextBox adsenseBannerIdTextBox = new TextBox();
	private TextBox adsenseBannerSlotTextBox = new TextBox();
	private DefaultSettingWidget bannerBackgroundTextBox = new DefaultSettingWidget(new ColorPickerTextBox());
	private DefaultSettingWidget newsURLTextBox = new DefaultSettingWidget();
//	private URLWidget startPageURLTextBox = new URLWidget();
	private DefaultSettingWidget helpURLTextBox = new DefaultSettingWidget();
//	private TextBox supportURLTextBox = new TextBox();
	private DefaultSettingWidget supportEmailTextBox = new DefaultSettingWidget();
//	private URLWidget trainingURLTextBox = new URLWidget();
//	private TextBox salesURLTextBox = new TextBox();
	private DefaultSettingWidget salesEmailTextBox = new DefaultSettingWidget();
	private TextBox logoutUrlTextBox = new TextBox();
	
//	private CheckBox directoryCheckBox = new CheckBox();
//	private Anchor directoryLink = new Anchor("View Directory", false, "Directory.html", "_blank");
//	private TextArea directoryText = new TextArea();
//	private CheckBox showTutorialCheckBox = new CheckBox();
	private TextBox tutorialUrlTextBox = new TextBox();
	
	private CheckBox useEmailCampaignCheckBox = new CheckBox();
	private int emailCampaignRow;
	private RiseListBox emailCampaignServiceListBox = new RiseListBox();
	private TextBox emailCampaignApiUrlTextBox = new TextBox();
	private TextBox emailCampaignApiKeyTextBox = new TextBox();
	private TextBox emailCampaignListIdTextBox = new TextBox();
	
	private CheckBox allowCompanyRegistrationsCheckBox = new CheckBox();

	private TextBox analyticsIDTextBox = new TextBox();
//	private TextBox analyticsURLTextBox = new TextBox();
//	private URLWidget registrationTermsTextBox = new URLWidget();	
	
	private PresentationSelectorWidget operatorPresentationWidget = new PresentationSelectorWidget();
	private PresentationSelectorWidget userPresentationWidget = new PresentationSelectorWidget();
	
	private DemoContentGridWidget demoContentGridWidget = new DemoContentGridWidget();
	
	//roles section
//	private RolesWidget rolesWidget = new RolesWidget();
	
	private LastModifiedWidget lastModifiedWidget = LastModifiedWidget.getInstance();
	
	public static ManagePortalWidget getInstance() {
		try {
			if (instance == null)
				instance = new ManagePortalWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}
	
	public ManagePortalWidget() {
		initWidget(mainPanel);
		mainPanel.add(formValidator);
		mainPanel.add(mainGrid);
		
		styleControls();

		mainGrid.addRow("Logo URL (" + ManageSettingsInfo.LOGO_WIDTH + "x" + ManageSettingsInfo.LOGO_HEIGHT + ") :",
				"Add your logo to the left side of the header", 
				logoURLTextBox, "rdn-TextBoxMedium");
		mainGrid.addRow("Logo Target URL:", 
				"The URL that opens in a new tab if your logo is clicked", 
				logoTargetTextBox, "rdn-TextBoxLong");
		mainGrid.addRow("Use DoubleClick Banner:",
				"Show banners from your Google DoubleClick account in the middle of the header to any Company below yours", 
				useAdsenseCheckBox, "rdn-CheckBox");
		
		mainGrid.addRow("Banner URL (" + ManageSettingsInfo.BANNER_WIDTH + "x" + ManageSettingsInfo.BANNER_HEIGHT + ") :", 
				"Add a banner to show in the middle of the header to any Company below yours", 
				bannerURLTextBox, "rdn-TextBoxMedium");
		bannerRow = mainGrid.getRow();
		mainGrid.addRow("Banner Target URL:", 
				"The URL that opens in a new tab if your banner is clicked", 
				bannerTargetURLTextBox, "rdn-TextBoxLong");
		mainGrid.addRow("Adsense Campaign ID:", 
				"Available from your Adsense Account", 
				adsenseBannerIdTextBox, "rdn-TextBoxMedium");
		mainGrid.addRow("Adsense Slot Name:", 
				"Available from your Adsense Account", 
				adsenseBannerSlotTextBox, "rdn-TextBoxMedium");
		mainGrid.addRow("Header Background Color:", 
				"The color of the header behind your logo and banner", 
				bannerBackgroundTextBox, "rdn-TextBoxMedium");
		
		//mainGrid.addRow("Start Page URL:", startPageURLTextBox, "rdn-TextBoxLong");
		mainGrid.addRow("Help URL/Email:", 
				"The URL that opens when Help is clicked by any Company below yours", 
				helpURLTextBox, "rdn-TextBoxLong");
//		mainGrid.addRow("Support URL*:", supportURLTextBox, "rdn-TextBoxLong");
		mainGrid.addRow("Support URL/Email:",
				"The URL that opens when Support is clicked by any Company below yours", 
				supportEmailTextBox, "rdn-TextBoxLong");
//		mainGrid.addRow("Sales URL:", salesURLTextBox, "rdn-TextBoxLong");
		mainGrid.addRow("Sales URL/Email:", 
				"The URL that opens when Sales is clicked by any Company below yours",
				salesEmailTextBox, "rdn-TextBoxLong");
		//mainGrid.addRow("Training URL:", trainingURLTextBox, "rdn-TextBoxLong");
		mainGrid.addRow("News URL:", 
				"The URL that opens when News is clicked by any Company below yours", 
				newsURLTextBox, "rdn-TextBoxLong");
		mainGrid.addRow("Logout URL:", 
				"The URL that a User lands on when they logout for any Company below yours",
				logoutUrlTextBox, "rdn-TextBoxLong");
		
//		HorizontalPanel directoryPanel = new HorizontalPanel();
//		directoryPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
//		directoryPanel.add(directoryCheckBox);
//		directoryPanel.add(new SpacerWidget());
//		directoryPanel.add(directoryLink);
//		mainGrid.addRow("Show Tutorial:", 
//				"Show the startup Tutorial upon logging in for any Company below yours",
//				showTutorialCheckBox, "rdn-CheckBox");
		mainGrid.addRow("Tutorial URL:", tutorialUrlTextBox, "rdn-TextBoxLong");
		
		mainGrid.addRow("Use Email Campaign Service:", 
				"Manage Email Campaigns to Users of any Company below yours", 
				useEmailCampaignCheckBox, "rdn-CheckBox");

		// Service [dropdown - only includes MailChimp for now] Need to Sign Up for MailChimp? 
		// (affiliate program link to the right of the drop down)
		emailCampaignServiceListBox.addItem("MailChimp", "MailChimp");
		HorizontalPanel emailCampaignServicePanel = new HorizontalPanel();
		emailCampaignServicePanel.add(emailCampaignServiceListBox);
		emailCampaignServicePanel.add(new SpacerWidget());
//		emailCampaignServicePanel.add(new Anchor("Need to Sign Up for MailChimp?", "http://www.mailchimp.com", "_blank"));
		emailCampaignServicePanel.add(new Anchor("Need to Sign Up for MailChimp?", "http://eepurl.com/o4dlj", "_blank"));
//		mainGrid.addRow("Service:", emailCampaignServiceListBox, "rdn-ListBoxMedium");
		mainGrid.addRow("Service:", emailCampaignServicePanel);
		emailCampaignRow = mainGrid.getRow();

		mainGrid.addRow("API URL:", emailCampaignApiUrlTextBox, "rdn-TextBoxLong");
        mainGrid.addRow("API Key:", emailCampaignApiKeyTextBox, "rdn-TextBoxLong");
        mainGrid.addRow("List ID:", emailCampaignListIdTextBox, "rdn-TextBoxLong");
		
		mainGrid.addRow("Allow New Registrations:",
				"Provide your own login link and allow new Users to automatically sign up from it", 
				allowCompanyRegistrationsCheckBox, "rdn-CheckBox");
		
//		mainGrid.addRow("List in Directory:", directoryPanel, null);
//		mainGrid.addRow("Directory Description:", directoryText, null);
//		descriptionRow = row;
		
//		mainGrid.addRow("Terms & Conditions URL:", registrationTermsTextBox, "rdn-TextBoxLong");
		mainGrid.addRow("Google Analytics ID:", 
				"Use your Google Analytics account to track your User traffic",
				analyticsIDTextBox, "rdn-TextBoxMedium");
//		mainGrid.addRow("Analytics Reporting URL:", analyticsURLTextBox, "rdn-TextBoxLong");
		mainGrid.addRow("Start Presentation:", 
				"A Presentation that is shown on the Start page to your Users when they login", 
				operatorPresentationWidget, null);
		mainGrid.addRow("Client Company Start Presentation:", 
				"A Presentation that is shown on the Start page to Users of any Company below yours when they login",
				userPresentationWidget, null);
		
		mainGrid.addRow("Demonstration Presentations:",
				"Presentations that show on Displays that have no scheduled content for your Company and any Company below yours", 
				demoContentGridWidget, null);

		// add Security fields
		//mainGrid.addRowHTML(0, "&nbsp"); //empty row
		//mainGrid.addRow("Roles:", rolesWidget);
		
//		directoryCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
//			@Override
//			public void onValueChange(ValueChangeEvent<Boolean> event) {
//				mainGrid.getRowFormatter().setVisible(descriptionRow, directoryCheckBox.getValue());
//			}
//		});
		
		initValidator();
	}
	
	private void styleControls(){
		//style the table	
		mainGrid.setCellSpacing(0);
		mainGrid.setCellPadding(0);
		mainGrid.setStyleName("rdn-Table");
		
		emailCampaignServiceListBox.setStyleName("rdn-ListBoxMedium");
		
//		directoryCheckBox.setStyleName("rdn-CheckBox");
//		directoryText.setSize("450px", "50px");
	}

	private void initValidator() {
		// Add widgets to validator
//		formValidator.addValidationElement(helpURLTextBox, "Help URL", FormValidatorInfo.requiredFieldValidator);
//		formValidator.addValidationElement(supportURLTextBox, "Support URL", FormValidatorInfo.requiredFieldValidator);
//		formValidator.addValidationElement(supportEmailTextBox, "Support Email", FormValidatorInfo.requiredFieldValidator);
	}
	
	protected void onLoad() {
		super.onLoad();

		if (!SelectedCompanyController.getInstance().getSelectedCompany().isPno()) {
			UiEntryPoint.loadContentStatic(ContentId.HOME);
			return;
		}
		
//		directoryLink.setVisible(true);
		initActions();
		clearData();
//		loadParentDataRPC();
		loadDataRPC();
		logoURLTextBox.setFocus(true);
		
//		StorageAppWidget.getInstance().load();
		
		PresentationSelectPopupWidget.getInstance().load();
	}
	
	private void initActions() {	
		Command cmdSave = new Command() {
			public void execute() {
				doActionSave();
			}			
		};
	
		actionsWidget.addAction("Save", cmdSave);
		
		useAdsenseCheckBox.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				onAdsenseCheckBoxClicked(); 
			}
		});
		
		useEmailCampaignCheckBox.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				onEmailCampaignCheckBoxClicked(); 
			}
		});
	}
	
	private void onAdsenseCheckBoxClicked() {
		boolean useAdsense = useAdsenseCheckBox.getValue();
		
		mainGrid.getRowFormatter().setVisible(bannerRow, !useAdsense);
		mainGrid.getRowFormatter().setVisible(bannerRow + 1, !useAdsense);
		mainGrid.getRowFormatter().setVisible(bannerRow + 2, useAdsense);
		mainGrid.getRowFormatter().setVisible(bannerRow + 3, useAdsense);
	}
	
	private void onEmailCampaignCheckBoxClicked() {
		boolean useEmailCampaign = useEmailCampaignCheckBox.getValue();
		
		mainGrid.getRowFormatter().setVisible(emailCampaignRow, useEmailCampaign);
		mainGrid.getRowFormatter().setVisible(emailCampaignRow + 1, useEmailCampaign);
		mainGrid.getRowFormatter().setVisible(emailCampaignRow + 2, useEmailCampaign);
		mainGrid.getRowFormatter().setVisible(emailCampaignRow + 3, useEmailCampaign);
	}
	
	private void doActionSave(){
		if (!formValidator.validate())
			return;
		
		CompanyInfo company = new CompanyInfo();
		HashMap<String, String> settings = new HashMap<String, String>();
		
		settings.put(ManageSettingsInfo.LOGO_URL, logoURLTextBox.getText());
		settings.put(ManageSettingsInfo.LOGO_TARGET_URL, logoTargetTextBox.getText());
		settings.put(ManageSettingsInfo.BANNER_URL, useAdsenseCheckBox.getValue() ? "" : bannerURLTextBox.getText());
		settings.put(ManageSettingsInfo.BANNER_TARGET_URL, useAdsenseCheckBox.getValue() ? "" : bannerTargetURLTextBox.getText());
		settings.put(ManageSettingsInfo.ADSENSE_SERVICE_ID, useAdsenseCheckBox.getValue() ? adsenseBannerIdTextBox.getText() : "");
		settings.put(ManageSettingsInfo.ADSENSE_SERVICE_SLOT, useAdsenseCheckBox.getValue() ? adsenseBannerSlotTextBox.getText() : "");
		settings.put(ManageSettingsInfo.BANNER_BACKGROUND_COLOR, bannerBackgroundTextBox.getText());
		settings.put(ManageSettingsInfo.NEWS_URL, newsURLTextBox.getText());
		settings.put(ManageSettingsInfo.LOGOUT_URL, logoutUrlTextBox.getText());
//		settings.put(ManageSettingsInfo.START_PAGE_URL, startPageURLTextBox.getText());
		settings.put(ManageSettingsInfo.HELP_URL, helpURLTextBox.getText());
//		settings.put(ManageSettingsInfo.SUPPORT_URL, supportURLTextBox.getText());
		settings.put(ManageSettingsInfo.SUPPORT_EMAIL, supportEmailTextBox.getText());
//		settings.put(ManageSettingsInfo.TRAINING_URL, trainingURLTextBox.getText());
//		settings.put(ManageSettingsInfo.SALES_URL, salesURLTextBox.getText());
		settings.put(ManageSettingsInfo.SALES_EMAIL, salesEmailTextBox.getText());
//		settings.put(ManageSettingsInfo.DIRECTORY_LISTING, directoryCheckBox.getValue() ? "true" : "false");
//		settings.put(ManageSettingsInfo.DIRECTORY_DESCRIPTION, directoryText.getText());
//		settings.put(ManageSettingsInfo.SHOW_TUTORIAL, showTutorialCheckBox.getValue() ? "true" : "false");
		settings.put(ManageSettingsInfo.TUTORIAL_URL, tutorialUrlTextBox.getText());
		settings.put(CompanySetting.MAIL_SYNC_ENABLED, useEmailCampaignCheckBox.getValue() ? "true" : "false");
		settings.put(CompanySetting.MAIL_SYNC_SERVICE, emailCampaignServiceListBox.getSelectedValue());
		settings.put(CompanySetting.MAIL_SYNC_API_URL, emailCampaignApiUrlTextBox.getText());
		settings.put(CompanySetting.MAIL_SYNC_API_KEY, emailCampaignApiKeyTextBox.getText());
		settings.put(CompanySetting.MAIL_SYNC_LIST_ID, emailCampaignListIdTextBox.getText());
		settings.put(ManageSettingsInfo.ALLOW_COMPANY_REGISTRATIONS, allowCompanyRegistrationsCheckBox.getValue() ? "true" : "false");
		settings.put(ManageSettingsInfo.ANALYTICS_ID, analyticsIDTextBox.getText());
//		settings.put(ManageSettingsInfo.ANALYTICS_REPORTING_URL, analyticsURLTextBox.getText());
//		settings.put(ManageSettingsInfo.COMPANY_REGISTRATION_TERMS, registrationTermsTextBox.getText());
		settings.put(ManageSettingsInfo.OPERATOR_START_PRESENTATION, operatorPresentationWidget.getId());
		settings.put(ManageSettingsInfo.USER_START_PRESENTATION, userPresentationWidget.getId());

//		String tempRoles = RiseUtils.ListToString(rolesWidget.getRoles(), ",");
		
//		settings.put(ManageSettingsInfo.SECURITY_ROLES, tempRoles);
		
		company.setSettings(settings);
		company.setId(companyId);
		saveDataRPC(company);
	}
	
	private void saveDataRPC(CompanyInfo company){
		actionsWidget.setEnabled(false);
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, StatusBoxWidget.SAVING);
		
		companyService.savePnoSettings(company, new RiseAsyncCallback<Void>() {

			@Override
			public void onFailure() {
				statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
				actionsWidget.setEnabled(true);
			}

			@Override
			public void onSuccess(Void result) {
				actionsWidget.setEnabled(true);
				
				statusBox.clear();
			}
		});
		
	}
	
//	private void loadParentDataRPC(){
//		statusBox.setStatus(StatusBoxWidget.Status.WARNING, StatusBoxWidget.LOADING);	
//		
//		CompanyInfo selectedCompany = SelectedCompanyController.getInstance().getSelectedCompany();
//		
//		companyId = selectedCompany.getId();
//		String parentCompanyId = selectedCompany.getParentId();
//		
//		if (parentCompanyId != null && !parentCompanyId.isEmpty()) {
//			companyService.getPnoSettings(parentCompanyId, new RiseAsyncCallback<CompanyInfo>() {
//				public void onFailure() {
//					statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
//				}
//	
//				public void onSuccess(CompanyInfo result) {
//					if (result != null)
//						bindParentData(result);
//					loadDataRPC();
//				}
//			});
//		}
//		else {
//			loadDataRPC();
//		}
//	}
	
	private void loadDataRPC() {	
		CompanyInfo selectedCompany = SelectedCompanyController.getInstance().getSelectedCompany();
		
		companyId = selectedCompany.getId();
		companyService.getCompany(companyId, new RiseAsyncCallback<CompanyInfo>() {
			public void onFailure() {
				statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
			}

			public void onSuccess(CompanyInfo result) {
				if (result != null) {
					if (result.getParentSettings() != null) {
						bindParentData(result);
					}
					
					bindData(result);
				}
				statusBox.clear();
			}
		});
	}
	
	protected void bindParentData(CompanyInfo company) {
		HashMap<String, String> settingsMap = company.getParentSettings();
		
		if (settingsMap.containsKey(ManageSettingsInfo.LOGO_URL)){
			logoURLTextBox.setDefaultText(settingsMap.get(ManageSettingsInfo.LOGO_URL));
		}
		if (settingsMap.containsKey(ManageSettingsInfo.LOGO_TARGET_URL)){
			logoTargetTextBox.setDefaultText(settingsMap.get(ManageSettingsInfo.LOGO_TARGET_URL));
		}
		if (settingsMap.containsKey(ManageSettingsInfo.BANNER_URL)){
			bannerURLTextBox.setDefaultText(settingsMap.get(ManageSettingsInfo.BANNER_URL));
		}
		if (settingsMap.containsKey(ManageSettingsInfo.BANNER_TARGET_URL)) {
			bannerTargetURLTextBox.setDefaultText(settingsMap.get(ManageSettingsInfo.BANNER_TARGET_URL));
		}
		if (settingsMap.containsKey(ManageSettingsInfo.BANNER_BACKGROUND_COLOR)){
			bannerBackgroundTextBox.setDefaultText(settingsMap.get(ManageSettingsInfo.BANNER_BACKGROUND_COLOR));
		}
//		if (result.containsKey(ManageSettingsInfo.START_PAGE_URL)){
//			startPageURLTextBox.setDefaultText(result.get(ManageSettingsInfo.START_PAGE_URL));
//		}
		if (settingsMap.containsKey(ManageSettingsInfo.HELP_URL)){
			helpURLTextBox.setDefaultText(settingsMap.get(ManageSettingsInfo.HELP_URL));
		}
		if (settingsMap.containsKey(ManageSettingsInfo.SUPPORT_EMAIL)){
			supportEmailTextBox.setDefaultText(settingsMap.get(ManageSettingsInfo.SUPPORT_EMAIL));
		}
		if (settingsMap.containsKey(ManageSettingsInfo.SALES_EMAIL)){
			salesEmailTextBox.setDefaultText(settingsMap.get(ManageSettingsInfo.SALES_EMAIL));
		}
		if (settingsMap.containsKey(ManageSettingsInfo.NEWS_URL)){
			newsURLTextBox.setDefaultText(settingsMap.get(ManageSettingsInfo.NEWS_URL));
		}
//		if (settingsMap.containsKey(ManageSettingsInfo.LOGOUT_URL)){
//			logoutUrlTextBox.setDefaultText(settingsMap.get(ManageSettingsInfo.LOGOUT_URL));
//		}
		// As per issue 700:
		// The Operator Start Presentation field's Default setting should not apply the Parent company's 
		// Operator presentation, instead it should use the User presentation
//		if (settingsMap.containsKey(ManageSettingsInfo.OPERATOR_START_PRESENTATION)){
//			operatorPresentationWidget.setDefaultPresentation(settingsMap.get(ManageSettingsInfo.OPERATOR_START_PRESENTATION));
//		}
		if (settingsMap.containsKey(ManageSettingsInfo.USER_START_PRESENTATION)){
			userPresentationWidget.setDefaultPresentation(settingsMap.get(ManageSettingsInfo.USER_START_PRESENTATION));
			// As per the note above, set the Operator Presentation field to the Parent Company's User Start presentation
			operatorPresentationWidget.setDefaultPresentation(settingsMap.get(ManageSettingsInfo.USER_START_PRESENTATION));
		}
//		if (result.containsKey(ManageSettingsInfo.TRAINING_URL)){
//			trainingURLTextBox.setDefaultText(result.get(ManageSettingsInfo.TRAINING_URL));
//		}
		
//		if (result.containsKey(ManageSettingsInfo.COMPANY_REGISTRATION_TERMS)){
//			registrationTermsTextBox.setDefaultText(result.get(ManageSettingsInfo.COMPANY_REGISTRATION_TERMS));
//		}
	}

	protected void bindData(CompanyInfo company) {
		HashMap<String, String> settingsMap = company.getSettings();

		lastModifiedWidget.Initialize(company.getChangedBy(), company.getChangeDate());
		
		if (settingsMap.containsKey(ManageSettingsInfo.LOGO_URL)){
			logoURLTextBox.setText(settingsMap.get(ManageSettingsInfo.LOGO_URL));
		}
		if (settingsMap.containsKey(ManageSettingsInfo.LOGO_TARGET_URL)){
			logoTargetTextBox.setText(settingsMap.get(ManageSettingsInfo.LOGO_TARGET_URL));
		}
		if (settingsMap.containsKey(ManageSettingsInfo.BANNER_URL)){
			bannerURLTextBox.setText(settingsMap.get(ManageSettingsInfo.BANNER_URL));
		}
		if (settingsMap.containsKey(ManageSettingsInfo.BANNER_TARGET_URL)) {
			bannerTargetURLTextBox.setText(settingsMap.get(ManageSettingsInfo.BANNER_TARGET_URL));
		}
		if (settingsMap.containsKey(ManageSettingsInfo.ADSENSE_SERVICE_ID)) {
			String serviceId = settingsMap.get(ManageSettingsInfo.ADSENSE_SERVICE_ID);
			if (serviceId != null && !serviceId.isEmpty()) {
				useAdsenseCheckBox.setValue(true);
			}
			else {
				useAdsenseCheckBox.setValue(false);
			}
			onAdsenseCheckBoxClicked();

			adsenseBannerIdTextBox.setText(serviceId);
		}
		if (settingsMap.containsKey(ManageSettingsInfo.ADSENSE_SERVICE_SLOT)) {
			adsenseBannerSlotTextBox.setText(settingsMap.get(ManageSettingsInfo.ADSENSE_SERVICE_SLOT));
		}
		if (settingsMap.containsKey(ManageSettingsInfo.BANNER_BACKGROUND_COLOR)){
			bannerBackgroundTextBox.setText(settingsMap.get(ManageSettingsInfo.BANNER_BACKGROUND_COLOR));
		}
		if (settingsMap.containsKey(ManageSettingsInfo.NEWS_URL)){
			newsURLTextBox.setText(settingsMap.get(ManageSettingsInfo.NEWS_URL));
		}
		if (settingsMap.containsKey(ManageSettingsInfo.LOGOUT_URL)){
			logoutUrlTextBox.setText(settingsMap.get(ManageSettingsInfo.LOGOUT_URL));
		}
//		if (result.containsKey(ManageSettingsInfo.START_PAGE_URL)){
//			startPageURLTextBox.setText(result.get(ManageSettingsInfo.START_PAGE_URL));
//		}
		if (settingsMap.containsKey(ManageSettingsInfo.HELP_URL)){
			helpURLTextBox.setText(settingsMap.get(ManageSettingsInfo.HELP_URL));
		}
//		if (result.containsKey(ManageSettingsInfo.SUPPORT_URL)){
//			supportURLTextBox.setText(result.get(ManageSettingsInfo.SUPPORT_URL));
//		}
		if (settingsMap.containsKey(ManageSettingsInfo.SUPPORT_EMAIL)){
			supportEmailTextBox.setText(settingsMap.get(ManageSettingsInfo.SUPPORT_EMAIL));
		}
//		if (result.containsKey(ManageSettingsInfo.TRAINING_URL)){
//			trainingURLTextBox.setText(result.get(ManageSettingsInfo.TRAINING_URL));
//		}
//		if (result.containsKey(ManageSettingsInfo.SALES_URL)){
//			salesURLTextBox.setText(result.get(ManageSettingsInfo.SALES_URL));
//		}
		if (settingsMap.containsKey(ManageSettingsInfo.SALES_EMAIL)){
			salesEmailTextBox.setText(settingsMap.get(ManageSettingsInfo.SALES_EMAIL));
		}
//		if (settingsMap.containsKey(ManageSettingsInfo.DIRECTORY_LISTING)){
//			directoryCheckBox.setValue("true".equals(settingsMap.get(ManageSettingsInfo.DIRECTORY_LISTING)));
//		}
//		mainGrid.getRowFormatter().setVisible(descriptionRow, directoryCheckBox.getValue());
//
//		if (settingsMap.containsKey(ManageSettingsInfo.DIRECTORY_DESCRIPTION)){
//			directoryText.setText(settingsMap.get(ManageSettingsInfo.DIRECTORY_DESCRIPTION));
//		}

		// default value is true, so not equals to false
//		showTutorialCheckBox.setValue(!settingsMap.containsKey(ManageSettingsInfo.SHOW_TUTORIAL) 
//				|| !"false".equals(settingsMap.get(ManageSettingsInfo.SHOW_TUTORIAL)));
		
		if (settingsMap.containsKey(ManageSettingsInfo.TUTORIAL_URL)) {
			tutorialUrlTextBox.setText(settingsMap.get(ManageSettingsInfo.TUTORIAL_URL));
		}

		useEmailCampaignCheckBox.setValue(settingsMap.containsKey(CompanySetting.MAIL_SYNC_ENABLED) 
				&& "true".equals(settingsMap.get(CompanySetting.MAIL_SYNC_ENABLED)));
		
		onEmailCampaignCheckBoxClicked();
		
		if (settingsMap.containsKey(CompanySetting.MAIL_SYNC_SERVICE)){
			emailCampaignServiceListBox.setSelectedValue(settingsMap.get(CompanySetting.MAIL_SYNC_SERVICE));
		}
		if (settingsMap.containsKey(CompanySetting.MAIL_SYNC_API_URL)){
			emailCampaignApiUrlTextBox.setText(settingsMap.get(CompanySetting.MAIL_SYNC_API_URL));
		}
		if (settingsMap.containsKey(CompanySetting.MAIL_SYNC_API_KEY)){
			emailCampaignApiKeyTextBox.setText(settingsMap.get(CompanySetting.MAIL_SYNC_API_KEY));
		}
		if (settingsMap.containsKey(CompanySetting.MAIL_SYNC_SERVICE)){
			emailCampaignListIdTextBox.setText(settingsMap.get(CompanySetting.MAIL_SYNC_LIST_ID));
		}
		
		allowCompanyRegistrationsCheckBox.setValue(settingsMap.containsKey(ManageSettingsInfo.ALLOW_COMPANY_REGISTRATIONS) 
				&& "true".equals(settingsMap.get(ManageSettingsInfo.ALLOW_COMPANY_REGISTRATIONS)));
		
		if (settingsMap.containsKey(ManageSettingsInfo.ANALYTICS_ID)){
			analyticsIDTextBox.setText(settingsMap.get(ManageSettingsInfo.ANALYTICS_ID));
		}
//		if (result.containsKey(ManageSettingsInfo.ANALYTICS_REPORTING_URL)){
//			analyticsURLTextBox.setText(result.get(ManageSettingsInfo.ANALYTICS_REPORTING_URL));
//		}
//		if (result.containsKey(ManageSettingsInfo.COMPANY_REGISTRATION_TERMS)){
//			registrationTermsTextBox.setText(result.get(ManageSettingsInfo.COMPANY_REGISTRATION_TERMS));
//		}
		
//		if (result.containsKey(ManageSettingsInfo.SECURITY_ROLES)){
//			String[] rolesEntry = result.get(ManageSettingsInfo.SECURITY_ROLES);
//			rolesWidget.setRoles(RiseUtils.StringToList(rolesEntry[0], ","));
//		}
//		else
//			rolesWidget.setSelected();
		
//		setDirectoryLinkVisibility();
		
		operatorPresentationWidget.init(settingsMap.get(ManageSettingsInfo.OPERATOR_START_PRESENTATION));
		userPresentationWidget.init(settingsMap.get(ManageSettingsInfo.USER_START_PRESENTATION));
	}
	
	private void clearData() {
		logoURLTextBox.setText("");
		logoURLTextBox.setDefaultText("");
		logoTargetTextBox.setText("");
		logoTargetTextBox.setDefaultText("");
		
		useAdsenseCheckBox.setValue(false);
		onAdsenseCheckBoxClicked();
		
		bannerURLTextBox.setText("");
		bannerURLTextBox.setDefaultText("");
		bannerTargetURLTextBox.setText("");
		bannerTargetURLTextBox.setDefaultText("");
		adsenseBannerIdTextBox.setText("");
		adsenseBannerSlotTextBox.setText("");
		bannerBackgroundTextBox.setText("");
		bannerBackgroundTextBox.setDefaultText("");
		
		newsURLTextBox.setText("");
		newsURLTextBox.setDefaultText("");
		logoutUrlTextBox.setText("");
//		logoutUrlTextBox.setDefaultText("");
//		startPageURLTextBox.setText("");
		helpURLTextBox.setText("");
		helpURLTextBox.setDefaultText("");
//		supportURLTextBox.setText("");
		supportEmailTextBox.setText("");
		supportEmailTextBox.setDefaultText("");
//		trainingURLTextBox.setText("");
//		salesURLTextBox.setText("");
		salesEmailTextBox.setText("");
		salesEmailTextBox.setDefaultText("");
//		directoryCheckBox.setValue(false, true);
//		directoryText.setText("");
		
		useEmailCampaignCheckBox.setValue(false);
		onEmailCampaignCheckBoxClicked();
		
		emailCampaignApiKeyTextBox.setText("");
		emailCampaignListIdTextBox.setText("");
		analyticsIDTextBox.setText("");
//		analyticsURLTextBox.setText("");
//		registrationTermsTextBox.setText("");
		
//		rolesWidget.setSelected();
	}
	
//	private void setDirectoryLinkVisibility() {
//		if (logoURLTextBox.isDefaultText() 
//				|| logoTargetTextBox.isDefaultText() 
//				|| bannerURLTextBox.isDefaultText()
//				|| bannerBackgroundTextBox.isDefaultText()
//				|| newsURLTextBox.isDefaultText()
//				|| helpURLTextBox.isDefaultText()
//				|| supportEmailTextBox.isDefaultText()
//				|| salesEmailTextBox.isDefaultText()) {
//			directoryLink.setVisible(false);
//		}
//	}

}
