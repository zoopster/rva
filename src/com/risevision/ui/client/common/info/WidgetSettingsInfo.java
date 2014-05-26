// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.info;

import com.risevision.common.client.utils.RiseUtils;

public class WidgetSettingsInfo {
	
//	private boolean isCustomUI;
	
//	private HashMap<String, WidgetSettingInfo> widgetSettings = new HashMap<String, WidgetSettingInfo>();
	
	private String widgetUrl;
	private String widgetUiUrl;
	
	private String additionalParams;
	
	public WidgetSettingsInfo(String gadgetUrl, String additionalParams) {
		if (gadgetUrl != null)
			gadgetUrl = gadgetUrl.trim();
		this.widgetUrl = gadgetUrl;
		this.additionalParams = additionalParams;
	}
	
	public void setAdditionalParams(String additionalParams) {
		this.additionalParams = additionalParams;
	}
	
	public String getAdditionalParams() {
		return additionalParams;
	}
	
	public void setWidgetUIUrl(String uiUrl) {
		this.widgetUiUrl = uiUrl;
		
//		parseUrl();
	}
			
//	private boolean isRiseProperty(String propertyName) {
//		String[] sa = {"rsA","rsS"};
//		for (String s:sa)
//			if (s.equals(propertyName))
//				return true;
//		return false;
//	}

//	public boolean isCustomUI() {
//		return isCustomUI;
//	}
	
//	private void parseUrl() {
//		String xmlUrl = getWidgetHtmlUrl();
//		
//		if (widgetUrl.length() > xmlUrl.length()) {
//			String gadgetUrlParams = widgetUrl.substring(xmlUrl.length() + 1);
//					 
//			updateGadgetSettings(gadgetUrlParams);
//		}
//			
//		for (GadgetSetting g: gadgetSettings) {
//			g.setValue(urlParams.get("up_" + g.getName()));
//		}
//	}
	
//	public void updateGadgetSettings(String gadgetUrlParams) {
//		try {
//
//			String[] ray = gadgetUrlParams.split("&");
//			for (int i = 0; i < ray.length; i++) {
//				String[] substrRay = ray[i].split("=");
//				
//				if (substrRay.length > 0) {
//					String name = substrRay[0], value = "";
//					
//					if (name.indexOf("up_") != -1) {
//						name = name.substring(3);
//					}
//					
//					if (substrRay.length == 2) {
//						value = substrRay[1];
//					}
//	
//					if (widgetSettings.get(name) != null) {
//						widgetSettings.get(name).setValue(value);
//					}
//					else {
//						WidgetSettingInfo setting = new WidgetSettingInfo(name, value);
//						widgetSettings.put(name, setting);
//					}
//				}
//			}
//		} catch(Exception ex) {}
//
//	}
	
	public String getWidgetUrl() {
//		if (!isCustomUI) {
//			return (getWidgetHtmlUrl() + getUrlParams()).trim();
//		}
//		else {
//			return widgetUrl;
//		}
		
		return widgetUrl;
	}
	
	public String getWidgetUiUrl() {
		return (widgetUiUrl + getUrlParams()).trim();
	}
	
	private String getUrlParams() {
		String res = "";
		int queryParamsStartPos = widgetUrl.indexOf("?");
		if (queryParamsStartPos == -1) {
			queryParamsStartPos = widgetUrl.indexOf("&");
		}
		
		if (queryParamsStartPos > 0) {
			res = widgetUrl.substring(queryParamsStartPos);
		}
		
		return res;
	}
	
	private String getWidgetHtmlUrl(String url) {
		String res = "";
		int queryParamsStartPos = url.indexOf("?");
//		if (queryParamsStartPos == -1) {
//			queryParamsStartPos = url.indexOf("&");
//		}
		
		if (queryParamsStartPos > 0) {
			res = url.substring(0, queryParamsStartPos);
		}
		else if (!url.contains("&")) {
			res = url;
		}
		
		return res;
	}
	
//	private String getGadgetSettingsJsonString() {
//		ArrayList<JSOModel> values = new ArrayList<JSOModel>();
//		
//		for (GadgetSetting setting: gadgetSettings.values()) {
//			JSOModel value = JSOModel.create();
//			value.set(setting.getName(), setting.getDefaultValue());
//			values.add(value);
//		}
//		
//		String response = JSOModel.arrayToString(values); 
//		if (response.charAt(0) == '[') response = response.substring(1);
//		if (response.charAt(response.length() - 1) == ']') response = response.substring(0, response.length() - 1);
//		
//		return response;
//	}
	
	public void setParams(String params) {
		if (!RiseUtils.strIsNullOrEmpty(params)) {
			if (!RiseUtils.strIsNullOrEmpty(getWidgetHtmlUrl(params))) {
				widgetUrl = params;
				return;
			}
			
			if (params.charAt(0) == '&') {
				params = params.replaceFirst("&", "?");
			}
			if (params.charAt(0) != '?') {
				params = "?" + params;
			}
		}
		widgetUrl = getWidgetHtmlUrl(widgetUrl) + params;

	}
	
}
