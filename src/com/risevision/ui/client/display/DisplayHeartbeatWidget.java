// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.display;

import com.risevision.ui.client.common.info.DisplayInfo;
import com.risevision.ui.client.common.widgets.TooltipLabelWidget;

public class DisplayHeartbeatWidget extends TooltipLabelWidget {
	private static final String BLACK_HEART = "<div style='color:black;'>\u2764</div>";
	private static final String RED_HEART = "<div style='color:red;'>\u2764</div>";
	private static final String ORANGE_HEART = "<div style='color:#fc0;'>\u2764</div>";
	private static final String GREEN_HEART = "<div style='color:green;'>\u2764</div>";
//	private static final String BLUE_HEART = "<div style='color:blue;'>\u2764</div>";
	
	public DisplayHeartbeatWidget() {
		this(DisplayInfo.DISPLAY_NOT_INSTALLED /*, null */);
	}

	public DisplayHeartbeatWidget(int displayStatus /* , Date endDate */) {	
		setStatus(displayStatus /*, endDate */);
	}
	
	public void setStatus(int displayStatus /*, Date endDate */) {
		switch (displayStatus) {
		case DisplayInfo.DISPLAY_ERROR:
			setTooltip(ORANGE_HEART, "Display Error. See the Display Errors list for details.");
			break;
		case DisplayInfo.DISPLAY_ONLINE:
			setTooltip(GREEN_HEART, "Online");
			break;
		case DisplayInfo.DISPLAY_OFFLINE:
			setTooltip(RED_HEART, "Offline");
			break;
		case DisplayInfo.DISPLAY_BLOCKED:
			setTooltip(RED_HEART, "This Display has been Blocked and the Block can last up to 3 hours.");
//					+ " The Block will expire at " + LastModifiedWidget.getLastModified(endDate));
			break;
		case DisplayInfo.DISPLAY_NOT_INSTALLED:
		default:
			setTooltip(BLACK_HEART, "Not Installed");
			break;
		}
	}

}
