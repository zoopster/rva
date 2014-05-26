package com.risevision.ui.client.common.widgets;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HTMLTable.ColumnFormatter;
import com.google.gwt.user.client.ui.HTMLTable.RowFormatter;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.ui.client.common.info.GridInfo;

public class GridWidget extends Composite implements ClickHandler {

	private VerticalPanel mainPanel = new VerticalPanel();
	private Grid searchGrid = new Grid(1, 5);
	private TextBox searchTextBox = new TextBox();
	private RiseListBox categoryListBox = new RiseListBox();
	private Anchor searchLink = new Anchor("Search");
	private FlexTable usersFlexTable = new FlexTable();
	private HorizontalPanel pagingPanel = new HorizontalPanel();
	private GridInfo gridInfo;

	private Command dataCommand = null;
	private int currentCommand;
	private String currentKey;
	private boolean currentCheckBoxValue;

	/*
	 * Be careful with saving UI components in datastructures like this: if you
	 * remove a button from the app, make sure you also remove its reference
	 * from buttonMap HashMap to avoid memory leaks.
	 */
	private Map<Widget, String> actionMap = new HashMap<Widget, String>();
	private String[][] header;

	public GridInfo getGridInfo() {
		return gridInfo;
	}
	public int getCurrentCommand() {
		return currentCommand;
	}
	
	public String getCurrentKey() {
		return currentKey;
	}

	public boolean getCurrentCheckBoxValue() {
		return currentCheckBoxValue;
	}
	
	public GridWidget(Command dataCommand, String[][] header, String[][] searchCategories) {
		this.dataCommand = dataCommand;

		if (searchCategories != null) {
			searchTextBox.setStyleName("rdn-TextBoxMedium");
			categoryListBox.setStyleName("rdn-ListBoxMedium");
			
			searchLink.addClickHandler(this);
			actionMap.put(searchLink, GridInfo.SEARCHACTION + "");

			searchGrid.setCellPadding(0);
			searchGrid.setCellSpacing(0);
			//searchGrid.setStyleName("rdn-Grid");
			searchGrid.setWidget(0, 0, searchTextBox);
			searchGrid.setWidget(0, 1, new InlineHTML("&nbsp;&nbsp;&nbsp;"));
			searchGrid.setWidget(0, 2, categoryListBox);
			searchGrid.setWidget(0, 3, new InlineHTML("&nbsp;&nbsp;&nbsp;"));
			searchGrid.setWidget(0, 4, searchLink);
			
			mainPanel.add(searchGrid);
			
			setSearchCategories(searchCategories);
		}

		mainPanel.add(usersFlexTable);
		mainPanel.add(pagingPanel);
		mainPanel.setStyleName("rdn-TableShort");

		// style the table
		usersFlexTable.setCellSpacing(0);
		usersFlexTable.setCellPadding(0);
		usersFlexTable.setStyleName("rdn-Grid");

		this.header = header;
		setHeader();

		initWidget(mainPanel);
	}

	private void setSearchCategories(String[][] searchCategories) {
		for (String[] def : searchCategories) {
			if (def[1] != null){
				categoryListBox.addItem(def[0], def[1]);
			}
		}
	}
	
	private void setHeader() {
		int i = 0;
		for (String[] def : header) {
			if (def[1] != null) {
				if (GridInfo.CHECKBOX.equals(def[1])) {
					CheckBox checkAll = new CheckBox();
					checkAll.addClickHandler(this);
					actionMap.put(checkAll, GridInfo.CHECKALLACTION + Integer.toString(i));
					usersFlexTable.setWidget(0, i, checkAll);
				}
				else {
					Anchor sortLink = new Anchor(def[0]);
					sortLink.setStyleName("rdn-GridItemLink");
					sortLink.addClickHandler(this);
					actionMap.put(sortLink, GridInfo.SORTACTION + def[1]);
					usersFlexTable.setWidget(0, i, sortLink);
				}
			} else {
				if (def[0] == null || def[0].isEmpty())
					usersFlexTable.setHTML(0, i, "&nbsp;");
				else
					usersFlexTable.setText(0, i, def[0]);
			}
				
			CellFormatter cellFormatter = usersFlexTable.getCellFormatter();
			if (def[0] == null || def[0].isEmpty()) {
				cellFormatter.setStyleName(0, i, "rdn-GridItemHeaderShort");
			}
			else {
				cellFormatter.setStyleName(0, i, "rdn-GridItemHeader");
			}
				
			if (def.length > 2){
				ColumnFormatter colFormatter = usersFlexTable.getColumnFormatter();
				colFormatter.setWidth(i, def[2]);
				// for firefox
				cellFormatter.setWidth(0, i, def[2]);
			}
			
			i++;
		}
		RowFormatter rowFormatter = usersFlexTable.getRowFormatter();
		rowFormatter.setStyleName(0, "rdn-GridHeader");

	}

	protected void onLoad() {
		searchTextBox.setFocus(true);
		super.onLoad();
	}

	public void loadGrid(GridInfo gridInfo) {
		this.gridInfo = gridInfo;
		if (gridInfo != null && dataCommand != null) {

			updatePaging();
		}
	}

	public void setText(int row, int column, String text) {
		Label textLabel = new Label(text);
		try {
			String [] colProperties = header[column];
			
			textLabel.addStyleName("rdn-OverflowElipsis");
			textLabel.setWidth(colProperties[2]);
		}
		catch (Exception e) {
			
		}
		setWidget(row, column, textLabel);
	}

