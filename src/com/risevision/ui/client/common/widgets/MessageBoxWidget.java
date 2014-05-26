package com.risevision.ui.client.common.widgets;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.risevision.ui.client.common.widgets.ActionsWidget;

public class MessageBoxWidget extends PopupPanel {
	private static MessageBoxWidget instance;
	
	//UI pieces
	private VerticalPanel mainPanel = new VerticalPanel();

	private Label messageLabel = new Label();
	private Label messageLabel2 = new Label();
	
	private ActionsWidget actionWidget = new ActionsWidget();
	
	private Command closeCommand;

	public MessageBoxWidget() {
		super(false, true); //set auto-hide and modal
		
		add(mainPanel);
		
		mainPanel.add(messageLabel);
		mainPanel.add(new HTML("&nbsp;"));
		mainPanel.add(messageLabel2);

		mainPanel.add(actionWidget);
		
		styleControls();

		initActions();		
	}
	
	private void styleControls() {		
		setSize("400px", "100%");
		
		this.getElement().getStyle().setProperty("padding", "10px");
		actionWidget.addStyleName("rdn-VerticalSpacer");
	}
	
	private void initActions() {
		Command cmdOkay = new Command() {
			public void execute() {
				doActionOkay();
			}
		};	
		
		actionWidget.addAction("Okay", cmdOkay);
	}
	
	private void doActionOkay() {
		if (closeCommand != null) {
			closeCommand.execute();
		}
		
		hide();
	}
	
	public static MessageBoxWidget getInstance() {
		try {
			if (instance == null)
				instance = new MessageBoxWidget();
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
			case KeyCodes.KEY_ENTER:
				doActionOkay();
				break;
			}

		return true;
	}
	
	public void show(String text, Command closeCommand) {
		this.closeCommand = closeCommand;
		
		show(text);
	}
	
	public void show(String text) {
		super.show();

		setMessageLabel(text);
		center();
	}
	
	private void setMessageLabel(String text) {
		messageLabel.setText(text);
	}

}
