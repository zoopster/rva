// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.ui.client.common.controller.ConfigurationController;
import com.risevision.ui.client.common.controller.SelectedCompanyController;
import com.risevision.ui.client.common.controller.UserAccountController;
import com.risevision.ui.client.common.info.CompanyInfo;
import com.risevision.ui.client.common.info.ManageSettingsInfo;
import com.risevision.ui.client.common.service.UserService;
import com.risevision.ui.client.common.service.UserServiceAsync;
import com.risevision.ui.client.common.widgets.ActionsWidget;
import com.risevision.ui.client.common.widgets.EmailWidget;
import com.risevision.ui.client.common.widgets.LastModifiedWidget;
import com.risevision.ui.client.common.widgets.MenuWidget;
import com.risevision.ui.client.common.widgets.SpacerWidget;
import com.risevision.ui.client.common.widgets.StatusBoxWidget;
import com.risevision.ui.client.common.widgets.tutorial.TutorialWidget;
import com.risevision.ui.client.company.CompanySelectorWidget;
import com.risevision.ui.client.user.UserAccountWidget;

public class UiControlBinder extends Composite implements ClickHandler {
	private static UiControlBinderUiBinder uiBinder = GWT.create(UiControlBinderUiBinder.class);
	interface UiControlBinderUiBinder extends UiBinder<Widget, UiControlBinder> {}
	private static UiControlBinder instance;
	
	private boolean startFrameLoaded = false;
	private String startFramePresentationId;
	
	private MenuWidget menuWidget = MenuWidget.getInstance();
	private LastModifiedWidget lastModified = LastModifiedWidget.getInstance();
	private StatusBoxWidget statusBox = StatusBoxWidget.getInstance();
	private ActionsWidget actionsWidget = ActionsWidget.getInstance();
	
	private static final String blankTarget = "_blank";

	private HashMap<String, String> currentSettings;
	private HashMap<String, String> parentSettings;
	
	private String companyAuthKey;
	
	private Anchor termsLink = new Anchor("Terms of Service and Privacy");
	
	private Anchor logoLink = new Anchor();
	private Anchor bannerLink = new Anchor();
	
	private FlowPanel linksPanel = new FlowPanel();
	private Anchor newsLink = new Anchor("News!");
	private Anchor tutorialLink = new Anchor("Tutorial");
	private Anchor helpLink = new Anchor("Help");
	private Anchor supportLink = new Anchor("Support");
	private Anchor salesLink = new Anchor("Sales");
//	private Anchor feedbackLink = new Anchor("Feedback");
	
	@UiField SimplePanel mainMenu;
	@UiField SimplePanel accountContainer; 

	@UiField TableElement topTable;
	@UiField TableCellElement logoContainer; 
	@UiField TableCellElement bannerContainer; 
	@UiField SimplePanel companySelectorContainer; 
	@UiField SimplePanel linksContainer; 
	
	@UiField TableCellElement titleContainer; 
	@UiField TableCellElement lastModifiedContainer; 
	@UiField SimplePanel actionContainer; 
	
	@UiField TableCellElement statusContainer;
	@UiField DeckPanel contentDeckPanel;
	@UiField Frame startFrame;
	@UiField SimplePanel contentContainer;

	@UiField TableCellElement termsContainer; 

	public static UiControlBinder getInstance() {
//		try {
//			if (instance == null)
//				instance = new UiControlBinder();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return instance;
	}
	
