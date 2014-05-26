// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.info;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ConfigurationInfo implements Serializable  {
	private String riseId;
	private String serverURL;
	private String viewerURL;
	private String mediaLibraryURL;
	private String windowsInstallerURL;
	private String linuxInstallerURL;
	private String macInstallerURL;
	private String installerURL;

	private String redirectURL;
	private String logoutURL;
	private String termsURL;
	private String requestTokenURL;
	private String authorizeTokenURL;
	private String accessTokenURL;
	
	private String awsAccessKeyId;
	
	private String gcsAccountEmail;
	
	private String financialServerURL;
	
	public String getRiseId() {
		return riseId;
	}

	public void setRiseId(String riseId) {
		this.riseId = riseId;
	}
	
	public String getServerURL() {
		return serverURL;
	}
	
	public void setServerURL(String serverURL) {
		this.serverURL = serverURL;
	}

	public String getViewerURL() {
		return viewerURL;
	}

	public void setViewerURL(String viewerURL) {
		this.viewerURL = viewerURL;
	}
	
	public String getMediaLibraryURL() {
		return mediaLibraryURL;
	}

	public void setMediaLibraryURL(String mediaLibraryURL) {
		this.mediaLibraryURL = mediaLibraryURL;
	}

	public String getWindowsInstallerURL() {
		return windowsInstallerURL;
	}

	public void setWindowsInstallerURL(String windowsInstallerURL) {
		this.windowsInstallerURL = windowsInstallerURL;
	}
	
	public String getLinuxInstallerURL() {
		return linuxInstallerURL;
	}

	public void setLinuxInstallerURL(String linuxInstallerURL) {
		this.linuxInstallerURL = linuxInstallerURL;
	}
	
	public String getMacInstallerURL() {
		return macInstallerURL;
	}

	public void setMacInstallerURL(String macInstallerURL) {
		this.macInstallerURL = macInstallerURL;
	}
	
	public String getInstallerURL() {
		return installerURL;
	}

	public void setInstallerURL(String installerURL) {
		this.installerURL = installerURL;
	}
	
	public String getRedirectURL() {
		return redirectURL;
	}

	public void setRedirectURL(String redirectURL) {
		this.redirectURL = redirectURL;
	}
	
	public String getLogoutURL() {
		return logoutURL;
	}

	public void setLogoutURL(String logoutURL) {
		this.logoutURL = logoutURL;
	}
	
	public String getTermsURL() {
		return termsURL;
	}

	public void setTermsURL(String termsURL) {
		this.termsURL = termsURL;
	}
	
	public String getRequestTokenURL() {
		return requestTokenURL;
	}

	public void setRequestTokenURL(String requestTokenURL) {
		this.requestTokenURL = requestTokenURL;
	}
	
	public String getAuthorizeTokenURL() {
		return authorizeTokenURL;
	}

	public void setAuthorizeTokenURL(String authorizeTokenURL) {
		this.authorizeTokenURL = authorizeTokenURL;
	}
	
	public String getAccessTokenURL() {
		return accessTokenURL;
	}

	public void setAccessTokenURL(String accessTokenURL) {
		this.accessTokenURL = accessTokenURL;
	}

	public String getAwsAccessKeyId() {
		return awsAccessKeyId;
	}

	public void setAwsAccessKeyId(String awsAccessKeyId) {
		this.awsAccessKeyId = awsAccessKeyId;
	}

	public String getGcsAccountEmail() {
		return gcsAccountEmail;
	}

	public void setGcsAccountEmail(String gcsAccountEmail) {
		this.gcsAccountEmail = gcsAccountEmail;
	}
	
	public String getFinancialServerURL() {
		return financialServerURL;
	}
	
	public void setFinancialServerURL(String financialServerURL) {
		this.financialServerURL = financialServerURL;
	}
	
}
