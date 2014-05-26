// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.mediaLibrary;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.PopupPanel;
import com.risevision.common.client.json.JSOModel;
import com.risevision.ui.client.common.utils.HtmlUtils;

public class GooglePickerWidget extends Frame {
	private static GooglePickerWidget instance;
	
	private static final String HTML_STRING = "<html>" +
			"<head>" +
			"<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">" +
			"<script type=\"text/javascript\" language=\"javascript\" src=\"/scripts/driveScripts.js\"></script>" +
			"<script type=\"text/javascript\">" +
			"	// Create and render a Picker object for searching images.\n" +
			"	function showPicker(viewId) {" +
			"		var requestedView = new google.picker.DocsView(viewId);" +
//			"		requestedView.setIncludeFolders(true);" +
			"" +
			"		var foldersView = new google.picker.DocsView(google.picker.ViewId.FOLDERS);" +
			"		foldersView.setIncludeFolders(true);" +
			"" +
			"		var uploadView = new google.picker.DocsUploadView();" +
			"		uploadView.setIncludeFolders(true);" +
			"" +
			"		var picker = new google.picker.PickerBuilder()" +
//			"		.enableFeature(google.picker.Feature.NAV_HIDDEN)" +
			"		.setAppId(\"726689182011\")" +
			"		.setOAuthToken(oauthToken)" +
			"		.addView(requestedView)" +
			"		.addView(foldersView)" +
			"		.addView(new google.picker.DocsView(google.picker.ViewId.RECENTLY_PICKED))" +
			"		.addView(uploadView)" +
			"		.setCallback(function(data) {" +
			"			if (data[google.picker.Response.ACTION] == google.picker.Action.PICKED ||" +
			"				data[google.picker.Response.ACTION] == google.picker.Action.CANCEL) {" +
			"				var doc;" +
			"" +
			"				if (data[google.picker.Response.ACTION] == google.picker.Action.PICKED) {" +
			"					doc = data[google.picker.Response.DOCUMENTS][0];" +
			"				}" +
			"" +
			"				pickerCallback(doc);" +
			"			}" +
			"		})" +
			"		.build();" +
			"" +
			"		picker.setVisible(true);" +
			"	}" +
			"	function pickerCallback(doc) {" +
//			"		var fileUrl;" +
//			"" +
//			"		if (doc != null) {" +
//			"			fileUrl = doc.url;" +
//			"		}" +
//			"		var doc," +
//			"		fileId," +
//			"		fileUrl;" +
//			"" +
//			"   	if (data[google.picker.Response.ACTION] == google.picker.Action.PICKED) {" +
//			"			doc = data[google.picker.Response.DOCUMENTS][0];" +
//			"			" +
//			"			fileId = doc.id;" +
//			"			fileUrl = doc.url;" +
//			"		}" +
			"		parent.rdn2_googlePicker_pickerCallback(doc);" +
			"	}" +	
			"	function pickerReady(authorized) {" +
			"		parent.rdn2_googlePicker_pickerReady(authorized);" +
			"	}" +
			"	function fileUrlCallback(fileUrl) {" +
			"		parent.rdn2_googlePicker_fileUrlCallback(fileUrl);" +
			"	}" +
			"</script>" +
			"</head>" +
			"<body style=\"margin:0px;\">" +
			"  <!-- The Google API Loader script. -->" +
			"  <script type=\"text/javascript\" src=\"https://apis.google.com/js/api.js?onload=onApiLoad\"></script>" + 
			"</body>" +
			"</html>" +
			"";
	
	private PopupPanel pickerPanel = new PopupPanel(false, false);
	
	private Command onSave;
	
	private static boolean isReady = false;
	private static boolean isAuthorized = false;
	private boolean requireFileUrl = false;
	private JavaScriptObject fileObject;
	private String fileUrl;
	private String fileExtension;

