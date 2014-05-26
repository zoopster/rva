package com.risevision.ui.client.common.exception;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.risevision.ui.client.common.service.LoginService;
import com.risevision.ui.client.common.service.LoginServiceAsync;

public abstract class RiseAsyncCallback<T> implements AsyncCallback<T>{
	public Throwable caught;
	public abstract void onFailure();
	
	@Override
	public final void onFailure(Throwable caught) {
		if (caught instanceof StatusCodeException) {
			switch (((StatusCodeException)caught).getStatusCode()){
			case 401:
				redirectToLoginPage();
				break;
			}
		}
		else if (caught instanceof ServiceFailedException) {
			switch (((ServiceFailedException)caught).getReason()){
			case ServiceFailedException.AUTHENTICATION_FAILED: 
				reAuthenticateUser();
				break;
			default:
				this.caught = caught;
				onFailure();
				break;
			}
		}
		else {
			onFailure();
		}
	}
	
	public static final void reAuthenticateUser() {
		String query = "";
		query += Window.Location.getQueryString().length() == 0 ? "" : Window.Location.getQueryString();
		query += History.getToken().length() == 0 ? "" : (query.length() == 0 ? "?" : "&") + History.getToken();
		Window.Location.replace("/user/request" + query);
	}
	
	public final void redirectToLoginPage() {
		// simple service that doesn't require authentication
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		
		loginService.getLoginURL(Window.Location.getHref(), new RpcCallBackHandler());

	}
	
	class RpcCallBackHandler implements AsyncCallback<String> {
		public void onSuccess(String result) {
			if (result != null) {
				Window.Location.replace(result);
			}
		}

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
			
		}
	}
}
