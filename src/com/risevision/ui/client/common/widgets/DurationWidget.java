// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
//import com.risevision.common.client.utils.RiseUtils;
import com.google.gwt.user.client.ui.HasText;

public class DurationWidget extends Grid implements HasText {

	private NumericBoxWidget tb = new NumericBoxWidget();
	private CheckBox cb = new CheckBox("Play Until Done");

	public DurationWidget() {
		this(true);
	}

	public DurationWidget(boolean isPlayUntilDone) {	
		super(1, 5);
		
		setDefaultDuration();
		
		styleControls();
		
		setWidget(0, 0, tb);
		setWidget(0, 1, new SpacerWidget());
		setWidget(0, 2, new Label("(seconds)"));
		setWidget(0, 3, new SpacerWidget());
		setWidget(0, 4, cb);
		
		cb.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				enableVolumeControl(!cb.getValue());
			}
		});
		
		setPlayUntilDone(isPlayUntilDone);
	}
	
	public void showPlayUntilDone(boolean isPlayUntilDone) {
		cb.setVisible(isPlayUntilDone);
	}

	private void styleControls() {
		setCellPadding(0);
		setCellSpacing(0);
		setStyleName("rdn_Table");

		tb.setStyleName("rdn-TextBoxShort");
		tb.setMaxLength(6);
		cb.setStyleName("rdn-CheckBox");		
	}
	
	public void setFocus(boolean focused) {
		tb.setFocus(focused);
	}

	@Override
	public String getText() {
		return getDuration();
	}

	@Override
	public void setText(String text) {
		// TODO Auto-generated method stub
		
	}
	
	public String getDuration() {
		//make sure value is integer and between 0 and 100
//		int value = RiseUtils.StrToInt(tb.getValue(), 100);
//		if (value < 0) 
//			value = 0;
//		else if (value > 100) 
//			value = 100;
//		return Integer.toString(value);	
		
		return tb.getValue();
	}

	public void setDuration(String value) {
		setDuration(value, false);
	}
	
	public void setDuration(String value, boolean playUntilDone) {
		setPlayUntilDone(playUntilDone);
		tb.setValue(value);	
	}

	public void setDefaultDuration() {
		setDuration("10", false);	
	}

	public boolean getPlayUntilDone() {
		return cb.getValue();	
	}

	private void setPlayUntilDone(boolean value) {
		cb.setValue(value);
		enableVolumeControl(!value);
	}

	private void enableVolumeControl(boolean value) {
		if (!value) {
			tb.setText("10");
		}
		tb.setEnabled(value);		
	}

}