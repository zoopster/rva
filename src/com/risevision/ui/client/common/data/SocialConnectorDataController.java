// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.data;

import java.util.ArrayList;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.risevision.common.client.json.JSOModel;
import com.risevision.core.api.social.attributes.AccessTokenAttribute;
import com.risevision.ui.client.common.info.SocialConnectorAccessInfo;
import com.risevision.ui.client.common.info.SocialConnectorAccessInfo.NetworkType;

public class SocialConnectorDataController extends DataControllerBase {
	protected static SocialConnectorDataController instance;
	
	public static SocialConnectorDataController getInstance() {
		try {
			if (instance == null)
				instance = new SocialConnectorDataController();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}
	
	public SocialConnectorDataController() {
		headerEntries.add(new HeaderEntry(AccessTokenAttribute.ID));
		headerEntries.add(new HeaderEntry(AccessTokenAttribute.SOCIAL_NETWORK_NAME));		
		headerEntries.add(new HeaderEntry(AccessTokenAttribute.NAME));
		headerEntries.add(new HeaderEntry(AccessTokenAttribute.VALUE));
		headerEntries.add(new HeaderEntry(AccessTokenAttribute.DEFAULT));
//		headerEntries.add(new HeaderEntry(CommonAttribute.CHANGED_BY));
//		headerEntries.add(new HeaderEntry(CommonAttribute.CHANGE_DATE));
	}
	
	private class AccessTokensDataResponse extends DataResponseBase {
		protected AsyncCallback<ArrayList<SocialConnectorAccessInfo>> callback;	
		
		protected AccessTokensDataResponse(AsyncCallback<ArrayList<SocialConnectorAccessInfo>> callback) {
			this.callback = callback;
		}
		
		protected void onResponseImpl(JSOModel jso) {
			parseAccessTokensData(jso, this);
		}
		
		protected void onErrorImpl(Throwable caught) {
			callback.onFailure(caught);
		}
	}
	
	public void getAccessTokensList(String companyId, AsyncCallback<ArrayList<SocialConnectorAccessInfo>> callback) {
		String action = "/company/" + companyId + "/tokens";
		String method = "GET"; 
		
		String query = "";
		
		AccessTokensDataResponse response = new AccessTokensDataResponse(callback);
		
		getData(action, method, query, response);
	}

	protected void parseAccessTokensData(JSOModel jsCompanies, AccessTokensDataResponse response) {

		JsArray<JSOModel> rows = jsCompanies.getArray("rows");
		
		ArrayList<SocialConnectorAccessInfo> accessTokensList = new ArrayList<SocialConnectorAccessInfo>();
		
		for (int i = 0; i < rows.length(); i++) {
			JSOModel row = rows.get(i);
			
			JsArray<JSOModel> column = row.getArray("c");
			
			SocialConnectorAccessInfo accessToken = new SocialConnectorAccessInfo();
			int j = 0;
			
			accessToken.setId(column.get(headerEntries.get(j++).col).get("v"));
			accessToken.setNetworkType(NetworkType.valueOf(column.get(headerEntries.get(j++).col).get("v")));
			accessToken.setName(column.get(headerEntries.get(j++).col).get("v"));
			accessToken.setValue(column.get(headerEntries.get(j++).col).get("v"));
			accessToken.setDefault(column.get(headerEntries.get(j++).col).getBoolean("v"));
//			accessToken.setChangedBy(column.get(headerEntries.get(j++).col).get("v"));
//			accessToken.setChangeDate(column.get(headerEntries.get(j++).col).getDate("v"));
			
			accessTokensList.add(accessToken);
		}
		
		response.callback.onSuccess(accessTokensList);
	}
	
}
