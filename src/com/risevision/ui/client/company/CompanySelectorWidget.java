// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.company;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.risevision.ui.client.UiEntryPoint;
import com.risevision.ui.client.common.ContentId;
import com.risevision.ui.client.common.controller.SelectedCompanyController;
import com.risevision.ui.client.common.controller.UserAccountController;
import com.risevision.ui.client.common.info.CompanyInfo;
import com.risevision.ui.client.common.widgets.SpacerWidget;

public class CompanySelectorWidget extends FlowPanel {
	private static CompanySelectorWidget instance;
	private boolean companyChanged = false;
	//UI pieces
	private InlineLabel lbName = new InlineLabel();
	private Anchor btName = new Anchor("");
	private Anchor btChange  = new Anchor("Switch");
	private Anchor btReset  = new Anchor("Reset");
	
	public CompanySelectorWidget() {
		btName.addClickHandler(new ClickHandler() {			
			public void onClick(ClickEvent event) {
				navigateToCompanyPage();
			}
		});
		
		btChange.addClickHandler(new ClickHandler() {			
			public void onClick(ClickEvent event) {
				doActionChange();
			}
		});

		btReset.addClickHandler(new ClickHandler() {			
			public void onClick(ClickEvent event) {				
				doActionReset();
			}
		});
		
		if (UserAccountController.getInstance().userHasRoleUserAdministrator())
			add(btName);
		else
			add(lbName);

		add(new SpacerWidget());
		add(btChange);
		add(new SpacerWidget());
		add(btReset);
		
		btName.setTabIndex(-1);
		btChange.setTabIndex(-1);
		btReset.setTabIndex(-1);
		btReset.setVisible(false);
	}
	
	public static CompanySelectorWidget getInstance() {
		try {

			if (instance == null)
				instance = new CompanySelectorWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}

	protected void onLoad() {
		super.onLoad();
//		bindData();
	}
	
	public void bindData() {
		CompanyInfo companyInfo = SelectedCompanyController.getInstance().getSelectedCompany();
		
		if (companyInfo != null && companyInfo.getId() != null) {
			lbName.setText(companyInfo.getName());
			btName.setText(companyInfo.getName());
			
			if (companyInfo.getId().equals(SelectedCompanyController.getInstance().getUserCompany().getId())){
				lbName.getElement().getStyle().clearProperty("color");
				btName.getElement().getStyle().clearProperty("color");
				companyChanged = false;
			}
			else {
				lbName.getElement().getStyle().setProperty("color", "red");
				btName.getElement().getStyle().setProperty("color", "red");
				companyChanged = true;
			}
		} else {
			//TODO: remove this part later
			lbName.setText("Dummy Company, Inc.");
			btName.setText("Dummy Company, Inc.");
		}
		
		setCanChangeCompany(true);
	}

	protected void navigateToCompanyPage() {
		UiEntryPoint.loadContentStatic(ContentId.COMPANY_MANAGE);
	}
	
	private void doActionChange() {
		CompaniesWidget.getInstance().show();
	}

	private void doActionReset() {
		SelectedCompanyController.getInstance().reset();
	}

	public void setCanChangeCompany(boolean canChangeCompany) {
		if (SelectedCompanyController.getInstance().getUserCompany() == null || !SelectedCompanyController.getInstance().getUserCompany().isPno()) {
			canChangeCompany = false;
		}
		btReset.setVisible(companyChanged && canChangeCompany);		
		btChange.setVisible(canChangeCompany);	
	}
}