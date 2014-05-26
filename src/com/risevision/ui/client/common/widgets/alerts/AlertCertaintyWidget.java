 package com.risevision.ui.client.common.widgets.alerts;

import com.risevision.core.alerts.CAP12;
import com.risevision.ui.client.common.widgets.RiseMultiSelectListBox;

public class AlertCertaintyWidget extends RiseMultiSelectListBox {
	public AlertCertaintyWidget() {
		super();
		
		loadData();
	}

	private void loadData() {
		addItem("Observed", CAP12.CERTAINTY_OBSERVED);
		// Deprecated in CAP12
//		addItem("Very Likely", CAP12.CERTAINTY_VERY_LIKELY);
		addItem("Likely", CAP12.CERTAINTY_LIKELY);
		addItem("Possible", CAP12.CERTAINTY_POSSIBLE);
		addItem("Unlikely", CAP12.CERTAINTY_UNLIKELY);
		addItem("Unknown", CAP12.CERTAINTY_UNKNOWN);
	}
}