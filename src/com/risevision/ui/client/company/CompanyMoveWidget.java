package com.risevision.ui.client.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.risevision.ui.client.common.controller.SelectedCompanyController;
import com.risevision.ui.client.common.exception.RiseAsyncCallback;
import com.risevision.ui.client.common.exception.ServiceFailedException;
import com.risevision.ui.client.common.info.CompanyInfo;
import com.risevision.ui.client.common.info.FormValidatorInfo;
import com.risevision.ui.client.common.info.RpcResultInfo;
import com.risevision.ui.client.common.service.CompanyService;
import com.risevision.ui.client.common.service.CompanyServiceAsync;
import com.risevision.ui.client.common.widgets.ActionsWidget;
import com.risevision.ui.client.common.widgets.FormValidatorWidget;
import com.risevision.ui.client.common.widgets.SpacerWidget;
import com.risevision.ui.client.common.widgets.StatusBoxWidget;
import com.risevision.ui.client.common.widgets.StatusBoxWidget.Status;

public class CompanyMoveWidget extends PopupPanel {
	private static CompanyMoveWidget instance;
	
	private final CompanyServiceAsync companyService = GWT.create(CompanyService.class);

	//UI pieces
	private VerticalPanel mainPanel = new VerticalPanel();
	private Label titleLabel = new Label("Move a Company to your Network");
	private Label infoLabel = new Label("Please enter the Company Authentication Key of the Company you wish to move:");
	private FormValidatorWidget formValidator = new FormValidatorWidget();
	private TextBox authKeyTextBox = new TextBox();
	private String authKey;
	private Anchor searchLink = new Anchor("Search");
	
	private Grid mainGrid = new Grid(2, 2);
	private int row = -1;
	
	private VerticalPanel companyPanel = new VerticalPanel();
	private Label resultsLabel = new Label();
	private Label companyNameLabel = new Label();
	private Label companyAddressLabel = new Label();
	
	private ActionsWidget actionWidget = new ActionsWidget();
	
	private StatusBoxWidget statusBox = new StatusBoxWidget();
	
	public CompanyMoveWidget() {
		super(false, true); //set auto-hide and modal
		add(mainPanel);
		
		mainPanel.add(titleLabel);
		mainPanel.add(statusBox);
		mainPanel.add(new HTML("&nbsp;"));
		mainPanel.add(infoLabel);
		mainPanel.add(new HTML("&nbsp;"));

		mainPanel.add(formValidator);
		
		HorizontalPanel authKeyPanel = new HorizontalPanel();
		authKeyPanel.add(authKeyTextBox);
		authKeyPanel.add(new SpacerWidget());
		authKeyPanel.add(searchLink);
		
		mainPanel.add(authKeyPanel);
		mainPanel.add(new HTML("&nbsp;"));

		gridAdd("Company Name:", companyNameLabel);
		gridAdd("Company Address:", companyAddressLabel);
		companyPanel.add(mainGrid);
		companyPanel.add(new HTML("&nbsp;"));
		companyPanel.add(new Label("Would you like to move the this Company?"));
		
		mainPanel.add(companyPanel);
		
		mainPanel.add(actionWidget);
			
		styleControls();

		initActions();		
		initValidator();
	}
	
	private void gridAdd(String label, Widget widget) {
		row++;
		mainGrid.getCellFormatter().setStyleName(row, 0, "rdn-Column1");
		mainGrid.setText(row, 0, label);
		if (widget != null) {
			mainGrid.setWidget(row, 1, widget);
		}
	}
	
	private void styleControls() {		
		setSize("600px", "100%");
		
		//style the table	
		mainGrid.setCellSpacing(0);
		mainGrid.setCellPadding(0);
		mainGrid.setStyleName("rdn-Table");
		
		titleLabel.setStyleName("rdn-Head");
		resultsLabel.setStyleName("rdn-Head");
		
		authKeyTextBox.setStyleName("rdn-TextBoxMedium");
		
		this.getElement().getStyle().setProperty("padding", "10px");
		actionWidget.addStyleName("rdn-VerticalSpacer");
	}
	
