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