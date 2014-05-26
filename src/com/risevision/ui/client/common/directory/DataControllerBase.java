// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.directory;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsDate;
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
		protected abstract void onResponseImpl(JavaScriptObject jsDataTable, String cursor);
		
		protected abstract void onErrorImpl(Throwable caught);
		
		protected void onResponse(JavaScriptObject jsDataTable, String cursor) {
//			JSOModel jsoModel = reportDataReady(jsonObjectString);
			
			if (jsDataTable != null) {
				parseHeaderEntries(jsDataTable);
				onResponseImpl(jsDataTable, cursor);
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
	
	protected void getData(String urlToken, String tq, DataResponseBase response) {
		DataAccessController.getData(urlToken, tq, response);
	}
	
	protected String createQuery(ScrollingGridInfo gridInfo, String[] searchCategories) {
		return createQuery("", gridInfo, searchCategories);
	}
	
	protected String createQuery(String customQuery, ScrollingGridInfo gridInfo, String[] searchCategories) {
		String query = "&search=";
		String searchFor = gridInfo.getSearchFor();
		
		if (!RiseUtils.strIsNullOrEmpty(searchFor)) {
//			query += "where ";
//			if (searchFor.length() == 1) {
//				query += "" + mainSearchCategory + ": ~" + searchFor.toLowerCase() + " ";
//			}
//			else {
				query += createSearchString(searchFor, searchCategories);
//			}
		}
//		else {
//			query += customQuery.isEmpty() ? "" : "where ";
//		}

		query += customQuery;
		query += createTQ(gridInfo);
		
		return query;
	}
	
	private String createSearchString(String searchFor, String[] searchCategories) {
		String searchString = "";
		
		searchFor = searchFor.toLowerCase();
//		searchFor = "%" + searchFor + "%";
		searchFor = searchFor.contains("'") ? "\"" + searchFor + "\"" : "'" + searchFor + "'";
		searchFor = "~" + searchFor;
		
		for (String category : searchCategories) {
			searchString += "" + category + ": " + searchFor + " OR ";
		}
		searchString = searchString.substring(0, searchString.length() - 4) + "";
//		searchString = searchFor;
		
		return searchString;
	}
	
	private String createTQ(ScrollingGridInfo gridInfo) {
		String tq = "";
		
		if (!RiseUtils.strIsNullOrEmpty(gridInfo.getSortBy())) {
			tq += "&sort=" + gridInfo.getSortBy();
		}
		else {
			tq += "&sort=" + CommonAttribute.CHANGE_DATE;
		}
		
		if (!RiseUtils.strIsNullOrEmpty(gridInfo.getSortDirection())) {
			tq += " " + gridInfo.getSortDirection();
		}
		
		if (gridInfo.getPageSize() != -1) {
			tq += "&count=" + gridInfo.getPageSize();
			if (gridInfo.getPage() > 0 && !RiseUtils.strIsNullOrEmpty(gridInfo.getCursor())) {
				tq += "&cursor=" + gridInfo.getCursor();
			}
		}
		
		return tq;
	}
	
//	private JSOModel reportDataReady(String jsonObjectString) {
// 		JSOModel jsoModel = JSOModel.fromJson(jsonObjectString);
//
//		return jsoModel;
//	}
	
//	private void parseHeaderEntries(JSOModel jsoModel) {
//		JsArray<JSOModel> header = jsoModel.getArray("cols");
//		
//		for (int i = 0; i < header.length(); i++) {
//			JSOModel headerEntry = header.get(i);
//			String headerId = headerEntry.get("id");
//			
//			for (HeaderEntry entry: headerEntries) {
//				if (entry.id.equals(headerId)) {
//					entry.col = i;
//					break;
//				}
//			}
//		}
//	}
	
	private void parseHeaderEntries(JavaScriptObject jsDataTable) {
		for (int i = 0; i < getNumberOfColumns(jsDataTable); i++) {
			String headerId = getColumnId(jsDataTable, i);
			
			for (HeaderEntry entry: headerEntries) {
				if (entry.id.equals(headerId)) {
					entry.col = i;
					break;
				}
			}
		}
	}
	
	protected native int getNumberOfColumns(JavaScriptObject jsDataTable) /*-{
		try {
			return jsDataTable.getNumberOfColumns();
		}
		catch (e) {
			return 0;
		}
	}-*/;
	
	protected native int getNumberOfRows(JavaScriptObject jsDataTable) /*-{
		try {
			return jsDataTable.getNumberOfRows();
		}
		catch (e) {
			return 0;
		}
	}-*/;
	
	private native String getColumnId(JavaScriptObject jsDataTable, int columnIndex) /*-{
		try {
			return jsDataTable.getColumnId(columnIndex);
		}
		catch (e) {
			return "";
		}
	}-*/;	

	protected native String getValue(JavaScriptObject jsDataTable, int columnIndex, int rowIndex) /*-{
		try {
			return jsDataTable.getValue(columnIndex, rowIndex);
		}
		catch (e) {
			return "";
		}
	}-*/;	
	
	protected native int getValueInt(JavaScriptObject jsDataTable, int columnIndex, int rowIndex) /*-{
		try {
			return jsDataTable.getValue(columnIndex, rowIndex);
		}
		catch (e) {
			return 0;
		}
	}-*/;	
	
	protected native double getValueDouble(JavaScriptObject jsDataTable, int columnIndex, int rowIndex) /*-{
		try {
			return jsDataTable.getValue(columnIndex, rowIndex);
		}
		catch (e) {
			return 0.0;
		}
	}-*/;	
	
	protected native boolean getValueBoolean(JavaScriptObject jsDataTable, int columnIndex, int rowIndex) /*-{
		try {
			return jsDataTable.getValue(columnIndex, rowIndex);
		}
		catch (e) {
			return false;
		}
	}-*/;
	
	protected Date getValueDate(JavaScriptObject jsDataTable, int columnIndex, int rowIndex) {
		JsDate jsDate = getValueDateNative(jsDataTable, columnIndex, rowIndex);
		if (jsDate != null) {
			return new Date((long) jsDate.getTime());
		}
		else {
			return null;
		}
	}
	
	protected native JsDate getValueDateNative(JavaScriptObject jsDataTable, int columnIndex, int rowIndex) /*-{
		try {
			var date = jsDataTable.getValue(columnIndex, rowIndex);	
			
//			if (!isNaN(date.getTime())) {
				return new Date(date.getTime() - (date.getTimezoneOffset() * 60 * 1000));
//			}
//			else 
//				return null;
		}
		catch (e) {
			return null;
		}
	}-*/;	
	
}
