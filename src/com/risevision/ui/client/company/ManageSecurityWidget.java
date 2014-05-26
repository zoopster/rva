// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.company;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.ui.client.common.controller.SelectedCompanyController;
import com.risevision.ui.client.common.exception.RiseAsyncCallback;
import com.risevision.ui.client.common.info.CompanyInfo;
import com.risevision.ui.client.common.info.ManageSettingsInfo;
import com.risevision.ui.client.common.service.CompanyService;
import com.risevision.ui.client.common.service.CompanyServiceAsync;
import com.risevision.ui.client.common.widgets.ActionsWidget;
import com.risevision.ui.client.common.widgets.LastModifiedWidget;
import com.risevision.ui.client.common.widgets.RolesWidget;
import com.risevision.ui.client.common.widgets.StatusBoxWidget;

public class ManageSecurityWidget extends Composite {
	private static ManageSecurityWidget instance;
	
	//RPC
	private final CompanyServiceAsync companyService = GWT
			.create(CompanyService.class);
	private String companyId;
	
	//UI pieces
	private ActionsWidget actionsWidget = ActionsWidget.getInstance();
	private StatusBoxWidget statusBox = StatusBoxWidget.getInstance();

	private Grid mainGrid = new Grid(3, 2);
	private int row = -1;
	
	//roles section
	private RolesWidget rolesWidget = new RolesWidget();
	
	private LastModifiedWidget lastModified = LastModifiedWidget.getInstance();
	
	public static ManageSecurityWidget getInstance() {
		try {
			if (instance == null)
				instance = new ManageSecurityWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}
	
	public ManageSecurityWidget() {
		
		//style the table	
		mainGrid.setCellSpacing(0);
		mainGrid.setCellPadding(0);
		mainGrid.setStyleName("rdn-Table");
		
		gridAdd("Roles:", rolesWidget);

		initWidget(mainGrid);
	}


	protected void onLoad() {
		super.onLoad();

		initActions();
		loadDataRPC();
	}
	
	private void initActions() {	
		Command cmdSave = new Command() {
			public void execute() {
				doActionSave();
			}			
		};
	
		actionsWidget.addAction("Save", cmdSave);
	}
	
	private void doActionSave(){
		CompanyInfo company = new CompanyInfo();
		HashMap<String, String> settings = new HashMap<String, String>();
		
		String tempSettings = RiseUtils.listToString(rolesWidget.getRoles(), ",");
		
		settings.put(ManageSettingsInfo.SECURITY_ROLES, tempSettings);
		
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
	
	private void loadDataRPC() {	
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, StatusBoxWidget.LOADING);	
		
		companyId = SelectedCompanyController.getInstance().getSelectedCompanyId();
		
		companyService.getCompany(companyId, new RiseAsyncCallback<CompanyInfo>() {
			public void onFailure() {
				statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
			}

			public void onSuccess(CompanyInfo result) {
				if (result != null)
					bindData(result);
				statusBox.clear();
			}
		});
	}
	
	protected void bindData(CompanyInfo company) {
		HashMap<String, String> settingsMap = company.getSettings();

		lastModified.Initialize(company.getChangedBy(), company.getChangeDate());
		if (settingsMap.containsKey(ManageSettingsInfo.SECURITY_ROLES)){
			rolesWidget.setRoles(RiseUtils.stringToList(settingsMap.get(ManageSettingsInfo.SECURITY_ROLES), ","));
		}
		else
			rolesWidget.setSelected();
		
	}

	private void gridAdd(String label, Widget widget) {
		gridAdd(label, widget, null);
	}

	private void gridAdd(String label, Widget widget, String styleName) {
		row++;
		mainGrid.getCellFormatter().setStyleName(row, 0, "rdn-Column1");
		mainGrid.setText(row, 0, label);
		if (widget != null)
		{
			mainGrid.setWidget(row, 1, widget);
			if (styleName != null)
				widget.setStyleName(styleName);
		}
	}
}
