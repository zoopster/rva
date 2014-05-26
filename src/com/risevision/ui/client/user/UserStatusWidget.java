// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.user;

import com.risevision.core.api.types.UserStatus;
import com.risevision.ui.client.common.widgets.RiseListBox;

public class UserStatusWidget extends RiseListBox{

	private static UserStatusWidget instance;
	
	public UserStatusWidget() {
		loadData();
	}

	public static UserStatusWidget getInstance() {
		try {
			if (instance == null)
				instance = new UserStatusWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}

	private void loadData() {
		addItem("Inactive", UserStatus.INACTIVE + "");
		addItem("Active", UserStatus.ACTIVE + "");			
		setVisibleItemCount(1);
	}

}