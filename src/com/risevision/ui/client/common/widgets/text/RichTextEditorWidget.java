// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.text;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.HasLoadHandlers;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;

public class RichTextEditorWidget extends Composite implements HasLoadHandlers {

	private TextArea textArea;
	private String editorId;
	private boolean isLoaded = false;
	private JavaScriptObject editorInstance;

	private HandlerRegistration loadHandler;
	private HandlerRegistration changeHandler;

	public RichTextEditorWidget() {
		editorId = "tinyMCE-" + System.currentTimeMillis();

		textArea = new TextArea();
		textArea.setWidth("90%");
		DOM.setElementAttribute(textArea.getElement(), "id", editorId);
		initWidget(textArea);

		// DeferredCommand.addCommand(new Command() {
		//
		// public void execute() {
		// initEditor();
		// }
		// });
	}

	protected void onLoad() {
		if (editorInstance == null) {
			initEditor();
		}
		else {
			focus();
		}
	}

	private native void initEditor()/*-{
		debugger;

		var editor = new $wnd.tinyMCE.Editor(this.@com.risevision.ui.client.common.widgets.text.RichTextEditorWidget::editorId, {
			theme : "advanced",
			skin : "o2k7",
			//	          width : "90%",
			//	          height : 100,
			plugins : "safari,pagebreak,style,layer,table,save,advhr,advimage,advlink,emotions,iespell,insertdatetime,preview,media,searchreplace,print,contextmenu,paste,directionality,fullscreen,noneditable,visualchars,nonbreaking,xhtmlxtras,template,inlinepopups",
	
			theme_advanced_buttons1 : "bold,italic,underline,|,bullist,numlist,|,paste,pastetext,pasteword,|,fullscreen,|,code",
			theme_advanced_buttons2 : "",
			theme_advanced_buttons3 : "",
			theme_advanced_toolbar_location : "top",
			theme_advanced_toolbar_align : "left",
			theme_advanced_statusbar_location : "none",
			theme_advanced_resizing : true
		});

		this.@com.risevision.ui.client.common.widgets.text.RichTextEditorWidget::editorInstance = editor;

		var self = this;
		editor.onInit.add(function(ed) {
			self.@com.risevision.ui.client.common.widgets.text.RichTextEditorWidget::isLoaded = true;
			self.@com.risevision.ui.client.common.widgets.text.RichTextEditorWidget::fireLoadEvent()();
		});
		editor.onKeyUp.add(function(ed, l) {
			self.@com.risevision.ui.client.common.widgets.text.RichTextEditorWidget::fireChangeEvent()();
		});

		editor.render();
	}-*/;

	public native void focus()/*-{
		this.@com.risevision.ui.client.common.widgets.text.RichTextEditorWidget::editorInstance.focus(false);
	}-*/;

	private void fireLoadEvent() {

	}

	private void fireChangeEvent() {
//	    NativeEvent nativeEvent = Document.get().createChangeEvent();
//	    ChangeEvent.fireNativeEvent(nativeEvent, this);
	}

	public void setHTML(final String html) {
		if (!isLoaded) {
			loadHandler = addLoadHandler(new LoadHandler() {
				@Override
				public void onLoad(LoadEvent event) {
					Scheduler.get().scheduleDeferred(
							new Scheduler.ScheduledCommand() {

								@Override
								public void execute() {
									_setHTML(html);
								}
							});
				}
			});

		} else {
			_setHTML(html);
		}
	}

	private native void _setHTML(String html)/*-{
		this.@com.risevision.ui.client.common.widgets.text.RichTextEditorWidget::editorInstance.setContent(html);
	}-*/;

	public native String getHTML()/*-{
		return this.@com.risevision.ui.client.common.widgets.text.RichTextEditorWidget::editorInstance.getContent();
	}-*/;

	public HandlerRegistration addLoadHandler(LoadHandler handler) {
		return addHandler(handler, LoadEvent.getType());
	}

}
