package com.risevision.ui.client.common.widgets.tutorial;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.risevision.ui.client.UiEntryPoint;
import com.risevision.ui.client.common.controller.UserAccountController;
import com.risevision.ui.client.common.info.RpcResultInfo;
import com.risevision.ui.client.common.info.UserInfo;
import com.risevision.ui.client.common.service.UserService;
import com.risevision.ui.client.common.service.UserServiceAsync;

public class TutorialSliderWidget extends PopupPanel {
//	private static final String OVERVIEW_VIDEO = "NlrH1dp1W2o";
	private static final String OVERVIEW_VIDEO = "QagsiDIpT8k";

	private static TutorialSliderWidget instance;
	
	private AbsolutePanel mainPanel = new AbsolutePanel();
	private VerticalPanel linksPanel = new VerticalPanel();
    private Frame videoFrame = new Frame();
	private HTML closePanel = new HTML("<span style='cursor:pointer;font-size:26px;'>&times;</span>");
	private Image startVideoImage = new Image("images/help-video.jpg");
	
//	private AbsolutePanel tutorialPanel = new AbsolutePanel();
//	private HTML tutorialButton = new HTML("<span style='cursor:pointer;font-size:16px;font-weight:bolder;' class='sideways-text'>" +
//		"Tutorial</span>");
	private CheckBox dontShowCheckbox = new CheckBox("I'm now a genius, don't show me this anymore.");
	private UserServiceAsync userService = GWT.create(UserService.class);
	
//	private JavaScriptObject playerObject;

	public static TutorialSliderWidget getInstance() {
		try {
			if (instance == null)
				instance = new TutorialSliderWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}
	
    public TutorialSliderWidget() {
    	super(false, false);
		
		linksPanel.add(createAndStyleLabel("Take The Tour"));
		linksPanel.add(createAndStyleLink("The Two Minute Overview", OVERVIEW_VIDEO, 0));
		linksPanel.add(new HTML("<span style='padding-top: 5px;'></span>"));
		
		linksPanel.add(createAndStyleLabel("Be One With Your Network"));
		linksPanel.add(createAndStyleLink("Create a Presentation from a Template", "ybA8H4bTZrA", 1));
		linksPanel.add(createAndStyleLink("Create a Presentation from Scratch", "GgIYYtj6umI", 2));
		linksPanel.add(createAndStyleLink("Add a Display", "-dwV_PnVkjA", 3));
		linksPanel.add(createAndStyleLink("Schedule a Presentation", "mYkKdnd2K6Q", 4));
		linksPanel.add(createAndStyleLink("Setup your Company and Users", "eAzYcZJLRuQ", 5));
		linksPanel.add(new HTML("<span style='padding-top: 5px;'></span>"));

		linksPanel.add(createAndStyleLabel("Rule The World!"));
		linksPanel.add(createAndStyleLink("Be a Network Operator", "34QErw4_wdY", 6));
		linksPanel.add(createAndStyleLink("Create Multi-Site Presentations", "yiolDjxshFw", 7));
		linksPanel.add(createAndStyleLink("Build your own Gadgets", "ePUOdgt7Zw0", 8));
		linksPanel.add(new HTML("<span style='padding-top: 5px;'></span>"));

		linksPanel.add(dontShowCheckbox);
//		linksPanel.add(closePanel);
//		mainPanel.setCellHorizontalAlignment(closePanel, HasHorizontalAlignment.ALIGN_RIGHT);
		
//		tutorialPanel.add(tutorialButton, -23, 28);
		mainPanel.add(linksPanel, -1, 7);
		mainPanel.add(videoFrame, 283, 7);
		mainPanel.add(startVideoImage, 285, 9);
		mainPanel.add(closePanel, 915, -10);
		
		add(mainPanel);
		
        styleControls();
        initActions();
        
//        videoFrame.getElement().setId("youtubeFrame");
        
//        loadYoutubeAPI();
//        exportStaticMethods();
    }
    
    private Label createAndStyleLabel(String text) {
    	Label label = new Label(text);
    	styleText(label);
//    	label.addStyleName("");
    	
    	return label;
    }
    
    private Anchor createAndStyleLink(String text, final String videoId, final int videoIndex) {
    	Anchor link = new Anchor(text);
//    	styleText(link);
    	link.getElement().getStyle().setMarginLeft(8, Unit.PX);
//    	link.addStyleName("");
    	
    	link.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				playVideo(videoId);
			}
		});
    	
    	return link;
    }
    
    private void styleText(Widget widget) {
//    	widget.getElement().getStyle().setFontSize(13, Unit.PX);
    	widget.getElement().getStyle().setFontWeight(FontWeight.BOLDER);
    }
    
    private void styleControls() {  
    	linksPanel.getElement().getStyle().setPadding(10, Unit.PX);
    	linksPanel.setSize("275px", "389px");
    	linksPanel.addStyleName("tutorial-inner-gradient");
    	linksPanel.addStyleName("tutorial-gradient-overlay-middle");
    	
    	dontShowCheckbox.addStyleName("rdn-CheckBox");
    	dontShowCheckbox.getElement().getStyle().setFontSize(13, Unit.PX);

    	videoFrame.setSize("640px", "385px");
    	videoFrame.getElement().setPropertyInt("frameBorder", 0);
    	videoFrame.getElement().getStyle().setBackgroundColor("black");
    	
    	startVideoImage.setSize("640px", "385px");
    	startVideoImage.getElement().getStyle().setCursor(Cursor.POINTER);
    	
    	mainPanel.getElement().getStyle().setOverflow(Overflow.VISIBLE);
    	
    	setSize("928px", "398px");

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
		
		startVideoImage.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				playVideo(OVERVIEW_VIDEO);
			}
		});
		
