// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.alerts;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.risevision.ui.client.common.controller.SelectedCompanyController;
import com.risevision.ui.client.common.exception.RiseAsyncCallback;
import com.risevision.ui.client.common.service.CompanyService;
import com.risevision.ui.client.common.service.CompanyServiceAsync;
import com.risevision.ui.client.common.widgets.SpacerWidget;
import com.risevision.ui.client.common.widgets.StatusBoxWidget;

public class AlertURLWidget extends HorizontalPanel {
	//RPC
	private final CompanyServiceAsync companyService = GWT.create(CompanyService.class);
	// UI Pieces
	private StatusBoxWidget statusBox = StatusBoxWidget.getInstance();
	private TextBox urlTextBox = new TextBox();
	private Anchor resetLink = new Anchor("Reset");
	
	public AlertURLWidget() {
		add(urlTextBox);
		add(new SpacerWidget());
		add(resetLink);
		
		initActions();
		styleControls();
	}
	
	private void initActions() {		
		resetLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				resetAlertsURL();
			}
		});
	}
	
	private void styleControls() {
		urlTextBox.addStyleName("rdn-TextBoxMedium");
		urlTextBox.setReadOnly(true);
	}

	public void setText(String alertsURL) {
		urlTextBox.setText(alertsURL);
	}
	
	public String getText() {
		return urlTextBox.getText();
	}
	
//	protected void onLoad() {
//		loadAuthKey();
//	}

//	private void loadAuthKey(){	
//		companyService.getCompanyAuthKey(SelectedCompanyController.getInstance().getSelectedCompanyId(), new RpcCallBackHandler());
//	}
	
	private void resetAlertsURL() {
		if (Window.confirm("Resetting the Web Service URL means alert providers can no longer connect to the service until they are updated with the new URL.")) {
			companyService.resetAlertsURL(SelectedCompanyController.getInstance().getSelectedCompanyId(), new RpcCallBackHandler());
		}
	}
	
	class RpcCallBackHandler extends RiseAsyncCallback<String> {
		public void onFailure() {
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(String result) {
			statusBox.clear();
			if (result != null)
				urlTextBox.setText(result);
		}
	}
}
