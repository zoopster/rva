// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.mediaLibrary;

import java.util.List;

import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.risevision.ui.client.common.info.WidgetSettingsInfo;
import com.risevision.ui.client.common.widgets.iframe.CommandType;
import com.risevision.ui.client.common.widgets.iframe.RpcDialogBoxWidget;
import com.risevision.ui.client.gadget.GadgetCommandHelper;

public class StorageFrameWidget extends RpcDialogBoxWidget {
	private static StorageFrameWidget instance;
	
	private static final String HTML_STRING = 
			GadgetCommandHelper.HTML_STRING +
			"" +
			"<script type=\"text/javascript\">" +
			"	var callbackFunction;" +
			"	function saveSettings(data) {" +
			"		if (callbackFunction) {" +
			"			callbackFunction(data);" +
			"			callbackFunction = null;" +
			"		}" +
			"		else {" +
			"			var params = '';" +
			"			var additionalParams = null;" +
			"			if (data) {" +
			"				if (data.params) {" +
			"					params = data.params;" +
			"				}" +
			"" +
			"				if (data.additionalParams) {" +
			"					additionalParams = data.additionalParams;" +
			"				}" +
			"			}" +
			"			var paramsArray = [ params, additionalParams ];" +
			"			%save%" +
			"		}" +
			"	}" +
			"	function closeSettings() {" +
			"		%close%" +
			"	}" +
			"	function loadEditor() {" +
			"		gadgets.rpc.register(\"rscmd_saveSettings\", saveSettings);" +
			"		gadgets.rpc.register(\"rscmd_closeSettings\", closeSettings);" +
			"" +
			"		initCallbacks();" +
			"" +
			"		gadgets.rpc.setupReceiver('if_divEditor');" +
			"	}" +
			"	window.onload = loadEditor;" +
			"" +
			"	function getSettings(callback) {" +
			"		callbackFunction = function(data) {" +
			"				callback(data);" +
			"			};" +
			"		try {" +
			"			gadgets.rpc.call(\"if_divEditor\", \"rscmd_getSettings\", callbackFunction);" +
			"		} catch (err) {" +
			"		}" +
			"	}" +
			"</script>" +
			"";
		
	private WidgetSettingsInfo widgetSettings;
	
	private Command onSave;
	
	public static StorageFrameWidget getInstance() {
		try {
			if (instance == null) {
				instance = new StorageFrameWidget();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}
	
	public StorageFrameWidget() {
		super();

		GadgetCommandHelper.init();
		
		String htmlString = HTML_STRING.replace("%save%", getButtonString(CommandType.SAVE_COMMAND, "paramsArray"))
										.replace("%close%", getButtonString(CommandType.CLOSE_COMMAND));
		
		init(htmlString);
		
	}

//	public void show(Command onSave) {
//		show(onSave, null);
//	}
	
	public void show(Command onSave, WidgetSettingsInfo widgetSettings) {
		this.onSave = onSave;
		this.widgetSettings = widgetSettings;
		
		String url = widgetSettings.getWidgetUrl();
		url += url.contains("?") ? "&" : "?";
		url += "up_id=" + "if_divEditor";
		url += "&parent=" + URL.encodeQueryString(Window.Location.getHref());
		
		url = url.replace("'", "\\'");

		show(url);
	}

	private void onWidgetSave(String params, String additionalParams) {
		hide();

		if (widgetSettings != null) {
			widgetSettings.setParams(params);

			if (onSave != null) {
				onSave.execute();
			}
		}
	}

	@Override
	public void onMessage(String command, List<String> values) {

		if (command.equals(CommandType.SAVE_COMMAND)) {
			String params = values.get(0);
			String additionalParams = values.get(1);
			
			onWidgetSave(params, additionalParams);
		}
		else if (command.equals(CommandType.CLOSE_COMMAND)) {
			hide();
		}		
		
	}

}