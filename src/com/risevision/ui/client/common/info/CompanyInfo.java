// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.info;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.risevision.common.client.utils.RiseUtils;
import com.risevision.core.api.attributes.CompanyAttribute;
import com.risevision.core.api.types.CompanyNetworkOperatorStatus;
import com.risevision.core.api.types.CompanyStatus;
import com.risevision.ui.client.common.controller.ConfigurationController;
import com.risevision.ui.client.common.lists.SearchSortable;


@SuppressWarnings("serial")
public class CompanyInfo implements Serializable, SearchSortable {
	
	public static String DEFAULT_TIMEZONE = "";
	public static String DEFAULT_COUNTRY = "";
	public static String DEFAULT_PROVINCE = "";
//	public static String PNOSTATUS_NO = "0";
//	public static String PNOSTATUS_SUBSCRIBED = "1";
//	public static String PNOSTATUS_CANCELLED = "2";
//	public static String CUSTOMER_ACTIVE = "1";
//	public static String CUSTOMER_INACTIVE = "0";
	
//	public static final String ID = "id";
//	public static final String NAME = "name";
//	public static final String STREET = "street";
//	public static final String UNIT = "unit";
//	public static final String CITY = "city";
//	public static final String PROVINCE = "province";
//	public static final String COUNTRY = "country";
//	public static final String POSTAL = "postalCode";
//	public static final String TIMEZONE = "timeZone";
//	public static final String TELEPHONE = "telephone";
//	public static final String FAX = "fax";
//	public static final String THROTTLE = "displayThrottle";
//	public static final String PARENT = "parentId";
//	public static final String SENDTOPARENT = "sendToParent";
//	public static final String REFERENCE = "pnoAccountReference";
//	public static final String STATUS = "pnoStatus";
//	public static final String CUSTOMER_STATUS = "customerStatus";
//	public static final String ROLES = "roles";
	
	private String id;
	private String name;
	private String address;
	
	private String street;
	private String unit;
	private String city;
	private String province;
	private String country;
	private String postalCode;
	private String timeZone;
	private String telephone;
	private String fax;

	// PNO-editable fields
	private String parentId;
	private int pnoStatus; //0=NO, 1=SUBSCRIBED, 2=CANCELLED
	private int customerStatus;
	private Date customerStatusChangeDate;
	
	private List<String> displayMonitorEmailRecipients;
	
	private String enabledFeaturesJson;
	
	private HashMap<String, String> settings;
	private HashMap<String, String> parentSettings;

	private String authKey;
	private String claimId;
	
	private AlertsInfo alertsSettings = new AlertsInfo();
	
	private DemographicsInfo demographicsInfo = new DemographicsInfo();

	private Date creationDate;
	
	private String changedBy;
	private Date changeDate;

	public CompanyInfo() {		
		this.setStreet("");
		this.setUnit("");
		this.setCity("");
		this.setCountry(DEFAULT_COUNTRY);
		this.setProvince(DEFAULT_PROVINCE);
		this.setPostalCode("");
		this.setTimeZone(DEFAULT_TIMEZONE);
		this.setTelephone("");
		this.setFax("");

		this.setPnoStatus(CompanyNetworkOperatorStatus.SUBSCRIBED);
		this.setCustomerStatus(CompanyStatus.ACTIVE);
	}

	public boolean isRise() {
		return ConfigurationController.getInstance().getConfiguration().getRiseId().equals(id);
		//return Globals.RISE_ID.equals(id);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return address;
	}
	
	public void setStreet(String street) {
		this.street = street;
	}

	public String getStreet() {
		return street;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUnit() {
		return unit;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCity() {
		return city;
	}

	public void setProvince(String province) {
		if (province == null)
			province = "";
		this.province = province;
	}

	public String getProvince() {
		return province;
	}

	public void setCountry(String country) {
		if (country == null)
			country = DEFAULT_COUNTRY;
		this.country = country;
	}

	public String getCountry() {
		return country;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getTimeZone() {
		if (country == null)
			country = DEFAULT_TIMEZONE;
		if (country.isEmpty())
			country = DEFAULT_TIMEZONE;
		return timeZone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getFax() {
		return fax;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentId() {
		return parentId;
	}

	public int getPnoStatus() {
		return pnoStatus;
	}

	public void setPnoStatus(int pnoStatus) {
		if (CompanyNetworkOperatorStatus.SUBSCRIBED == pnoStatus || CompanyNetworkOperatorStatus.CANCELLED == pnoStatus)
			this.pnoStatus = pnoStatus;
		else
			this.pnoStatus = CompanyNetworkOperatorStatus.NO;			
	}

	public boolean isPno() {
		return CompanyNetworkOperatorStatus.SUBSCRIBED == pnoStatus;
	}

	public void setChangedBy(String changedBy) {
		this.changedBy = changedBy;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
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

	public void setCustomerStatus(int customerStatus) {
		this.customerStatus = customerStatus;
	}

	public int getCustomerStatus() {
		return customerStatus;
	}

	public void setCustomerStatusChangeDate(Date customerStatusChangeDate) {
		this.customerStatusChangeDate = customerStatusChangeDate;
	}

	public Date getCustomerStatusChangeDate() {
		return customerStatusChangeDate;
	}

	public void setSettings(HashMap<String, String> settings) {
		this.settings = settings;
	}

	public HashMap<String, String> getSettings() {
		return settings;
	}

	public void setParentSettings(HashMap<String, String> parentSettings) {
		this.parentSettings = parentSettings;
	}

	public HashMap<String, String> getParentSettings() {
		if (parentSettings != null) {
			return parentSettings;
		}
		else { 
			return settings;
		}
	}
	
	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}
	
	public String getAuthKey() {
		return authKey;
	}
	
	public String getClaimId() {
		return claimId;
	}

	public void setClaimId(String claimId) {
		this.claimId = claimId;
	}

	public List<String> getDisplayMonitorEmailRecipients() {
		return displayMonitorEmailRecipients;
	}

	public void setDisplayMonitorEmailRecipients(List<String> displayMonitorEmailRecipients) {
		this.displayMonitorEmailRecipients = displayMonitorEmailRecipients;
	}

	public void setEnabledFeaturesJson(String enabledFeaturesJson) {
		this.enabledFeaturesJson = enabledFeaturesJson;
	}

	public String getEnabledFeaturesJson() {
		return enabledFeaturesJson;
	}
	
	public AlertsInfo getAlertsSettings() {
		return alertsSettings;
	}

	public void setAlertsSettings(AlertsInfo alertsSettings) {
		this.alertsSettings = alertsSettings;
	}
	
	public DemographicsInfo getDemographicsInfo() {
		return demographicsInfo;
	}

	public void setDemographicsInfo(DemographicsInfo demographicsInfo) {
		this.demographicsInfo = demographicsInfo;
	}

	public boolean search(String query) {
		if (!RiseUtils.strIsNullOrEmpty(this.name)) {
			return this.name.toLowerCase().contains(query.toLowerCase());
		}
		
		if (!RiseUtils.strIsNullOrEmpty(this.id)) {
			return this.id.toLowerCase().contains(query.toLowerCase());
		}
		
		return false;
	}
	
	public int compare(SearchSortable item, String column) {
		if (item instanceof CompanyInfo) {
			CompanyInfo company = (CompanyInfo) item;
			if (CompanyAttribute.NAME.equals(column)) {
				return company.name.toLowerCase().compareTo(this.name.toLowerCase());
			}
		}
		
		return -1;
	}
	
}