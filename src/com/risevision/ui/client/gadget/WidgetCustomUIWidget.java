// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.gadget;

import java.util.List;

import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.risevision.ui.client.common.controller.SelectedCompanyController;
import com.risevision.ui.client.common.info.WidgetSettingsInfo;
import com.risevision.ui.client.common.widgets.iframe.CommandType;
import com.risevision.ui.client.common.widgets.iframe.RpcDialogBoxWidget;
import com.risevision.ui.client.presentation.PresentationLayoutWidget;

public class WidgetCustomUIWidget extends RpcDialogBoxWidget {
	private static WidgetCustomUIWidget instance;
	
	private static final String HTML_STRING = "" +
			GadgetCommandHelper.HTML_STRING +
			"" +
			"<script type=\"text/javascript\">" +
			"	var callbackFunction;" +
			"	function getAdditionalParams() {" +
			"		return parent.rdn2_rpc_widgetUI_widgetGetAdditionalParams();" +
//			"		return \"%s1%\";" +
			"	}" +
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
//			"	function editorFrameReady() {" +
//			"		parent.rdn2_rpc_customUI_editorFrameReady();" +
//			"	}" +
			"	function loadEditor() {" +
//			"		addWidget('if_divEditor', html);" +
			"" +
//			"		var gadgetUrlParams = \"&url=\" + \"%s0%\";" +
//			"		var gadgetUrl = \"http://rvagadgets.appspot.com/?view=editor\";" +
//			"		var commonParams = \"&up_id=test1&up_rsA=mc&up_rsS=90&up_rsH=500&up_rsW=600\";" +
//			"		var editorPosition = \"&w=600&h=400&l=0&t=0&pid=divEditor\";" +
//			"		if (gadgetUrlParams !== null) {" +
//			"			var head = document.getElementsByTagName('head')[0];" +
//			"			var scriptE = document.createElement('script');" +
//			"			scriptE.type = 'text/javascript';" +
//			"			scriptE.src = gadgetUrl + gadgetUrlParams + editorPosition;" +
//			"			head.appendChild(scriptE);" +
//			"		}	" +
			"		gadgets.rpc.register(\"rscmd_getAdditionalParams\", getAdditionalParams);" +
			"		gadgets.rpc.register(\"rscmd_saveSettings\", saveSettings);" +
			"		gadgets.rpc.register(\"rscmd_closeSettings\", closeSettings);" +
			"" +
			"		gadgets.rpc.register('rsmakeRequest_get', function(id, callbackName, url, optParams) {" +
			"			gadgets.io.makeRequest(url, function(data) {" +
			"				gadgets.rpc.call('if_' + id, callbackName, null, data);" +
			"			}, optParams);" +
			"		});" +
			"" +
			"		initCallbacks();" +
			"" +
			"		gadgets.rpc.setupReceiver('if_divEditor');" +
			"	}" +
			"	window.onload = loadEditor;" +
			"" +
//			"	function addWidget(frameName, html) {" +
//			"   	var myFrame = document.getElementById(frameName);" +
//			"" +
//			"		var myFrameObj = (myFrame.contentWindow) ? myFrame.contentWindow : (myFrame.contentDocument.document) ? myFrame.contentDocument.document : myFrame.contentDocument;" +
//			"		myFrameObj.document.open();" +
//			"		myFrameObj.document.write(html);" +
//			"		myFrameObj.document.close();" +
//			"" +
//			"		gadgets.rpc.setupReceiver(frameName);" +
//			"	}" +
			"	function getSettings(callback) {" +
			"		callbackFunction = function(data) {" +
			"				callback(data);" +
			"			};" +
			"		try {" +
			"			gadgets.rpc.call(\"if_divEditor\", \"rscmd_getSettings\", callbackFunction);" +
//			"			gadgets.rpc.call(\"if_divEditor\", \"getSettings\", callbackFunction);" +
			"		} catch (err) {" +
			"		}" +
			"	}" +
			"</script>" +
			"";
	
