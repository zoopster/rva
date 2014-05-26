// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.colorPicker;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class ColorPickerHistory extends Composite implements ClickHandler {
	String[] historyColors = new String[8];
    FlexTable table = new FlexTable();
    
    private ColorPickerPreview previewWidget = ColorPickerPreview.getInstance();
    
    public ColorPickerHistory() {       	
    	for (int i = 0; i < historyColors.length; i++) {
    		historyColors[i] = "rgb(255, 255, 255)";
    	}
    	
    	table.getElement().getStyle().setProperty("border", "1px solid #999");
    	table.setCellSpacing(3);
    	table.setCellPadding(0);
    	
        initWidget(table);
    }
    
    public void addColor(String newColor) {
    	int colorLocation = historyColors.length - 1;
    	// first check if color is not already in the list:
    	for (int i = historyColors.length - 1; i > 0; i--) {
    		if (historyColors[i].equals(newColor)) {
    			colorLocation = i;
    		}
    	}
    	
    	for (int i = colorLocation; i > 0; i--) {
    		historyColors[i] = historyColors[i - 1];
    	}
    	historyColors[0] = newColor;
    }
    
    public void onLoad() {
    	updateHistory();
    }
    
    private void updateHistory() {
    	int i = 0;
        for (String color : historyColors) {
        	HTML colorSwab = new HTML();
        	colorSwab.setWidth("20px");
        	colorSwab.setHeight("20px");

            DOM.setStyleAttribute(colorSwab.getElement(), "border", "1px solid #000");
//            DOM.setStyleAttribute(getElement(), "cursor", "default");
            DOM.setStyleAttribute(colorSwab.getElement(), "backgroundColor", color);
        	colorSwab.addClickHandler(this);
        	
        	table.setWidget(i / 2, i % 2, colorSwab);
        	table.getCellFormatter().getElement(i / 2, i % 2).getStyle().setProperty("backgroundImage", "url(/images/preview-opacity.png)");
        	
        	i++;
        }
    }

    public void onClick(ClickEvent event) {
        Widget sender = (Widget)event.getSource();
        previewWidget.setPreview(sender.getElement().getStyle().getBackgroundColor());  
    }
}