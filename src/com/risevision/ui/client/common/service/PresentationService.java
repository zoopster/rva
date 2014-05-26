package com.risevision.ui.client.common.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.risevision.common.client.info.PresentationInfo;
import com.risevision.ui.client.common.exception.ServiceFailedException;
import com.risevision.ui.client.common.info.RpcResultInfo;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("presentation")
public interface PresentationService extends RemoteService {
//	PresentationsInfo getPresentations(PresentationsInfo presentationsInfo) throws ServiceFailedException;
	PresentationInfo getPresentation(String companyId, String presentationId) throws ServiceFailedException;
	PresentationInfo getTemplate(String companyId, String presentationId) throws ServiceFailedException;
	PresentationInfo restorePresentation(String companyId, String presentationId) throws ServiceFailedException;
	RpcResultInfo putPresentation(String companyId, PresentationInfo presentation) throws ServiceFailedException;
	RpcResultInfo publishPresentation(String companyId, String presentationId) throws ServiceFailedException;
	RpcResultInfo deletePresentation(String companyId, String presentationId) throws ServiceFailedException;
//	RpcResultInfo copyTemplate(String companyId, String templateId) throws ServiceFailedException;
	//Placeholder API
//	RpcResultInfo deletePlaceholders(String companyId, String presentationId) throws ServiceFailedException; //return error if any
	//Placeholder API
//	RpcResultInfo putPlaceholder(String companyId, String presentationId, PlaceholderInfo placeholder) throws ServiceFailedException;
}
