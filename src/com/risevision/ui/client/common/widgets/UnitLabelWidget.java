package com.risevision.ui.client.common.widgets;

import com.google.gwt.user.client.ui.Label;

public class UnitLabelWidget extends Label {
	private static String PIXEL_LABEL = "Pixels";
	public static String PIXEL_UNIT = "px";
	public static String PERCENT_UNIT = "%";
	
	public UnitLabelWidget(String text) {
		super(text);

		if (text.equals(PIXEL_UNIT)) {
			setText(PIXEL_LABEL);
		}
	}
	
	public void setText(String text) {
		if (text.equals(PIXEL_UNIT)) {
			text = PIXEL_LABEL;
		}
		
		super.setText(text);
	}
	
	public String getText() {
		String text = super.getText();
		
		if (text.equals(PIXEL_LABEL)) {
			text = PIXEL_UNIT;
		}
		
		return text;
	}
}
