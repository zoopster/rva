// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.risevision.ui.client.common.exception.ServiceFailedException;
import com.risevision.ui.client.common.info.DisplayInfo;
import com.risevision.ui.client.common.info.RpcResultInfo;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("display")
public interface DisplayService extends RemoteService {
	//Display API
//	DisplaysInfo getDisplays(DisplaysInfo displaysInfo, boolean activeOnly) throws ServiceFailedException;
	DisplayInfo getDisplay(String companyId, String displayId) throws ServiceFailedException;
	RpcResultInfo putDisplay(String companyId, DisplayInfo display) throws ServiceFailedException;
	RpcResultInfo deleteDisplay(String companyId, String displayId) throws ServiceFailedException;
	RpcResultInfo restartPlayer(String companyId, String displayId) throws ServiceFailedException;
	RpcResultInfo rebootPlayer(String companyId, String displayId) throws ServiceFailedException;
	RpcResultInfo putBrowserUpgradeMode(String companyId, int browserUpgradeMode, String osVersion) throws ServiceFailedException;


//	ArrayList<DisplayStatusInfo> getDisplaysStatus(String companyId, String displayIds) throws ServiceFailedException;
}
