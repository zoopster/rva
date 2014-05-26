// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.info;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UserOAuthInfo implements Serializable {
	private String userName;

	private String userToken;
	private String userTokenSecret;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public String getUserTokenSecret() {
		return userTokenSecret;
	}
	public void setUserTokenSecret(String userTokenSecret) {
		this.userTokenSecret = userTokenSecret;
	}
}
