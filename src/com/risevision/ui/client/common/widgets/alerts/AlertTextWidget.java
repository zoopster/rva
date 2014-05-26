 package com.risevision.ui.client.common.widgets.alerts;

import com.risevision.core.alerts.CAP12;
import com.risevision.ui.client.common.widgets.RiseMultiSelectListBox;

public class AlertTextWidget extends RiseMultiSelectListBox {
	public AlertTextWidget() {
		super();
		
		loadData();
	}

	private void loadData() {
		addItem("Headline", CAP12.TEXT_HEADLINE);
		addItem("Description", CAP12.TEXT_DESCRIPTION);
		addItem("Instruction", CAP12.TEXT_INSTRUCTION);
	}
}