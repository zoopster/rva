// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.company;

import java.util.Date;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.core.api.types.CompanyStatus;
import com.risevision.ui.client.common.widgets.RiseListBox;
import com.risevision.ui.client.common.widgets.SpacerWidget;

public class CompanyStatusWidget extends HorizontalPanel{
	private static CompanyStatusWidget instance;
	
	private RiseListBox statusListBox = new RiseListBox();
	private Label lastModifiedLabel = new Label();
	
	public CompanyStatusWidget() {
		loadData();
		
		add(statusListBox);
		add(new SpacerWidget());
		add(lastModifiedLabel);
		
		statusListBox.setStyleName("rdn-ListBoxShort");
	}

	public static CompanyStatusWidget getInstance() {
		try {
			if (instance == null)
				instance = new CompanyStatusWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}

	private void loadData() {
		statusListBox.addItem("Active", CompanyStatus.ACTIVE + "");
		statusListBox.addItem("Inactive", CompanyStatus.INACTIVE + "");
	}
	
	public void setValue(int customerStatus, Date customerStatusChangeDate) {
		statusListBox.setSelectedValue(customerStatus);
		if (customerStatusChangeDate != null)
			lastModifiedLabel.setText(RiseUtils.dateToString(customerStatusChangeDate));
	}
	
	public String getValue() {
		return statusListBox.getSelectedValue();
	}
	
	public void setEnabled(boolean enabled) {
		statusListBox.setEnabled(enabled);
	}
}

