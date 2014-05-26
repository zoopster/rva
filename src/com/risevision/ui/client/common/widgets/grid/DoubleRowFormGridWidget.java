// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.grid;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;
import com.risevision.ui.client.common.widgets.TooltipLabelWidget;

public class DoubleRowFormGridWidget extends Grid {
	private int row = -1;

	public DoubleRowFormGridWidget(int rows) {
		super(rows * 2, 1);
		
		styleControls();
	}

	private void styleControls() {
		// style the table
		setCellSpacing(0);
		setCellPadding(0);
		setStyleName("rdn-Table");
	}
	
	public void addRow(String label, Widget widget) {
		addRow(label, widget, null);
	}
	
	public void addRow(String label, String tooltip, Widget widget, String styleName) {
		TooltipLabelWidget tooltipWidget = new TooltipLabelWidget(label, tooltip);
		
		addRow(tooltipWidget, widget, styleName);
	}
	
	public void addRow(Widget label, Widget widget, String styleName) {
		row++;
		
		setWidget(row, 0, label);
		
		addField(widget, styleName);
	}
	
	public void addRow(String label, Widget widget, String styleName) {
		row++;
		
		setText(row, 0, label);

		addField(widget, styleName);
	}
	
	private void addField(Widget widget, String styleName) {
		row++;
//		getCellFormatter().setStyleName(row, 0, "rdn-Column1");
		if (widget != null)	{
			setWidget(row, 0, widget);

			if (styleName != null) {
				widget.setStyleName(styleName);
			}
			
			widget.addStyleName("rdn-Indent");
		}
	}
	
	public void resizeGrid() {
		resizeRows((row * 2) + 1);
	}
	
	public int getRow() {
		return row / 2;
	}
	
	public void setRowVisibility(int rowNumber, boolean visible) {
		getRowFormatter().setVisible(rowNumber * 2, visible);
		getRowFormatter().setVisible((rowNumber * 2) + 1, visible);
	}
	
	public boolean getRowVisibility(int rowNumber) {
		return getRowFormatter().isVisible(rowNumber * 2);
	}
	
}
