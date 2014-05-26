package com.risevision.ui.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.ui.client.common.ContentId;
import com.risevision.ui.client.common.controller.PrerequisitesController;
import com.risevision.ui.client.common.controller.SelectedCompanyController;
import com.risevision.ui.client.common.info.HistoryTokenInfo;
import com.risevision.ui.client.common.widgets.ActionsWidget;
import com.risevision.ui.client.common.widgets.LastModifiedWidget;
import com.risevision.ui.client.common.widgets.MenuWidget;
import com.risevision.ui.client.company.CompanySelectorWidget;
import com.risevision.ui.client.company.CompanyWidget;
import com.risevision.ui.client.company.ManagePortalWidget;
import com.risevision.ui.client.display.DisplayManageWidget;
import com.risevision.ui.client.display.DisplaysWidget;
import com.risevision.ui.client.gadget.GadgetManageWidget;
import com.risevision.ui.client.gadget.GadgetsWidget;
import com.risevision.ui.client.presentation.PresentationManageWidget;
import com.risevision.ui.client.presentation.PresentationsWidget;
import com.risevision.ui.client.schedule.ScheduleManageWidget;
import com.risevision.ui.client.schedule.SchedulesWidget;
import com.risevision.ui.client.user.UserManageWidget;
import com.risevision.ui.client.user.UsersWidget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class UiEntryPoint implements EntryPoint, ValueChangeHandler<String> {
	private static final String HASH_PARAM_ID = "id";
	private static final String HASH_PARAM_COMPANY = "company";
	public static final String HASH_PARAM_FROM_COMPANY_ID = "fromCompanyId";
	private static final String HASH_PARAM_BOOKMARK = "bookmark";
	
	private static UiEntryPoint instance;
	private HandlerRegistration handler;
	
//	private final EventBus eventBus = new SimpleEventBus();
//	private final ReloadOnAuthenticationFailure reloadOnAuthenticationFailure;
	
	private Label titleLabel = new Label();
	private UiControlBinder uiControlBinder;
	private SimplePanel contentPanel = new SimplePanel();
	private MenuWidget menuWidget = MenuWidget.getInstance();
	private ActionsWidget actionsWidget = ActionsWidget.getInstance(); 
	private LastModifiedWidget lastModified = LastModifiedWidget.getInstance();
	private CompanySelectorWidget companySelectorWidget;

	public static UiEntryPoint getInstance(){
		return instance;
	}
	
	public UiEntryPoint() {
//		this.reloadOnAuthenticationFailure = new ReloadOnAuthenticationFailure();
	}
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		instance = this;
		
		// Check for authentication failures or mismatches
//		reloadOnAuthenticationFailure.register(eventBus);
		
		uiControlBinder = new UiControlBinder();
		uiControlBinder.setContentContainer(contentPanel);
		RootPanel.get("main").add(uiControlBinder);
		
		PrerequisitesController prereqController = PrerequisitesController.getInstance();
		Command cmdPrerequisiteLoaded = new Command() {
			public void execute() {
				onPrerequisitesLoaded();
			}
		};
		
		prereqController.load(cmdPrerequisiteLoaded);
	}

	private void onPrerequisitesLoaded() {
		companySelectorWidget = CompanySelectorWidget.getInstance();
		initHistoryState();
	}
		
	public void reloadContent() {
		String historyToken = History.getToken();
    	String[] tokens = historyToken.split("/");
    	String companyId = "";

		for (String token: tokens) {
    		String[] keyValuePair = token.split("=");
    		
    		if (keyValuePair.length == 2 && keyValuePair[0].equals("company")) {
    			companyId = keyValuePair[1];
    		}
		}
		
		if (!companyId.isEmpty() && !companyId.equals(SelectedCompanyController.getInstance().getSelectedCompanyId())) {
			History.newItem(historyToken.replace(companyId, SelectedCompanyController.getInstance().getSelectedCompanyId()));
			return;
		}
    	
		if (ContentId.MANAGE_PORTAL.contains(historyToken)) {
			History.newItem(ContentId.HOME);
			return;
		}

		onHistoryChange(historyToken);
	}
	
	public static void loadContentStatic(HistoryTokenInfo tokenInfo, boolean triggerEvent) {
		if (triggerEvent || instance.handler == null) {
			loadContentStatic(tokenInfo);
		}
		else {
			instance.handler.removeHandler();
			loadContentStatic(tokenInfo);
			instance.handler = History.addValueChangeHandler(instance);
		}
	}
	
	public static void loadContentStatic(String contentId) {
		HistoryTokenInfo tokenInfo = new HistoryTokenInfo();
		
		tokenInfo.setContentId(contentId);
		
		loadContentStatic(tokenInfo);
	}

	public static void loadContentStatic(HistoryTokenInfo tokenInfo) {
		String historyToken = tokenInfo.getContentId();
		if (!RiseUtils.strIsNullOrEmpty(tokenInfo.getId())) {
			historyToken += "/" + HASH_PARAM_ID + "=" + tokenInfo.getId();
		}
		
		String companyId;
		if (!RiseUtils.strIsNullOrEmpty(tokenInfo.getCompanyId())) {
			companyId = tokenInfo.getCompanyId();
		}
		else {
			companyId = SelectedCompanyController.getInstance().getSelectedCompanyId();
		}
		historyToken += "/" + HASH_PARAM_COMPANY + "=" + companyId;
		
		if (!RiseUtils.strIsNullOrEmpty(tokenInfo.getFromCompanyId())) {
			historyToken += "/" + HASH_PARAM_FROM_COMPANY_ID + "=" + tokenInfo.getFromCompanyId();
		}
		
		if (!RiseUtils.strIsNullOrEmpty(tokenInfo.getBookmark())) {
			historyToken += "/" + HASH_PARAM_BOOKMARK + "=" + tokenInfo.getBookmark();
		}
		
		History.newItem(historyToken, true);
	}
	
	private void loadContent(HistoryTokenInfo tokenInfo) {		
		String contentId = tokenInfo.getContentId();
		if (contentId == null)
			contentId = "";

		trackEvent(contentId);
		
		contentPanel.clear();
		actionsWidget.clearActions();
		
		companySelectorWidget.setCanChangeCompany(true);
		titleLabel.setText("");
		lastModified.Hide();
		uiControlBinder.showStartContainer(false);
		
		if (contentId.equals(ContentId.COMPANY_MANAGE)) {
			titleLabel.setText("Company");
			menuWidget.setActiveLink(ContentId.COMPANY_MANAGE);
			CompanyWidget company = CompanyWidget.getInstance();
			company.setToken(tokenInfo);
			contentPanel.add(company);
		} else if (contentId.equals(ContentId.USERS)) {
			titleLabel.setText("Users");
			menuWidget.setActiveLink(ContentId.USERS);
			UsersWidget users = UsersWidget.getInstance();
			contentPanel.add(users);
		} else if (contentId.equals(ContentId.USER_MANAGE)) {
			titleLabel.setText("User");
			menuWidget.setActiveLink(ContentId.USERS);
			companySelectorWidget.setCanChangeCompany(false);
			UserManageWidget userManage = UserManageWidget.getInstance();
			userManage.setToken(tokenInfo);
			contentPanel.add(userManage);
		} else if (contentId.equals(ContentId.SCHEDULES)) {
			titleLabel.setText("Schedules");
			menuWidget.setActiveLink(ContentId.SCHEDULES);
			SchedulesWidget schedules = SchedulesWidget.getInstance();
			contentPanel.add(schedules);
		} else if (contentId.equals(ContentId.SCHEDULE_MANAGE)) {
			titleLabel.setText("Schedule");
			menuWidget.setActiveLink(ContentId.SCHEDULES);
			companySelectorWidget.setCanChangeCompany(false);
			ScheduleManageWidget scheduleManage = ScheduleManageWidget.getInstance();
			scheduleManage.setToken(tokenInfo);
			contentPanel.add(scheduleManage);
		} else if (contentId.equals(ContentId.GADGETS)) {
			titleLabel.setText("Gadgets");
			menuWidget.setActiveLink(ContentId.GADGETS);
			GadgetsWidget gadgets = GadgetsWidget.getInstance();
			contentPanel.add(gadgets);
		} else if (contentId.equals(ContentId.GADGET_MANAGE)) {
			titleLabel.setText("Gadget");
			menuWidget.setActiveLink(ContentId.GADGETS);
			companySelectorWidget.setCanChangeCompany(false);
			GadgetManageWidget gadgetManage = GadgetManageWidget.getInstance();
			gadgetManage.setToken(tokenInfo);
			contentPanel.add(gadgetManage);
		} else if (contentId.equals(ContentId.PRESENTATIONS)) {
			titleLabel.setText("Presentations");
			menuWidget.setActiveLink(ContentId.PRESENTATIONS);
			PresentationsWidget presentations = PresentationsWidget.getInstance();
			contentPanel.add(presentations);
		} else if (contentId.equals(ContentId.PRESENTATION_MANAGE)) {
			titleLabel.setText("Presentation");
			menuWidget.setActiveLink(ContentId.PRESENTATIONS);
			companySelectorWidget.setCanChangeCompany(false);
			PresentationManageWidget presentationManage = PresentationManageWidget.getInstance();
			presentationManage.setToken(tokenInfo);
			contentPanel.add(presentationManage);
		} else if (contentId.equals(ContentId.DISPLAYS)) {
			titleLabel.setText("Displays");
			menuWidget.setActiveLink(ContentId.DISPLAYS);
			DisplaysWidget displays = DisplaysWidget.getInstance();
			contentPanel.add(displays);
		} else if (contentId.equals(ContentId.DISPLAY_MANAGE)) {
			titleLabel.setText("Display");
			menuWidget.setActiveLink(ContentId.DISPLAYS);
			companySelectorWidget.setCanChangeCompany(false);
			DisplayManageWidget displayManage = DisplayManageWidget.getInstance();
			displayManage.setToken(tokenInfo);
			contentPanel.add(displayManage);
		} else if (contentId.equals(ContentId.MANAGE_PORTAL)) {
			titleLabel.setText("Network");
			menuWidget.setActiveLink(ContentId.MANAGE_PORTAL);
			//companySelectorWidget.setCanChangeCompany(false);
			ManagePortalWidget managePortal = ManagePortalWidget.getInstance();
			contentPanel.add(managePortal);
		} else {
			titleLabel.setText("Start");
			menuWidget.setActiveLink(ContentId.HOME);
			uiControlBinder.showStartContainer(true);
		}
		
		AdsenseBannerWidget.getInstance().updateBanner();
	}

    private void initHistoryState(){
        // Add history listener
        handler = History.addValueChangeHandler(this);

        String token = History.getToken();
 
        if(token.length() == 0){
            onHistoryChange(ContentId.HOME);
        }else{
            onHistoryChange(token);
        }
    }

    private void onHistoryChange(String token){
    	String[] tokens = token.split("/");
    	String companyId = "";
    	HistoryTokenInfo historyToken = new HistoryTokenInfo();
    	
    	for (String param: tokens) {
			if (!param.contains("=")) {
				historyToken.setContentId(param);
			} 
			else {
	    		String[] keyValuePair = param.split("=");
	
	    		if (keyValuePair.length == 2) {
	    			if (keyValuePair[0].toLowerCase().equals(HASH_PARAM_COMPANY)) {
	    				companyId = keyValuePair[1]; 
	        			historyToken.setCompanyId(keyValuePair[1]);
	    			}
	    			else if (keyValuePair[0].toLowerCase().equals(HASH_PARAM_ID)) {
	        			historyToken.setId(keyValuePair[1]);
	    			}
	    			else if (keyValuePair[0].toLowerCase().equals(HASH_PARAM_FROM_COMPANY_ID.toLowerCase())) {
	        			historyToken.setFromCompanyId(keyValuePair[1]);
	    			}
	    			else if (keyValuePair[0].toLowerCase().equals(HASH_PARAM_BOOKMARK.toLowerCase())) {
	        			historyToken.setBookmark(keyValuePair[1]);
	    			}
	    		}
			}
    	}
    	
//    	String contentId = "", companyId = "";
//    	String[] params = {"", ""};
//    	
//    	for (String param: tokens) {
//    		if (!param.contains("=")) {
//    			contentId = param;
//    		} 
//    		else {
//        		String[] keyValuePair = param.split("=");
//
//        		if (keyValuePair.length == 2) {
//        			if (keyValuePair[0].toLowerCase().equals(HASH_PARAM_COMPANY)) {
//            			companyId = keyValuePair[1];
//        			}
//        			else if (keyValuePair[0].toLowerCase().equals(HASH_PARAM_ID)) {
//            			params[0] = keyValuePair[1];
//        			}
//        			else if (keyValuePair[0].toLowerCase().equals(HASH_PARAM_FROM_COMPANY_ID.toLowerCase())) {
//            			params[1] = keyValuePair[1];
//        			}
//        		}
//    		}
//    	}
    	
    	if (!companyId.isEmpty() && !companyId.equals(SelectedCompanyController.getInstance().getSelectedCompanyId())) {
    		SelectedCompanyController.getInstance().setSelectedCompany(companyId);
    	}
    	else {
    		loadContent(historyToken);
    	}
    }
	
	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
        onHistoryChange((String)event.getValue());
	}
	
	private static void trackEvent(String contentId) {
		trackAnalyticsEvent(contentId, "clicked", "");
	}
	
	public static native void trackAnalyticsEvent(String eventName, String eventAction, String eventLabel) /*-{
	try {
			$wnd.trackEvent(eventName, eventAction, eventLabel);
		} catch (err) {}
	}-*/;	
	
//	private static native void trackEvent(String contentId) /*-{
//		try {
//			$wnd.trackEvent(contentId);
//		} catch (err) {}
//	}-*/;

	public static native void trackPageview(String contentId) /*-{
//		try {
			$wnd.trackPageview(contentId);
//		} catch (err) {}
	}-*/;
	
	public static native void trackConversion() /*-{
//		try {
			$wnd.attachConversionScript();
//		} catch (err) {}
	}-*/;
}
