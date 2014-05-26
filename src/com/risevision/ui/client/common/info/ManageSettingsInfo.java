package com.risevision.ui.client.common.info;

//import java.util.HashMap;

public class ManageSettingsInfo {
	public static final int LOGO_WIDTH = 234;
	public static final int LOGO_HEIGHT = 60;
	public static final int BANNER_WIDTH = 468;
	public static final int BANNER_HEIGHT = 60;
//	public static final int MANAGE_ALL_SETTINGS = 0;
//	public static final int MANAGE_PORTAL_PAGE = 1;
//	public static final int MANAGE_SECURITY_PAGE = 2;
//	public static final int MANAGE_DEFAULT_PRESENTATIONS_PAGE = 3;
//	public static final int MANAGE_TERMS_PAGE = 4;

//	public static final String LOGIN_URL = "loginURL";
	public static final String LOGO_URL = "logoURL";
	public static final String LOGO_TARGET_URL = "logoTargetURL";
	public static final String BANNER_URL = "bannerURL";
	public static final String BANNER_TARGET_URL = "bannerTargetURL";
	public static final String ADSENSE_SERVICE_ID = "adsenseServiceId";
	public static final String ADSENSE_SERVICE_SLOT = "adsenseServiceSlot";
	public static final String BANNER_BACKGROUND_COLOR = "bannerBackgroundColor";
//	public static final String LINE_COLOR = "lineColor";
//	public static final String SHOW_NEWS = "showNews";
	public static final String NEWS_URL = "newsURL";
//	public static final String START_PAGE_URL = "startPageURL";
	public static final String HELP_URL = "helpURL";
//	public static final String SUPPORT_URL = "supportURL";
	public static final String SUPPORT_EMAIL = "supportEmail";
//	public static final String TRAINING_URL = "trainingURL";
//	public static final String SALES_URL = "salesURL";
	public static final String SALES_EMAIL = "salesEmail";
	public static final String LOGOUT_URL = "logoutURL";
//	public static final String DIRECTORY_LISTING = "directoryListing";
	public static final String DIRECTORY_DESCRIPTION = "directoryDescription";
//	public static final String SHOW_TUTORIAL = "showTutorial";
	public static final String TUTORIAL_URL = "tutorialURL";
	public static final String ALLOW_COMPANY_REGISTRATIONS = "allowCompanyRegistrations";
	public static final String ANALYTICS_ID = "analyticsID";
//	public static final String ANALYTICS_REPORTING_URL = "analyticsReportingURL";
//	public static final String COMPANY_REGISTRATION_TERMS = "companyRegistrationTerms";
//	public static final String COMPANY_INSTALLATION_TERMS = "displayInstallationTerms";
//	public static final String DISPLAY_SUBSCRIPTION_TERMS = "displaySubscriptionTerms";
//	public static final String NETWORK_OPERATOR_SUBSCRIPTION_TERMS = "networkOperatorSubscriptionTerms";
	public static final String SECURITY_ROLES = "securityRoles";
	public static final String OPERATOR_START_PRESENTATION = "operatorStartPresentation";
	public static final String USER_START_PRESENTATION = "userStartPresentation";

