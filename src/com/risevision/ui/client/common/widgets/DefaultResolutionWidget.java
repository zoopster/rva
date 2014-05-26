// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.ui.client.common.info.FormValidatorInfo;

public class DefaultResolutionWidget extends VerticalPanel {	

	private RiseListBox defaultResolutions = new RiseListBox();
	private Grid nonDefaultPanel = new Grid(2, 3);
	private NumericBoxWidget widthTextBox = new NumericBoxWidget();
	private NumericBoxWidget heightTextBox = new NumericBoxWidget();
	private UnitLabelWidget widthUnitLabel = new UnitLabelWidget(UnitLabelWidget.PIXEL_UNIT);
	private UnitLabelWidget heightUnitLabel = new UnitLabelWidget(UnitLabelWidget.PIXEL_UNIT);
	private FormValidatorWidget formValidator;
	
	public DefaultResolutionWidget() {
		add(defaultResolutions);
		add(nonDefaultPanel);
		
		nonDefaultPanel.setWidget(0, 0, new Label("Width:"));
		nonDefaultPanel.setWidget(0, 1, widthTextBox);
		nonDefaultPanel.setWidget(0, 2, widthUnitLabel);
		nonDefaultPanel.setWidget(1, 0, new Label("Height:"));
		nonDefaultPanel.setWidget(1, 1, heightTextBox);
		nonDefaultPanel.setWidget(1, 2, heightUnitLabel);
		
		styleControls();
		initResolutionDropdown();
	}
	
	private void styleControls() {
		nonDefaultPanel.setCellPadding(0);
		nonDefaultPanel.setCellSpacing(0);
		nonDefaultPanel.setStyleName("rdn_Table");
		nonDefaultPanel.getCellFormatter().setWidth(0, 0, "60px");

		defaultResolutions.addStyleName("rdn-ListBoxMedium");
		
		widthTextBox.addStyleName("rdn-TextBoxShort");
		heightTextBox.addStyleName("rdn-TextBoxShort");
	}
	
	private void initResolutionDropdown() {
		defaultResolutions.addItem("Custom", "");
		defaultResolutions.addItem("1280 x 720 (wide)", "1280x720");
		defaultResolutions.addItem("1280 x 768 (wide)", "1280x768");
		defaultResolutions.addItem("1360 x 768 (wide)", "1360x768");
		defaultResolutions.addItem("1366 x 768 (wide)", "1366x768");
		defaultResolutions.addItem("1440 x 900 (wide)", "1440x900");
		defaultResolutions.addItem("1680 x 1050 (wide)", "1680x1050");
		defaultResolutions.addItem("1920 x 1080 (wide)", "1920x1080");
		defaultResolutions.addItem("1024 x 768", "1024x768");
		defaultResolutions.addItem("1280 x 1024", "1280x1024");
		defaultResolutions.addItem("1600 x 1200", "1600x1200");
		defaultResolutions.addItem("720 x 1280 (portrait)", "720x1280");
        defaultResolutions.addItem("768 x 1280 (portrait)", "768x1280");
		defaultResolutions.addItem("768 x 1360 (portrait)", "768x1360");
		defaultResolutions.addItem("768 x 1366 (portrait)", "768x1366");
		defaultResolutions.addItem("1080 x 1920 (portrait)", "1080x1920");
		
		defaultResolutions.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				selectedValueChanged();
			}
		});
	}
	
	private void selectedValueChanged() {
		nonDefaultPanel.setVisible(defaultResolutions.getSelectedValue().equals(""));
		
		if (!defaultResolutions.getSelectedValue().equals("")) {
			String widthStr = defaultResolutions.getSelectedValue();
			widthStr = widthStr.substring(0, widthStr.indexOf("x"));
			widthTextBox.setText(widthStr);
			
			String heightStr = defaultResolutions.getSelectedValue();
			heightStr = heightStr.substring(heightStr.indexOf("x") + 1, heightStr.length());
			heightTextBox.setText(heightStr);
			
			widthUnitLabel.setText(UnitLabelWidget.PIXEL_UNIT);
			heightUnitLabel.setText(UnitLabelWidget.PIXEL_UNIT);
		}
	}
	
	private void initValidator(){
		if (formValidator != null) {
			formValidator.addValidationElement(heightTextBox, "Height", FormValidatorInfo.requiredFieldValidator);
			formValidator.addValidationElement(widthTextBox, "Width", FormValidatorInfo.requiredFieldValidator);
		}
	}
	
	public int getWidth() {
		return RiseUtils.strToInt(widthTextBox.getValue(), 0);
	}
	
	public String getWidthUnits() {
		return widthUnitLabel.getText();
	}
	
	public int getHeight() {
		return RiseUtils.strToInt(heightTextBox.getValue(), 0);
	}
	
	public String getHeightUnits() {
		return heightUnitLabel.getText();
	}
	
	public void setValue(int width, int height) {
		setValue(width, UnitLabelWidget.PIXEL_UNIT, height, UnitLabelWidget.PIXEL_UNIT);
	}
	
	public void setValue(int width, String widthUnits, int height, String heightUnits) {
		for (int i = 1; i < defaultResolutions.getItemCount(); i++) {
			if (defaultResolutions.getValue(i) != null && defaultResolutions.getValue(i).equals(width + "x" + height)) {
				defaultResolutions.setSelectedIndex(i);
				selectedValueChanged();
				nonDefaultPanel.setVisible(false);
				return;
			}
		}

		defaultResolutions.setSelectedIndex(0);
		nonDefaultPanel.setVisible(true);
		widthTextBox.setText(width + "");
		widthUnitLabel.setText(widthUnits);
		heightTextBox.setText(height + "");
		heightUnitLabel.setText(heightUnits);
	}
	
	public void setValidator(FormValidatorWidget formValidator) {
		this.formValidator = formValidator;
		initValidator();
	}
}
