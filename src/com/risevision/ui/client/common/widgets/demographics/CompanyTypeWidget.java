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
