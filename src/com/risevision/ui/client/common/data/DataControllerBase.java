package com.risevision.ui.client.common.data;

import java.util.ArrayList;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.risevision.common.client.json.JSOModel;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.core.api.attributes.CommonAttribute;
import com.risevision.core.api.attributes.CompanyAttribute;
import com.risevision.ui.client.common.exception.ServiceFailedException;
import com.risevision.ui.client.common.info.ScrollingGridInfo;

public abstract class DataControllerBase {
	protected ArrayList<HeaderEntry> headerEntries = new ArrayList<HeaderEntry>();
	
	// Search category applied for 1 character search
	protected String mainSearchCategory = CompanyAttribute.NAME;

	protected class HeaderEntry {
		public String id;
		public int col;
		
		public HeaderEntry(String id) {
			this.id = id;
		}
	}
	
	public abstract class DataResponseBase {	
		protected abstract void onResponseImpl(JSOModel jso);
		
		protected abstract void onErrorImpl(Throwable caught);
		
		protected void onResponse(JavaScriptObject jso) {
			JSOModel jsoModel = reportDataReady(jso);
			
			if (jsoModel != null) {
				parseHeaderEntries(jsoModel);
				onResponseImpl(jsoModel);
			}
			else {
				onError("");
			}
		}
		
		protected final void onError(String errorCode) {
			// Unfortunately error codes are not available for JSONP requests, so errors cannot be identified
			onErrorImpl(new ServiceFailedException());
		}
	}
	
	protected void getData(String urlToken, String method, String tq, DataResponseBase response) {
		DataAccessController.getData(urlToken, method, tq, response);
	}
	
	protected void getFinancialData(String urlToken, String tq, DataResponseBase response) {
		DataAccessController.getFinancialData(urlToken, tq, response, false);
	}
	
	protected void getFinancialData(String urlToken, String tq, DataResponseBase response, boolean customQuery) {
		DataAccessController.getFinancialData(urlToken, tq, response, customQuery);
	}
	
	protected String createQuery(ScrollingGridInfo gridInfo, String[] searchCategories) {
		return createQuery("", gridInfo, searchCategories);
	}
	
	protected String createQuery(String customQuery, ScrollingGridInfo gridInfo, String[] searchCategories) {
		String query = "";
		String searchFor = gridInfo.getSearchFor();
		
		if (!RiseUtils.strIsNullOrEmpty(searchFor)) {
			query += "where ";
			if (searchFor.length() == 1) {
				query += "(lower(" + mainSearchCategory + ") starts with '" + searchFor.toLowerCase() + "') ";
			}
			else {
				query += createSearchString(searchFor, searchCategories);
			}
		}
		else {
			query += customQuery.isEmpty() ? "" : "where ";
		}

		query += customQuery;
		query += createTQ(gridInfo);
		
		return query;
	}
	
	private String createSearchString(String searchFor, String[] searchCategories) {
		String searchString = "(";
		
		searchFor = searchFor.toLowerCase();
		searchFor = "%" + searchFor + "%";
		searchFor = searchFor.contains("'") ? "\"" + searchFor + "\"" : "'" + searchFor + "'";
		
		for (String category : searchCategories) {
			searchString += "(lower(" + category + ") like " + searchFor + ") or ";
		}
		searchString = searchString.substring(0, searchString.length() - 4) + ")";
		
		return searchString;
	}
	
	private String createTQ(ScrollingGridInfo gridInfo) {
		String tq = " ";
		
		if (!RiseUtils.strIsNullOrEmpty(gridInfo.getSortBy())) {
			tq += "order by " + gridInfo.getSortBy();
		}
		else {
			tq += "order by " + CommonAttribute.CHANGE_DATE;
		}
		
		if (!RiseUtils.strIsNullOrEmpty(gridInfo.getSortDirection())) {
			tq += " " + gridInfo.getSortDirection();
		}
		
		if (gridInfo.getPageSize() != -1) {
			tq += " limit " + gridInfo.getPageSize();
			tq += " offset " + (gridInfo.getPage() * gridInfo.getPageSize());
		}
		
		return tq;
	}
	
	private JSOModel reportDataReady(JavaScriptObject jso) {
 		JSOModel jsoModel = (JSOModel) jso;

		String status = jsoModel.get("status", "");	
		if (status.equalsIgnoreCase("OK") || status.equalsIgnoreCase("warning")) {
			return jsoModel.getObject("table");
		}
		else {
			// throw error
			return null;
		}
	}
	
	private void parseHeaderEntries(JSOModel jsoModel) {
		JsArray<JSOModel> header = jsoModel.getArray("cols");
		
		for (int i = 0; i < header.length(); i++) {
			JSOModel headerEntry = header.get(i);
			String headerId = headerEntry.get("id");
			
			for (HeaderEntry entry: headerEntries) {
				if (entry.id.equals(headerId)) {
					entry.col = i;
					break;
				}
			}
		}
	}
}
