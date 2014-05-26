package com.risevision.ui.client.common.widgets;

import com.google.gwt.user.client.ui.Grid;
import com.risevision.common.client.utils.RiseUtils;

public class VolumeWidget extends Grid{

	private NumericBoxWidget tb = new NumericBoxWidget();
//	private CheckBox cb = new CheckBox("mute");

	public VolumeWidget() {
		super(1,3);
		
		setDefaultVolume();
		
		styleControls();
		
		setWidget(0, 0, tb);
		setHTML(0, 1, "&nbsp&nbsp(0-100)&nbsp&nbsp");
//		setWidget(0, 2, cb);
		
//		cb.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
//			public void onValueChange(ValueChangeEvent<Boolean> event) {
//				enableVolumeControl(!cb.getValue());
//			}
//		});
	}

	private void styleControls() {
		setCellPadding(0);
		setCellSpacing(0);
		setStyleName("rdn_Table");

		tb.setStyleName("rdn-TextBoxShort");
		tb.setMaxLength(6);
//		cb.setStyleName("rdn-CheckBox");		
	}

	public String getVolume()
	{
		//make sure value is integer and between 0 and 100
		int value = RiseUtils.strToInt(tb.getValue(), 100);
		if (value < 0) 
			value = 0;
		else if (value > 100) 
			value = 100;
		return Integer.toString(value);	
	}

	public void setVolume(String value)
	{
		tb.setValue(value);	
	}

	public void setDefaultVolume()
	{
		setVolume("100");	
	}

//	public boolean getMute()
//	{
//		return cb.getValue();	
//	}
//
//	public void setMute(boolean value)
//	{
//		cb.setValue(value);
//		enableVolumeControl(!value);
//	}
//
//	private void enableVolumeControl(boolean value) {
//		tb.setEnabled(value);		
//	}
}