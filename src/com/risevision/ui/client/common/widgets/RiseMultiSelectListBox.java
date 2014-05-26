// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RiseMultiSelectListBox extends ScrollPanel {
	private VerticalPanel listPanel = new VerticalPanel();
	
	private ArrayList<String> valuesList = new ArrayList<String>();
	private ArrayList<CheckBox> checkBoxList = new ArrayList<CheckBox>();
	
	public RiseMultiSelectListBox() {
		super();
		
		add(listPanel);
		
		styleControls();
	}
	
	private void styleControls() {
		setAlwaysShowScrollBars(false);
		getScrollableElement().getStyle().setOverflowY(Overflow.AUTO);
		getScrollableElement().getStyle().setOverflowX(Overflow.HIDDEN);

	}
	
	public void addItem(String name, String value) {
		CheckBox itemCheckBox = new CheckBox();
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

	public List<String> getSelectedValues()	{
		ArrayList<String> selectedValues = new ArrayList<String>();
		
		for (int i = 0; i < checkBoxList.size(); i++) {
			if (checkBoxList.get(i).getValue()) {
				selectedValues.add(valuesList.get(i));
			}
		}
		
		return selectedValues;
	};
	
	public void setSelectedValues(List<String> values) {
		setAllValues(false);

		if (values == null) {
			// All values selected by default
			// If we don't want this behavior, initialize the values list
			setAllValues(true);
			return;
		}
		
		for (int i = 0; i < checkBoxList.size(); i++) {
			for (String value: values) {
				if (valuesList.get(i).equals(value)) {
					checkBoxList.get(i).setValue(true);
					break;
				}
			}
		}
	}
	
	private void setAllValues(boolean value) {
		for (CheckBox checkBox: checkBoxList) {
			checkBox.setValue(value);
		}
	}

}