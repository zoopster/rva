package com.risevision.ui.client.display;

import com.google.gwt.user.client.ui.Label;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.core.api.types.PlayerOS;

public class PlayerOSLabel extends Label {
	public static final String WINDOWS = "Windows";
	public static final String LINUX = "Linux 32-bit";
	public static final String LINUX_64BIT = "Linux 64-bit";
	public static final String MAC_OS = "Mac OS";
	public static final String RASPBIAN = "Raspbian";
	
	public void setVersion(String value) {
		if (RiseUtils.strIsNullOrEmpty(value)) {
			setText("N/A");
		}
		else if (value.equals(PlayerOS.WINDOWS)) {
			setText(WINDOWS);
		}
		else if (value.equals(PlayerOS.LINUX)) {
			setText(LINUX);
		}
		else if (value.equals(PlayerOS.LINUX_64BIT)) {
			setText(LINUX_64BIT);
		}
		else if (value.equals(PlayerOS.MAC_OS)) {
			setText(MAC_OS);
		}
		else if (value.equals(PlayerOS.RASPBIAN)) {
			setText(RASPBIAN);
		}
		else {
			setText(value);
		}

	}
	
}
