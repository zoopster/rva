// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.server.data;

import java.io.Serializable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.risevision.ui.client.common.info.ConfigurationInfo;
import com.risevision.ui.server.utils.ServerUtils;

@SuppressWarnings("serial")
@PersistenceCapable
public class PersistentConfigurationInfo implements Serializable {

	private static final String SERVER_URL = "https://rvacore-test.appspot.com";
//	private static final String SERVER_URL = "https://rvacore-test2.appspot.com/";
//	private static final String SERVER_URL = "https://rvaserver2.appspot.com";
	
	private static final String REQUEST_TOKEN_URL = SERVER_URL + "/_ah/OAuthGetRequestToken";
	private static final String AUTHORIZE_TOKEN_URL = SERVER_URL + "/_ah/OAuthAuthorizeToken";
	private static final String ACCESS_TOKEN_URL = SERVER_URL + "/_ah/OAuthGetAccessToken";

	// URL of the Viewer (used for Preview functionality)
	private static final String VIEWER_URL = "http://viewer-test.appspot.com/";     // old non HRD
//	private static final String VIEWER_URL = "http://rvaviewer-test.appspot.com/";  //new HRD for viewer-test.appspot.com
//	private static final String VIEWER_URL = "http://rvashow.appspot.com/";    //old non HRD app engine app
//	private static final String VIEWER_URL = "http://rvashow2.appspot.com/";   //HRD app engine app
//	private static final String VIEWER_URL = "http://preview.risevision.com/"; //production
	
	private static final String MEDIA_LIBRARY_URL = "http://storage.rvacore-test.appspot.com";
	
	// ID of Rise Vision Inc - Top tier PNO
	private static final String RISE_ID = "b428b4e8-c8b9-41d5-8a10-b4193c789443"; // Dev
//	private static final String RISE_ID = "f114ad26-949d-44b4-87e9-8528afc76ce4"; // Production

	// Link to the Rise Installer
	private static final String WINDOWS_INSTALLER_URL = SERVER_URL + "/player/download?os=win";
	private static final String LINUX_INSTALLER_URL = SERVER_URL + "/player/download?os=lnx";
	private static final String MAC_INSTALLER_URL = SERVER_URL + "/player/download?os=mac";
	
	private static final String INSTALLER_URL = "http://www.risevision.com/player/";
	
	// Logout URL 
//	private static final String LOGOUT_URL = "/logout/"; // Logout servlet (redirects to login page)
	private static final String LOGOUT_URL = "http://www.risevision.com/logout/";
	
	// Terms and Conditions URL
//	private static final String TERMS_URL = "http://docs.google.com/document/pub?id=1YwrUFIBCKG4iIo34Q-jxHM-4yJdJ13ohb6v3wLXun1A";
//	private static final String TERMS_URL = "RiseVisionTermsofServiceandPrivacy.html";
//	private static final String TERMS_URL = "http://www.risevision.com/TermsOfServiceAndPrivacy/";
	private static final String TERMS_URL = "http://www.risevision.com/terms-service-privacy/";
	
	// Account associated with these keys has been deactivated
    public static final String AWS_ACCESS_KEY_ID = "";
    public static final String AWS_SECRET_ACCESS_KEY = "";
    
    public static final String GCS_ACCOUNT_EMAIL = "";
    
    public static final String FINANCIAL_SERVER_URL = "http://contentfinancial2-test.appspot.com";
//    public static final String FINANCIAL_SERVER_URL = "http://contentfinancial2.appspot.com";
	
	@Persistent
	@PrimaryKey
	private String entityKey;
	@Persistent
	private String riseId;
	@Persistent
	private String serverURL;
	@Persistent
	private String viewerURL;
	@Persistent
	private String mediaLibraryURL;
	@Persistent
	private String windowsInstallerURL;
	@Persistent
	private String linuxInstallerURL;
	@Persistent
	private String macInstallerURL;
	@Persistent
	private String installerURL;
	@Persistent
	private String logoutURL;
	@Persistent
	private String termsURL;
	@Persistent
	private String requestTokenURL;
	@Persistent
	private String authorizeTokenURL;
	@Persistent
	private String accessTokenURL;
	
//	@Persistent
	private String awsAccessKeyId;
//	@Persistent
	private String awsSecretAccessKey;
	
	@Persistent
	private String gcsAccountEmail;
	
	@Persistent
	private String financialServerURL;
	
	public static final String ENTITY_KEY = "Config";

	public PersistentConfigurationInfo() {
		//Run this for Dev.
		entityKey = ENTITY_KEY;
		riseId = RISE_ID;
		serverURL = SERVER_URL;
		viewerURL = VIEWER_URL;
		mediaLibraryURL = MEDIA_LIBRARY_URL;
		windowsInstallerURL = WINDOWS_INSTALLER_URL;
		linuxInstallerURL = LINUX_INSTALLER_URL;
		macInstallerURL = MAC_INSTALLER_URL;
		installerURL = INSTALLER_URL;
		logoutURL = LOGOUT_URL;
		termsURL = TERMS_URL;
		requestTokenURL = REQUEST_TOKEN_URL;
		authorizeTokenURL = AUTHORIZE_TOKEN_URL;
		accessTokenURL = ACCESS_TOKEN_URL;
		awsAccessKeyId = AWS_ACCESS_KEY_ID;
		awsSecretAccessKey = AWS_SECRET_ACCESS_KEY;
		gcsAccountEmail = GCS_ACCOUNT_EMAIL;
		financialServerURL = FINANCIAL_SERVER_URL;
	}
	
	public String getEntityKey() {
		return entityKey;
	}

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
	
	public void setMacInstallerURL(String macInstallerURL) {
		this.macInstallerURL = macInstallerURL;
	}

	public String getMacInstallerURL() {
		return macInstallerURL;
	}
	
	public void setInstallerURL(String installerURL) {
		this.installerURL = installerURL;
	}
	
	public String getInstallerURL() {
		return installerURL;
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

	public String getAwsSecretAccessKey() {
		return awsSecretAccessKey;
	}

	public void setAwsSecretAccessKey(String awsSecretAccessKey) {
		this.awsSecretAccessKey = awsSecretAccessKey;
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

	public ConfigurationInfo getConfigurationInfo() {
		ConfigurationInfo config = new ConfigurationInfo();
		
		config.setRiseId(this.getRiseId());
		config.setServerURL(this.getServerURL());
		config.setViewerURL(this.getViewerURL());
		config.setMediaLibraryURL(this.getMediaLibraryURL());
		config.setWindowsInstallerURL(this.getWindowsInstallerURL());
		config.setLinuxInstallerURL(this.getLinuxInstallerURL());
		config.setMacInstallerURL(this.getMacInstallerURL());
		config.setInstallerURL(this.getInstallerURL());
		config.setRedirectURL(this.getLogoutURL());
		config.setLogoutURL(ServerUtils.getLogoutURL(this.getLogoutURL()));
		config.setTermsURL(this.getTermsURL());	
		config.setRequestTokenURL(this.getRequestTokenURL());	
		config.setAuthorizeTokenURL(this.getAuthorizeTokenURL());	
		config.setAccessTokenURL(this.getAccessTokenURL());	
		
//		config.setAwsAccessKeyId(this.getAwsAccessKeyId());
		// Secret Access Key is not sent to the Client Side
//		config.setAwsSecretAccessKey(this.getAwsSecretAccessKey());
		
		config.setGcsAccountEmail(this.getGcsAccountEmail());
		
		config.setFinancialServerURL(this.getFinancialServerURL());
		
		return config;
	}
}

