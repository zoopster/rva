// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets;

import com.risevision.common.client.info.PlaceholderInfo;

public class TransitionWidget extends InheritedSettingWidget {
	//UI: default settings: Transition
	private RiseListBox lsTransition = new RiseListBox();
	private boolean inheritedCheckBoxIsVisible;
	
	public TransitionWidget() {
		this(true);
	}
	
	public TransitionWidget(boolean inheritedCheckBoxIsVisible) {
		this(inheritedCheckBoxIsVisible, "Inherited");
	}

	public TransitionWidget(boolean inheritedCheckBoxIsVisible, String inheritedCheckBoxLabel) {
		super(inheritedCheckBoxIsVisible, inheritedCheckBoxLabel);
		this.inheritedCheckBoxIsVisible = inheritedCheckBoxIsVisible;
		
		setWidget(lsTransition);

		lsTransition.setStyleName("rdn-ListBoxShort");
		
		for (String[] s : PlaceholderInfo.TRANSITION_TYPES) {
			lsTransition.addItem(s[0], s[1]);
		}
	}
	
	public String getTransition() {
		return inheritedToValue(getIsInherited(), lsTransition.getSelectedValue());
	}

	public void setTransition(String value) {
		// case when #inherited# is set, but the widget doesn't have inherited - put default value
		if (!inheritedCheckBoxIsVisible && valueIsInherited(value)) {
			lsTransition.setSelectedValue("none");
			return;
		}
		
		setIsInherited(valueIsInherited(value));

		if (!valueIsInherited(value))
			lsTransition.setSelectedValue(value);
	}
}
