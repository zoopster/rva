// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.data;

import java.util.ArrayList;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.risevision.common.client.json.JSOModel;
import com.risevision.core.api.attributes.CommonAttribute;
import com.risevision.core.api.attributes.GadgetAttribute;
import com.risevision.ui.client.common.info.GadgetInfo;
import com.risevision.ui.client.common.info.GadgetsInfo;

public class GadgetDataController extends DataControllerBase {
	private static GadgetDataController instance;
	private final String[] gadgetsSearchCategories = new String[] {
			GadgetAttribute.NAME,
			GadgetAttribute.ID,
			GadgetAttribute.GADGET_TYPE,
			GadgetAttribute.URL,
			GadgetAttribute.UI_URL,
			GadgetAttribute.HELP_URL,
			GadgetAttribute.AUTHOR,
			GadgetAttribute.AUTHOR_URL,
			GadgetAttribute.CATEGORY,
			GadgetAttribute.DESCRIPTION,
			GadgetAttribute.THUMBNAIL_URL,
			GadgetAttribute.SCREENSHOT_URL
//			DisplayAttribute.STATUS
			};
	
	public static GadgetDataController getInstance() {
		try {
			if (instance == null)
				instance = new GadgetDataController();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (GadgetDataController) instance;
	}
	
	public GadgetDataController() {
		headerEntries.add(new HeaderEntry(GadgetAttribute.ID));
		headerEntries.add(new HeaderEntry(GadgetAttribute.NAME));
		headerEntries.add(new HeaderEntry(GadgetAttribute.GADGET_TYPE));
		headerEntries.add(new HeaderEntry(GadgetAttribute.URL));
		headerEntries.add(new HeaderEntry(GadgetAttribute.UI_URL));
		headerEntries.add(new HeaderEntry(GadgetAttribute.HELP_URL));
		headerEntries.add(new HeaderEntry(GadgetAttribute.AUTHOR));
		headerEntries.add(new HeaderEntry(GadgetAttribute.AUTHOR_URL));
		headerEntries.add(new HeaderEntry(GadgetAttribute.CATEGORY));
		headerEntries.add(new HeaderEntry(GadgetAttribute.DESCRIPTION));
		headerEntries.add(new HeaderEntry(GadgetAttribute.THUMBNAIL_URL));
		headerEntries.add(new HeaderEntry(GadgetAttribute.SCREENSHOT_URL));
		headerEntries.add(new HeaderEntry(GadgetAttribute.SHARED));
		headerEntries.add(new HeaderEntry(CommonAttribute.CHANGED_BY));
		headerEntries.add(new HeaderEntry(CommonAttribute.CHANGE_DATE));
	}
	
	private class GadgetsDataResponse extends DataResponseBase {
		protected GadgetsInfo gadgetsInfo;
		protected AsyncCallback<GadgetsInfo> callback;
		
		protected GadgetsDataResponse(GadgetsInfo gadgetsInfo, AsyncCallback<GadgetsInfo> callback) {
			this.gadgetsInfo = gadgetsInfo;
			this.callback = callback;
		}
		
		protected void onResponseImpl(JSOModel jso) {
			parseGadgetsData(jso, this);
		}
		
		protected void onErrorImpl(Throwable caught) {
			callback.onFailure(caught);
		}
	}
	
	public void getGadgets(GadgetsInfo gadgetsInfo, boolean sharedGadgets, AsyncCallback<GadgetsInfo> callback) {
		String action;
		if (sharedGadgets) {
			action = "/company/" + gadgetsInfo.getCompanyId() + "/sharedgadgets";	
		}
		else {
			action = "/company/" + gadgetsInfo.getCompanyId() + "/gadgets";
		}
		 
		String method = "GET"; 
	
		String customQuery = createSearchString(gadgetsInfo.getSearchFor());
		String query = createQuery(customQuery, gadgetsInfo, gadgetsSearchCategories);
		
		GadgetsDataResponse response = new GadgetsDataResponse(gadgetsInfo, callback);
		
		getData(action, method, query, response);
	}
	
	private String createSearchString(String searchFor) {
		String query = "";
		
		return query;
	}

	protected void parseGadgetsData(JSOModel jsGadgets, GadgetsDataResponse response) {

		JsArray<JSOModel> rows = jsGadgets.getArray("rows");
		
		GadgetsInfo gadgetsInfo = response.gadgetsInfo;

		ArrayList<GadgetInfo> gadgets = gadgetsInfo.getGadgets();
		if (gadgetsInfo.getPage() == 0) {
			gadgets = new ArrayList<GadgetInfo>();
		}
		
		for (int i = 0; i < rows.length(); i++) {
			JSOModel row = rows.get(i);
			
			JsArray<JSOModel> column = row.getArray("c");
			
			GadgetInfo gadget = new GadgetInfo();
			int j = 0;
			
			gadget.setId(column.get(headerEntries.get(j++).col).get("v"));
			gadget.setName(column.get(headerEntries.get(j++).col).get("v"));
			gadget.setType(column.get(headerEntries.get(j++).col).get("v"));
			gadget.setUrl(column.get(headerEntries.get(j++).col).get("v"));
			gadget.setUiUrl(column.get(headerEntries.get(j++).col).get("v"));
			gadget.setHelpUrl(column.get(headerEntries.get(j++).col).get("v"));
			gadget.setAuthorName(column.get(headerEntries.get(j++).col).get("v"));
			gadget.setAuthorUrl(column.get(headerEntries.get(j++).col).get("v"));
			gadget.setCategory(column.get(headerEntries.get(j++).col).get("v"));
			gadget.setDescription(column.get(headerEntries.get(j++).col).get("v"));
			gadget.setThumbnailUrl(column.get(headerEntries.get(j++).col).get("v"));
			gadget.setScreenshotUrl(column.get(headerEntries.get(j++).col).get("v"));
			gadget.setShared(column.get(headerEntries.get(j++).col).getBoolean("v"));
			gadget.setChangedBy(column.get(headerEntries.get(j++).col).get("v"));
			gadget.setChangeDate(column.get(headerEntries.get(j++).col).getDate("v"));
			
			gadgets.add(gadget);
		}
		
		gadgetsInfo.setGadgets(gadgets);
//		displaysInfo.setCurrentPage(1);
//		displaysInfo.setPageCount(1);
		
		response.callback.onSuccess(gadgetsInfo);
	}
	
}
