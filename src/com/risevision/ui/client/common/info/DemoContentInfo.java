// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.info;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class DemoContentInfo  implements Serializable {
	private int width = 1920;
	private int height = 1080;
	private String objectRef;
	private String objectName;
	
	private String changedBy;
	private Date changeDate;
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public String getObjectRef() {
		return objectRef;
	}
	
	public void setObjectRef(String objectRef) {
		this.objectRef = objectRef;
	}
	
	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getChangedBy() {
		return changedBy;
	}
	
	public void setChangedBy(String changedBy) {
		this.changedBy = changedBy;
	}
	
	public Date getChangeDate() {
		return changeDate;
	}
	
	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}
	
}
