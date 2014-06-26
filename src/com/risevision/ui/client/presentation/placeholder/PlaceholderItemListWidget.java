// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.presentation.placeholder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.risevision.common.client.info.PlaylistItemInfo;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.ui.client.common.dnd.FlexTableRowDragController;
import com.risevision.ui.client.common.dnd.FlexTableRowDropController;
import com.risevision.ui.client.common.widgets.SpacerWidget;
import com.risevision.ui.client.presentation.common.ItemTypeSelectWidget;

public class PlaceholderItemListWidget extends VerticalPanel implements ClickHandler {

	public static final int ACTION_SELECT = 1;
//	public static final int ACTION_ADD = 2;
//	public static final int ACTION_INSERT = 3;
	public static final int ACTION_DELETE = 4;
	public static final int ACTION_MOVEUP = 5;
	public static final int ACTION_MOVEDOWN = 6;

	private FlexTable itemFlexTable = new FlexTable();
	private AbsolutePanel itemTablePanel = new AbsolutePanel();
//	private PlaceholderGadgetManageWidget gadgetManageWidget = PlaceholderGadgetManageWidget.getInstance();
//	private PlaceholderTextManageWidget textManageWidget = PlaceholderTextManageWidget.getInstance();
//	private PlaceholderEmbedManageWidget embedManageWidget = PlaceholderEmbedManageWidget.getInstance();
	
	private HorizontalPanel clearListPanel = new HorizontalPanel();
	private Anchor clearListLink = new Anchor("Clear List");
	
	private FlexTableRowDragController tableRowDragController;
	FlexTableRowDropController flexTableRowDropController;

	private int currentCommand;
	private String currentKey;
	
	String[] header = new String[] {"20px", "100%", "100px", "20px", "20px", "35px", "50px"};

	/*
	 * Be careful with saving UI components in datastructures like this: if you
	 * remove a button from the app, make sure you also remove its reference
	 * from buttonMap HashMap to avoid memory leaks.
	 */
	private Map<Widget, String> actionMap = new HashMap<Widget, String>();

	private ArrayList<PlaylistItemInfo> playlistItems;

	public int getCurrentCommand() {
		return currentCommand;
	}
	
	public String getCurrentKey() {
		return currentKey;
	}
	
	public PlaceholderItemListWidget() {
		super();

		add(clearListPanel);
		add(itemTablePanel);
		add(new HTML("&nbsp;"));

		itemTablePanel.add(itemFlexTable);
		
		clearListPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		clearListPanel.setWidth("100%");
		clearListPanel.add(new HTML("&nbsp;"));
		clearListPanel.add(clearListLink);
		clearListLink.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				clearList();
			}
		});
		
		// style the table
		itemFlexTable.setCellSpacing(0);
		itemFlexTable.setCellPadding(0);
		itemFlexTable.addStyleName("rdn-Grid");
		itemFlexTable.addStyleName("rdn-GridNoHeader");
		
		setWidth("500px");
		
		initDragController();
	}
	
	private void initDragController() {
		DragHandler dragHandler = new DragHandler() {
			
			@Override
			public void onPreviewDragStart(DragStartEvent event)
					throws VetoDragException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onDragStart(DragStartEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onDragEnd(DragEndEvent event) {
				Object o = event.getSource();
				
				if (o instanceof HTML) {
					String key = actionMap.get(o);
					
					if (key != null) {
						int rowIndex = RiseUtils.strToInt(key, 0);
						int targetRow = flexTableRowDropController.getTargetRow();

						PlaylistItemInfo item = playlistItems.get(rowIndex);
						playlistItems.remove(rowIndex);
						
						if (targetRow >= playlistItems.size()) {
							playlistItems.add(item);
						}
						else if (targetRow >= rowIndex) {
							playlistItems.add(targetRow, item);
						}
						else {							
							playlistItems.add(targetRow + 1, item);
						}
						
						fixScheduleItemsId();
						updateTable();
					}
				}
			}
		};
	    // instantiate our drag controller
	    tableRowDragController = new FlexTableRowDragController(itemTablePanel);
	    tableRowDragController.addDragHandler(dragHandler);
	    
	    // instantiate a drop controller for each table
	    flexTableRowDropController = new FlexTableRowDropController(itemFlexTable);
	    tableRowDragController.registerDropController(flexTableRowDropController);
	}

	protected void onLoad() {
		super.onLoad();
	}

	public void addItem(PlaylistItemInfo item, int index) {
		playlistItems.add(index, item);
		fixScheduleItemsId();
		updateTable();
	}
	
	public void deleteItem(int rowIndex) {
		playlistItems.remove(rowIndex);
		fixScheduleItemsId();
		updateTable();
		PlaceholderManageWidget.getInstance().bindItem();
	}

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
					item = playlistItems.get(rowIndex);
					PlaylistItemManageWidget.getInstance().show(item, rowIndex, false, new HashMap<String, Object>());
				}

//				if (currentCommand == ACTION_ADD) {
//					itemManageWidget.show(null, 0);
//				}

