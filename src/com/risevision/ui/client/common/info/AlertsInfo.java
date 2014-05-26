package com.risevision.ui.client.common.info;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class AlertsInfo implements Serializable {
	
	private boolean allowAlerts;
	private String alertURL;
	private String alertUserName;
	private String alertPassword;
	private List<String> alertSenders;
	private List<String> alertStatuses;
	private List<String> alertHandlingCodes;
	private List<String> alertCategories;
	private List<String> alertUrgencies;
	private List<String> alertSeverities;
	private List<String> alertCertainties;
	private List<String> alertEventCodes;
	private List<String> alertTextFields;
	private String alertPresentationId;
	private ArrayList<String> alertDistribution;
	private int alertExpiry;
	
	public AlertsInfo() {
		
	}

	public boolean isAllowAlerts() {
		return allowAlerts;
	}

	public void setAllowAlerts(boolean allowAlerts) {
		this.allowAlerts = allowAlerts;
	}

	public String getAlertURL() {
		return alertURL;
	}

	public void setAlertURL(String alertURL) {
		this.alertURL = alertURL;
	}

	public String getAlertUserName() {
		return alertUserName;
	}

	public void setAlertUserName(String alertUserName) {
		this.alertUserName = alertUserName;
	}

	public String getAlertPassword() {
		return alertPassword;
	}

	public void setAlertPassword(String alertPassword) {
		this.alertPassword = alertPassword;
	}

	public List<String> getAlertSenders() {
		return alertSenders;
	}

	public void setAlertSenders(List<String> alertSenders) {
		this.alertSenders = alertSenders;
	}

	public List<String> getAlertStatuses() {
		return alertStatuses;
	}

	public void setAlertStatuses(List<String> alertStatuses) {
		this.alertStatuses = alertStatuses;
	}

	public List<String> getAlertHandlingCodes() {
		return alertHandlingCodes;
	}

	public void setAlertHandlingCodes(List<String> alertHandlingCodes) {
		this.alertHandlingCodes = alertHandlingCodes;
	}

	public List<String> getAlertCategories() {
		return alertCategories;
	}

	public void setAlertCategories(List<String> alertCategories) {
		this.alertCategories = alertCategories;
	}

	public List<String> getAlertUrgencies() {
		return alertUrgencies;
	}

	public void setAlertUrgencies(List<String> alertUrgencies) {
		this.alertUrgencies = alertUrgencies;
	}

	public List<String> getAlertSeverities() {
		return alertSeverities;
	}

	public void setAlertSeverities(List<String> alertSeverities) {
		this.alertSeverities = alertSeverities;
	}

	public List<String> getAlertCertainties() {
		return alertCertainties;
	}

	public void setAlertCertainties(List<String> alertCertainties) {
		this.alertCertainties = alertCertainties;
	}

	public List<String> getAlertEventCodes() {
		return alertEventCodes;
	}

	public void setAlertEventCodes(List<String> alertEventCodes) {
		this.alertEventCodes = alertEventCodes;
	}

	public List<String> getAlertTextFields() {
		return alertTextFields;
	}

	public void setAlertTextFields(List<String> alertTextFields) {
		this.alertTextFields = alertTextFields;
	}

	public String getAlertPresentationId() {
		return alertPresentationId;
	}

	public void setAlertPresentationId(String alertPresentationId) {
		this.alertPresentationId = alertPresentationId;
	}

	public ArrayList<String> getAlertDistribution() {
		return alertDistribution;
	}

	public void setAlertDistribution(ArrayList<String> alertDistribution) {
		this.alertDistribution = alertDistribution;
	}

	public int getAlertExpiry() {
		return alertExpiry;
	}

	public void setAlertExpiry(int alertExpiry) {
		this.alertExpiry = alertExpiry;
	}

}
