// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.risevision.ui.client.common.controller.SelectedCompanyController;
import com.risevision.ui.client.common.exception.RiseAsyncCallback;
import com.risevision.ui.client.common.service.CompanyService;
import com.risevision.ui.client.common.service.CompanyServiceAsync;
import com.risevision.ui.client.common.widgets.SpacerWidget;
import com.risevision.ui.client.common.widgets.StatusBoxWidget;

public class AuthKeyWidget extends HorizontalPanel {
	//RPC
	private final CompanyServiceAsync companyService = GWT.create(CompanyService.class);
	// UI Pieces
	private StatusBoxWidget statusBox = StatusBoxWidget.getInstance();
	private Label authKeyLabel = new Label();
	private Anchor resetKeyLink = new Anchor("Reset");
	
	public AuthKeyWidget() {
		add(authKeyLabel);
		add(new SpacerWidget());
		add(resetKeyLink);
		
		initActions();
		styleControls();
	}
	
	private void initActions() {		
		resetKeyLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				resetAuthKey();
			}
		});
	}
	
	private void styleControls() {
		authKeyLabel.addStyleName("rdn-OverflowElipsis");
		authKeyLabel.setWidth("175px");
	}

	public void initWidget(String authKey) {
		authKeyLabel.setText(authKey);
	}
	
	public String getAuthKey() {
		return authKeyLabel.getText();
	}
	
//	protected void onLoad() {
//		loadAuthKey();
//	}

//	private void loadAuthKey(){	
//		companyService.getCompanyAuthKey(SelectedCompanyController.getInstance().getSelectedCompanyId(), new RpcCallBackHandler());
//	}
	
	private void resetAuthKey() {
		if (Window.confirm("Resetting the Company Authentication Key will cause existing Data Gadgets to no longer report data until they are updated with the new Key.")) {
			companyService.resetCompanyAuthKey(SelectedCompanyController.getInstance().getSelectedCompanyId(), new RpcCallBackHandler());
		}
	}
	
	class RpcCallBackHandler extends RiseAsyncCallback<String> {
		public void onFailure() {
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(String result) {
			statusBox.clear();
			if (result != null)
				authKeyLabel.setText(result);
		}
	}
}
