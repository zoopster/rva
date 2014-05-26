// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.gadget;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.core.api.types.GadgetType;
import com.risevision.ui.client.common.widgets.RiseListBox;
import com.risevision.ui.client.common.widgets.SpacerWidget;

public class GadgetTypeListBox extends HorizontalPanel {
	private RiseListBox typeListBox = new RiseListBox();
	private Label typeLabel = new Label("", false);
	
	public GadgetTypeListBox() {
		typeListBox.setStyleName("rdn-ListBoxMedium");
		add(typeListBox);
		add(new SpacerWidget());
		add(typeLabel);
		
		loadData();
		
		initControls();
	}

	private void loadData() {
		typeListBox.addItem(RiseUtils.capitalizeFirstLetter(GadgetType.GADGET), GadgetType.GADGET);
		typeListBox.addItem(RiseUtils.capitalizeFirstLetter(GadgetType.WIDGET), GadgetType.WIDGET);
		typeListBox.addItem((GadgetType.URL).toUpperCase(), GadgetType.URL);
		typeListBox.setVisibleItemCount(1);
		
		typeValueChanged();
	}
	
	private void initControls() {
		typeListBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				typeValueChanged();
			}
		});
	}
	
	public void addChangeHandler(ChangeHandler handler) {
		typeListBox.addChangeHandler(handler);
	}
	
	public void setSelectedValue(String value) {
		typeListBox.setSelectedValue(value);
		
		typeValueChanged();
	}
	
	public String getSelectedValue() {
		return typeListBox.getSelectedValue();
	}
	
	private void typeValueChanged() {
		if (typeListBox.getSelectedValue().equals(GadgetType.GADGET)) {
			typeLabel.setText("Uses Gadgets API");
		}
		else if (typeListBox.getSelectedValue().equals(GadgetType.URL)) {
			typeLabel.setText("");
		}
		else {
			typeLabel.setText("Uses Widgets API");
		}
	}

}