//				if (currentCommand == ACTION_INSERT) {
//					rowIndex = RiseUtils.strToInt(currentKey, 0);
//					itemManageWidget.show(null, rowIndex);
//					updateTable();
//				}

				if (currentCommand == ACTION_DELETE) {
					if (Window.confirm("Are you sure you want to delete this item?")) {
						rowIndex = RiseUtils.strToInt(currentKey, 0);
						deleteItem(rowIndex);
					}
				}
				
				if (currentCommand == ACTION_MOVEUP) {
					if (playlistItems.size() > 1) {
						rowIndex = RiseUtils.strToInt(currentKey, 0);
						if (rowIndex > 0) {
							item = playlistItems.get(rowIndex);
							playlistItems.remove(rowIndex);
							playlistItems.add(rowIndex-1, item);
							fixScheduleItemsId();
							updateTable();
						}
					}
				}

				if (currentCommand == ACTION_MOVEDOWN) {
					if (playlistItems.size() > 1) {
						rowIndex = RiseUtils.strToInt(currentKey, 0);
						if (rowIndex < (playlistItems.size() - 1)) {
							item = playlistItems.get(rowIndex);
							playlistItems.remove(rowIndex);
							playlistItems.add(rowIndex+1, item);
							fixScheduleItemsId();
							updateTable();
						}
					}
				}
			}
		}
	}
	
	private void clearList() {
		if (Window.confirm("Are you sure you want to delete all items?")) {
			playlistItems.clear();
			fixScheduleItemsId();
			updateTable();
		}
	}

//	public ArrayList<PlaylistItemInfo> getPlaylistItems() {
//		if (playlistItems == null)
//			playlistItems = new ArrayList<PlaylistItemInfo>();
//		return playlistItems;
//	}	

	public void setPlaylistItems(ArrayList<PlaylistItemInfo> playlistItems) {
		this.playlistItems = playlistItems;
		fixScheduleItemsId();
		updateTable();
	}	
	
	public void updateTable() {
		clearGrid();
		setHeader();
		if (playlistItems.size() == 0) {
			addEmptyRow();
//			clearListLink.setVisible(false);
		}
		else {
			for (int i = 0; i < playlistItems.size(); i++)
				updateTableRow(playlistItems.get(i), i);
//			clearListLink.setVisible(true);
		}
		// only show clear list for 2+ items
		clearListLink.setVisible(playlistItems.size() > 1);

	}

	private void setHeader() {
		int i = 0;
		for (String def : header) {
			itemFlexTable.getColumnFormatter().setWidth(i, def);
			// for firefox
			itemFlexTable.getCellFormatter().setWidth(0, i, def);
			
			i++;
		}
	}
	
	private void updateTableRow(final PlaylistItemInfo item, int row) {
		int col = 0;
		String rowId = Integer.toString(row);
		
		setDragHandle(row, col++, rowId);
		if (playlistItems.size() == 1) {
			setText(row, col++, item.getName());
		}
		else {
			setAction(row, col++, item.getName(), ACTION_SELECT, rowId);
		}
		setWidget(row, col++, new Label(RiseUtils.capitalizeFirstLetter(item.getType())));
		setAction(row, col++, "\u25B2", ACTION_MOVEUP, rowId); //arrow up
		setAction(row, col++, "\u25BC", ACTION_MOVEDOWN, rowId); //arrow down
		setWidget(row, col++, new ItemTypeSelectWidget(row));
		setAction(row, col++, "Delete", ACTION_DELETE, rowId);
	}

	private void addEmptyRow() {
		int col = 0;
		setWidget(0, col++, new SpacerWidget());
		
		setWidget(0, col++, new Label("(Playlist is empty.)"));
	
		setWidget(0, col++, new SpacerWidget());
		setWidget(0, col++, new SpacerWidget());
		setWidget(0, col++, new SpacerWidget());
		
		setWidget(0, col++, new ItemTypeSelectWidget(0, false));		
		setWidget(0, col++, new SpacerWidget());
	}
	
	public void clearGrid() {
		for (int i = itemFlexTable.getRowCount() - 1; i >= 0; i--) {
//			for (int j = 0; j < itemFlexTable.getCellCount(1); j++) {
//				Widget widget = itemFlexTable.getWidget(i, j); 
//				if (widget != null && widget instanceof Anchor) {
//					Anchor link = (Anchor) widget;
//					actionMap.remove(link);
//				}
//			}
			Widget widget = itemFlexTable.getWidget(i, 0); 
			if (widget != null && widget instanceof HTML) {
				tableRowDragController.makeNotDraggable(widget);
			}
			itemFlexTable.removeRow(i);
		}
		actionMap.clear();
	}

	private void setWidget(int row, int column, Widget w) {
		formatCell(row, column);
		itemFlexTable.setWidget(row, column, w);
	}
	
	private void setText(int row, int column, String text) {
		formatCell(row, column);
		itemFlexTable.setText(row, column, text);
	}
	
	private void setDragHandle(int row, int column, String rowId) {
		formatCell(row, column);
		
		HTML handle = new HTML("&nbsp;");
		handle.addStyleName("dragdrop-handle-reorder");
		
		tableRowDragController.makeDraggable(handle);
		
		actionMap.put(handle, rowId);
		itemFlexTable.setWidget(row, column, handle);
	}

	private void setAction(int row, int column, String text, int actionType, String id) {
		formatCell(row, column);
		Anchor actionHyperlink = new Anchor(text);
		actionHyperlink.addClickHandler(this);
		itemFlexTable.setWidget(row, column, actionHyperlink);
		actionMap.put(actionHyperlink, actionType + id);
	}
	
	private void formatCell(int row, int column){
		CellFormatter cellFormatter = itemFlexTable.getCellFormatter();
		
		String def = header[column];
		if (def != null && !def.isEmpty()) {
			cellFormatter.setWidth(row, column, def);
		}
	}
	
	private void fixScheduleItemsId() {
		for (int i = 0; i < playlistItems.size(); i++)
			playlistItems.get(i).setId(Integer.toString(i));
	}
}
