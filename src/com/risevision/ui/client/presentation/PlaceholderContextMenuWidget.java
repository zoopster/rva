// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.presentation;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;

public class PlaceholderContextMenuWidget extends PopupPanel {	
	public PlaceholderContextMenuWidget(Command bringToFrontCommand, Command bringForwardCommand, Command sendBackwardCommand,
											Command sendToBackCommand, Command propertiesCommand, Command deleteCommand) {
		super(true, false);
		MenuBar popupMenuBar = new MenuBar(true);
		
		MenuItem propertiesItem = new MenuItem("Properties", true, propertiesCommand);
		MenuItem deleteItem = new MenuItem("Delete", true, deleteCommand);
		MenuItem bringToFrontItem = new MenuItem("Bring to Front", true, bringToFrontCommand);
		MenuItem bringForwardItem = new MenuItem("Bring Forward", true, bringForwardCommand);
		MenuItem sendBackwardItem = new MenuItem("Send Backward", true, sendBackwardCommand);
		MenuItem sendToBackItem = new MenuItem("Send to Back", true, sendToBackCommand);
		
		popupMenuBar.addItem(propertiesItem);
		popupMenuBar.addItem(deleteItem);
		popupMenuBar.addSeparator();
		popupMenuBar.addItem(bringToFrontItem);
		popupMenuBar.addItem(bringForwardItem);
		popupMenuBar.addItem(sendBackwardItem);
		popupMenuBar.addItem(sendToBackItem);

    	popupMenuBar.setVisible(true);
    	add(popupMenuBar);
    	
		sinkEvents(Event.ONCONTEXTMENU); 
	}
	
	public void onBrowserEvent(Event event) {
        event.stopPropagation(); //This will stop the event from being propagated to parent elements.
        switch (DOM.eventGetType(event)) {
	        case Event.ONCONTEXTMENU: 
	        	event.preventDefault();
	        	break;
        }//end switch
	}

}
