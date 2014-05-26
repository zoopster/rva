// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.grid;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HTMLTable.ColumnFormatter;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.ui.client.common.controller.SelectedCompanyController;
import com.risevision.ui.client.common.info.ScrollingGridInfo;
import com.risevision.ui.client.common.widgets.SpacerWidget;

public class ScrollingGridWidget extends Composite implements ClickHandler {

	private VerticalPanel mainPanel = new VerticalPanel();
	private TextBox searchTextBox = new TextBox();
//	private RiseListBox categoryListBox = new RiseListBox();
	private Anchor searchLink = new Anchor("Search");
	private Grid headerTable;
	private ScrollPanel scrollingBodyPanel = new ScrollPanel();
	private FlexTable bodyFlexTable = new FlexTable();
//	private HorizontalPanel pagingPanel = new HorizontalPanel();
	private ScrollingGridInfo gridInfo;

	private Command dataCommand = null;
	private int currentCommand;
	private String currentKey;
	private boolean currentCheckBoxValue;
	
	private boolean lastColumnShortened = false;

	/*
	 * Be careful with saving UI components in datastructures like this: if you
	 * remove a button from the app, make sure you also remove its reference
	 * from buttonMap HashMap to avoid memory leaks.
	 */
	private Map<Widget, String> actionMap = new HashMap<Widget, String>();
	private String[][] header;

	public ScrollingGridInfo getGridInfo() {
		return gridInfo;
	}
	
	public int getCurrentCommand() {
		return currentCommand;
	}
	
	public String getCurrentKey() {
		return currentKey;
	}
	
//	public void setCurrentKey(String key) {
//		this.currentKey = key;
//	}

	public boolean getCurrentCheckBoxValue() {
		return currentCheckBoxValue;
	}
	
	public ScrollingGridWidget(Command dataCommand, ScrollingGridInfo gridInfo, String[][] header) {
		this(dataCommand, gridInfo, header, true);
	}
	
	public ScrollingGridWidget(Command dataCommand, ScrollingGridInfo gridInfo, String[][] header, boolean enableSearch) {
		this.dataCommand = dataCommand;
		this.gridInfo = gridInfo;
		
		if(enableSearch) {
			searchLink.addClickHandler(this);
			actionMap.put(searchLink, ScrollingGridInfo.SEARCHACTION + "");
	
			HorizontalPanel searchPanel = new HorizontalPanel();
	
			AbsolutePanel searchTextBoxPanel = new AbsolutePanel();
			searchTextBoxPanel.add(searchTextBox);
			
			createClearableTextBox(searchTextBoxPanel);
			
			searchPanel.add(searchTextBoxPanel);
			searchPanel.add(new SpacerWidget());
			searchPanel.add(searchLink);
			
			searchPanel.getElement().getStyle().setPaddingBottom(6, Unit.PX);
			
			mainPanel.add(searchPanel);
		}

		headerTable = new Grid(1, header.length);
		mainPanel.add(headerTable);
		
		scrollingBodyPanel.add(bodyFlexTable);
		
		mainPanel.add(scrollingBodyPanel);
//		mainPanel.add(pagingPanel);

		this.header = header;
		styleControls();
		setHeader();
		initHandlers();

		initWidget(mainPanel);
	}
	
