package com.risevision.ui.client.display;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.risevision.ui.client.common.controller.ConfigurationController;
import com.risevision.ui.client.common.widgets.ActionsWidget;
import com.risevision.ui.client.common.widgets.SpacerWidget;

@Deprecated
public class DisplayInstallWidget extends PopupPanel{
	private static DisplayInstallWidget instance;
	
	private static final String infoText = "The Rise Vision Player is best run on a dedicated digital " +
			"signage appliance running " +
			"Windows 7 (32 or 64 Bit) or Ubuntu Linux (32 Bit)" +
//			"Windows XP Pro, " +
//			"Vista Professional / Ultimate (32 or 64 Bit), " +
//			"Windows 7 Pro / Ultimate (32 or 64 Bit) or " +
//			"Ubuntu Linux" +
			". Everytime you start the computer it is installed on it will " +
			"automatically begin showing it's assigned Presentations, which will drive you nuts if this is " +
			"your personal computer. If you just want to check out Presentations, the best way to do that is " +
			"to Preview them in your browser from the Presentation editor. Otherwise if you still want to download " +
			"and install on this computer please read our Terms of Service and Privacy and if you agree with them " +
			"click the check box and then Install. Please note that if you are running the Chrome browser it will be " +
			"closed to complete the installation.";

	//UI pieces
	private VerticalPanel mainPanel = new VerticalPanel();
	private Label titleLabel = new Label("Install Rise Vision Player");
	private Label infoLabel = new Label(infoText, true);
	private CheckBox termsCheckBox = new CheckBox();
	
	private VerticalPanel linksPanel = new VerticalPanel();
	private Anchor windowsLink = new Anchor();
	private Anchor linuxLink = new Anchor();
	
	private ActionsWidget actionWidget = new ActionsWidget();
	
	public DisplayInstallWidget() {
		super(true, true); //set auto-hide and modal
		add(mainPanel);
		
		mainPanel.add(titleLabel);
		mainPanel.add(infoLabel);
		
		mainPanel.add(new HTML("&nbsp;"));
		HorizontalPanel termsPanel = new HorizontalPanel();
		termsPanel.add(termsCheckBox);
		termsPanel.add(new SpacerWidget());
		termsPanel.add(new HTML("I agree with the <a href='" + 
				ConfigurationController.getInstance().getConfiguration().getTermsURL() + 
				"' target='blank'>Terms of Service and Privacy</a>", true));
		
		mainPanel.add(termsPanel);
		mainPanel.add(new HTML("&nbsp;"));
		mainPanel.add(linksPanel);
		
		linksPanel.add(new Label("Windows Installer:"));
		linksPanel.add(windowsLink);
		linksPanel.add(new HTML("&nbsp;"));
		linksPanel.add(new Label("Linux Installer:"));
		linksPanel.add(linuxLink);
		
		mainPanel.add(actionWidget);
			
		styleControls();

		initActions();		
	}
	
	private void styleControls() {		
		setSize("600px", "100%");
		titleLabel.setStyleName("rdn-Head");
		termsCheckBox.setStyleName("rdn-CheckBox");
		
		this.getElement().getStyle().setProperty("padding", "10px");
		actionWidget.addStyleName("rdn-VerticalSpacer");
	}
	
	private void initActions() {
		Command cmdCancel = new Command() {
			public void execute() {
				doActionCancel();
			}
		};		
		
		actionWidget.addAction("Close", cmdCancel);
		
		termsCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				showInstallLinks();
			}
		});
		
		windowsLink.setText(ConfigurationController.getInstance().getConfiguration().getWindowsInstallerURL());
		windowsLink.setHref(ConfigurationController.getInstance().getConfiguration().getWindowsInstallerURL());
		windowsLink.setTarget("_blank");

		linuxLink.setText(ConfigurationController.getInstance().getConfiguration().getLinuxInstallerURL());
		linuxLink.setHref(ConfigurationController.getInstance().getConfiguration().getLinuxInstallerURL());
		linuxLink.setTarget("_blank");
	}
	
	private void doActionCancel() {
		hide();
	}
	
	public static DisplayInstallWidget getInstance() {
		try {
			if (instance == null)
				instance = new DisplayInstallWidget();
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

		termsCheckBox.setValue(false);
		showInstallLinks();
		center();
	}
	
	private void showInstallLinks() {
		linksPanel.setVisible(termsCheckBox.getValue());
	}

}
