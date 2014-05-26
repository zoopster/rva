// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Label;
import com.risevision.common.client.utils.RiseUtils;

public class LastModifiedWidget extends Label {
	private static LastModifiedWidget instance;
//	private UserServiceAsync userService = (UserServiceAsync) GWT.create(UserService.class); 
//	private RpcGetUserCallbackHandler callbackHandler = new RpcGetUserCallbackHandler();
	
//	private Date changeDate;
	
	public static LastModifiedWidget getInstance() {
		try {
			if (instance == null)
				instance = new LastModifiedWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}
	
	public LastModifiedWidget(){
		setStyleName("rdn-LastModified");
	}

	public void Initialize(String changedBy, Date changeDate) {
//		this.changeDate = changeDate;
		
		if (changeDate != null) {
			setText("Saved: " + getLastModified(changedBy, changeDate));
		}
		
//		if (!RiseUtils.strIsNullOrEmpty(changedBy)) {
//			userService.getUser(changedBy, callbackHandler);	
//		}
	}
	
	public void Hide() {
		setText("");
	}
	
	public void Clear(){
		setText("");
	}
	
	public static String getLastModified(Date changeDate) {
		return getDateString(changeDate);
	}
	
	public static String getLastModified(String changedBy, Date changeDate) {
		if (changedBy == null) {
			return getLastModified(changeDate);
		}
		else {
			String username = changedBy;
			
			if (changedBy.indexOf("@") != -1) {
				username = changedBy.substring(0, changedBy.indexOf("@"));
			}
			
			return getDateString(changeDate) + " by " + username;
		}
	}

	@SuppressWarnings("deprecation")
	private static String getDateString(Date oldDate) {
		if (oldDate == null) {
			return "";
		}
		
		String difference = "";
		Date newDate = new Date();
		
		if (newDate.getYear() > oldDate.getYear() || newDate.getMonth() > oldDate.getMonth()) {
			difference = DateTimeFormat.getFormat(RiseUtils.SHORT_DATE_FORMAT).format(oldDate);
		}
		else if (newDate.getDate() > oldDate.getDate()) {
			difference = DateTimeFormat.getFormat("MMM d").format(oldDate);
		}
		else {
			difference = RiseUtils.timeToString(oldDate);
		}
		
		return difference;
	}

//	class RpcGetUserCallbackHandler implements AsyncCallback<UserInfo> {
//
//		public void onFailure(Throwable caught) {
//		}
//
//		public void onSuccess(UserInfo user) {
//			if (user != null) {
//				setText("Saved " + RiseUtils.dateToString(changedDate) + " by " + user.getFirstName() + " " + user.getLastName());
//			}
//		}
//	}

}