//    	tutorialButton.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				show();
//			}
//		});
	}
	
	private void playVideo(String videoId) {
		trackEvent("Play", videoId);
		startVideoImage.setVisible(false);
    	videoFrame.setUrl("http://www.youtube.com/embed/" + videoId + "?autoplay=1");
//		playerObject = loadYoutubeVideo(videoId);
	}
	
	public void show() {
//		remove(tutorialPanel);
		super.show();
		center();
		
		trackEvent("Show", "");
		startVideoImage.setVisible(true);
		
//		videoFrame.setUrl("http://www.youtube.com/embed/p/444CCAE310D039D2?autoplay=0&enablejsapi=1");

//		setPopupPosition(Window.getClientWidth() - getOffsetWidth()+ 5, 85);
	}

	public void hide() {
		if (dontShowCheckbox.getValue()) {		
			UserInfo user = UserAccountController.getInstance().getUserInfo();
			user.setLastLogin(null);
			user.setShowTutorial(false);
			userService.putUser(user.getCompany(), user, new RpcSaveUserCallBackHandler());
		}
//		else {
//			setSize("12px", "70px");
			
//			remove(mainPanel);
//			add(tutorialPanel);
//		}
		
		trackEvent("Hide", "");
		
		super.hide();
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
	
//	private void reportDoneEvent(int status) {
//		
//	}
	
//	protected static void reportStateChangeStatic(String status) {
//		int iStatus = RiseUtils.strToInt(status, -1);
//		if (iStatus == 0) {
//			instance.reportDoneEvent(iStatus);
//		}
//	}
	
//	private native void loadYoutubeAPI() /*-{
//		var tag = $wnd.document.createElement('script');
//		tag.src = "http://www.youtube.com/player_api";
//		var firstScriptTag = $wnd.document.getElementsByTagName('script')[0];
//		firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);
//	}-*/;
	
//	private native void exportStaticMethods() /*-{
//		$wnd.reportStateChange =
//		$entry(@com.risevision.ui.client.common.widgets.tutorial.TutorialSliderWidget::reportStateChangeStatic(Ljava/lang/String;));
//	}-*/;
	
//	private native JavaScriptObject loadYoutubeVideo(String id) /*-{
////		debugger;
//		
//		var player = new $wnd.YT.Player('youtubeFrame', {
//			events : {
//				'onPlayerReady': new function(event) {
//					debugger;
//					
//					
//				},
//				'onStateChange': new function(data) {
//					debugger; 
//					
//					$wnd.reportStateChange(data);
//				}
//			}
//		});
//		
//		return player;
//	}-*/;
	
//	private native void playYoutubeVideo(int videoIndex) /*-{
//		var player = new $wnd.YT.Player('youtubeFrame', {
//			events : {
//
//			}
//		});
//		
//		debugger;
//		
//		player.playVideoAt(videoIndex);
//	}-*/;

}
