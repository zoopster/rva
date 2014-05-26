package com.risevision.ui.client.display;

import com.risevision.core.api.types.DisplayStatus;
import com.risevision.ui.client.common.info.DisplayInfo;
import com.risevision.ui.client.common.widgets.RiseListBox;

public class DisplayStatusWidget extends RiseListBox{

	private static DisplayStatusWidget instance;
	
	public DisplayStatusWidget() {
		loadData();
	}

	public static DisplayStatusWidget getInstance() {
		try {
			if (instance == null)
				instance = new DisplayStatusWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}

	private void loadData() {
		addItem(DisplayInfo.STATUSNAME_ACTIVE, Integer.toString(DisplayStatus.ACTIVE));
		addItem(DisplayInfo.STATUSNAME_INACTIVE, Integer.toString(DisplayStatus.INACTIVE));
	}

}