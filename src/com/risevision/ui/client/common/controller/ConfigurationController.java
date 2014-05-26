package com.risevision.ui.client.common.controller;

import com.risevision.ui.client.common.info.ConfigurationInfo;
import com.risevision.ui.client.common.info.OAuthInfo;
import com.risevision.ui.client.common.info.PrerequisitesInfo;
import com.risevision.ui.client.common.info.UserOAuthInfo;

public class ConfigurationController {	
	private static ConfigurationController instance;
	private ConfigurationInfo configuration;
	private OAuthInfo oAuth;
	private UserOAuthInfo userOAuth;

	public ConfigurationInfo getConfiguration() {
		return configuration;
	}
	
	public OAuthInfo getOAuth() {
		return oAuth;
	}
	
	public UserOAuthInfo getUserOAuth() {
		return userOAuth;
	}
	
	public void setConfiguration(PrerequisitesInfo prereqInfo) {
		this.configuration = prereqInfo.getConfiguration();
		this.oAuth = prereqInfo.getOAuth();
		this.userOAuth = prereqInfo.getUserOAuth();
	}
	
	public static ConfigurationController getInstance() {
		try {
			if (instance == null)
				instance = new ConfigurationController();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}
	
	public boolean isRiseCompanyId(String id) {
		if (configuration != null && id != null) {
			return configuration.getRiseId().equals(id);
		}
		return false;
	}
}
