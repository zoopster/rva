// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.info;

import java.util.ArrayList;

public class DistributionCheckInfo {
	private ArrayList<DistributionCheckScheduleInfo> scheduleAll;
	private ArrayList<DistributionCheckScheduleInfo> scheduleAny;
	private ArrayList<DistributionCheckDisplayInfo> display;
	
	public void setScheduleAll(ArrayList<DistributionCheckScheduleInfo> scheduleAll) {
		this.scheduleAll = scheduleAll;
	}

	public ArrayList<DistributionCheckScheduleInfo> getScheduleAll() {
		return scheduleAll;
	}

	public void setScheduleAny(ArrayList<DistributionCheckScheduleInfo> scheduleAny) {
		this.scheduleAny = scheduleAny;
	}

	public ArrayList<DistributionCheckScheduleInfo> getScheduleAny() {
		return scheduleAny;
	}

	public void setDisplay(ArrayList<DistributionCheckDisplayInfo> display) {
		this.display = display;
	}

	public ArrayList<DistributionCheckDisplayInfo> getDisplay() {
		return display;
	}

	public class DistributionCheckScheduleInfo {
		private String id;
		private String name;
		
		public void setId(String id) {
			this.id = id;
		}
		
		public String getId() {
			return id;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
	}
	
	public class DistributionCheckDisplayInfo {
		private String id;
		private String name;
		private ArrayList<DistributionCheckScheduleInfo> schedule;
		
		public void setId(String id) {
			this.id = id;
		}
		
		public String getId() {
			return id;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}

		public void setSchedule(ArrayList<DistributionCheckScheduleInfo> schedule) {
			this.schedule = schedule;
		}

		public ArrayList<DistributionCheckScheduleInfo> getSchedule() {
			return schedule;
		}
	}
	
	public String parseDistributionCheck() {
		String result = "";
		if (this.getScheduleAll() != null) {
			result = "Displays cannot be selected because All Displays is selected in schedule '" + this.getScheduleAll().get(0).getName() + "'";
		}
		if (this.getScheduleAny() != null) {
			for (DistributionCheckScheduleInfo schedule: this.getScheduleAny()) {
				if (result.isEmpty()) {
					result += "All Displays cannot be selected because ";
				}
				else {
					result += " and ";
				}
				result += "Schedule '" + schedule.getName() + "' has some displays selected";
			}
		}
		if (this.getDisplay() != null) {
			for (DistributionCheckDisplayInfo display: this.getDisplay()) {
				if (display.getSchedule() != null) {
					if (result.isEmpty()) {
						result += "";
					}
					else {
						result += " and ";
					}
					result += "Display '" + display.getName() + "' is already selected in schedule '" + display.getSchedule().get(0).getName() + "'";
				}
			}
		}
		
		if (!result.isEmpty())
			result = "Distribution Error: " + result;
		
		return result;
	}
}
