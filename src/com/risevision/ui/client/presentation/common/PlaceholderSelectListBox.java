package com.risevision.ui.client.presentation.common;

import com.risevision.common.client.info.PlaceholderInfo;
import com.risevision.common.client.info.PresentationInfo;
import com.risevision.ui.client.common.widgets.RiseListBox;

public class PlaceholderSelectListBox extends RiseListBox {

	public PlaceholderSelectListBox() {
		super();
		
		setStyleName("rdn-ListBoxMedium");
	}
	
	public void bindSelectListBox(PresentationInfo presentation, String selectedPlaceholder) {
		clear();
		if (presentation != null) {
			for (PlaceholderInfo ph: presentation.getPlaceholders()) {
				if (!ph.isDeleted()) {
					if (ph.getNewId() != null && !ph.getNewId().isEmpty()) {
						addItemAlphabetically(ph.getNewId(), ph.getId());
					}
					else {
						addItemAlphabetically(ph.getId(), ph.getId());
					}
				}
			}
			
			setSelectedValue(selectedPlaceholder);
		}
	}
	
}
