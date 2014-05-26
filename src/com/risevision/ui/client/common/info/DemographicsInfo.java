// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.info;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class DemographicsInfo implements Serializable {

	private String companyType;
	private List<String> servicesProvided;
	private List<String> marketSegments;
	private List<String> targetGroups;
//	private String viewsPerDisplay;
	private boolean advertisingRevenueEarn;
	private boolean advertisingRevenueInterested;
	
	public String getCompanyType() {
		return companyType;
	}
	
	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}
	
	public List<String> getServicesProvided() {
		return servicesProvided;
	}
	
	public void setServicesProvided(List<String> servicesProvided) {
		this.servicesProvided = servicesProvided;
	}
	
	public List<String> getMarketSegments() {
		return marketSegments;
	}
	
	public void setMarketSegments(List<String> marketSegments) {
		this.marketSegments = marketSegments;
	}
	
	public List<String> getTargetGroups() {
		return targetGroups;
	}
	
	public void setTargetGroups(List<String> targetGroups) {
		this.targetGroups = targetGroups;
	}
	
//	public String getViewsPerDisplay() {
//		return viewsPerDisplay;
//	}
	
//	public void setViewsPerDisplay(String viewsPerDisplay) {
//		this.viewsPerDisplay = viewsPerDisplay;
//	}
	
	public boolean isAdvertisingRevenueEarn() {
		return advertisingRevenueEarn;
	}
	
	public void setAdvertisingRevenueEarn(boolean advertisingRevenueEarn) {
		this.advertisingRevenueEarn = advertisingRevenueEarn;
	}
	
	public boolean isAdvertisingRevenueInterested() {
		return advertisingRevenueInterested;
	}

	public void setAdvertisingRevenueInterested(boolean advertisingRevenueInterested) {
		this.advertisingRevenueInterested = advertisingRevenueInterested;
	}
	
}
