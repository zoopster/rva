// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.risevision.ui.client.common.ContentId;
import com.risevision.ui.client.common.controller.SelectedCompanyController;
import com.risevision.ui.client.common.controller.UserAccountController;
import com.risevision.ui.client.common.widgets.mediaLibrary.StorageFrameWidget;

public class MenuWidget extends FlowPanel {
	private static MenuWidget instance;	
	private Map<Anchor, String> linkMap = new HashMap<Anchor, String>();
	private Anchor activeLink;
	private Anchor menuItemNetwork;

	public static MenuWidget getInstance() {
		try {

			if (instance == null)
				instance = new MenuWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}	

	public MenuWidget() {
		buildMenu();
	}
	
	private void buildMenu() {
//		boolean canViewContent = UserAccountController.getInstance().userHasRoleContentEditor();
		
		boolean canEditContent = UserAccountController.getInstance().userHasRoleContentEditor();
		boolean canPublishContent = UserAccountController.getInstance().userHasRoleContentPublisher();

		boolean canViewDisplay = UserAccountController.getInstance().userHasRoleDisplayAdministrator();
		boolean canViewUser = UserAccountController.getInstance().userHasRoleUserAdministrator();
		
		clear();
		linkMap.clear();

		addAction("Start", ContentId.HOME);
		addDivider();
//		if (canViewContent) {
		if (canEditContent || canPublishContent) {
			addAction("Presentations", ContentId.PRESENTATIONS);
//			addAction("Playlists", ContentId.PLAYLISTS);
		}
		if (canPublishContent) {
			addAction("Gadgets", ContentId.GADGETS);
		}
		if (canEditContent || canPublishContent) {
			addStorageLink("Storage");
		}
		if (canViewDisplay)
			addAction("Displays", ContentId.DISPLAYS);
//		if (canViewContent)
		if (canPublishContent) {
			addAction("Schedules", ContentId.SCHEDULES);
		}
		addDivider();
		if (canViewUser) {
			addAction("Settings", ContentId.COMPANY_MANAGE);
			addAction("Users", ContentId.USERS);
		}
		menuItemNetwork = addAction("Network", ContentId.MANAGE_PORTAL);
	}
	
	private Anchor addAction(String text, String contentId) {
		Anchor newLink = new Anchor(text);
		newLink.setTabIndex(-1);
		newLink.setHref("#" + contentId + "/company=" + SelectedCompanyController.getInstance().getSelectedCompanyId());
		add(newLink);

		add(new SpacerWidget());
		linkMap.put(newLink, contentId);
		
		return newLink;
	}
	
	private Anchor addStorageLink(String text) {
		Anchor newLink = new Anchor(text);
		newLink.setTabIndex(-1);
		newLink.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				StorageFrameWidget.getInstance().show(null);
			}
		});
//		newLink.setHref(ConfigurationController.getInstance().getConfiguration().getMediaLibraryURL() + "#/files/" + SelectedCompanyController.getInstance().getSelectedCompanyId());
//		newLink.setTarget("_blank");
		add(newLink);

		add(new SpacerWidget());
		
		return newLink;
	}
	
	private void addDivider() {
		add(new InlineLabel("|"));
		add(new SpacerWidget());
	}
	
	public void setEnabled(boolean enabled) {
		for (Anchor link : linkMap.keySet()){
			link.setEnabled(enabled);
		}
	}
	
	public void setEnabled(boolean enabled, String text){
		for (Anchor link : linkMap.keySet()){
			if (linkMap.get(link).equals(text))
				link.setEnabled(enabled);
		}
	}
	
	public void setVisible(boolean visible, String text){
		for (Anchor link : linkMap.keySet()){
			if (linkMap.get(link).equals(text))
				link.setVisible(visible);
		}
	}
	
	public void setActiveLink(String text) {
		Anchor oldActiveLink = activeLink;
		for (Anchor link : linkMap.keySet()){
			if (linkMap.get(link).equals(text)) {
				if (activeLink != link) {
					link.getElement().getStyle().setFontWeight(FontWeight.BOLD);
					
					activeLink = link;
					break;
				}
				else
					return;
			}
		}

		if (oldActiveLink != null) {
			oldActiveLink.getElement().getStyle().setFontWeight(FontWeight.NORMAL);
		}
	}
	
	// The shared ClickHandler code.
//	public void onClick(ClickEvent event) {
//		Object sender = event.getSource();
//		if (sender instanceof Anchor) {
//			Anchor b = (Anchor) sender;
//			String contentId = linkMap.get(b);
//			if (contentId != null) {
//				UiEntryPoint.loadContentStatic(contentId);
//			}
//		}
//	}
	
	public void refreshMenu() {
		//"Network" menu item is available if selected company is PNO.
		boolean canViewNetwork = SelectedCompanyController.getInstance().getSelectedCompany().isPno();
		boolean canViewUser = UserAccountController.getInstance().userHasRoleUserAdministrator();
		
		buildMenu();
		menuItemNetwork.setVisible(canViewNetwork && canViewUser);
	}
}
