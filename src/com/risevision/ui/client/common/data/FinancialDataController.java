// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.data;

import java.util.ArrayList;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.risevision.common.client.json.JSOModel;
import com.risevision.ui.client.common.info.FinancialItemInfo;
import com.risevision.ui.client.common.info.FinancialItemsInfo;

public class FinancialDataController extends DataControllerBase {
	protected static FinancialDataController instance;
	
	private final String[] financialSearchCategories = new String[] {
			FinancialItemInfo.CODE_ATTRIBUTE,
			FinancialItemInfo.NAME_ATTRIBUTE,
			};
	
	public static FinancialDataController getInstance() {
		try {
			if (instance == null)
				instance = new FinancialDataController();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}
	
	public FinancialDataController() {
		headerEntries.add(new HeaderEntry(FinancialItemInfo.CODE_ATTRIBUTE));
		headerEntries.add(new HeaderEntry(FinancialItemInfo.NAME_ATTRIBUTE));		
	}
	
	public class FinancialDataResponse extends DataResponseBase {
		protected FinancialItemsInfo financialItemsInfo;
		protected AsyncCallback<FinancialItemsInfo> callback;
		
		protected FinancialDataResponse(FinancialItemsInfo financialItemsInfo, AsyncCallback<FinancialItemsInfo> callback) {
			this.financialItemsInfo = financialItemsInfo;
			this.callback = callback;
		}

		@Override
		protected void onResponseImpl(JSOModel jso) {
			parseFinancialItemsData(jso, this);
		}

		@Override
		protected void onErrorImpl(Throwable caught) {
			callback.onFailure(caught);
		}
		
	}
	
	public void getLocalLookup(FinancialItemsInfo financialItemsInfo, AsyncCallback<FinancialItemsInfo> callback) {
		String action = "/lookup/local";
				
//		FinancialItemsInfo financialItemsInfo = new FinancialItemsInfo();
//		financialItemsInfo.setSortByDefault(FinancialItemInfo.NAME_ATTRIBUTE);
//		financialItemsInfo.setSortDirection(ScrollingGridInfo.SORT_DOWN);
//		financialItemsInfo.setPageSize(-1);
		String query = createQuery(financialItemsInfo, financialSearchCategories);
		
		FinancialDataResponse response = new FinancialDataResponse(financialItemsInfo, callback);
		
		getFinancialData(action, query, response);
	}
	
	public void getInstruments(String[] instruments, AsyncCallback<FinancialItemsInfo> callback) {
		String action = "/lookup/local";

		String query = "where";
		
		for (String instrument: instruments) {
			query += " code='" + instrument + "' or";
		}
		
		query = query.substring(0, query.length() - 3);
		
		FinancialDataResponse response = new FinancialDataResponse(new FinancialItemsInfo(), callback);
		
		getFinancialData(action, query, response);
	}
	
	public void getRemoteLookup(FinancialItemsInfo financialItemsInfo, AsyncCallback<FinancialItemsInfo> callback) {		
		String action = "/lookup/remote";
		
		String query = "query=" + financialItemsInfo.getSearchFor();
//		String query = createQuery(customQuery, financialItemsInfo, companySearchCategories);
		
		FinancialDataResponse response = new FinancialDataResponse(financialItemsInfo, callback);
		
		getFinancialData(action, query, response, true);
	}

	protected void parseFinancialItemsData(JSOModel jsItems, FinancialDataResponse response) {

		JsArray<JSOModel> rows = jsItems.getArray("rows");

		FinancialItemsInfo financialItemsInfo = response.financialItemsInfo;

		ArrayList<FinancialItemInfo> items = financialItemsInfo.getItems();
		if (financialItemsInfo.getPage() == 0) {
			items = new ArrayList<FinancialItemInfo>();
		}
		
		for (int i = 0; i < rows.length(); i++) {
			JSOModel row = rows.get(i);
			
			JsArray<JSOModel> column = row.getArray("c");
			
			FinancialItemInfo item = new FinancialItemInfo();
			int j = 0;
			
			item.setCode(column.get(headerEntries.get(j++).col).get("v"));
			item.setName(column.get(headerEntries.get(j++).col).get("v"));
			
			items.add(item);
		}
		
		financialItemsInfo.setItems(items);
//		companiesInfo.setCurrentPage(1);
//		companiesInfo.setPageCount(1);
		
		response.callback.onSuccess(financialItemsInfo);
	}
	
}
