// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.risevision.common.client.info.PlaylistItemInfo;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.ui.client.common.widgets.grid.SimpleGridWidget;

public class ScheduleItemGridWidget extends Composite implements ClickHandler {

	public static final int ACTION_SELECT = 1;
	public static final int ACTION_ADD = 2;
	public static final int ACTION_INSERT = 3;
	public static final int ACTION_DELETE = 4;
	public static final int ACTION_MOVEUP = 5;
	public static final int ACTION_MOVEDOWN = 6;

	private VerticalPanel mainPanel = new VerticalPanel();
	private ScheduleItemManageWidget itemManageWidget = new ScheduleItemManageWidget();

	private int currentCommand;
	private String currentKey;
//	private boolean listChanged;
	
//	private int currentRow = 0;
	
	private String[][] header = new String[][] {
			{"Presentation/URL", "100%"},
			{"", "25px"},
			{"", "25px"},
			{"", "40px"},
			{"", "60px"}
			};

	private SimpleGridWidget bodyFlexTable = new SimpleGridWidget(header);
	
	/*
	 * Be careful with saving UI components in datastructures like this: if you
	 * remove a button from the app, make sure you also remove its reference
	 * from buttonMap HashMap to avoid memory leaks.
	 */
	private Map<Anchor, String> actionMap = new HashMap<Anchor, String>();

	private ArrayList<PlaylistItemInfo> playListItems;

	public int getCurrentCommand() {
		return currentCommand;
	}
	
	public String getCurrentKey() {
		return currentKey;
	}
	
	public ScheduleItemGridWidget() {		
		mainPanel.add(bodyFlexTable);

		mainPanel.setWidth("600px");

		initWidget(mainPanel);
	}
	
//	protected void onLoad() {
//		super.onLoad();
//	}
	
	public void load() {
		itemManageWidget.load();
	}

	public void setAction(int row, int column, String text, int actionType,
			String id) {
		Anchor actionHyperlink = new Anchor(text);
		actionHyperlink.addClickHandler(this);
		bodyFlexTable.setWidget(row, column, actionHyperlink);
		actionMap.put(actionHyperlink, actionType + id);
	}
	
	public void setAction(int row, int column, String text, String id) {
		setAction(row, column, text, ACTION_SELECT, id);
	}
	
	private Command onPlayListItemChanged = new Command() {
		public void execute() {
			if (itemManageWidget.getItemIsNew()) {
				getPlayListItems().add(itemManageWidget.getItemIndex(), itemManageWidget.getPlayListItem());
				fixScheduleItemsId();
//				setListChanged(true);
			}
			updateTable();
		}
	};
	
	// The shared ClickHandler code.
	public void onClick(ClickEvent event) {
		Object sender = event.getSource();
		PlaylistItemInfo item;
		int rowIndex;
		if (sender instanceof Anchor) {
			Anchor b = (Anchor) sender;
			String keyString = actionMap.get(b);
			
			if (keyString != null && !keyString.isEmpty()) {
				currentCommand = RiseUtils.strToInt(keyString.substring(0, 1), -1);
				currentKey = keyString.substring(1);
				
				if (currentCommand == ACTION_SELECT) {
					rowIndex = RiseUtils.strToInt(currentKey, 0);
					item = getPlayListItems().get(rowIndex);
					itemManageWidget.show(item, onPlayListItemChanged);
				}

				if (currentCommand == ACTION_ADD) {
					item = new PlaylistItemInfo("New Item");
					itemManageWidget.show(item, true, 0, onPlayListItemChanged);
				}

				if (currentCommand == ACTION_INSERT) {
					item = new PlaylistItemInfo("New Item");
					rowIndex = RiseUtils.strToInt(currentKey, 0);
					itemManageWidget.show(item, true, rowIndex, onPlayListItemChanged);
					updateTable();
				}

				if (currentCommand == ACTION_DELETE) {
					if (Window.confirm("Are you sure you want to delete this item?")) {
						rowIndex = RiseUtils.strToInt(currentKey, 0);
						getPlayListItems().remove(rowIndex);
						fixScheduleItemsId();
//						setListChanged(true);
						updateTable();
					}
				}
				
				if (currentCommand == ACTION_MOVEUP) {
					if (getPlayListItems().size() > 1) {
						rowIndex = RiseUtils.strToInt(currentKey, 0);
						if (rowIndex > 0) {
							item = getPlayListItems().get(rowIndex);
							getPlayListItems().remove(rowIndex);
							getPlayListItems().add(rowIndex-1, item);
							fixScheduleItemsId();
//							setListChanged(true);
							updateTable();
						}
					}
				}

				if (currentCommand == ACTION_MOVEDOWN) {
					if (getPlayListItems().size() > 1) {
						rowIndex = RiseUtils.strToInt(currentKey, 0);
						if (rowIndex < (getPlayListItems().size() - 1)) {
							item = getPlayListItems().get(rowIndex);
							getPlayListItems().remove(rowIndex);
							getPlayListItems().add(rowIndex+1, item);
							fixScheduleItemsId();
//							setListChanged(true);
							updateTable();
						}
					}
				}
			}
		}
	}

	public ArrayList<PlaylistItemInfo> getPlayListItems() {
		if (playListItems == null)
			playListItems = new ArrayList<PlaylistItemInfo>();
		return playListItems;
	}	

	public void setPlayListItems(ArrayList<PlaylistItemInfo> playListItems) {
		this.playListItems = playListItems;
		fixScheduleItemsId();
//		setListChanged(false);
		updateTable();
	}	
	
	private void updateTable() {
		bodyFlexTable.clearGrid();
		if (getPlayListItems().size() == 0)
			addEmptyRow();
		else
			for (int i = 0; i < playListItems.size(); i++)
				updateTableRow(playListItems.get(i), i);
	}

	private void updateTableRow(final PlaylistItemInfo item, int row) {
		String rowId = Integer.toString(row);

		//setAction(row, 0, item.getName(), ACTION_SELECT, item.getId());
		setAction(row, 0, item.getName(), ACTION_SELECT, rowId);
		setAction(row, 1, "\u25B2", ACTION_MOVEUP, rowId); //arrow up
		setAction(row, 2, "\u25BC", ACTION_MOVEDOWN, rowId); //arrow down
		setAction(row, 3, "Add", ACTION_INSERT, rowId);
		setAction(row, 4, "Delete", ACTION_DELETE, rowId);
	}

	private void addEmptyRow() {
		setAction(0, 0, "(List is empty. Click here to add items.)", ACTION_ADD, "0");
		setAction(0, 3, "Add", ACTION_ADD, "0");
	}

//	public void setListChanged(boolean listChanged) {
//		this.listChanged = listChanged;
//	}

//	public boolean isListChanged() {
//		return listChanged;
//	}
	
	private void fixScheduleItemsId() {
		for (int i = 0; i < getPlayListItems().size(); i++)
			getPlayListItems().get(i).setId(Integer.toString(i));
	}

}
