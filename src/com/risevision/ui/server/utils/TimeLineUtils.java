// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.server.utils;

import org.restlet.data.Form;
import org.w3c.dom.Element;

import com.risevision.common.client.info.TimeLineInfo;
import com.risevision.core.api.attributes.ScheduleItemAttribute;
import com.risevision.core.api.utils.DayOfWeek;

public class TimeLineUtils {

	public static TimeLineInfo parseTimeLine(Element node) {
		TimeLineInfo t = new TimeLineInfo();
		try {
			t.setUseSchedule(parseBool(node, ScheduleItemAttribute.TIME_DEFINED, false));

			t.setStartDateString(ServerUtils.getNodeValue(node, ScheduleItemAttribute.START_DATE));
			t.setEndDateString(ServerUtils.getNodeValue(node, ScheduleItemAttribute.END_DATE));
			t.setNoEndDate(t.getEndDateString().isEmpty());

			t.setStartTimeString(ServerUtils.getNodeValue(node, ScheduleItemAttribute.START_TIME));
			t.setEndTimeString(ServerUtils.getNodeValue(node, ScheduleItemAttribute.END_TIME));
			t.setAllDay(t.getStartTimeString().isEmpty());

			t.setRecurrenceType(parseRecurrenceType(node));
			t.setRecurrenceFrequency(parseInt(node, ScheduleItemAttribute.RECURRENCE_FREQUENCY ,1));
			t.setRecurrenceIsAbsolute(parseBool(node, ScheduleItemAttribute.RECURRENCE_ABSOLUTE, true));
        	t.setRecurrenceDayOfWeek(parseInt(node, ScheduleItemAttribute.RECURRENCE_DAY_OF_WEEK, 0));
			t.setRecurrenceDayOfMonth(parseInt(node, ScheduleItemAttribute.RECURRENCE_DAY_OF_MONTH ,1));
			t.setRecurrenceWeekOfMonth(parseInt(node, ScheduleItemAttribute.RECURRENCE_WEEK_OF_MONTH ,0));
			t.setRecurrenceMonthOfYear(parseInt(node, ScheduleItemAttribute.RECURRENCE_MONTH_OF_YEAR ,0));

			if (TimeLineInfo.RecurrenceType.Weekly.equals(t.getRecurrenceType())) {
				String wd = ServerUtils.getNodeValue(node, ScheduleItemAttribute.RECURRENCE_DAYS_OF_WEEK);
				wd = (wd == null) ? "" : wd;
				t.setRecurrenceSunday(wd.contains(DayOfWeek.SUNDAY));
				t.setRecurrenceMonday(wd.contains(DayOfWeek.MONDAY));
				t.setRecurrenceTuesday(wd.contains(DayOfWeek.TUESDAY));
				t.setRecurrenceWednesday(wd.contains(DayOfWeek.WEDNESDAY));
				t.setRecurrenceThursday(wd.contains(DayOfWeek.THURSDAY));
				t.setRecurrenceFriday(wd.contains(DayOfWeek.FRIDAY));
				t.setRecurrenceSaturday(wd.contains(DayOfWeek.SATURDAY));
			}

		} catch (Exception e) {
		}
		return t;
	}


