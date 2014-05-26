// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.risevision.common.client.utils.RiseUtils;

public class ScaleWidget extends Grid{

	public static final String SCALETOFIT = "fit";

	private NumericBoxWidget tb = new NumericBoxWidget();
	private CheckBox cb = new CheckBox("Fit");
	
	public ScaleWidget() {
		super(1,3);
		
		setDefaultScale();

		styleControls();
		
		setWidget(0, 0, cb);
		setWidget(0, 1, tb);
		
		cb.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				enableScaleFactorTextBox();
			}
		});
	}

	private void styleControls() {
		setCellPadding(0);
		setCellSpacing(0);
		setStyleName("rdn_Table");
		getCellFormatter().setWidth(0, 0, "40px");

		tb.setStyleName("rdn-TextBoxShort");
		tb.setMaxLength(9);
		tb.getElement().getStyle().setWidth(135, Unit.PX);
		cb.setStyleName("rdn-CheckBox");		
	}

	public String getScale()
	{
		if (cb.getValue())
			return SCALETOFIT;
		else {
			//make sure value is integer and between 1 and 1,000,000
			//default is 100
			int value = RiseUtils.strToInt(tb.getValue(), 100);
			if (value < 1)
				value = 1;
			else if (value > 1000000) 
				value = 1000000;
			return Integer.toString(value);	
		}
	}

	public void setScale(String value)
	{
		if (SCALETOFIT.equals(value)) {
			cb.setValue(true);
			tb.setValue("100");
		} else {
			cb.setValue(false);
			tb.setValue(value);
		}
		enableScaleFactorTextBox();
	}

	public void setDefaultScale()
	{
		setScale(SCALETOFIT);
	}

	private void enableScaleFactorTextBox() {
		tb.setEnabled(!cb.getValue());		
	}
}