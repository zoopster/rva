// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.grid;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.risevision.common.client.utils.RiseUtils;

public class ScrollingFlexTable extends VerticalPanel {

	private FlexTable headerFlexTable = new FlexTable();
	private FlexTable bodyFlexTable = new FlexTable();
	private ArrayList<Widget> headerWidgets = new ArrayList<Widget>();

	public ScrollingFlexTable() {
		super();

		add(headerFlexTable);
		add(bodyFlexTable);
	}

	public void setHeight(String height) {
//		DOM.setElementAttribute(getBodyElement(), "height", height);
	}

	public void setHeaderText(int column, String text, String width) {
		if (!RiseUtils.strIsNullOrEmpty(text)) {
			setHeaderWidget(column, new Label(text), width);
		}
		else {
			setHeaderWidget(column, new Label(""), width, "rdn-GridItemHeaderShort");
		}
	}

	public void setHeaderWidget(int column, Widget widget, String width) {
		setHeaderWidget(column, widget, width, "rdn-GridItemHeader");
	}
	
	private void setHeaderWidget(int column, Widget widget, String width, String styleName) {
		if (widget != null) {
			headerFlexTable.setWidget(0, column, widget);
			
			headerWidgets.add(column, widget);
		}
	}
	
	public Widget getHeaderWidget(int column) {
		return headerWidgets.get(column);		
	}
	
	public void setHeaderStyleName(String className) {
		headerFlexTable.addStyleName(className);
	}

}