	public static GooglePickerWidget getInstance() {
		try {
			if (instance == null) {
				instance = new GooglePickerWidget();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}
	
	public GooglePickerWidget() {
//		frame.getElement().setId("gadgetCustomrUiFrame");
//		frame.getElement().setAttribute("name","gadgetCustomrUiFrame");

		pickerPanel.add(this);
		
		registerJavaScriptCallbacks();
		
		styleControls();
		
		// show panel to render iFrame
		pickerPanel.show();
		
		loadGooglePickerFrame();
	}

	private void styleControls() {
		setSize("100%", "100%");
		
		getElement().getStyle().setBorderWidth(0, Unit.PX);
		getElement().setAttribute("frameborder", "0");
		getElement().setAttribute("scrolling", "no");
		
		pickerPanel.removeStyleName("gwt-PopupPanel");
		pickerPanel.getElement().getStyle().setProperty("width", "100%");
		pickerPanel.getElement().getStyle().setProperty("height", "100%");
		pickerPanel.getElement().getStyle().setBackgroundColor("transparent");
		
		showPanel(false);
	}
	
	private void showPanel(boolean show) {
		pickerPanel.getElement().getStyle().setZIndex(show ? 1000 : -1000);
	}

	protected void onLoad() {
		super.onLoad();
	}
	
	private void loadGooglePickerFrame() {
		String htmlString = HTML_STRING;
		
//		htmlString = htmlString.replace("%s1%", params);
		
//		this.addLoadHandler(new LoadHandler() {
//			@Override
//			public void onLoad(LoadEvent event) {
//				
//			}
//		});
		
		HtmlUtils.writeHtml(getElement(), htmlString);	

	}
	
	private static void onPickerReady(boolean authorized) {
		isAuthorized = authorized;
		isReady = true;
	}

//	public void show(Command onSave) {
//		show(onSave, null);
//	}
	
	public void show(Command onSave, String type) {
		show(onSave, type, false);
	}
	
	public void show(Command onSave, String type, boolean requireFileUrl) {
		this.onSave = onSave;
		this.requireFileUrl = requireFileUrl;
		fileObject = null;
		fileUrl = null;

		if (!isReady) {
			return;
		}
		
		if (isAuthorized) {
			showPickerNative(getElement(), type);
		}
		else {
			authorizeUserNative(getElement(), type);
		}
		
		showPanel(true);
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
	
	public JavaScriptObject getFileObject() {
		return fileObject;
	}
	
	public String getFileUrl() {
		return fileUrl;
	}
	
	public String getFileExtension() {
		return fileExtension;
	}
	
	private String getFileId() {
		if (fileObject == null) {
			return null;
		}
		
		JSOModel fileJSOObject = (JSOModel) fileObject;
		return fileJSOObject.get("id");
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
	
	private static void onPickerCallbackStatic(JavaScriptObject fileObject) {
		instance.onPickerCallback(fileObject);
	}

	private void onPickerCallback(JavaScriptObject fileObject) {
		showPanel(false);

		if (fileObject != null) {			
//			System.out.println("onPickerCallback: fileUrl = " + fileObject);
//			parseUrl();
//			parseOnSaveResponse(params);
//			setGadgetUrl(buildUrl());
	//		hide();
			
			this.fileObject = fileObject;
			
			if (requireFileUrl) {
				getFileUrlNative(getElement(), getFileId());
			}
			else if (onSave != null) {
				onSave.execute();
			}
		}
	}
	
	private static void onFileUrlCallbackStatic(JavaScriptObject fileObject) {
		instance.onFileUrlCallback(fileObject);
	}

	private void onFileUrlCallback(JavaScriptObject fileObject) {
		if (fileObject != null) {
			JSOModel fileJSOObject = (JSOModel) fileObject;
			fileUrl = fileJSOObject.get("webContentLink");
			fileExtension = fileJSOObject.get("fileExtension");
		
			if (onSave != null)
				onSave.execute();
		}

	}

//	private static void onGooglePickerOpen(final JavaScriptObject callback, final String propertyId) {
//		Command onClose = new Command() {
//			@Override
//			public void execute() {
//				javascriptCallback(callback, propertyId, MediaLibraryWidget.getInstance().getItemUrl(), null);
//			}
//		};
//		MediaLibraryWidget.getInstance().show(onClose);
//	}
	
	// Set up the JS-callable signature as a global JS function.
	private native void registerJavaScriptCallbacks() /*-{
		$wnd.rdn2_googlePicker_pickerReady = @com.risevision.ui.client.common.widgets.mediaLibrary.GooglePickerWidget::onPickerReady(Z);
		$wnd.rdn2_googlePicker_pickerCallback = @com.risevision.ui.client.common.widgets.mediaLibrary.GooglePickerWidget::onPickerCallbackStatic(Lcom/google/gwt/core/client/JavaScriptObject;);
		$wnd.rdn2_googlePicker_fileUrlCallback = @com.risevision.ui.client.common.widgets.mediaLibrary.GooglePickerWidget::onFileUrlCallbackStatic(Lcom/google/gwt/core/client/JavaScriptObject;);
	}-*/;

	private native void showPickerNative(Element gadgetFrame, String type) /*-{
		if (gadgetFrame.contentWindow && gadgetFrame.contentWindow.showPicker) {
			res = gadgetFrame.contentWindow.showPicker(type);
		}
	}-*/;
	
	private native void authorizeUserNative(Element gadgetFrame, String type) /*-{
		if (gadgetFrame.contentWindow && gadgetFrame.contentWindow.showPicker) {
			res = gadgetFrame.contentWindow.checkAuth(type);
		}
	}-*/;
	
	private native void getFileUrlNative(Element gadgetFrame, String fileId) /*-{
		if (gadgetFrame.contentWindow && gadgetFrame.contentWindow.showPicker) {
			res = gadgetFrame.contentWindow.getFileUrl(fileId);
		}
	}-*/;
	
//	private native String saveSettingsAsync(Element gadgetFrame) /*-{
//		var res = "";
//
//		var instance = this;
//		var callbackSave = function(data) {
////			debugger; 
//			
//			var params = null; 
//			if (data && data.params) {
//				params = data.params;
//			}
//			
//			instance.@com.risevision.ui.client.common.widgets.mediaLibrary.GooglePickerWidget::saveResponse(Ljava/lang/String;)( params );
//		};
//
//		if (gadgetFrame.contentWindow && gadgetFrame.contentWindow.getSettings) {
//			res = gadgetFrame.contentWindow.getSettings(callbackSave);
//		}
//
//		return res;
//	}-*/;
	
//	private static native void javascriptCallback(JavaScriptObject callback, String propertyId, String value, String description) /*-{
//		debugger;
//		
//		if (callback) {
//			callback(propertyId, value, description);
//		}
//	
//	}-*/;
}