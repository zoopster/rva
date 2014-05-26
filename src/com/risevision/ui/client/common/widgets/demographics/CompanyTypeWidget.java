// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.demographics;

import com.risevision.ui.client.common.widgets.RiseListBox;

public class CompanyTypeWidget extends RiseListBox {

	public CompanyTypeWidget() {
		super();
		
		loadData();
	}
	
	private void loadData() {
		addItem("", "");
		addItem("Service Provider", "serviceProvider");
		addItem("End User", "endUser");
	}
}
