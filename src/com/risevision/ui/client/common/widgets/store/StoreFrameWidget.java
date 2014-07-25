// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.store;

import java.util.List;

import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.risevision.ui.client.common.controller.ConfigurationController;
import com.risevision.ui.client.common.controller.SelectedCompanyController;
import com.risevision.ui.client.common.data.GadgetDataController;
import com.risevision.ui.client.common.info.GadgetInfo;
import com.risevision.ui.client.common.info.GadgetsInfo;
import com.risevision.ui.client.common.widgets.StatusBoxWidget;
import com.risevision.ui.client.common.widgets.iframe.CommandType;
import com.risevision.ui.client.common.widgets.iframe.RpcDialogBoxWidget;
import com.risevision.ui.client.gadget.GadgetCommandHelper;

public class StoreFrameWidget extends RpcDialogBoxWidget {
	private static StoreFrameWidget instance;
	
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

	private Command onSave;
	private Command onCancel;
	
	private GadgetInfo selectedGadget = null;
	private StatusBoxWidget statusBox = StatusBoxWidget.getInstance();
	
	public static StoreFrameWidget getInstance() {
		try {
			if (instance == null) {
				instance = new StoreFrameWidget();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}
	
	public StoreFrameWidget() {
		super();

		GadgetCommandHelper.init();
		
		String htmlString = HTML_STRING.replace("%save%", getButtonString(CommandType.SAVE_COMMAND, "paramsArray"))
										.replace("%close%", getButtonString(CommandType.CLOSE_COMMAND));
		
		init(htmlString);
		
	}

	public void show(String storePath, Command onSave, Command onCancel){
		this.onSave = onSave;
		this.onCancel = onCancel;
		
		String url = ConfigurationController.getInstance().getConfiguration().getStoreURL();
		url += "?up_id=" + "if_divEditor";
		url += "&parent=" + URL.encodeQueryString(Window.Location.getHref());
		url += storePath;
		url += "?inRVA&cid=" + SelectedCompanyController.getInstance().getSelectedCompanyId();

		this.getElement().getStyle().clearBackgroundColor();
		
		this.getElement().addClassName("responsive-iframe");

		show(url);
	}

	private void onWidgetSave(GadgetInfo gadget) {
		show("about:blank");
		hide();
		
		selectedGadget = gadget;

		if (onSave != null) {
			onSave.execute();
		}
	}

	private void onWidgetCancel() {
		show("about:blank");
		hide();
		
		if (onCancel!= null) {
			onCancel.execute();
		}
	}


	@Override
	public void onMessage(String command, List<String> values) {

		if (command.equals(CommandType.SAVE_COMMAND)) {
			final String productCode = values.get(0);
			GadgetsInfo gadgetsInfo = new GadgetsInfo();
			gadgetsInfo.setProductCode(productCode);
			String companyId = SelectedCompanyController.getInstance().getSelectedCompanyId();
			gadgetsInfo.setCompanyId(companyId);
			boolean shared = !ConfigurationController.getInstance().isRiseCompanyId(companyId);
			GadgetDataController.getInstance().getGadgets(gadgetsInfo, shared, new AsyncCallback<GadgetsInfo>() {
				
				@Override
				public void onSuccess(GadgetsInfo result) {
					if (result.getGadgets().size() > 0) {
						GadgetInfo gadget = result.getGadgets().get(0);
						onWidgetSave(gadget);
					}
					else {
						StoreFrameWidget.this.onFailure("No Gadget associated with productCode: " + productCode);
						onWidgetCancel();
						hide();
					}
				}
				
				@Override
				public void onFailure(Throwable caught) {
					StoreFrameWidget.this.onFailure(StatusBoxWidget.RPC_ERROR);
					onWidgetCancel();
					hide();
				}
			});
		}
		else if (command.equals(CommandType.CLOSE_COMMAND)) {
			onWidgetCancel();
			hide();
		}		
		
	}

	protected void onFailure(String message) {
		statusBox.setStatus(StatusBoxWidget.Status.ERROR, message);
	}

	public GadgetInfo getSelectedGadget() {
		return selectedGadget;
	}

	public void setSelectedGadget(GadgetInfo selectedGadget) {
		this.selectedGadget = selectedGadget;
	}

}