package com.risevision.ui.client.common.directory;

import com.risevision.ui.client.common.controller.ConfigurationController;
import com.risevision.ui.client.common.directory.DataControllerBase.DataResponseBase;

public class DataAccessController {
	private static ConfigurationController configController = ConfigurationController.getInstance();
		
	public static void getData(String urlToken, String tq, DataResponseBase response) {
		String action = 
				configController.getConfiguration().getServerURL() +
//				"http://rvacore-test2.appspot.com" +
				"/d" +
				urlToken +
				"?auth=noauth" +
				tq;
		
//		String url = "auth=noauth&search=name:+Rise&sort=name+desc";
		
		getDataNative(action, response);
		
	}
	
	public static void correctTimestamp(long timestamp) {
		correctTimestamp((int) (timestamp / 1000));
	}
	
	private static native void correctTimestamp (int seconds) /*-{
		$wnd.OAuth.correctTimestamp(seconds);
	}-*/;	
				
//	private static native void getDataNative(String url, DataResponseBase response) /*-{
////		debugger;
//		
//		var instance = this;
//		var callbackSuccess = function(data, cursor) {
////			@com.risevision.ui.client.common.directory.DataAccessController::onResponse(Lcom/google/gwt/core/client/JavaScriptObject;)( response );
//			response.@com.risevision.ui.client.common.directory.DataControllerBase.DataResponseBase::onResponse(Ljava/lang/String;Ljava/lang/String;)( data , cursor );
//		};
//		
//		var callbackError = function(errorString) {
//			response.@com.risevision.ui.client.common.directory.DataControllerBase.DataResponseBase::onError(Ljava/lang/String;)( errorString );
//		};
//	
//
//		var query = new $wnd.google.visualization.Query(url, {
//			sendMethod: 'scriptInjection'
//		});
//
//		query.send(function(queryResponse) { 
//		    debugger;
//
//			try {
//				if (queryResponse.isError()) {
//					callbackError(queryResponse.getMessage());
//				}
//				else {
//					var responseString = queryResponse.getDataTable().toJSON();
//					var cursor = queryResponse.getDataTable().getTableProperty("cursor");
//					callbackSuccess(responseString, cursor);
//				}
//			}
//			catch (err) {
//				callbackError(err.message);
//			}
//		});
//
//	}-*/;	
	
	private static native void getDataNative(String url, DataResponseBase response) /*-{
//		debugger;
		
		var instance = this;
		var callbackSuccess = function(dataTable, cursor) {
			response.@com.risevision.ui.client.common.directory.DataControllerBase.DataResponseBase::onResponse(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)( dataTable , cursor );
		};
		
		var callbackError = function(errorString) {
			response.@com.risevision.ui.client.common.directory.DataControllerBase.DataResponseBase::onError(Ljava/lang/String;)( errorString );
		};
	
	
		var query = new $wnd.google.visualization.Query(url, {
			sendMethod: 'scriptInjection'
		});
	
		query.send(function(queryResponse) { 
//		    debugger;
	
			try {
				if (queryResponse.isError()) {
					callbackError(queryResponse.getMessage());
				}
				else {
					var dataTable = queryResponse.getDataTable();
					var cursor = dataTable.getTableProperty("cursor");

					callbackSuccess(dataTable, cursor);
				}
			}
			catch (err) {
				callbackError(err.message);
			}
		});
	
	}-*/;	
	
	
}
