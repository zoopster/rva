package com.risevision.ui.client.common.data;

import java.util.ArrayList;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.risevision.common.client.json.JSOModel;
import com.risevision.core.api.attributes.CommonAttribute;
import com.risevision.core.api.attributes.DemoAttribute;
import com.risevision.ui.client.common.info.DemoContentInfo;

public class DemoContentDataController extends DataControllerBase {
	protected static DemoContentDataController instance;
	
	public static DemoContentDataController getInstance() {
		try {
			if (instance == null)
				instance = new DemoContentDataController();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}
	
	public DemoContentDataController() {
//		headerEntries.add(new HeaderEntry(DemoAttribute.ID));
		headerEntries.add(new HeaderEntry(DemoAttribute.WIDTH));		
		headerEntries.add(new HeaderEntry(DemoAttribute.HEIGHT));
		headerEntries.add(new HeaderEntry(DemoAttribute.OBJECT_REFERENCE));
		headerEntries.add(new HeaderEntry(CommonAttribute.CHANGED_BY));
		headerEntries.add(new HeaderEntry(CommonAttribute.CHANGE_DATE));
	}
	
	private class DemoContentDataResponse extends DataResponseBase {
		protected AsyncCallback<ArrayList<DemoContentInfo>> callback;	
		
		protected DemoContentDataResponse(AsyncCallback<ArrayList<DemoContentInfo>> callback) {
			this.callback = callback;
		}
		
		protected void onResponseImpl(JSOModel jso) {
			parseDemoContentData(jso, this);
		}
		
		protected void onErrorImpl(Throwable caught) {
			callback.onFailure(caught);
		}
	}
	
	public void getDemoContentList(String companyId, AsyncCallback<ArrayList<DemoContentInfo>> callback) {
		String action = "/company/" + companyId + "/demos";
		String method = "GET"; 
		
		String query = "";
		
		DemoContentDataResponse response = new DemoContentDataResponse(callback);
		
		getData(action, method, query, response);
	}

	protected void parseDemoContentData(JSOModel jsCompanies, DemoContentDataResponse response) {

		JsArray<JSOModel> rows = jsCompanies.getArray("rows");
		
		ArrayList<DemoContentInfo> demoContentList = new ArrayList<DemoContentInfo>();
		
		for (int i = 0; i < rows.length(); i++) {
			JSOModel row = rows.get(i);
			
			JsArray<JSOModel> column = row.getArray("c");
			
			DemoContentInfo demoContent = new DemoContentInfo();
			int j = 0;
			
			demoContent.setWidth(column.get(headerEntries.get(j++).col).getInt("v"));
			demoContent.setHeight(column.get(headerEntries.get(j++).col).getInt("v"));
			demoContent.setObjectRef(column.get(headerEntries.get(j++).col).get("v"));
			demoContent.setChangedBy(column.get(headerEntries.get(j++).col).get("v"));
			demoContent.setChangeDate(column.get(headerEntries.get(j++).col).getDate("v"));
			
			demoContentList.add(demoContent);
		}
		
		response.callback.onSuccess(demoContentList);
	}
	
}
