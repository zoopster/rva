// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.directory;

import java.util.ArrayList;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.core.api.attributes.CommonAttribute;
import com.risevision.core.api.attributes.DisplayAttribute;
import com.risevision.core.api.attributes.ViewerStatusAttribute;
import com.risevision.core.api.types.DisplayStatus;
import com.risevision.ui.client.common.info.DisplayInfo;
import com.risevision.ui.client.common.info.DisplaysInfo;

public class DisplayDataController extends DataControllerBase {
	private static DisplayDataController instance;
	private final String[] displaySearchCategories = new String[] {
			DisplayAttribute.NAME,
			DisplayAttribute.ID,
//			DisplayAttribute.HEIGHT,
//			DisplayAttribute.WIDTH,
			DisplayAttribute.ADDRESS_DESCRIPTION,
			DisplayAttribute.STREET,
			DisplayAttribute.UNIT,
			DisplayAttribute.CITY,
			DisplayAttribute.PROVINCE,
			DisplayAttribute.COUNTRY,
			DisplayAttribute.POSTAL,
//			DisplayAttribute.STATUS
			};
	
	public static DisplayDataController getInstance() {
		try {
			if (instance == null)
				instance = new DisplayDataController();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (DisplayDataController) instance;
	}
	
	public DisplayDataController() {
		headerEntries.add(new HeaderEntry(DisplayAttribute.ID));
		headerEntries.add(new HeaderEntry(DisplayAttribute.COMPANY_ID));
		headerEntries.add(new HeaderEntry(DisplayAttribute.NAME));
		headerEntries.add(new HeaderEntry(DisplayAttribute.STATUS));
		headerEntries.add(new HeaderEntry(DisplayAttribute.BROWSER_UPGRADE_MODE));
		headerEntries.add(new HeaderEntry(DisplayAttribute.WIDTH));
		headerEntries.add(new HeaderEntry(DisplayAttribute.HEIGHT));
		headerEntries.add(new HeaderEntry(DisplayAttribute.ADDRESS));
		headerEntries.add(new HeaderEntry(ViewerStatusAttribute.CONNECTED));
		headerEntries.add(new HeaderEntry(ViewerStatusAttribute.LAST_CONNECTION_DATE));
		headerEntries.add(new HeaderEntry(DisplayAttribute.PLAYER_STATUS));
		headerEntries.add(new HeaderEntry(DisplayAttribute.BLOCK_EXPIRY));
		headerEntries.add(new HeaderEntry(CommonAttribute.CHANGED_BY));
		headerEntries.add(new HeaderEntry(CommonAttribute.CHANGE_DATE));
	}
	
	private class DisplaysDataResponse extends DataResponseBase {
		protected DisplaysInfo displaysInfo;
		protected AsyncCallback<DisplaysInfo> callback;
		
		protected DisplaysDataResponse(DisplaysInfo displaysInfo, AsyncCallback<DisplaysInfo> callback) {
			this.displaysInfo = displaysInfo;
			this.callback = callback;
		}
		
		protected void onResponseImpl(JavaScriptObject jsDataTable, String cursor) {
			displaysInfo.setCursor(cursor);
			try {
				parseDisplaysData(jsDataTable, this);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		protected void onErrorImpl(Throwable caught) {
			callback.onFailure(caught);
		}
	}
	
	public void getDisplays(DisplaysInfo displaysInfo, boolean activeOnly, AsyncCallback<DisplaysInfo> callback) {
		String companyId = displaysInfo.getCompanyId();
		String action = "/company/" + companyId + "/displays";
	
		String customQuery = createSearchString(displaysInfo.getSearchFor(), companyId, activeOnly);
		String query = createQuery(customQuery, displaysInfo, displaySearchCategories);
		
		DisplaysDataResponse response = new DisplaysDataResponse(displaysInfo, callback);
		
		getData(action, query, response);
	}
	
	private String createSearchString(String searchFor, String companyId, boolean activeOnly) {
		String query = "";
			
		if (!RiseUtils.strIsNullOrEmpty(searchFor)) {
			// search width/height if the value is an integer
			if (RiseUtils.strToInt(searchFor, -1) != -1) {
				query += "" + DisplayAttribute.WIDTH + ": " + searchFor;
				query += " OR " + DisplayAttribute.HEIGHT + ": " + searchFor + " ";
			}
			
			if (searchFor.equalsIgnoreCase("active")) {
				query += query.isEmpty() ? "" : " OR ";
				query += DisplayAttribute.STATUS + ": " + DisplayStatus.ACTIVE;
			}
			else if (searchFor.equalsIgnoreCase("inactive")) {
				query += query.isEmpty() ? "" : " OR ";
				query += DisplayAttribute.STATUS + ": " + DisplayStatus.INACTIVE;
			}
			
			if (searchFor.contains("x")) {
				String[] resolutionTokens = searchFor.split("x");
				if (resolutionTokens.length == 2 && RiseUtils.strToInt(resolutionTokens[0], -1) != -1 &&
							RiseUtils.strToInt(resolutionTokens[1], -1) != -1) {
					query += query.isEmpty() ? "" : " OR ";
					query += "(" + DisplayAttribute.WIDTH + ": " + RiseUtils.strToInt(resolutionTokens[0], -1);
					query += " AND " + DisplayAttribute.HEIGHT + ": " + RiseUtils.strToInt(resolutionTokens[1], -1) + ")";
				}
			}
		}
		
		if (!query.isEmpty()) {
			query = " OR (" + query + ")";
		}
		
		if (activeOnly) {
			query += RiseUtils.strIsNullOrEmpty(searchFor) ? " " : " AND ";
			query += "" + DisplayAttribute.STATUS + ": " + DisplayStatus.ACTIVE + "";
		}
		
		if (RiseUtils.strIsNullOrEmpty(searchFor) || activeOnly) {
			query += RiseUtils.strIsNullOrEmpty(searchFor) && query.isEmpty() && !activeOnly ? "" : " AND ";
			query += DisplayAttribute.COMPANY_ID + ": " + companyId + " ";
		}
		
		return query;
	}

	protected void parseDisplaysData(JavaScriptObject jsDataTable, DisplaysDataResponse response) {
//		String displays =  jsDisplays.getAsString();
//    	for (var i=0; i< jsoModel.table.rows.length; i++) {
//		logText("display name: " + jsoModel.table.rows[i].c[1].v);
//	}
	
		DisplaysInfo displaysInfo = response.displaysInfo;

		ArrayList<DisplayInfo> displays = displaysInfo.getDisplays();
		if (displaysInfo.getPage() == 0) {
			displays = new ArrayList<DisplayInfo>();
		}
		
		for (int i = 0; i < getNumberOfRows(jsDataTable); i++) {
			
			DisplayInfo display = new DisplayInfo();
			int j = 0;
			
			display.setId(getValue(jsDataTable, i, headerEntries.get(j++).col));
			display.setCompanyId(getValue(jsDataTable, i, headerEntries.get(j++).col));
			display.setName(getValue(jsDataTable, i, headerEntries.get(j++).col));
			display.setSubscriptionStatus(getValueInt(jsDataTable, i, headerEntries.get(j++).col));
			display.setBrowserUpgradeMode(getValueInt(jsDataTable, i, headerEntries.get(j++).col));
			display.setWidth(getValueInt(jsDataTable, i, headerEntries.get(j++).col));
			display.setHeight(getValueInt(jsDataTable, i, headerEntries.get(j++).col));
			display.setAddress(getValue(jsDataTable, i, headerEntries.get(j++).col));
			display.setConnected(getValueBoolean(jsDataTable, i, headerEntries.get(j++).col));
			display.setLastConnectionDate(getValueDate(jsDataTable, i, headerEntries.get(j++).col));
			display.setPlayerStatus(getValueInt(jsDataTable, i, headerEntries.get(j++).col));
			display.setBlockExpiryDate(getValueDate(jsDataTable, i, headerEntries.get(j++).col));
			display.setChangedBy(getValue(jsDataTable, i, headerEntries.get(j++).col));
			display.setChangeDate(getValueDate(jsDataTable, i, headerEntries.get(j++).col));
			
			displays.add(display);
		}
		
		displaysInfo.setDisplays(displays);
//		displaysInfo.setCurrentPage(1);
//		displaysInfo.setPageCount(1);
		
		response.callback.onSuccess(displaysInfo);
	}
	
}
