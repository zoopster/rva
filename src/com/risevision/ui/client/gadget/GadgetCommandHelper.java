package com.risevision.ui.client.gadget;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Command;
import com.risevision.ui.client.common.widgets.colorPicker.ColorPickerDialog;
import com.risevision.ui.client.common.widgets.financial.InstrumentListWidget;
import com.risevision.ui.client.common.widgets.mediaLibrary.GooglePickerWidget;
import com.risevision.ui.client.common.widgets.mediaLibrary.StorageAppWidget;
import com.risevision.ui.client.common.widgets.textStyle.TextStyleDialog;

public class GadgetCommandHelper {
	private static GadgetCommandHelper instance;
	
	public static final String HTML_STRING = "" +
			"" +
			"<script type=\"text/javascript\">" +
			"	var callbackFunction;" +
			"	function openColorPicker(propertyId, color) {\n" +
			"		var colorPickerCallback = function(propertyId, newColor) {\n" +
			"			gadgets.rpc.call(\"if_divEditor\", \"rscmd_colorPickerCallback\", null, propertyId, newColor);\n" +
			"		};\n" +
			"		parent.rdn2_rpc_customUI_colorPickerOpen(colorPickerCallback, propertyId, color);\n" +
			"	}\n" +
			"	function openFontSelector(propertyId, font) {" +
			"		var fontSelectorCallback = function(propertyId, newFont, fontDescription) {" +
			"			gadgets.rpc.call(\"if_divEditor\", \"rscmd_fontSelectorCallback\", null, propertyId, newFont, fontDescription);" +
			"		};" +
			"		parent.rdn2_rpc_customUI_fontSelectorOpen(fontSelectorCallback, propertyId, font);" +
			"	}" +	
			"	function openFinancialSelector(propertyId, stockList) {" +
			"		var financialSelectorCallback = function(propertyId, newStockList) {" +
			"			gadgets.rpc.call(\"if_divEditor\", \"rscmd_financialSelectorCallback\", null, propertyId, newStockList);" +
			"		};" +
			"		parent.rdn2_rpc_customUI_financialSelectorOpen(financialSelectorCallback, propertyId, stockList);" +
			"	}" +	
			"	function openMediaLibrary(propertyId) {" +
			"		var mediaLibraryCallback = function(propertyId, newUrl) {" +
			"			gadgets.rpc.call(\"if_divEditor\", \"rscmd_mediaLibraryCallback\", null, propertyId, newUrl);" +
			"		};" +
			"		parent.rdn2_rpc_customUI_mediaLibraryOpen(mediaLibraryCallback, propertyId);" +
			"	}" +	
			"	function openGooglePicker(propertyId, viewId) {" +
			"		var googlePickerCallback = function(propertyId, newUrl) {" +
			"			gadgets.rpc.call(\"if_divEditor\", \"rscmd_googlePickerCallback\", null, propertyId, newUrl);" +
			"		};" +
			"		parent.rdn2_rpc_customUI_googlePickerOpen(googlePickerCallback, propertyId, viewId);" +
			"	}" +
			"	function initCallbacks() {" +
			"		gadgets.rpc.register(\"rscmd_openColorPicker\", openColorPicker);" +
			"		gadgets.rpc.register(\"rscmd_openFontSelector\", openFontSelector);" +
			"		gadgets.rpc.register(\"rscmd_openFinancialSelector\", openFinancialSelector);" +
			"		gadgets.rpc.register(\"rscmd_openMediaLibrary\", openMediaLibrary);" +
			"		gadgets.rpc.register(\"rscmd_openGooglePicker\", openGooglePicker);" +
			"	}" +
			"</script>" +
			"";
	
	private GadgetCommandHelper() {
		registerJavaScriptCallbacks();
	}
	
	public static void init() {
		if (instance == null) {
			instance = new GadgetCommandHelper();
		}
	}

	private static void onColorPickerOpen(final JavaScriptObject callback, final String propertyId, String color) {
		Command onClose = new Command() {
			@Override
			public void execute() {
				javascriptCallback(callback, propertyId, ColorPickerDialog.getInstance().getColor(), null);
			}
		};
		ColorPickerDialog.getInstance().show(onClose, color);
	}
	
	private static void onFontSelectorOpen(final JavaScriptObject callback, final String propertyId, String font) {
		Command onClose = new Command() {
			@Override
			public void execute() {
				javascriptCallback(callback, propertyId, TextStyleDialog.getInstance().getStyle(), TextStyleDialog.getInstance().getLabelText());
			}
		};
		TextStyleDialog.getInstance().show(onClose, font, propertyId);
	}
	
	private static void onFinancialSelectorOpen(final JavaScriptObject callback, final String propertyId, String instruments) {
		Command onClose = new Command() {
			@Override
			public void execute() {
				javascriptCallback(callback, propertyId, InstrumentListWidget.getInstance().getItemsList(), null);
			}
		};
		InstrumentListWidget.getInstance().show(onClose, instruments);
	}
	
	private static void onMediaLibraryOpen(final JavaScriptObject callback, final String propertyId) {
		Command onClose = new Command() {
			@Override
			public void execute() {
				javascriptCallback(callback, propertyId, StorageAppWidget.getInstance().getItemUrl(), null);
			}
		};
		StorageAppWidget.getInstance().show(onClose);
	}
	
	private static void onGooglePickerOpen(final JavaScriptObject callback, final String propertyId, String viewId) {
		Command onClose = new Command() {
			@Override
			public void execute() {
				javascriptCallback(callback, propertyId, GooglePickerWidget.getInstance().getFileObject(), null);
			}
		};
		GooglePickerWidget.getInstance().show(onClose, viewId);
	}
	
	// Set up the JS-callable signature as a global JS function.
	private native void registerJavaScriptCallbacks() /*-{
		$wnd.rdn2_rpc_customUI_colorPickerOpen = @com.risevision.ui.client.gadget.GadgetCommandHelper::onColorPickerOpen(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;Ljava/lang/String;)
		$wnd.rdn2_rpc_customUI_fontSelectorOpen = @com.risevision.ui.client.gadget.GadgetCommandHelper::onFontSelectorOpen(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;Ljava/lang/String;)
		
		$wnd.rdn2_rpc_customUI_financialSelectorOpen = @com.risevision.ui.client.gadget.GadgetCommandHelper::onFinancialSelectorOpen(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;Ljava/lang/String;)

		$wnd.rdn2_rpc_customUI_mediaLibraryOpen = @com.risevision.ui.client.gadget.GadgetCommandHelper::onMediaLibraryOpen(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)
		$wnd.rdn2_rpc_customUI_googlePickerOpen = @com.risevision.ui.client.gadget.GadgetCommandHelper::onGooglePickerOpen(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;Ljava/lang/String;)

	}-*/;

	private static native void javascriptCallback(JavaScriptObject callback, String propertyId, JavaScriptObject value, String description) /*-{
		debugger;
		
		if (callback) {
			callback(propertyId, value, description);
		}
	
	}-*/;
	
	private static native void javascriptCallback(JavaScriptObject callback, String propertyId, String value, String description) /*-{
		debugger;
		
		if (callback) {
			callback(propertyId, value, description);
		}
	
	}-*/;
}