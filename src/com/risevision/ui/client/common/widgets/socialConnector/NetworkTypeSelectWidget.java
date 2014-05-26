// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.socialConnector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.risevision.ui.client.common.info.SocialConnectorAccessInfo.NetworkType;

public class NetworkTypeSelectWidget extends Anchor {
	private PopupPanel menuPanel = new PopupPanel(true, false);
	
	public NetworkTypeSelectWidget() {
		super("Add");
		MenuBar popupMenuBar = new MenuBar(true);

		for (final NetworkType networkType: NetworkType.values()) {
			MenuItem item = new MenuItem(networkType.getDefaultName(), true, 
					new Command() {
				
						@Override
						public void execute() {
							Window.open(networkType.getRequestUrl(), "_blank", "");
							menuPanel.hide();
						}
					});
			
			popupMenuBar.addItem(item);
		}
		
//		MenuItem foursquareItem = new MenuItem(NetworkType.foursquare.getDefaultName(), true, 
//				new Command() {
//			
//					@Override
//					public void execute() {
//						Window.open(NetworkType.foursquare.getRequestUrl(), "_blank", "");
//						menuPanel.hide();
//					}
//				});
//		MenuItem twitterItem = new MenuItem(RiseUtils.capitalizeFirstLetter(NetworkType.twitter.getDefaultName()), true, 
//				new Command() {
//			
//					@Override
//					public void execute() {
//						Window.open(NetworkType.twitter.getRequestUrl(), "_blank", "");
//						menuPanel.hide();
//					}
//				});
//		
//		popupMenuBar.addItem(foursquareItem);
//		popupMenuBar.addItem(twitterItem);
		
    	popupMenuBar.setVisible(true);
    	menuPanel.add(popupMenuBar);
    	
    	addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
		    	int x = event.getClientX() + Window.getScrollLeft() + 1;
		    	int y = event.getClientY() + Window.getScrollTop() + 1;
		    	menuPanel.setPopupPosition(x, y);
		    	menuPanel.show();
			}
		});
	}
}

//import com.google.gwt.event.dom.client.ChangeEvent;
//import com.google.gwt.event.dom.client.ChangeHandler;
//import com.risevision.common.client.info.PlaylistItemInfo;
//import com.risevision.common.client.utils.RiseUtils;
//import com.risevision.ui.client.common.widgets.RiseListBox;

//public class ItemTypeSelectWidget extends RiseListBox implements ChangeHandler {
//	private int row;
//	
//	public ItemTypeSelectWidget(int row) {
//		this.row = row;
//		
//		addItem("Add", "");
//		addItem(RiseUtils.capitalizeFirstLetter(PlaylistItemInfo.TYPE_GADGET), PlaylistItemInfo.TYPE_GADGET);
//		addItem(RiseUtils.capitalizeFirstLetter(PlaylistItemInfo.TYPE_TEXT), PlaylistItemInfo.TYPE_TEXT);
//		
//		setSelectedIndex(0);
//		
//		addChangeHandler(this);
//	}
//	
//	public int getRow() {
//		return row;
//	}
//	
//	@Override
//	public void onChange(ChangeEvent event) {
//		if (PlaylistItemInfo.TYPE_GADGET.equals(getSelectedValue())) {
//			PlaceholderGadgetManageWidget.getInstance().show(null, row);
//		}
//		else if (PlaylistItemInfo.TYPE_TEXT.equals(getSelectedValue())) {
//			PlaceholderTextManageWidget.getInstance().show(null, row);
//		}
//		
//		setSelectedIndex(0);
//	} 
//	
//
//}
