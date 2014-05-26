// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.demographics;

import com.risevision.ui.client.common.widgets.RiseMultiSelectWidget;

public class TargetGroupsWidget extends RiseMultiSelectWidget {
	public TargetGroupsWidget() {
		super();
		
		loadData();
	}

	private void loadData() {
		addItem("Customers", "customers");
		addItem("Employees", "employees");
		addItem("Community", "community");
		addOther();
	}
}