package com.risevision.ui.client.common.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.risevision.ui.client.common.exception.ServiceFailedException;
import com.risevision.ui.client.common.info.RpcResultInfo;
import com.risevision.ui.client.common.info.SocialConnectorAccessInfo;
import com.risevision.ui.client.common.info.SocialConnectorAccessInfo.NetworkType;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("socialConnector")
public interface SocialConnectorService extends RemoteService {
	// Access Token APIs
//	ArrayList<SocialConnectorAccessInfo> getAccessTokens(String companyId) throws ServiceFailedException;
	RpcResultInfo putAccessToken(String companyId, SocialConnectorAccessInfo accessInfo) throws ServiceFailedException;
	RpcResultInfo deleteAccessToken(String companyId, SocialConnectorAccessInfo accessInfo) throws ServiceFailedException;
	
	// Location Token APIs
	String getLocationToken(String companyId, String networkName) throws ServiceFailedException;
	RpcResultInfo putLocationToken(String companyId, String networkName, String locationToken) throws ServiceFailedException;

	// Display Token APIs
	HashMap<NetworkType, ArrayList<String>> getDisplayTokens(String companyId, String displayId) throws ServiceFailedException;
	RpcResultInfo putDisplayTokens(String companyId, String displayId, String networkName, String accessToken, String locationToken) throws ServiceFailedException;

}
