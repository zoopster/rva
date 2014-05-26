// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.gadget;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Frame;
import com.risevision.common.client.info.GadgetSettingsInfo;
import com.risevision.ui.client.common.utils.HtmlUtils;

public class GadgetCustomUIWidget extends Frame {
//	private static GadgetCustomUIWidget instance;
	
//	private static final String GADGET_SERVER_URL = "http://www-open-opensocial.googleusercontent.com/gadgets/ifr";
	
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
			"		return \"%s1%\";" +
			"	}" +
			"	function saveSettings(data) {" +
			"		if (callbackFunction) {" +
			"			callbackFunction(data);" +
			"		}" +
			"	}" +
			"	function editorFrameReady() {" +
			"		parent.rdn2_rpc_customUI_editorFrameReady();" +
			"	}" +
			"	function loadEditor(html) {" +
			"		addWidget('if_divEditor', html);" +
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
			"" +
			"		initCallbacks();" +
			"	}" +
			"	function addWidget(frameName, html) {" +
			"   	var myFrame = document.getElementById(frameName);" +
			"" +
			"		var myFrameObj = (myFrame.contentWindow) ? myFrame.contentWindow : (myFrame.contentDocument.document) ? myFrame.contentDocument.document : myFrame.contentDocument;" +
			"		myFrameObj.document.open();" +
			"		myFrameObj.document.write(html);" +
			"		myFrameObj.document.close();" +
			"" +
			"		gadgets.rpc.setupReceiver(frameName);" +
			"	}" +
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
			"			frameborder=0 scrolling=\"no\">" +
			"		</iframe>" +
			"	</div>" +
			"</body>" +
			"</html>" +
			"";
	
//	private String gadgetUrl;
	private GadgetSettingsInfo gadgetSettings;
//	private String additionalParams;
//	private HashMap<String, String> urlParams = new HashMap<String, String>();

	private Command onSave;

	public GadgetCustomUIWidget(Command onSave) {
		this.onSave = onSave;
		
//		instance = this;
//		frame.getElement().setId("gadgetCustomrUiFrame");
//		frame.getElement().setAttribute("name","gadgetCustomrUiFrame");

		GadgetCommandHelper.init();
		
		styleControls();
	}

	private void styleControls() {
		setSize("600px", "400px");
		
		getElement().getStyle().setBorderWidth(0, Unit.PX);
		getElement().setAttribute("frameborder", "0");
		getElement().setAttribute("scrolling", "no");
	}

	protected void onLoad() {
		super.onLoad();
	}

//	public void show(String gadgetUrl) {
	public void show(GadgetSettingsInfo gadgetSettings) {
		this.gadgetSettings = gadgetSettings;
//		this.setGadgetUrl(gadgetUrl);	

		loadCustomUIFrame();
	}
	
	private void loadCustomUIFrame() {
		String htmlString = HTML_STRING;
		
//		String url = GADGET_SERVER_URL + "?url=" + gadgetUrl + "&up_id=divEditor&view=editor";
//		url += "&source=" + URL.encodeQueryString(Window.Location.getHref()) +
//				"&parent=" + URL.encodeQueryString(Window.Location.getHref()) +
////				"&container=open&view=home&lang=all&country=ALL&debug=0&nocache=0&sanitize=0" +
//				"&libs=core%3Acore.io#st=%25st%25" +
//				"";
		
		htmlString = htmlString
//				.replace("%url%", url)
				.replace("\n", "")
				.replace("\r", "");
		
		String params = gadgetSettings.getAdditionalParams() != null ? gadgetSettings.getAdditionalParams() : "";
		params = params.replace("\"", "\\\"");
		htmlString = htmlString.replace("%s1%", params);
		
		this.addLoadHandler(new LoadHandler() {
			@Override
			public void onLoad(LoadEvent event) {
				loadEditorNative(getElement(), gadgetSettings.getGadgetHtml(true));				
			}
		});
		
		HtmlUtils.writeHtml(getElement(), htmlString);	
//		getElement().setInnerHTML(htmlString);
		
//		frame.setUrl("/GadgetCustomUI.html");
		//frame.setUrl("/GadgetCustomUI.html?url="+gadgetUrl);
	}
	
	public void save() {
//		String id = frame.getElement().getId();
		//make an asynch call to the gadget to get the setting.
		saveSettingsAsync(getElement());
	}
	
	private void doActionCancel() {
		setVisible(false);
	}
	
//	private void parseUrl() {
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
	
//	private void setGadgetUrl(String gadgetUrl) {
//		if (gadgetUrl != null)
//			gadgetUrl = gadgetUrl.trim();
//		this.gadgetUrl = gadgetUrl;
//	}

	public String getGadgetUrl() {
		return gadgetSettings != null ? gadgetSettings.getGadgetUrl() : "";
	}
	
	public String getAdditionalParams() {
		return gadgetSettings.getAdditionalParams();
	}

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

	private void saveResponse(String params, String additionalParams) {
		if (params != null) {
			System.out.println("onSave: params = " + params + "\nadditionalParams = " + additionalParams);
			gadgetSettings.updateGadgetSettings(params);
//			parseUrl();
//			parseOnSaveResponse(params);
//			setGadgetUrl(buildUrl());
	//		hide();
			
			gadgetSettings.setAdditionalParams(additionalParams);
	
			if (onSave != null)
				onSave.execute();
		}
	}

	// Set up the JS-callable signature as a global JS function.
//	private native void registerJavaScriptCallbacks() /*-{
//
//		$wnd.rdn2_rpc_customUI_save = @com.risevision.ui.client.gadget.GadgetCustomUIWidget::onSaveCallback(Ljava/lang/String;);
//		$wnd.rdn2_rpc_customUI_getGadgetUrl = @com.risevision.ui.client.gadget.GadgetCustomUIWidget::onGetGadgetUrl();
//	}-*/;

	private native void loadEditorNative(Element gadgetFrame, String html) /*-{
		if (gadgetFrame.contentWindow && gadgetFrame.contentWindow.loadEditor) {
			res = gadgetFrame.contentWindow.loadEditor(html);
		}
	}-*/;
		
	
	private native String saveSettingsAsync(Element gadgetFrame) /*-{
		var res = "";

		var instance = this;
		var callbackSave = function(data) {
//			debugger; 
			
			var params = null; 
			var additionalParams = null;
			if (data && data.params) {
				params = data.params;
				
				if (data.additionalParams) {
					additionalParams = data.additionalParams;
				}
			}
			
			instance.@com.risevision.ui.client.gadget.GadgetCustomUIWidget::saveResponse(Ljava/lang/String;Ljava/lang/String;)( params, additionalParams );
		};

//		if (window.parent.frames.gadgetCustomrUiFrame && window.parent.frames.gadgetCustomrUiFrame.getSettings) {
		if (gadgetFrame.contentWindow && gadgetFrame.contentWindow.getSettings) {
			res = gadgetFrame.contentWindow.getSettings(callbackSave);
		}

		return res;
	}-*/;
	
}