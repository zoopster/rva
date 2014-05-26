package com.risevision.ui.client.company;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.risevision.ui.client.common.controller.UserAccountController;
import com.risevision.ui.client.common.data.CompanyDataController;
import com.risevision.ui.client.common.exception.RiseAsyncCallback;
import com.risevision.ui.client.common.info.CompaniesInfo;
import com.risevision.ui.client.common.info.CompanyInfo;
import com.risevision.ui.client.common.widgets.RiseListBox;
import com.risevision.ui.client.common.widgets.SpacerWidget;
import com.risevision.ui.client.common.widgets.StatusBoxWidget;

@Deprecated
public class CompanyListBoxWidget extends HorizontalPanel {
	private static CompanyListBoxWidget instance;
	
	private SimplePanel fieldPanel = new SimplePanel(); 
	private RiseListBox companyListBox = new RiseListBox();
	private Label companyLabel = new Label();
	private Anchor changeCompanyLink = new Anchor("Change");
	private Anchor saveCompanyLink = new Anchor("Save");
	private Anchor cancelCompanyLink = new Anchor("Cancel");
	
	//RPC
//	private final CompanyServiceAsync companyService = GWT.create(CompanyService.class);
	private RpcPnoCallBackHandler rpcPnoCallBackHandler = new RpcPnoCallBackHandler();
	
	private CompanyInfo companyInfo;

	private StatusBoxWidget statusBox = StatusBoxWidget.getInstance();
	
	public CompanyListBoxWidget() {
		fieldPanel.add(companyLabel);
		add(fieldPanel);
		
//		add(companyLabel);
//		add(companyListBox);
		add(new SpacerWidget());
		HorizontalPanel linksPanel = new HorizontalPanel();
		linksPanel.add(changeCompanyLink);
		linksPanel.add(saveCompanyLink);
		linksPanel.add(new SpacerWidget());
		linksPanel.add(cancelCompanyLink);
		
		add(linksPanel);
		
		showListBox(false);
		
		styleControls();
		initActions();
	}
	
	private void styleControls() {
		companyListBox.addStyleName("rdn-ListBoxMedium");
		companyLabel.addStyleName("rdn-OverflowElipsis");
		companyLabel.setWidth("175px");
	}
	
	private void initActions() {
		changeCompanyLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showListBox(true);
			}
		});
		
		saveCompanyLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				companyLabel.setText(companyListBox.getSelectedText("<Not Specified>"));
				showListBox(false);
			}
		});
		
		cancelCompanyLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				companyListBox.setSelectedValue(companyInfo.getParentId());
				companyLabel.setText(companyListBox.getSelectedText("<Not Specified>"));
				
				showListBox(false);
			}
		});
	}
	
	private void showListBox(boolean show) {
		fieldPanel.remove(companyLabel);
		fieldPanel.remove(companyListBox);
		
		if (show) {
			fieldPanel.add(companyListBox);
		}
		else {
			fieldPanel.add(companyLabel);
		}
//		companyLabel.setVisible(!show);
//		companyListBox.setVisible(show);
		
		changeCompanyLink.setVisible(!show);
		saveCompanyLink.setVisible(show);
		cancelCompanyLink.setVisible(show);
	}
	
	public static CompanyListBoxWidget getInstance() {
		try {
			if (instance == null)
				instance = new CompanyListBoxWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}
	
	public void loadPnoCompanies(CompanyInfo companyInfo) {
		this.companyInfo = companyInfo;
		
		String userCompanyId = null, excludeCompanyId = null;
		
		if (UserAccountController.getInstance().getUserInfo() != null) {
			userCompanyId = UserAccountController.getInstance().getUserInfo().getCompany();
		}
		if (companyInfo.getId() != null) {
			excludeCompanyId = companyInfo.getId();
		}
		
		CompanyDataController controller = CompanyDataController.getInstance();
		
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, "PNO list is loading...");
		companyListBox.clear();
		
		controller.getNetworkOperators(userCompanyId, excludeCompanyId, rpcPnoCallBackHandler);
//		companyService.getPnoCompanies(userCompanyId, excludeCompanyId, rpcPnoCallBackHandler);
	}

	private void loadData(ArrayList<CompanyInfo> companies) {
		companyListBox.clear();

		if (companies != null) {
			for (int i = 0; i < companies.size(); i++)
				companyListBox.addItem(companies.get(i).getName(), companies.get(i).getId());
		} else
			companyListBox.addItem("<Not Specified>", "");
	}

	private class RpcPnoCallBackHandler extends RiseAsyncCallback<CompaniesInfo> {
		public void onFailure() {
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(CompaniesInfo result) {
			if (result == null || result.getCompanies() == null) {
				statusBox.setStatus(StatusBoxWidget.Status.ERROR, "Error retrieving PNO list.");
			} else {
				loadData(result.getCompanies());
				//assign Parent
				companyListBox.setSelectedValue(companyInfo.getParentId());
				companyLabel.setText(companyListBox.getSelectedText("<Not Specificed>"));
//				actionsWidget.setEnabled(true);
				statusBox.clear();
			}
		}
	}
	
	public String getSelectedValue() {
		return companyListBox.getSelectedValue(companyInfo.getParentId());
	}
}