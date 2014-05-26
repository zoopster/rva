// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets;

import java.util.Date;

import com.google.gwt.dom.client.Style.Unit;
import com.risevision.common.client.utils.RiseUtils;

public class TimeListBoxWidget extends RiseListBox {

	public TimeListBoxWidget() {		
		populateTimes();
		
		setStyleName("rdn-ListBoxShort");
		getElement().getStyle().setWidth(85, Unit.PX);
	}
	
	@SuppressWarnings("deprecation")
	private void populateTimes() {
		Date startTime = new Date(0,0,0,0,0,0); 		
		for (int i=0; i<24; i++) {
			startTime.setHours(i);
			for (int j=0; j<4; j++) {
				startTime.setMinutes(j*15);
				//addItem(DateTimeFormat.getShortTimeFormat().format(startTime), Long.toString(startTime.getTime()));
				//addItem(DateTimeFormat.getShortTimeFormat().format(startTime));
				addItem(RiseUtils.timeToString(startTime));
			}
		}
	}

	public Date getValue() {
		return RiseUtils.stringToTime(getSelectedValue());
		//return DateTimeFormat.getShortTimeFormat().parse(getSelectedValue());
	}

	public void setValue(Date value) {
		try {
			setSelectedValue(RiseUtils.timeToString(value));
			//setSelectedValue(DateTimeFormat.getShortTimeFormat().format(value));
		} catch (Exception e) {
		}		
	}
}
