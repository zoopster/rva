// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.presentation.common;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.SimplePanel;

public class HtmlEditorWidget extends SimplePanel {
	private String elementId = "htmlEditorDiv_" + System.currentTimeMillis();
	private JavaScriptObject editor;
	private boolean fixedSize = false;

	private HandlerRegistration resizeHandler;
	
	public HtmlEditorWidget() {
		this(false);
	}
	
	public HtmlEditorWidget(boolean fixedSize) {
		super();
		
		this.fixedSize = fixedSize;
		styleControls();
	}
	
	protected void onLoad() {
		if (editor == null) {
			this.getElement().setAttribute("id", elementId);
			editor = initCodeMirrorNative();	
		}
		
		if (!fixedSize) {
			resizeHandler = Window.addResizeHandler(new ResizeHandler() {	
				@Override
				public void onResize(ResizeEvent event) {
					resize();
				}
			});
			
			resize();
		}
	}
	
	protected void onUnload() {
		super.onUnload();
		
		if (resizeHandler != null) {
			resizeHandler.removeHandler();
			resizeHandler = null;
		}
		
		if (editor != null) {
			destroyInstance();
			editor = null;
		}
//		this.getElement().setInnerHTML("");
	}
	
	public void onShow() {
		refresh();
	}
	
	private void styleControls() {
		this.setStyleName("rdn-DeckPanel");
		this.getElement().getStyle().setOverflow(Overflow.HIDDEN);
	}
	
	private void resize() {
		if (Window.getClientWidth() - 10 > 200) {
			this.setWidth((Window.getClientWidth() - 31) + "px");
		}
		else {
			this.setWidth("200px");
		}
		
		if (Window.getClientHeight() - 220 > 50) {
			this.setHeight((Window.getClientHeight() - 206) + "px");
		}
		else {
			this.setHeight("50px");
		}
		
		refresh();
	}
	
	private native JavaScriptObject initCodeMirrorNative()  /*-{
//		debugger;
		
		var id = this.@com.risevision.ui.client.presentation.common.HtmlEditorWidget::elementId;
		var element = $wnd.document.getElementById(id);
		
		return new $wnd.CodeMirror(element, {
			mode: "htmlmixed",
			indentUnit: 4,
			indentWithTabs: true,
//			lineWrapping: true,
		});
	}-*/;
	
	public void setText(String text) {
		if (editor != null && text != null) {
			setTextNative(text);			
		}
	}
	
	private native void setTextNative(String text) /*-{ 
		this.@com.risevision.ui.client.presentation.common.HtmlEditorWidget::editor.setValue(text);
	}-*/;
	
	public String getText() {
		if (editor != null) {
			return getTextNative();
		}
		return "";
	}
	
	private native String getTextNative() /*-{ 
		return this.@com.risevision.ui.client.presentation.common.HtmlEditorWidget::editor.getValue();
	}-*/;
	
	private native void refresh() /*-{ 
		this.@com.risevision.ui.client.presentation.common.HtmlEditorWidget::editor.refresh();
	}-*/;
	
	private native void destroyInstance() /*-{
//		debugger;
		
		var instance = this.@com.risevision.ui.client.presentation.common.HtmlEditorWidget::editor.getWrapperElement();
		if (instance) {
			var id = this.@com.risevision.ui.client.presentation.common.HtmlEditorWidget::elementId;
			var element = $wnd.document.getElementById(id);
			if (element) {
				element.removeChild(instance);
			}
		}
	}-*/;
}
