// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.alerts;

import com.risevision.core.alerts.CAP12;
import com.risevision.ui.client.common.widgets.RiseMultiSelectListBox;

public class AlertCategoryWidget extends RiseMultiSelectListBox {
	public AlertCategoryWidget() {
		super();
		
		loadData();
	}

	private void loadData() {
		addItem("Geophysical", CAP12.CATEGORY_GEOPHYSICAL);
		addItem("Meteorological", CAP12.CATEGORY_METEOROLOGICAL);
		addItem("Safety", CAP12.CATEGORY_SAFETY);
		addItem("Security", CAP12.CATEGORY_SECURITY);
		addItem("Rescue", CAP12.CATEGORY_RESCUE); 
		addItem("Fire", CAP12.CATEGORY_FIRE);
		addItem("Health", CAP12.CATEGORY_HEALTH);
		addItem("Environment", CAP12.CATEGORY_ENVIRONMENTAL); 
		addItem("Transport", CAP12.CATEGORY_TRANSPORT);
		addItem("Infrastructure", CAP12.CATEGORY_INFRASTRUCTURAL); 
		addItem("Chemical, Biological, Radiological, Nuclear, Explosive", CAP12.CATEGORY_CBRNE);
		addItem("Other", CAP12.CATEGORY_OTHER);
	}
}