// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.risevision.ui.client.common.controller.SelectedCompanyController;
import com.risevision.ui.client.common.controller.UserAccountController;
import com.risevision.ui.client.common.info.CompanyInfo;
import com.risevision.ui.client.common.info.EmailInfo;
import com.risevision.ui.client.common.info.UserInfo;
import com.risevision.ui.client.common.service.UserService;
import com.risevision.ui.client.common.service.UserServiceAsync;

public class EmailWidget extends PopupPanel{
	private static EmailWidget instance;
	private String toAddressString;
	
	private UserServiceAsync userService = (UserServiceAsync) GWT.create(UserService.class); 
	private RpcSendFeedbackCallBackHandler rpcSendFeedbackCallBackHandler = new RpcSendFeedbackCallBackHandler();
	
	//UI pieces
	private VerticalPanel mainPanel = new VerticalPanel();
	private Label titleLabel = new Label("Send E-Mail");
	private StatusBoxWidget statusBox = new StatusBoxWidget();
	private TextBox subjectText = new TextBox();
	private TextArea messageText = new TextArea();
	private ActionsWidget actionWidget = new ActionsWidget();
	
	private static final String messageString = "" +
			"From: %fn %ln, %em, %cn \n\n" +
			"Message:\n" +
			"%m";
	
	public EmailWidget() {
		super(true, true); //set auto-hide and modal
		add(mainPanel);
		
		mainPanel.add(titleLabel);
		mainPanel.add(statusBox);
		mainPanel.add(new Label("", true));
		
		mainPanel.add(new HTML("&nbsp;"));
		mainPanel.add(new Label("Subject:"));
		mainPanel.add(subjectText);
		mainPanel.add(new Label("Message:"));
		mainPanel.add(messageText);
		mainPanel.add(actionWidget);
			
		styleControls();

		initActions();		
		initValidator();
	}
	
	private void styleControls() {		
		setSize("500px", "100%");
		titleLabel.setStyleName("rdn-Head");
		messageText.setSize("450px", "80px");
		subjectText.setStyleName("rdn-TextBoxMedium");
		
		this.getElement().getStyle().setProperty("padding", "10px");
		actionWidget.addStyleName("rdn-VerticalSpacer");
	}
	
	private void initActions() {
		Command cmdSend = new Command() {
			public void execute() {
				doActionSend();
			}
		};

		Command cmdCancel = new Command() {
			public void execute() {
				doActionCancel();
			}
		};		
		
		actionWidget.addAction("Send", cmdSend);
		actionWidget.addAction("Cancel", cmdCancel);
	}
	
	private void doActionSend() {
		UserInfo user = UserAccountController.getInstance().getUserInfo();
		CompanyInfo company = SelectedCompanyController.getInstance().getUserCompany();
		
		if (user != null) {
			EmailInfo email = new EmailInfo();

			statusBox.setStatus(StatusBoxWidget.Status.WARNING, "Sending message...");
			String message = messageString.replace("%fn", user.getFirstName() == null ? "" : user.getFirstName());
			message = message.replace("%ln", user.getLastName() == null ? "" : user.getLastName());
			message = message.replace("%em", user.getEmail());
			if (company != null) {
				message = message.replace("%cn", company.getName());
			}
			else {
				message = message.replace("%cn", user.getCompany());				
			}
			message = message.replace("%m", messageText.getText());
			
			email.setToAddressString(toAddressString);
			email.setFromString(user.getEmail());
			email.setSubjectString(subjectText.getText());
			email.setMessageString(message);
			
			userService.sendEmail(email, rpcSendFeedbackCallBackHandler);
		}
	}
	
	private void doActionCancel() {
		hide();
	}
	
	private void initValidator() {

	}
	
	public static EmailWidget getInstance() {
		try {
			if (instance == null)
				instance = new EmailWidget();
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
		show("support@risevision.com");
	}
	
	public void show(String toAddressString) {
		super.show();
		
		this.toAddressString = toAddressString;
		subjectText.setText("");
		messageText.setText("");
		center();
	}
	
	//--------- RPC CLASSES ---------------//
	class RpcSendFeedbackCallBackHandler implements AsyncCallback<Void> {

		public void onFailure(Throwable caught) {
			actionWidget.setEnabled(true);
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}

		public void onSuccess(Void result) {
			actionWidget.setEnabled(true);
			statusBox.clear();
			hide();
		}
	}
}
