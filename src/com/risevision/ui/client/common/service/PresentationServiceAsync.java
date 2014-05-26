package com.risevision.ui.client.common.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.risevision.common.client.info.PresentationInfo;
import com.risevision.ui.client.common.info.RpcResultInfo;

public interface PresentationServiceAsync {
//	void getPresentations(PresentationsInfo presentationsInfo, AsyncCallback<PresentationsInfo> callback);
	void getPresentation(String companyId, String presentationId, AsyncCallback<PresentationInfo> callback);
	void getTemplate(String companyId, String presentationId, AsyncCallback<PresentationInfo> callback);
	void restorePresentation(String companyId, String presentationId, AsyncCallback<PresentationInfo> callback);
	void putPresentation(String companyId, PresentationInfo presentation, AsyncCallback<RpcResultInfo> callback);
	void publishPresentation(String companyId, String presentationId, AsyncCallback<RpcResultInfo> callback);
	void deletePresentation(String companyId, String presentationId, AsyncCallback<RpcResultInfo> callback);
//	void copyTemplate(String companyId, String templateId, AsyncCallback<RpcResultInfo> callback);
	//Placeholder API
//	void deletePlaceholders(String companyId, String presentationId, AsyncCallback<RpcResultInfo> callback); //return error if any
	//Placeholder API
//	void putPlaceholder(String companyId, String presentationId, PlaceholderInfo placeholder, AsyncCallback<RpcResultInfo> callback);

}