	/*

	private HashMap<String, String[]> settingsMap;
	private HashMap<String, String[]> pnoSettingsMap;
	
	public void setSettingsMap(HashMap<String, String[]> settingsMap) {
		this.settingsMap = settingsMap;
	}

	public HashMap<String, String[]> getSettingsMap() {
		return settingsMap;
	}

	public void setPnoSettingsMap(HashMap<String, String[]> pnoSettingsMap) {
		this.pnoSettingsMap = pnoSettingsMap;
	}

	public HashMap<String, String[]> getPnoSettingsMap() {
		return pnoSettingsMap;
	}
	
	private int currentPage;
	
	// LAST MODIFIED
	private String changedBy;
	private Date changeDate;
	
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public String getChangedBy() {
		return changedBy;
	}

	public void setChangedBy(String changedBy) {
		this.changedBy = changedBy;
	}

	public Date getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}

	// PORTAL SETTINGS
	// # Login URL (pnoname.risedisplaynetwork.appspot.com (read only))
	private String loginURL;
	// # Logo URL (content)
	private String logoURL;
	// # Logo URL (URL to PNO home page, opens in new tab when user clicks on
	// it)
	private String logoTargetURL;
	// # Banner URL
	private String bannerURL;
	// # Banner Background Color (area behind Logo, Banner and User, Signout,
	// News, Help and Support)
	private String bannerBackgroundColor;
	// # Line Color
	private String lineColor;
	// # [ ] News (default not checked, if checked prompt and show URL)
	private Boolean showNews;
	private String newsURL;
	// # Start Page URL
	private String startPageURL;
	// # *Help URL (default Rise Learning Center, with Restore option)
	private String helpURL;
	// # *Support URL
	private String supportURL;
	// # *Support Email
	private String supportEmail;
	// # Training URL (default Rise provided Training URL, with restore option,
	// can specify the URL of a Training Page that will pop-up whenever the User
	// is editing a Trial Presentation until such time as the User has selected
	// "don't show again" for that particular Presentation. Or they can use the
	// default as specified by the Network Operator above them)
	private String trainingURL;
	// # Sales URL
	private String salesURL;
	// # Sales Email
	private String salesEmail;
	// # Google Analytics
	private String analyticsID;
	private String analyticsReportingURL;

	// TERMS AND CONDITIONS URLs
	// * Company Registration
	private String companyRegistrationTerms;
	// * Display Installation
	private String displayInstallationTerms;
	// * Display Subscription
	private String displaySubscriptionTerms;
	// * Private Network Operator Subscription
	private String networkOperatorSubscriptionTerms;

	// SECURITY
	private ArrayList<String> securityRoles;
	
	// CONSTRUCTOR
	public ManageSettingsInfo(HashMap<String, String[]> companySettings){
		
	}
	
	// GETTERS AND SETTERS
	public String getLoginURL() {
		return loginURL;
	}

	public void setLoginURL(String loginURL) {
		this.loginURL = loginURL;
	}

	public String getLogoURL() {
		return logoURL;
	}

	public void setLogoURL(String logoURL) {
		this.logoURL = logoURL;
	}

	public String getLogoTargetURL() {
		return logoTargetURL;
	}

	public void setLogoTargetURL(String logoTargetURL) {
		this.logoTargetURL = logoTargetURL;
	}

	public String getBannerURL() {
		return bannerURL;
	}

	public void setBannerURL(String bannerURL) {
		this.bannerURL = bannerURL;
	}

	public String getBannerBackgroundColor() {
		return bannerBackgroundColor;
	}

	public void setBannerBackgroundColor(String bannerBackgroundColor) {
		this.bannerBackgroundColor = bannerBackgroundColor;
	}

	public String getLineColor() {
		return lineColor;
	}

	public void setLineColor(String lineColor) {
		this.lineColor = lineColor;
	}

	public Boolean getShowNews() {
		return showNews;
	}

	public void setShowNews(Boolean showNews) {
		this.showNews = showNews;
	}

	public String getNewsURL() {
		return newsURL;
	}

	public void setNewsURL(String newsURL) {
		this.newsURL = newsURL;
	}

	public String getStartPageURL() {
		return startPageURL;
	}

	public void setStartPageURL(String startPageURL) {
		this.startPageURL = startPageURL;
	}

	public String getHelpURL() {
		return helpURL;
	}

	public void setHelpURL(String helpURL) {
		this.helpURL = helpURL;
	}

	public String getSupportURL() {
		return supportURL;
	}

	public void setSupportURL(String supportURL) {
		this.supportURL = supportURL;
	}

	public String getSupportEmail() {
		return supportEmail;
	}

	public void setSupportEmail(String supportEmail) {
		this.supportEmail = supportEmail;
	}

	public String getTrainingURL() {
		return trainingURL;
	}

	public void setTrainingURL(String trainingURL) {
		this.trainingURL = trainingURL;
	}

	public String getSalesURL() {
		return salesURL;
	}

	public void setSalesURL(String salesURL) {
		this.salesURL = salesURL;
	}

	public String getSalesEmail() {
		return salesEmail;
	}

	public void setSalesEmail(String salesEmail) {
		this.salesEmail = salesEmail;
	}

	public String getAnalyticsID() {
		return analyticsID;
	}

	public void setAnalyticsID(String analyticsID) {
		this.analyticsID = analyticsID;
	}

	public String getAnalyticsReportingURL() {
		return analyticsReportingURL;
	}

	public void setAnalyticsReportingURL(String analyticsReportingURL) {
		this.analyticsReportingURL = analyticsReportingURL;
	}

	public String getCompanyRegistrationTerms() {
		return companyRegistrationTerms;
	}

	public void setCompanyRegistrationTerms(String companyRegistrationTerms) {
		this.companyRegistrationTerms = companyRegistrationTerms;
	}

	public String getDisplayInstallationTerms() {
		return displayInstallationTerms;
	}

	public void setDisplayInstallationTerms(String displayInstallationTerms) {
		this.displayInstallationTerms = displayInstallationTerms;
	}

	public String getDisplaySubscriptionTerms() {
		return displaySubscriptionTerms;
	}

	public void setDisplaySubscriptionTerms(String displaySubscriptionTerms) {
		this.displaySubscriptionTerms = displaySubscriptionTerms;
	}

	public String getNetworkOperatorSubscriptionTerms() {
		return networkOperatorSubscriptionTerms;
	}

	public void setNetworkOperatorSubscriptionTerms(
			String networkOperatorSubscriptionTerms) {
		this.networkOperatorSubscriptionTerms = networkOperatorSubscriptionTerms;
	}

	public ArrayList<String> getSecurityRoles() {
		return securityRoles;
	}

	public void setSecurityRoles(ArrayList<String> securityRoles) {
		this.securityRoles = securityRoles;
	}
	*/
}
