// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.financial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.ui.client.common.data.FinancialDataController;
import com.risevision.ui.client.common.dnd.FlexTableRowDragController;
import com.risevision.ui.client.common.dnd.FlexTableRowDropController;
import com.risevision.ui.client.common.info.FinancialItemInfo;
import com.risevision.ui.client.common.info.FinancialItemsInfo;
import com.risevision.ui.client.common.widgets.ActionsWidget;
import com.risevision.ui.client.common.widgets.SpacerWidget;
import com.risevision.ui.client.common.widgets.StatusBoxWidget;

public class InstrumentListWidget extends PopupPanel implements ClickHandler {
	private static InstrumentListWidget instance;

//	public static final int ACTION_SELECT = 1;
	public static final int ACTION_ADD = 2;
//	public static final int ACTION_INSERT = 3;
	public static final int ACTION_DELETE = 4;
//	public static final int ACTION_MOVEUP = 5;
//	public static final int ACTION_MOVEDOWN = 6;

	private VerticalPanel mainPanel = new VerticalPanel();
	
	private Label titleLabel = new Label("Instruments");
	private StatusBoxWidget statusBox = new StatusBoxWidget();
	
	private FlexTable itemFlexTable = new FlexTable();
	private AbsolutePanel itemTablePanel = new AbsolutePanel();
	
//	private HorizontalPanel clearListPanel = new HorizontalPanel();
//	private Anchor clearListLink = new Anchor("Clear List");

	private ActionsWidget actionsWidget = new ActionsWidget();
	
	private FlexTableRowDragController tableRowDragController;
	FlexTableRowDropController flexTableRowDropController;

	private Command saveCommand;
	
	StockSelectorWidget stockSelector;
	private Command addCommand = new Command() {
		@Override
		public void execute() {
			addItem(stockSelector.getItem(), RiseUtils.strToInt(currentKey, 0));
		}
	};

	private int currentCommand;
	private String currentKey;
	
	String[] header = new String[] {"20px", 
			"100%", 
//			"20px", 
//			"20px", 
			"35px", 
			"50px"};

	/*
	 * Be careful with saving UI components in datastructures like this: if you
	 * remove a button from the app, make sure you also remove its reference
	 * from buttonMap HashMap to avoid memory leaks.
	 */
	private Map<Widget, String> actionMap = new HashMap<Widget, String>();

	private ArrayList<FinancialItemInfo> financialItems;

//	public int getCurrentCommand() {
//		return currentCommand;
//	}
	
//	public String getCurrentKey() {
//		return currentKey;
//	}
	
	public InstrumentListWidget() {
		super(true, false); //set auto-hide and modal
		
		mainPanel.add(titleLabel);
		mainPanel.setCellHeight(titleLabel, "20px");
		
		mainPanel.add(statusBox);
		
//		mainPanel.add(clearListPanel);
//		mainPanel.add(new HTML("&nbsp;"));

		itemTablePanel.add(itemFlexTable);
		
		mainPanel.add(itemTablePanel);
		mainPanel.add(actionsWidget);

		add(mainPanel);
		
//		clearListPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
//		clearListPanel.setWidth("100%");
//		clearListPanel.add(new HTML("&nbsp;"));
//		clearListPanel.add(clearListLink);
//		clearListLink.addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				clearList();
//			}
//		});
		
		styleControls();
		initActions();
		initDragController();
		
//		FinancialDataAccessController.initializeApi();
	}
	
	private void styleControls() {
		// style the table
		itemFlexTable.setCellSpacing(0);
		itemFlexTable.setCellPadding(0);
		itemFlexTable.addStyleName("rdn-Grid");
		itemFlexTable.addStyleName("rdn-GridNoHeader");
		
		setSize("600px", "100%");
		
		titleLabel.setStyleName("rdn-Head");
		
		this.getElement().getStyle().setProperty("padding", "10px");
		getElement().getStyle().setZIndex(1000);

		actionsWidget.addStyleName("rdn-VerticalSpacer");
	}
	
