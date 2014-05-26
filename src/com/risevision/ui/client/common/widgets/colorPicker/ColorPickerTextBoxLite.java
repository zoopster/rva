package com.risevision.ui.client.common.widgets.colorPicker;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.TextBox;

public class ColorPickerTextBoxLite extends TextBox implements ClickHandler {
	private ColorPickerDialog colorPicker = ColorPickerDialog.getInstance();
	private Command hideCommand;
	
	public ColorPickerTextBoxLite() {
		addClickHandler(this);
		hideCommand = new Command() {
			@Override
			public void execute() {
				// fire value change handler
				setValue(colorPicker.getColor(), true);
			}
		};
	}

	@Override
	public void onClick(ClickEvent event) {
		colorPicker.show(hideCommand);
	}
}