	private WidgetSettingsInfo widgetSettings;
	
//	private Command onSave;
	
	public static WidgetCustomUIWidget getInstance() {
		try {
			if (instance == null) {
				instance = new WidgetCustomUIWidget();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}
	
	private WidgetCustomUIWidget() {

		GadgetCommandHelper.init();

		String htmlString = HTML_STRING.replace("%save%", getButtonString(CommandType.SAVE_COMMAND, "paramsArray"))
				.replace("%close%", getButtonString(CommandType.CLOSE_COMMAND));

		init(htmlString);
		
		registerJavaScriptCallbacks();

	}

//	public void show(Command onSave) {
//		show(onSave, null);
//	}
	
	public void show(Command onSave, WidgetSettingsInfo widgetSettings) {
//		this.onSave = onSave;
		this.widgetSettings = widgetSettings;
		
		String url = widgetSettings.getWidgetUiUrl();
		url += url.contains("?") ? "&" : "?";
		url += "up_id=" + "if_divEditor";
		url += "&parent=" + URL.encodeQueryString(Window.Location.getHref());
		url += "&up_rsW=" + PresentationLayoutWidget.getInstance().getPlaceholderWidth();
		url += "&up_rsH=" + PresentationLayoutWidget.getInstance().getPlaceholderHeight();
		url += "&up_companyId=" + SelectedCompanyController.getInstance().getSelectedCompanyId();
		
		url = url.replace("'", "\\'");

//		String params = widgetSettings.getAdditionalParams() != null ? widgetSettings.getAdditionalParams() : "";
//		params = params.replace("\"", "\\\"");
		
		show(url);
	}
	
//	public void save() {
//		//make an asynch call to the gadget to get the setting.
//		saveSettingsAsync(getElement());
//	}
	
	private static String onWidgetGetAdditionParamsStatic() {
		if (instance != null && instance.widgetSettings != null && instance.widgetSettings.getAdditionalParams() != null) {
			return instance.widgetSettings.getAdditionalParams();
		}
		
		return "";
	}

	private void onWidgetSave(String params, String additionalParams) {
		hide();

		if (widgetSettings != null) {
			widgetSettings.setParams(params);
			widgetSettings.setAdditionalParams(additionalParams);
			
//			System.out.println("onPickerCallback: fileUrl = " + fileObject);
//			parseUrl();
//			parseOnSaveResponse(params);
//			setGadgetUrl(buildUrl());
			
//			if (onSave != null) {
//				onSave.execute();
//			}
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

	// Set up the JS-callable signature as a global JS function.
	private native void registerJavaScriptCallbacks() /*-{
		$wnd.rdn2_rpc_widgetUI_widgetGetAdditionalParams = @com.risevision.ui.client.gadget.WidgetCustomUIWidget::onWidgetGetAdditionParamsStatic();	
	}-*/;

	
//	private native String saveSettingsAsync(Element gadgetFrame) /*-{
//		var res = "";
//	
//		var instance = this;
//		var callbackSave = function(data) {
////			debugger; 
//			
//			var params = null; 
//			var additionalParams = null;
//			if (data && data.params) {
//				params = data.params;
//				
//				if (data.additionalParams) {
//					additionalParams = data.additionalParams;
//				}
//			}
//			
//			instance.@com.risevision.ui.client.gadget.WidgetCustomUIWidget::onWidgetSave(Ljava/lang/String;Ljava/lang/String;)( params, additionalParams );
//		};
//	
////		if (window.parent.frames.gadgetCustomrUiFrame && window.parent.frames.gadgetCustomrUiFrame.getSettings) {
//		if (gadgetFrame.contentWindow && gadgetFrame.contentWindow.getSettings) {
//			res = gadgetFrame.contentWindow.getSettings(callbackSave);
//		}
//	
//		return res;
//	}-*/;
}