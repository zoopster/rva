// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.data;

import java.util.ArrayList;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.risevision.common.client.info.PresentationInfo;
import com.risevision.common.client.json.JSOModel;
import com.risevision.core.api.attributes.CommonAttribute;
import com.risevision.core.api.attributes.PresentationAttribute;
import com.risevision.core.api.types.PresentationRevisionStatus;
import com.risevision.ui.client.common.info.PresentationsInfo;

public class PresentationDataController extends DataControllerBase {
	private static PresentationDataController instance;
	private final String[] presentationsSearchCategories = new String[] {
			PresentationAttribute.NAME
			};
	
	public static PresentationDataController getInstance() {
		try {
			if (instance == null)
				instance = new PresentationDataController();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (PresentationDataController) instance;
	}
	
	public PresentationDataController() {
		headerEntries.add(new HeaderEntry(PresentationAttribute.ID));
		headerEntries.add(new HeaderEntry("companyId"));
		headerEntries.add(new HeaderEntry(PresentationAttribute.NAME));
//		headerEntries.add(new HeaderEntry(PresentationAttribute.LAYOUT));
//		headerEntries.add(new HeaderEntry(PresentationAttribute.PUBLISH));
		headerEntries.add(new HeaderEntry(PresentationAttribute.TEMPLATE));
		headerEntries.add(new HeaderEntry(PresentationAttribute.REVISION_STATUS));
		headerEntries.add(new HeaderEntry(PresentationAttribute.DISTRIBUTION));
		headerEntries.add(new HeaderEntry(CommonAttribute.CHANGED_BY));
		headerEntries.add(new HeaderEntry(CommonAttribute.CHANGE_DATE));
	}
	
	private class PresentationsDataResponse extends DataResponseBase {
		protected PresentationsInfo presentationsInfo;
		protected AsyncCallback<PresentationsInfo> callback;

		protected PresentationsDataResponse(PresentationsInfo presentationsInfo, AsyncCallback<PresentationsInfo> callback) {
			this.presentationsInfo = presentationsInfo;
			this.callback = callback;
		}
		
		protected void onResponseImpl(JSOModel jso) {
			parsePresentationsData(jso, this);
		}
		
		protected void onErrorImpl(Throwable caught) {
			callback.onFailure(caught);
		}
	}
	
	public void getPresentations(PresentationsInfo presentationsInfo, boolean templates, AsyncCallback<PresentationsInfo> callback) {
		String action;
		String customQuery = "";
		if (templates) {
			action = "/company/" + presentationsInfo.getCompanyId() + "/templates";	
		}
		else {
			customQuery = createSearchString(presentationsInfo.getSearchFor());

			action = "/company/" + presentationsInfo.getCompanyId() + "/presentations";
		}
		 
		String method = "GET"; 
	
		String query = createQuery(customQuery, presentationsInfo, presentationsSearchCategories);
		String selectString = " SELECT ";
		for (HeaderEntry entry: headerEntries) {
			selectString += entry.id + ",";
		}
		selectString = selectString.substring(0, selectString.length() - 1);
		selectString += " ";
		query = selectString + query;
		
		PresentationsDataResponse response = new PresentationsDataResponse(presentationsInfo, callback);
		
		getData(action, method, query, response);
	}
	
	private String createSearchString(String searchFor) {
		String query = "";
		
		if (searchFor.equalsIgnoreCase("published")) {
			query += " or " + PresentationAttribute.REVISION_STATUS + " = " + PresentationRevisionStatus.PUBLISHED;
		}
		else if (searchFor.equalsIgnoreCase("revised")) {
			query += " or " + PresentationAttribute.REVISION_STATUS + " = " + PresentationRevisionStatus.REVISED;
		}
		
		if (searchFor.equalsIgnoreCase("template")) {
			query += " or " + PresentationAttribute.TEMPLATE + " = " + Boolean.toString(true);
		}
		
		return query;
	}

	protected void parsePresentationsData(JSOModel jsPresentations, PresentationsDataResponse response) {

		JsArray<JSOModel> rows = jsPresentations.getArray("rows");
		
		PresentationsInfo presentationsInfo = response.presentationsInfo;

		ArrayList<PresentationInfo> presentations = presentationsInfo.getPresentations();
		if (presentationsInfo.getPage() == 0) {
			presentations = new ArrayList<PresentationInfo>();
		}
		
		for (int i = 0; i < rows.length(); i++) {
			JSOModel row = rows.get(i);
			
			JsArray<JSOModel> column = row.getArray("c");
			
			PresentationInfo presentation = new PresentationInfo();
			int j = 0;
			
			presentation.setId(column.get(headerEntries.get(j++).col).get("v"));
			presentation.setCompanyId(column.get(headerEntries.get(j++).col).get("v"));
			presentation.setName(column.get(headerEntries.get(j++).col).get("v"));
//			presentation.setLayout(column.get(headerEntries.get(j++).col).get("v"));
//			presentation.setPublishType(column.get(headerEntries.get(j++).col).getInt("v"));
			presentation.setTemplate(column.get(headerEntries.get(j++).col).getBoolean("v"));
			presentation.setRevisionStatus(column.get(headerEntries.get(j++).col).getInt("v"));
			presentation.setDistributionString(column.get(headerEntries.get(j++).col).get("v"));
			presentation.setChangedBy(column.get(headerEntries.get(j++).col).get("v"));
			presentation.setChangeDate(column.get(headerEntries.get(j++).col).getDate("v"));
			
			presentations.add(presentation);
		}
		
		presentationsInfo.setPresentations(presentations);
//		displaysInfo.setCurrentPage(1);
//		displaysInfo.setPageCount(1);
		
		response.callback.onSuccess(presentationsInfo);
	}
	
}
