// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.iframe;

import java.util.List;

import com.google.gwt.user.client.Command;

public final class DialogBoxWidget extends IFramePanelWidget {
	private static DialogBoxWidget instance;
	
	private Command command;
	private boolean result = false;
	
	public static DialogBoxWidget getInstance() {
		try {
			if (instance == null) {
				instance = new DialogBoxWidget();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}
	
	private DialogBoxWidget() {
		super();
	}

	public void showPanel(Command command, boolean show, String htmlString) {
		this.command = command;
		
		htmlString = htmlString.replace("%okay%", getOkayButtonString())
				.replace("%close%", getCloseButtonString());
		
		init(htmlString);
		show();
	}


	private void onClose() {
		result = false;
		
		closePanel();
	}
	
	private void onOkay() {
		result = true;
		
		closePanel();
	}
	
	private void closePanel() {
		hide();
		
		if (command != null) command.execute();
	}
	
	public boolean getResult() {
		return result;
	}
	
	protected String getCloseButtonString() {
		return "parent.rva_iFrameController_onMessageStatic(\"" + iFrameName + "\", \"" + CommandType.CLOSE_COMMAND + "\");";
	}

	protected String getOkayButtonString() {
		return "parent.rva_iFrameController_onMessageStatic(\"" + iFrameName + "\", \"" + CommandType.OKAY_COMMAND + "\");";
	}
	
	// Set up the JS-callable signature as a global JS function.
//	private native void registerJavaScriptCallbacks() /*-{
//		$wnd.rdn2_dialogBox_close = @com.risevision.ui.client.common.widgets.iframe.DialogBoxWidget::onCloseStatic();
//		$wnd.rdn2_dialogBox_okay = @com.risevision.ui.client.common.widgets.iframe.DialogBoxWidget::onOkayStatic();
//	}-*/;

	@Override
	public void onMessage(String command, List<String> values) {
		// TODO Auto-generated method stub
		if (command.equals(CommandType.CLOSE_COMMAND)) {
			onClose();
		}
		else if (command.equals(CommandType.OKAY_COMMAND)) {
			onOkay();
		}
	}

}