	private void initActions() {
		searchLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				doActionContinue();		
			}
		});

		Command cmdAccept = new Command() {
			public void execute() {
				doActionAccept();
			}
		};

		Command cmdCancel = new Command() {
			public void execute() {
				doActionCancel();
			}
		};		
		
		actionWidget.addAction("Accept", cmdAccept);
		actionWidget.addAction("Cancel", cmdCancel);
	}
	
	private void doActionContinue() {
		if (formValidator.validate()) {	
			statusBox.setStatus(Status.WARNING, "Searching Company...");
			
			// save the authKey in case the user decides to change the textbox value prior to pressing Accept
			authKey = authKeyTextBox.getText();
			companyService.getCompanyByAuthKey(authKeyTextBox.getText(), new RpcGetCompanyCallBackHandler());
		}
	}
	
	private void doActionAccept() {
		if (authKey != null && !authKey.isEmpty()) {
			statusBox.setStatus(Status.WARNING, "Moving Company...");

			companyService.moveCompanyByAuthKey(SelectedCompanyController.getInstance().getSelectedCompanyId(), 
					authKey, new RpcMoveCompanyCallBackHandler());
		}
	}
	
	private void doActionCancel() {
		hide();
	}
	
	private void initValidator() {
		formValidator.addValidationElement(authKeyTextBox, "Authentication Key", FormValidatorInfo.requiredFieldValidator);
	}
	
	public static CompanyMoveWidget getInstance() {
		try {
			if (instance == null)
				instance = new CompanyMoveWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}
	
	@Override
	public boolean onKeyDownPreview(char key, int modifiers) {
		// Use the popup's key preview hooks to close the dialog when either
		// enter or escape is pressed.
		switch (key) {
			case KeyCodes.KEY_ESCAPE:
				hide();
				break;
			}

		return true;
	}
	
	public void show() {
		super.show();

		statusBox.clear();
		clearControls();
		
		center();
	}
	
	private void clearControls() {
		companyPanel.setVisible(false);
		authKeyTextBox.setText("");
		formValidator.clear();
		actionWidget.setEnabled(false, "Accept");
	}
	
	private void bindData(CompanyInfo result) {
		if (result == null) {
			statusBox.setStatus(Status.ERROR, "No Company was found that matches that Key.");
		}
		else {
			companyPanel.setVisible(true);
			actionWidget.setEnabled(true, "Accept");

			statusBox.setStatus(Status.OK, "The Company was found.");
			companyNameLabel.setText(result.getName());
			companyAddressLabel.setText(result.getAddress());
			
			mainGrid.getRowFormatter().setVisible(1, result.getAddress() != null && !result.getAddress().isEmpty());
		}
	}
	
	private void onMoveComplete(RpcResultInfo result) {
		if (result == null) {
			statusBox.setStatus(Status.ERROR, "The Company could not be moved.");
		}
		else {
			clearControls();
			statusBox.setStatus(Status.OK, "The Company was moved successfully.");
		}
	}

	class RpcGetCompanyCallBackHandler extends RiseAsyncCallback<CompanyInfo> {
		public void onFailure() {
			bindData(null);
		}

		public void onSuccess(CompanyInfo result) {
			bindData(result);
		}
	}
	
	class RpcMoveCompanyCallBackHandler extends RiseAsyncCallback<RpcResultInfo> {
		public void onFailure() {
			if (caught instanceof ServiceFailedException 
					&& ((ServiceFailedException)caught).getReason() == ServiceFailedException.BAD_REQUEST) {
				statusBox.setStatus(Status.ERROR, "The Company cannot be moved under itself or any of its subcompanies.");
			}
			else {
				onMoveComplete(null);
			}
		}

		public void onSuccess(RpcResultInfo result) {
			onMoveComplete(result);
		}
	}
}
