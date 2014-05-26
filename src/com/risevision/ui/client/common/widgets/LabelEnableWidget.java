// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

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