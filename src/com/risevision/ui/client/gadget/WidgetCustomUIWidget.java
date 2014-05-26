// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.gadget;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.PopupPanel;
import com.risevision.ui.client.common.info.WidgetSettingsInfo;
import com.risevision.ui.client.common.utils.HtmlUtils;
import com.risevision.ui.client.presentation.PresentationLayoutWidget;

public class WidgetCustomUIWidget extends Frame {
	private static WidgetCustomUIWidget instance;
	
	private static final String HTML_STRING = "<html>" +
			"<head>" +
			"<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">" +
//			"<script type=\"text/javascript\" language=\"javascript\" src=\"http://ig.gmodules.com/gadgets/js/rpc.js\"></script>" +
//			"<script type=\"text/javascript\" language=\"javascript\" src=\"/gadgets/globals.js\"></script>" +
//			"<script type=\"text/javascript\" language=\"javascript\" src=\"/gadgets/urlparams.js\"></script>" +
//			"<script type=\"text/javascript\" language=\"javascript\" src=\"/gadgets/config.js\"></script>" +
//			"<script type=\"text/javascript\" language=\"javascript\" src=\"/gadgets/wpm.transport.js\"></script>" +
//			"<script type=\"text/javascript\" language=\"javascript\" src=\"/gadgets/rpc.js\"></script>" +
			"<script type=\"text/javascript\" language=\"javascript\" src=\"/gadgets/gadgets.min.js\"></script>" +
			"" +
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
			"			parent.rdn2_googlePicker_widgetSave(params, additionalParams);" +
			"		}" +
			"	}" +
			"	function closeSettings() {" +
			"		parent.rdn2_googlePicker_widgetClose();" +
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
			"</head>" +
			"<body style=\"margin:0px;\">" +
			"	<div id=\"divEditor\" name=\"divEditor\" style=\"width: 100%; height: 100%;\">" +
			"		<iframe id=\"if_divEditor\" name=\"if_divEditor\" allowTransparency=\"true\" " +
			"			style=\"display:block;position:absolute;height:100%;width:100%;\" " +
			"			frameborder=0 scrolling=\"no\" src=\"%url%\">" +
			"		</iframe>" +
			"	</div>" +
			"</body>" +
			"</html>" +
			"";
	
	private PopupPanel containerPanel = new PopupPanel(false, false);
	
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
	
	public WidgetCustomUIWidget() {
//		frame.getElement().setId("gadgetCustomrUiFrame");
//		frame.getElement().setAttribute("name","gadgetCustomrUiFrame");

		containerPanel.add(this);
		
		GadgetCommandHelper.init();
		registerJavaScriptCallbacks();
		
		styleControls();
		
	}

	private void styleControls() {
		setSize("100%", "100%");
		
		getElement().getStyle().setBorderWidth(0, Unit.PX);
		getElement().setAttribute("frameborder", "0");
		getElement().setAttribute("scrolling", "no");
		
		containerPanel.removeStyleName("gwt-PopupPanel");
		containerPanel.getElement().getStyle().setProperty("width", "100%");
		containerPanel.getElement().getStyle().setProperty("height", "100%");
		containerPanel.getElement().getStyle().setBackgroundColor("transparent");
		
		showPanel(false);
	}
	
	private void showPanel(boolean show) {
		containerPanel.getElement().getStyle().setZIndex(show ? 1000 : -1000);
		containerPanel.getElement().getStyle().setOpacity(show ? 1 : 0);
		
		if (show) containerPanel.show();
		else containerPanel.hide();
	}

	protected void onLoad() {
		super.onLoad();
	}
	
	private static void onPickerReady() {

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
		
		url = url.replace("'", "\\'");

		String params = widgetSettings.getAdditionalParams() != null ? widgetSettings.getAdditionalParams() : "";
		params = params.replace("\"", "\\\"");
		
		final String htmlString = HTML_STRING.replace("%url%", url)
								.replace("%s1%", params);
		
//		this.addLoadHandler(new LoadHandler() {
//			@Override
//			public void onLoad(LoadEvent event) {
//			}
//		});
		
		showPanel(true);
		
		HtmlUtils.writeHtml(getElement(), htmlString);	
	}
	
