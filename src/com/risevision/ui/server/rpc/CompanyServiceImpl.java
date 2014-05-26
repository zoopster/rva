package com.risevision.ui.server.rpc;

import java.util.HashMap;
import java.util.UUID;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.core.api.Entity;
import com.risevision.core.api.attributes.AuthKeyAttribute;
import com.risevision.core.api.attributes.CommonAttribute;
import com.risevision.core.api.attributes.CompanyAttribute;
import com.risevision.core.api.attributes.DemoAttribute;
import com.risevision.core.api.types.CompanyNetworkOperatorStatus;
import com.risevision.core.api.types.CompanyStatus;
import com.risevision.ui.client.common.exception.ServiceFailedException;
import com.risevision.ui.client.common.info.AlertsInfo;
import com.risevision.ui.client.common.info.CompanyInfo;
import com.risevision.ui.client.common.info.DemoContentInfo;
import com.risevision.ui.client.common.info.DemographicsInfo;
import com.risevision.ui.client.common.info.RpcResultInfo;
import com.risevision.ui.client.common.service.CompanyService;
import com.risevision.ui.server.RiseRemoteServiceServlet;
import com.risevision.ui.server.utils.ServerUtils;

import org.restlet.data.Form;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class CompanyServiceImpl extends RiseRemoteServiceServlet implements CompanyService {

//	public CompaniesInfo getCompanies(String parentCompanyId, CompaniesInfo companiesInfo) throws ServiceFailedException {
//		String bookmark = null;
//
//		//PART 1 - get bookmarks
//		String bookmarkUrl = createCompaniesBookmarksResource(parentCompanyId, companiesInfo);
//		Document d1;
//
//		d1 = get(bookmarkUrl);
//		if (d1 != null)
//			bookmark = ServerUtils.getGridBookmark(d1, companiesInfo);
//		
//		//PART 2 - get companies
//		if (bookmark != null)
//		{
//			String pageUrl = createCompaniesResource(parentCompanyId, bookmark);
//			Document d;
//
//			d = get(pageUrl);
//
//			if (d != null) {
//				ArrayList<CompanyInfo> companies = parseGetCompanies(d);
//				companiesInfo.setCompanies(companies);
//				return companiesInfo;
//			}
//		}		
//		companiesInfo.setCompanies(null);
//		return companiesInfo;
//	}

	public CompanyInfo getCompany(String companyId) throws ServiceFailedException {
		String url = createCompanyResource(companyId);

		Document d = get(url);
		if (d != null){
			CompanyInfo company = DocToCompany(d);
			return company;		
		}
		return null;
	}

	public CompanyInfo saveCompany(CompanyInfo company) throws ServiceFailedException {
		// new user- generate userId
		if ((company.getId() == null) || (company.getId().isEmpty()))
			company.setId(UUID.randomUUID().toString());
		String url = createCompanyResource(company.getId());

		Form form = new Form();

		form.add(CompanyAttribute.ID, company.getId());
		form.add(CompanyAttribute.NAME, company.getName());
		form.add(CompanyAttribute.STREET, company.getStreet());
		form.add(CompanyAttribute.UNIT, company.getUnit());
		form.add(CompanyAttribute.CITY, company.getCity());
		form.add(CompanyAttribute.COUNTRY, company.getCountry());
		form.add(CompanyAttribute.PROVINCE, company.getProvince());
		form.add(CompanyAttribute.POSTAL, company.getPostalCode());
		form.add(CompanyAttribute.TIMEZONE, company.getTimeZone());
		form.add(CompanyAttribute.TELEPHONE, company.getTelephone());
		form.add(CompanyAttribute.FAX, company.getFax());
		form.add(CompanyAttribute.PARENT, company.getParentId());
		form.add(CompanyAttribute.NETWORK_OPERATOR_STATUS, Integer.toString(company.getPnoStatus()));
		form.add(CompanyAttribute.COMPANY_STATUS, Integer.toString(company.getCustomerStatus()));
		form.add(CompanyAttribute.NOTIFICATION_EMAILS, RiseUtils.listToString(company.getDisplayMonitorEmailRecipients(), ","));
		form.add(CompanyAttribute.ENABLED_FEATURES, company.getEnabledFeaturesJson());
		
//		form.add(CompanyAttribute.APP_IDS, "rdntwo");
		
		AlertsInfo alertsSettings = company.getAlertsSettings();
		
		form.add(CompanyAttribute.ALLOW_ALERTS, ServerUtils.BooleanToStr(alertsSettings.isAllowAlerts()));
		form.add(CompanyAttribute.ALERT_URL, alertsSettings.getAlertURL());
		form.add(CompanyAttribute.ALERT_USERNAME, alertsSettings.getAlertUserName());
		form.add(CompanyAttribute.ALERT_PASSWORD, alertsSettings.getAlertPassword());
		form.add(CompanyAttribute.ALERT_SENDERS, RiseUtils.listToString(alertsSettings.getAlertSenders(), ","));
		form.add(CompanyAttribute.ALERT_STATUSES, RiseUtils.listToString(alertsSettings.getAlertStatuses(), ","));
		form.add(CompanyAttribute.ALERT_HANDLING_CODES, RiseUtils.listToString(alertsSettings.getAlertHandlingCodes(), ","));
		form.add(CompanyAttribute.ALERT_CATEGORIES, RiseUtils.listToString(alertsSettings.getAlertCategories(), ","));
		form.add(CompanyAttribute.ALERT_URGENCIES, RiseUtils.listToString(alertsSettings.getAlertUrgencies(), ","));
		form.add(CompanyAttribute.ALERT_SEVERITIES, RiseUtils.listToString(alertsSettings.getAlertSeverities(), ","));
		form.add(CompanyAttribute.ALERT_CERTAINTIES, RiseUtils.listToString(alertsSettings.getAlertCertainties(), ","));
		form.add(CompanyAttribute.ALERT_EVENT_CODES, RiseUtils.listToString(alertsSettings.getAlertEventCodes(), ","));
		form.add(CompanyAttribute.ALERT_TEXT_FIELDS, RiseUtils.listToString(alertsSettings.getAlertTextFields(), ","));
		form.add(CompanyAttribute.ALERT_PRESENTATION_ID, alertsSettings.getAlertPresentationId());
		form.add(CompanyAttribute.ALERT_DISTRIBUTION, RiseUtils.listToString(alertsSettings.getAlertDistribution(), ","));
		form.add(CompanyAttribute.ALERT_EXPIRY, Integer.toString(alertsSettings.getAlertExpiry()));

		DemographicsInfo demographicsInfo = company.getDemographicsInfo();
		
		form.add(CompanyAttribute.COMPANY_TYPE, demographicsInfo.getCompanyType());
		form.add(CompanyAttribute.SERVICES_PROVIDED, RiseUtils.listToString(demographicsInfo.getServicesProvided(), ","));
		form.add(CompanyAttribute.MARKET_SEGMENTS, RiseUtils.listToString(demographicsInfo.getMarketSegments(), ","));
		form.add(CompanyAttribute.TARGET_GROUPS, RiseUtils.listToString(demographicsInfo.getTargetGroups(), ","));
//		form.add(CompanyAttribute.VIEWS_PER_DISPLAY, demographicsInfo.getViewsPerDisplay());
		form.add(CompanyAttribute.ADVERTISING_REVENUE_EARN, ServerUtils.BooleanToStr(demographicsInfo.isAdvertisingRevenueEarn()));
		form.add(CompanyAttribute.ADVERTISING_REVENUE_INTERESTED, ServerUtils.BooleanToStr(demographicsInfo.isAdvertisingRevenueInterested()));
		
		put(url, form);		
		
		return company;
	}
	
	public void deleteCompany(String companyId) throws ServiceFailedException {
		String url = createCompanyResource(companyId);
		
//		int status = 
		delete(url);
//		if (status == Status.CLIENT_ERROR_CONFLICT.getCode()) {
//			return new RpcResultInfo(companyId, "Company delete failed; please ensure all subcompanies are deleted!");
//		}
//		return new RpcResultInfo();
	}
	
	public CompanyInfo getCompanyByAuthKey(String companyAuthKey) throws ServiceFailedException {
		String url = createCompanyAuthKeyLookup(companyAuthKey);
		
		Document d = get(url);
		if (d != null){
			CompanyInfo company = DocToCompany(d);
			return company;		
		}
		return null; 
	}
	
	public RpcResultInfo moveCompanyByAuthKey(String companyId, String companyAuthKey) throws ServiceFailedException {
		String url = createCompanyAuthKeyMove(companyId, companyAuthKey);
		
		put(url, new Form());		

		return new RpcResultInfo();
	}
	
//	public CompanyInfo getPnoSettings(String companyId) throws ServiceFailedException {
//		String url = createPnoSettingsResource(companyId);
//		Document d;
//		CompanyInfo company;
//
//		d = get(url);
//
//		if (d != null)
//		{
//			company = parsePnoSettings(d);
//			return company;
//		}
//		return null;
//	}
	
//	public void savePnoSettings(CompanyInfo company) throws ServiceFailedException {
//		String url = createPnoSettingsResource(company.getId());
//
//		Form form = new Form();
//		
//		// iterate through list to add elements.
//		for (String key : company.getSettings().keySet()){
//			form.add(key, company.getSettings().get(key));
//		}
//		
//		put(url, form);
//	}
	
	public void savePnoSettings(CompanyInfo company) throws ServiceFailedException {
		String url = createCompanyResource(company.getId());
		String settingsString = "";

		try {
			JSONArray settingsArray = new JSONArray();
			// iterate through list to add elements.
			for (String key : company.getSettings().keySet()){
				JSONObject setting = new JSONObject();
				setting.put("name", key);
				setting.put("value", company.getSettings().get(key));
				settingsArray.put(setting);
			}
			
			settingsString = settingsArray.toString();
			
		} catch (JSONException e) {
			e.printStackTrace();
			
			throw new ServiceFailedException();
		}
		
		Form form = new Form();
		
		form.add(CompanyAttribute.SETTINGS, settingsString);
		
		put(url, form);
	}

//	public ArrayList<CompanyInfo> getPnoCompanies(String companyId, String excludeCompanyId) throws ServiceFailedException {
//		String url = createPnoCompaniesResource(companyId, excludeCompanyId);
//		
//		Document d;
//		d = get(url);
//
//		if (d != null) {
//			ArrayList<CompanyInfo> companies = parseGetCompanies(d);
//			return companies;
//		}
//		return null;
//	}
	
//	public String getCompanyAuthKey(String companyId) throws ServiceFailedException {
//		String url = createCompanyAuthKeyResource(companyId);
//		
//		Document d;
//		d = get(url);
//		
//		if (d != null) {
//			String companyAuthKey = parseGetCompanyAuthKey(d);
//			return companyAuthKey;
//		}
//		
//		return null;
//	}
	
	public String resetCompanyAuthKey(String companyId) throws ServiceFailedException {
		String url = createCompanyAuthKeyResetResource(companyId);
		
		Document d;
//		d = post(url, new Form());
		d = get(url);
		
		if (d != null) {
			String companyAuthKey = parseGetCompanyAuthKey(d);
			return companyAuthKey;
		}
		
		return null;
	}
	
	public String resetCompanyClaimId(String companyId) throws ServiceFailedException {
		String url = createCompanyClaimIdResetResource(companyId);
		
		Document d;
//		d = post(url, new Form());
		d = get(url);
		
		if (d != null) {
			String companyClaimId = parseGetCompanyClaimId(d);
			return companyClaimId;
		}
		
		return null;
	}
	
	public String resetAlertsURL(String companyId) throws ServiceFailedException {
		String url = createAlertsURLResetResource(companyId);
		
		Document d;
		d = get(url);
		
		if (d != null) {
			String alertsURL = parseGetAlertsURL(d);
			return alertsURL;
		}
		
		return null;
	}
	
	public DemoContentInfo getDemoContent(String companyId, int contentWidth, int contentHeight) throws ServiceFailedException {
		String url = createDemoContentResource(companyId, contentWidth, contentHeight);
		
		Document d;
		d = get(url);
		
		if (d != null) {
			DemoContentInfo demoContent = parseGetDemoContent(d);
			return demoContent;
		}
		
		return null;
	}
	
//	public ArrayList<DemoContentInfo> getDemoContent(String companyId) throws ServiceFailedException {
//		String url = createDemoContentResource(companyId);
//		
//		Document d;
//		d = get(url);
//		
//		if (d != null) {
//			ArrayList<DemoContentInfo> demoContentList = parseGetDemoContentList(d);
//			return demoContentList;
//		}
//		
//		return null;
//	}
	
	public RpcResultInfo saveDemoContent(String companyId, DemoContentInfo demoContent) throws ServiceFailedException {
		String url = createDemoContentResource(companyId, demoContent.getWidth(), demoContent.getHeight());

		Form form = new Form();

		form.add(DemoAttribute.OBJECT_REFERENCE, demoContent.getObjectRef());

		put(url, form);

		return new RpcResultInfo();
	}
	
	public RpcResultInfo deleteDemoContent(String companyId, int contentWidth, int contentHeight) throws ServiceFailedException {
		String url = createDemoContentResource(companyId, contentWidth, contentHeight);

		delete(url);
		
		return new RpcResultInfo();
	}
	
//	private String createCompaniesBookmarksResource(String parentCompanyId, CompaniesInfo companiesInfo) {
//		// -- /company/{companyId}/companies/pagemap?sortBy=sortByFieldName&searchFor=searchString&pageSize=recordsPerPage
//		// -- /companies/pagemap?sortBy=sortByFieldName&searchFor=searchString&pageSize=recordsPerPage
//		
////		String resourceUrl = serverUrl + "/company/" + companyId + "/companies/pagemap?";
//		String resourceUrl;
//		if (parentCompanyId == null)
//			resourceUrl = "/companies/pagemap?";
//		else
//			resourceUrl = "/company/" + parentCompanyId + "/companies/pagemap?";
//			
//		if (!RiseUtils.strIsNullOrEmpty(companiesInfo.getSortBy()))
//			resourceUrl = resourceUrl + "sortBy=" + companiesInfo.getSortBy() + "&";
//		else
//			resourceUrl = resourceUrl + "sortBy=name&";
//			
//			
//		if (companiesInfo.getSearchFor() != null){
//			resourceUrl = resourceUrl + "searchFor=" + companiesInfo.getSearchFor() + "&";
//		}
//		
//		resourceUrl = resourceUrl + "pageSize=" + Integer.toString(companiesInfo.getPageSize());
//
//		return resourceUrl;
//	}

//	private String createCompaniesResource(String parentCompanyId, String bookmark) {
//		String resourceUrl = "";
//		
//		// [AD] The companies resource is no longer available without a partentCompanyId parameter
//		if (parentCompanyId == null)
//			;
//			//resourceUrl = "/companies?bookmark=" + bookmark;
//		else
//			resourceUrl = "/company/" + parentCompanyId + "/companies?bookmark=" + bookmark;		
//
//		return resourceUrl;
//	}

//	private String createPnoCompaniesResource(String companyId, String excludeCompanyId) {
//		// https://rdncore.appspot.com/company/{companyId}/networkoperators?excludeId={excludeCompanyId}
//		String resourceUrl = "/company/" + companyId + "/networkoperators";
//		
//		if (excludeCompanyId != null && !excludeCompanyId.isEmpty())
//			resourceUrl += "?excludeId=" + excludeCompanyId;
//
//		return resourceUrl;
//	}
	
	private String createCompanyResource(String companyId) {
		return "/company/" + companyId;
	}
	
	private String createCompanyAuthKeyLookup(String companyAuthKey) {
		//  https://rdncore.appspot.com/company/{authKey}/lookup
		return "/company/" + companyAuthKey + "/lookup";
	}
	
	private String createCompanyAuthKeyMove(String companyId, String companyAuthKey) {
		// https://rdncore.appspot.com/company/{companyId}/move/{authKey}
		return "/company/" + companyId + "/move/" + companyAuthKey;
	}
	
//	private String createPnoSettingsResource(String companyId) {
//		// /company/{companyId}/settings/
//
//		return "/company/" + companyId + "/settings";
//	}
	
//	private String createCompanyAuthKeyResource(String companyId) {
//		// /company/{companyId}/authkey
//		
//		return "/company/" + companyId + "/authkey";
//	}
	
	private String createCompanyAuthKeyResetResource(String companyId) {
		// /company/{companyId}/authkey-reset
		
		return "/company/" + companyId + "/authkey-reset";
	}
	
	private String createCompanyClaimIdResetResource(String companyId) {
		// /company/{companyId}/claimId-reset
		
		return "/company/" + companyId + "/claimId-reset";
	}
		
	private String createAlertsURLResetResource(String companyId) {
		// /company/{companyId}/alerturl-reset 
		
		return "/company/" + companyId + "/alerturl-reset";
	}
	
	private String createDemoContentResource(String companyId, int contentWidth, int contentHeight) {
		// /company/{companyId}/demo/{width}/{height}
		
		return "/company/" + companyId + "/demo/" + Integer.toString(contentWidth) + "/" + Integer.toString(contentHeight);
	}
	
//	private String createDemoContentResource(String companyId) {
//		// /company/{companyId}/demos
//		
//		return "/company/" + companyId + "/demos";
//	}
	
	public static CompanyInfo DocToCompany(Document doc) {
		try {
			doc.getDocumentElement().normalize();
			
			NodeList nodeList = doc.getElementsByTagName(Entity.COMPANY);

			Node fstNode = nodeList.item(0);

			CompanyInfo company = new CompanyInfo();
	
			if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

				Element fstElmnt = (Element) fstNode;
	
				company.setId(ServerUtils.getNode(fstElmnt, CompanyAttribute.ID));
				company.setName(ServerUtils.getNode(fstElmnt, CompanyAttribute.NAME));
				
				company.setStreet(ServerUtils.getNode(fstElmnt, CompanyAttribute.STREET));
				company.setUnit(ServerUtils.getNode(fstElmnt, CompanyAttribute.UNIT));
				company.setCity(ServerUtils.getNode(fstElmnt, CompanyAttribute.CITY));
				company.setProvince(ServerUtils.getNode(fstElmnt, CompanyAttribute.PROVINCE));
				company.setCountry(ServerUtils.getNode(fstElmnt, CompanyAttribute.COUNTRY));
				company.setPostalCode(ServerUtils.getNode(fstElmnt, CompanyAttribute.POSTAL));
				company.setTimeZone(ServerUtils.getNode(fstElmnt, CompanyAttribute.TIMEZONE, company.getTimeZone()));
				
				// Added the address field for the getCompanyByAuthKey API
				company.setAddress(ServerUtils.getNode(fstElmnt, CompanyAttribute.ADDRESS));
				
				company.setTelephone(ServerUtils.getNode(fstElmnt, CompanyAttribute.TELEPHONE));
				company.setFax(ServerUtils.getNode(fstElmnt, CompanyAttribute.FAX));
		
				company.setParentId(ServerUtils.getNode(fstElmnt, CompanyAttribute.PARENT));
				
				company.setPnoStatus(RiseUtils.strToInt(ServerUtils.getNode(fstElmnt, CompanyAttribute.NETWORK_OPERATOR_STATUS), CompanyNetworkOperatorStatus.NO));
				company.setCustomerStatus(RiseUtils.strToInt(ServerUtils.getNode(fstElmnt, CompanyAttribute.COMPANY_STATUS), CompanyStatus.ACTIVE));
				company.setCustomerStatusChangeDate(ServerUtils.strToDate(ServerUtils.getNode(fstElmnt, CompanyAttribute.COMPANY_STATUS_CHANGE_DATE), null));
				
				company.setDisplayMonitorEmailRecipients(ServerUtils.getNodeListElements(fstElmnt.getElementsByTagName(CompanyAttribute.NOTIFICATION_EMAIL)));
				
				company.setEnabledFeaturesJson(ServerUtils.getNode(fstElmnt, CompanyAttribute.ENABLED_FEATURES));
				
				String settingsString = ServerUtils.getNode(fstElmnt, CompanyAttribute.SETTINGS);
				company.setSettings(parsePnoSettings(settingsString));
				
				String parentSettingsString = ServerUtils.getNode(fstElmnt, "parentSettings");
				company.setParentSettings(parsePnoSettings(parentSettingsString));

				// if non-PNO inherit parent settings; now in UiControlBinder
//				if (company.getSettings() == null) {
//					company.setSettings(company.getParentSettings());
//				}
				
				company.setAuthKey(ServerUtils.getNode(fstElmnt, CompanyAttribute.AUTH_KEY));
				company.setClaimId(ServerUtils.getNode(fstElmnt, CompanyAttribute.CLAIM_ID));
				
				company.setCreationDate(ServerUtils.strToDate(ServerUtils.getNode(fstElmnt, CompanyAttribute.CREATION_DATE)));
				
				company.setChangedBy(ServerUtils.getNode(fstElmnt, CommonAttribute.CHANGED_BY));
				company.setChangeDate(ServerUtils.strToDate(ServerUtils.getNode(fstElmnt, CommonAttribute.CHANGE_DATE), null));
				
				AlertsInfo alertsSettings = new AlertsInfo();
				
				alertsSettings.setAllowAlerts(ServerUtils.StrToBoolean(ServerUtils.getNode(fstElmnt, CompanyAttribute.ALLOW_ALERTS)));
				alertsSettings.setAlertURL(ServerUtils.getNode(fstElmnt, CompanyAttribute.ALERT_URL));
				alertsSettings.setAlertUserName(ServerUtils.getNode(fstElmnt, CompanyAttribute.ALERT_USERNAME));
				alertsSettings.setAlertPassword(ServerUtils.getNode(fstElmnt, CompanyAttribute.ALERT_PASSWORD));
				
				alertsSettings.setAlertSenders(ServerUtils.getNodeListElements(fstElmnt.getElementsByTagName(CompanyAttribute.ALERT_SENDER)));
				alertsSettings.setAlertStatuses(ServerUtils.getNodeListElements(fstElmnt.getElementsByTagName(CompanyAttribute.ALERT_STATUS)));
				alertsSettings.setAlertHandlingCodes(ServerUtils.getNodeListElements(fstElmnt.getElementsByTagName(CompanyAttribute.ALERT_HANDLING_CODE)));
				alertsSettings.setAlertCategories(ServerUtils.getNodeListElements(fstElmnt.getElementsByTagName(CompanyAttribute.ALERT_CATEGORY)));
				alertsSettings.setAlertUrgencies(ServerUtils.getNodeListElements(fstElmnt.getElementsByTagName(CompanyAttribute.ALERT_URGENCY)));
				alertsSettings.setAlertSeverities(ServerUtils.getNodeListElements(fstElmnt.getElementsByTagName(CompanyAttribute.ALERT_SEVERITY)));
				alertsSettings.setAlertCertainties(ServerUtils.getNodeListElements(fstElmnt.getElementsByTagName(CompanyAttribute.ALERT_CERTAINTY)));
				alertsSettings.setAlertEventCodes(ServerUtils.getNodeListElements(fstElmnt.getElementsByTagName(CompanyAttribute.ALERT_EVENT_CODE)));
				alertsSettings.setAlertTextFields(ServerUtils.getNodeListElements(fstElmnt.getElementsByTagName(CompanyAttribute.ALERT_TEXT_FIELD)));
				
				alertsSettings.setAlertPresentationId(ServerUtils.getNode(fstElmnt, CompanyAttribute.ALERT_PRESENTATION_ID));
				alertsSettings.setAlertDistribution(ServerUtils.getNodeListElements(fstElmnt.getElementsByTagName(CompanyAttribute.DISPLAY_ID)));
				alertsSettings.setAlertExpiry(RiseUtils.strToInt(ServerUtils.getNode(fstElmnt, CompanyAttribute.ALERT_EXPIRY), 60));
				
				company.setAlertsSettings(alertsSettings);
				
				DemographicsInfo demographicsInfo = new DemographicsInfo();
				
				demographicsInfo.setCompanyType(ServerUtils.getNode(fstElmnt, CompanyAttribute.COMPANY_TYPE));
				demographicsInfo.setServicesProvided(ServerUtils.getNodeListElements(fstElmnt.getElementsByTagName(CompanyAttribute.SERVICE_PROVIDED)));
				demographicsInfo.setMarketSegments(ServerUtils.getNodeListElements(fstElmnt.getElementsByTagName(CompanyAttribute.MARKET_SEGMENT)));
				demographicsInfo.setTargetGroups(ServerUtils.getNodeListElements(fstElmnt.getElementsByTagName(CompanyAttribute.TARGET_GROUP)));
//				demographicsInfo.setViewsPerDisplay(ServerUtils.getNode(fstElmnt, CompanyAttribute.VIEWS_PER_DISPLAY));
				demographicsInfo.setAdvertisingRevenueEarn(ServerUtils.StrToBoolean(ServerUtils.getNode(fstElmnt, CompanyAttribute.ADVERTISING_REVENUE_EARN)));
				demographicsInfo.setAdvertisingRevenueInterested(ServerUtils.StrToBoolean(ServerUtils.getNode(fstElmnt, CompanyAttribute.ADVERTISING_REVENUE_INTERESTED)));

				company.setDemographicsInfo(demographicsInfo);
			
			}	
			return company;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
//	private ArrayList<CompanyInfo> parseGetCompanies(Document doc) {
//		ArrayList<CompanyInfo> companies = new ArrayList<CompanyInfo>();
//
//		try {
//			doc.getDocumentElement().normalize();
//
//			NodeList nodeList = doc.getElementsByTagName("company");
//
//			for (int s = 0; s < nodeList.getLength(); s++) {
//
//				Node fstNode = nodeList.item(s);
//
//				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
//					CompanyInfo c = new CompanyInfo();
//
//					Element fstElmnt = (Element) fstNode;
//
//					c.setId(ServerUtils.getNode(fstElmnt, CompanyAttribute.ID));
//					c.setName(ServerUtils.getNode(fstElmnt, CompanyAttribute.NAME));
//					c.setAddress(ServerUtils.getNode(fstElmnt, CompanyAttribute.ADDRESS));
//					companies.add(c);
//				}
//			}
//			return companies;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return null;
//	}
	
	private static HashMap<String, String> parsePnoSettings(String settingsString) {
		if (!RiseUtils.strIsNullOrEmpty(settingsString)) {
			try {
				JSONArray settingsArray = new JSONArray(settingsString);
	
				HashMap<String, String> settings = new HashMap<String, String>();
	
				for (int i = 0; i < settingsArray.length(); i++) {
					JSONObject setting = settingsArray.getJSONObject(i);
					String name = setting.getString("name");
					String value = setting.optString("value", null);
					
					settings.put(name, value);
				}
				
				return settings;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}

//	private CompanyInfo parsePnoSettings(Document doc) {
//		boolean firstEntry = true;
//		CompanyInfo company = new CompanyInfo();
//		HashMap<String, String> settings = new HashMap<String, String>();
//
//		try {
//			doc.getDocumentElement().normalize();
//
//			NodeList nodeList = doc.getElementsByTagName("setting");
//
//			for (int s = 0; s < nodeList.getLength(); s++) {
//
//				Node fstNode = nodeList.item(s);
//
//				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
//					Element fstElmnt = (Element) fstNode;
//					String nodeName;
//					String nodeValue;
//					
//					nodeName = ServerUtils.getNode(fstElmnt, "name");
//					nodeValue = ServerUtils.getNode(fstElmnt, "value");
//
////					nodeName = RiseServerUtils.getNode(fstElmnt, CompanySettingAttribute.NAME);
////					nodeValue[0] = RiseServerUtils.getNode(fstElmnt, CompanySettingAttribute.VALUE);
//					
//					if (firstEntry) {
//						company.setChangedBy(ServerUtils.getNode(fstElmnt, CommonAttribute.CHANGED_BY));
//						company.setChangeDate(ServerUtils.strToDate(ServerUtils.getNode(fstElmnt, CommonAttribute.CHANGE_DATE), null));
//						firstEntry = false;
//					}
//					
//					settings.put(nodeName, nodeValue);
//				}
//			}
//			company.setSettings(settings);
//			return company;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	private String parseGetCompanyAuthKey(Document doc) {
		try {
			doc.getDocumentElement().normalize();

			Node rootNode = doc.getDocumentElement();

			if (rootNode.getNodeType() == Node.ELEMENT_NODE) {
				Element fstElmnt = (Element) rootNode;
				
				String companyAuthKey = ServerUtils.getNode(fstElmnt, AuthKeyAttribute.VALUE);		
				return companyAuthKey;
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	private String parseGetCompanyClaimId(Document doc) {
		try {
			doc.getDocumentElement().normalize();

			Node rootNode = doc.getDocumentElement();

			if (rootNode.getNodeType() == Node.ELEMENT_NODE) {
				Element fstElmnt = (Element) rootNode;
				
				String companyClaimId = ServerUtils.getNode(fstElmnt, CompanyAttribute.CLAIM_ID);		
				return companyClaimId;
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	private String parseGetAlertsURL(Document doc) {
		try {
			doc.getDocumentElement().normalize();

			Node rootNode = doc.getDocumentElement();

			if (rootNode.getNodeType() == Node.ELEMENT_NODE) {
				Element fstElmnt = (Element) rootNode;
				
				String alertsURL = ServerUtils.getNode(fstElmnt, CompanyAttribute.ALERT_URL);		
				return alertsURL;
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	private DemoContentInfo parseGetDemoContent(Document doc) {
		try {
			doc.getDocumentElement().normalize();

			Node rootNode = doc.getDocumentElement();

			if (rootNode.getNodeType() == Node.ELEMENT_NODE) {
				Element fstElmnt = (Element) rootNode;
				
				DemoContentInfo demoContent = new DemoContentInfo();
				demoContent.setWidth(RiseUtils.strToInt(ServerUtils.getNode(fstElmnt, DemoAttribute.WIDTH), -1));
				demoContent.setHeight(RiseUtils.strToInt(ServerUtils.getNode(fstElmnt, DemoAttribute.HEIGHT), -1));
				demoContent.setObjectRef(ServerUtils.getNode(fstElmnt, DemoAttribute.OBJECT_REFERENCE));
				demoContent.setChangedBy(ServerUtils.getNode(fstElmnt, CommonAttribute.CHANGED_BY));
				demoContent.setChangeDate(ServerUtils.strToDate(ServerUtils.getNode(fstElmnt, CommonAttribute.CHANGE_DATE), null));

				return demoContent;
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
//	private ArrayList<DemoContentInfo> parseGetDemoContentList(Document doc) {
//		try {
//			ArrayList<DemoContentInfo> demoContentList = new ArrayList<DemoContentInfo>();
//
//			doc.getDocumentElement().normalize();
//
//			NodeList nodeList = doc.getElementsByTagName(Entity.DEMO);
//
//			for (int s = 0; s < nodeList.getLength(); s++) {
//				Node fstNode = nodeList.item(s);
//				
//				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
//					Element fstElmnt = (Element) fstNode;
//					
//					DemoContentInfo demoContent = new DemoContentInfo();
//					demoContent.setWidth(RiseUtils.strToInt(ServerUtils.getNode(fstElmnt, DemoAttribute.WIDTH), -1));
//					demoContent.setHeight(RiseUtils.strToInt(ServerUtils.getNode(fstElmnt, DemoAttribute.HEIGHT), -1));
//					demoContent.setObjectRef(ServerUtils.getNode(fstElmnt, DemoAttribute.OBJECT_REFERENCE));
//					demoContent.setChangedBy(ServerUtils.getNode(fstElmnt, CommonAttribute.CHANGED_BY));
//					demoContent.setChangeDate(ServerUtils.strToDate(ServerUtils.getNode(fstElmnt, CommonAttribute.CHANGE_DATE), null));
//					
//					demoContentList.add(demoContent);
//				}
//			}
//			return demoContentList;
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return null;
//	}
}
