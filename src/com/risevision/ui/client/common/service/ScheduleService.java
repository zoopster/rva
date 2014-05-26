package com.risevision.ui.client.common.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.risevision.common.client.info.ScheduleInfo;
import com.risevision.ui.client.common.exception.ServiceFailedException;
import com.risevision.ui.client.common.info.RpcResultInfo;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("schedule")
public interface ScheduleService extends RemoteService {
	//Schedule API
//	SchedulesInfo getSchedules(SchedulesInfo schedulesInfo) throws ServiceFailedException;
	ScheduleInfo getSchedule(String companyId, String scheduleId) throws ServiceFailedException;
	RpcResultInfo putSchedule(String companyId, ScheduleInfo schedule) throws ServiceFailedException;
	RpcResultInfo deleteSchedule(String companyId, String scheduleId) throws ServiceFailedException;
	//Schedule Items API
//	RpcResultInfo deleteScheduleItems(String companyId, String scheduleId) throws ServiceFailedException; //return error if any
	//Schedule Item API
//	RpcResultInfo putScheduleItem(String companyId, String scheduleId, PlaylistItemInfo scheduleItem) throws ServiceFailedException;
	RpcResultInfo checkDistribution(String companyId, ScheduleInfo schedule) throws ServiceFailedException;
}
