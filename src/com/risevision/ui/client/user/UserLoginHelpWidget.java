// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.user;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.risevision.ui.client.common.exception.RiseAsyncCallback;
import com.risevision.ui.client.common.widgets.ActionsWidget;

public class UserLoginHelpWidget extends PopupPanel {
	//UI pieces
	private VerticalPanel mainPanel = new VerticalPanel();
	private Label titleLabel = new Label("User account error");
	private Label infoLabel = new Label();
	private Label infoLabel2 = new Label("If this is wrong, or it doesn't work, " +
			"we're sorry about this but your browser is confused, and the only way to fix " +
			"it is to clear your browser cache and try again.");
//	private Frame termsFrame = new Frame(termsURL);
	private ActionsWidget actionWidget = new ActionsWidget();
	
	public UserLoginHelpWidget() {
		super(false, true); //set auto-hide and modal
		add(mainPanel);
		
		mainPanel.add(titleLabel);
		mainPanel.add(infoLabel);
		mainPanel.add(new HTML("&nbsp;"));
		mainPanel.add(new Image("images/account_selection.png", 0, 0, 685, 343));
		mainPanel.add(infoLabel2);
//		mainPanel.add(new HTML("&nbsp;"));
//		mainPanel.add(termsFrame);

		mainPanel.add(actionWidget);
		
		styleControls();

		initActions();		
	}
	
	private void styleControls() {		
		setSize("600px", "100%");
		titleLabel.setStyleName("rdn-Head");
		
//		termsFrame.getElement().getStyle().setHeight(Math.min(Window.getClientHeight() - 200, 400), Unit.PX);
//		termsFrame.getElement().getStyle().setWidth(Math.min(Window.getClientWidth() - 50, 800), Unit.PX);
//		termsFrame.getElement().getStyle().setBorderWidth(0, Unit.PX);
//		termsFrame.getElement().setAttribute("frameBorder", "0px");
//		termsFrame.getElement().setAttribute("allowTransparency", "true");
		
		this.getElement().getStyle().setProperty("padding", "10px");
		actionWidget.addStyleName("rdn-VerticalSpacer");
	}
	
	private void initActions() {
		Command cmdHelp = new Command() {
			public void execute() {
				Window.open("http://www.risevision.com/help/authorization-and-cache-issues/", "_blank", "");
			}
		};
		
		Command cmdConfirm = new Command() {
			public void execute() {
				doActionConfirm();
			}
		};

		Command cmdCancel = new Command() {
			public void execute() {
				doActionCancel();
			}
		};		
		
		actionWidget.addAction("Okay", cmdConfirm);
		actionWidget.addAction("Cancel", cmdCancel);
		actionWidget.addAction("Help", cmdHelp);
	}
	
	private void doActionConfirm() {
		RiseAsyncCallback.reAuthenticateUser();
	}
	
	private void doActionCancel() {
		UserAccountWidget.getInstance().logoutUser();
	}
	
	public void show(String username) {
		String labelText = "We think your account authentication got mixed up with another account. " +
				"If you want to login with " + username + " press okay and " +
				"make sure you authorize that account. Here's what we mean:";

		
		infoLabel.setText(labelText);
		
		super.show();
		center();
	}
}
