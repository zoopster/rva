// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.risevision.core.api.types.UserRole;
import com.risevision.ui.client.common.info.RoleInfo;

public class RolesWidget extends Grid{

	private static RolesWidget instance;
	private int row = -1;
	private ArrayList<RoleInfo> roles = new ArrayList<RoleInfo>();
	private ArrayList<CheckBox> widgets = new ArrayList<CheckBox>();
    
	public RolesWidget() {
		loadRoleNames();
		resize(roles.size(), 1);
		styleGrid();
		bindControls();
	}

	private void loadRoleNames() {
//		roles.add(new RoleInfo(UserRole.CONTENT_ADMINISTRATOR, "Content"));
		
		roles.add(new RoleInfo(UserRole.CONTENT_EDITOR, "Editor", 
				"Can create and edit Presentations, but cannot mark Presentations as Published"));
		roles.add(new RoleInfo(UserRole.CONTENT_PUBLISHER, "Publisher",
				"Can create and edit Presentations, Gadgets, and Schedules and publish Presentations"));
		
		roles.add(new RoleInfo(UserRole.DISPLAY_ADMINISTRATOR, "Display", 
				"Can add and edit Displays"));
		roles.add(new RoleInfo(UserRole.USER_ADMINISTRATOR, "Administrator",
				"Can manage Users, Company, Network, and Sub Companies"));
		
		roles.add(new RoleInfo(UserRole.PURCHASER, "Purchaser",
				"Can Purchase products from the Rise Vision Store"));
		
		roles.add(new RoleInfo(UserRole.BILLING_ADMINISTRATOR, "Billing Administrator",
				"Can Manage products in the Rise Vision Store"));
		
		roles.add(new RoleInfo(UserRole.SYSTEM_ADMINISTRATOR, "System",
				"Can add and move Companies and manage Network Settings"));
//		roles.add(new RoleInfo("ca", "Content (can create content and approve, and assign editors and approvers)"));
//		roles.add(new RoleInfo("da", "Display (can manage Displays)"));
//		roles.add(new RoleInfo("ua", "Administrator (can manage Company and User settings)"));
	}

	private void styleGrid() {
		setCellSpacing(0);
		setCellPadding(0);
		setStyleName("rnd-Table");
		//getColumnFormatter().setStyleName(0, "rdn-ColumnGeneral");
	}

	private void bindControls() {
		clearRoles();
		for (RoleInfo r:roles) {
			CheckBox itemCheckBox = new CheckBox();
			itemCheckBox.setStyleName("rdn-CheckBox");
		
			TooltipLabelWidget tooltipWidget = new TooltipLabelWidget(r.getDescription(), r.getInfo());

			tooltipWidget.setStyleName("rva-ShortText");
			tooltipWidget.getElement().getStyle().setProperty("whiteSpace", "nowrap");
			
			HorizontalPanel checkBoxPanel = new HorizontalPanel();
			checkBoxPanel.add(itemCheckBox);
			checkBoxPanel.add(new SpacerWidget());
			checkBoxPanel.add(tooltipWidget);
			
			widgets.add(itemCheckBox);
			
			row++;
			setWidget(row, 0, checkBoxPanel);
		}
//		for (CheckBox cb:widgets)
//			addRow(cb);
	}

//	private void addRow(Widget widget) {
//		row++;
//		setWidget(row, 0, widget);
//		widget.setStyleName("rdn-CheckBox");
//	}
	
	public void clearRoles()
	{
		for (CheckBox cb:widgets)
			cb.setValue(false);
	}
	
	public void setSelected() {
		for (CheckBox cb:widgets)
			cb.setValue(true);
	}
	
	public static RolesWidget getInstance() {
		try {
			if (instance == null)
				instance = new RolesWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}

	public void setRoles(List<String> roleNames) {
		clearRoles();
		if (roleNames == null)
			return;
        for (String roleName : roleNames) {
        	for (int i=0; i < roles.size(); i++) {
        		if (roleName.equals(roles.get(i).getName())) {
        			widgets.get(i).setValue(true);
        			break;
        		}
        	}
        }	
	}

	public ArrayList<String> getRoles() {
		ArrayList<String> roleNames = new ArrayList<String>();
    	for (int i=0; i < widgets.size(); i++) {
    		if (widgets.get(i).getValue())
				roleNames.add(roles.get(i).getName());
    	}
		return roleNames;
	}

	public void setEnabled(boolean enabled) {
		for (CheckBox cb:widgets)
			cb.setEnabled(enabled);
	}

	public void enableRole(String roleName, boolean enabled) {
    	for (int i=0; i < roles.size(); i++) {
    		if (roleName.equals(roles.get(i).getName())) {
    			widgets.get(i).setEnabled(enabled);
    			break;
    		}
    	}
	}

	public void showRole(String roleName, boolean visible) {
    	for (int i=0; i < roles.size(); i++) {
    		if (roleName.equals(roles.get(i).getName())) {
//    			widgets.get(i).setVisible(visible);
    			getRowFormatter().setVisible(i, visible);
    			break;
    		}
    	}
	}

}