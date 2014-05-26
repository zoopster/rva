// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.tutorial;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.risevision.core.api.settings.CompanySetting;
import com.risevision.ui.client.UiEntryPoint;
import com.risevision.ui.client.common.controller.SelectedCompanyController;
import com.risevision.ui.client.common.controller.UserAccountController;
import com.risevision.ui.client.common.info.CompanyInfo;
import com.risevision.ui.client.common.info.RpcResultInfo;
import com.risevision.ui.client.common.info.UserInfo;
import com.risevision.ui.client.common.service.UserService;
import com.risevision.ui.client.common.service.UserServiceAsync;
import com.risevision.ui.client.common.widgets.SpacerWidget;

public class TutorialWidget extends PopupPanel {
	private static TutorialWidget instance;
	
	private AbsolutePanel mainPanel = new AbsolutePanel();
	private HorizontalPanel linksPanel = new HorizontalPanel();
    private Frame videoFrame = new Frame();
	private HTML closePanel = new HTML("<span style='cursor:pointer;font-size:26px;'>&times;</span>");
//	private Image startVideoImage = new Image("images/help-video.jpg");
//	private String videoUrl = "http://www.youtube.com/embed/1QJygspi8MA?autoplay=0";
	private String videoUrl = "http://www.youtube.com/embed/fgPqkDfcLx4?autoplay=0";
	
	private CheckBox tutorialCheckbox = new CheckBox();
	private HorizontalPanel emailUpdatesPanel = new HorizontalPanel();
	private CheckBox emailUpdatesCheckbox = new CheckBox();
	private UserServiceAsync userService = GWT.create(UserService.class);
	
	public static TutorialWidget getInstance() {
		try {
			if (instance == null)
				instance = new TutorialWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}
	
    public TutorialWidget() {
    	super(false, false);
		
		mainPanel.add(videoFrame, 0, 7);
//		mainPanel.add(startVideoImage, 285, 9);
		mainPanel.add(closePanel, 635, -10);
		mainPanel.add(linksPanel, 0, 405);
		
		linksPanel.setWidth("100%");
		linksPanel.add(new Label("Don't show me this again", false));
		linksPanel.add(new SpacerWidget());
		linksPanel.add(tutorialCheckbox);
		linksPanel.setCellWidth(tutorialCheckbox, "100%");

		linksPanel.add(emailUpdatesPanel);
		emailUpdatesPanel.add(new Label("Subscribe to email updates", false));
		emailUpdatesPanel.add(new SpacerWidget());
		emailUpdatesPanel.add(emailUpdatesCheckbox);
		
		add(mainPanel);
		
        styleControls();
        initActions();
        
    }
    
    private void styleControls() {  
//    	linksPanel.getElement().getStyle().setPadding(10, Unit.PX);
//    	linksPanel.setSize("640px", "5px");
//    	linksPanel.addStyleName("tutorial-inner-gradient");
//    	linksPanel.addStyleName("tutorial-gradient-overlay-middle");
    	linksPanel.getElement().getStyle().setColor("white");
    	emailUpdatesPanel.getElement().getStyle().setColor("white");
    	
    	tutorialCheckbox.addStyleName("rdn-CheckBox");
    	tutorialCheckbox.getElement().getStyle().setFontSize(13, Unit.PX);

    	emailUpdatesCheckbox.addStyleName("rdn-CheckBox");
    	emailUpdatesCheckbox.getElement().getStyle().setFontSize(13, Unit.PX);

    	videoFrame.setSize("640px", "385px");
    	videoFrame.getElement().setPropertyInt("frameBorder", 0);
    	videoFrame.getElement().getStyle().setBackgroundColor("black");
    	
//    	startVideoImage.setSize("640px", "385px");
//    	startVideoImage.getElement().getStyle().setCursor(Cursor.POINTER);
    	
    	mainPanel.getElement().getStyle().setOverflow(Overflow.VISIBLE);
    	
    	setSize("645px", "428px");

		addStyleName("tutorial-rounded-border");
		addStyleName("tutorial-gradient-overlay-up");
    }
    
	private void initActions() {
		closePanel.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				videoFrame.setUrl("");

				hide();
			}
		});
		
//		startVideoImage.addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				playVideo();
//			}
//		});
	}
	
//	private void playVideo() {
//		trackEvent("Play", "Tutorial");
//		startVideoImage.setVisible(false);
//    	videoFrame.setUrl("http://www.youtube.com/embed/" + videoId + "?autoplay=0");
//	}
	
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	
	public void show() {
		super.show();
		center();
		
		trackEvent("Show", "");
		videoFrame.setUrl(videoUrl);
//		startVideoImage.setVisible(true);
		
//		videoFrame.setUrl("http://www.youtube.com/embed/p/444CCAE310D039D2?autoplay=0&enablejsapi=1");

//		setPopupPosition(Window.getClientWidth() - getOffsetWidth()+ 5, 85);
		
		bindData();
	}
	
	private void bindData() {
		UserInfo user = UserAccountController.getInstance().getUserInfo();

		if (user != null) {
			tutorialCheckbox.setValue(!user.isShowTutorial());
			emailUpdatesCheckbox.setValue(user.isMailSyncEnabled());
		}
		
		CompanyInfo company = SelectedCompanyController.getInstance().getUserCompany();
		emailUpdatesPanel.setVisible(company.getParentSettings() == null || 
				!company.getParentSettings().containsKey(CompanySetting.MAIL_SYNC_ENABLED) ||
				!"false".equals(company.getParentSettings().get(CompanySetting.MAIL_SYNC_ENABLED)));
	}

	public void hide() {
		saveData();
		
		trackEvent("Hide", "");
		
		super.hide();
	}
	
	private void saveData() {
		boolean requiresUpdate = false;
		
		UserInfo user = UserAccountController.getInstance().getUserInfo();
		if (tutorialCheckbox.getValue() == user.isShowTutorial()) {		
			user.setShowTutorial(!tutorialCheckbox.getValue());
			
			requiresUpdate = true;
		}
		
		if (emailUpdatesCheckbox.getValue() != user.isMailSyncEnabled()) {
			user.setMailSyncEnabled(emailUpdatesCheckbox.getValue());
			
			requiresUpdate = true;
		}
		
		if (requiresUpdate) {
			user.setLastLogin(null);
			userService.putUser(user.getCompany(), user, new RpcSaveUserCallBackHandler());
		}
	}
	
	private void trackEvent(String action, String label) {
		UiEntryPoint.trackAnalyticsEvent("Tour", action, label);
	}
	
	class RpcSaveUserCallBackHandler implements AsyncCallback<RpcResultInfo> {
		public void onFailure(Throwable caught) {
		}

		public void onSuccess(RpcResultInfo result) {
			if (result != null) {

			}
		}
	}
	
}
