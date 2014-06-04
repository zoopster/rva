// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.mediaLibrary;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Command;
import com.google.gwt.dom.client.Element;
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
	private JavaScriptObject fileObject;

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
		this.onSave = onSave;
		fileObject = null;

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
	
	public JavaScriptObject getFileObject() {
		return fileObject;
	}
	
	private String getFileId() {
		if (fileObject == null) {
			return null;
		}
		
		JSOModel fileJSOObject = (JSOModel) fileObject;
		return fileJSOObject.get("id");
	}
	
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
			
			if (onSave != null) {
				onSave.execute();
			}
		}
	}
	
	// Set up the JS-callable signature as a global JS function.
	private native void registerJavaScriptCallbacks() /*-{
		$wnd.rdn2_googlePicker_pickerReady = @com.risevision.ui.client.common.widgets.mediaLibrary.GooglePickerWidget::onPickerReady(Z);
		$wnd.rdn2_googlePicker_pickerCallback = @com.risevision.ui.client.common.widgets.mediaLibrary.GooglePickerWidget::onPickerCallbackStatic(Lcom/google/gwt/core/client/JavaScriptObject;);
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
	
}