	private void createClearableTextBox(AbsolutePanel panel) {
		HTML clearPanel = new HTML("<a class='textbox-clear-button'>Delete</a>");
			
		clearPanel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {				
				clearSearch();
			}
		});

		searchTextBox.getElement().getStyle().setPaddingRight(25, Unit.PX);
		
		panel.add(clearPanel, 434, 4);
	}
	
	private void styleControls() {
		searchTextBox.setStyleName("rdn-TextBoxLong");

		mainPanel.setStyleName("rdn-TableShort");
		mainPanel.getElement().getStyle().setHeight(100, Unit.PCT);

		headerTable.setCellSpacing(0);
		headerTable.setCellPadding(0);
		headerTable.setStyleName("rdn-GridHeader");
//		headerTable.getElement().getStyle().setTableLayout(TableLayout.FIXED);
//		headerTable.getElement().getStyle().setWidth(100, Unit.PCT);
		headerTable.getElement().getStyle().setProperty("maxWidth", "1200px");
		
		// style the table
		bodyFlexTable.setCellSpacing(0);
		bodyFlexTable.setCellPadding(0);
		bodyFlexTable.setStyleName("rdn-Grid");
//		bodyFlexTable.getElement().getStyle().setTableLayout(TableLayout.FIXED);
//		bodyFlexTable.getElement().getStyle().setWidth(100, Unit.PCT);
		
		scrollingBodyPanel.getElement().getStyle().setHeight(100, Unit.PCT);
		scrollingBodyPanel.getElement().getStyle().setOverflow(Overflow.AUTO);
		scrollingBodyPanel.setAlwaysShowScrollBars(false);
		scrollingBodyPanel.addStyleName("jfk-scrollbar");
		
		scrollingBodyPanel.getElement().getParentElement().getStyle().setHeight(100, Unit.PCT);
	}
	
	private void setHeader() {
		int i = 0;
		for (String[] def : header) {	
//			String width = (100 / header.length) + "%";
			
			if (def[1] != null) {
				if (ScrollingGridInfo.CHECKBOX.equals(def[1])) {
					CheckBox checkAll = new CheckBox();
					checkAll.addClickHandler(this);
					actionMap.put(checkAll, ScrollingGridInfo.CHECKALLACTION + Integer.toString(i));
					headerTable.setWidget(0, i, checkAll);
				}
				else {
					String name = def[0];
					
					if (def[1].equals(gridInfo.getSortBy())) {
						name += " " + gridInfo.getSortDirectionChar();
					}
						
					Anchor sortLink = new Anchor(name);
//					sortLink.setStyleName("rdn-GridItemLink");
					sortLink.addClickHandler(this);
					actionMap.put(sortLink, ScrollingGridInfo.SORTACTION + def[1]);
					headerTable.setWidget(0, i, sortLink);
				}
			} else {
				if (def[0] == null || def[0].isEmpty())
					headerTable.setText(0, i, "");
				else
					headerTable.setText(0, i, def[0]);
			}
			
//			CellFormatter cellFormatter = headerTable.getCellFormatter();
//			if (def[0] == null || def[0].isEmpty()) {
//				cellFormatter.setStyleName(0, i, "rdn-GridItemHeaderShort");
//			}
//			else {
//			cellFormatter.setStyleName(0, i, "rdn-GridItemHeader");
//			}
				
			if (def.length > 2){
				ColumnFormatter colFormatter = headerTable.getColumnFormatter();
				colFormatter.setWidth(i, def[2]);
				// for firefox
				CellFormatter cellFormatter = headerTable.getCellFormatter();
				cellFormatter.setWidth(0, i, def[2]);
//				if (def[2].equals("100%")) {
//					cellFormatter.getElement(0, i).getStyle().setProperty("maxWidth", "400px");
//					colFormatter.getElement(i).getStyle().setProperty("maxWidth", "400px");
//				}
			}
			
			i++;
		}
		
		// no style corrresponds to rdn-GridHeader
//		RowFormatter rowFormatter = headerTable.getRowFormatter();
//		rowFormatter.setStyleName(0, "rdn-GridHeader");
	}

	private void initHandlers() {
		searchTextBox.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getCharCode() == KeyCodes.KEY_ENTER) {
//					if (!searchTextBox.getText().isEmpty()) {
						updateSearch(searchTextBox.getText());
//					}
				}
			}
		});
		
		scrollingBodyPanel.addScrollHandler(new ScrollHandler() {
			@Override
			public void onScroll(ScrollEvent event) {
				onGridScroll();
			}
		});
	}
	
	protected void onAttach() {
		mainPanel.getElement().getParentElement().getStyle().setProperty("maxWidth", "1200px");
		
		super.onAttach();
	}
	
	protected void onLoad() {
		searchTextBox.setFocus(true);
		super.onLoad();
	}

	public void loadGrid(ScrollingGridInfo gridInfo) {
		this.gridInfo = gridInfo;
//		if (gridInfo != null && dataCommand != null) {
//
//			updatePaging();
//		}
	}
	
	public int getRowCount() {
		return bodyFlexTable.getRowCount();
	}
	
	public void clearSearch() {
		searchTextBox.setText("");

		updateSearch("");
	}
	
	private void updateSearch(String searchString) {
		if (!(RiseUtils.strIsNullOrEmpty(searchString) && RiseUtils.strIsNullOrEmpty(gridInfo.getSearchFor()))) {
			currentCommand = ScrollingGridInfo.SEARCHACTION;
			
			gridInfo.setSearchFor(searchString);
			
			// set paging to first page
			gridInfo.resetPage();
	
			dataCommand.execute();
		}
		// if both search strings are null, don't reset results
		else {
			
		}
	}

	public void setGridHeight(String height) {
		scrollingBodyPanel.getElement().getStyle().setProperty("height", height);
	}
	
	public void setText(int row, int column, String text) {
//		Label textLabel = new Label(text);
//		try {
//			String [] colProperties = header[column];
//			
//			textLabel.addStyleName("rdn-OverflowElipsis");
//			textLabel.setWidth(colProperties[2]);
//		}
//		catch (Exception e) {
//			
//		}
//		setWidget(row, column, textLabel);
		
		formatCell(row, column);
		bodyFlexTable.setText(row, column, text);
	}

	public void setAction(int row, int column, String text, int actionType, String id) {
		Anchor actionHyperlink = new Anchor(text);
		actionHyperlink.addClickHandler(this);
		setWidget(row, column, actionHyperlink);
		actionMap.put(actionHyperlink, actionType + id);
	}
	
	public void setWidget(int row, int column, Widget widget) {
		formatCell(row, column);	
		bodyFlexTable.setWidget(row, column, widget);
	}
	
	public void setAction(int row, int column, String text, String id) {
		setAction(row, column, text, ScrollingGridInfo.SELECTACTION, id);
	}
	
	public void setHyperlink(int row, int column, String text, String contentId, String id) {
		setHyperlink(row, column, text, contentId, id, 
				SelectedCompanyController.getInstance().getSelectedCompanyId());
	}
	
	public void setHyperlink(int row, int column, String text, String contentId, String id, String companyId) {
		Anchor actionHyperlink = new Anchor(text);
		actionHyperlink.setHref("#" + contentId + "/id=" + id + "/company=" + companyId);

		setWidget(row, column, actionHyperlink);
	}
	
	public void setCheckBox(int row, int column, Boolean checked,  String id) {
		CheckBox cb = new CheckBox();
		cb.setValue(checked);
		cb.addClickHandler(this);
		setWidget(row, column, cb);
		actionMap.put(cb, ScrollingGridInfo.CHECKACTION + id);
	}
	
	public CellFormatter getCellFormatter() {
		return bodyFlexTable.getCellFormatter();
	}

	private void formatCell(int row, int column){	
		CellFormatter cellFormatter = bodyFlexTable.getCellFormatter();
		ColumnFormatter columnFormatter = bodyFlexTable.getColumnFormatter();
//		RowFormatter rowFormatter = bodyFlexTable.getRowFormatter();
//		cellFormatter.setStyleName(row, column, "rdn-GridItem");
		
//		for (int i = 0; i < bodyFlexTable.getCellCount(0); i++){			
//			cellFormatter.addStyleName(row, i, "rdn-GridItemFooterBottom");
//			
//			if (row > 0) {
//				cellFormatter.removeStyleName(row - 1, i, "rdn-GridItemFooterBottom");
//			}
//		}

//		if (row%2 == 0){
//			rowFormatter.addStyleName(row, "rdn-GridAlternateRow");
//		}
		
		String[] def = header[column];
		if (def != null && def.length > 2 && def[2] != null && !def[2].isEmpty()) {
			cellFormatter.setWidth(row, column, def[2]);
			columnFormatter.setWidth(column, def[2]);
		}
		
		if (bodyFlexTable.getOffsetWidth() < headerTable.getOffsetWidth()) {
			if (!lastColumnShortened) {
				int width = headerTable.getCellFormatter().getElement(0, header.length -1).getOffsetWidth();
				
				headerTable.getColumnFormatter().setWidth(header.length -1, width + 16 + "px");
				
				lastColumnShortened = true;
			}
		}
		else {
			if (lastColumnShortened) {
				int width = headerTable.getCellFormatter().getElement(0, header.length -1).getOffsetWidth();
				
				headerTable.getColumnFormatter().setWidth(header.length -1, width - 16 + "px");
				
				lastColumnShortened = false;
			}		
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
				
				if (currentCommand == ScrollingGridInfo.SORTACTION){
					clearSorting();
					
					if (gridInfo.getSortBy() != null && gridInfo.getSortBy().equals(currentKey) && gridInfo.getSortDirection().equals(ScrollingGridInfo.SORT_DOWN)) {
						gridInfo.setSortDirection(ScrollingGridInfo.SORT_UP);
					}
					else {
						gridInfo.setSortDirection(ScrollingGridInfo.SORT_DOWN);
					}

					b.setText(b.getText() + " " + gridInfo.getSortDirectionChar());
					gridInfo.setSortBy(currentKey);

					// set paging to first page
					gridInfo.resetPage();
				}
//				else if (currentCommand == ScrollingGridInfo.PAGEACTION) {
//					int key = RiseUtils.strToInt(currentKey, 0);
//					if (key != 0) {
//						gridInfo.setCurrentPage(key);
//					}
//				}
				else if (currentCommand == ScrollingGridInfo.SEARCHACTION) {
					gridInfo.setSearchFor(searchTextBox.getText());
					
					// set paging to first page
					gridInfo.resetPage();
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
	
	private void onGridScroll() {
		double totalRecords = (gridInfo.getPage() + 1) * gridInfo.getPageSize();
//		double totalRecords = bodyFlexTable.getRowCount();

		if (totalRecords == bodyFlexTable.getRowCount()) {
			double scrollPosition = scrollingBodyPanel.getVerticalScrollPosition() + scrollingBodyPanel.getOffsetHeight();
						
			// position out of 1
			double position = scrollPosition / bodyFlexTable.getOffsetHeight();
			
			double recordsLeft = (1 - position) * totalRecords;
			
			if (recordsLeft < (gridInfo.getPageSize() / 4)) {
				currentCommand = ScrollingGridInfo.PAGEACTION;
	
				gridInfo.nextPage();
				
				dataCommand.execute();
			}
		}
	}

	public void clear() {
		searchTextBox.setText("");
		if (gridInfo != null) {
//			gridInfo.setPageCount(0);
			clearGrid();
//			gridInfo.setSortBy("");
//			gridInfo.setDefaultSort();
			gridInfo.setSearchFor("");
			
			gridInfo.resetPage();
		}
	}

	public void clearGrid() {
//		updatePaging();
		for (int i = bodyFlexTable.getRowCount() - 1; i >= 0; i--) {
			for (int j = 0; j < bodyFlexTable.getCellCount(0); j++) {
				Widget widget = bodyFlexTable.getWidget(i, j); 
				if (widget != null && widget instanceof Anchor) {
					Anchor link = (Anchor) widget;
					actionMap.remove(link);
				}
			}
			bodyFlexTable.removeRow(i);
		}
	}
	
	private void clearSorting() {
		int i = 0;
		for (String[] def : header) {
			if (def[1] != null) {
				if (!ScrollingGridInfo.CHECKBOX.equals(def[1])) {
					Widget widget = headerTable.getWidget(0, i);
					if (widget != null && widget instanceof Anchor) {
						Anchor sortLink = (Anchor) widget; 
						sortLink.setText(def[0]);
					}
				}
			}
			
			i++;
		}
	}
	
//	private void updatePaging() {
//		// remove previous widgets
//		for (int i = 0; i < pagingPanel.getWidgetCount(); i++) {
//			if (pagingPanel.getWidget(i) instanceof Anchor) {
//				Anchor link = (Anchor) pagingPanel.getWidget(i);
//				actionMap.remove(link);
//			}
//		}
//		pagingPanel.clear();
//
//		updatePagingHyperlink("<<", gridInfo.getFirstPage());
//		updatePagingHyperlink("<", gridInfo.getPrevPage());
//		
//		for (int i = 1; i <= gridInfo.getPageCount(); i++) {
//			if (i != gridInfo.getCurrentPage()) {
//				updatePagingHyperlink(i + "", i);
//			} else {
//				Label pageLabel = new Label(i + "");
//				pageLabel.setStyleName("rdn-GridItemLink");
//				pagingPanel.add(pageLabel);
//			}
//		}
//
//		updatePagingHyperlink(">", gridInfo.getNextPage());
//		updatePagingHyperlink(">>", gridInfo.getLastPage());
//	}
	
//	private void updatePagingHyperlink(String text, Integer key){
//		Anchor pageHyperlink;
//		
//		if (key != 0) {
//			pageHyperlink = new Anchor(text);
//			pageHyperlink.setStyleName("rdn-GridItemLink");
//			pageHyperlink.addClickHandler(this);
//			pagingPanel.add(pageHyperlink);
//			actionMap.put(pageHyperlink, ScrollingGridInfo.PAGEACTION + key.toString());
//			pagingPanel.setSpacing(3);
//		}
//	}
	
	public void checkAll(boolean value) {
		for (Widget w: actionMap.keySet())
			if (w instanceof CheckBox)
				((CheckBox)w).setValue(value);
	}
}
