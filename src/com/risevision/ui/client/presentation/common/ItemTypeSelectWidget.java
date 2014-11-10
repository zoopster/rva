// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.presentation.common;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.risevision.common.client.info.PlaylistItemInfo;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.ui.client.gadget.GadgetSelectWidget;
import com.risevision.ui.client.presentation.placeholder.PlaceholderManageWidget;

public class ItemTypeSelectWidget extends Anchor {
	private static boolean enablePaste = false;
	
	private int row;
	private PopupPanel menuPanel = new PopupPanel(true, false);
	private MenuBar popupMenuBar = new MenuBar(true);
	
	public ItemTypeSelectWidget(int row) {
		this(row, true);
	}
	
	public ItemTypeSelectWidget(int row, boolean enableCopy) {
		super("Add");
		this.row = row;

		MenuItem copyItem = new MenuItem("Copy", true, 
				new Command() {
					@Override
					public void execute() {
						copyItem();
					}
				});
		
		final MenuItem pasteItem = new MenuItem("Paste", true, 
				new Command() {
					@Override
					public void execute() {
						pasteItem();
					}
				});

		final MenuItem contentItem = new MenuItem("Content", true, 
				new Command() {
					@Override
					public void execute() {
						final HashMap<String, Object> data = new HashMap<String, Object>();
						data.put("via", PlaceholderManageWidget.VIA_STORE);
						data.put("storePath", "#/");
						selectItem(PlaylistItemInfo.TYPE_GADGET, data);
					}
				});
		
		final MenuItem byUrlItem = new MenuItem("Content by URL", true, 
				new Command() {
					@Override
					public void execute() {
						final HashMap<String, Object> data = new HashMap<String, Object>();
						data.put("via", GadgetSelectWidget.Content.SELECT_GADGET_BY_URL);
						selectItem(PlaylistItemInfo.TYPE_GADGET, data);
					}
				});

		final MenuItem byCompanyItem = new MenuItem("Your Content", true, 
				new Command() {
					@Override
					public void execute() {
						final HashMap<String, Object> data = new HashMap<String, Object>();
						data.put("via", GadgetSelectWidget.Content.SELECT_GADGET_FROM_COMPANY);
						selectItem(PlaylistItemInfo.TYPE_GADGET, data);
					}
				});

		final MenuItem sharedGadgetsItem = new MenuItem("Content Shared with You", true, 
				new Command() {
					@Override
					public void execute() {
						final HashMap<String, Object> data = new HashMap<String, Object>();
						data.put("via", GadgetSelectWidget.Content.SELECT_GADGET_FROM_SHARED);
						selectItem(PlaylistItemInfo.TYPE_GADGET, data);
					}
				});

		copyItem.setEnabled(enableCopy);
				
		popupMenuBar.addItem(copyItem);
		popupMenuBar.addItem(pasteItem);
		popupMenuBar.addSeparator();
		
		popupMenuBar.addItem(contentItem);
		
//		addMenuOption(RiseUtils.capitalizeFirstLetter(PlaylistItemInfo.TYPE_TEXT), PlaylistItemInfo.TYPE_TEXT);
		addMenuOption(RiseUtils.capitalizeFirstLetter(PlaylistItemInfo.TYPE_PRESENTATION), PlaylistItemInfo.TYPE_PRESENTATION);
		addMenuOption(RiseUtils.capitalizeFirstLetter(PlaylistItemInfo.TYPE_IMAGE), PlaylistItemInfo.TYPE_IMAGE);
		addMenuOption(RiseUtils.capitalizeFirstLetter(PlaylistItemInfo.TYPE_VIDEO), PlaylistItemInfo.TYPE_VIDEO);
		addMenuOption(PlaylistItemInfo.TYPE_HTML, PlaylistItemInfo.TYPE_HTML);
		
		popupMenuBar.addSeparator();
		popupMenuBar.addItem(byUrlItem);
		popupMenuBar.addItem(byCompanyItem);
		popupMenuBar.addItem(sharedGadgetsItem);

    	popupMenuBar.setVisible(true);
    	menuPanel.add(popupMenuBar);
    	
    	addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
		    	int x = event.getClientX() + Window.getScrollLeft() + 1;
		    	int y = event.getClientY() + Window.getScrollTop() + 1;
		    	menuPanel.setPopupPosition(x, y);
		    	
		    	pasteItem.setEnabled(enablePaste);
		    	
		    	menuPanel.show();
			}
		});
	}

	private void addMenuOption(String text, final String value) {
		MenuItem menuItem = new MenuItem(text, true, 
				new Command() {
					@Override
					public void execute() {
						selectItem(value, new HashMap<String, Object>());
					}
				});
		
		popupMenuBar.addItem(menuItem);
	}
	
	private void selectItem(String type, Map<String, Object> data) {
		menuPanel.hide();
		PlaceholderManageWidget.getInstance().addItem(type, row, data);		
	}
	
	private void copyItem() {
		PlaceholderManageWidget.getInstance().copyItem(row);
		
		enablePaste = true;
		menuPanel.hide();
	}
	
	private void pasteItem() {
		PlaceholderManageWidget.getInstance().pasteItem(row);
		
		menuPanel.hide();
	}
}