// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.risevision.common.client.info.ScheduleInfo;
import com.risevision.ui.client.common.info.RpcResultInfo;

public interface ScheduleServiceAsync {
	//Schedule API
//	void getSchedules(SchedulesInfo schedulesInfo, AsyncCallback<SchedulesInfo> callback);
	void getSchedule(String companyId, String scheduleId, AsyncCallback<ScheduleInfo> callback);
	void putSchedule(String companyId, ScheduleInfo schedule, AsyncCallback<RpcResultInfo> callback);
	void deleteSchedule(String companyId, String scheduleId, AsyncCallback<RpcResultInfo> callback);
	//Schedule Items API
//	void deleteScheduleItems(String companyId, String scheduleId, AsyncCallback<RpcResultInfo> callback); //return error if any
	//Schedule Item API
//	void putScheduleItem(String companyId, String scheduleId, PlaylistItemInfo scheduleItem, AsyncCallback<RpcResultInfo> callback);
	void checkDistribution(String companyId, ScheduleInfo schedule, AsyncCallback<RpcResultInfo> callback);
}
