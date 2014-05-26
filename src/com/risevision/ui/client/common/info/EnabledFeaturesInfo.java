// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.info;

import java.util.ArrayList;

import com.google.gwt.core.client.JsArray;
import com.risevision.common.client.json.JSOModel;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.ui.client.common.controller.SelectedCompanyController;

public class EnabledFeaturesInfo {
	public static final String NAME_ATTRIBUTE = "name";
	public static final String VALUE_ATTRIBUTE = "value";
	public static final String FEATURE_MEDIA_LIBRARY = "Media Library";
	
	private ArrayList<String> enabledFeaturesList = new ArrayList<String>();
	
	public EnabledFeaturesInfo() {
		
	}
	
	public EnabledFeaturesInfo(String enabledFeaturesString) {
		setEnabledFeaturesString(enabledFeaturesString);
	}
	
	public void addEnabledFeature(String feature) {
		enabledFeaturesList.add(feature);
	}
	
	public void removeEnabledFeature(String feature) {
		enabledFeaturesList.remove(feature);
	}
	
	public boolean isFeatureEnabled(String selectedFeature) {
		if (SelectedCompanyController.getInstance().getSelectedCompany().isRise()) {
			return true;
		}
		
		for (String feature: enabledFeaturesList) {
			if (feature.equals(selectedFeature)) {
				return true;
			}
		}
		return false;
	}
	
	public void setEnabledFeaturesString(String enabledFeaturesString) {	
		try {		
			if (!RiseUtils.strIsNullOrEmpty(enabledFeaturesString)) {
				JsArray<JSOModel> jsEnabledFeatures = JSOModel.arrayFromJson(enabledFeaturesString);
		
				parseJsEnabledFeatures(jsEnabledFeatures);
			}
		}
		catch (Exception e) {
		}

	}
	
	private void parseJsEnabledFeatures(JsArray<JSOModel> jsFeatures) {	
		enabledFeaturesList.clear();
		
		if (jsFeatures != null) {	
			for (int i = 0; i < jsFeatures.length(); i++) {
				String feature = jsFeatures.get(i).get(NAME_ATTRIBUTE);
				
				if (!RiseUtils.strIsNullOrEmpty(feature)) {
					enabledFeaturesList.add(feature);
				}	
			}
		}
	}
	
	public String getEnabledFeaturesString() {
		try {
			ArrayList<JSOModel> jsEnabledFeatures = updateJsEnabledFeatures();
			
			return JSOModel.arrayToString(jsEnabledFeatures);
		}
		catch (Exception e) {
			return null;
		}
	}
	
	private ArrayList<JSOModel> updateJsEnabledFeatures() {
		ArrayList<JSOModel> jsEnabledFeatures = new ArrayList<JSOModel>();
		
		if (enabledFeaturesList != null) {
			for (String feature : enabledFeaturesList) {
				JSOModel jsFeature = JSOModel.create();
				jsFeature.set(NAME_ATTRIBUTE, feature);
				jsFeature.set(VALUE_ATTRIBUTE, "true");
				
				jsEnabledFeatures.add(jsFeature);
			}
		}
		
		return jsEnabledFeatures;
	}

}
