package com.risevision.ui.client.common.widgets;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.risevision.ui.client.common.info.FormValidatorInfo;
import com.risevision.ui.client.common.widgets.ActionsWidget;
import com.risevision.ui.client.common.widgets.FormValidatorWidget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ConfirmDialogWidget extends PopupPanel {	
	private static ConfirmDialogWidget instance;
	//UI pieces
	private Label titleLabel = new Label("Delete Confirmation");
	private Label warningLabel = new Label();
	private Label descriptionLabel = new Label("Please type \"DELETE\" in the dialog below:");
	private FormValidatorWidget formValidator = new FormValidatorWidget();
	private TextBox confirmTextBox = new TextBox();
	
	private String confirmString;
	private Command onConfirm;
	
	private ActionsWidget actionsWidget = new ActionsWidget();
	
	public static ConfirmDialogWidget getInstance() {
		try {
			if (instance == null)
				instance = new ConfirmDialogWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}
	
	public ConfirmDialogWidget() {
		VerticalPanel mainPanel = new VerticalPanel();
		
		add(mainPanel);
		
		mainPanel.add(titleLabel);
		mainPanel.add(warningLabel);
		mainPanel.add(new HTML("<span style='line-height:3px;'>&nbsp;</span>"));
		mainPanel.add(descriptionLabel);
		mainPanel.add(new HTML("<span style='line-height:3px;'>&nbsp;</span>"));
		mainPanel.add(formValidator);
		mainPanel.add(confirmTextBox);
		
		mainPanel.add(actionsWidget);
		
		styleControls();
		
		initActions();
		initValidator();
	}

	private void styleControls() {
		setSize("400px", "100%");
		getElement().getStyle().setProperty("padding", "10px");
		
		titleLabel.setStyleName("rdn-Head");
		warningLabel.getElement().getStyle().setPaddingTop(3, Unit.PX);
		warningLabel.getElement().getStyle().setColor("red");
		confirmTextBox.addStyleName("rdn-TextBoxMedium");

//		urlTextBox.setReadOnly(true);
		actionsWidget.addStyleName("rdn-VerticalSpacer");
	}

	private void initValidator() {
		// Add widgets to validator
		formValidator.addValidationElement(confirmTextBox, "Id", FormValidatorInfo.requiredFieldValidator);
	}

	private void initActions() {
		Command cmdSave = new Command() {
			public void execute() {
				doActionSave();
			}
		};			
		
		Command cmdCancel = new Command() {
			public void execute() {
				doActionCancel();
			}
		};	

		actionsWidget.addAction("Okay", cmdSave);
		actionsWidget.addAction("Cancel", cmdCancel);
	}
	
	public void show(Command onChange, String dialogTitle, String warningLabel, String confirmString) {
		this.onConfirm = onChange;
		this.confirmString = confirmString;
		titleLabel.setText(dialogTitle);
		this.warningLabel.setText(warningLabel);
		descriptionLabel.setText("Please type \"" + confirmString + "\" in the dialog below:");
		
		formValidator.clear();
		confirmTextBox.setText("");
		confirmTextBox.setFocus(true);
		super.show();
		center();
	}
	
	private void doActionSave() {
		formValidator.clear();
		if (confirmTextBox.getText().equals(confirmString)) {
			if (onConfirm != null)
				onConfirm.execute();
			
			hide();
		}
		else {
			formValidator.addErrorMessage("* Cannot confirm operation");
		}
	}
	
	private void doActionCancel() {
		hide();
	}

}