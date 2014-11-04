// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.server.rpc;

import java.util.UUID;
import java.util.logging.Level;

import com.risevision.common.client.info.PresentationInfo;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.core.api.Global;
import com.risevision.core.api.attributes.CommonAttribute;
import com.risevision.core.api.attributes.PresentationAttribute;
import com.risevision.ui.client.common.exception.ServiceFailedException;
import com.risevision.ui.client.common.info.RpcResultInfo;
import com.risevision.ui.client.common.service.PresentationService;
import com.risevision.ui.server.RiseRemoteServiceServlet;
import com.risevision.ui.server.utils.ServerUtils;

import org.apache.commons.lang.StringEscapeUtils;
import org.restlet.data.Form;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class PresentationServiceImpl extends RiseRemoteServiceServlet implements PresentationService {

//	public PresentationsInfo getPresentations(PresentationsInfo presentationsInfo) throws ServiceFailedException {
//		String bookmark = null;
//
//		//PART 1 - get bookmarks
//		String bookmarkUrl;
//		if (!presentationsInfo.getIsTemplate())
//			bookmarkUrl = createPresentationsBookmarksResource(presentationsInfo);
//		else
//			bookmarkUrl = createTemplatesBookmarksResource(presentationsInfo);
//		Document d1;
//
//		d1 = get(bookmarkUrl);
//		if (d1 != null)
//			bookmark = ServerUtils.getGridBookmark(d1, presentationsInfo);
//
//		//PART 2 - get presentations
//		if (bookmark != null)
//		{
//			String pageUrl;
//			if (!presentationsInfo.getIsTemplate())
//				pageUrl = createPresentationsResource(presentationsInfo.getCompanyId(), bookmark);
//			else 
//				pageUrl = createTemplatesResource(presentationsInfo.getCompanyId(), bookmark);
//			Document d;
//
//			d = get(pageUrl);
//
//			if (d != null) {
//				ArrayList<PresentationInfo> presentations = parseGetPresentations(d, presentationsInfo.getIsTemplate());
//				presentationsInfo.setPresentations(presentations);
//				return presentationsInfo;
//			}
//		}		
//		presentationsInfo.setPresentations(null);
//		return presentationsInfo;
//	}
	
	public PresentationInfo getPresentation(String companyId, String presentationId) throws ServiceFailedException {
		String url = createPresentationResource(companyId, presentationId);

		Document d = get(url);
		PresentationInfo presentation = null;
		if (d != null)
			presentation = parsePresentation(d);
		return presentation;		
	}
	
	public PresentationInfo getTemplate(String companyId, String presentationId) throws ServiceFailedException {
		String url = createTemplateResource(companyId, presentationId);

		Document d = get(url);
		PresentationInfo presentation = null;
		if (d != null)
			presentation = parsePresentation(d);
		return presentation;		
	}
	
	public PresentationInfo restorePresentation(String companyId, String presentationId) throws ServiceFailedException {
		String url = createRestoreResource(companyId, presentationId);

		Document d = get(url);
		PresentationInfo presentation = null;
		if (d != null)
			presentation = parsePresentation(d);
		return presentation;		
	}
	
	public RpcResultInfo putPresentation(String companyId, PresentationInfo presentation) throws ServiceFailedException {
		// generate Id
		if ((presentation.getId() == null) || (presentation.getId().isEmpty()))
			presentation.setId(UUID.randomUUID().toString());
		String url = createPresentationResource(companyId, presentation.getId());

		Form form = new Form();

		form.add(PresentationAttribute.ID, presentation.getId());
		form.add(PresentationAttribute.NAME, presentation.getName());

		if (presentation.getLayout() == null || presentation.getLayout().isEmpty()) {
			log.log(Level.SEVERE, "HTML data missing");
			return new RpcResultInfo(presentation.getId(), "HTML data missing, click <a onClick='window.location.reload()' style='font-weight:bold; cursor:pointer; text-decoration:underline;'>reload</a> here to refresh the page and try again");
		}
		
        form.add(PresentationAttribute.LAYOUT, StringEscapeUtils.escapeXml(presentation.getLayout()));
        form.add(PresentationAttribute.EMBEDDED_IDS, RiseUtils.listToString(presentation.getEmbeddedIds(), ","));
//        form.add(PresentationAttribute.PUBLISH, Integer.toString(presentation.getPublishType())); 
        form.add(PresentationAttribute.TEMPLATE, presentation.isTemplate() ? Global.TRUE: Global.FALSE);
        form.add(PresentationAttribute.DISTRIBUTION, presentation.getDistributionString());

        put(url, form);
		
		return new RpcResultInfo(presentation.getId());
	}
	
	public RpcResultInfo publishPresentation(String companyId, String presentationId) throws ServiceFailedException {
		String url = createPublishResource(companyId, presentationId);

        put(url, new Form());
		
		return new RpcResultInfo(presentationId);	
	}

	public RpcResultInfo deletePresentation(String companyId, String presentationId) throws ServiceFailedException {
		// new user- generate userId
		if ((presentationId == null) || (presentationId.isEmpty()))
			return new RpcResultInfo("null","Presentation ID is null or enmty");

		String url = createPresentationResource(companyId, presentationId);

		delete(url);
		
		// 409 status means Presentation is used in a Schedule Item
//		if (status == 409){
//			return new RpcResultInfo("null", "409");
//		}
		
		return new RpcResultInfo();
	}
	
//	public RpcResultInfo copyTemplate(String companyId, String templateId) throws ServiceFailedException {
//		String presentationId = UUID.randomUUID().toString();
//
//		String url = createTemplateCopyResource(companyId, presentationId, templateId);
//
//		try {
//			put(url, new Form(), 0);
//		} catch (ServiceFailedException e) {
//			if (e.getReason() == ServiceFailedException.CONFLICT) {
//				return new RpcResultInfo("null", "409");
//			}
//			else {
//				throw e;
//			}
//		}
//		
//		return new RpcResultInfo(presentationId);
//	}
	
//	public RpcResultInfo putPlaceholder(String companyId, String presentationId, PlaceholderInfo placeholder) throws ServiceFailedException {
//		String url = createPlaceholderResource(companyId, presentationId, placeholder.getId());
//
//		Form form = new Form();
//
//		form.add(PlaceholderAttribute.NAME, placeholder.getId());
//		form.add(PlaceholderAttribute.TYPE, placeholder.getType());
//		form.add(PlaceholderAttribute.OBJECT_REFERENCE, placeholder.getObjectRef());
//
//		put(url, form);
//		
//		return new RpcResultInfo(placeholder.getId());
//	}
	
	//delete all placeholders
//	public RpcResultInfo deletePlaceholders(String companyId, String presentationId) throws ServiceFailedException {
//		String url = createPlaceholdersResource(companyId, presentationId);
//
//		delete(url);
//		return new RpcResultInfo();
//	}

	private String createPresentationResource(String companyId, String presentationId) {
		//URI: https://rdncore.appspot.com/company/{companyId}/presentation/{presentationId}
			
		return "/company/" + companyId + "/presentation/" + presentationId;
	}
	
	private String createTemplateResource(String companyId, String presentationId) {
		//URI: https://rdncore.appspot.com/company/{companyId}/template/{presentationId}
			
		return "/company/" + companyId + "/template/" + presentationId;
	}
	
	private String createRestoreResource(String companyId, String presentationId) {
		//URI: https://rdncore.appspot.com/company/{companyId}/presentation/{presentationId}/restore
			
		return "/company/" + companyId + "/presentation/" + presentationId + "/restore";
	}
	
	private String createPublishResource(String companyId, String presentationId) {
		//URI: https://rdncore.appspot.com/company/{companyId}/presentation/{presentationId}/publish
			
		return "/company/" + companyId + "/presentation/" + presentationId + "/publish";
	}
	
//	private String createPresentationsBookmarksResource(PresentationsInfo presentationsInfo) {
//		//URI: https://rdncore.appspot.com/company/{companyId}/presentations?bookmark=pageBookmark
//			
//		String resourceUrl = "/company/" + presentationsInfo.getCompanyId() + "/presentations/pagemap?";
//		if (!RiseUtils.strIsNullOrEmpty(presentationsInfo.getSortBy()))
//			resourceUrl = resourceUrl + "sortBy=" + presentationsInfo.getSortBy() + "&";
//		else
//			resourceUrl = resourceUrl + "sortBy=name" + "&"; //sortBy is Required
//			
//		if (presentationsInfo.getSearchFor() != null){
//			resourceUrl = resourceUrl + "searchFor=" + presentationsInfo.getSearchFor() + "&";
//		}
//		
//		resourceUrl = resourceUrl + "pageSize=" + Integer.toString(presentationsInfo.getPageSize());
//
//		return resourceUrl;
//	}
	
//	private String createPresentationsResource(String companyId, String bookmark) {
//		//URI: https://rdncore.appspot.com/company/{companyId}/presentations?bookmark=pageBookmark
//
//		return "/company/" + companyId + "/presentations?bookmark=" + bookmark;
//	}
	
//	private String createTemplatesBookmarksResource(PresentationsInfo presentationsInfo) {
//		//URI: https://rdncore.appspot.com/company/{companyId}/templates?bookmark=pageBookmark
//			
//		String resourceUrl = "/company/" + presentationsInfo.getCompanyId() + "/templates/pagemap?";
//		if (!RiseUtils.strIsNullOrEmpty(presentationsInfo.getSortBy()))
//			resourceUrl = resourceUrl + "sortBy=" + presentationsInfo.getSortBy() + "&";
//		else
//			resourceUrl = resourceUrl + "sortBy=name" + "&"; //sortBy is Required
//			
//		if (presentationsInfo.getSearchFor() != null){
//			resourceUrl = resourceUrl + "searchFor=" + presentationsInfo.getSearchFor() + "&";
//		}
//		
//		resourceUrl = resourceUrl + "pageSize=" + Integer.toString(presentationsInfo.getPageSize());
//
//		return resourceUrl;
//	}
	
//	private String createTemplatesResource(String companyId, String bookmark) {
//		//URI: https://rdncore.appspot.com/company/{companyId}/templates?bookmark=pageBookmark
//
//		return "/company/" + companyId + "/templates?bookmark=" + bookmark;
//	}

//	private String createPlaceholderResource(String companyId, String presentationId, String placeholderId) {
//		//URI: https://rdncore.appspot.com/company/{companyId}/presentation/{presentationId}/placeholder/{placeholderId}
//			
//		return "/company/" + companyId + "/presentation/" + presentationId + "/placeholder/" + placeholderId;
//	}

//	private String createPlaceholdersResource(String companyId, String presentationId) {
//		//URI: https://rdncore.appspot.com/company/{companyId}/presentation/{presentationId}/placeholders
//			
//		return "/company/" + companyId + "/presentation/" + presentationId + "/placeholders";
//	}
	
//	private String createTemplateCopyResource(String companyId, String presentationId, String templateId) {
//		// URI: https://SERVER/v1/company/{companyId}/presentation/{presentationId}/from/{templateId}?appId={appId}
//		
//		return "/company/" + companyId + "/presentation/" + presentationId + "/from/" + templateId;
//	}
	
	private PresentationInfo parsePresentation(Document doc) {
		PresentationInfo presentation = new PresentationInfo();

		try {
			doc.getDocumentElement().normalize();

			Node rootNode = doc.getDocumentElement();

			if (rootNode.getNodeType() == Node.ELEMENT_NODE) {

				Element fstElmnt = (Element) rootNode;
				presentation.setId(ServerUtils.getNode(fstElmnt, PresentationAttribute.ID));
				presentation.setName(ServerUtils.getNode(fstElmnt, PresentationAttribute.NAME));
		        		
//				presentation.setLayout(ServerUtils.getNode(fstElmnt, PresentationAttribute.LAYOUT));
				presentation.setLayout(StringEscapeUtils.unescapeXml(ServerUtils.getNode(fstElmnt, PresentationAttribute.LAYOUT)));
//				presentation.setPublishType(ServerUtils.strToInt(ServerUtils.getNode(fstElmnt, PresentationAttribute.PUBLISH), 0));
				presentation.setTemplate(ServerUtils.getNode(fstElmnt, PresentationAttribute.TEMPLATE).equals(Global.TRUE));
				presentation.setRevisionStatus(ServerUtils.strToInt(ServerUtils.getNode(fstElmnt, PresentationAttribute.REVISION_STATUS), 0));
				presentation.setDistributionString(ServerUtils.getNode(fstElmnt, PresentationAttribute.DISTRIBUTION));
				
				presentation.setChangedBy(ServerUtils.getNode(fstElmnt, CommonAttribute.CHANGED_BY));
				presentation.setChangeDate(ServerUtils.strToDate(ServerUtils.getNode(fstElmnt, CommonAttribute.CHANGE_DATE), null));

//				presentation.setPlaceholders(parsePlaceholders(fstElmnt));
			}	
			return presentation;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
//	private ArrayList<PresentationInfo> parseGetPresentations(Document doc, boolean isTemplate) {
//		ArrayList<PresentationInfo> presentations = new ArrayList<PresentationInfo>();
//
//		try {
//			String parentNode;
//			if (!isTemplate) 
//				parentNode = Entity.PRESENTATION;
//			else
//				parentNode = Entity.TEMPLATE;
//			
//			doc.getDocumentElement().normalize();
//
//			NodeList nodeList = doc.getElementsByTagName(parentNode);
//
//			for (int i = 0; i < nodeList.getLength(); i++) {
//
//				Node fstNode = nodeList.item(i);
//
//				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
//					PresentationInfo c = new PresentationInfo();
//
//					Element fstElmnt = (Element) fstNode;
//
//					c.setId(ServerUtils.getNode(fstElmnt, PresentationAttribute.ID));
//					c.setName(ServerUtils.getNode(fstElmnt, PresentationAttribute.NAME));
//					if (ServerUtils.getNode(fstElmnt, PresentationAttribute.TEMPLATE) != null)
//						c.setTemplate(ServerUtils.getNode(fstElmnt, PresentationAttribute.TEMPLATE).equals(Global.TRUE));
//					if (ServerUtils.getNode(fstElmnt, PresentationAttribute.REVISION_STATUS) != null)
//						c.setRevisionStatus(ServerUtils.strToInt(ServerUtils.getNode(fstElmnt, PresentationAttribute.REVISION_STATUS), 0));
//					presentations.add(c);
//				}
//			}
//			return presentations;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return null;
//	}
	
//	private ArrayList<PlaceholderInfo> parsePlaceholders(Node parentNode) {
//		
//		ArrayList<PlaceholderInfo> placeholders = new ArrayList<PlaceholderInfo>();
//		
//		Node distributionNode = ServerUtils.findFirstNode(parentNode, PresentationAttribute.PLACEHOLDERS);
//		if (distributionNode != null) {
//			NodeList nodeList = distributionNode.getChildNodes();
//			if (nodeList != null) {
//				for (int i = 0; i < nodeList.getLength(); i++) {
//					Node node = nodeList.item(i);
//					if ((node.getNodeType() == Node.ELEMENT_NODE) && (PresentationAttribute.PLACEHOLDER.equals(node.getNodeName()))) {
//						PlaceholderInfo placeholder = new PlaceholderInfo();
//						Element element = (Element)node;
//
//						placeholder.setId(ServerUtils.getNodeValue(element, PlaceholderAttribute.ID));
//						placeholder.setType(ServerUtils.getNodeValue(element, PlaceholderAttribute.TYPE));
//						placeholder.setObjectRef(ServerUtils.getNodeValue(element, PlaceholderAttribute.OBJECT_REFERENCE));
//						
//						placeholders.add(placeholder);
//					}
//				}
//			}
//		}
//		
//		return placeholders;
//	}

}
