// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.presentation.common;

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
		
		popupMenuBar.addItem(copyItem);
		copyItem.setEnabled(enableCopy);
		
		popupMenuBar.addItem(pasteItem);
		
		popupMenuBar.addSeparator();
		
		addMenuOption(RiseUtils.capitalizeFirstLetter(PlaylistItemInfo.TYPE_GADGET), PlaylistItemInfo.TYPE_GADGET);
		addMenuOption(RiseUtils.capitalizeFirstLetter(PlaylistItemInfo.TYPE_TEXT), PlaylistItemInfo.TYPE_TEXT);
		addMenuOption(RiseUtils.capitalizeFirstLetter(PlaylistItemInfo.TYPE_PRESENTATION), PlaylistItemInfo.TYPE_PRESENTATION);
		addMenuOption(RiseUtils.capitalizeFirstLetter(PlaylistItemInfo.TYPE_IMAGE), PlaylistItemInfo.TYPE_IMAGE);
		addMenuOption(RiseUtils.capitalizeFirstLetter(PlaylistItemInfo.TYPE_VIDEO), PlaylistItemInfo.TYPE_VIDEO);
		addMenuOption(PlaylistItemInfo.TYPE_HTML, PlaylistItemInfo.TYPE_HTML);
		
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
						selectItem(value);
					}
				});
		
		popupMenuBar.addItem(menuItem);
	}
	
	private void selectItem(String type) {
		PlaceholderManageWidget.getInstance().addItem(type, row);
		
		menuPanel.hide();
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