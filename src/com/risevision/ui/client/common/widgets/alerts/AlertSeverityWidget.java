package com.risevision.ui.client.common.widgets.alerts;

import com.risevision.core.alerts.CAP12;
import com.risevision.ui.client.common.widgets.RiseMultiSelectListBox;

public class AlertSeverityWidget extends RiseMultiSelectListBox {
	public AlertSeverityWidget() {
		super();
		
		loadData();
	}

	private void loadData() {
		addItem("Extreme", CAP12.SEVERITY_EXTREME);
		addItem("Severe", CAP12.SEVERITY_SEVERE);
		addItem("Moderate", CAP12.SEVERITY_MODERATE);
		addItem("Minor", CAP12.SEVERITY_MINOR);
		addItem("Unknown", CAP12.SEVERITY_UNKNOWN);
	}
}