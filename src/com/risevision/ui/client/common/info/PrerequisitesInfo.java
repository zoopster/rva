// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.info;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class PrerequisitesInfo implements Serializable  {
	public static final int STATUS_OK = 0;
	public static final int STATUS_NEW = 1;
	public static final int STATUS_DELETED = 2;
	public static final int STATUS_ERROR = 3;
	public static final int STATUS_WRONG_USERNAME = 4;
	
//	private String loginUrl;
//	private String logoutUrl;
	private UserInfo currentUser;
	private CompanyInfo currentUserCompany;
	private int currentUserStatus = STATUS_OK;
	private ConfigurationInfo configuration;
	private OAuthInfo oAuth;
	private UserOAuthInfo userOAuth;
	private long serverTimestamp;
	
	private List<SystemMessageInfo> systemMessages;
	
//	public String getLoginUrl() {
//		return loginUrl;
//	}

//	public void setLoginUrl(String loginUrl) {
//		this.loginUrl = loginUrl;
//	}

//	public String getLogoutUrl() {
//		return logoutUrl;
//	}
//
//	public void setLogoutUrl(String logoutUrl) {
//		this.logoutUrl = logoutUrl;
//	}

	public UserInfo getCurrentUser() {
		return currentUser;
	}
	
	public void setCurrentUser(UserInfo currentUser) {
		this.currentUser = currentUser;
	}
	
	public CompanyInfo getCurrentUserCompany() {
		return currentUserCompany;
	}
	
	public void setCurrentUserCompany(CompanyInfo currentUserCompany) {
		this.currentUserCompany = currentUserCompany;
	}
	
	public void setCurrentUserStatus(int currentUserStatus) {
		this.currentUserStatus = currentUserStatus;
	}

	public int getCurrentUserStatus() {
		return currentUserStatus;
	}

	public ConfigurationInfo getConfiguration() {
		return configuration;
	}
	
	public void setConfiguration(ConfigurationInfo configuration) {
		this.configuration = configuration;
	}
	
	public OAuthInfo getOAuth() {
		return oAuth;
	}
	
	public void setOAuth(OAuthInfo oAuth) {
		this.oAuth = oAuth;
	}

	public UserOAuthInfo getUserOAuth() {
		return userOAuth;
	}

	public void setUserOAuth(UserOAuthInfo userOAuth) {
		this.userOAuth = userOAuth;
	}

	public long getServerTimestamp() {
		return serverTimestamp;
	}

	public void setServerTimestamp(long serverTimestamp) {
		this.serverTimestamp = serverTimestamp;
	}

	public List<SystemMessageInfo> getSystemMessages() {
		return systemMessages;
	}
	
	public void setSystemMessages(List<SystemMessageInfo> systemMessages) {
		this.systemMessages = systemMessages;
	}
	
}
