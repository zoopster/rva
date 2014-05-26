package com.risevision.ui.client.common.widgets.alerts;

import com.risevision.core.alerts.CAP12;
import com.risevision.ui.client.common.widgets.RiseMultiSelectListBox;

public class AlertsStatusWidget extends RiseMultiSelectListBox {
	public AlertsStatusWidget() {
		super();
		
		loadData();
	}

	private void loadData() {
		addItem("Actual", CAP12.STATUS_ACTUAL);
		addItem("Exercise", CAP12.STATUS_EXERCISE);
		addItem("System", CAP12.STATUS_SYSTEM);
		addItem("Test", CAP12.STATUS_TEST);
		addItem("Draft", CAP12.STATUS_DRAFT);
	}
}