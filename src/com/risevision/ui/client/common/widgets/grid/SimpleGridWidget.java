// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.grid;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HTMLTable.ColumnFormatter;
import com.google.gwt.user.client.ui.Widget;

public class SimpleGridWidget extends VerticalPanel {

	private Grid headerTable;
	private FlexTable bodyFlexTable = new FlexTable();
	private String[][] header;
	
	public SimpleGridWidget(String[] header) {
		this.header = new String[2][header.length];
		
		for (int i = 0; i < header.length; i++) {
			this.header[0][i] = "";
			this.header[1][i] = header[i];
		}
	}
	
	public SimpleGridWidget(String[][] header) {
		this.header = header;
		
		headerTable = new Grid(1, header.length);

		add(headerTable);
		add(bodyFlexTable);

		styleControls();
		setHeader();
		
	}

	private void styleControls() {
		// style the table
		headerTable.setCellSpacing(0);
		headerTable.setCellPadding(0);
		headerTable.setStyleName("rdn-GridHeader");
		
		bodyFlexTable.setCellSpacing(0);
		bodyFlexTable.setCellPadding(0);
		bodyFlexTable.setStyleName("rdn-Grid");
	}
	
	private void setHeader() {
		int i = 0;
		for (String[] def : header) {

			if (def[0] == null || def[0].isEmpty())
				headerTable.setHTML(0, i, "&nbsp;");
			else
				headerTable.setText(0, i, def[0]);
					
			CellFormatter cellFormatter = headerTable.getCellFormatter();
			cellFormatter.setStyleName(0, i, "rdn-GridHeader");
			
			if (def.length > 1){
				ColumnFormatter colFormatter = headerTable.getColumnFormatter();
				colFormatter.setWidth(i, def[1]);
				// for firefox
				cellFormatter.setWidth(0, i, def[1]);
			}
			
			i++;
		}
	}
	
	public void setText(int row, int column, String text) {
		formatCell(row, column);
		bodyFlexTable.setText(row, column, text);
	}
	
	public void setHTML(int row, int column, String text) {
		formatCell(row, column);
		bodyFlexTable.setHTML(row, column, text);
	}
	
	public void setWidget(int row, int column, Widget widget) {
		formatCell(row, column);
		bodyFlexTable.setWidget(row, column, widget);
	}

	private void formatCell(int row, int column){
		CellFormatter cellFormatter = bodyFlexTable.getCellFormatter();
		
		String[] def = header[column];
		if (def != null && def.length > 1 && def[1] != null && !def[1].isEmpty()) {
			cellFormatter.setWidth(row, column, def[1]);
		}
	}

	public void clearGrid() {
		for (int i = bodyFlexTable.getRowCount() - 1; i >= 0; i--) {
			bodyFlexTable.removeRow(i);
		}
	}
	
	public FlexTable getFlexTable() {
		return bodyFlexTable;
	}
	
}
