package com.risevision.ui.client.common.widgets.mediaLibrary;

import com.google.gwt.user.client.Command;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.ui.client.common.controller.ConfigurationController;
import com.risevision.ui.client.common.controller.SelectedCompanyController;
import com.risevision.ui.client.common.info.WidgetSettingsInfo;

public class StorageAppWidget {
	
	private static StorageAppWidget instance;
	
	private WidgetSettingsInfo widgetSettings;
	
	public StorageAppWidget() {
//		initWidget();
	}
		
	public static StorageAppWidget getInstance() {
		try {
			if (instance == null)
				instance = new StorageAppWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}	
	
	public void load() {
	}
	
	public void show(Command selectCommand){
//		this.selectCommand = selectCommand;
				
//		initWidget();
	
		String widgetUrl = ConfigurationController.getInstance().getConfiguration().getMediaLibraryURL() + "/modal.html#/files/" + SelectedCompanyController.getInstance().getSelectedCompanyId();
		widgetSettings = new WidgetSettingsInfo(widgetUrl, "");
		
		StorageFrameWidget.getInstance().show(selectCommand, widgetSettings);
		
	}	
	
//	private void initWidget() {
//		boolean isEnabled = SelectedCompanyController.getInstance().isSelectedCompanyFeature(EnabledFeaturesInfo.FEATURE_MEDIA_LIBRARY);
//
//	}
	
	public String getItemUrl() {
		if (widgetSettings != null && !RiseUtils.strIsNullOrEmpty(widgetSettings.getWidgetUrl())) {
			return widgetSettings.getWidgetUrl();
		}
		
		return "";
	}
	


}