	private void initActions() {
		Command saveCommand = new Command() {
			@Override
			public void execute() {
				saveItems();
			}
		};
		
		Command closeCommand = new Command() {
			@Override
			public void execute() {
				hide();
			}
		};
			
		actionsWidget.addAction("Save", saveCommand);
		actionsWidget.addAction("Cancel", closeCommand);
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

						FinancialItemInfo item = financialItems.get(rowIndex);
						financialItems.remove(rowIndex);
						
						if (targetRow >= financialItems.size()) {
							financialItems.add(item);
						}
						else if (targetRow >= rowIndex) {
							financialItems.add(targetRow, item);
						}
						else {							
							financialItems.add(targetRow + 1, item);
						}
						
//						fixScheduleItemsId();
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
	
	public static InstrumentListWidget getInstance() {
		try {
			if (instance == null)
				instance = new InstrumentListWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}	

	public void show(Command saveCommand, String items) {
		this.saveCommand = saveCommand;
		
		stockSelector = StockSelectorWidget.getInstance();
		
		statusBox.clear();
		 
		setItemsList(items);
		
		super.show();
		center();
	}

	private void addItem(FinancialItemInfo item, int index) {
		financialItems.add(index, item);
//		fixScheduleItemsId();
		updateTable();
	}
	
	private void deleteItem(int rowIndex) {
		financialItems.remove(rowIndex);
//		fixScheduleItemsId();
		updateTable();
	}

	// The shared ClickHandler code.
	public void onClick(ClickEvent event) {
		Object sender = event.getSource();
//		FinancialItemInfo item;
		int rowIndex;
		if (sender instanceof Anchor) {
			Anchor b = (Anchor) sender;
			String keyString = actionMap.get(b);
			
			if (keyString != null && !keyString.isEmpty()) {
				currentCommand = RiseUtils.strToInt(keyString.substring(0, 1), -1);
				currentKey = keyString.substring(1);
				
//				if (currentCommand == ACTION_SELECT) {
//					rowIndex = RiseUtils.strToInt(currentKey, 0);
//					item = financialItems.get(rowIndex);
//					PlaylistItemManageWidget.getInstance().show(item, rowIndex);
//				}

				if (currentCommand == ACTION_ADD) {
					stockSelector.show(addCommand);
				}

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
				
//				if (currentCommand == ACTION_MOVEUP) {
//					if (financialItems.size() > 1) {
//						rowIndex = RiseUtils.strToInt(currentKey, 0);
//						if (rowIndex > 0) {
//							item = financialItems.get(rowIndex);
//							financialItems.remove(rowIndex);
//							financialItems.add(rowIndex-1, item);
//							updateTable();
//						}
//					}
//				}

//				if (currentCommand == ACTION_MOVEDOWN) {
//					if (financialItems.size() > 1) {
//						rowIndex = RiseUtils.strToInt(currentKey, 0);
//						if (rowIndex < (financialItems.size() - 1)) {
//							item = financialItems.get(rowIndex);
//							financialItems.remove(rowIndex);
//							financialItems.add(rowIndex+1, item);
//							updateTable();
//						}
//					}
//				}
			}
		}
	}
	
//	private void clearList() {
//		if (Window.confirm("Are you sure you want to delete all items?")) {
//			financialItems.clear();
//			fixScheduleItemsId();
//			updateTable();
//		}
//	}
	
	private void saveItems() {
		if (saveCommand != null) {
			saveCommand.execute();
		}
		
		hide();
	}

	public String getItemsList() {
		String items = "";
		
		for (FinancialItemInfo item: financialItems) {
			items += item.getCode() + ", ";
		}
		
		items = items.isEmpty() ? items : items.substring(0, items.length() - 2);
		
		return items;
	}
	
	private void setItemsList(String items) {
		financialItems = new ArrayList<FinancialItemInfo>();
		
		if (items.isEmpty()) {
			updateTable();			
		}
		else {
			String [] itemsList = items.split(",");
			
			for (int i = 0; i < itemsList.length; i++) {
				itemsList[i] = itemsList[i].trim();
			}
			
			for (String item: itemsList) {
				financialItems.add(new FinancialItemInfo(item));
			}
			
//			fixScheduleItemsId();
			
			loadDataRPC(itemsList);
			
//			updateTable();
		}
	}	
	
	private void updateTable() {
		clearGrid();
		setHeader();
		if (financialItems.size() == 0) {
			addEmptyRow();
//			clearListLink.setVisible(false);
		}
		else {
			for (int i = 0; i < financialItems.size(); i++)
				updateTableRow(financialItems.get(i), i);
//			clearListLink.setVisible(true);
		}
		
		center();
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
	
	private void updateTableRow(final FinancialItemInfo item, int row) {
		int col = 0;
		String rowId = Integer.toString(row);
		
		setDragHandle(row, col++, rowId);
		setText(row, col++, RiseUtils.strIsNullOrEmpty(item.getName()) ? item.getCode() : item.getName() + " - " + item.getCode());
//		setAction(row, col++, "\u25B2", ACTION_MOVEUP, rowId); //arrow up
//		setAction(row, col++, "\u25BC", ACTION_MOVEDOWN, rowId); //arrow down
		setAction(row, col++, "Add", ACTION_ADD, rowId);
		setAction(row, col++, "Delete", ACTION_DELETE, rowId);
	}

	private void addEmptyRow() {
		int col = 0;
		setWidget(0, col++, new SpacerWidget());
		
		setWidget(0, col++, new Label("(List is empty.)"));
	
//		setWidget(0, col++, new SpacerWidget());
//		setWidget(0, col++, new SpacerWidget());
		
		setAction(0, col++, "Add", ACTION_ADD, "0");		
		setWidget(0, col++, new SpacerWidget());
	}
	
	private void clearGrid() {
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
	
	private void loadDataRPC(String[] itemsList) {
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, "Data is loading...");
		
		FinancialDataController.getInstance().getInstruments(itemsList, new RpcCallBackHandler());
	}
	
	private void bindData(ArrayList<FinancialItemInfo> result) {
		for(FinancialItemInfo item : financialItems) {
			for (FinancialItemInfo namedItem : result) {
				if (item.getCode().equals(namedItem.getCode()))	{
					item.setName(namedItem.getName());
					break;
				}
			}
		}
		
		updateTable();
	}
	
	class RpcCallBackHandler implements AsyncCallback<FinancialItemsInfo> {
		public void onFailure(Throwable caught) {
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}
		
		public void onSuccess(FinancialItemsInfo result) {	
			statusBox.clear();
			bindData(result.getItems());
		}
	}

}
