// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.server.rpc;

import com.risevision.core.api.attributes.CommonAttribute;
import com.risevision.core.api.attributes.DisplayAttribute;
import com.risevision.core.api.attributes.ViewerStatusAttribute;
import com.risevision.core.api.types.BrowserUpgradeMode;
import com.risevision.core.api.types.DisplayStatus;
import com.risevision.ui.client.common.exception.ServiceFailedException;
import com.risevision.ui.client.common.info.DisplayInfo;
import com.risevision.ui.client.common.info.RpcResultInfo;
import com.risevision.ui.client.common.service.DisplayService;
import com.risevision.ui.server.RiseRemoteServiceServlet;
import com.risevision.ui.server.utils.ServerUtils;

import org.apache.commons.lang.RandomStringUtils;
import org.restlet.data.Form;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class DisplayServiceImpl extends RiseRemoteServiceServlet implements DisplayService {

//	public DisplaysInfo getDisplays(DisplaysInfo displaysInfo, boolean activeOnly) throws ServiceFailedException {
//		String bookmark = null;
//
//		//PART 1 - get bookmarks
//		String bookmarkUrl = createDisplaysBookmarksResource(displaysInfo, activeOnly);
//		Document d1;
//
//		d1 = get(bookmarkUrl);
//		if (d1 != null)
//			bookmark = ServerUtils.getGridBookmark(d1, displaysInfo);
//		
//		//PART 2 - get displays
//		if (bookmark != null)
//		{
//			String pageUrl = createDisplaysResource(displaysInfo.getCompanyId(), bookmark);
//			Document d;
//
//			d = get(pageUrl);
//
//			if (d != null) {
//				ArrayList<DisplayInfo> displays = parseGetDisplays(d);
//				displaysInfo.setDisplays(displays);
//				return displaysInfo;
//			}
//		}		
//		displaysInfo.setDisplays(null);
//		return displaysInfo;
//	}

	public DisplayInfo getDisplay(String companyId, String displayId) throws ServiceFailedException {
		
		String url = createDisplayResource(companyId, displayId);

		Document d = get(url);
		DisplayInfo display = null;
		if (d != null)
			display = DocToDisplay(d);
		return display;		
	}
	
	public RpcResultInfo putDisplay(String companyId, DisplayInfo display) throws ServiceFailedException {
		// new user- generate userId
		if ((display.getId() == null) || (display.getId().isEmpty())) {
//			display.setId(UUID.randomUUID().toString());
			
			String id = RandomStringUtils.random(12, "23456789ABCDEFGHJKMNPQRSTUVWXYZ");
			
			display.setId(id);
		}
		
//		if ((display.getId() == null) || (display.getId().isEmpty())) {
//			generateTestDisplayRecords(companyId, 903);
//			return null;			
//		}

		String url = createDisplayResource(companyId, display.getId());

		Form form = new Form();

		form.add(DisplayAttribute.ID, display.getId());
		form.add(DisplayAttribute.NAME, display.getName());

		form.add(DisplayAttribute.ADDRESS_DESCRIPTION, display.getAddressDescription());
		form.add(DisplayAttribute.USE_COMPANY_ADDRESS, ServerUtils.BooleanToStr(display.getUseCompanyAddress()));
		form.add(DisplayAttribute.STREET, display.getStreet());
		form.add(DisplayAttribute.UNIT, display.getUnit());
		form.add(DisplayAttribute.CITY, display.getCity());
		form.add(DisplayAttribute.POSTAL, display.getPostalCode());
		form.add(DisplayAttribute.COUNTRY, display.getCountry());
		form.add(DisplayAttribute.PROVINCE, display.getProvince());
		form.add(DisplayAttribute.TIMEZONE, display.getTimeZone());

		form.add(DisplayAttribute.STATUS, Integer.toString(display.getSubscriptionStatus()));
//		form.add(DisplayAttribute.WIDTH, Integer.toString(display.getWidth()));
//		form.add(DisplayAttribute.HEIGHT, Integer.toString(display.getHeight()));
		form.add(DisplayAttribute.BROWSER_UPGRADE_MODE, Integer.toString(display.getBrowserUpgradeMode()));
		form.add(DisplayAttribute.RESTART_ENABLED, ServerUtils.BooleanToStr(display.getRestartEnabled()));
		form.add(DisplayAttribute.RESTART_TIME, display.getRestartTime());	
		form.add(DisplayAttribute.MONITORING_ENABLED, ServerUtils.BooleanToStr(display.isMonitoringEnabled()));
		
		put(url, form);
		
		return new RpcResultInfo(display.getId());
	}

	public RpcResultInfo deleteDisplay(String companyId, String displayId) throws ServiceFailedException {
		// new user- generate userId
		if ((displayId == null) || (displayId.isEmpty()))
			return new RpcResultInfo("null","Shedule ID is null or enmty");

		String url = createDisplayResource(companyId, displayId);

		delete(url);
		return new RpcResultInfo();
	}		
	
	public RpcResultInfo restartPlayer(String companyId, String displayId) throws ServiceFailedException {
		String url = createRestartPlayerResource(companyId, displayId);
		
		put(url, URL_PATH_V2, new Form());
		
		return new RpcResultInfo();
	}
	
	public RpcResultInfo rebootPlayer(String companyId, String displayId) throws ServiceFailedException {
		String url = createRebootPlayerResource(companyId, displayId);
		
		put(url, URL_PATH_V2, new Form());
		
		return new RpcResultInfo();
	}
	
	public RpcResultInfo putBrowserUpgradeMode(String companyId, int browserUpgradeMode, String osVersion) throws ServiceFailedException {
		String url = createBrowserUpgradeModeResource(companyId);

		Form form = new Form();

		form.add(DisplayAttribute.BROWSER_UPGRADE_MODE, Integer.toString(browserUpgradeMode));
		form.add(DisplayAttribute.OS, osVersion);
		
		put(url, URL_PATH_V2, form);
		
		return new RpcResultInfo();
	}
	
//	private ArrayList<DisplayInfo> parseGetDisplays(Document doc) {
//		ArrayList<DisplayInfo> displays = new ArrayList<DisplayInfo>();
//
//		try {
//			doc.getDocumentElement().normalize();
//
//			NodeList nodeList = doc.getElementsByTagName("display");
//
//			for (int i = 0; i < nodeList.getLength(); i++) {
//
//				Node fstNode = nodeList.item(i);
//
//				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
//					DisplayInfo display = new DisplayInfo();
//
//					Element fstElmnt = (Element) fstNode;
//
//					display.setId(ServerUtils.getNode(fstElmnt, DisplayAttribute.ID));
//					display.setName(ServerUtils.getNode(fstElmnt, DisplayAttribute.NAME));
//
//					display.setSubscriptionStatus(ServerUtils.strToInt(ServerUtils.getNode(fstElmnt, DisplayAttribute.STATUS), DisplayStatus.ACTIVE));
//					display.setWidth(ServerUtils.strToInt(ServerUtils.getNodeValue(fstElmnt, DisplayAttribute.WIDTH), 0));
//					display.setHeight(ServerUtils.strToInt(ServerUtils.getNodeValue(fstElmnt, DisplayAttribute.HEIGHT), 0));
//					display.setAddress(ServerUtils.getNode(fstElmnt, DisplayAttribute.ADDRESS));
//					display.setCompanyName(ServerUtils.getNode(fstElmnt, DisplayAttribute.COMPANY_NAME));
//					
//					displays.add(display);
//				}
//			}
//			return displays;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return null;
//	}
	
//	public ArrayList<DisplayStatusInfo> getDisplaysStatus(String companyId, String displayIds) throws ServiceFailedException {
//		String url = createDisplaysStatusResource(companyId);
//		
//		Form form = new Form();
//		form.add(ViewerStatusAttribute.DISPLAY_IDS, displayIds);
//
//		Document d = post(url, form);
//		if (d != null) {
//			return parseDisplaysStatus(d);
//		}
//
//		return null;
//	}
	
//	private ArrayList<DisplayStatusInfo> parseDisplaysStatus(Document doc) {
//		ArrayList<DisplayStatusInfo> displayStatusList = new ArrayList<DisplayStatusInfo>();
//
//		try {
//			doc.getDocumentElement().normalize();
//
//			NodeList nodeList = doc.getElementsByTagName("viewer");
//
//			for (int i = 0; i < nodeList.getLength(); i++) {
//
//				Node fstNode = nodeList.item(i);
//
//				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
//					DisplayStatusInfo displayStatus = new DisplayStatusInfo();
//
//					Element fstElmnt = (Element) fstNode;
//
//					displayStatus.setDisplayId(ServerUtils.getNode(fstElmnt, ViewerStatusAttribute.DISPLAY_ID));
//					displayStatus.setLastConnected(ServerUtils.strToDate(ServerUtils.getNode(fstElmnt, ViewerStatusAttribute.LAST_CONNECTION_DATE), null));
//					displayStatus.setConnected(ServerUtils.StrToBoolean(ServerUtils.getNode(fstElmnt, ViewerStatusAttribute.CONNECTED)));
//					
//					displayStatusList.add(displayStatus);
//				}
//			}
//			return displayStatusList;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return null;
//	}

//	private String createDisplaysBookmarksResource(DisplaysInfo displaysInfo, boolean activeOnly) {
//		//URI: https://rdncore.appspot.com/company/{companyId}/displays/pagemap?sortBy=sortByFieldName&searchFor=searchString&pageSize=recordsPerPage
//			
//		String resourceUrl = "/company/" + displaysInfo.getCompanyId() + "/displays/pagemap?";
//		if (!RiseUtils.strIsNullOrEmpty(displaysInfo.getSortBy()))
//			resourceUrl = resourceUrl + "sortBy=" + displaysInfo.getSortBy() + "&";
//		else
//			resourceUrl = resourceUrl + "sortBy=name" + "&"; //sortBy is Required
//			
//		if (!RiseUtils.strIsNullOrEmpty(displaysInfo.getSearchFor())) {
//			resourceUrl = resourceUrl + "searchFor=" + displaysInfo.getSearchFor() + "&";
//		}
//		
//		resourceUrl = resourceUrl + "pageSize=" + Integer.toString(displaysInfo.getPageSize()) + "&";
//		resourceUrl = resourceUrl + "activeonly=" + (activeOnly ? 1 : 0);
//
//		return resourceUrl;
//	}

//	private String createDisplaysResource(String companyId, String bookmark) {
//		//URI: https://rdncore.appspot.com/company/{companyId}/displays?bookmark=pageBookmark
//
//		return "/company/" + companyId + "/displays?bookmark=" + bookmark;
//	}
	
	private String createDisplayResource(String companyId, String displayId) {
		//URI: https://rdncore.appspot.com/company/{companyId}/display/{displayId}
			
		return "/company/" + companyId + "/display/" + displayId;
	}
	
	private String createRestartPlayerResource(String companyId, String displayId) {
		//URI: https://rdncore.appspot.com/company/{companyId}/display/{displayId}/player/restart
		
		return "/company/" + companyId + "/display/" + displayId + "/restart";
	}
	
	private String createRebootPlayerResource(String companyId, String displayId) {
		//URI: https://rdncore.appspot.com/company/{companyId}/display/{displayId}/player/reboot
		
		return "/company/" + companyId + "/display/" + displayId + "/reboot";
	}
	
//	private String createDisplaysStatusResource(String companyId) {
//		//URI: https://rdncore.appspot.com/company/{companyId}/viewerstatus
//		
//		return "/company/" + companyId + "/viewerstatus";
//	}
	
	private String createBrowserUpgradeModeResource(String companyId) {
		// URI: https://rvacore2.appspot.com/v2/company/{companyId}/displays/upgrademode
		
		return "/company/" + companyId + "/displays/upgrademode";
	}
	
	private DisplayInfo DocToDisplay(Document doc) {
		DisplayInfo display = new DisplayInfo();

		try {
			doc.getDocumentElement().normalize();

			Node rootNode = doc.getDocumentElement();

			if (rootNode.getNodeType() == Node.ELEMENT_NODE) {

				Element fstElmnt = (Element) rootNode;
				display.setId(ServerUtils.getNode(fstElmnt, DisplayAttribute.ID));
				display.setName(ServerUtils.getNode(fstElmnt, DisplayAttribute.NAME));

				display.setAddressDescription(ServerUtils.getNode(fstElmnt, DisplayAttribute.ADDRESS_DESCRIPTION));
				display.setUseCompanyAddress(ServerUtils.StrToBoolean(ServerUtils.getNode(fstElmnt, DisplayAttribute.USE_COMPANY_ADDRESS)));
				display.setStreet(ServerUtils.getNode(fstElmnt, DisplayAttribute.STREET));
				display.setUnit(ServerUtils.getNode(fstElmnt, DisplayAttribute.UNIT));
				display.setCity(ServerUtils.getNode(fstElmnt, DisplayAttribute.CITY));
				display.setPostalCode(ServerUtils.getNode(fstElmnt, DisplayAttribute.POSTAL));
				display.setCountry(ServerUtils.getNode(fstElmnt, DisplayAttribute.COUNTRY));
				display.setProvince(ServerUtils.getNode(fstElmnt, DisplayAttribute.PROVINCE));
				display.setTimeZone(ServerUtils.getNode(fstElmnt, DisplayAttribute.TIMEZONE));

				display.setSubscriptionStatus(ServerUtils.strToInt(ServerUtils.getNode(fstElmnt, DisplayAttribute.STATUS), DisplayStatus.ACTIVE));
				display.setWidth(ServerUtils.strToInt(ServerUtils.getNodeValue(fstElmnt, DisplayAttribute.WIDTH), 0));
				display.setHeight(ServerUtils.strToInt(ServerUtils.getNodeValue(fstElmnt, DisplayAttribute.HEIGHT), 0));
				display.setRestartEnabled(ServerUtils.StrToBoolean(ServerUtils.getNode(fstElmnt, DisplayAttribute.RESTART_ENABLED)));
				display.setRestartTime(ServerUtils.getNodeValue(fstElmnt, DisplayAttribute.RESTART_TIME));
				display.setMonitoringEnabled(ServerUtils.StrToBoolean(ServerUtils.getNode(fstElmnt, DisplayAttribute.MONITORING_ENABLED)));
				
				display.setBrowserUpgradeMode(ServerUtils.strToInt(ServerUtils.getNode(fstElmnt, DisplayAttribute.BROWSER_UPGRADE_MODE), BrowserUpgradeMode.AUTO));
				display.setChromiumVersion(ServerUtils.getNode(fstElmnt, DisplayAttribute.CHROMIUM_VERSION));
				display.setBrowserName(ServerUtils.getNode(fstElmnt, DisplayAttribute.BROWSER_NAME));
				display.setRecommendedBrowserVersion(ServerUtils.getNode(fstElmnt, DisplayAttribute.RECOMMENDED_BROWSER_VERSION));
//				display.setInstallerVersion(ServerUtils.getNode(fstElmnt, DisplayAttribute.INSTALLER_VERSION));
				display.setPlayerName(ServerUtils.getNode(fstElmnt, DisplayAttribute.PLAYER_NAME));
				display.setPlayerVersion(ServerUtils.getNode(fstElmnt, DisplayAttribute.PLAYER_VERSION));
				display.setOsVersion(ServerUtils.getNode(fstElmnt, DisplayAttribute.OS));
				display.setCacheVersion(ServerUtils.getNode(fstElmnt, DisplayAttribute.CACHE_VERSION));
				display.setViewerVersion(ServerUtils.getNode(fstElmnt, DisplayAttribute.VIEWER_VERSION));
				
				display.setConnected(ServerUtils.StrToBoolean(ServerUtils.getNode(fstElmnt, ViewerStatusAttribute.CONNECTED)));
				display.setLastConnectionDate(ServerUtils.strToDate(ServerUtils.getNode(fstElmnt, ViewerStatusAttribute.LAST_CONNECTION_DATE), null));
				
				display.setPlayerStatus(ServerUtils.strToInt(ServerUtils.getNode(fstElmnt, DisplayAttribute.PLAYER_STATUS), 0));
				
				display.setBlockExpiryDate(ServerUtils.strToDate(ServerUtils.getNode(fstElmnt, DisplayAttribute.BLOCK_EXPIRY), null));

				display.setChangedBy(ServerUtils.getNode(fstElmnt, CommonAttribute.CHANGED_BY));
				display.setChangeDate(ServerUtils.strToDate(ServerUtils.getNode(fstElmnt, CommonAttribute.CHANGE_DATE), null));
			}	
			return display;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
//	public void generateTestDisplayRecords(String companyId, int count) {
//		Random generator = new Random();
//		int maxRandomValue = 1000000;
//		for (int i=0; i<count; i++) {
//			System.out.println(Integer.toString(i));
//			DisplayInfo display = new DisplayInfo();
//			display.setId(UUID.randomUUID().toString());
//
//			display.setName("Display  "+generator.nextInt(maxRandomValue));
//
//			display.setAddressDescription("Address  "+generator.nextInt(maxRandomValue));
//			display.setUseCompanyAddress(false);
//			display.setStreet("Street  "+generator.nextInt(maxRandomValue));
//			display.setUnit(Integer.toString(generator.nextInt(maxRandomValue)));
//			display.setCity("City  "+generator.nextInt(maxRandomValue));
//			display.setPostalCode(Integer.toString(generator.nextInt(maxRandomValue)));
//			display.setCountry("Country  "+generator.nextInt(maxRandomValue));
//			display.setProvince("Pprovince  "+generator.nextInt(maxRandomValue));
//			display.setTimeZone(Integer.toString(generator.nextInt(10)));
//
//			display.setSubscriptionStatus(1);
//			display.setWidth(1024);
//			display.setHeight(768);
//			
//			display.setChangedBy("100918744358134810665");
//			display.setChangeDate(new Date(1000));
//			
//			try {
//				putDisplay(companyId, display);
//			} catch (ServiceFailedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
	
}