	public void setContentContainer(SimplePanel contentPanel) {
		contentContainer.add(contentPanel);
	}
	
	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == logoLink) {
			UiEntryPoint.trackAnalyticsEvent("Logo", "clicked", "");
		}
		else if (event.getSource() == bannerLink) {
			UiEntryPoint.trackAnalyticsEvent("Banner", "clicked", "");
		}
		else if (event.getSource() instanceof Anchor) {
			UiEntryPoint.trackAnalyticsEvent(((Anchor) event.getSource()).getText(), "clicked", "");
		}
	}
	
	public UiControlBinder() {
		instance = this; 
		
		// createAndBindUi initializes this.nameSpan
	    initWidget(uiBinder.createAndBindUi(this));

	    mainMenu.add(menuWidget);

	    lastModifiedContainer.appendChild(lastModified.getElement());
	    statusContainer.appendChild(statusBox.getElement());
	    actionContainer.add(actionsWidget);
	    
		Window.addResizeHandler(new ResizeHandler() {	
			@Override
			public void onResize(ResizeEvent event) {
				styleControls();
			}
		});
	    
		initHandlers();
	    styleControls();
	}
	
	public void initHandlers() {
		termsLink.addClickHandler(this);
		logoLink.addClickHandler(this);
		bannerLink.addClickHandler(this);
		newsLink.addClickHandler(this);
		tutorialLink.addClickHandler(this);
		helpLink.addClickHandler(this);
		supportLink.addClickHandler(this);
		salesLink.addClickHandler(this);
	}
	
	private void styleControls() {
		startFrame.getElement().getStyle().setHeight((int)(Window.getClientHeight() - 145), Unit.PX);
		startFrame.getElement().getStyle().setWidth((int)(Window.getClientWidth() - 29), Unit.PX);
		startFrame.getElement().getStyle().setBorderWidth(0, Unit.PX);
		startFrame.getElement().setAttribute("frameBorder", "0px");
		startFrame.getElement().setAttribute("allowTransparency", "true");
		
		contentDeckPanel.getElement().getStyle().setHeight(100, Unit.PCT);
	}
	
	public void bindCompanySelector() {
		CompanySelectorWidget companySelector = CompanySelectorWidget.getInstance();
		companySelectorContainer.add(companySelector);
		companySelector.bindData();
		
		initStartPanel();
	}
	
	public void bindAccountWidget() {
		UserAccountWidget accountWidget = UserAccountWidget.getInstance();
		accountContainer.add(accountWidget);
	}
	
	public void initCompany(CompanyInfo company) {
		loadPNOSettings(company);
		
		bindCompanyAuthKey(company.getAuthKey());
	}
	
	private void loadPNOSettings(CompanyInfo company) {
//		if (company.getSettings() != null) {
			currentSettings = company.getSettings();
			parentSettings = company.getParentSettings();
			if (currentSettings == null) {
				currentSettings = parentSettings;
			}
			
			if (parentSettings == null) {
				parentSettings = currentSettings;
			}
			
			if (currentSettings == null) {
				return;
			}
			
			initWidgets();
			
			addLinksPanel();
			addTermsLink();
			addImages();
		
			// set Google Analytics url
			if (currentSettings.containsKey(ManageSettingsInfo.ANALYTICS_ID)){
				initTracker(currentSettings.get(ManageSettingsInfo.ANALYTICS_ID));
			}
			
//			initAdSenseService("ca-pub-2013654478569194", "RVA");
			
			initStartPanel();
			
			showTutorialSlider();
			
			loadLogoutUrl();
//		}
		
		loadParentPNOSettings();
	}
	
	private void loadLogoutUrl() {
		String logoutUrl = "";
		if(currentSettings.containsKey(ManageSettingsInfo.LOGOUT_URL)) {
			logoutUrl = currentSettings.get(ManageSettingsInfo.LOGOUT_URL);
//			loadLogoutURL(currentSettings.get(ManageSettingsInfo.LOGOUT_URL));
		}
		
		if (RiseUtils.strIsNullOrEmpty(logoutUrl) && parentSettings.containsKey(ManageSettingsInfo.LOGOUT_URL)) {
			logoutUrl = parentSettings.get(ManageSettingsInfo.LOGOUT_URL);
		}	
		
		if (!RiseUtils.strIsNullOrEmpty(logoutUrl)) {
//			UserAccountWidget.getInstance().setRedirectUrl(logoutUrl);
			
			loadLogoutURL(logoutUrl);
		}
	}
	
	private void loadParentPNOSettings() {	
		if (parentSettings != null) {
			if (!getValue(parentSettings, ManageSettingsInfo.ADSENSE_SERVICE_ID).isEmpty()) {
				AdsenseBannerWidget bannerWidget = AdsenseBannerWidget.getInstance();
				bannerContainer.appendChild(bannerWidget.getElement());
				bannerWidget.renderItem(getValue(parentSettings, ManageSettingsInfo.ADSENSE_SERVICE_ID), getValue(parentSettings, ManageSettingsInfo.ADSENSE_SERVICE_SLOT));
			}
			else {
				if (!getValue(parentSettings, ManageSettingsInfo.BANNER_URL).isEmpty()) {
					if (!getValue(parentSettings, ManageSettingsInfo.BANNER_TARGET_URL).isEmpty())
						bannerLink.setHref(getValue(parentSettings, ManageSettingsInfo.BANNER_TARGET_URL));
		
					if (!getValue(parentSettings, ManageSettingsInfo.BANNER_URL).isEmpty()) {
						DivElement div = createImageDiv(getValue(parentSettings, ManageSettingsInfo.BANNER_URL), ManageSettingsInfo.BANNER_WIDTH, ManageSettingsInfo.BANNER_HEIGHT);
						bannerLink.getElement().appendChild(div);
					}
					bannerContainer.appendChild(bannerLink.getElement());
				}
				else if (SelectedCompanyController.getInstance().getUserCompany() != null && 
						(ConfigurationController.getInstance().isRiseCompanyId(SelectedCompanyController.getInstance().getUserCompany().getId()) ||
						ConfigurationController.getInstance().isRiseCompanyId(SelectedCompanyController.getInstance().getUserCompany().getParentId()))) {
					AdsenseBannerWidget bannerWidget = AdsenseBannerWidget.getInstance();
					bannerContainer.appendChild(bannerWidget.getElement());
					bannerWidget.renderItem();
				}
			}
		}
	}
	
	private void bindCompanyAuthKey(String companyAuthKey) {
		this.companyAuthKey = companyAuthKey;
		
		loadStartPanel();
	}
		
	private void initWidgets(){
		logoLink.setTarget(blankTarget);
		bannerLink.setTarget(blankTarget);
		termsLink.setTarget(blankTarget);
		newsLink.setTarget(blankTarget);
		helpLink.setTarget(blankTarget);
		supportLink.setTarget(blankTarget);
		salesLink.setTarget(blankTarget);

		logoLink.setTabIndex(-1);
		bannerLink.setTabIndex(-1);
		termsLink.setTabIndex(-1);
		newsLink.setTabIndex(-1);
		helpLink.setTabIndex(-1);
		supportLink.setTabIndex(-1);
		salesLink.setTabIndex(-1);

		newsLink.getElement().getStyle().setProperty("color", "red");
	}
	
	private void initStartPanel() {
		if (currentSettings != null && SelectedCompanyController.getInstance().getUserCompany() != null) {
			if (SelectedCompanyController.getInstance().getUserCompany().isPno() &&
					currentSettings.containsKey(ManageSettingsInfo.OPERATOR_START_PRESENTATION)) {
				startFramePresentationId = currentSettings.get(ManageSettingsInfo.OPERATOR_START_PRESENTATION);
			}
			else {
				startFramePresentationId = parentSettings.get(ManageSettingsInfo.USER_START_PRESENTATION);
			}
			
			loadStartPanel();
		}
	}
	
	private void addLinksPanel() {
		if (!getValue(currentSettings, ManageSettingsInfo.NEWS_URL).isEmpty()){
			newsLink.setHref(getValue(currentSettings, ManageSettingsInfo.NEWS_URL));
			linksPanel.add(newsLink);
		}
		
		linksPanel.add(new SpacerWidget());
		linksPanel.add(tutorialLink);
		tutorialLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				TutorialWidget.getInstance().show();
			}
		});
		
		if (!getValue(currentSettings, ManageSettingsInfo.HELP_URL).isEmpty()) {
			addActionToLink(helpLink, getValue(currentSettings, ManageSettingsInfo.HELP_URL));
			
			linksPanel.add(new SpacerWidget());
			linksPanel.add(helpLink);
		}
		if (!getValue(currentSettings, ManageSettingsInfo.SUPPORT_EMAIL).isEmpty()) {
			addActionToLink(supportLink, getValue(currentSettings, ManageSettingsInfo.SUPPORT_EMAIL));
			
			linksPanel.add(new SpacerWidget());
			linksPanel.add(supportLink);
		}
		if (!getValue(currentSettings, ManageSettingsInfo.SALES_EMAIL).isEmpty()) {
			addActionToLink(salesLink, getValue(currentSettings, ManageSettingsInfo.SALES_EMAIL));
			
			linksPanel.add(new SpacerWidget());
			linksPanel.add(salesLink);
		}
		addFeedbackLink();
		
		linksContainer.add(linksPanel);
	}
	
	private void addActionToLink(Anchor link, final String target) {
		if (RiseUtils.isEmail(target)) {
			link.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					EmailWidget.getInstance().show(target);
				}
			});	
		}
		else {
			link.setHref(target);
		}
	}
	
	private void addTermsLink() {	
		termsContainer.appendChild(termsLink.getElement());
		termsLink.setHref(ConfigurationController.getInstance().getConfiguration().getTermsURL());
		//termsLink.setHref(Globals.TERMS_URL);
		
//		if (!getValue(currentSettings, ManageSettingsInfo.COMPANY_REGISTRATION_TERMS).isEmpty())
//			termsLink.setHref(getValue(currentSettings, ManageSettingsInfo.COMPANY_REGISTRATION_TERMS));
	}
	
	private void addFeedbackLink() {
//		feedbackLink.getElement().setAttribute("onclick", "feedback_widget.show();");
//		
//		linksPanel.add(new SpacerWidget());
//		linksPanel.add(feedbackLink);
	}
	
	private void addImages() {
		if (!getValue(currentSettings, ManageSettingsInfo.LOGO_TARGET_URL).isEmpty())
			logoLink.setHref(getValue(currentSettings, ManageSettingsInfo.LOGO_TARGET_URL));

		if (!getValue(currentSettings, ManageSettingsInfo.LOGO_URL).isEmpty()) {
			DivElement div = createImageDiv(getValue(currentSettings, ManageSettingsInfo.LOGO_URL), ManageSettingsInfo.LOGO_WIDTH, ManageSettingsInfo.LOGO_HEIGHT);
			logoLink.getElement().appendChild(div);
		}
		logoContainer.appendChild(logoLink.getElement());
		
//		if (!getValue(currentSettings, ManageSettingsInfo.BANNER_URL).isEmpty()) {
//			DivElement div = createImageDiv(getValue(currentSettings, ManageSettingsInfo.BANNER_URL), ManageSettingsInfo.BANNER_WIDTH, ManageSettingsInfo.BANNER_HEIGHT);
//			bannerContainer.appendChild(div);
//		}
		
		if (!getValue(currentSettings, ManageSettingsInfo.BANNER_BACKGROUND_COLOR).isEmpty())
			topTable.getStyle().setProperty("background", getValue(currentSettings, ManageSettingsInfo.BANNER_BACKGROUND_COLOR));
	}
	
	public void showStartContainer(boolean show) {
		if (show) {
			contentDeckPanel.showWidget(0);
			loadStartPanel();
		}
		else 
			contentDeckPanel.showWidget(1);
	}
	
	private void loadStartPanel() {
		if (!startFrameLoaded && !RiseUtils.strIsNullOrEmpty(startFramePresentationId) && companyAuthKey != null && startFrame.isVisible()) {
			startFrame.setUrl(ConfigurationController.getInstance().getConfiguration().getViewerURL() + "Viewer.html?type=presentation&id=" + startFramePresentationId
				+ "&showui=false&CompanyAuthKey=" + companyAuthKey);
			//startFrame.setUrl(Globals.VIEWER_URL + "Viewer.html?type=presentation&id=" + startFramePresentationId);
			startFrameLoaded = true;
		}
	}
	
	private void showTutorialSlider() {
		if (!getValue(currentSettings, ManageSettingsInfo.TUTORIAL_URL).isEmpty()) {
			TutorialWidget.getInstance().setVideoUrl(getValue(currentSettings, ManageSettingsInfo.TUTORIAL_URL));
		}
		else if (!getValue(parentSettings, ManageSettingsInfo.TUTORIAL_URL).isEmpty()) {
			TutorialWidget.getInstance().setVideoUrl(getValue(parentSettings, ManageSettingsInfo.TUTORIAL_URL));
		}
		else {
			tutorialLink.setVisible(false);
		}
		
		if (UserAccountController.getInstance().getUserInfo() != null 
				&& UserAccountController.getInstance().getUserInfo().isShowTutorial()) {
//			if (!currentSettings.containsKey(ManageSettingsInfo.SHOW_TUTORIAL) 
//				|| !"false".equals(currentSettings.get(ManageSettingsInfo.SHOW_TUTORIAL))) {
				TutorialWidget.getInstance().show();
//			}
		}
	}
	
	private DivElement createImageDiv(String imageUrl, int width, int height) {
		DivElement div = Document.get().createDivElement();	 
		div.getStyle().setPropertyPx("width", width);
		div.getStyle().setPropertyPx("height", height);
		
		div.getStyle().setProperty("backgroundImage", "url(" + imageUrl + ")");
		div.getStyle().setProperty("backgroundPosition", "0% 50%");
		div.getStyle().setProperty("backgroundSize", "auto");
		div.getStyle().setProperty("backgroundRepeat", "no-repeat"); 
		
		return div;
	}

	private String getValue(HashMap<String, String> map, String key){
		if (map.containsKey(key) && map.get(key) != null){
			return map.get(key);
		}
		return "";
	}
	
	private void loadLogoutURL(String logoutURL) {
		if (logoutURL != null && !logoutURL.isEmpty()) {
			UserServiceAsync userService = GWT.create(UserService.class);

			userService.getLogoutURL(logoutURL, new RpcGetLogoutURLCallBackHandler());
		}
	}
	
	private void updateLogoutURL(String logoutURL) {
//		UserAccountWidget.getInstance().setLogoutUrl(logoutURL);
		
		UserAccountWidget.getInstance().setRedirectUrl(logoutURL);
	}
	
	class RpcGetLogoutURLCallBackHandler implements AsyncCallback<String> {
		public void onFailure(Throwable caught) {
		}

		public void onSuccess(String result) {
			if (result != null && !result.isEmpty()) {
				updateLogoutURL(result);
			}
		}
	}
	
	private static native void initTracker(String trackerID) /*-{
		try {
			$wnd.initTracker(trackerID);
		} catch (err) {}
	}-*/;
}
