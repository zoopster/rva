// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RiseMultiSelectWidget extends SimplePanel implements ValueChangeHandler<Boolean>, 
			HasValue<String> {
	private VerticalPanel listPanel = new VerticalPanel();
	
	private ArrayList<String> valuesList = new ArrayList<String>();
	private ArrayList<CheckBox> checkBoxList = new ArrayList<CheckBox>();
	
	TextBox otherTextBox = null;
	
	public RiseMultiSelectWidget() {
		super();
		
		add(listPanel);
		
		styleControls();
	}
	
	private void styleControls() {
//		setAlwaysShowScrollBars(false);
//		getScrollableElement().getStyle().setOverflowY(Overflow.AUTO);
//		getScrollableElement().getStyle().setOverflowX(Overflow.HIDDEN);

	}
	
	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<String> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValue(String value) {
		setValue(value, true);
	}

	@Override
	public void setValue(String value, boolean fireEvents) {
		if (fireEvents) {
			ValueChangeEvent.fire(this, value);
		}
	}
	
	@Override
	public void onValueChange(ValueChangeEvent<Boolean> event) {
		int itemIndex = checkBoxList.indexOf(event.getSource());
		
		if (itemIndex != -1) {
			setValue(valuesList.get(itemIndex), true);
		}
	}
	
	public void addItem(String name, String value) {
		CheckBox itemCheckBox = new CheckBox();
		itemCheckBox.addValueChangeHandler(this);
//		itemCheckBox.setStyleName("rdn-CheckBox");
	
		Label nameLabel = new Label(name);
		nameLabel.setStyleName("rva-ShortText");
		nameLabel.getElement().getStyle().setProperty("whiteSpace", "nowrap");
		
		HorizontalPanel checkBoxPanel = new HorizontalPanel();
		checkBoxPanel.add(itemCheckBox);
		checkBoxPanel.add(new SpacerWidget());
		checkBoxPanel.add(nameLabel);
		
		listPanel.add(checkBoxPanel);
		
		checkBoxList.add(itemCheckBox);
		valuesList.add(value);
	}
	
	public void addOther() {
		CheckBox itemCheckBox = new CheckBox();
		itemCheckBox.addValueChangeHandler(this);
//		itemCheckBox.setStyleName("rdn-CheckBox");
	
		Label nameLabel = new Label("Other:");
		nameLabel.setStyleName("rva-ShortText");
		nameLabel.getElement().getStyle().setProperty("whiteSpace", "nowrap");

		otherTextBox = new TextBox();
		
		HorizontalPanel checkBoxPanel = new HorizontalPanel();
		checkBoxPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		checkBoxPanel.add(itemCheckBox);
		checkBoxPanel.add(new SpacerWidget());
		checkBoxPanel.add(nameLabel);
		checkBoxPanel.add(new SpacerWidget());
		checkBoxPanel.add(otherTextBox);
		
		listPanel.add(checkBoxPanel);
		
		checkBoxList.add(itemCheckBox);
		valuesList.add(null);
	}

	public List<String> getSelectedValues()	{
		ArrayList<String> selectedValues = new ArrayList<String>();
		
		for (int i = 0; i < checkBoxList.size(); i++) {
			if (checkBoxList.get(i).getValue()) {
				if (valuesList.get(i) == null) {
					selectedValues.add(otherTextBox.getText());
				}
				else {
					selectedValues.add(valuesList.get(i));
				}
			}
		}
		
		return selectedValues;
	};
	
	public void setSelectedValues(List<String> values) {
		setAllValues(false);

		if (values == null) {
			return;
		}
		
		for (String value: values) {
			boolean found = false;
			for (int i = 0; i < valuesList.size(); i++) {
				if (valuesList.get(i) != null && valuesList.get(i).equals(value)) {
					checkBoxList.get(i).setValue(true);
					found = true;
					break;
				}
			}
			
			if (!found && valuesList.contains(null)) {
				checkBoxList.get(valuesList.indexOf(null)).setValue(true);
				otherTextBox.setText(value);
			}
		}
	}
	
	private void setAllValues(boolean value) {
		for (CheckBox checkBox: checkBoxList) {
			checkBox.setValue(value);
		}
	}

}