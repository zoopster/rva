// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.iframe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.JsArrayString;
import com.risevision.common.client.utils.RiseUtils;

public class IFrameController {
	private static IFrameController instance;
	private static Map<String, IFramePanelWidget> widgets = new HashMap<String, IFramePanelWidget>();
	
	public static IFrameController getInstance() {
		try {
			if (instance == null) {
				instance = new IFrameController();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}
	
	private IFrameController() {
		registerJavaScriptCallbacks();
	}
	
	public void registerWidget(String id, IFramePanelWidget widget) {
		if (widget == null || RiseUtils.strIsNullOrEmpty(id)) {
			return;
		}
		
		widgets.put(id, widget);
	}
	
	public void unregisterWidget(String id) {
		widgets.remove(id);
	}
	
	private static void onMessageStatic(String source, String command, JsArrayString rawValues) {
		IFramePanelWidget widget = widgets.get(source);
		
		if (widget != null) {
			List<String> values = new ArrayList<>();
			if (rawValues != null) {
				for (int i = 0; i < rawValues.length(); i++) {
					values.add(rawValues.get(i));
				}
			}
			
			widget.onMessage(command, values);
		}
		
	}
	
	// Set up the JS-callable signature as a global JS function.
	private native void registerJavaScriptCallbacks() /*-{	
		$wnd.rva_iFrameController_onMessageStatic = @com.risevision.ui.client.common.widgets.iframe.IFrameController::onMessageStatic(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JsArrayString;);
	}-*/;
	
}