// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.data;

import java.util.ArrayList;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.risevision.common.client.json.JSOModel;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.core.api.attributes.CommonAttribute;
import com.risevision.core.api.attributes.CompanyAttribute;
import com.risevision.ui.client.common.info.CompaniesInfo;
import com.risevision.ui.client.common.info.CompanyInfo;
import com.risevision.ui.client.common.info.ScrollingGridInfo;

@Deprecated
public class CompanyDataController extends DataControllerBase {
	protected static CompanyDataController instance;

	private final String[] companySearchCategories = new String[] {
			CompanyAttribute.NAME,
			CompanyAttribute.ID,
			CompanyAttribute.STREET,
			CompanyAttribute.UNIT,
			CompanyAttribute.CITY,
			CompanyAttribute.PROVINCE,
			CompanyAttribute.COUNTRY,
			CompanyAttribute.POSTAL,
//			CompanyAttribute.STATUS
			};
	
	public static CompanyDataController getInstance() {
		try {
			if (instance == null)
				instance = new CompanyDataController();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}
	
	public CompanyDataController() {
		headerEntries.add(new HeaderEntry(CompanyAttribute.ID));
		headerEntries.add(new HeaderEntry(CompanyAttribute.NAME));
		
		headerEntries.add(new HeaderEntry(CompanyAttribute.STREET));
		headerEntries.add(new HeaderEntry(CompanyAttribute.UNIT));
		headerEntries.add(new HeaderEntry(CompanyAttribute.CITY));
		headerEntries.add(new HeaderEntry(CompanyAttribute.PROVINCE));
		headerEntries.add(new HeaderEntry(CompanyAttribute.COUNTRY));
		headerEntries.add(new HeaderEntry(CompanyAttribute.POSTAL));
		headerEntries.add(new HeaderEntry(CompanyAttribute.ADDRESS));
		headerEntries.add(new HeaderEntry(CompanyAttribute.TIMEZONE));
		headerEntries.add(new HeaderEntry(CompanyAttribute.TELEPHONE));
		headerEntries.add(new HeaderEntry(CompanyAttribute.FAX));
		headerEntries.add(new HeaderEntry(CompanyAttribute.PARENT));
		headerEntries.add(new HeaderEntry(CompanyAttribute.NETWORK_OPERATOR_STATUS));
//		headerEntries.add(new HeaderEntry(CompanyAttribute.NETWORK_OPERATOR_STATUS_CHANGE_DATE));
		headerEntries.add(new HeaderEntry(CompanyAttribute.COMPANY_STATUS));
		headerEntries.add(new HeaderEntry(CompanyAttribute.COMPANY_STATUS_CHANGE_DATE));
		headerEntries.add(new HeaderEntry(CompanyAttribute.NOTIFICATION_EMAILS));
		headerEntries.add(new HeaderEntry(CommonAttribute.CHANGED_BY));
		headerEntries.add(new HeaderEntry(CommonAttribute.CHANGE_DATE));
	}
	
	private class CompaniesDataResponse extends DataResponseBase {
		protected CompaniesInfo companiesInfo;
		protected AsyncCallback<CompaniesInfo> callback;
		
		protected CompaniesDataResponse(CompaniesInfo companiesInfo, AsyncCallback<CompaniesInfo> callback) {
			this.companiesInfo = companiesInfo;
			this.callback = callback;
		}
		
		protected void onResponseImpl(JSOModel jso) {
			parseCompaniesData(jso, this);
		}
		
		protected void onErrorImpl(Throwable caught) {
			callback.onFailure(caught);
		}
	}
	
	// Unused; provides a flat list, only called by CompanyListBoxWidget which is deprecated  
	public void getNetworkOperators(String companyId, String excludeCompanyId, AsyncCallback<CompaniesInfo> callback) {
		CompaniesInfo companiesInfo = new CompaniesInfo();
		companiesInfo.setCompanyId(companyId);
		companiesInfo.setSortByDefault(CompanyAttribute.NAME);
		companiesInfo.setSortDirection(ScrollingGridInfo.SORT_DOWN);
		companiesInfo.setPageSize(-1);
		
		getNetworkOperators(excludeCompanyId, companiesInfo, callback);
	}
	
	public void getNetworkOperators(String excludeCompanyId, CompaniesInfo companiesInfo, AsyncCallback<CompaniesInfo> callback) {
		String action = "/company/" + companiesInfo.getCompanyId() + "/networkoperators";
		
		if (excludeCompanyId != null && !excludeCompanyId.isEmpty())
			action += "?excludeId=" + excludeCompanyId;
		
		String method = "GET"; 
		
		String customQuery = createNetworkOperatorsSearchString(companiesInfo.getSearchFor(), excludeCompanyId);

		String query = createQuery(customQuery, companiesInfo, companySearchCategories);
		
		CompaniesDataResponse response = new CompaniesDataResponse(companiesInfo, callback);
		
		getData(action, method, query, response);
	}
	
	public void getNetworkOperator(String companyId, String operatorId, AsyncCallback<CompaniesInfo> callback) {
		String action = "/company/" + companyId + "/networkoperators";
		
		String customQuery = "";
		if (operatorId != null && !operatorId.isEmpty())
			customQuery = CompanyAttribute.ID + " = '" + operatorId + "'";
		
		String method = "GET"; 
		
		CompaniesInfo companiesInfo = new CompaniesInfo();
		String query = createQuery(customQuery, companiesInfo, companySearchCategories);
		
		CompaniesDataResponse response = new CompaniesDataResponse(companiesInfo, callback);
		
		getData(action, method, query, response);
	}
	
	private String createNetworkOperatorsSearchString(String searchFor, String excludeCompanyId) {
		String query = "";
		
		if (!RiseUtils.strIsNullOrEmpty(excludeCompanyId)) {
			query += CompanyAttribute.ID + " != '" + excludeCompanyId + "'";
			query += " and " + CompanyAttribute.PARENT + " != '" + excludeCompanyId + "'";

			if (!RiseUtils.strIsNullOrEmpty(searchFor)) {
				query = " and (" + query + ")";
			}
		}
		
		return query;
	}
	
	public void getCompanies(String parentCompanyId, CompaniesInfo companiesInfo, AsyncCallback<CompaniesInfo> callback) {		
		String action = "/company/" + parentCompanyId + "/companies";
		String method = "GET"; 
		
		String customQuery = createCompaniesSearchString(companiesInfo.getSearchFor(), parentCompanyId);
		String query = createQuery(customQuery, companiesInfo, companySearchCategories);
		
		CompaniesDataResponse response = new CompaniesDataResponse(companiesInfo, callback);
		
		getData(action, method, query, response);
	}
	
	private String createCompaniesSearchString(String searchFor, String parentCompanyId) {
		String query = "";
		
		if (RiseUtils.strIsNullOrEmpty(searchFor)) {
			query += CompanyAttribute.PARENT + " = '" + parentCompanyId + "'";
		}
		
		return query;
	}

	protected void parseCompaniesData(JSOModel jsCompanies, CompaniesDataResponse response) {

		JsArray<JSOModel> rows = jsCompanies.getArray("rows");
		
		CompaniesInfo companiesInfo = response.companiesInfo;

		ArrayList<CompanyInfo> companies = companiesInfo.getCompanies();
		if (companiesInfo.getPage() == 0) {
			companies = new ArrayList<CompanyInfo>();
		}
		
		for (int i = 0; i < rows.length(); i++) {
			JSOModel row = rows.get(i);
			
			JsArray<JSOModel> column = row.getArray("c");
			
			CompanyInfo company = new CompanyInfo();
			int j = 0;
			
			company.setId(column.get(headerEntries.get(j++).col).get("v"));
			company.setName(column.get(headerEntries.get(j++).col).get("v"));
			
			company.setStreet(column.get(headerEntries.get(j++).col).get("v"));
			company.setUnit(column.get(headerEntries.get(j++).col).get("v"));
			company.setCity(column.get(headerEntries.get(j++).col).get("v"));
			company.setProvince(column.get(headerEntries.get(j++).col).get("v"));
			company.setCountry(column.get(headerEntries.get(j++).col).get("v"));
			company.setPostalCode(column.get(headerEntries.get(j++).col).get("v"));
			company.setAddress(column.get(headerEntries.get(j++).col).get("v"));
			
			company.setTimeZone(column.get(headerEntries.get(j++).col).get("v"));
			company.setTelephone(column.get(headerEntries.get(j++).col).get("v"));
			company.setFax(column.get(headerEntries.get(j++).col).get("v"));
			company.setParentId(column.get(headerEntries.get(j++).col).get("v"));
			company.setPnoStatus(column.get(headerEntries.get(j++).col).getInt("v"));
			company.setCustomerStatus(column.get(headerEntries.get(j++).col).getInt("v"));
			company.setCustomerStatusChangeDate(column.get(headerEntries.get(j++).col).getDate("v"));
			
			ArrayList<String> emailsList = new ArrayList<String>();
			String[] emails = column.get(headerEntries.get(j++).col).get("v", "").split(",");
			for (String email: emails) {
				emailsList.add(email.trim());
			}
			company.setDisplayMonitorEmailRecipients(emailsList);
			
			company.setChangedBy(column.get(headerEntries.get(j++).col).get("v"));
			company.setChangeDate(column.get(headerEntries.get(j++).col).getDate("v"));
			
			companies.add(company);
		}
		
		companiesInfo.setCompanies(companies);
//		companiesInfo.setCurrentPage(1);
//		companiesInfo.setPageCount(1);
		
		response.callback.onSuccess(companiesInfo);
	}
	
}
