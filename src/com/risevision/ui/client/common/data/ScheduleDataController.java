package com.risevision.ui.client.common.data;

import java.util.ArrayList;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.risevision.common.client.info.ScheduleInfo;
import com.risevision.common.client.json.JSOModel;
import com.risevision.core.api.attributes.CommonAttribute;
import com.risevision.core.api.attributes.ScheduleAttribute;
import com.risevision.ui.client.common.info.SchedulesInfo;

public class ScheduleDataController extends DataControllerBase {
	private static ScheduleDataController instance;
	private final String[] schedulesSearchCategories = new String[] {
			ScheduleAttribute.NAME,
			ScheduleAttribute.ID,
//			DisplayAttribute.STATUS
			};
	
	public static ScheduleDataController getInstance() {
		try {
			if (instance == null)
				instance = new ScheduleDataController();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (ScheduleDataController) instance;
	}
	
	public ScheduleDataController() {
		headerEntries.add(new HeaderEntry(ScheduleAttribute.ID));
		headerEntries.add(new HeaderEntry(ScheduleAttribute.NAME));
		headerEntries.add(new HeaderEntry(CommonAttribute.CHANGED_BY));
		headerEntries.add(new HeaderEntry(CommonAttribute.CHANGE_DATE));
	}
	
	private class SchedulesDataResponse extends DataResponseBase {
		protected SchedulesInfo schedulesInfo;
		protected AsyncCallback<SchedulesInfo> callback;
		
		protected SchedulesDataResponse(SchedulesInfo schedulesInfo, AsyncCallback<SchedulesInfo> callback) {
			this.schedulesInfo = schedulesInfo;
			this.callback = callback;
		}
		
		protected void onResponseImpl(JSOModel jso) {
			parseSchedulesData(jso, this);
		}
		
		protected void onErrorImpl(Throwable caught) {
			callback.onFailure(caught);
		}
	}
	
	public void getSchedules(SchedulesInfo schedulesInfo, AsyncCallback<SchedulesInfo> callback) {
		String action = "/company/" + schedulesInfo.getCompanyId() + "/schedules";
		String method = "GET"; 
	
		String customQuery = createSearchString(schedulesInfo.getSearchFor());
		String query = createQuery(customQuery, schedulesInfo, schedulesSearchCategories);
		
		SchedulesDataResponse response = new SchedulesDataResponse(schedulesInfo, callback);
		
		getData(action, method, query, response);
	}
	
	private String createSearchString(String searchFor) {
		String query = "";
		
		return query;
	}

	protected void parseSchedulesData(JSOModel jsSchedules, SchedulesDataResponse response) {
//		String displays =  jsDisplays.getAsString();
//    	for (var i=0; i< jsoModel.table.rows.length; i++) {
//		logText("display name: " + jsoModel.table.rows[i].c[1].v);
//	}
	
		JsArray<JSOModel> rows = jsSchedules.getArray("rows");
		
		SchedulesInfo schedulesInfo = response.schedulesInfo;

		ArrayList<ScheduleInfo> schedules = schedulesInfo.getSchedules();
		if (schedulesInfo.getPage() == 0) {
			schedules = new ArrayList<ScheduleInfo>();
		}
		
		for (int i = 0; i < rows.length(); i++) {
			JSOModel row = rows.get(i);
			
			JsArray<JSOModel> column = row.getArray("c");
			
			ScheduleInfo schedule = new ScheduleInfo();
			int j = 0;
			
			schedule.setId(column.get(headerEntries.get(j++).col).get("v"));
			schedule.setName(column.get(headerEntries.get(j++).col).get("v"));
			schedule.setChangedBy(column.get(headerEntries.get(j++).col).get("v"));
			schedule.setChangeDate(column.get(headerEntries.get(j++).col).getDate("v"));
			
			schedules.add(schedule);
		}
		
		schedulesInfo.setSchedules(schedules);
//		displaysInfo.setCurrentPage(1);
//		displaysInfo.setPageCount(1);
		
		response.callback.onSuccess(schedulesInfo);
	}
	
}
