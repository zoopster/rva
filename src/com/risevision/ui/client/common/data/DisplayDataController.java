package com.risevision.ui.client.common.data;

import java.util.ArrayList;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.risevision.common.client.json.JSOModel;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.core.api.attributes.CommonAttribute;
import com.risevision.core.api.attributes.DisplayAttribute;
import com.risevision.core.api.attributes.ViewerStatusAttribute;
import com.risevision.core.api.types.DisplayStatus;
import com.risevision.ui.client.common.info.DisplayInfo;
import com.risevision.ui.client.common.info.DisplaysInfo;

@Deprecated
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
		
		protected void onResponseImpl(JSOModel jso) {
			parseDisplaysData(jso, this);
		}
		
		protected void onErrorImpl(Throwable caught) {
			callback.onFailure(caught);
		}
	}
	
	public void getDisplays(DisplaysInfo displaysInfo, boolean activeOnly, AsyncCallback<DisplaysInfo> callback) {
		String action = "/company/" + displaysInfo.getCompanyId() + "/displays";
		String method = "GET"; 
	
		String customQuery = createSearchString(displaysInfo.getSearchFor(), displaysInfo.getCompanyId(), activeOnly);
		String query = createQuery(customQuery, displaysInfo, displaySearchCategories);
		
		DisplaysDataResponse response = new DisplaysDataResponse(displaysInfo, callback);
		
		getData(action, method, query, response);
	}
	
	private String createSearchString(String searchFor, String companyId, boolean activeOnly) {
		String query = "";
			
		if (!RiseUtils.strIsNullOrEmpty(searchFor)) {
			// search width/height if the value is an integer
			if (RiseUtils.strToInt(searchFor, -1) != -1) {
				query += "(" + DisplayAttribute.WIDTH + " = " + searchFor;
				query += " or " + DisplayAttribute.HEIGHT + " = " + searchFor + ")";
			}
			
			if (searchFor.equalsIgnoreCase("active")) {
				query += query.isEmpty() ? "" : " or ";
				query += DisplayAttribute.STATUS + " = " + DisplayStatus.ACTIVE;
			}
			else if (searchFor.equalsIgnoreCase("inactive")) {
				query += query.isEmpty() ? "" : " or ";
				query += DisplayAttribute.STATUS + " = " + DisplayStatus.INACTIVE;
			}
			
			if (searchFor.contains("x")) {
				String[] resolutionTokens = searchFor.split("x");
				if (resolutionTokens.length == 2 && RiseUtils.strToInt(resolutionTokens[0], -1) != -1 &&
							RiseUtils.strToInt(resolutionTokens[1], -1) != -1) {
					query += query.isEmpty() ? "" : " or ";
					query += "(" + DisplayAttribute.WIDTH + " = " + RiseUtils.strToInt(resolutionTokens[0], -1);
					query += " and " + DisplayAttribute.HEIGHT + " = " + RiseUtils.strToInt(resolutionTokens[1], -1) + ")";
				}
			}
		}
		
		if (!query.isEmpty()) {
			query = " or (" + query + ")";
		}
		
		if (activeOnly) {
			query += RiseUtils.strIsNullOrEmpty(searchFor) ? " " : " and ";
			query += "(" + DisplayAttribute.STATUS + " = " + DisplayStatus.ACTIVE + ")";
		}
		
		if (RiseUtils.strIsNullOrEmpty(searchFor) || activeOnly) {
			query += RiseUtils.strIsNullOrEmpty(searchFor) && query.isEmpty() && !activeOnly ? "" : " and ";
			query += DisplayAttribute.COMPANY_ID + " = '" + companyId + "'";
		}
		
		return query;
	}

	protected void parseDisplaysData(JSOModel jsDisplays, DisplaysDataResponse response) {
//		String displays =  jsDisplays.getAsString();
//    	for (var i=0; i< jsoModel.table.rows.length; i++) {
//		logText("display name: " + jsoModel.table.rows[i].c[1].v);
//	}
	
		JsArray<JSOModel> rows = jsDisplays.getArray("rows");
		
		DisplaysInfo displaysInfo = response.displaysInfo;

		ArrayList<DisplayInfo> displays = displaysInfo.getDisplays();
		if (displaysInfo.getPage() == 0) {
			displays = new ArrayList<DisplayInfo>();
		}
		
		for (int i = 0; i < rows.length(); i++) {
			JSOModel row = rows.get(i);
			
			JsArray<JSOModel> column = row.getArray("c");
			
			DisplayInfo display = new DisplayInfo();
			int j = 0;
			
			display.setId(column.get(headerEntries.get(j++).col).get("v"));
			display.setCompanyId(column.get(headerEntries.get(j++).col).get("v"));
			display.setName(column.get(headerEntries.get(j++).col).get("v"));
			display.setSubscriptionStatus(column.get(headerEntries.get(j++).col).getInt("v"));
			display.setBrowserUpgradeMode(column.get(headerEntries.get(j++).col).getInt("v"));
			display.setWidth(column.get(headerEntries.get(j++).col).getInt("v"));
			display.setHeight(column.get(headerEntries.get(j++).col).getInt("v"));
			display.setAddress(column.get(headerEntries.get(j++).col).get("v"));
			display.setConnected(column.get(headerEntries.get(j++).col).getBoolean("v"));
			display.setLastConnectionDate(column.get(headerEntries.get(j++).col).getDate("v"));
			display.setChangedBy(column.get(headerEntries.get(j++).col).get("v"));
			display.setChangeDate(column.get(headerEntries.get(j++).col).getDate("v"));
			
			displays.add(display);
		}
		
		displaysInfo.setDisplays(displays);
//		displaysInfo.setCurrentPage(1);
//		displaysInfo.setPageCount(1);
		
		response.callback.onSuccess(displaysInfo);
	}
	
}
