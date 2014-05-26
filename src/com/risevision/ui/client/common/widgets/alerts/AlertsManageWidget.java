// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.alerts;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.ui.client.common.info.AlertsInfo;
import com.risevision.ui.client.common.widgets.NumericBoxWidget;
import com.risevision.ui.client.common.widgets.alerts.AlertCategoryWidget;
import com.risevision.ui.client.common.widgets.alerts.AlertCertaintyWidget;
import com.risevision.ui.client.common.widgets.alerts.AlertSeverityWidget;
import com.risevision.ui.client.common.widgets.alerts.AlertTextWidget;
import com.risevision.ui.client.common.widgets.alerts.AlertURLWidget;
import com.risevision.ui.client.common.widgets.alerts.AlertUrgencyWidget;
import com.risevision.ui.client.common.widgets.alerts.AlertsStatusWidget;
import com.risevision.ui.client.common.widgets.grid.FormGridWidget;
import com.risevision.ui.client.display.DistributionWidget;
import com.risevision.ui.client.presentation.PresentationSelectPopupWidget;
import com.risevision.ui.client.presentation.common.PresentationSelectorWidget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class AlertsManageWidget extends Composite {
	private String ALERTS_DISCLAIMER = "<span class='rva-shortText'>The Alert Service is based upon " +
			"the Common Alert Protocol version " +
			"1.2. This standard does not require authentication or a secure connection, which could result " +
			"in somebody sending false Alerts by pretending to be your Alerts provider. We highly recommend " +
			"that, at the very least, you have your Alerts provider connect to our service with HTTPS, " +
			"not HTTP, and that you require an Authentication Name and Password for that connection. " +
			"That password should be complex and changed regularly. You should test your Alerts system " +
			"at least weekly. You have read and understood this disclaimer and you agree that Rise Vision " +
			"cannot be responsible for the erroneous showing of Alert Messages on your Displays. " +
			"<br /> " +
			"You further acknowledge that there is currently no charge for the Alert Service, but at " +
			"an as of yet to be determined date this service will cost $2 per display, per month. " +
			"At that time you will have 30 days to decide if you wish to continue using this Alert Service " +
			"or not.</span>";
	
	private AlertsInfo alertsSettings;
	//UI pieces
	private FormGridWidget topGrid = new FormGridWidget(5, 2);
	private FormGridWidget filterGrid = new FormGridWidget(8, 2);
	private FormGridWidget bottomGrid = new FormGridWidget(4, 2);
	//Customer fields
	private CheckBox alertsDisclaimerCheckBox = new CheckBox();
	
	private AlertURLWidget alertURLWidget = new AlertURLWidget();
	private TextBox alertUserNameTextBox = new TextBox();
	private TextBox alertPasswordTextBox = new TextBox();
	
	private Anchor alertFilterLink = new Anchor("Show");
	private boolean isFilterShowing = false;
	
	private TextBox alertSendersTextBox = new TextBox();
	private AlertsStatusWidget alertStatusesWidget = new AlertsStatusWidget();
	private TextBox alertHandlingCodesTextBox = new TextBox();
	private AlertCategoryWidget alertCategoriesWidget = new AlertCategoryWidget();
	private AlertUrgencyWidget alertUrgenciesWidget = new AlertUrgencyWidget();
	private AlertSeverityWidget alertSeveritiesWidget = new AlertSeverityWidget();
	private AlertCertaintyWidget alertCertaintiesWidget = new AlertCertaintyWidget();
	private TextBox alertEventCodesTextBox = new TextBox();
	private AlertTextWidget alertTextFieldsWidget = new AlertTextWidget();
	private PresentationSelectorWidget alertsPresentationSelector = new PresentationSelectorWidget(false);
	private DistributionWidget alertDistributionWidget = new DistributionWidget(false);
	private NumericBoxWidget alertExpiryTextBox = new NumericBoxWidget();
	
	public AlertsManageWidget() {
		HorizontalPanel disclaimerPanel = new HorizontalPanel();
		disclaimerPanel.add(alertsDisclaimerCheckBox);
		disclaimerPanel.add(new HTML(ALERTS_DISCLAIMER));
		
		topGrid.addRow("Acceptance", disclaimerPanel, "rdn-CheckBox");
		topGrid.addRow("Web Service URL:", "URL that the Alert Service posts its Alerts to", 
				alertURLWidget, null);
		topGrid.addRow("Authentication Name:", "Login name required to authenticate the Alert post to the URL", 
				alertUserNameTextBox, "rdn-TextBoxLong");
		topGrid.addRow("Authentication Password:", "Password required to authenticate the Login", 
				alertPasswordTextBox, "rdn-TextBoxLong");
		topGrid.addRow("Filter:", "Alert fields that can be used to filter which messages from the Alert Service to show",
				alertFilterLink, null);

		filterGrid.addRow("Sender:", "Comma separated list of Senders that you will accept Alerts from", 
				alertSendersTextBox, "rdn-TextBoxLong");
		filterGrid.addRow("Status:", "Alert Status to accept", 
				alertStatusesWidget, "rdn-MultiSelectBoxMedium");
		filterGrid.addRow("Handling:", "User defined special handling codes, comma seperated, to accept, or leave blank for all", 
				alertHandlingCodesTextBox, "rdn-TextBoxLong");
		filterGrid.addRow("Category:", "Categories to accept", 
				alertCategoriesWidget, "rdn-MultiSelectBoxMedium");
		filterGrid.addRow("Urgency:", "Urgency to accept", 
				alertUrgenciesWidget, "rdn-MultiSelectBoxMedium");
		filterGrid.addRow("Severity:", "Severity to accept", 
				alertSeveritiesWidget, "rdn-MultiSelectBoxMedium");
		filterGrid.addRow("Certainty:", "Certainty to accept", 
				alertCertaintiesWidget, "rdn-MultiSelectBoxMedium");
		filterGrid.addRow("Code:", "Comma separated Event Codes and their Values \"WhichDisplay=LobbyDisplay\", or leave blank for all", 
				alertEventCodesTextBox, "rdn-TextBoxLong");
		
		bottomGrid.addRow("Text:", "Alert fields that contain text from the Alert Service to show on the Displays", 
				alertTextFieldsWidget, "rdn-MultiSelectBoxMedium");
		bottomGrid.addRow("Presentation:", "The Presentation to show on the Displays when an Alert is sent", 
				alertsPresentationSelector, "rdn-TextBoxLong");
		bottomGrid.addRow("Distribution:", "The Displays to which the Alert is sent by default", 
				alertDistributionWidget, null);
		
		HorizontalPanel alertExpiryPanel = new HorizontalPanel();
		alertExpiryPanel.add(alertExpiryTextBox);
		alertExpiryTextBox.setStyleName("rdn-TextBoxShort");
		alertExpiryPanel.add(new HTML("&nbsp;&nbsp;"));
		alertExpiryPanel.add(new Label("Minutes"));
		bottomGrid.addRow("Default Expiry:", "How long an Alert is shown on the Displays, by default, can be overwritten by setting the Expiration Date/Time", 
				alertExpiryPanel, null);
		
		VerticalPanel gridPanel = new VerticalPanel();
		gridPanel.add(topGrid);
		gridPanel.add(filterGrid);
		gridPanel.add(bottomGrid);
		
		initWidget(gridPanel);

		styleControls();
		initLinks();
	}

	private void styleControls() {
		setStyleName("rdn-Indent");
		
		filterGrid.setStyleName("rdn-Indent");
	}

	private void initLinks() {
		alertFilterLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showFilter();
			}
		});
	}
	
	private void showFilter() {
		isFilterShowing = !isFilterShowing; 
		
		filterGrid.setVisible(isFilterShowing);
		
		alertFilterLink.setText(isFilterShowing ? "Hide" : "Show");
	}

	public void initWidget(AlertsInfo alertsSettings) {
		this.alertsSettings = alertsSettings;
		
		PresentationSelectPopupWidget.getInstance().load();
		
		bindData();
	}
	
	private void bindData() {
		if (alertsSettings == null) {
			return;
		}
		
		alertsDisclaimerCheckBox.setValue(alertsSettings.isAllowAlerts());
		alertURLWidget.setText(alertsSettings.getAlertURL());
		alertUserNameTextBox.setText(alertsSettings.getAlertUserName());
		alertPasswordTextBox.setText(alertsSettings.getAlertPassword());
		alertSendersTextBox.setText(RiseUtils.listToString(alertsSettings.getAlertSenders(), ","));
		alertStatusesWidget.setSelectedValues(alertsSettings.getAlertStatuses());
		alertHandlingCodesTextBox.setText(RiseUtils.listToString(alertsSettings.getAlertHandlingCodes(), ","));
		alertCategoriesWidget.setSelectedValues(alertsSettings.getAlertCategories());
		alertUrgenciesWidget.setSelectedValues(alertsSettings.getAlertUrgencies());
		alertSeveritiesWidget.setSelectedValues(alertsSettings.getAlertSeverities());
		alertCertaintiesWidget.setSelectedValues(alertsSettings.getAlertCertainties());
		alertEventCodesTextBox.setText(RiseUtils.listToString(alertsSettings.getAlertEventCodes(), ","));
		alertTextFieldsWidget.setSelectedValues(alertsSettings.getAlertTextFields());
		alertsPresentationSelector.init(alertsSettings.getAlertPresentationId());
		alertDistributionWidget.setDistribution(alertsSettings.getAlertDistribution());
		alertExpiryTextBox.setText(Integer.toString(alertsSettings.getAlertExpiry()));
		
		isFilterShowing = true;
		showFilter();
		
	}

	public AlertsInfo saveData() {
		if (alertsSettings == null)
			return null;

		alertsSettings.setAllowAlerts(alertsDisclaimerCheckBox.getValue());
		alertsSettings.setAlertURL(alertURLWidget.getText());
		alertsSettings.setAlertUserName(alertUserNameTextBox.getText());
		alertsSettings.setAlertPassword(alertPasswordTextBox.getText());
		alertsSettings.setAlertSenders(RiseUtils.stringToList(alertSendersTextBox.getText(), ","));
		alertsSettings.setAlertStatuses(alertStatusesWidget.getSelectedValues());
		alertsSettings.setAlertHandlingCodes(RiseUtils.stringToList(alertHandlingCodesTextBox.getText(), ","));
		alertsSettings.setAlertCategories(alertCategoriesWidget.getSelectedValues());
		alertsSettings.setAlertUrgencies(alertUrgenciesWidget.getSelectedValues());
		alertsSettings.setAlertSeverities(alertSeveritiesWidget.getSelectedValues());
		alertsSettings.setAlertCertainties(alertCertaintiesWidget.getSelectedValues());
		alertsSettings.setAlertEventCodes(RiseUtils.stringToList(alertEventCodesTextBox.getText(), ","));
		alertsSettings.setAlertTextFields(alertTextFieldsWidget.getSelectedValues());
		alertsSettings.setAlertPresentationId(alertsPresentationSelector.getId());
		alertsSettings.setAlertDistribution(alertDistributionWidget.getDistribution());
		
		alertsSettings.setAlertExpiry(RiseUtils.strToInt(alertExpiryTextBox.getText(), 60));
		
		return alertsSettings;
	}

}