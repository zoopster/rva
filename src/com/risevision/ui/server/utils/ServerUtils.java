// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.server.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.resource.ClientResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.risevision.core.api.Global;
import com.risevision.ui.client.common.exception.ServiceFailedException;
import com.risevision.ui.client.common.info.GridInfo;
import com.risevision.ui.server.data.DataService;
import com.risevision.ui.server.data.PersistentConfigurationInfo;
import com.risevision.ui.server.data.PersistentOAuthInfo;
import com.risevision.ui.server.data.PersistentUserInfo;
import com.risevision.ui.server.data.PersistentOAuthInfo.OAuthType;

public class ServerUtils {

	public static int strToInt(String value, int defaultValue) {
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static Date strToDate(String value, Date defaultValue) {
		try {			
			SimpleDateFormat df = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z"); // RFC-822 date-time with time zone  
			return df.parse(value);        			
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public static String dateToRfc822(Date date) {
		
    	if (date != null) {
    		SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z"); // RFC-822 date-time with time zone 
    		return formatter.format(date);
    	} else {
    		return "";
    	}
	}
	
	public static Date strToDate(String value) {
		return strToDate(value, null);
	}

	@SuppressWarnings("deprecation")
	public static String dateToString(Date date) {
		try {
			return DateTimeFormat.getMediumDateTimeFormat().format(date);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Date strToTime(String value, Date defaultValue) {
		try {			
			SimpleDateFormat df = new SimpleDateFormat("HH:mm");
			return df.parse(value);        			
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public static Boolean StrToBoolean(String value) {
		return StrToBoolean(value, false);
	}
	
	public static Boolean StrToBoolean(String value, Boolean defaultValue) {
		try {
			if (Global.TRUE.equals(value))
				return true;
			else if (Global.FALSE.equals(value))
				return false;
			else
				return defaultValue;
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static String BooleanToStr(Boolean value) {
		return value ? Global.TRUE : Global.FALSE;
	}

	public static String ListToString(ArrayList<String> list, String delimiter) {
		String result = "";
		if (list != null) {
			for (int i = 0; i < list.size(); i++)
				result += list.get(i) + delimiter;
			if (list.size() > 0)
				result = result.substring(0, result.length() - 1);
		}
		return result;
	}

	@Deprecated
	public static String getGridBookmark(Document doc, GridInfo gridInfo){

		try {
			doc.getDocumentElement().normalize();

			NodeList nodeList = doc.getElementsByTagName("page");

			Map<Integer, String> pageMap = new HashMap<Integer, String>();
			
			for (int s = 0; s < nodeList.getLength(); s++) {

				Node fstNode = nodeList.item(s);

				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
					Integer number;
					String bookmark; 

					Element fstElmnt = (Element) fstNode;

					number = strToInt(getNode(fstElmnt, GridInfo.NUMBER), 0);
					bookmark = getNode(fstElmnt, GridInfo.BOOKMARK);

					pageMap.put(number, bookmark);
				}

			}
			
			String bookmark = null;
			
			if (pageMap.size() != 0){
				if (pageMap.get(gridInfo.getCurrentPage()) != null) {
					bookmark = pageMap.get(gridInfo.getCurrentPage());					
				}
				else{
					bookmark = pageMap.get(1);
				}
			}
			
			gridInfo.setPageCount(pageMap.size());
			
			return bookmark;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//find the first occurrence of the nodeName in the list of immediate children
	public static Node findFirstNode(Node parentNode, String nodeName) {
		NodeList nodeList = parentNode.getChildNodes();
		if ((nodeList != null) && (nodeName != null))
			for (int i = 0; i < nodeList.getLength(); i++)
				if (nodeName.equals(nodeList.item(i).getNodeName()))
					return nodeList.item(i);
		return null;
	}
	
	//Optimised version of the getNode(Element myElement, String attribute) implementation
	public static String getNodeValue(Node parentNode, String nodeName) {
			return getNodeValue(parentNode, nodeName, "");
	}

	//Optimised version of the getNode(Element myElement, String attribute, int location) implementation
	public static String getNodeValue(Node parentNode, String nodeName, String defaultValue) {
		Node node = findFirstNode(parentNode, nodeName);
		if (node == null)
			return null;
		else
			return node.getTextContent();
	}

	public static String getNode(Element myElement, String attribute) {
		return getNode(myElement, attribute, 0);
	}

	public static String getNode(Element myElement, String attribute, int location) {
		return getNode(myElement, attribute, location, null);
	}
	
	public static String getNode(Element myElement, String attribute, String defaultValue) {
		return getNode(myElement, attribute, 0, defaultValue);
	}
	
	public static String getNode(Element myElement, String attribute, int location, String defaultValue) {
		String s = null;

		NodeList nodeList = myElement.getElementsByTagName(attribute);
		if (nodeList.item(location) != null
				&& nodeList.item(location).getNodeType() == Node.ELEMENT_NODE) {
			Element element = (Element) nodeList.item(location);
			NodeList nodeList2 = element.getChildNodes();
			if (nodeList2.item(location) != null
					&& nodeList2.item(location).getNodeType() == Node.TEXT_NODE)
				s = ((Node) nodeList2.item(location)).getNodeValue();
		}
		return s;
	}
	
	public static ArrayList<String> getNodeListElements(NodeList nodeList) {
		ArrayList<String> resultList = new ArrayList<String>();
		if (nodeList != null) {
			for (int s = 0; s < nodeList.getLength(); s++) {
				Node node = nodeList.item(s);
	
				if (node.getNodeType() == Node.ELEMENT_NODE && node.hasChildNodes()) {
					String element = node.getFirstChild().getNodeValue();
	
					resultList.add(element);
				}
			}
		}
		
		return resultList;
	}
	
	public static String getLogoutURL(String returnURL){	
		UserService userService = UserServiceFactory.getUserService();		
		return userService.createLogoutURL(returnURL);
	}
	
	public static String getLoginURL(String returnURL) {
		UserService userService = UserServiceFactory.getUserService();		
		return userService.createLoginURL(returnURL);
	}

	public static boolean isUserLoggedIn() {
		UserService userService = UserServiceFactory.getUserService();
		return userService.isUserLoggedIn();
	}
	
	public static String getGoogleUsername() {
		String username = null;
		UserService userService = UserServiceFactory.getUserService();
		if (userService.isUserLoggedIn()) {
			User currentUser = userService.getCurrentUser();
			username = currentUser.getEmail();
	    }
		else {
			return null;
		}
		return username;
	}		
	
	public static String getRequestURL(HttpServletRequest request) {
		String file = request.getRequestURI();
		if (request.getQueryString() != null) {
		   file += '?' + request.getQueryString();
		}

		try {
			URL reconstructedURL = new URL(request.getScheme(),
			                               request.getServerName(),
			                               request.getServerPort(),
			                               file);
			
			return reconstructedURL.toString();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String formatUrl(String url, String urlPath) {
		PersistentConfigurationInfo pConfig = DataService.getInstance().getConfig();
		
		url = pConfig.getServerURL() + "/" + urlPath + url;
		
//		if (url.contains("?")) 
//			url += "&";
//		else
//			url += "?";
//		
//		url += "appId=rdntwo";
		
		return url;
	}
	
	public static PersistentUserInfo getPersistentUser() throws ServiceFailedException {
		PersistentUserInfo user = DataService.getInstance().getUser();
		
		if (user == null) {
			throw new ServiceFailedException(ServiceFailedException.AUTHENTICATION_FAILED);
		}
		
		return user;
	}
	
//	public static PersistentUserInfo getPersistentUser(HttpServletRequest request) throws ServiceFailedException {
//		String token = (String) request.getSession().getAttribute("userToken");
//		String tokenSecret = (String) request.getSession().getAttribute("userTokenSecret");
//		String tokenOwner = (String) request.getSession().getAttribute("userTokenOwner");
//
//		if (token == null || tokenSecret == null || tokenOwner == null || !tokenOwner.equals(ServerUtils.getGoogleUsername())) {
//			PersistentUserInfo user = DataService.getInstance().getUser();
//			
//			if (user != null) {
//				request.getSession().setAttribute("userToken", user.getUserToken());
//				request.getSession().setAttribute("userTokenSecret", user.getUserTokenSecret());
//				request.getSession().setAttribute("userTokenOwner", user.getUserName());
//			}
//			else {
//				throw new ServiceFailedException(ServiceFailedException.AUTHENTICATION_FAILED);
//			}
//			
//			return user;
//		}
//		else {
//			PersistentUserInfo user = new PersistentUserInfo(token, tokenSecret);
//			
//			return user;
//		}
//	}
	
	private static OAuthConsumer createConsumer(PersistentUserInfo user) throws ServiceFailedException {	
		PersistentOAuthInfo oAuth = DataService.getInstance().getOAuth(OAuthType.user);
		OAuthConsumer consumer = new DefaultOAuthConsumer(oAuth.getConsumerKey(), oAuth.getConsumerSecret());
		
		consumer.setTokenWithSecret(user.getUserToken(), user.getUserTokenSecret());
		
		return consumer;
	}
	
	public static void signResource(ClientResource clientResource, HttpServletRequest request, String url, String method) throws ServiceFailedException {
		PersistentUserInfo user = ServerUtils.getPersistentUser();
		OAuthConsumer consumer = ServerUtils.createConsumer(user);
		
		try {
			URL payload_url = new URL(url);
//			URL payload_url = new URL(ServerUtils.formatUrl(url));
			
			HttpURLConnection r = (HttpURLConnection) payload_url.openConnection();
			r.setRequestMethod(method);
	
	        // sign the request
	        try {
				consumer.sign(r);
			} catch (OAuthMessageSignerException e) {
	//			log.severe(e.getMessage());
				e.printStackTrace();
			} catch (OAuthExpectationFailedException e) {
	//			log.severe(e.getMessage());
				e.printStackTrace();
			} catch (OAuthCommunicationException e) {
	//			log.severe(e.getMessage());
				e.printStackTrace();
			}
				
			String header = "";
			try {
				header = r.getRequestProperty(OAuth.HTTP_AUTHORIZATION_HEADER);
				if (header.contains("OAuth ")) {
					header = header.substring("OAuth ".length(), header.length());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	
			ChallengeResponse challenge = new ChallengeResponse(ChallengeScheme.HTTP_OAUTH);
			challenge.setRawValue(header);
			
			clientResource.setChallengeResponse(challenge);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeDebugInfo(ClientResource clientResource) {
		if (clientResource.getStatus().isSuccess() && clientResource.getResponseEntity().isAvailable()) {
			try {
				clientResource.getResponseEntity().write(System.out);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			System.out.printf("Status: %s %s", clientResource.getStatus()
					.getCode(), clientResource.getStatus().getDescription());
		}
		
		System.out.println();
	}
	
}
