// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.info;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class SystemMessageInfo implements Serializable {
	public final static String TEXT = "text";
	public final static String START_DATE = "startDate";
	public final static String END_DATE = "endDate";

	private String text;
	private Date startDate;
	private Date endDate;
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public boolean canPlay() {
		Date today = toDateUTC(new Date());
		if (startDate != null && today.before(startDate)) {
			return false;
		}
		
		if (endDate != null && today.after(endDate)) {
			return false;
		}

		return true;
	}
	
	@SuppressWarnings("deprecation")
	private static Date toDateUTC(Date d) {
		return (d == null) ? null : new Date(Date.UTC(d.getYear(), d.getMonth(), d.getDate(),0 ,0 ,0));
	}
	
}
