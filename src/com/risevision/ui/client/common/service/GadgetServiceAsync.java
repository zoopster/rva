package com.risevision.ui.client.common.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.risevision.ui.client.common.info.GadgetInfo;
import com.risevision.ui.client.common.info.RpcResultInfo;

public interface GadgetServiceAsync {
	//Gadget API
//	void getGadgets(GadgetsInfo gadgetsInfo, AsyncCallback<GadgetsInfo> callback);
//	void getSharedGadgets(GadgetsInfo gadgetsInfo, AsyncCallback<GadgetsInfo> callback);
	void getGadget(String companyId, String gadgetId, AsyncCallback<GadgetInfo> callback);
	void getGadget(String gadgetId, AsyncCallback<GadgetInfo> callback);
	void putGadget(String companyId, GadgetInfo gadget, AsyncCallback<RpcResultInfo> callback);
	void deleteGadget(String companyId, String gadgetId, AsyncCallback<RpcResultInfo> callback);

	void getGadgetXml(String gadgetUrl, AsyncCallback<String> callback);
}
