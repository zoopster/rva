package com.risevision.ui.client.common.widgets.mediaLibrary;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.PopupPanel;
import com.risevision.ui.client.common.info.WidgetSettingsInfo;
import com.risevision.ui.client.common.utils.HtmlUtils;
import com.risevision.ui.client.gadget.GadgetCommandHelper;

public class StorageFrameWidget extends Frame {
	private static StorageFrameWidget instance;
	
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
			"			parent.rdn2_storage_widgetSave(params, additionalParams);" +
			"		}" +
			"	}" +
			"	function closeSettings() {" +
			"		parent.rdn2_storage_widgetClose();" +
			"	}" +
//			"	function editorFrameReady() {" +
//			"		parent.rdn2_rpc_customUI_editorFrameReady();" +
//			"	}" +
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
		this.onSave = onSave;
		this.widgetSettings = widgetSettings;
		
		String url = widgetSettings.getWidgetUrl();
		url += url.contains("?") ? "&" : "?";
		url += "up_id=" + "if_divEditor";
		url += "&parent=" + URL.encodeQueryString(Window.Location.getHref());
		
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
	
	private static void onWidgetSaveStatic(String params, String additionalParams) {
		instance.onWidgetSave(params, additionalParams);
	}

	private void onWidgetSave(String params, String additionalParams) {
		showPanel(false);

		if (widgetSettings != null) {
			widgetSettings.setParams(params);

			if (onSave != null) {
				onSave.execute();
			}
		}
	}
	
	private static void onWidgetCloseStatic() {
		instance.showPanel(false);
	}
	
	// Set up the JS-callable signature as a global JS function.
	private native void registerJavaScriptCallbacks() /*-{	
//		$wnd.rdn2_rpc_widgetUI_widgetReady = @com.risevision.ui.client.common.widgets.mediaLibrary.MediaLibraryFrameWidget::onWidgetReady();
		$wnd.rdn2_storage_widgetSave = @com.risevision.ui.client.common.widgets.mediaLibrary.StorageFrameWidget::onWidgetSaveStatic(Ljava/lang/String;Ljava/lang/String;);
		$wnd.rdn2_storage_widgetClose = @com.risevision.ui.client.common.widgets.mediaLibrary.StorageFrameWidget::onWidgetCloseStatic();
	}-*/;

}