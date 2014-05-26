// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.info;

import java.io.Serializable;

@SuppressWarnings("serial")
public class EmailInfo implements Serializable {

	private String subjectString;
	private String fromString;
	private String toAddressString;
	private String messageString;
	
	public String getSubjectString() {
		return subjectString;
	}
	public void setSubjectString(String subjectString) {
		this.subjectString = subjectString;
	}
	
	public String getFromString() {
		return fromString;
	}
	public void setFromString(String fromString) {
		this.fromString = fromString;
	}
	
	public String getToAddressString() {
		return toAddressString;
	}
	public void setToAddressString(String toAddressString) {
		this.toAddressString = toAddressString;
	}
	
	public String getMessageString() {
		return messageString;
	}
	public void setMessageString(String messageString) {
		this.messageString = messageString;
	}
}
