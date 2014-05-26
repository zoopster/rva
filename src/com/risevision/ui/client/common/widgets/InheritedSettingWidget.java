package com.risevision.ui.client.common.widgets;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.risevision.common.client.utils.Global;

public class InheritedSettingWidget extends HorizontalPanel {
	private boolean inheritedCheckBoxIsVisible;
	private String inheritedCheckBoxLabel;
	private CheckBox cb = new CheckBox("inherited");
	private Widget wg;
		
//	public DefaultSettingWidget() {
//	}

	public InheritedSettingWidget(boolean inheritedCheckBoxIsVisible, String inheritedCheckBoxLabel) {
		this.inheritedCheckBoxIsVisible = inheritedCheckBoxIsVisible;
		this.inheritedCheckBoxLabel = inheritedCheckBoxLabel;
	}

	public void setWidget(Widget widget) {	
		wg = widget;
		
		cb.setVisible(inheritedCheckBoxIsVisible);
		setIsInherited(inheritedCheckBoxIsVisible);
		
		styleControls();
		
		if (inheritedCheckBoxIsVisible) {
			add(cb);
			add(new SpacerWidget());
			add(wg);
			cb.setText(inheritedCheckBoxLabel);
			cb.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					wg.setVisible(!cb.getValue());
				}
			});
		}
		else {
			add(wg);
		}
	}

	private void styleControls() {
		cb.setStyleName("rdn-CheckBox");
	}

	public void setIsInherited(boolean value) {
		if (!cb.isVisible())
			value = false;
		
		cb.setValue(value);
		wg.setVisible(!value);			
	}

	public boolean getIsInherited() {
		return cb.getValue() && cb.isVisible();
	}
	
	protected boolean valueIsInherited(String value) {
		return Global.INHERITED.equals(value);
	}

	protected String inheritedToValue(boolean isInherited, String value) {
		if (isInherited)
			return Global.INHERITED;
		else
			return value;
	}
}
