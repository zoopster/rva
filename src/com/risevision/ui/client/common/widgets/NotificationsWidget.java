// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;

public class NotificationsWidget extends Grid {

	private static NotificationsWidget instance;
	private int row = -1;	
	
    private CheckBox receiveSubsciptionEmailCheckBox = new CheckBox("Receive Subscription Emails");
    private CheckBox receiveReviewEmailCheckBox= new CheckBox("Receive Review Emails");
    private CheckBox receiveMonitorEmailCheckBox= new CheckBox("Receive Monitoring Emails");    
    
	public NotificationsWidget() {
		resize(3, 1);
		styleGrid();
		loadData();
	}

	private void styleGrid() {
		setCellSpacing(0);
		setCellPadding(0);
		setStyleName("rnd-Table");
	}

	private void loadData() {
		clearNotifications();
		addRow(receiveSubsciptionEmailCheckBox);
		addRow(receiveReviewEmailCheckBox);
		addRow(receiveMonitorEmailCheckBox);
	}

	private void addRow(Widget widget) {
		row++;
		setWidget(row, 0, widget);
		widget.setStyleName("rdn-CheckBox");
	}
	
	public void clearNotifications()
	{
		receiveSubsciptionEmailCheckBox.setValue(false);
		receiveReviewEmailCheckBox.setValue(false);
		receiveMonitorEmailCheckBox.setValue(false);
	}
	
	public static NotificationsWidget getInstance() {
		try {
			if (instance == null)
				instance = new NotificationsWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}

	public void setNotifications(ArrayList<String> notifications) {
		clearNotifications();
		if (notifications == null)
			return;
        for (String notification : notifications) {
			if (notification.equals("Receive Subscription Email")) receiveSubsciptionEmailCheckBox.setValue(true);
			else if (notification.equals("Receive Review Email")) receiveReviewEmailCheckBox.setValue(true);
			else if (notification.equals("Receive Monitor Email")) receiveMonitorEmailCheckBox.setValue(true);
        }	
	}

	public ArrayList<String> getNotifications() {
		ArrayList<String> notifications = new ArrayList<String>();
		if (receiveSubsciptionEmailCheckBox.getValue()) notifications.add("Receive Subscription Email");
		if (receiveReviewEmailCheckBox.getValue()) notifications.add("Receive Review Email");
		if (receiveMonitorEmailCheckBox.getValue()) notifications.add("Receive Monitor Email");

		return notifications;
	}
}