	public void hide() {
		showPanel(false);
	}
	
//	public void save() {
//		//make an asynch call to the gadget to get the setting.
//		saveSettingsAsync(getElement());
//	}
	
//	private void doActionCancel() {
//		setVisible(false);
//	}
	
//		try {
//			String xmlUrl = getGadgetXmlUrl();
//			String[] ray = getGadgetUrl().substring(xmlUrl.length()+1).split("&");
//			urlParams.clear();
//			for (int i = 0; i < ray.length; i++) {
//				String[] substrRay = ray[i].split("=");
//				if (substrRay.length == 2)
//					urlParams.put(substrRay[0], substrRay[1]);
//				else if (substrRay.length == 1)
//					urlParams.put(substrRay[0], "");
//			}
//		} catch(Exception ex) {}
//	}
	
//	private String buildUrl() {
//		String params = "";
//				
//		for (Map.Entry<String, String> obj: urlParams.entrySet()) {
//			params += "&" + obj.getKey() + "=" + obj.getValue();
//		}
//
//		return getGadgetXmlUrl() + params;
//	}

//	private void parseOnSaveResponse(String params) {
//		try {
//			if ((params != null) && (params.length() > 1)) {
//				String[] ray = params.split("&");
//				urlParams.clear();
//				for (int i = 0; i < ray.length; i++) {
//					if (ray[i].length() > 1) {
//						String[] substrRay = ray[i].split("=");
//						if (substrRay.length == 2)
//							urlParams.put(substrRay[0], substrRay[1]);
//						else if (substrRay.length == 1)
//							urlParams.put(substrRay[0], "");
//					}
//				}
//			}
//		} catch (Exception ex) {
//		}
//	}
	
//	public void init(String gadgetUrl, String additionalParams) {
//		setGadgetUrl(gadgetUrl);
//
//		this.additionalParams = additionalParams;
//	}
	
//	public String getGadgetXmlUrl() {
//		//handle improperly url that have "?"
//		String res = getGadgetUrl().replaceFirst("\\?", "&");
//		//get the gadget URL part
//		int queryParamsStartPos = res.indexOf('&');
//		if (queryParamsStartPos > 0)
//			res = getGadgetUrl().substring(0, queryParamsStartPos);
//		return res;
//	}

	// Expose the following method into JavaScript.
//	private static void onSaveCallback(String params) {
//		System.out.println("onSave: params = " + params);
//		instance.saveAndClose(params);
//	}

	// Expose the following method into JavaScript.
//	private static String onGetGadgetUrl() {
//		return instance.gadgetUrl;
//	}
	
	private static String onWidgetGetAdditionParamsStatic() {
		if (instance != null && instance.widgetSettings != null && instance.widgetSettings.getAdditionalParams() != null) {
			return instance.widgetSettings.getAdditionalParams();
		}
		
		return "";
	}
	
	private static void onWidgetSaveStatic(String params, String additionalParams) {
		instance.onWidgetSave(params, additionalParams);
	}

	private void onWidgetSave(String params, String additionalParams) {
		showPanel(false);

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
	
	private static void onWidgetCloseStatic() {
		instance.showPanel(false);
	}
	
	// Set up the JS-callable signature as a global JS function.
	private native void registerJavaScriptCallbacks() /*-{
		$wnd.rdn2_rpc_widgetUI_widgetGetAdditionalParams = @com.risevision.ui.client.gadget.WidgetCustomUIWidget::onWidgetGetAdditionParamsStatic();
	
//		$wnd.rdn2_rpc_widgetUI_widgetReady = @com.risevision.ui.client.gadget.WidgetCustomUiWidget::onWidgetReady();
		$wnd.rdn2_googlePicker_widgetSave = @com.risevision.ui.client.gadget.WidgetCustomUIWidget::onWidgetSaveStatic(Ljava/lang/String;Ljava/lang/String;);
		$wnd.rdn2_googlePicker_widgetClose = @com.risevision.ui.client.gadget.WidgetCustomUIWidget::onWidgetCloseStatic();
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