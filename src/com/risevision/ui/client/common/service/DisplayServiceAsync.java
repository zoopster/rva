// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.risevision.ui.client.common.info.DisplayInfo;
import com.risevision.ui.client.common.info.RpcResultInfo;

public interface DisplayServiceAsync {
	//Display API
//	void getDisplays(DisplaysInfo displaysInfo, boolean activeOnly, AsyncCallback<DisplaysInfo> callback);
	void getDisplay(String companyId, String displayId, AsyncCallback<DisplayInfo> callback);
	void putDisplay(String companyId, DisplayInfo display, AsyncCallback<RpcResultInfo> callback);
	void deleteDisplay(String companyId, String displayId, AsyncCallback<RpcResultInfo> callback);
	void restartPlayer(String companyId, String displayId, AsyncCallback<RpcResultInfo> callback);
	void rebootPlayer(String companyId, String displayId, AsyncCallback<RpcResultInfo> callback);
	void putBrowserUpgradeMode(String companyId, int browserUpgradeMode, String osVersion, AsyncCallback<RpcResultInfo> callback);

	
//	void getDisplaysStatus(String companyId, String displayIds, AsyncCallback<ArrayList<DisplayStatusInfo>> callback);
}
