package com.risevision.ui.server.rpc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

import com.risevision.core.api.attributes.CommonAttribute;
import com.risevision.core.api.attributes.GadgetAttribute;
import com.risevision.ui.client.common.exception.ServiceFailedException;
import com.risevision.ui.client.common.info.GadgetInfo;
import com.risevision.ui.client.common.info.RpcResultInfo;
import com.risevision.ui.client.common.service.GadgetService;
import com.risevision.ui.server.RiseRemoteServiceServlet;
import com.risevision.ui.server.utils.ServerUtils;

import org.restlet.data.Form;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GadgetServiceImpl extends RiseRemoteServiceServlet implements GadgetService {

//	public GadgetsInfo getGadgets(GadgetsInfo gadgetsInfo) throws ServiceFailedException {
//		String bookmark = null;
//
//		//PART 1 - get bookmarks
//		String bookmarkUrl = createGadgetsBookmarkResource(gadgetsInfo);
//		Document d1;
//
//		d1 = get(bookmarkUrl);
//		if (d1 != null)
//			bookmark = ServerUtils.getGridBookmark(d1, gadgetsInfo);
//		
//		//PART 2 - get gadgets
//		if (bookmark != null)
//		{
//			String pageUrl = createGadgetsResource(gadgetsInfo.getCompanyId(), bookmark);
//			Document d;
//
//			d = get(pageUrl);
//
//			if (d != null) {
//				ArrayList<GadgetInfo> gadgets = parseGetGadgets(d);
//				gadgetsInfo.setGadgets(gadgets);
//				return gadgetsInfo;
//			}
//		}		
//		gadgetsInfo.setGadgets(null);
//		return gadgetsInfo;
//	}

//	public GadgetsInfo getSharedGadgets(GadgetsInfo gadgetsInfo) throws ServiceFailedException {
//		String bookmark = null;
//
//		//PART 1 - get bookmarks
//		String bookmarkUrl = createSharedGadgetsBookmarkResource(gadgetsInfo);
//		Document d1;
//
//		d1 = get(bookmarkUrl);
//		if (d1 != null)
//			bookmark = ServerUtils.getGridBookmark(d1, gadgetsInfo);
//		
//		//PART 2 - get gadgets
//		if (bookmark != null)
//		{
//			String pageUrl = createSharedGadgetsResource(gadgetsInfo.getCompanyId(), bookmark);
//			Document d;
//			
//			d = get(pageUrl);
//
//			if (d != null) {
//				ArrayList<GadgetInfo> gadgets = parseGetGadgets(d);
//				gadgetsInfo.setGadgets(gadgets);
//				return gadgetsInfo;
//			}
//		}		
//
//		gadgetsInfo.setGadgets(null);
//		return gadgetsInfo;
//	}

	public GadgetInfo getGadget(String companyId, String gadgetId) throws ServiceFailedException {
		
		String url = createGadgetResource(companyId, gadgetId);

		Document d = get(url);
		GadgetInfo gadget = null;
		if (d != null)
			gadget = DocToGadget(d);
		return gadget;		
	}
	
	public GadgetInfo getGadget(String gadgetId) throws ServiceFailedException {
		
		String url = createGadgetResource(gadgetId);

		Document d = get(url);
		GadgetInfo gadget = null;
		if (d != null)
			gadget = DocToGadget(d);
		return gadget;		
	}
	
	public RpcResultInfo putGadget(String companyId, GadgetInfo gadget) throws ServiceFailedException {
		// new user- generate userId
		if ((gadget.getId() == null) || (gadget.getId().isEmpty()))
			gadget.setId(UUID.randomUUID().toString());
		
//		if ((gadget.getId() == null) || (gadget.getId().isEmpty())) {
//			generateTestGadgetRecords(companyId, 903);
//			return null;			
//		}

		String url = createGadgetResource(companyId, gadget.getId());

		Form form = new Form();

		form.add(GadgetAttribute.ID, gadget.getId());
		form.add(GadgetAttribute.NAME, gadget.getName());
		form.add(GadgetAttribute.GADGET_TYPE, gadget.getType());
		form.add(GadgetAttribute.URL, gadget.getUrl());
		form.add(GadgetAttribute.UI_URL, gadget.getUiUrl());
		form.add(GadgetAttribute.HELP_URL, gadget.getHelpUrl());
		
		form.add(GadgetAttribute.SCREENSHOT_URL, gadget.getScreenshotUrl());
		form.add(GadgetAttribute.THUMBNAIL_URL, gadget.getThumbnailUrl());
		form.add(GadgetAttribute.DESCRIPTION, gadget.getDescription());
		form.add(GadgetAttribute.AUTHOR, gadget.getAuthorName());
		form.add(GadgetAttribute.AUTHOR_URL, gadget.getAuthorUrl());
		form.add(GadgetAttribute.CATEGORY, gadget.getCategory());
		form.add(GadgetAttribute.SHARED, ServerUtils.BooleanToStr(gadget.isShared()));
		
		put(url, form);

		return new RpcResultInfo(gadget.getId());
	}

	public RpcResultInfo deleteGadget(String companyId, String gadgetId) throws ServiceFailedException {
		// new user- generate userId
		if ((gadgetId == null) || (gadgetId.isEmpty()))
			return new RpcResultInfo("null","Gadget ID is null or empty");

		String url = createGadgetResource(companyId, gadgetId);

		delete(url);
		return new RpcResultInfo();
	}		
	
//	private ArrayList<GadgetInfo> parseGetGadgets(Document doc) {
//		ArrayList<GadgetInfo> gadgets = new ArrayList<GadgetInfo>();
//
//		try {
//			doc.getDocumentElement().normalize();
//
//			NodeList nodeList = doc.getElementsByTagName("gadget");
//
//			for (int i = 0; i < nodeList.getLength(); i++) {
//
//				Node fstNode = nodeList.item(i);
//
//				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
//					GadgetInfo gadget = new GadgetInfo();
//
//					Element fstElmnt = (Element) fstNode;
//
//					gadget.setId(ServerUtils.getNode(fstElmnt, GadgetAttribute.ID));
//					gadget.setName(ServerUtils.getNode(fstElmnt, GadgetAttribute.NAME));
//					gadget.setUrl(ServerUtils.getNode(fstElmnt, GadgetAttribute.URL));
//					
//					gadget.setScreenshotUrl(ServerUtils.getNode(fstElmnt, GadgetAttribute.SCREENSHOT_URL));
//					gadget.setThumbnailUrl(ServerUtils.getNode(fstElmnt, GadgetAttribute.THUMBNAIL_URL));
//					gadget.setDescription(ServerUtils.getNode(fstElmnt, GadgetAttribute.DESCRIPTION));
//					gadget.setAuthorName(ServerUtils.getNode(fstElmnt, GadgetAttribute.AUTHOR));
//					gadget.setAuthorUrl(ServerUtils.getNode(fstElmnt, GadgetAttribute.AUTHOR_URL));
//					gadget.setCategory(ServerUtils.getNode(fstElmnt, GadgetAttribute.CATEGORY));
//					
//					gadget.setShared(ServerUtils.StrToBoolean(ServerUtils.getNodeValue(fstElmnt, GadgetAttribute.SHARED)));
//					
//					gadgets.add(gadget);
//				}
//			}
//			return gadgets;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return null;
//	}

//	private String createGadgetsBookmarkResource(GadgetsInfo gadgetsInfo) {
//		//URI: https://rdncore.appspot.com/company/{companyId}/gadgets/pagemap?sortBy=sortByFieldName&searchFor=searchString&pageSize=recordsPerPage			
//		
//		String resourceUrl = "/company/" + gadgetsInfo.getCompanyId() + "/gadgets/pagemap?";
//		if (!RiseUtils.strIsNullOrEmpty(gadgetsInfo.getSortBy()))
//			resourceUrl = resourceUrl + "sortBy=" + gadgetsInfo.getSortBy() + "&";
//		else
//			resourceUrl = resourceUrl + "sortBy=name" + "&"; //sortBy is Required
//			
//		if (!RiseUtils.strIsNullOrEmpty(gadgetsInfo.getSearchFor())) {
//			resourceUrl = resourceUrl + "searchFor=" + gadgetsInfo.getSearchFor() + "&";
//		}
//		
//		resourceUrl = resourceUrl + "pageSize=" + Integer.toString(gadgetsInfo.getPageSize());
//
//		return resourceUrl;
//	}

//	private String createGadgetsResource(String companyId, String bookmark) {
//		//URI: https://rdncore.appspot.com/company/{companyId}/gadgets?bookmark=pageBookmark
//
//		return "/company/" + companyId + "/gadgets?bookmark=" + bookmark;
//	}
	
//	private String createSharedGadgetsBookmarkResource(GadgetsInfo gadgetsInfo) {
//		//URI: https://rdncore.appspot.com/company/{companyId}/sharedgadgets/pagemap?sortBy=sortByFieldName&searchFor=searchString&pageSize=recordsPerPage			
//		
//		String resourceUrl = "/company/" + gadgetsInfo.getCompanyId() + "/sharedgadgets/pagemap?";
//		if (!RiseUtils.strIsNullOrEmpty(gadgetsInfo.getSortBy()))
//			resourceUrl = resourceUrl + "sortBy=" + gadgetsInfo.getSortBy() + "&";
//		else
//			resourceUrl = resourceUrl + "sortBy=name" + "&"; //sortBy is Required
//			
//		if (!RiseUtils.strIsNullOrEmpty(gadgetsInfo.getSearchFor())) {
//			resourceUrl = resourceUrl + "searchFor=" + gadgetsInfo.getSearchFor() + "&";
//		}
//		
//		resourceUrl = resourceUrl + "pageSize=" + Integer.toString(gadgetsInfo.getPageSize());
//
//		return resourceUrl;
//	}

//	private String createSharedGadgetsResource(String companyId, String bookmark) {
//		//URI: https://rdncore.appspot.com/company/{companyId}/gadgets?bookmark=pageBookmark
//
//		return "/company/" + companyId + "/sharedgadgets?bookmark=" + bookmark;
//	}
	
	private String createGadgetResource(String companyId, String gadgetId) {
		//URI: https://rdncore.appspot.com/company/{companyId}/gadget/{gadgetId}
			
		return "/company/" + companyId + "/gadget/" + gadgetId;
	}
	
	private String createGadgetResource(String gadgetId) {
		//URI: https://rdncore.appspot.com/gadget/{gadgetId}
			
		return "/gadget/" + gadgetId;
	}
	
	private GadgetInfo DocToGadget(Document doc) {
		GadgetInfo gadget = new GadgetInfo();

		try {
			doc.getDocumentElement().normalize();

			Node rootNode = doc.getDocumentElement();

			if (rootNode.getNodeType() == Node.ELEMENT_NODE) {

				Element fstElmnt = (Element) rootNode;
				gadget.setId(ServerUtils.getNode(fstElmnt, GadgetAttribute.ID));
				gadget.setName(ServerUtils.getNode(fstElmnt, GadgetAttribute.NAME));
				gadget.setType(ServerUtils.getNode(fstElmnt, GadgetAttribute.GADGET_TYPE));
				gadget.setUrl(ServerUtils.getNode(fstElmnt, GadgetAttribute.URL));
				gadget.setUiUrl(ServerUtils.getNode(fstElmnt, GadgetAttribute.UI_URL));
				gadget.setHelpUrl(ServerUtils.getNode(fstElmnt, GadgetAttribute.HELP_URL));
				
				gadget.setScreenshotUrl(ServerUtils.getNode(fstElmnt, GadgetAttribute.SCREENSHOT_URL));
				gadget.setThumbnailUrl(ServerUtils.getNode(fstElmnt, GadgetAttribute.THUMBNAIL_URL));
				gadget.setDescription(ServerUtils.getNode(fstElmnt, GadgetAttribute.DESCRIPTION));
				gadget.setAuthorName(ServerUtils.getNode(fstElmnt, GadgetAttribute.AUTHOR));
				gadget.setAuthorUrl(ServerUtils.getNode(fstElmnt, GadgetAttribute.AUTHOR_URL));
				gadget.setCategory(ServerUtils.getNode(fstElmnt, GadgetAttribute.CATEGORY));
				
				gadget.setShared(ServerUtils.StrToBoolean(ServerUtils.getNodeValue(fstElmnt, GadgetAttribute.SHARED)));
				
				gadget.setChangedBy(ServerUtils.getNode(fstElmnt, CommonAttribute.CHANGED_BY));
				gadget.setChangeDate(ServerUtils.strToDate(ServerUtils.getNode(fstElmnt, CommonAttribute.CHANGE_DATE), null));
			}	
			return gadget;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
//	public void generateTestGadgetRecords(String companyId, int count) {
//		Random generator = new Random();
//		int maxRandomValue = 1000000;
//		for (int i=0; i<count; i++) {
//			System.out.println(Integer.toString(i));
//			GadgetInfo gadget = new GadgetInfo();
//			gadget.setId(UUID.randomUUID().toString());
//
//			gadget.setName("Gadget  "+generator.nextInt(maxRandomValue));
//
//			
//			gadget.setChangedBy("100918744358134810665");
//			gadget.setChangeDate(new Date(1000));
//			
//			try {
//				putGadget(companyId, gadget);
//			} catch (ServiceFailedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}

	public String getGadgetXml(String gadgetUrl) throws Exception {
		String result = "";
		if ((gadgetUrl != null) && (!gadgetUrl.isEmpty())) {
			try { 
	            URL url = new URL(gadgetUrl);
	            URLConnection urlConnection = url.openConnection(); 
	            urlConnection.setReadTimeout(20000);
	            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())); 
	            String line; 
	 
	            while ((line = reader.readLine()) != null) { 
	            	result += line + "\n";
	            } 
	            reader.close(); 
	 
	        } catch (MalformedURLException e) { 
				e.printStackTrace();
	        	throw new Exception("Malformed URL, " + e.getMessage());
	        } catch (IOException e) {
				e.printStackTrace();
	        	throw new Exception(e.getMessage());
	            //result = e.getMessage();
	        }
		}
		return result;
	}

}
