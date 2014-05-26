// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.messages;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.PopupPanel;
import com.risevision.ui.client.common.utils.HtmlUtils;

public final class DialogBoxWidget extends Frame {
	private static DialogBoxWidget instance;
	
	private PopupPanel containerPanel = new PopupPanel(false, false);
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
		containerPanel.add(this);
		
		registerJavaScriptCallbacks();
		
		styleControls();
		
	}

	private void styleControls() {
		setSize("100%", "100%");
		
		getElement().getStyle().setBorderWidth(0, Unit.PX);
		getElement().setAttribute("frameborder", "0");
		getElement().setAttribute("scrolling", "no");
		getElement().getStyle().setPropertyPx("minHeight", 600);
		
		containerPanel.removeStyleName("gwt-PopupPanel");
		containerPanel.getElement().getStyle().setProperty("width", "100%");
		containerPanel.getElement().getStyle().setProperty("height", "100%");
		containerPanel.getElement().getStyle().setBackgroundColor("transparent");
		
		showPanel(false);
	}
	
	private void showPanel(boolean show) {
		showPanel(show, "");
	}
	
	private void showPanel(Command command, boolean show) {
		showPanel(command, show, "");
	}
	
	public void showPanel(Command command, boolean show, String htmlString) {
		this.command = command;
		
		showPanel(show, htmlString);
	}
	
	private void showPanel(boolean show, String htmlString) {
		containerPanel.getElement().getStyle().setZIndex(show ? 1000 : -1000);
		containerPanel.getElement().getStyle().setOpacity(show ? 1 : 0);
		
		if (show) {
			containerPanel.show();
			
			HtmlUtils.writeHtml(getElement(), htmlString);	
		}
		else containerPanel.hide();
	}

	private static void onCloseStatic() {
		instance.result = false;
		
		instance.closePanel();
	}
	
	private static void onOkayStatic() {
		instance.result = true;
		
		instance.closePanel();
	}
	
	private void closePanel() {
		showPanel(false);
		
		if (command != null) command.execute();
	}
	
	public boolean getResult() {
		return result;
	}
	
	// Set up the JS-callable signature as a global JS function.
	private native void registerJavaScriptCallbacks() /*-{
		$wnd.rdn2_dialogBox_close = @com.risevision.ui.client.common.widgets.messages.DialogBoxWidget::onCloseStatic();
		$wnd.rdn2_dialogBox_okay = @com.risevision.ui.client.common.widgets.messages.DialogBoxWidget::onOkayStatic();
	}-*/;

}
