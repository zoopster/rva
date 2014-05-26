// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.risevision.ui.client.common.info.RpcResultInfo;
import com.risevision.ui.client.common.info.SocialConnectorAccessInfo;
import com.risevision.ui.client.common.info.SocialConnectorAccessInfo.NetworkType;

public interface SocialConnectorServiceAsync {
	// Access Token APIs
//	void getAccessTokens(String companyId, AsyncCallback<ArrayList<SocialConnectorAccessInfo>> callback);
	void putAccessToken(String companyId, SocialConnectorAccessInfo accessInfo, AsyncCallback<RpcResultInfo> callback);
	void deleteAccessToken(String companyId, SocialConnectorAccessInfo accessInfo, AsyncCallback<RpcResultInfo> callback);
	
	// Location Token APIs
	void getLocationToken(String companyId, String networkName, AsyncCallback<String> callback);
	void putLocationToken(String companyId, String networkName, String locationToken, AsyncCallback<RpcResultInfo> callback);

	// Display Token APIs
	void getDisplayTokens(String companyId, String displayId, AsyncCallback<HashMap<NetworkType, ArrayList<String>>> callback);
	void putDisplayTokens(String companyId, String displayId, String networkName, String accessToken, String locationToken, AsyncCallback<RpcResultInfo> callback);

}
