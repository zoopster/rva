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

public class ClaimIdWidget extends HorizontalPanel {
	//RPC
	private final CompanyServiceAsync companyService = GWT.create(CompanyService.class);
	// UI Pieces
	private StatusBoxWidget statusBox = StatusBoxWidget.getInstance();
	private Label claimIdLabel = new Label();
	private Anchor resetKeyLink = new Anchor("Reset");
	
	public ClaimIdWidget() {
		add(claimIdLabel);
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
		claimIdLabel.addStyleName("rdn-OverflowElipsis");
		claimIdLabel.setWidth("175px");
	}

	public void initWidget(String claimId) {
		claimIdLabel.setText(claimId);
	}
	
	public String getClaimId() {
		return claimIdLabel.getText();
	}
	
//	protected void onLoad() {
//		loadAuthKey();
//	}

//	private void loadAuthKey(){	
//		companyService.getCompanyAuthKey(SelectedCompanyController.getInstance().getSelectedCompanyId(), new RpcCallBackHandler());
//	}
	
	private void resetAuthKey() {
		if (Window.confirm("Resetting the Company Claim Id will cause existing installations to no longer be associated with your Company.")) {
			companyService.resetCompanyClaimId(SelectedCompanyController.getInstance().getSelectedCompanyId(), new RpcCallBackHandler());
		}
	}
	
	class RpcCallBackHandler extends RiseAsyncCallback<String> {
		public void onFailure() {
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(String result) {
			statusBox.clear();
			if (result != null)
				claimIdLabel.setText(result);
		}
	}
}
