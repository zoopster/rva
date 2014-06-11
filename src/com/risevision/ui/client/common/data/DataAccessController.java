// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.data;

import java.util.Date;

import com.google.gwt.http.client.URL;
import com.risevision.ui.client.common.controller.ConfigurationController;
import com.risevision.ui.client.common.data.DataControllerBase.DataResponseBase;

public class DataAccessController {
	private static ConfigurationController configController = ConfigurationController.getInstance();
		
	public static void getData(String urlToken, String method, String tq, DataResponseBase response) {

		String consumerSecret = configController.getOAuth().getConsumerSecret();
		String tokenSecret = configController.getUserOAuth().getUserTokenSecret();
		
		String action = configController.getConfiguration().getServerURL() + "/v2" + urlToken;
		String callback = "callback" + (int)(Math.random() * 1000000) + "_" + new Date().getTime();
		String tqx = "responseHandler:" + callback;
		
		String oauth_token = configController.getUserOAuth().getUserToken();
		String oauth_consumer_key = configController.getOAuth().getConsumerKey();
		
		String url = createURLNative(consumerSecret, tokenSecret, action, method, tqx, tq, oauth_consumer_key, oauth_token);
		getDataNative(url, callback, response);
		
	}
	
	public static void getFinancialData(String urlToken, String tq, DataResponseBase response, boolean customQuery) {
		String action = configController.getConfiguration().getFinancialServerURL() + urlToken;
//		String action = "http://contentfinancial2.appspot.com" + urlToken;
		
		String callback = "callback" + (int)(Math.random() * 1000000) + "_" + new Date().getTime();
		String tqx = "responseHandler:" + callback;
		
		String url = action + "?tqx=" + tqx + "&";
		if (customQuery) {
			url += tq;
		}
		else {
			url += "tq=" + URL.encodeQueryString(tq);
		}
		
		getDataNative(url, callback, response);
		
	}
	
//	private void onResponse(JavaScriptObject jso) {
//		JSOModel jsoModel = (JSOModel) jso;
//
//		String status = jsoModel.get("status");
//		if (status.equalsIgnoreCase("OK")) {
//			parseData(jsoModel.getObject("table"));
//		}
//	}
	
	public static void correctTimestamp(long timestamp) {
		correctTimestamp((int) (timestamp / 1000));
	}
	
	private static native void correctTimestamp (int seconds) /*-{
		$wnd.OAuth.correctTimestamp(seconds);
	}-*/;	
	
	private static native String createURLNative(String consumerSecret, String tokenSecret, String action, String method, 
			String tqx, String tq, String oauth_consumer_key, String oauth_token) /*-{
		var accessor = { 
			consumerSecret: consumerSecret, 
			tokenSecret: tokenSecret
			};
		
		// NOTE: createURL functionality relies on the object being created in Javascript; GWT doesn't construct
		// the object properly 
		var messageURL = $wnd.createURL(accessor, action, method, tqx, tq, oauth_consumer_key, oauth_token);
	
		return messageURL;
	}-*/;	
				
	public static native void getDataNative(String url, String callbackName, DataResponseBase response) /*-{
//		debugger;
		
		var instance = this;
		var callbackSuccess = function(data) {
//			@com.risevision.ui.client.common.data.DataAccessController::onResponse(Lcom/google/gwt/core/client/JavaScriptObject;)( response );
			response.@com.risevision.ui.client.common.data.DataControllerBase.DataResponseBase::onResponse(Lcom/google/gwt/core/client/JavaScriptObject;)( data );
		};
		
		var callbackError = function(errorString) {
			response.@com.risevision.ui.client.common.data.DataControllerBase.DataResponseBase::onError(Ljava/lang/String;)( errorString );
		};
	
//    	$wnd.$.ajax({
//			url: url,
//		    dataType: "jsonp",
//		    cache: true,
//		    jsonp: false,
//		    jsonpCallback: callbackName, 
//		    success: function(data) { 
////		    		debugger;
//
//					try {
//	    				callbackSuccess(data);
//					}
//					catch (err) {
//						$wnd.alert("Error - " + result + " - " + err.message);
//					}
//	    					    	
////		    		if (uppercase(data.status) == "OK") {
////		    			callback(data);
////		    		}
////		    		else {
////		    			// report error;
////		    		}
//		    		
////		    		$wnd.logText("status: " + data.status);
//		    },
//			error: function(xOptions, textStatus) {
//				debugger;
//				
//				callbackError(textStatus);
//			}
//		});

		// using separate JSONP library that allows for error catching in the requests
    	$wnd.$.jsonp({
			url: url,
			cache: true,
		    callback: callbackName, 
		    success: function(data) { 
//		    		debugger;

					try {
	    				callbackSuccess(data);
					}
					catch (err) {
						$wnd.alert("Error - " + result + " - " + err.message);
					}
		    },
			error: function(xOptions, textStatus) {
//					debugger;
					
					callbackError(textStatus);
			}
		});

	}-*/;	
	
	
	
}
