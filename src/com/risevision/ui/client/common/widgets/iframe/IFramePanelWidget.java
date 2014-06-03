// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.iframe;

import java.util.Date;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.PopupPanel;
import com.risevision.ui.client.common.utils.HtmlUtils;

public abstract class IFramePanelWidget extends PopupPanel {
	protected Frame iFrameElement = new Frame();
	protected String iFrameName = "if-" + Integer.toString((int) (Math.random() * 10000)) + "-" + new Date().getTime();

	public IFramePanelWidget() {
		this.add(iFrameElement);
		
		iFrameElement.getElement().setPropertyString("name", iFrameName);
		
		styleControls();
		
		IFrameController.getInstance().registerWidget(iFrameName, this);
		
	}
	
	protected void onDetach() {
		super.onDetach();

		IFrameController.getInstance().unregisterWidget(iFrameName);
		
	}

	private void styleControls() {
		setSize("100%", "100%");
		
		iFrameElement.getElement().getStyle().setBorderWidth(0, Unit.PX);
		iFrameElement.getElement().setAttribute("frameborder", "0");
		iFrameElement.getElement().setAttribute("scrolling", "no");
		
		this.removeStyleName("gwt-PopupPanel");
		this.getElement().getStyle().setProperty("width", "100%");
		this.getElement().getStyle().setProperty("height", "100%");
		this.getElement().getStyle().setBackgroundColor("transparent");

		showPanel(false);
		super.show();
	}
	
	private void showPanel(boolean show) {
		this.getElement().getStyle().setZIndex(show ? 1000 : -1000);
		this.getElement().getStyle().setOpacity(show ? 1 : 0);
		
//		if (show) super.show();
//		else super.hide();
	}
	
	protected void init(String htmlString) {
//		this.addLoadHandler(new LoadHandler() {
//			@Override
//			public void onLoad(LoadEvent event) {
//			}
//		});
				
		HtmlUtils.writeHtml(iFrameElement.getElement(), htmlString);
		
	}

	protected void onLoad() {
		super.onLoad();
	}

	public void show() {
		showPanel(true);
	}
	
	public void hide() {
		showPanel(false);
	}
	
	public abstract void onMessage(String command, List<String> values);
	
//	public String getIFrameName() {
//		return iFrameName;
//	}

	
	protected String getButtonString(String commandName) {
		return "parent.rva_iFrameController_onMessageStatic(\"" + iFrameName + "\", \"" + commandName + "\");";
	}

	protected String getButtonString(String commandName, String valueName) {
		return "parent.rva_iFrameController_onMessageStatic(\"" + iFrameName + "\", \"" + commandName + "\", " + valueName + ");";
	}
	
}