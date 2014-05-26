// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.presentation.common;

import java.util.ArrayList;

import com.google.gwt.core.client.JavaScriptObject;
import com.risevision.common.client.info.PresentationInfo;
import com.risevision.common.client.json.JSOModel;
import com.risevision.core.api.attributes.PresentationAttribute;
import com.risevision.ui.client.common.controller.ConfigurationController;
import com.risevision.ui.client.common.controller.SelectedCompanyController;

public class PresentationPreviewController {
	private static PresentationPreviewController instance;
	private JavaScriptObject presentationObject;
	private JavaScriptObject windowInstance;
	
	public static PresentationPreviewController getInstance() {
		try {
			if (instance == null)
				instance = new PresentationPreviewController();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}
	
	public PresentationPreviewController() {
		exportStaticMethods();	
	}
	
	private static native void exportStaticMethods() /*-{
		$wnd.getPreviewDataNative =
		$entry(@com.risevision.ui.client.presentation.common.PresentationPreviewController::getPreviewDataNative(Ljava/lang/String;));
		
//		debugger;
		
		$wnd.addEventListener("message", function(event) {
//			debugger;

			$wnd.getPreviewDataNative(event.origin);
		}, false);
	}-*/;

	public void launchPreview(PresentationInfo presentation) {
		presentationObject = createPresentationJSON(presentation);
		launchPreviewNative(ConfigurationController.getInstance().getConfiguration().getViewerURL() + "Viewer.html?type=preview");
	}
	
	private static native void launchPreviewNative(String url) /*-{
		this.@com.risevision.ui.client.presentation.common.PresentationPreviewController::windowInstance = window.open(url, "_blank", "");
	}-*/;
	
	private static void getPreviewDataNative(String eventSource) {
		if (ConfigurationController.getInstance().getConfiguration().getViewerURL().contains(eventSource))
			updatePreviewDataNative(eventSource, instance.presentationObject);
	}
	
	private static native void updatePreviewDataNative(String eventSource, JavaScriptObject presentationObject) /*-{
		debugger;
	
		var popup = this.@com.risevision.ui.client.presentation.common.PresentationPreviewController::windowInstance;
		if (popup) {
			popup.postMessage(presentationObject, eventSource);
		}	
	}-*/;

	private static JSOModel createPresentationJSON(PresentationInfo presentation) {
		if (presentation == null)
			return null;
		
		try {		
			// make a copy of the object
			JSOModel jsPresentation = updateJsPresentation(presentation);
			
			JSOModel jsContent = JSOModel.create();
			
			ArrayList<JSOModel> jsPresentations = new ArrayList<JSOModel>();
			jsPresentations.add(jsPresentation);			

			jsContent.setArray("presentations", jsPresentations);
			
			JSOModel jsObject = JSOModel.create();
			jsObject.set("content", jsContent);

			return jsObject;
		}
		catch (Exception e) {
			return null;
		}
	}
	
	private static JSOModel updateJsPresentation(PresentationInfo presentation) {
		JSOModel jsPresentation = JSOModel.create();
		
		jsPresentation.set(PresentationAttribute.ID, presentation.getId());
		jsPresentation.set(PresentationAttribute.NAME, presentation.getName());
		jsPresentation.set("companyId", SelectedCompanyController.getInstance().getSelectedCompanyId());
		jsPresentation.set(PresentationAttribute.PUBLISH, presentation.getPublishType());
		jsPresentation.set(PresentationAttribute.TEMPLATE, presentation.isTemplate());
		
		jsPresentation.set(PresentationAttribute.LAYOUT, presentation.getLayout());
		
		return jsPresentation;
	}
}
