// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.user;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.risevision.ui.client.UiEntryPoint;
import com.risevision.ui.client.common.ContentId;
import com.risevision.ui.client.common.controller.UserAccountController;
import com.risevision.ui.client.common.info.HistoryTokenInfo;
import com.risevision.ui.client.common.info.UserInfo;
import com.risevision.ui.client.common.widgets.SpacerWidget;
import com.risevision.ui.client.common.widgets.messages.SystemMessagesWidget;

public class UserAccountWidget extends FlowPanel {
	private static UserAccountWidget instance;
	private Anchor usernameLink = new Anchor("");
	private Anchor logoutLink = new Anchor("Sign Out");
	
	private String redirectUrl;
//	private String logoutUrl;
	
	public static UserAccountWidget getInstance() {
		try {
			if (instance == null)
				instance = new UserAccountWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}

	public UserAccountWidget(){
		initActions();
		
		add(SystemMessagesWidget.getInstance());
		add(new SpacerWidget());
		add(usernameLink);
		add(new SpacerWidget());
		logoutLink.setVisible(false);
		add(logoutLink);
		
		usernameLink.setTabIndex(-1);
		logoutLink.setTabIndex(-1);
	}

	private void initActions() {
		usernameLink.addClickHandler(new ClickHandler() {			
			public void onClick(ClickEvent event) {
				navigateToUserPage();
			}
		});		
		
		logoutLink.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				UiEntryPoint.trackAnalyticsEvent("Logout", "clicked", "");

				logoutUser();
			}
		});
	}
	
	public void setUsername(UserInfo user){
		if (user != null) {
			usernameLink.setText(user.getUserName());
		}
	}
	
	public void setRedirectUrl(String redirectUrl) {
		if (redirectUrl != null && !redirectUrl.isEmpty()) {
			this.redirectUrl = redirectUrl;
			
			logoutLink.setVisible(true);
		}
	}
	
//	public void setLogoutUrl(String logoutUrl) {
//		this.logoutUrl = logoutUrl;
//		
//		logoutLink.setHref(logoutUrl);
////		logoutLink.setVisible(true);
//	}
	
	public void logoutUser() {
//		Window.Location.replace(redirectUrl);
		Window.Location.assign(redirectUrl);
	}
	
	protected void navigateToUserPage() {
		UserInfo user = UserAccountController.getInstance().getUserInfo();
		
		HistoryTokenInfo tokenInfo = new HistoryTokenInfo();
		tokenInfo.setId(user.getId());
		tokenInfo.setContentId(ContentId.USER_MANAGE);
//		String[] params = {user.getId()};
		
		UiEntryPoint.loadContentStatic(tokenInfo);
	}
}
