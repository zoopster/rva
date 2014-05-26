// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.TextBox;

public class NumericBoxWidget extends TextBox {

	public NumericBoxWidget() {
		this(false);
	}
	
	public NumericBoxWidget(final boolean decimal) {
		super();

		addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				char keyCode = event.getCharCode();
				if (!Character.isDigit(keyCode) 
//						&& Character.isLetter(keyCode)
//						&& (keyCode != (char) KeyCodes.KEY_TAB)
//						&& (keyCode != (char) KeyCodes.KEY_BACKSPACE)
//						&& (keyCode != (char) KeyCodes.KEY_DELETE)
//						&& (keyCode != (char) KeyCodes.KEY_ENTER)
//						&& (keyCode != (char) KeyCodes.KEY_HOME)
//						&& (keyCode != (char) KeyCodes.KEY_END)
//						&& (keyCode != (char) KeyCodes.KEY_LEFT)
//						&& (keyCode != (char) KeyCodes.KEY_UP)
//						&& (keyCode != (char) KeyCodes.KEY_RIGHT)
//						&& (keyCode != (char) KeyCodes.KEY_DOWN)
						// Fix for FF (which still sends event for Arrow/Delete/Backspace, etc)
						&& (keyCode != (char) 0)
						&& !(decimal && (keyCode == '.'))
						) {
					// TextBox.cancelKey() suppresses the current keyboard event.
					cancelKey();
				}
			}
		});
	}

}