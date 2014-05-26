package com.risevision.ui.client.common.info;

import java.io.Serializable;
import java.util.Date;

import com.risevision.common.client.info.HasAddress;
import com.risevision.core.api.types.BrowserUpgradeMode;
import com.risevision.core.api.types.DisplayStatus;

@SuppressWarnings("serial")
public class DisplayInfo implements Serializable, HasAddress {

//	public static final String ID = "id";
//	public static final String NAME = "name";
//	public static final String PLAYERID = "playerId";
//	public static final String STATUS = "subscriptionStatus";
	public static final String RESOLUTION = "resolution";
//	public static final String ADDRESSDESCRIPTION = "addressDescription";
//	public static final String COMPANYADDRESS = "useCompanyAddress";
//	public static final String STREET = "street";
//	public static final String UNIT = "unit";
//	public static final String CITY = "city";
//	public static final String PROVINCE = "province";
//	public static final String COUNTRY = "country";
//	public static final String POSTAL = "postalCode";
//	public static final String TIMEZONE = "timeZone";
//	public static final String RESTARTENABLED = "restartEnabled";
//	public static final String RESTARTTIME = "restartTime";
//	public static final String PLAYERVERSION = "playerVersion";
//	
//	// output attributes only
//	public static final String ADDRESS = "address";
//	public static final String COMPANYNAME = "companyName";

	//subscription statuses
//	public static final String STATUS_ACTIVE = "1";
//	public static final String STATUS_INACTIVE = "0";
	public static final String STATUSNAME_ACTIVE = "Active";
	public static final String STATUSNAME_INACTIVE = "Inactive";
	
	public static final int DISPLAY_NOT_INSTALLED = 0;
	public static final int DISPLAY_OFFLINE = 1;
	public static final int DISPLAY_ONLINE = 2;
	public static final int DISPLAY_ERROR = 3;
	public static final int DISPLAY_BLOCKED = 4;
	
	public static final String BROWSER_AUTOUPGRADE = "Auto Upgrade";
	public static final String BROWSER_CURRENT = "Current";
	public static final String BROWSER_PREVIOUS = "Previous";
	public static final String BROWSER_USER_MANAGED = "User Managed";
	
	private String id;
	private String companyId;
	private String name;

	private String changedBy;
	private Date changeDate;

	private String addressDescription;
	private String city;
	private String country;
	private String postalCode;
	private String province;
	private String street;
	private String timeZone;
	private String unit;
	private boolean useCompanyAddress;

	private int width;
	private int height;
	private int subscriptionStatus;
	private boolean restartEnabled = true;
	private String restartTime = "03:00";
	private boolean monitoringEnabled = false;

	// output fields only
	private String address;
	private String companyName;
	
	private int browserUpgradeMode;
	private String chromiumVersion;
	private String browserName;	
	private String recommendedBrowserVersion;
	
//	private String installerVersion;
	private String playerName;
	private String playerVersion;
	private String osVersion;
	private String cacheVersion;
	private String viewerVersion;
	
	private boolean connected;
	private Date lastConnectionDate;
	private int playerStatus;
	
	private Date blockExpiryDate;
		
	public DisplayInfo() {
		setSubscriptionStatus(DisplayStatus.ACTIVE);

		setUseCompanyAddress(true); 
		
		setCountry("");
		setProvince("");
		setTimeZone("");
		
		width = 1920;
		height = 1080;
	}
	
