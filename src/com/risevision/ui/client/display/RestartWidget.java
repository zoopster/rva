package com.risevision.ui.client.display;

import java.util.Date;

import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;

import com.risevision.common.client.utils.RiseUtils;
import com.risevision.ui.client.common.widgets.TimeListBoxWidget;

public class RestartWidget extends HorizontalPanel {
//	private CheckBox cbRestartEnabled = new CheckBox();
	private TimeListBoxWidget lstRestartTime = new TimeListBoxWidget();
	//private Button btRebootNow = new Button("Reboot Now");
//	private InlineHTML lbRestartTime = new InlineHTML("&nbsp;&nbsp;at&nbsp;&nbsp;");

	public RestartWidget() {
		setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
//		add(cbRestartEnabled);
//		add(lbRestartTime);
		add(lstRestartTime);	
			
		styleControls();
		
		lstRestartTime.addItem("None", "none");
		
//		cbRestartEnabled.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
//			public void onValueChange(ValueChangeEvent<Boolean> event) {
//				lstRestartTime.setEnabled(cbRestartEnabled.getValue());
//			}
//		});
	}

	private void styleControls() {
//		cbRestartEnabled.setStyleName("rdn-CheckBox");
//		lbRestartTime.setStyleName("rdn-TextBox");
		//btRebootNow.setStyleName("rdn-Button");
	}
	
	public void loadData(boolean isEnabled, String value) {
		if (!isEnabled) {
			lstRestartTime.setSelectedValue("none");
		}
		else if (value == null) {
			lstRestartTime.setSelectedIndex(0); //12:00 AM
		}
		else {
			lstRestartTime.setValue(RiseUtils.stringToTimeISO8061(value));
		}

//		cbRestartEnabled.setValue(isEnabled);
//		lstRestartTime.setEnabled(isEnabled);
	}
	
	public String getRestartTime() {
		if (getRestartEnabled()) {
			return RiseUtils.dateToStringRfc822IgnoreTimezone(lstRestartTime.getValue());
		}
		else {
			return getDefaultRestartTime();
		}
	}
	
	public boolean getRestartEnabled() {
		return !lstRestartTime.getSelectedValue().equals("none");
	}
	
	@SuppressWarnings("deprecation")
	private String getDefaultRestartTime() {
		Date startTime = new Date(0,0,0,3,0,0); 		
		return RiseUtils.dateToStringRfc822IgnoreTimezone(startTime);
	}
}