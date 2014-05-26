package com.risevision.ui.client.common.widgets.socialConnector;

import com.google.gwt.user.client.Command;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.ui.client.common.info.SocialConnectorAccessInfo.NetworkType;

public class SocialConnectorAccessController {	
	private static final String TOKEN_STRING = "#access_token=";
	private static SocialConnectorAccessController instance;
	private String accessToken;
	private NetworkType networkType;
	private Command readyCommand;

	public static SocialConnectorAccessController getInstance() {
		try {
			if (instance == null)
				instance = new SocialConnectorAccessController();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}
	
	public SocialConnectorAccessController() {
		exportStaticMethods();
	}
	
	public static native void exportStaticMethods() /*-{
		$wnd._setFoursquareToken =
		$entry(@com.risevision.ui.client.common.widgets.socialConnector.SocialConnectorAccessController::setFoursquareToken(Ljava/lang/String;));
		$wnd._setTwitterToken =
		$entry(@com.risevision.ui.client.common.widgets.socialConnector.SocialConnectorAccessController::setTwitterToken(Ljava/lang/String;));
	}-*/;
	
	private static void setFoursquareToken(String token) {
		if (token != null & token.contains(TOKEN_STRING)) {
			instance.setTokenResponse(NetworkType.foursquare, 
					token.substring(TOKEN_STRING.length(), token.length()));
		}
	}
	
	private static void setTwitterToken(String token) {
		if (!RiseUtils.strIsNullOrEmpty(token)) {
			instance.setTokenResponse(NetworkType.twitter, token);
		}
	}
	
	private void setTokenResponse(NetworkType networkType, String accessToken) {
		this.accessToken = accessToken;
		this.networkType = networkType;

		if (readyCommand != null) {
			readyCommand.execute();
			
//			readyCommand = null;
		}
	}
	
	public void setReadyCommand(Command readyCommand) {
		this.readyCommand = readyCommand;
	}
	
	public NetworkType getNetworkType()	 {
		return networkType;
	}
	
	public String getAccessToken() {
		return accessToken;
	}

}
