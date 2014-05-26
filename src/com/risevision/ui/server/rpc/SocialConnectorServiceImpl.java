// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.server.rpc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.risevision.common.client.utils.RiseUtils;
import com.risevision.core.api.Entity;
import com.risevision.core.api.social.attributes.AccessTokenAttribute;
import com.risevision.core.api.social.attributes.SocialConnectorAttribute;
import com.risevision.ui.client.common.exception.ServiceFailedException;
import com.risevision.ui.client.common.info.RpcResultInfo;
import com.risevision.ui.client.common.info.SocialConnectorAccessInfo;
import com.risevision.ui.client.common.info.SocialConnectorAccessInfo.NetworkType;
import com.risevision.ui.client.common.service.SocialConnectorService;
import com.risevision.ui.server.RiseRemoteServiceServlet;
import com.risevision.ui.server.utils.ServerUtils;

import org.restlet.data.Form;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class SocialConnectorServiceImpl extends RiseRemoteServiceServlet implements SocialConnectorService {

//	public ArrayList<SocialConnectorAccessInfo> getAccessTokens(String companyId) throws ServiceFailedException {
//		String url = createAccessTokensResource(companyId);
//		Document d;
//
//		d = get(url);
//
//		if (d != null) {
//			return parseGetAccessTokens(d);
//		}
//
//		return null;
//	}

//	public SocialConnectorAccessInfo getAccessToken(String companyId,  SocialConnectorAccessInfo accessInfo) throws ServiceFailedException {
//		String url = createAccessTokenResource(companyId, accessInfo.getId(), accessInfo.getSocialNetworkName());
//
//		Document d = get(url);
//		SocialConnectorAccessInfo accessToken = null;
//		if (d != null)
//			accessToken = DocToAccessToken(d);
//		return accessToken;		
//	}
	
	public RpcResultInfo putAccessToken(String companyId,  SocialConnectorAccessInfo accessToken) throws ServiceFailedException {
		// new token - generate Id
		if ((accessToken.getId() == null) || (accessToken.getId().isEmpty()))
			accessToken.setId(UUID.randomUUID().toString());

		String url = createAccessTokenResource(companyId, accessToken.getNetworkType().toString(), accessToken.getId());
		
		Form form = new Form();

		form.add(AccessTokenAttribute.NAME, accessToken.getName());
		form.add(AccessTokenAttribute.VALUE, accessToken.getValue());
		form.add(AccessTokenAttribute.DEFAULT, ServerUtils.BooleanToStr(accessToken.isDefault()));
		
		put(url, form);

		return new RpcResultInfo(accessToken.getId());
	}

	public RpcResultInfo deleteAccessToken(String companyId,  SocialConnectorAccessInfo accessInfo) throws ServiceFailedException {
		// new user- generate userId
		if ((accessInfo == null) || (RiseUtils.strIsNullOrEmpty(accessInfo.getId())))
			return new RpcResultInfo("null","Access Token ID is null or empty");

		String url = createAccessTokenResource(companyId, accessInfo.getNetworkType().toString(), accessInfo.getId());

		delete(url);
		return new RpcResultInfo();
	}		
	
//	private ArrayList<SocialConnectorAccessInfo> parseGetAccessTokens(Document doc) {
//		ArrayList<SocialConnectorAccessInfo> accessTokens = new ArrayList<SocialConnectorAccessInfo>();
//
//		try {
//			doc.getDocumentElement().normalize();
//
//			NodeList nodeList = doc.getElementsByTagName(Entity.ACCESS_TOKEN);
//
//			for (int i = 0; i < nodeList.getLength(); i++) {
//
//				Node fstNode = nodeList.item(i);
//
//				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
//					SocialConnectorAccessInfo accessToken = new SocialConnectorAccessInfo();
//
//					Element fstElmnt = (Element) fstNode;
//
//					accessToken.setId(ServerUtils.getNode(fstElmnt, AccessTokenAttribute.ID));
//					accessToken.setNetworkName(ServerUtils.getNode(fstElmnt, AccessTokenAttribute.SOCIAL_NETWORK_NAME));
//					accessToken.setName(ServerUtils.getNode(fstElmnt, AccessTokenAttribute.NAME));
//					accessToken.setValue(ServerUtils.getNode(fstElmnt, AccessTokenAttribute.VALUE));
//					
//					accessToken.setDefault(ServerUtils.StrToBoolean(ServerUtils.getNodeValue(fstElmnt, AccessTokenAttribute.DEFAULT)));
//					
//					accessTokens.add(accessToken);
//				}
//			}
//			return accessTokens;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return null;
//	}

	public String getLocationToken(String companyId, String networkName) throws ServiceFailedException {
		String url = createLocationTokenResource(companyId, networkName);
		Document d = null;

		try {
			d = get(url);
		}
		catch (ServiceFailedException e) {
			if (e.getReason() == ServiceFailedException.NOT_FOUND) {
				return null;
			}
		}

		if (d != null) {
			return parseGetLocationToken(d);
		}

		return null;
	}

	public RpcResultInfo putLocationToken(String companyId, String networkName, String locationToken) throws ServiceFailedException {
		String url = createLocationTokenResource(companyId, networkName);
		
		Form form = new Form();
		
		form.add(SocialConnectorAttribute.LOCATION_TOKEN, locationToken);
		
		put(url, form);

		return new RpcResultInfo(locationToken);
	}
	
	private String parseGetLocationToken(Document doc) {
		String locationToken = null;

		try {
			doc.getDocumentElement().normalize();

			NodeList nodeList = doc.getElementsByTagName(Entity.COMPANY_SOCIAL_CONNECTOR);

			for (int i = 0; i < nodeList.getLength(); i++) {

				Node fstNode = nodeList.item(i);

				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
					Element fstElmnt = (Element) fstNode;

					locationToken = ServerUtils.getNode(fstElmnt, SocialConnectorAttribute.LOCATION_TOKEN);
				}
			}
			return locationToken;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public HashMap<NetworkType, ArrayList<String>> getDisplayTokens(String companyId, String displayId) throws ServiceFailedException {
		HashMap<NetworkType, ArrayList<String>> displayTokens = new HashMap<SocialConnectorAccessInfo.NetworkType, ArrayList<String>>();
		
		for (NetworkType type: NetworkType.values()) {
			String url = createDisplayTokenResource(companyId, displayId, type.toString());
			Document d = null;
			ArrayList<String> displayToken = null; 
			
			try {
				d = get(url);
			}
			catch (ServiceFailedException e) {
				if (e.getReason() == ServiceFailedException.NOT_FOUND) {
					// not found? return a null token
				}
			}
	
			if (d != null) {
				displayToken = parseGetDisplayTokens(d);
			}
			
			displayTokens.put(type, displayToken);

		}
		
		return displayTokens;
	}

	public RpcResultInfo putDisplayTokens(String companyId, String displayId, String networkName, String accessToken, String locationToken) throws ServiceFailedException {
		String url = createDisplayTokenResource(companyId, displayId, networkName);
		
		Form form = new Form();

		form.add(SocialConnectorAttribute.ACCESS_TOKEN_ID, accessToken);
		form.add(SocialConnectorAttribute.LOCATION_TOKEN, locationToken);
		
		put(url, form);

		return new RpcResultInfo(displayId);
	}
	
	private ArrayList<String> parseGetDisplayTokens(Document doc) {
		ArrayList<String> tokens = new ArrayList<String>();

		try {
			doc.getDocumentElement().normalize();
	
			Node rootNode = doc.getDocumentElement();
	
			if (rootNode.getNodeType() == Node.ELEMENT_NODE) {
	
				Element fstElmnt = (Element) rootNode;
				tokens.add(ServerUtils.getNode(fstElmnt, SocialConnectorAttribute.ACCESS_TOKEN_ID));
				tokens.add(ServerUtils.getNode(fstElmnt, SocialConnectorAttribute.LOCATION_TOKEN));
			}	
			return tokens;
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return null;
	}
	
	private String createAccessTokenResource(String companyId, String networkName, String tokenId) {
		//URI: https://rdncore.appspot.com/company/{companyId}/socialnetwork/{networkName}/token/{tokenId}

		return "/company/" + companyId + "/socialnetwork/" + networkName + "/token/" + tokenId;
	}

//	private String createAccessTokensResource(String companyId) {
//		//URI: https://rdncore.appspot.com/company/{companyId}/tokens
//
//		return "/company/" + companyId + "/tokens";
//	}

	private String createLocationTokenResource(String companyId, String networkName) {
		//URI: https://rdncore.appspot.com/company/{companyId}/socialnetwork/{networkName} 

		return "/company/" + companyId + "/socialnetwork/" + networkName;
	}
	
	private String createDisplayTokenResource(String companyId, String displayId, String networkName) {
		//URI: https://rdncore.appspot.com/company/{companyId}//{displayId}/socialnetwork/{networkName}
		
		return  "/company/" + companyId + "/display/" + displayId + "/socialnetwork/" + networkName;
	}

//	private SocialConnectorAccessInfo DocToAccessToken(Document doc) {
//		SocialConnectorAccessInfo accessToken = new SocialConnectorAccessInfo();
//
//		try {
//			doc.getDocumentElement().normalize();
//
//			Node rootNode = doc.getDocumentElement();
//
//			if (rootNode.getNodeType() == Node.ELEMENT_NODE) {
//
//				Element fstElmnt = (Element) rootNode;
//				accessToken.setId(ServerUtils.getNode(fstElmnt, "ID"));
//				accessToken.setName(ServerUtils.getNode(fstElmnt, "NAME"));
//				accessToken.setValue(ServerUtils.getNode(fstElmnt, "VALUE"));
//				
//				accessToken.setDefault(ServerUtils.StrToBoolean(ServerUtils.getNodeValue(fstElmnt, "IS_DEFAULT")));
//			}	
//			return accessToken;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return null;
//	}

}
