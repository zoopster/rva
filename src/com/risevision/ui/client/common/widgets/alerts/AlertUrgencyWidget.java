// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.alerts;

import com.risevision.core.alerts.CAP12;
import com.risevision.ui.client.common.widgets.RiseMultiSelectListBox;

public class AlertUrgencyWidget extends RiseMultiSelectListBox {
	public AlertUrgencyWidget() {
		super();
		
		loadData();
	}

	private void loadData() {
		addItem("Immediate", CAP12.URGENCY_IMMEDIATE);
		addItem("Expected", CAP12.URGENCY_EXPECTED);
		addItem("Future", CAP12.URGENCY_FUTURE);
		addItem("Past", CAP12.URGENCY_PAST);
		addItem("Unknown", CAP12.URGENCY_UNKNOWN);
	}
}