	public int getDisplayStatus() {
		if (blockExpiryDate != null) {
			return DISPLAY_BLOCKED;
		}
		else if (lastConnectionDate != null && lastConnectionDate.getTime() != 0) {
			if (playerStatus != 0) {
				return DISPLAY_ERROR;
			}
			else if (connected) {
				return DISPLAY_ONLINE;
			}	
			else {
				return DISPLAY_OFFLINE;
			}
		}
		else {
			return DISPLAY_NOT_INSTALLED;
		}	
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setChangedBy(String changedBy) {
		this.changedBy = changedBy;
	}

	public String getChangedBy() {
		return changedBy;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}

	public Date getChangeDate() {
		return changeDate;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public int getSubscriptionStatus() {
		return subscriptionStatus;
	}

	public String getSubscriptionStatusName() {
		if (DisplayStatus.ACTIVE == subscriptionStatus)
			return STATUSNAME_ACTIVE;
		else
			return STATUSNAME_INACTIVE;
	}

	public void setSubscriptionStatus(int subscriptionStatus) {
		this.subscriptionStatus = subscriptionStatus;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return address;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setAddressDescription(String addressDescription) {
		this.addressDescription = addressDescription;
	}

	public String getAddressDescription() {
		return addressDescription;
	}

	public void setUseCompanyAddress(boolean useCompanyAddress) {
		this.useCompanyAddress = useCompanyAddress;
	}

	public boolean getUseCompanyAddress() {
		return useCompanyAddress;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getWidth() {
		return width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getHeight() {
		return height;
	}
	
	public boolean getRestartEnabled() {
		return restartEnabled;
	}
	
	public void setRestartEnabled(boolean restartEnabled) {
		this.restartEnabled = restartEnabled;
	}
	
	public String getRestartTime() {
		return restartTime;
	}
	
	public void setRestartTime(String restartTime) {
		this.restartTime = restartTime;
	}
	
	public boolean isMonitoringEnabled() {
		return monitoringEnabled;
	}

	public void setMonitoringEnabled(boolean monitoringEnabled) {
		this.monitoringEnabled = monitoringEnabled;
	}

	public int getBrowserUpgradeMode() {
		return browserUpgradeMode;
	}
	
	public String getBrowserUpgradeModeString() {
		switch(browserUpgradeMode) {
		case BrowserUpgradeMode.CURRENT_VERSION:
			return DisplayInfo.BROWSER_CURRENT;
		case BrowserUpgradeMode.PREVIOUS_VERSION:
			return DisplayInfo.BROWSER_PREVIOUS;
		case BrowserUpgradeMode.USER_MANAGED:
			return DisplayInfo.BROWSER_USER_MANAGED;
		case BrowserUpgradeMode.AUTO:
		default: 
			return DisplayInfo.BROWSER_AUTOUPGRADE;
		}
	}

	public void setBrowserUpgradeMode(int browserUpgradeMode) {
		this.browserUpgradeMode = browserUpgradeMode;
	}

	public String getChromiumVersion() {
		return chromiumVersion;
	}

	public void setChromiumVersion(String chromiumVersion) {
		this.chromiumVersion = chromiumVersion;
	}

	public String getBrowserName() {
		return browserName;
	}

	public void setBrowserName(String browserName) {
		this.browserName = browserName;
	}

	public String getRecommendedBrowserVersion() {
		return recommendedBrowserVersion;
	}

	public void setRecommendedBrowserVersion(String recommendedBrowserVersion) {
		this.recommendedBrowserVersion = recommendedBrowserVersion;
	}

//	public String getInstallerVersion() {
//		return installerVersion;
//	}
//
//	public void setInstallerVersion(String installerVersion) {
//		this.installerVersion = installerVersion;
//	}
	
	public String getPlayerName() {
		return playerName;
	}
	
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	public String getPlayerVersion() {
		return playerVersion;
	}
	
	public void setPlayerVersion(String playerVersion) {
		this.playerVersion = playerVersion;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public void setCacheVersion(String cacheVersion) {
		this.cacheVersion = cacheVersion;
	}

	public String getCacheVersion() {
		return cacheVersion;
	}
	
	public void setViewerVersion(String viewerVersion) {
		this.viewerVersion = viewerVersion;
	}
	
	public String getViewerVersion() {
		return viewerVersion;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public Date getLastConnectionDate() {
		return lastConnectionDate;
	}

	public void setLastConnectionDate(Date lastConnectionDate) {
		this.lastConnectionDate = lastConnectionDate;
	}
	
	public int getPlayerStatus() {
		return playerStatus;
	}
	
	public void setPlayerStatus(int playerStatus) {
		this.playerStatus = playerStatus;
	}

	public Date getBlockExpiryDate() {
		return blockExpiryDate;
	}

	public void setBlockExpiryDate(Date blockExpiryDate) {
		this.blockExpiryDate = blockExpiryDate;
	}
	
	
}