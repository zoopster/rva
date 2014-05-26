// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.info;

import java.io.Serializable;
import java.util.ArrayList;

import com.risevision.common.client.info.ScheduleInfo;


@SuppressWarnings("serial")
public class SchedulesInfo extends ScrollingGridInfo implements Serializable {
	private String companyId;
	private ArrayList<ScheduleInfo> schedules;
	
	public String getCompanyId() {
		return companyId;
	}
	
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
	public ArrayList<ScheduleInfo> getSchedules() {
		return schedules;
	}
	
	public void setSchedules(ArrayList<ScheduleInfo> schedules) {
		this.schedules = schedules;
	}
	
	public void clearData(){
		schedules = null;
	}
}