	public static void addToForm(Form form, TimeLineInfo t) {
		if (t == null) {
			form.add(ScheduleItemAttribute.TIME_DEFINED, ServerUtils.BooleanToStr(false));
			return;
		}
		form.add(ScheduleItemAttribute.TIME_DEFINED, ServerUtils.BooleanToStr(t.getUseSchedule()));
		if (t.getUseSchedule()) {
			form.add(ScheduleItemAttribute.START_DATE, t.getStartDateString());
			if (!t.getNoEndDate())
				form.add(ScheduleItemAttribute.END_DATE, t.getEndDateString());
			else
				form.add(ScheduleItemAttribute.END_DATE, null);
			
			if (!t.getAllDay()) {
				form.add(ScheduleItemAttribute.START_TIME, t.getStartTimeString());
				form.add(ScheduleItemAttribute.END_TIME, t.getEndTimeString());
			}
			else {
				form.add(ScheduleItemAttribute.START_TIME, null);
				form.add(ScheduleItemAttribute.END_TIME, null);
			}
			
			form.add(ScheduleItemAttribute.RECURRENCE_TYPE, t.getRecurrenceType().toString());
			form.add(ScheduleItemAttribute.RECURRENCE_FREQUENCY, Integer.toString(t.getRecurrenceFrequency()));
			form.add(ScheduleItemAttribute.RECURRENCE_ABSOLUTE, ServerUtils.BooleanToStr(t.getRecurrenceIsAbsolute()));
			form.add(ScheduleItemAttribute.RECURRENCE_DAY_OF_WEEK, Integer.toString(t.getRecurrenceDayOfWeek()));
			form.add(ScheduleItemAttribute.RECURRENCE_DAY_OF_MONTH, Integer.toString(t.getRecurrenceDayOfMonth()));
			form.add(ScheduleItemAttribute.RECURRENCE_WEEK_OF_MONTH, Integer.toString(t.getRecurrenceWeekOfMonth()));
			form.add(ScheduleItemAttribute.RECURRENCE_MONTH_OF_YEAR, Integer.toString(t.getRecurrenceMonthOfYear()));
			if (TimeLineInfo.RecurrenceType.Weekly.equals(t.getRecurrenceType())) {
				String wd = "";
				wd += t.getRecurrenceSunday() ? "," + DayOfWeek.SUNDAY : "";
				wd += t.getRecurrenceMonday() ? "," + DayOfWeek.MONDAY : "";
				wd += t.getRecurrenceTuesday() ? "," + DayOfWeek.TUESDAY : "";
				wd += t.getRecurrenceWednesday() ? "," + DayOfWeek.WEDNESDAY : "";
				wd += t.getRecurrenceThursday() ? "," + DayOfWeek.THURSDAY : "";
				wd += t.getRecurrenceFriday() ? "," + DayOfWeek.FRIDAY : "";
				wd += t.getRecurrenceSaturday() ? "," + DayOfWeek.SATURDAY : "";
				if (wd.length() > 0)
					wd = wd.substring(1);
				form.add(ScheduleItemAttribute.RECURRENCE_DAYS_OF_WEEK, wd);
			}
				
		}
	}
	
	private static boolean parseBool(Element node, String name, boolean defaultValue) {
		return ServerUtils.StrToBoolean(ServerUtils.getNodeValue(node, name), defaultValue);
	}

	private static int parseInt(Element node, String name, int defaultValue) {
		return ServerUtils.strToInt(ServerUtils.getNodeValue(node, name), defaultValue);
	}

	private static TimeLineInfo.RecurrenceType parseRecurrenceType(Element node) {
		TimeLineInfo.RecurrenceType res = TimeLineInfo.RecurrenceType.Daily;
		String s = ServerUtils.getNodeValue(node, ScheduleItemAttribute.RECURRENCE_TYPE);
		if ("week".equals(s) || TimeLineInfo.RecurrenceType.Weekly.toString().equals(s))
			res = TimeLineInfo.RecurrenceType.Weekly;
		else if ("month".equals(s) || TimeLineInfo.RecurrenceType.Monthly.toString().equals(s))
			res = TimeLineInfo.RecurrenceType.Monthly;
		else if ("year".equals(s) || TimeLineInfo.RecurrenceType.Yearly.toString().equals(s))
			res = TimeLineInfo.RecurrenceType.Yearly;
		return res;
	}

//	private static String RecurrenceTypeToString(TimeLineInfo.RecurrenceType rt) {
//		switch (rt) {
//		case Daily:
//			return RecurrenceType.DAILY;
//		case Weekly:
//			return RecurrenceType.WEEKLY;
//		case Monthly:
//			return RecurrenceType.MONTHLY;
//		case Yearly:
//			return RecurrenceType.YEARLY;
//		default:
//			return RecurrenceType.DAILY;
//		}
//	}
}
