// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.display;

import com.risevision.core.api.types.BrowserUpgradeMode;
import com.risevision.ui.client.common.info.DisplayInfo;
import com.risevision.ui.client.common.widgets.RiseListBox;

public class BrowserUpgradeModeWidget extends RiseListBox {
	
	public BrowserUpgradeModeWidget() {
		super();
		
		addItem(DisplayInfo.BROWSER_AUTOUPGRADE, Integer.toString(BrowserUpgradeMode.AUTO));
		addItem(DisplayInfo.BROWSER_CURRENT, Integer.toString(BrowserUpgradeMode.CURRENT_VERSION));
		addItem(DisplayInfo.BROWSER_PREVIOUS, Integer.toString(BrowserUpgradeMode.PREVIOUS_VERSION));
		addItem(DisplayInfo.BROWSER_USER_MANAGED, Integer.toString(BrowserUpgradeMode.USER_MANAGED));
		
		setStyleName("rdn-ListBoxMedium");
	}
}
