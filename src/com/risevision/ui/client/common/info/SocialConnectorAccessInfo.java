// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.info;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SocialConnectorAccessInfo implements Serializable {
	
	private String id;
	private NetworkType networkType;
	private String name;
	private String value;
	private boolean isDefault;

	public enum NetworkType {
		foursquare("foursquare", "Foursquare", 
				"/foursquare/auth"),
		twitter("twitter", "Twitter",
				"/twitter/request")
				;
		
		private String networkName;
		private String defaultName;
		private String requestUrl;
		private NetworkType(String networkName, String defaultName, String requestUrl) {
			this.networkName = networkName;
			this.defaultName = defaultName;
			this.requestUrl = requestUrl;
		}
		
		@Override
		public String toString() {
			return networkName;
		}
		
		public String getDefaultName() {
			return defaultName;
		}
		
		public String getRequestUrl() {
			return requestUrl;
		}
		
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public void setNetworkType(NetworkType networkType) {
		this.networkType = networkType;
	}

	public NetworkType getNetworkType() {
		return networkType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
}
