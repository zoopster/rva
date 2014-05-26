package com.risevision.ui.client.common.widgets;

import com.google.gwt.user.client.ui.InlineLabel;

public class LabelEnableWidget extends InlineLabel {

	public void setEnabled(boolean enabled) {
		if (enabled)
			removeStyleName("rdn-Disabled");
		else
			addStyleName("rdn-Disabled");
	}
}