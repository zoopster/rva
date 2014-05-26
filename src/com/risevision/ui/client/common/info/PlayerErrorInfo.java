// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.info;

import java.io.Serializable;
import java.util.Date;

import com.risevision.common.client.utils.RiseUtils;

@SuppressWarnings("serial")
public class PlayerErrorInfo implements Serializable {
	
	public final static String STATUS_CODE = "statusCode";
	public final static String TIMESTAMP = "timestamp";
	
	private ErrorType errorType;
	private Date timestamp;
	
//	private final static String HELP_LINK = "http://www.risevision.com/displayerrors/";
	
	private enum ErrorType {
		STATUS_0(0,
				"OK (no errors)",
				""
				),
		STATUS_BLOCK(1, 
				"This display has been blocked and the block can last up to 3 hours.",
				"http://www.risevision.com/display-block-help/"
				),
		STATUS_1001(1001, 
				"Rise Vision Player does not have sufficient privileges to complete it's upgrade.",
//				"http://www.risevision.com/error-1001/"
				"http://www.risevision.com/displayerrors/"
				),
		STATUS_1002(1002, 
				"The upgrade for Rise Vision Player failed to complete.",
//				"http://www.risevision.com/error-1002/"
				"http://www.risevision.com/displayerrors/"
				),
		STATUS_1003(1003, 
				"The upgrade for Rise Vision Player failed to complete.",
//				"http://www.risevision.com/error-1003/"
				"http://www.risevision.com/displayerrors/"
				),
		STATUS_1004(1004, 
				"Rise Vision Viewer is no longer responding.",
//				"http://www.risevision.com/error-1004/"
				"http://www.risevision.com/displayerrors/"
				),
		STATUS_1005(1005, 
				"The upgrade for Rise Vision Player failed to complete because Java 7 is not set to default.",
//				"http://www.risevision.com/error-1005/"	
				"http://www.risevision.com/displayerrors/"
				);
		
		private int statusCode;
		private String message;
		private String helpLink;
		private ErrorType(int statusCode, String message, String helpLink) {
			this.statusCode = statusCode;
			this.message = message;
			this.helpLink = helpLink;
		}
		
		@Override
		public String toString() {
			return Integer.toString(statusCode);
		}
		
		public int getStatusCode() {
			return statusCode;
		}
		
		public String getMessage() {
			return message;
		}
		
		public String getHelpLink() {
			return helpLink;
		}
		
	}

	public void setStatusCode(String statusCode) {
		if (errorType == null) {
			errorType = ErrorType.valueOf("STATUS_" + statusCode);
		}
	}
	
	public int getStatusCode() {
		return errorType.getStatusCode();
	}

	public String getErrorMessage() {
		return errorType.getMessage();
	}
	
	public String getHelpLink() {
		return errorType.getHelpLink();
//		return HELP_LINK;
	}

	public String getTimestamp() {
		return RiseUtils.dateToString(timestamp);
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	
}
