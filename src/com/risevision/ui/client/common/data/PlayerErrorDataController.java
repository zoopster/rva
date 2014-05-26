// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.data;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.risevision.common.client.json.JSOModel;
import com.risevision.ui.client.common.info.PlayerErrorInfo;

public class PlayerErrorDataController extends DataControllerBase {
	private static PlayerErrorDataController instance;
	
	public static PlayerErrorDataController getInstance() {
		try {
			if (instance == null)
				instance = new PlayerErrorDataController();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (PlayerErrorDataController) instance;
	}
	
	public PlayerErrorDataController() {
		headerEntries.add(new HeaderEntry(PlayerErrorInfo.STATUS_CODE));
		headerEntries.add(new HeaderEntry(PlayerErrorInfo.TIMESTAMP));
	}
	
	private class PlayerErrorDataResponse extends DataResponseBase {
		protected AsyncCallback<List<PlayerErrorInfo>> callback;
		
		protected PlayerErrorDataResponse(AsyncCallback<List<PlayerErrorInfo>> callback) {
			this.callback = callback;
		}
		
		protected void onResponseImpl(JSOModel jso) {
			parsePlayerErrorsData(jso, this);
		}
		
		protected void onErrorImpl(Throwable caught) {
			callback.onFailure(caught);
		}
	}
	
	public void getPlayerErrors(String displayId, AsyncCallback<List<PlayerErrorInfo>> callback) {
		String action = "/player/" + displayId + "/statuslog";
		String method = "GET"; 
	
		PlayerErrorDataResponse response = new PlayerErrorDataResponse(callback);
		
		getData(action, method, "", response);
	}
	
	protected void parsePlayerErrorsData(JSOModel jsDisplays, PlayerErrorDataResponse response) {
		JsArray<JSOModel> rows = jsDisplays.getArray("rows");
		
		List<PlayerErrorInfo> errors = new ArrayList<PlayerErrorInfo>();
		
		for (int i = 0; i < rows.length(); i++) {
			JSOModel row = rows.get(i);
			
			JsArray<JSOModel> column = row.getArray("c");
			
			PlayerErrorInfo error = new PlayerErrorInfo();
			int j = 0;
			
			error.setStatusCode(column.get(headerEntries.get(j++).col).get("v"));
			error.setTimestamp(column.get(headerEntries.get(j++).col).getDate("v"));
			
			errors.add(error);
		}
		
		response.callback.onSuccess(errors);
	}
	
}