	public void setAction(int row, int column, String text, int actionType, String id) {
		Anchor actionHyperlink = new Anchor(text);
		actionHyperlink.addClickHandler(this);
		setWidget(row, column, actionHyperlink);
		actionMap.put(actionHyperlink, actionType + id);
	}
	
	public void setWidget(int row, int column, Widget widget) {
		formatCell(row, column);	
		usersFlexTable.setWidget(row, column, widget);
	}
	
	public void setAction(int row, int column, String text, String id) {
		setAction(row, column, text, GridInfo.SELECTACTION, id);
	}
	
	public void setCheckBox(int row, int column, Boolean checked,  String id) {
		CheckBox cb = new CheckBox();
		cb.setValue(checked);
		cb.addClickHandler(this);
		setWidget(row, column, cb);
		actionMap.put(cb, GridInfo.CHECKACTION + id);
	}

	private void formatCell(int row, int column){
		CellFormatter cellFormatter = usersFlexTable.getCellFormatter();
		RowFormatter rowFormatter = usersFlexTable.getRowFormatter();
		cellFormatter.setStyleName(row, column, "rdn-GridItem");
		
		for (int i = 0; i < usersFlexTable.getCellCount(0); i++){			
			cellFormatter.addStyleName(row, i, "rdn-GridItemFooterBottom");
			
			if (row > 0) {
				cellFormatter.removeStyleName(row - 1, i, "rdn-GridItemFooterBottom");
			}
		}

		if (row%2 == 0){
			rowFormatter.addStyleName(row, "rdn-GridAlternateRow");
		}
	}

	// The shared ClickHandler code.
	public void onClick(ClickEvent event) {
		Object sender = event.getSource();
		if (sender instanceof Anchor) {
			Anchor b = (Anchor) sender;
			String keyString = actionMap.get(b);
			
			if (keyString != null && !keyString.isEmpty()) {
				currentCommand = RiseUtils.strToInt(keyString.substring(0, 1), -1);
				currentKey = keyString.substring(1);
				
				if (currentCommand == GridInfo.SORTACTION){
					gridInfo.setSortBy(currentKey);
					// set paging to first page
					gridInfo.setCurrentPage(1);
				}
				else if (currentCommand == GridInfo.PAGEACTION) {
					int key = RiseUtils.strToInt(currentKey, 0);
					if (key != 0) {
						gridInfo.setCurrentPage(key);
					}
				}
				else if (currentCommand == GridInfo.SEARCHACTION) {
					gridInfo.setSortBy(categoryListBox.getSelectedValue());
					gridInfo.setSearchFor(searchTextBox.getText());
					
					// set paging to first page
					gridInfo.setCurrentPage(1);
				}

				dataCommand.execute();
			}
		}
		else if (sender instanceof CheckBox) {
			String keyString = actionMap.get(sender);
			
			if (keyString != null && !keyString.isEmpty()) {
				currentCommand = RiseUtils.strToInt(keyString.substring(0, 1), -1);
				currentKey = keyString.substring(1);
				currentCheckBoxValue = ((CheckBox)sender).getValue();
				dataCommand.execute();
			}
		}
	}

	public void clear() {
		searchTextBox.setText("");
		if (gridInfo != null) {
			gridInfo.setPageCount(0);
			clearGrid();
			gridInfo.setSortBy("");
			gridInfo.setSearchFor("");
		}
	}

	public void clearGrid() {
		updatePaging();
		for (int i = usersFlexTable.getRowCount() - 1; i > 0; i--) {
			for (int j = 0; j < usersFlexTable.getCellCount(1); j++) {
				Widget widget = usersFlexTable.getWidget(i, j); 
				if (widget != null && widget instanceof Anchor) {
					Anchor link = (Anchor) widget;
					actionMap.remove(link);
				}
			}
			usersFlexTable.removeRow(i);
		}
	}
	
	private void updatePaging() {
		// remove previous widgets
		for (int i = 0; i < pagingPanel.getWidgetCount(); i++) {
			if (pagingPanel.getWidget(i) instanceof Anchor) {
				Anchor link = (Anchor) pagingPanel.getWidget(i);
				actionMap.remove(link);
			}
		}
		pagingPanel.clear();

		updatePagingHyperlink("<<", gridInfo.getFirstPage());
		updatePagingHyperlink("<", gridInfo.getPrevPage());
		
		for (int i = 1; i <= gridInfo.getPageCount(); i++) {
			if (i != gridInfo.getCurrentPage()) {
				updatePagingHyperlink(i + "", i);
			} else {
				Label pageLabel = new Label(i + "");
				pageLabel.setStyleName("rdn-GridItemLink");
				pagingPanel.add(pageLabel);
			}
		}

		updatePagingHyperlink(">", gridInfo.getNextPage());
		updatePagingHyperlink(">>", gridInfo.getLastPage());
	}
	
	private void updatePagingHyperlink(String text, Integer key){
		Anchor pageHyperlink;
		
		if (key != 0) {
			pageHyperlink = new Anchor(text);
			pageHyperlink.setStyleName("rdn-GridItemLink");
			pageHyperlink.addClickHandler(this);
			pagingPanel.add(pageHyperlink);
			actionMap.put(pageHyperlink, GridInfo.PAGEACTION + key.toString());
			pagingPanel.setSpacing(3);
		}
	}
	
	public void checkAll(boolean value) {
		for (Widget w: actionMap.keySet())
			if (w instanceof CheckBox)
				((CheckBox)w).setValue(value);
	}
}
