// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.data;

import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.risevision.common.client.json.JSOModel;

public class StoreDataController extends DataControllerBase {
	private static StoreDataController instance;
	
	public static StoreDataController getInstance() {
		try {
			if (instance == null)
				instance = new StoreDataController();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (StoreDataController) instance;
	}
	
	private class StoreDataResponse extends DataResponseBase {
		protected AsyncCallback<Object> callback;
		
		protected StoreDataResponse(AsyncCallback<Object> callback) {
			this.callback = callback;
		}
		
		protected void onResponseImpl(JSOModel jso) {
		}
		
		protected void onErrorImpl(Throwable caught) {
			callback.onFailure(caught);
		}
		
		protected void onResponse(JavaScriptObject jso) {			
			if (jso != null) {
				callback.onSuccess(jso);
			}
			else {
				onError("");
			}
		}

	}
	
	public void getSubscriptionStatus(String url, AsyncCallback<Object> callback) {

		StoreDataResponse response = new StoreDataResponse(callback);
		
		String callbackName = "callback" + (int)(Math.random() * 1000000) + "_" + new Date().getTime();

		DataAccessController.getDataNative(url, callbackName, response);
	}
	
	
}
