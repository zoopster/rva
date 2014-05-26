package com.risevision.ui.client.common.widgets.demographics;

import com.risevision.ui.client.common.widgets.RiseMultiSelectWidget;

public class MarketSegmentsWidget extends RiseMultiSelectWidget {
	public MarketSegmentsWidget() {
		super();
		
		loadData();
	}

	private void loadData() {
		addItem("Education", "education");
		addItem("Financial", "financial");
		addItem("Health Care", "healthCare");
		addItem("Hospitality", "hospitality");
		addItem("Manufacturing", "manufacturing");
		addItem("Technology", "technology");
		addItem("Non-Profit", "nonprofit");
		addItem("Religious", "religious");
		addItem("Quick Serve", "quickServe");
		addItem("Retail", "retail");
		addItem("Service", "service");
		addOther();
	}
}