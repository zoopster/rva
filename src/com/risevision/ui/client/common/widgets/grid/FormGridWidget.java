package com.risevision.ui.client.common.widgets.grid;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;
import com.risevision.ui.client.common.widgets.TooltipLabelWidget;

public class FormGridWidget extends Grid {
	private int row = -1;
	private boolean isShort = false;

	public FormGridWidget(int rows, int columns, boolean isShort) {
		this(rows, columns);

		this.isShort = isShort;
	}
	
	public FormGridWidget(int rows, int columns) {
		super(rows, columns);
		
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
	
	public void addRow(String label, String tooltip, Widget widget) {
		addRow(label, tooltip, widget, null);
	}
	
	public void addRow(String label, String tooltip, Widget widget, String styleName) {
		TooltipLabelWidget tooltipWidget = new TooltipLabelWidget(label, tooltip);
		
		addRow(tooltipWidget, widget, styleName);
	}
	
//	public void gridAdd(String label, Widget widget, String styleName, Widget units) {
//		if (widget != null){
//			if (styleName != null)
//				widget.setStyleName(styleName);
//			
//			if (units == null)
//				addRow(label, widget);
//			else{
//				HorizontalPanel panel = new HorizontalPanel();
//				panel.add(widget);
//				panel.add(new SpacerWidget());
//				panel.add(units);
//				addRow(label, panel);
//			}
//		}
//	}
	
	public void addRow(Widget label, Widget widget, String styleName) {
		addRow(widget, styleName);

		setWidget(row, 0, label);
	}
	
	public void addRow(String label, Widget widget, String styleName) {
		addRow(widget, styleName);

		setText(row, 0, label);
	}
	
	private void addRow(Widget widget, String styleName) {
		row++;
		getCellFormatter().setStyleName(row, 0, isShort ? "rdn-ColumnShort" : "rdn-Column1");
		if (widget != null)
		{
			setWidget(row, 1, widget);
			if (styleName != null)
				widget.setStyleName(styleName);
		}
	}
	
	public void resizeGrid() {
		resizeRows(row + 1);
	}
	
	public int getRow() {
		return row;
	}
}
